package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.CapitalAccount;
import com.xuanwu.mos.domain.entity.Department;
import com.xuanwu.mos.domain.entity.GsmsUser;
import com.xuanwu.mos.domain.entity.Role;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.User;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.UserType;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.UserMgrRepo;
import com.xuanwu.mos.service.RoleService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.SessionUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription
 * @Data 2017-4-12
 * @Version 1.0.0
 */
@Service
public class UserMgrService {

	@Autowired
	private UserMgrRepo userMgrRepo;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private CapitalAccountService capitalAccountService;

	@Autowired
	private ContactShareGroupService contactShareGroupService;

	@Autowired
	private ContactGroupService contactGroupService;

	public int countUser(QueryParameters params) {
		return userMgrRepo.findResultCount(params);
	}

	public List<User> listUsers(QueryParameters params) {
		return userMgrRepo.findResults(params);
	}

	public Department getDeptByEntId(int enterpriseId) {
		return userMgrRepo.getDeptByEntId(enterpriseId);
	}

	public Department getDeptIdePrefixByEntId(int enterpriseId) {
		return userMgrRepo.getDeptIdePrefixByEntId(enterpriseId);
	}

	public List<Department> getChildDepartments(String path) {
		return userMgrRepo.getChildDepartments(path);
	}
	
	public List<Department> getChildDepartmentsAll(String path) {
		return userMgrRepo.getChildDepartmentsAll(path);
	}
	public List<Department> autoCompleteDepartments(QueryParameters params) {
		return userMgrRepo.autoCompleteDepartments(params);
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean addDept(Department dept) throws RepositoryException {
		int insertId = userMgrRepo.addDepartment(dept);
		if (insertId > 0) {
			if (StringUtils.isBlank(dept.getIdentify())) {
				dept.setId(insertId);
				dept.setIdentify(dept.getDeptNoPrefix() + insertId);
				//因为user表的identify字段长度只有12，且不能随便改，所以如果”前缀+userId“的长度大于12，则不能成功入库 by jiangziyuan
				if ((dept.getDeptNoPrefix() + insertId).length() > 12) {
					return false;
				}
			}
			userMgrRepo.updateDepartment(dept);

			if (ListUtil.isNotBlank(dept.getBizTypes())) {
				userMgrRepo.addUserBusinessType(dept.getBizTypes(), insertId);
			}

			if (ListUtil.isNotBlank(dept.getRoles())) {
				userMgrRepo.addUserRole(dept.getRoles(), insertId);
			}

			//新增部门关联计费账户
			/*if (ListUtil.isNotBlank(dept.getCapitalAccounts())) {
				userMgrRepo.addUserAccountBind(dept.getCapitalAccounts());
			}*/
			return true;
		}
		return false;
	}

	/**
	 * 获取父部门的业务类型
	 * @param pDeptId 父部门ID
	 * @return
	 */
	public List<BizType> findBizTypesByDeptId(int pDeptId) {
		List<BizType> bizTypes = bizTypeService.findBizTypesByDeptId(pDeptId);
		//默认勾选最先创建的业务类型
		bizTypes.get(0).setBound(true);
		return bizTypes;
	}

	/**
	 * 获取本部门业务类型
	 * @param pDeptId 父部门ID
	 * @param deptId 本部门ID
	 * @return
	 */
	public List<BizType> findBizTypesByDeptId(int pDeptId, int enterpriseId, int deptId) {
		Department rootDept = getDeptByEntId(enterpriseId);
		List<BizType> pBizTypes = null;
		if (pDeptId == rootDept.getId()) {
			pBizTypes = bizTypeService.findBizTypeByEntId(enterpriseId);
		} else {
			pBizTypes = bizTypeService.findBizTypesByDeptId(pDeptId);
		}

		HashSet<Integer> tempBizTypes = new HashSet<>();
		List<BizType> bizTypes = bizTypeService.findBizTypesByDeptId(deptId);
		for (BizType bizType : bizTypes) {
			tempBizTypes.add(bizType.getId());
		}

		for (BizType pBizType : pBizTypes) {
			if (tempBizTypes.contains(pBizType.getId())) {
				pBizType.setBound(true);
			}
		}
		return pBizTypes;
	}

	/**
	 * 获取父部门角色
	 * @param pDeptId 父部门ID
	 * @return
	 */
	public List<Role> findRolesByDeptId(int pDeptId) {
		List<Role> roles = roleService.findRolesByDeptId(pDeptId, Platform.FRONTKIT);
		for (Role role : roles) {
			if (role.isDefault()) {
				role.setChecked(true);
			}
		}
		return roles;
	}

	/**
	 * 获取本部门角色
	 * @param pDeptId 父部门ID
	 * @param deptId 本部门ID
	 * @return
	 */
	public List<Role> findRolesByDeptId(int pDeptId, int deptId) {
		List<Role> pRoles = roleService.findRolesByDeptId(pDeptId, Platform.FRONTKIT);
		List<Role> roles = roleService.findRolesByDeptId(deptId, Platform.FRONTKIT);
		HashSet<Integer> tempRoles = new HashSet<>();
		for (Role role : roles) {
			tempRoles.add(role.getId());
		}

		for (Role pRole : pRoles) {
			if (tempRoles.contains(pRole.getId())) {
				pRole.setChecked(true);
			}
		}
		return pRoles;
	}

	/**
	 * 获取企业或部门所属计费子账户,用户管理新增部门或用户时使用
	 * @param deptId 部门ID
	 * @param type 用户类型
	 * @return
	 */
	public List<CapitalAccount> getCapitalAccounts(int deptId, UserType type) {
		int enterpriseId = 0;
		SimpleUser simpleUser = SessionUtil.getCurUser();
		//0:企业->查询部门的计费账户(顶级部门就查企业的计费账户，子级部门就查父级部门的计费账户),  1:部门查用户的所属计费账户
		if (UserType.ENTERPRISE.equals(type)) {
			GsmsUser user = userService.findUserById(deptId);
			if (StringUtils.isNotBlank(user.getPath())) {
				enterpriseId = deptId;
				type = UserType.DEPARTMENT; //转换成查询部门的计费账户
			} else {
				enterpriseId = simpleUser.getEnterpriseId();
			}

			if (deptId == simpleUser.getEnterpriseId()) {
				deptId = 0;
			}
		}
		return capitalAccountService.findCapitalAccountByType(enterpriseId, deptId, type);
	}

	/**
	 * 获取企业或部门所属计费子账户,用户管理修改部门或用户时使用
	 * @param deptId 部门ID
	 * @param type 用户类型
	 * @return
	 */
	public List<CapitalAccount> getUpdateCapitalAccounts(int deptId, UserType type) {
		int enterpriseId = 0;
		SimpleUser simpleUser = SessionUtil.getCurUser();
		//0:企业->查询部门的计费账户(顶级部门就查企业的计费账户，子级部门就查父级部门的计费账户),  1:部门查用户的所属计费账户
		if (UserType.ENTERPRISE.equals(type)) {
			Department department = (Department) userService.findUserById(deptId);
			if (StringUtils.isNotBlank(department.getPath())) {
				Department pDepartment = (Department) userService.
						findUserById(department.getParentId());
				if (StringUtils.isNotBlank(pDepartment.getPath())) {
					//转换成查询部门
					type = UserType.DEPARTMENT;
				} else {
					enterpriseId = pDepartment.getParentId();
				}
			} else {
				enterpriseId = simpleUser.getEnterpriseId();
			}

			if (deptId == simpleUser.getEnterpriseId()) {
				deptId = 0;
			}
		}
		return capitalAccountService.findCapitalAccountByType(enterpriseId, deptId, type);
	}

	public int getCountByParentId(Integer id) {
		return userMgrRepo.getCountByParentId(id);
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean delDept(Integer id) throws RepositoryException {
		int count = 0;
		count = userMgrRepo.delDept(id);
		if (count > 0) {
			userMgrRepo.delUserRoleByUserId(id);
			userMgrRepo.delUserBusinessByUserId(id);
			return true;
		}
		return false;
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean updateDept(Department dept) throws RepositoryException {
		if (dept != null) {
			int count = userService.updateDepartment(dept);
			if (count > 0) {
				List<Integer> userIds = userMgrRepo.
						getUserIdByPath(dept.getFullPath());
				List<Integer> removeBizIds = new ArrayList<>();
				List<Integer> removeRoleIds = new ArrayList<>();
				for (BizType type : dept.getBizTypes()) {
					if (!type.getBound()) {
						removeBizIds.add(type.getId());
					}
				}
				for (Role role : dept.getRoles()) {
					if (!role.isChecked()) {
						removeRoleIds.add(role.getId());
					}
				}
				//删除本部门与业务类型、角色绑定关系
				userMgrRepo.delUserBusinessByUserId(dept.getId());
				userMgrRepo.delUserRoleByUserId(dept.getId());
				//删除本部门以及本部门用户与子部门以及子部门用户与业务类型、角色绑定关系
				for (Integer userId : userIds) {
					if (removeBizIds.size() > 0) {
						userMgrRepo.delUserBusiness(removeBizIds, userId);
					}
					if (removeRoleIds.size() > 0) {
						userMgrRepo.delUserRoles(removeRoleIds, userId);
					}
				}
				userMgrRepo.addUserBusinessType(dept.getBizTypes(), dept.getId());
				userMgrRepo.addUserRole(dept.getRoles(), dept.getId());
				return true;
			}
		}
		return false;
	}

	public int listDeptInfoCount(QueryParameters params) {
		return userMgrRepo.listDeptInfoCount(params);
	}

	public List<Department> listDeptInfo(QueryParameters params) {
		return userMgrRepo.listDeptInfo(params);
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean addAccount(User user) throws RepositoryException {
		int userId = userService.addUser(user);
		if (userId > 0) {
			userService.addUserLogin(user);
			userService.addUserInfo(user);
			if (ListUtil.isNotBlank(user.getBizTypes())) {
				for (BizType bizType : user.getBizTypes()) {
					if (bizType.getId().equals(user.getCommonBizTypeId())) {
						//设置为用户默认业务类型
						bizType.setBusType(10);
						break;
					}
				}
				userMgrRepo.addUserBusinessType(user.getBizTypes(), userId);
			}

			if (ListUtil.isNotBlank(user.getRoles())) {
				userMgrRepo.addUserRole(user.getRoles(), userId);
			}

			//默认绑定企业计费总账户
			CapitalAccount entAccountInfo = capitalAccountService.
					findParentAccountInfo(user.getEnterpriseId(), null);
			capitalAccountService.bindEntAccount(entAccountInfo.getId(), userId);
			return true;
		}
		return false;
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean updateAccount(User user) throws RepositoryException {
		int count = userMgrRepo.updateUser(user);
		if (count > 0) {
			userService.updateUserInfo(user);
			userMgrRepo.delUserBusinessByUserId(user.getId());
			if (ListUtil.isNotBlank(user.getBizTypes())) {
				for (BizType bizType : user.getBizTypes()) {
					if (bizType.getId().equals(user.getCommonBizTypeId())) {
						bizType.setBusType(10);
						break;
					}
				}
				userMgrRepo.addUserBusinessType(user.getBizTypes(), user.getId());
			}

			userMgrRepo.delUserRoleByUserId(user.getId());
			if (ListUtil.isNotBlank(user.getRoles())) {
				userMgrRepo.addUserRole(user.getRoles(), user.getId());
			}
			return true;
		}
		return false;
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean delAccount(Integer[] ids) throws RepositoryException {
		userMgrRepo.delSelectUserBusiness(ids);
		userMgrRepo.delSelectUserRoles(ids);
		userMgrRepo.updateSelectUsers(ids);
		userMgrRepo.delUserInfo(ids);
		StringBuilder userIds = new StringBuilder();
		for (Integer id : ids) {
			userIds.append(id).append(Delimiters.COMMA);
		}
		String userIdsStr = userIds.deleteCharAt(userIds.length() - 1).toString();
		//删除联系人信息
		contactService.delByUserIds(userIdsStr);
		contactGroupService.delByUserIds(userIdsStr);
		contactShareGroupService.delByUserIds(userIdsStr);
		return true;
	}

	public User findAccountDetail(Integer userId, UserType userType) {
		return userMgrRepo.findUserDetail(userId, userType);
	}

	public List<User> findAllUserByEntId(int enterpriseId) {
		return userMgrRepo.findAllUserByEntId(enterpriseId);
	}


	public List<Department> findAllDeptByEntId(int enterpriseId) {
		return userMgrRepo.findAllDeptByEntId(enterpriseId);
	}

	public boolean isCheckedByChildDept(String path, String bizTypeId, String roleId) {
		if (StringUtils.isNotBlank(bizTypeId)) {
			List<BizType> bizTypes = bizTypeService.findChildTypeByPath(path);
			for (BizType bizType : bizTypes) {
				if (Integer.valueOf(bizTypeId).equals(bizType.getId())) {
					return true;
				}
			}
		}
		if (StringUtils.isNotBlank(roleId)) {
			List<Role> roles = roleService.findChildRoleByPath(path);
			for (Role role : roles) {
				if (Integer.valueOf(roleId).equals(role.getId())) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 根据dataScope来确定用户的query的参数
	*/
	public void convertQueryByDataScope(QueryParameters query, DataScope ds, SimpleUser user){
		query.addParam("enterpriseId",user.getEnterpriseId());
		switch (ds){
			case DEPARTMENT:
				// 获取当前用户所在部门,子部门，所有userId
				Integer deptId = user.getParentId();
				String path = userService.findPathById(deptId);
				path += deptId + Delimiters.DOT;
				QueryParameters p = new QueryParameters();
				p.addParam("showAllChild", true);
				p.addParam("path", path);
				p.addParam("platformId", Platform.FRONTKIT.getIndex());
				p.addParam("enterpriseId", user.getEnterpriseId());
				List<User> users = this.listUsers(p);
				List<Integer> userIds = new ArrayList<>();
				for (User u : users) {
					userIds.add(u.getId());
				}
				query.addParam("userIds", userIds);
				break;
			case PERSONAL:
				query.addParam("userId",user.getId());
				break;
			default:

		}
	}

	/**
	 * 根据页面传入部门ID，是否包含子部门，获取用户ID
	 * @param query
	 * @param reqt
	 * @param user
     */
	public void convertQueryByDept(QueryParameters query, PageReqt reqt, SimpleUser user) {
		if (null != reqt.getParams().get("deptId")) {
			QueryParameters p = new QueryParameters();
			String deptId = (String) reqt.getParams().get("deptId");
			p.addParam("parentId", reqt.getParams().get("deptId"));
			p.addParam("showAllChild", reqt.getParams().get("subDept")==null?false:reqt.getParams().get("subDept"));
			String path = (String) reqt.getParams().get("path");
			p.addParam("path", path + deptId + Delimiters.DOT);
			p.addParam("platformId", Platform.FRONTKIT.getIndex());
			p.addParam("enterpriseId", user.getEnterpriseId());
			List<User> users = this.listUsers(p);
			List<Integer> userIds = new ArrayList<>();
			for (User u : users) {
				userIds.add(u.getId());
			}
			if(userIds.size()>0){
				query.addParam("userIds", userIds);
			}
		}
	}

	public Department getDepartmentById(QueryParameters params){
		return userMgrRepo.getDepartmentById(params);
	}

	public List<Department> getDeptIncludeChildDept(String path, Integer parentId) {
		return userMgrRepo.getDeptIncludeChildDept(path, parentId);
	}
	public List<Department> getDeptIncludeChildDept4UserStat(String path, Integer parentId) {
		return userMgrRepo.getDeptIncludeChildDept4UserStat(path, parentId);
	}
	public String getDelAccountNames(Integer[] ids) {
		return userMgrRepo.getDelAccountNames(ids);
	}
	public List<Department> getAllChildDepartments(String path) {
		return userMgrRepo.getAllChildDepartments(path);
	}

	/**
	 * 查询某个企业，指定部门(包括子部门)的所有用户的id的集合
	 * @param parameters
	 * @return
	 */
	public List<Integer> findUserIdsByDept(QueryParameters parameters){
		List<User> results = userMgrRepo.findResults(parameters);
		List<Integer> userIds = new ArrayList<>();
		for (User user:results) {
			userIds.add(user.getId());
		}
		return userIds;
	}
}

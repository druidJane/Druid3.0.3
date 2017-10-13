package com.xuanwu.mos.rest.resource.sysmgmt;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.file.FileHeader;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.HeadDMapToF;
import com.xuanwu.mos.file.HeadInfo;
import com.xuanwu.mos.file.mapbean.UserMap;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.request.ImportRequest;
import com.xuanwu.mos.rest.service.BizTypeService;
import com.xuanwu.mos.rest.service.CapitalAccountService;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.service.RoleService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 用户管理
 * @Data 2017-4-12
 * @Version 1.0.0
 */
@Component
@Path(Keys.SYSTEMMGR_ACCOUNTMGR)
public class UserMgrResource {

	private static final Logger logger = LoggerFactory.getLogger(UserMgrResource.class);

	@Autowired
	private UserMgrService userMgrService;

	@Autowired
	private UserService userService;

	@Autowired
	private Config config;

	@Autowired
	private CapitalAccountService accountService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private FileImporter fileImporter;

	@Autowired
	private FileTaskExecutor taskExecutor;

	@Autowired
	private FileTaskService taskService;
	
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 通过部门来获取用户列表
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST)
	public PageResp listUsersByDept(@Valid PageReqt req) {
		SimpleUser user = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		params.addParam("platformId", config.getPlatformId());
		params.addParam("enterpriseId", user.getEnterpriseId());
		if (params.getParams().get("path") == null) {
			String resourceUrl = UrlResourceUtil.handleUrlResource(Keys.SYSTEMMGR_ACCOUNTMGR,
					Keys.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST);
			DataScope dataScope = SessionUtil.getDataSope(resourceUrl);
			if (DataScope.DEPARTMENT.equals(dataScope)) {
				params.addParam("path", user.getPath());
			}
		}
		int total = userMgrService.countUser(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo page = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(page);
		List<User> users = userMgrService.listUsers(params);
		return PageResp.success(total, users);
	}

	/**
	 * 部门查询
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_GETDEPTS)
	public PageResp listDeptInfo(@Valid PageReqt req) {
		SimpleUser user = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		String resourceUrl = UrlResourceUtil.handleUrlResource(Keys.SYSTEMMGR_ACCOUNTMGR,
				Keys.SYSTEMMGR_ACCOUNTMGR_GETDEPTS);
		DataScope dataScope = SessionUtil.getDataSope(resourceUrl);
		Department rootDept = userMgrService.getDeptByEntId(user.getEnterpriseId());
		String path = getPathByDataScope(dataScope, rootDept);
		params.addParam("path", path);
		int total = userMgrService.listDeptInfoCount(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo page = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(page);
		List<Department> departments = userMgrService.listDeptInfo(params);
		return PageResp.success(total, departments);
	}

	/**
	 * 新增部门预处理
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT_PRE)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp preAddDept(Department dept) {
		SimpleUser user = SessionUtil.getCurUser();
		//部门信息
		GsmsUser pDept = userService.findUserById(dept.getParentId());
		Department rootDept = userMgrService.getDeptByEntId(user.getEnterpriseId());
		int pId = dept.getParentId();
		List<BizType> bizTypes = null;
		if (pId == rootDept.getId()) {
			bizTypes = bizTypeService.findBizTypeByEntId(user.getEnterpriseId());
			//默认勾选最先创建的业务类型
			bizTypes.get(0).setBound(true);
		} else {
			bizTypes = userMgrService.findBizTypesByDeptId(pId);
		}
		//获取角色列表
		List<Role> roles = userMgrService.findRolesByDeptId(pId);

		pDept.setBizTypes(bizTypes);
		pDept.setRoles(roles);
		return JsonResp.success(pDept);
	}

	/**
	 * 新增部门
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp addDept(Department dept) throws RepositoryException {
		SimpleUser user = SessionUtil.getCurUser();
		dept.setEnterpriseId(user.getEnterpriseId());
		if (StringUtils.isNotBlank(dept.getPath())) {
			dept.setPath(dept.getPath() + dept.getParentId() + Delimiters.DOT);
		} else {
			dept.setPath(dept.getParentId() + Delimiters.DOT);
		}
		if (userService.isExistDeptName(dept)) {
			return JsonResp.fail("已存在该部门名称，请重新输入！");
		}

		if (userService.isExistIdentify(0, dept.getIdentify(),
				user.getEnterpriseId(), UserType.DEPARTMENT)) {
			return JsonResp.fail("已存在该部门编号，请重新输入！");
		}

		String[] tempArr = dept.getPath().split(Pattern.quote(Delimiters.DOT));
		if (tempArr.length > config.getMaxTreeDeep()) {
			return JsonResp.fail("系统最大只支持" + config.getMaxTreeDeep() + "级！");
		}
		//dept.setDeptNoPrefix(config.getDeptNoPrefix(Platform.BACKEND));
		//by jiangziyuan
		Department tempDept = userMgrService.getDeptIdePrefixByEntId(user.getEnterpriseId());
		if (null != tempDept && !"".equals(tempDept.getDeptNoPrefix()) && null != tempDept.getDeptNoPrefix()) {
			dept.setDeptNoPrefix(tempDept.getDeptNoPrefix());
		} else {
			dept.setDeptNoPrefix(config.getDeptNoPrefix(Platform.BACKEND));
		}

		dept.setCreateTime(new Date());
		dept.setPlatformId(config.getPlatformId());
		dept.setUserType(UserType.DEPARTMENT);
		dept.setState(UserState.NORMAL);
		if (userMgrService.addDept(dept)) {
			
			 sysLogService.addLog(user,OperationType.NEW,"【用户管理】/【用户部门名称】","Public","Login","【" + dept.getDeptName() + "】"); //添加访问日志
            
			return JsonResp.success();
		}
		return JsonResp.fail(Messages.SYSTEM_ERROR);
	}

	/**
	 * 删除部门
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_DELDEPARTMENT)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp delDept(Integer id) throws RepositoryException {
		int count = userMgrService.getCountByParentId(id);
		if (count > 0) {
			return JsonResp.fail("该部门下有子部门或用户，不能删除！");
		}

		if (userMgrService.delDept(id)) {
			SimpleUser user = SessionUtil.getCurUser();
			GsmsUser dep =  userService.findUserById(id);
			sysLogService.addLog(user,OperationType.DELETE,"【用户管理】/【用户部门名称】","Public","Login","【"+dep.getEnterpriseName()+"】"); //添加访问日志
	            
			return JsonResp.success();
		}
		return JsonResp.fail();
	}

	/**
	 * 修改部门预处理
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT_PRE)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp preUpdateDept(Department department) {
		//本部门信息
		GsmsUser user =  userService.findUserById(department.getId());
		if (user != null) {
			//父部门信息
			SimpleUser pDept = userService.findSimpleUserById(user.getParentId());
			List<BizType> bizTypes = userMgrService.findBizTypesByDeptId(user.getParentId(),
					user.getEnterpriseId(), department.getId());
			List<Role> roles = userMgrService.findRolesByDeptId(user.getParentId(), department.getId());
			user.setBizTypes(bizTypes);
			user.setRoles(roles);
			Map<String, Object> results = new HashMap<>();
			results.put("user", user);
			results.put("pDept", pDept);
			return JsonResp.success(results);
		}
		return JsonResp.fail();
	}

	/**
	 * 判断子部门是否勾选要修改部门的业务类型或角色
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT_CONFIRM)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp isChecked(Map<String, String> params) {
		String path = (String) params.get("path");
		String bizTypeId = (String) params.get("bizTypeId");
		String roleId = (String) params.get("roleId");
		boolean checked = userMgrService.isCheckedByChildDept(path, bizTypeId, roleId);
		return JsonResp.success(checked);
	}

	/**
	 * 修改部门
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp updateDept(Department dept) throws RepositoryException {
		if (userService.isExistDeptName(dept)) {
			return JsonResp.fail("已存在该部门名称，请重新输入！");
		}

		if (userMgrService.updateDept(dept)) {
			
			SimpleUser user = SessionUtil.getCurUser();
			 sysLogService.addLog(user,OperationType.MODIFY,"【用户管理】/【用户部门名称】","Public","Login","【"+dept.getDeptName()+"】"); //添加访问日志
	         
			return JsonResp.success();
		}
		return JsonResp.fail();
	}

	/**
	 * 新增用户预处理
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT_PRE)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp addAccountPre(User user) {
		Department department = (Department) userService.findUserById(user.getParentId());
		List<BizType> bizTypes = userMgrService.findBizTypesByDeptId(user.getParentId());
		List<Role> roles = userMgrService.findRolesByDeptId(user.getParentId());
		department.setRoles(roles);
		department.setBizTypes(bizTypes);
		return JsonResp.success(department);
	}

	/**
	 * 新增用户
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp addAccount(User user) throws Exception {
		SimpleUser simpleUser = SessionUtil.getCurUser();
		user.setEnterpriseId(simpleUser.getEnterpriseId());
		if (userService.isExistUserName(user)) {
			return JsonResp.fail("已存在该用户名，请重新输入！");
		}

		if (StringUtils.isNotBlank(user.getIdentify())) {
			if (userService.isExistIdentify(0,user.getIdentify(),
					simpleUser.getEnterpriseId(), UserType.PERSONAL)) {
				return JsonResp.fail("已存在该扩展码，请重新输入！");
			}
		}

		//判断所输入的电话号码是否附合需求——小灵通为10至12位，其余为11位 by jiangziyuan
		String tempPhoneTop = user.getPhone().substring(0, 1);
		String tempPhone = user.getPhone();
		if (tempPhoneTop.equals("0") && tempPhone.length() <= 10 && tempPhone.length() >= 12) {
			return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
		} else if(!tempPhoneTop.equals("0") && tempPhone.length() != 11) {
			return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
		}

		String secondPassword = RandomStringUtils.randomAlphanumeric(8);
		//登录密码
		user.setSecondPassword(Md5Utils.mixLoginPasswd(secondPassword, config.getMixKey()));
		user.setUserType(UserType.PERSONAL);
		user.setCreateTime(new Date());
		user.setPlatformId(Platform.FRONTKIT.getIndex());
		user.setUserTimeInterval(config.getUserTimeInterval());
		user.setUserSendNum(config.getUserSendNum());
		user.setContentSendnum(1);//对应moc后台——系统管理——发送限制——用户发送限制（tab页）——修改【同号同内容限制】，目前写死为“1”
		user.setUserContentInterval(config.getUserContentInterval());
		Enterprise enterprise = SessionUtil.getCurEnterprise();
		if (StringUtils.isBlank(enterprise.getSignature()) && StringUtils.isBlank(user.getSignature())) {
			user.setState(UserState.SUSPEND);
		} else {
			user.setState(UserState.NORMAL);
		}
		user.setPriority(10);
		if (user.getSignature() == null) {
			user.setSignature("");
		}
		String respInfo = "";
		if (user.getAccountType() == UserAccountType.OSPF) {
			//透传密码
			String midPassword = RandomStringUtils.randomAlphanumeric(8);
			user.setMidPassword(AuthSecurityUtil.encrypt(midPassword));
			respInfo = "初始化登录密码为：" + secondPassword + "，请登录后及时修改密码。<br/>" +
						"初始化透传密码为：" + midPassword + "，用于透传发送的密码。";
		} else {
			//发送密码
			String password = RandomStringUtils.randomAlphanumeric(8);
			user.setPassword(Md5Utils.mixLoginPasswd(password, config.getMixKey()));
			respInfo = "初始化登录密码为：" + secondPassword + "，请登录后及时修改密码。<br/>" +
					"初始化发送密码为：" + password + "，用于接口发送的密码。";
		}
		if (UserState.SUSPEND.equals(user.getState())) {
			respInfo += "<br/>新增子账号请联系客服审批启用（客服QQ：4001000566@qq.com 电话：4001000566）";
		}
		if (userMgrService.addAccount(user)) {
			sysLogService.addLog(simpleUser,OperationType.NEW,"【用户管理】/【用户账号】","Public","Login","【" + user.getUserName() + "】"); //添加访问日志
			return JsonResp.success(respInfo);
		}
		return JsonResp.fail(Messages.SYSTEM_ERROR);
	}

	/**
	 * 修改用户预处理
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT_PRE)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp updateAccountPre(User preUser) {
		User user = (User) userService.findUserById(preUser.getId());
		BizType bizType = bizTypeService.findCommonBusTypeByUserId(preUser.getId());
		if (bizType != null) {
			user.setCommonBizTypeId(bizType.getId());
		}
		SimpleUser pDept = userService.findSimpleUserById(user.getParentId());
		List<BizType> bizTypes = userMgrService.findBizTypesByDeptId(user.getParentId(),
				user.getEnterpriseId(), preUser.getId());
		List<Role> roles = userMgrService.
				findRolesByDeptId(user.getParentId(), preUser.getId());
		user.setBizTypes(bizTypes);
		user.setRoles(roles);
		Map<String, Object> results = new HashMap<>();
		results.put("user", user);
		results.put("pDept", pDept);
		return JsonResp.success(results);
	}

	/**
	 * 修改用户
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp updateAccount(User user) throws RepositoryException {
		SimpleUser simpleUser = SessionUtil.getCurUser();
		user.setEnterpriseId(simpleUser.getEnterpriseId());
		if (userService.isExistUserName(user)) {
			return JsonResp.fail("已存在该用户名，请重新输入！");
		}

		if (StringUtils.isNotBlank(user.getIdentify())) {
			if (userService.isExistIdentify(user.getId(), user.getIdentify(),
					simpleUser.getEnterpriseId(), UserType.PERSONAL)) {
				return JsonResp.fail("已存在该扩展码，请重新输入！");
			}
		}

		//判断所输入的电话号码是否附合需求——小灵通为10至12位，其余为11位 by jiangziyuan
		String tempPhoneTop = user.getPhone().substring(0, 1);
		String tempPhone = user.getPhone();
		if (tempPhoneTop.equals("0") && tempPhone.length() <= 10 && tempPhone.length() >= 12) {
			return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
		} else if(!tempPhoneTop.equals("0") && tempPhone.length() != 11) {
			return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
		}

		if (userMgrService.updateAccount(user)) {
			sysLogService.addLog(simpleUser,OperationType.MODIFY,"【用户管理】/【用户账号】","Public","Login","【"+user.getUserName()+"】"); //添加访问日志
			return JsonResp.success();
		}
		return JsonResp.fail(Messages.SYSTEM_ERROR);
	}

	/**
	 * 删除用户
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_DELETEUSER)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp delAccount(Integer[] ids) throws RepositoryException {
		String logContent = userMgrService.getDelAccountNames(ids);
		if (userMgrService.delAccount(ids)) {
			SimpleUser simpleUser = SessionUtil.getCurUser();
			sysLogService.addLog(simpleUser,OperationType.DELETE,"【用户管理】/【用户账号】",
					"Public","Login","【"+LogContentUtil.format(logContent)+"】"); //添加访问日志
			return JsonResp.success();
		}
		return JsonResp.fail();
	}

	/**
	 * 用户详情
	 */
	@GET
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_ACCOUNT_DETAIL)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp accountDetail(@QueryParam("userId") Integer userId) {
		User user = userMgrService.findAccountDetail(userId, UserType.PERSONAL);
		return JsonResp.success(user);
	}

	/**
	 * 上传
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_USERIMPORTING)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp upload(@Context HttpServletRequest request) {
		UploadResult result = fileImporter.upload(BizDataType.User, request);
		StatusCode statusCode = result.getStatusCode();
		if (statusCode != StatusCode.Success) {
			return JsonResp.fail(statusCode.getStateDesc());
		}
		return JsonResp.success(result);
	}

	/**
	 * 用户导入
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_USERIMPORTING)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp doImport(@Valid ImportRequest req) {
		try {
			if (!req.isCorrectFileHead()) {
				return JsonResp.fail(-1, Messages.INCORRECT_FILE_COLUMN);
			}
			SimpleUser user = SessionUtil.getCurUser();
			//构建文件映射列
			List<HeadDMapToF> headDMapToFList = req.getHeadDMapToFList();
			UserMap userMap = new UserMap();
			for (HeadDMapToF headDMapToF : headDMapToFList) {
				HeadInfo headInfo = headDMapToF.getDataHeadInfo();
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[0])) {
					userMap.setAccountType(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[1])) {
					userMap.setUserName(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[2])) {
					userMap.setLinkMan(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[3])) {
					userMap.setSendMsgPwd(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[4])) {
					userMap.setMidPwd(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[5])) {
					userMap.setLoginPwd(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[6])) {
					userMap.setPhone(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[7])) {
					userMap.setDeptIdentify(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[8])) {
					userMap.setDeptName(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[9])) {
					userMap.setUserIdentify(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[10])) {
					userMap.setSignature(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[11])) {
					userMap.setSigLocation(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[12])) {
					userMap.setBizIds(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[13])) {
					userMap.setProtocolType(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[14])) {
					userMap.setSrcPort(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[15])) {
					userMap.setCallbackAddress(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[16])) {
					userMap.setCustomerSignature(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[17])) {
					userMap.setSendSpeed(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[18])) {
					userMap.setLinkNum(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[19])) {
					userMap.setRoleNames(headInfo.getIndex());
				}
				/*if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[20])) {
					userMap.setRemark(headInfo.getIndex());
				}*/
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[20])) {
					userMap.setUpPush(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[21])) {
					userMap.setPushAddress(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[22])) {
					userMap.setStatusReportPush(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[23])) {
					userMap.setReportPushAddress(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.USER_MGR_FRONTKIT[24])) {
					userMap.setRemark(headInfo.getIndex());
				}
			}

			FileTask task = new FileTask();
			task.setFileName(req.getFileName());
			task.setTaskName("用户管理导入");
			task.setType(TaskType.Import);
			task.setDataType(BizDataType.User);
			task.setPostTime(new Date());
			task.setUserId(user.getId());
			task.setFileSize(req.getFileSize());
			task.setPlatformId(config.getPlatformId());

			String resourceUrl = UrlResourceUtil.handleUrlResource(Keys.SYSTEMMGR_ACCOUNTMGR,
					Keys.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST);
			DataScope dataScope = SessionUtil.getDataSope(resourceUrl);
			Department rootDept = userMgrService.
					getDeptByEntId(user.getEnterpriseId());
			String path = getPathByDataScope(dataScope, rootDept);
			Integer parentId = null;
			if (DataScope.NONE == dataScope || DataScope.GLOBAL == dataScope) {
				parentId = rootDept.getId();
			} else {
				parentId = user.getParentId();
			}
			Map<String, String> params = new HashMap<>();
			Enterprise enterprise = (Enterprise) userService.
					findUserById(user.getEnterpriseId());
			params.put("userId", String.valueOf(user.getId()));
			params.put("entId", String.valueOf(user.getEnterpriseId()));
			params.put("delimeter", req.getDelimiter());
			params.put("domain", enterprise.getDomain());
			params.put("path", path);
			params.put("parentId", String.valueOf(parentId));
			params.put("userMap", userMap.tran2Params());
			params.put("transparentSend", String.valueOf(enterprise.getTransparentSend()));//是否透传 by jiangziyuan

			task.setParamsMap(params);
			task.setHanldePercent(0);
			task.setState(TaskState.Wait);
			taskService.save(task);
			taskExecutor.putTask2Queue(task);
			
			SimpleUser simpleUser = SessionUtil.getCurUser();
			sysLogService.addLog(simpleUser,OperationType.IMPORT,"【用户管理】/【用户账号】","Public","Login",""); //添加访问日志
	        
		} catch (RepositoryException e) {
			logger.error("Import user failed：", e);
			JsonResp.fail(Messages.SYSTEM_ERROR);
		}
		return JsonResp.success();
	}

	@GET
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_USERIMPORTING)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getFileHeader() {
		List<HeadDMapToF> headDMapToFList = new ArrayList<>();
		for (int i = 0; i < FileHeader.USER_MGR_FRONTKIT.length; i++) {
			HeadDMapToF headDMapToF = new HeadDMapToF();
			headDMapToF.setDataHeadInfo(new HeadInfo(i, FileHeader.USER_MGR_FRONTKIT[i]));
			headDMapToFList.add(headDMapToF);
		}
		return JsonResp.success(headDMapToFList);
	}

	/**
	 * 根据用户权限范围来获取企业下部门树
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_GETDEPTS_DEPTTREE)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp getDeptTree() {
		SimpleUser user = SessionUtil.getCurUser();
		//获取企业根部门节点
		Department rootDept = userMgrService.
				getDeptByEntId(user.getEnterpriseId());
		String userListUrl = UrlResourceUtil.handleUrlResource(Keys.SYSTEMMGR_ACCOUNTMGR,
				Keys.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST);
		String deptListUrl = UrlResourceUtil.handleUrlResource(Keys.SYSTEMMGR_ACCOUNTMGR,
				Keys.SYSTEMMGR_ACCOUNTMGR_GETDEPTS);
		String resourceUrl = null;
		if (SessionUtil.hasPermission(userListUrl)) {
			resourceUrl = userListUrl;
		} else {
			resourceUrl = deptListUrl;
		}
		DataScope dataScope = SessionUtil.getDataSope(resourceUrl);
		String path = getPathByDataScope(dataScope, rootDept);
		Integer parentId = null;
		if (DataScope.NONE == dataScope || DataScope.GLOBAL == dataScope) {
			parentId = rootDept.getId();
		} else {
			parentId = user.getParentId();
		}
		List<Department> childDepartments = userMgrService.
				getDeptIncludeChildDept(path, parentId);
		for (Department dept : childDepartments) {
			if (dept.getId().equals(rootDept.getId())) {
				dept.setBaseDept(true);
				break;
			}
		}
		return JsonResp.success(childDepartments);
	}

	private String getPathByDataScope(DataScope dataScope, Department rootDept) {
		String path = null;
		SimpleUser user = SessionUtil.getCurUser();
		switch (dataScope) {
			case GLOBAL:
			case NONE:
				path = rootDept.getId() + Delimiters.DOT;
				break;
			case DEPARTMENT:
				path = userService.findPathById(user.getParentId()) + user.getParentId();
				break;
			default:
		}
		return path;
	}

	/**
	 * 导出
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ACCOUNTMGR_USEREXPORT)
	public JsonResp doExport(@Valid PageReqt req) {
		SimpleUser curUser = SessionUtil.getCurUser();
		try {
			String resourceUrl = UrlResourceUtil.handleUrlResource(Keys.SYSTEMMGR_ACCOUNTMGR,
					Keys.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST);
			DataScope dataScope = SessionUtil.getDataSope(resourceUrl);
			Map<String, Object> params = req.getParams();
			String fileName = (String) params.get("fileName");
			params.put("platformId", config.getPlatformId());
			params.put("enterpriseId", curUser.getEnterpriseId());
			params.put("dataScopeId", dataScope.getIndex());
			params.put("userParentId", curUser.getParentId());
			params.put("userPath", curUser.getPath());
			FileTask task = new FileTask();
			task.setFileName(fileName);
			task.setTaskName("用户管理导出");
			String taskName = (String) params.get("name");
			if (StringUtils.isNotBlank(taskName)) {
				task.setTaskName(task.getTaskName() + "(" + taskName + ")");
			}
			task.setType(TaskType.Export);
			task.setDataType(BizDataType.User);
			task.setPostTime(new Date());
			task.setUserId(curUser.getId());
			task.setHanldePercent(0);
			task.setState(TaskState.Wait);
			task.setPlatformId(config.getPlatformId());
			task.setParameters(XmlUtil.toXML(params));
			taskService.save(task);
			taskExecutor.putTask2Queue(task);
			sysLogService.addLog(curUser,OperationType.EXPORT,"【用户管理】/【用户账号】","Public","Login",""); //添加访问日志
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResp.fail();
		}
		return JsonResp.success();
	}
}

package com.xuanwu.module.service;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.Department;
import com.xuanwu.mos.domain.entity.DynamicParam;
import com.xuanwu.mos.domain.entity.Enterprise;
import com.xuanwu.mos.domain.entity.GsmsUser;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.User;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.domain.enums.UserType;
import com.xuanwu.mos.domain.repo.UserRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
	private final static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PlatformMode platformMode;

	@Autowired
	private Config config;

	/**
	 * 获取企业账号
	 * @param Userid
	 * @return
     */
	public Enterprise getEnterpriseUser(int Userid){
		return userRepo.getEnterpriseBaseInfoById(Userid);
	}

	public Enterprise getLoginEnt(int entId) {
		return userRepo.getLoginEnt(entId);
	}
	

	public SimpleUser getAccountInfo(int userId, int platform_id) {
		
		return userRepo.getAccountInfo(userId,platform_id);
	}
	public double getExsitBindAccountOfUser(int userId,int enterpriseId) {
		Double balance = 0.0;	
		if(userRepo.getExsitBindAccountOfUser(userId) > 0){
		   return balance = userRepo.getUserofAccountBalance(userId);
		}else{
		   return balance = userRepo.getEnterpriseAccountBalance(enterpriseId);
		}
	}
	public int updateAccInfo(Integer userId,String phone,String linkMan) throws RepositoryException {
		return userRepo.updateAccInfo(userId,phone,linkMan);
	}

	public String getTrustIps(int userId, Platform platform, String excludeIp) {
		return userRepo.getTrustIps(userId,platform,excludeIp);
	}

	public List<String> getEnterprisePhonesById(Integer id) {
		return userRepo.getEnterprisePhonesById(id);
	}

	public List<BizType> findBizByMsgType(int userId, int msgType) {
		return userRepo.findBizByMsgType(userId, msgType);
	}

	public List<BizType> findBizTypes(int id, int pId) {
		if (pId <= 0) {
			List<BizType> bizs = userRepo.findBizTypes(id);
			for (BizType biz : bizs) {
				biz.setName(StringUtil.replaceHtml(biz.getName()));
			}
			return bizs;
		}
		List<BizType> pBizs = userRepo.findBizTypes(pId);
		HashSet<Integer> bizSet = new HashSet<Integer>();
		List<BizType> bizs = userRepo.findBizTypes(id);
		for (BizType biz : bizs) {
			biz.setName(StringUtil.replaceHtml(biz.getName()));
			bizSet.add(biz.getId());
			biz.setBound(true);
		}

		for (BizType biz : pBizs) {
			biz.setName(StringUtil.replaceHtml(biz.getName()));
			if (bizSet.contains(biz.getId()))
				continue;
			bizs.add(biz);
		}
		return bizs;
	}



	public GsmsUser findUserById(int id) {
		if (id <= 0) {
			return null;
		}
		GsmsUser user = userRepo.findGsmsUser(id);
		if (user == null)
			return null;
		if (user instanceof Department) {
			Department dept = (Department) user;
			List<Integer> types = userRepo.findChildTypes(dept.getId());
			UserType userType;
			if (ListUtil.isNotBlank(types)) {
				for (int type : types) {
					userType = UserType.getType(type);
					if (userType == UserType.DEPARTMENT) {
						dept.setHasChild(true);
					} else if (userType == UserType.PERSONAL) {
						dept.setHasUser(true);
					}
				}
			}
		}
		return user;
	}

	public SimpleUser getLoginUser(String username, Platform platform) {
		return userRepo.getLoginUser(username, platform.getIndex());
	}

	public int  updatePasswd(Integer userId, String newPassword) throws RepositoryException {
		return userRepo.updatePassword(userId,newPassword);
	}

	public int findChargeUserCount(QueryParameters params) {
		return userRepo.findChargeUserCount(params);
	}

	public List<SimpleUser> findChargeUsers(QueryParameters params) {
		return userRepo.findChargeUsers(params);
	}

	public int findSelectorUserCount(QueryParameters params) {
		return userRepo.findSelectorUserCount(params);
	}

	public List<SimpleUser> findSelectorUsers(QueryParameters params) {
		return userRepo.findSelectorUsers(params);
	}

	public boolean isExistDeptName(Department department) {
		return userRepo.isExistDeptName(department) > 0;
	}

	public boolean isExistIdentify(int id, String identity, int enterpriseId, UserType userType) {
		return userRepo.isExistIdentify(id, identity, enterpriseId, userType) > 0;
	}

	public int updateDepartment(Department dept) throws RepositoryException {
		return userRepo.updateDepartment(dept);
	}

	public List<User> findSelfUsersSimple(int entId, String path, String name,
										  int maxSize) {
		if (org.apache.commons.lang.StringUtils.isBlank(path)) {
			path = null;
		}
		if (org.apache.commons.lang.StringUtils.isBlank(name)) {
			name = null;
		}
		if (maxSize <= 0) {
			return Collections.emptyList();
		}
		return userRepo.findSelfUsersSimple(entId, path, name, maxSize);
	}

	public List<User> findUsersSimple(int entId, String path, String name,
									  int maxSize) {
		if (org.apache.commons.lang.StringUtils.isBlank(path)) {
			path = null;
		}
		if (org.apache.commons.lang.StringUtils.isBlank(name)) {
			name = null;
		}
		if (maxSize <= 0) {
			return Collections.emptyList();
		}
		return userRepo.findUsersSimple(entId, path, name, maxSize);
	}

	public List<User> findUsersSimpleNotDel(int entId, String path,
											String name, int maxSize) {
		if (org.apache.commons.lang.StringUtils.isBlank(path)) {
			path = null;
		}
		if (org.apache.commons.lang.StringUtils.isBlank(name)) {
			name = null;
		}
		if (maxSize <= 0) {
			return Collections.emptyList();
		}
		return userRepo.findUsersSimpleNotDel(entId, path, name, maxSize);
	}

	/**
	 * 查找企业有发送短信权限的用户ID
	 *
	 * @param enterpriseId
	 * @return
	 */
	public List<Integer> findSendSmsPermitId(int enterpriseId){
		return userRepo.findSendSmsPermitId(enterpriseId);
	}

	public List<GsmsUser> findSimpleUsers(GsmsUser user, DynamicParam param,
										  UserState[] states) {
		switch (user.getType()) {
			case PERSONAL:
				return userRepo.findUsersSimple(user, param, states);
			case ENTERPRISE:
				return userRepo.findEntsSimple(user, param, states);
			default:
				break;
		}
		return Collections.emptyList();
	}

	public String findPathById(int id) {
		return userRepo.findPathById(id);
	}

	public boolean isExistUserName(User user) {
		return userRepo.existUserName(user) > 0;
	}
	public User findByName(int enterpriseId, String userName, boolean includeDel){
		return  userRepo.findUserByName(enterpriseId, userName, includeDel);
	}
	public int addUser(User user) throws RepositoryException {
		return userRepo.addUser(user);
	}

	public User findEnterpriseByName(String enterpriseName) {
		return userRepo.findEnterpriseByName(enterpriseName);
	}

	public void updateParamsConfig(int entId, Enterprise enterprise) throws RepositoryException {
		userRepo.updateEnterprise(entId, enterprise);
		userRepo.updateMoUser(enterprise.getDefaultMoUserId(), entId);
	}

	public int addUserInfo(User user) throws RepositoryException {
		return userRepo.addUserInfo(user);
	}

	public boolean resetSendPwd(User user) throws RepositoryException {
		return userRepo.resetSendPwd(user) > 0;
	}

	public boolean resetMidPwd(User user) throws RepositoryException {
		return userRepo.resetMidPwd(user) > 0;
	}

	public boolean resetLoginPwd(User user) throws RepositoryException {
		return userRepo.resetLoginPwd(user) > 0;
	}

	public int updateUserInfo(User user) throws RepositoryException {
		return userRepo.updateUserInfo(user);
	}

	public SimpleUser findSimpleUserById(int id) {
		if (id <= 0) {
			return null;
		}
		return userRepo.findSimpleUserById(id);
	}

	public int addUserLogin(User user) throws RepositoryException {
		return userRepo.addUserLogin(user);
	}

	public List<SimpleUser> autoCompleteUserName(QueryParameters params){
		return userRepo.autoCompleteUserName(params);
	}
		/**
	 * 根据UserId查部门名称
	 * @param userId
	 * @return
	 */
	public SimpleUser findDeptNameById(int userId){
		return userRepo.findDeptNameById(userId);
	}
}

/*   
* Copyright (c) 2016/8/23 by XuanWu Wireless Technology Co., Ltd 
*             All rights reserved  
*/
package com.xuanwu.mos.domain.repo;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.domain.enums.UserType;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;

/**
 * @author <a href="mailto:jiangpeng@wxchina.com">Peng.Jiang</a>
 * @version 1.0.0
 * @date 2016/8/23
 */
@Repository
public class UserRepo extends GsmsMybatisEntityRepository<User> {

	private static final Logger logger = LoggerFactory.getLogger(UserRepo.class);

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.UserMapper";
	}

	public Enterprise getEnterpriseBaseInfoById(Integer enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("enterpriseId", enterpriseId);
			return session.selectOne(fullSqlId("getEnterpriseBaseInfoById"), params);
		}
	}


	public SimpleUser getLoginUser(String username, Integer platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("username", username);
			params.put("platformId", platformId);
			return session.selectOne(fullSqlId("getLoginUser"), params);
		}
	}

	public SimpleUser getAccountInfo(Integer userId, Integer platform_id) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map param = new HashMap<String,Integer>();
			param.put("userId",userId);
			param.put("platform_id",platform_id);
			return session.selectOne(fullSqlId("getAccountInfo"), param);
		}
	}
	public int getExsitBindAccountOfUser(Integer userId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map param = new HashMap<String,Integer>();
			param.put("userId",userId);
			return session.selectOne(fullSqlId("getExsitBindAccountOfUser"), param);
		}
	}
	public double getUserofAccountBalance(Integer userId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map param = new HashMap<String,Integer>();
			param.put("userId",userId);
			return session.selectOne(fullSqlId("getUserofAccountBalance"), param);
		}
	}
	public double getEnterpriseAccountBalance(Integer enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map param = new HashMap<String,Integer>();
			param.put("enterpriseId",enterpriseId);
			return session.selectOne(fullSqlId("getEnterpriseAccountBalance"), param);
		}
	}
	
	public int updateAccInfo(Integer userId,String phone,String linkMan) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map params = new HashMap<String,Object>();
			params.put("userId",userId);
			params.put("phone",phone);
			params.put("linkMan",linkMan);
			count = session.update(fullSqlId("updateAccInfo"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("updateAccInfo failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int updateAccPasswd(Integer userId, String newPasswd) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map params = new HashMap<String,Object>();
			params.put("userId",userId);
			params.put("newPasswd",newPasswd);
			count = session.update(fullSqlId("updateAccPasswd"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("updateAccPasswd failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public Enterprise getLoginEnt(Integer entId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("entId", entId);
			return session.selectOne(fullSqlId("getLoginEnt"), params);
		}
	}

	/**
	 * 更新用户的登录时间
	 *
	 * @param id
	 * @return
	 * @throws RepositoryException
	 */
	public int updateLastLoginTime(int id, boolean selfAdd) {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			params.put("lastLoginTime", new Date());
			params.put("selfAdd", selfAdd);
			count = session.update(fullSqlId("updateLastLoginTime"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("UpdateLastLoginTime failed: ", e);
			//throw new RepositoryException(e);
		}
		return count;
	}

	public boolean updateForCertificate(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int ret = session.update(fullSqlId("updateForCertificate"), user);
			session.commit(true);
			return (ret > 0);
		} catch (Exception e) {
			logger.error("update for certificate failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public boolean updateRegAddress(Integer enterpriseId, String regAddress, Integer regionId)
			throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("enterpriseId", enterpriseId);
			params.put("regAddress", regAddress);
			params.put("regionId", regionId);
			params.put("updateTime", new Date());
			int ret = session.update(fullSqlId("updateRegAddress"), params);
			session.commit(true);
			return (ret > 0);
		} catch (Exception e) {
			logger.error("UpdateRegAddress failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public String getTrustIps(Integer entId, Platform platform, String excludeIp) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("entId", entId);
			params.put("platformId",platform.getIndex());
			params.put("excludeIp",excludeIp);
			return session.selectOne(fullSqlId("getTrustIps"), params);
		}
	}

	public List<String> getEnterprisePhonesById(Integer entId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("getEnterprisePhonesById"), entId);
		}
	}

	public int updatePassword(Integer id, String newPassword) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("id", id);
			params.put("newPassword", newPassword);
			count = session.update(fullSqlId("updatePassword"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("update password failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public void modifyPassword(Integer id, String newPassword, String secondPassword, String oldPassword)
			throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("id", id);
			params.put("newPassword", newPassword);
			params.put("secondPassword", secondPassword);
			params.put("oldPassword", oldPassword);
			session.update(fullSqlId("modifyPassword"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("update password failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public SimpleUser findEntByDomain(String domain) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findEntByDomain"), domain);
		}
	}



	public List<Map<String, String>> autoCompleteDomain(String domain) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("domain", domain);
			return session.selectList(fullSqlId("autoCompleteDomain"), params);
		}
	}
//  TODO DELETE ME END

	public User findEnterpriseAdmin(Integer enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findEnterpriseAdmin"), enterpriseId);
		}
	}

	public int initEnterpriseDomain(Integer enterpriseId, String domain) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("enterpriseId", enterpriseId);
			params.put("domain", domain);
			params.put("userName", "admin@" + domain);
			session.update(fullSqlId("updateAdminUserName"), params);
			return session.update(fullSqlId("updateEnterpriseDomain"), params);
		}
	}

	public List<User> findEnterpriseUsers(Integer enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findEnterpriseUsers"), enterpriseId);
		}
	}

	public int updateState(int id, UserState state) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			params.put("state", state);
			count = session.update(fullSqlId("updateState"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("UpdateState failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}




	public List<SimpleUser> autoCompleteUserName(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("autoCompleteUserName"), params);
		}
	}
	public List<SimpleUser> autoCompleteUserName4UserStat(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("autoCompleteUserName4UserStat"), params);
		}
	}
	public List<Map<String, String>> autoCompleteLinkMan(String linkMan, int platform) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("linkMan", linkMan);
			params.put("platform", Platform.getType(platform));
			return session.selectList(fullSqlId("autoCompleteInfo"), params);
		}
	}



	//by jiangziyuan
	public List<BizType> findBizByMsgType(int userId, int msgType) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			params.put("msgType", msgType);
			return session.selectList(fullSqlId("findBizByMsgType"), params);
		}
	}

	public List<BizType> findBizTypes(int userId) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			return session.selectList(fullSqlId("findBizTypes"), userId);
		} finally {
			session.close();
		}
	}

	public GsmsUser findGsmsUser(int id) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			GsmsUser gsmsUser = session.selectOne(fullSqlId("findGsmsUser"), id);
			return gsmsUser;
		} finally {
			session.close();
		}
	}

	public List<Integer> findChildTypes(int parentId) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			return session.selectList(fullSqlId("findChildTypes"), parentId);
		} finally {
			session.close();
		}
	}

	public String findDeptName(int deptId) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			return session.selectOne(fullSqlId("findDeptName"), deptId);
		} finally {
			session.close();
		}
	}

	public int findChargeUserCount(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findChargeUserCount"), params);
		}
	}

	public List<SimpleUser> findChargeUsers(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findChargeUsers"), params);
		}
	}

	public int findSelectorUserCount(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findSelectorUserCount"), params);
		}
	}

	public List<SimpleUser> findSelectorUsers(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findSelectorUsers"), params);
		}
	}

	public int isExistDeptName(Department department) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("isExistDeptName"), department);
		}
	}

	public int isExistIdentify(int id, String identity, int enterpriseId, UserType userType) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			params.put("identity", identity);
			params.put("enterpriseId", enterpriseId);
			params.put("userType", userType);
			return session.selectOne(fullSqlId("isExistIdentify"), params);
		}
	}

	public int updateDepartment(Department dept) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.update(fullSqlId("updateDepartment"), dept);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update department failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public User findUserByName(int enterpriseId, String userName, boolean includeDel) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("enterpriseId", enterpriseId);
			params.put("userName", userName);
			params.put("includeDel", includeDel);
			return session.selectOne(fullSqlId("findUserByName"), params);
		}
	}

	public int addUser(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.insert(fullSqlId("addUser"), user);
			session.commit(true);
			return user.getId();
		} catch (Exception e) {
			logger.error("Add user failed: ", e);
			throw new RepositoryException(e);
		}
	}
	public User findEnterpriseByName(String enterpriseName){
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findEnterpriseByName"), enterpriseName);
		}

	}

	//by jiangziyuan
	public String findUserNameById(int id) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findUserNameById"), id);
		}
	}

	public List<GsmsUser> findAllEntDepts(int entId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllEntDepts"), entId);
		}
	}

	public List<GsmsUser> findAllEntDeptsIncludeDel(int entId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllEntDeptsIncludeDel"), entId);
		}
	}

	public List<User> findSelfUsersSimple(int entId, String path, String name, int maxSize) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("entId", entId);
			params.put("path", path);
			params.put("name", name);
			params.put("maxSize", maxSize);
			return session.selectList(fullSqlId("findUsersSelfSimple"), params);
		}
	}

	public List<User> findUsersSimple(int entId, String path, String name, int maxSize) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("entId", entId);
			params.put("path", path);
			params.put("name", name);
			params.put("maxSize", maxSize);
			return session.selectList(fullSqlId("findUsersSimple"), params);
		}
	}

	public List<User> findUsersSimpleNotDel(int entId, String path, String name, int maxSize) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("entId", entId);
			params.put("path", path);
			params.put("name", name);
			params.put("maxSize", maxSize);
			return session.selectList(fullSqlId("findUsersSimpleNotDel"), params);
		}
	}

	public List<Integer> findSendSmsPermitId(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findSendSmsPermitId"), enterpriseId);
		}
	}

	public List<GsmsUser> findEntsSimple(GsmsUser user, DynamicParam param,
										 UserState[] states) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("user", user);
			params.put("param", param);
			params.put("states", states);
			return session.selectList(fullSqlId("findSimpleEnts"), params);
		}
	}

	public List<GsmsUser> findUsersSimple(GsmsUser user, DynamicParam param,
										  UserState[] states) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("user", user);
			params.put("param", param);
			params.put("states", states);
			return session.selectList(fullSqlId("findSimpleUsers"), params);
		}
	}

	public String findPathById(int id) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findPathById"), id);
		}
	}

	public int updateEnterprise(int entId, Enterprise enterprise) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("entId", entId);
			params.put("enterprise", enterprise);
			int count = session.update(fullSqlId("updateEnterprise"), params);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Update enterprise failed :" + e);
			throw new RepositoryException(e);
		}
	}

	public int updateMoUser(int defaultMoUserId, int entId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("entId", entId);
			params.put("defaultMoUserId", defaultMoUserId);
			int count = session.update(fullSqlId("updateMoUser"), params);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Update moUser failed :" + e);
			throw new RepositoryException(e);
		}
	}

	public int addUserInfo(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count = session.insert(fullSqlId("addUserInfo"), user);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Insert userInfo failed :" + e);
			throw new RepositoryException(e);
		}
	}

	public int resetMidPwd(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count = session.update(fullSqlId("resetMidPwd"), user);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Reset userInfo password failed :" + e);
			throw new RepositoryException(e);
		}
	}

	public int resetSendPwd(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count = session.update(fullSqlId("resetSendPwd"), user);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Reset user password failed :" + e);
			throw new RepositoryException(e);
		}
	}

	public int resetLoginPwd(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count = session.update(fullSqlId("resetLoginPwd"), user);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Reset user loginPassword failed :" + e);
			throw new RepositoryException(e);
		}
	}

	public int updateUserInfo(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count = session.update(fullSqlId("updateUserInfo"), user);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Update userInfo failed :" + e);
			throw new RepositoryException(e);
		}
	}

	public SimpleUser findSimpleUserById(int id) {
		try(SqlSession session = sqlSessionFactory.openSession()){
			return session.selectOne(fullSqlId("findSimpleUserById"),id);
		}
	}

	public int existUserName(User user) {
		try(SqlSession session = sqlSessionFactory.openSession()){
			return session.selectOne(fullSqlId("existUserName"),user);
		}
	}

	public int addUserLogin(User user) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count = session.insert(fullSqlId("addUserLogin"), user);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Add user login failed :" + e);
			throw new RepositoryException(e);
		}
	}
	
	public SimpleUser findDeptNameById(int userId){
		try(SqlSession session = sqlSessionFactory.openSession()){
			return session.selectOne(fullSqlId("findDeptNameById"),userId);
		}
	}
}

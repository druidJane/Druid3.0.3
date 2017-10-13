package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.UserType;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription
 * @Data 2017-4-12
 * @Version 1.0.0
 */
@Repository
public class UserMgrRepo extends GsmsMybatisEntityRepository<User> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.UserMgrMapper";
	}

	public Department getDeptByEntId(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getDeptByEntId"), enterpriseId);
		}
	}

	public Department getDeptIdePrefixByEntId(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getDeptIdePrefixByEntId"), enterpriseId);
		}
	}

	public List<Department> getChildDepartments(String path) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("getChildDepartments"), path);
		}
	}
	public List<Department> getAllChildDepartments(String path) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("getAllChildDepartments"), path);
		}
	}
	public List<Department> getChildDepartmentsAll(String path) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("getChildDepartmentsAll"), path);
		}
	}

	public List<Department> autoCompleteDepartments(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("autoCompleteDepartments"), params);
		}
	}

	public int addDepartment(Department dept) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.insert(fullSqlId("addDepartment"), dept);
			session.commit(true);
			return dept.getId();
		} catch (Exception e) {
			logger.error("Insert department failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public void updateDepartment(Department dept) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.update(fullSqlId("updateDepartment"), dept);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update department failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public void addUserBusinessType(List<BizType> bizTypes, Integer userId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			for (BizType bizType : bizTypes) {
				if (bizType.getBound()) {
					bizType.setUserId(userId);
					session.insert(fullSqlId("addUserBusinessType"), bizType);
				}
			}
			session.commit(true);
		} catch (Exception e) {
			logger.error("Add userBusinessType failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public void addUserRole(List<Role> roles, Integer userId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			for (Role role : roles) {
				if (role.isChecked()) {
					role.setUserId(userId);
					session.insert(fullSqlId("addUserRole"), role);
				}
			}
			session.commit(true);
		} catch (Exception e) {
			logger.error("Add userRole failed: ", e);
			throw new RepositoryException(e);
		}
	}

	/*public void addUserAccountBind(List<CapitalAccount> capitalAccounts) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			for (CapitalAccount account : capitalAccounts) {
				session.insert(fullSqlId("addUserAccountBind"), account);
			}
			session.commit(true);
		} catch (Exception e) {
			logger.error("Add userAccountBind failed: ", e);
			throw new RepositoryException(e);
		}
	}*/

	public int getCountByParentId(Integer id) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getCountByParentId"), id);
		}
	}

	public int delDept(Integer id) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.update(fullSqlId("delDept"), id);
			session.commit(true);
			return count;
		} catch (Exception e) {
			logger.error("Delete department failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public int delUserBusinessByUserId(Integer userId) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.delete(fullSqlId("delUserBusinessByUserId"), userId);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete user business type failed: " + e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int delUserRoleByUserId(Integer userId) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.delete(fullSqlId("delUserRoleByUserId"), userId);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete user role failed: " + e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int delUserAccountBind(Integer userId) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.delete(fullSqlId("delUserAccountBind"), userId);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete user bind account failed: " + e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int listDeptInfoCount(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("listDeptInfoCount"), params);
		}
	}

	public List<Department> listDeptInfo(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("listDeptInfo"), params);
		}
	}

	public int updateUser(User user) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.update(fullSqlId("updateUser"), user);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update user failed: " + e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public void delSelectUserBusiness(Integer[] userIds) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.delete(fullSqlId("delSelectUserBusiness"), userIds);
			session.commit(true);
		} catch (Exception e) {
			logger.error("delete selected user business failed: " + e);
			throw new RepositoryException(e);
		}
	}

	public void delSelectUserRoles(Integer[] userIds) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.delete(fullSqlId("delSelectUserRoles"), userIds);
			session.commit(true);
		} catch (Exception e) {
			logger.error("delete selected user roles failed: " + e);
			throw new RepositoryException(e);
		}
	}

	public void updateSelectUsers(Integer[] userIds) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.update(fullSqlId("updateSelectUsers"), userIds);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update selected users failed: " + e);
			throw new RepositoryException(e);
		}
	}

	public List<ContactGroup> findContactGroupByUserId(String userIds) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findContactGroupByUserId"), userIds);
		}
	}

	public void delContactByGroupId(int id) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.delete(fullSqlId("delContactByGroupId"), id);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete contact failed: " + e);
			throw new RepositoryException(e);
		}
	}

	public void delContactGroupById(Integer id) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.delete(fullSqlId("delContactGroupById"), id);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete contact group failed: " + e);
			throw new RepositoryException(e);
		}
	}

	public User findUserDetail(Integer userId, UserType userType) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			params.put("userType", userType);
			return session.selectOne(fullSqlId("findUserDetail"), params);
		}
	}

	public List<User> findAllUserByEntId(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllUserByEntId"), enterpriseId);
		}
	}

	public List<Department> findAllDeptByEntId(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllDeptByEntId"), enterpriseId);
		}
	}

	public void delUserInfo(Integer[] userIds) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.delete(fullSqlId("delUserInfo"), userIds);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete userInfo failed: " + e);
			throw new RepositoryException(e);
		}
	}

	//region 获取当前用户的直属部门
	public Department getDepartmentById(QueryParameters params){
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getDepartmentById"),params);
		}
	}

	public List<Department> getDeptIncludeChildDept(String path, Integer parentId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("path", path);
			params.put("parentId", parentId);
			return session.selectList(fullSqlId("getDeptIncludeChildDept"),params);
		}
	}
	public List<Department> getDeptIncludeChildDept4UserStat(String path, Integer parentId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("path", path);
			params.put("parentId", parentId);
			return session.selectList(fullSqlId("getDeptIncludeChildDept4UserStat"),params);
		}
	}
	public String getDelAccountNames(Integer[] ids) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getDelAccountNames"),ids);
		}
	}

	public int delUserBusiness(List<Integer> bizIds, Integer userId) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("bizIds", bizIds);
			params.put("userId", userId);
			count = session.delete(fullSqlId("delUserBusiness"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete user business type failed: " + e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int delUserRoles(List<Integer> roleIds, Integer userId) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("roleIds", roleIds);
			params.put("userId", userId);
			count = session.delete(fullSqlId("delUserRoles"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete user role failed: " + e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public List<Integer> getUserIdByPath(String path) {
		try(SqlSession session = sqlSessionFactory.openSession()){
			return session.selectList(fullSqlId("getUserIdByPath"), path);
		}
	}
}

/*
 * Copyright (c) 2016年10月08日 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.repo;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @version 1.0.0
 * @Description
 * @date 2016-10-8
 */
@Repository
public class RoleRepo extends GsmsMybatisEntityRepository<Role> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.RoleMapper";
	}

	public List<Permission> findPermissionsByRoleId(int roleId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("roleId", roleId);
			return session.selectList(fullSqlId("findPermissionsByRoleId"), map);
		}
	}

	public int updateUserRoleByEnt(int oldRoleId, int newRoleId, int entId) throws RepositoryException {
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("oldRoleId", oldRoleId);
			map.put("newRoleId", newRoleId);
			map.put("entId", entId);
			ret = session.update(fullSqlId("updateUserRoleByEnt"), map);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update user role by ent failed: ", e);
			throw new RepositoryException(e);
		}
		return ret;
	}

	public List<Permission> findPermissionsByPlatform(Integer platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("platformId", platformId);
			return session.selectList(fullSqlId("findPermissionsByPlatform"), platformId);
		}
	}

	public int cloneRolePermission(int oldRoleId, int newRoleId) throws RepositoryException {
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("oldRoleId", oldRoleId);
			map.put("newRoleId", newRoleId);
			ret = session.update(fullSqlId("cloneRolePermission"), map);
			session.commit(true);
		} catch (Exception e) {
			logger.error("clone role permission failed: ", e);
			throw new RepositoryException(e);
		}
		return ret;
	}

	public int deleteRolePermissionsByRoleId(int roleId) throws RepositoryException {
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("roleId", roleId);
			ret = session.delete(fullSqlId("deleteRolePermissionsByRoleId"), map);
			session.commit(true);
		} catch (Exception e) {
			logger.error("delete role permission failed: ", e);
			throw new RepositoryException(e);
		}
		return ret;
	}

	public Role findRoleByName(Role role) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findRoleByName"), role);
		}
	}

	public Role findDefaultRole(Platform platform) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findDefaultRole"), platform);
		}
	}

	public boolean addRolePermission(RolePermission rolePermission) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int ret = session.insert(fullSqlId("addRolePermission"), rolePermission);
			session.commit(true);
			return ret > 0;
		} catch (Exception e) {
			logger.error("Save/Update Entity failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public int addUserRole(Integer userId, Integer roleId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("roleId", roleId);
			int ret = session.insert(fullSqlId("addUserRole"), map);
			session.commit(true);
			return ret;
		} catch (Exception e) {
			logger.error("Add Role for User failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public int delUserRole(Integer userId, Integer roleId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("roleId", roleId);
			int ret = session.delete(fullSqlId("delUserRole"), map);
			session.commit(true);
			return ret;
		} catch (Exception e) {
			logger.error("Delete Role for User failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public int deleteUserRoleByRoleId(Integer roleId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("roleId", roleId);
			int ret = session.delete(fullSqlId("deleteUserRoleByRoleId"), map);
			session.commit(true);
			return ret;
		} catch (Exception e) {
			logger.error("Delete UserRole failed: ", e);
			throw new RepositoryException(e);
		}
	}

	public List<PlatformEntity> findAllPlatform() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllPlatform"));
		}
	}

	public List<Permission> findDataScopeByUserId(Integer id) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findDataScopeByUserId"), id);
		}
	}

	public List<Permission> findPermissionsForUser(Integer userId, Integer platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("platformId", platformId);
			return session.selectList(fullSqlId("findPermsByUserId"), map);
		}
	}

	public Map<Integer, Permission> findAllPermissionMap(Integer platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("platformId", platformId);
			return session.selectMap(fullSqlId("findAllPermissionMap"), map, "id");
		}
	}

	public List<Role> findRoles(Map<String, Object> params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findRoles"), params);
		}
	}

	public List<Role> findRolesByDeptId(Map<String, Object> params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findRolesByDeptId"), params);
		}
	}

	//by jiangziyuan
	public List<RolePermission> findAllRolePermissions() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllRolePermissions"));
		}
	}

	public int findGsmsSyncVersion(int type) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<Object, Object> params = new HashMap<>();
			params.put("type", type);
			return session.selectOne(fullSqlId("findGsmsSyncVersion"), params);
		}
	}

	public List<ConfigRecord> findAllConfigs() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllConfigs"));
		}
	}

	public int updateConfig(ConfigRecord config) throws RepositoryException {
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("config", config);
			ret = session.update(fullSqlId("updateConfig"), map);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update config failed: ", e);
			throw new RepositoryException(e);
		}
		return ret;
	}

	public List<Region> findAllRegions() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllRegions"));
		}
	}

	public List<Permission> findAllPermissions(int platformId) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			return session.selectList(fullSqlId("findAllPermissions"), platformId);
		} finally {
			session.close();
		}
	}

	public List<Permission> findMenuPermissions(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findMenuPermissions"), params);
		}
	}

	public List<Role> findChildRoleByPath(String path) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findChildRoleByPath"), path);
		}
	}

	public int saveUserRole(Role role) throws RepositoryException {
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ret = session.insert(fullSqlId("saveUserRole"), role);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Save user role failed: ", e);
			throw new RepositoryException(e);
		}
		return ret;
	}

	public String getDelRoleName(Integer[] ids) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getDelRoleName"), ids);
		}
	}
}

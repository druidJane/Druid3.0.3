/*
 * Copyright (c) 2016年10月08日 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.service;


import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.domain.entity.Role;
import com.xuanwu.mos.domain.entity.RolePermission;
import com.xuanwu.mos.domain.repo.RoleRepo;
import com.xuanwu.mos.dto.NavMenuDto;
import com.xuanwu.mos.dto.PermDto;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.PermissionUtil;
import com.xuanwu.msggate.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

/**
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @version 1.0.0
 * @Description
 * @date 2016-10-8
 */
@Component
public class RoleService {

	private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

	private static final String COLON = " : ";

	public static final String CH_COMMA = "，";

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PlatformMode platformMode;

	public List<Permission> getUserPermissions(int userId, Platform platform) {
		List<Permission> permissionList = roleRepo.findPermissionsForUser(userId,platform.getIndex());
		return permissionList;
	}

	public Map<Integer,Permission> findAllPermissionMap(Platform platform){
		Map<Integer,Permission> permissionMap = roleRepo.findAllPermissionMap(platform.getIndex());
		return permissionMap;
	}

	public List<Role> findBaseRoles(Platform platform) {
		Role role = new Role();
		QueryParameters params = new QueryParameters();
		role.setEnterpriseId(0);
		role.setUserId(0);
		role.setDefault(true);
		role.setPlatformId(platform.getIndex());
		return findRoles(role, params);
	}

	public Map<String, List<?>> getMenusAndBtns(Integer userId, Platform platform) {
		Map<String, List<?>> resultMap = new HashMap<>();
		try {
			List<Permission> permissionList = getUserPermissions(userId, platform);
			Map<Integer,Permission> allPermissionMap = findAllPermissionMap(platformMode.getPlatform());
			List<String> btnList = new ArrayList<>();
			Map<String,Boolean> btnMap = new HashMap<>();
			List<NavMenuDto> menuList = new ArrayList<>();
			Map<Integer, NavMenuDto> navMap = new HashMap<>();
			Map<Integer, List<NavMenuDto>> parentspMap = new HashMap<>();
			for (Permission outPermission : permissionList) {
				Permission permission = outPermission;
				do{
					String url = permission.getPermURL();
					if (permission.isDisplay() == true && !StringUtil.isBlank(url) && btnMap.get(url) == null) {
						btnList.add(url);
						btnMap.put(url,true);
					}
					boolean isMenu = permission.isMenu();
					if (!isMenu) {
						continue;
					}
					Integer parentId = permission.getParentId();
					NavMenuDto menu = NavMenuDto.copyFromSysPermission(permission);
					if (null != navMap.get(menu.getId())) {
						continue;
					}
					navMap.put(menu.getId(), menu);
					if (parentId == null || parentId == 0) {
						menuList.add(menu);
					} else {
						List<NavMenuDto> list = parentspMap.get(parentId);
						if (null == list) {
							list = new ArrayList<>();
							parentspMap.put(parentId, list);
						}
						list.add(menu);
					}
				} while ((permission = allPermissionMap.get(permission.getParentId())) != null);
			}
			setSubMenu(menuList, parentspMap);
			Collections.sort(menuList, NavMenuDto.MENUDTO_COMPARATOR);
			for (NavMenuDto menuDto : menuList) {
				menuDto.sort();
			}
			Collections.sort(btnList);
			resultMap.put("menuList", menuList);
			resultMap.put("btnList", btnList);
		} catch (Exception e) {
			LOG.error("get user menu failed by : ", e);
		}
		return resultMap;
	}

	/**
	 * 查询角色列表行数
	 */

	public int findRolesCount(Role role, QueryParameters param) {
		param.addParam("role", role);
		return roleRepo.findResultCount(param);
	}

	/**
	 * 查询角色列表
	 */
	public List<Role> findRoles(Role role, QueryParameters param) {
		param.addParam("role", role);
		return roleRepo.findResults(param);
	}



	/**
	 * 获取角色
	 */

	public Role findRoleById(int id) throws Exception {
		Role role = roleRepo.getById(id, 0);
		if (null != role) {
			role.setPermissions(roleRepo.findPermissionsByRoleId((Integer) role.getId()));
		}
		return role;
	}

	/**
	 * 获取所有权限
	 */

	public List<PermDto> findPermissionsByPlatform(Integer platformId) throws Exception {
		List<Permission> perms = roleRepo.findPermissionsByPlatform(platformId);
		if (ListUtil.isBlank(perms)) {
			return Collections.emptyList();
		}
		Role defaultRole = roleRepo.findDefaultRole(Platform.FRONTKIT);
		String[] defaultPermIds = defaultRole.getPermissionIds().split(",");
		for (Permission perm : perms) {
			if (perm.getId() == 11320 || perm.getId() == 17320) {
				perm.setDisplayName("发送记录");
			} else if (perm.getId() == 11330 || perm.getId() == 17330) {
				perm.setDisplayName("发送详情");
			}
			if (Arrays.binarySearch(defaultPermIds, String.valueOf(perm.getId())) > -1) {
				perm.setBase(true);
			}
		}
		return PermissionUtil.sortPermissions(perms);
	}

	public List<Permission> findPermissionsByRoleId(int roleId) {
		return roleRepo.findPermissionsByRoleId(roleId);
	}

	/**
	 * 新增角色
	 */
	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean addRole(Role role) throws RepositoryException {
		role = roleRepo.save(role);
		roleRepo.saveUserRole(role);
		if (((Integer) role.getId() > 0) && ListUtil.isNotBlank(role.getPermissions())) {
			for (Permission perm : role.getPermissions()) {
				RolePermission rolePermission = new RolePermission();
				rolePermission.setRoleId((Integer) role.getId());
				rolePermission.setPermissionId((Integer) perm.getId());
				rolePermission.setDataScope(perm.getDataScope());
				roleRepo.addRolePermission(rolePermission);
			}
		}
		return ((Integer) role.getId() > 0);
	}

	public boolean updateUserRoleByEnt(int oldRoleId, int newRoleId, int entId) throws RepositoryException {
		int ret = roleRepo.updateUserRoleByEnt(oldRoleId, newRoleId, entId);
		return (ret > 0);
	}

	public boolean cloneRolePermission(int oldRoleId, int newRoleId) throws RepositoryException {
		int ret = roleRepo.cloneRolePermission(oldRoleId, newRoleId);
		return (ret > 0);
	}

	/**
	 * 修改角色
	 */
	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean updateRole(Role role) throws RepositoryException {
		int result = roleRepo.deleteRolePermissionsByRoleId(role.getId());
		if (result > 0) {
			if ((role.getId() > 0) && ListUtil.isNotBlank(role.getPermissions())) {
				for (Permission perm : role.getPermissions()) {
					RolePermission rolePermission = new RolePermission();
					rolePermission.setRoleId(role.getId());
					rolePermission.setPermissionId(perm.getId());
					rolePermission.setDataScope(perm.getDataScope());
					roleRepo.addRolePermission(rolePermission);
				}
			}
		}
		int ret = roleRepo.update(role);
		return (ret > 0);
	}

	/**
	 * 删除角色
	 */
	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public int deleteRole(int id) throws RepositoryException {
		int ret = roleRepo.deleteById(id, 0);
		roleRepo.deleteUserRoleByRoleId(id);
		roleRepo.deleteRolePermissionsByRoleId(id);
		return ret;
	}



	/**
	 * 取消用户角色
	 */
	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public int delUserRole(Integer[] ids, Integer roleId) throws RepositoryException {
		int count = 0;
		for (int userId : ids) {
			count += roleRepo.delUserRole(userId, roleId);
		}
		return count;
	}

	/**
	 * 判断角色名称是否已经存在
	 */
	public boolean isExists(Role role) {
		return roleRepo.findRoleByName(role) != null;
	}

	public Role findDefaultRole(Platform platform) {
		return roleRepo.findDefaultRole(platform);
	}

	public List<Role> findRoles(int enterpriseId, Platform platform) {
		Map<String, Object> params = new HashMap<>();
		params.put("enterpriseId", enterpriseId);
		params.put("platform", platform);
		return roleRepo.findRoles(params);
	}

	public List<Role> findRolesByDeptId(int deptId, Platform platform) {
		Map<String, Object> params = new HashMap<>();
		params.put("deptId", deptId);
		params.put("platform", platform);
		return roleRepo.findRolesByDeptId(params);
	}

	public List<Role> findChildRoleByPath(String path) {
		return roleRepo.findChildRoleByPath(path);
	}

	public String getDelRoleName(Integer[] ids) {
		return roleRepo.getDelRoleName(ids);
	}


	/**
	 * 权限角色组合
	 */
	private static class RermissionRoleComp {
		private Role role;
		private Permission perm;

		public RermissionRoleComp(Role role, Permission perm) {
			this.role = role;
			this.perm = perm;
		}

		public Role getRole() {
			return role;
		}

		public Permission getPerm() {
			return perm;
		}

	}

	private boolean isReput(Permission cur, int curRoleUserId, RermissionRoleComp old) {
		Role oldRole = old.getRole();
		int oldRoleUserId = oldRole.getUserId();
		// 注意，判断顺序不可随意调整
		if (oldRoleUserId == curRoleUserId || (curRoleUserId > 0 && oldRoleUserId > 0)) {
			return cur.getDataScope().getIndex() > old.getPerm().getDataScope().getIndex();
		} else if (oldRoleUserId == 0) {
			return true;
		}
		return false;
	}

	private void setSubMenu(List<NavMenuDto> menuList, Map<Integer, List<NavMenuDto>> parentspMap) {
		for (NavMenuDto menuDto : menuList) {
			List<NavMenuDto> list = parentspMap.get(menuDto.getId());
			if (null != list) {
				// 递归设置子菜单
				setSubMenu(list, parentspMap);
				menuDto.setLists(list);
			}
		}
	}

	public int findRolesCount(QueryParameters params) {
		return roleRepo.findResultCount(params);
	}

	public List<Role> findRoles(QueryParameters params) {
		List<Role> roles = roleRepo.findResults(params);
		List<Permission> allPermissions = roleRepo.findAllPermissions(Platform.FRONTKIT.getIndex());
		for (Role role : roles) {
			List<Permission> ownPermissions = role.getPermissions();
			StringBuilder perBuilder = new StringBuilder();
			for (Permission permission : ownPermissions) {
				if (permission.getLevel() == 2) {
					recursiveSetMenuName(allPermissions, perBuilder, permission.getParentId());
					//递归拼接权限字符串
					recursivePermission(perBuilder, permission.getId(),permission.getDisplayName(), ownPermissions);
				}
			}
			if (perBuilder.toString().endsWith(CH_COMMA)) {
				role.setPermissionNames(perBuilder.deleteCharAt(perBuilder.length() - 1).toString());
			} else {
				role.setPermissionNames(perBuilder.toString());
			}
		}
		return roles;
	}

	private void recursiveSetMenuName(List<Permission> permissions, StringBuilder builder, int parentId) {
		Permission parentPermission = findParentPermission(permissions, parentId);
		if (parentPermission != null) {
			if (parentPermission.getParentId() != 0) {
				recursiveSetMenuName(permissions, builder, parentPermission.getParentId());
			} else {
				if (!builder.toString().contains("【" + parentPermission.getDisplayName() + "】" + COLON)) {
					builder.append("【" + parentPermission.getDisplayName() + "】").append(COLON);
				}
			}
		}
	}

	private Permission findParentPermission(List<Permission> permissions, int parentId) {
		Permission permission = null;
		for (Permission p : permissions) {
			if (p.getId() == parentId) {
				permission = p;
				break;
			}
		}
		return permission;
	}

	private void recursivePermission(StringBuilder builder, Integer id, String name,
									 List<Permission> permissions) {
		List<Permission> childPer = findChildPer(permissions, id);
		if (!builder.toString().endsWith(name)) {
			builder.append(name);
			if (childPer.size() == 0) {
				builder.append(CH_COMMA);
			}
		}
		if (childPer.size() > 0) {
			builder.append("->");
		}

		for (Permission permission : childPer) {
			builder.append(permission.getDisplayName());
			recursivePermission(builder, permission.getId(),
					permission.getDisplayName(), permissions);
			if (!builder.toString().endsWith(CH_COMMA)) {
				builder.append(CH_COMMA);
			}
		}
	}

	private List<Permission> findChildPer(List<Permission> permissions, int id) {
		List<Permission> childPermissions = new ArrayList<>();
		for (Permission permission : permissions) {
			if (permission.getParentId() == id) {
				childPermissions.add(permission);
			}
		}
		return childPermissions;
	}

	public Role findRoleById(Integer id) {
		Role role = roleRepo.getById(id, null);
		//补全父级权限
		Map<Integer, Permission> rolePermMap = new HashMap<>();
		Map<Integer, Permission> allPermMap = findAllPermissionMap(Platform.FRONTKIT);
		List<Permission> permissions = role.getPermissions();
		Map<Integer, Permission> parentPerMap = new HashMap<>();
		for (Permission permission : permissions) {
			if (permission.getId() == 11320 || permission.getId() == 17320) {
				permission.setDisplayName("发送记录");
			} else if (permission.getId() == 11330 || permission.getId() == 17330) {
				permission.setDisplayName("发送详情");
			}
			rolePermMap.put(permission.getId(), permission);
		}
		for (Permission permission : permissions) {
			if (permission.getParentId() != 0) {
				putParentPermMap(permission, rolePermMap, parentPerMap, allPermMap);
			}
		}
		for (Integer parentPermId : parentPerMap.keySet()) {
			//如果二级目录的权限display为false则将二级目录权限的子权限的parentId指向一级目录的id
			if (!parentPerMap.get(parentPermId).isDisplay()) {
				for (Permission p : permissions) {
					if (p.getParentId() == parentPermId) {
						p.setParentId(parentPerMap.get(parentPermId).getParentId());
					}
				}
			} else {
				permissions.add(parentPerMap.get(parentPermId));
			}
		}
		return role;
	}

	private void putParentPermMap(Permission permission,
								  Map<Integer, Permission> rolePermMap,
								  Map<Integer, Permission> parentPerMap,
								  Map<Integer, Permission> allPermMap) {
		if (rolePermMap.get(permission.getParentId()) == null) {
			Permission parentPerm = allPermMap.get(permission.getParentId());
			if (parentPerm != null) {
				if (parentPerMap.get(parentPerm.getId()) == null) {
					parentPerMap.put(parentPerm.getId(), parentPerm);
				}
				if (parentPerm.getParentId() != 0) {
					putParentPermMap(parentPerm, rolePermMap, parentPerMap, allPermMap);
				}
			}
		}
	}

}

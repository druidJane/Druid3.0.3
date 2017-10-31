package com.xuanwu.auth.service;

import com.xuanwu.auth.repo.RoleRepo;
import com.xuanwu.auth.repo.UserRepo;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.GsmsUser;
import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.DataScope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ShiroService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	public SimpleUser getLoginUser(String username, Platform platform) {
		return userRepo.getLoginUser(username, platform.getIndex());
	}

	public int updateLastLoginTime(int id,boolean errTimeSelfAdd) {
		return userRepo.updateLastLoginTime(id,errTimeSelfAdd);
	}

	public GsmsUser getUserById(int userId) {
		return userRepo.getById(userId, null);
	}

	/**
	 * 通过用户id获取所有权限的数据范围
	 */
	public HashMap<Integer, DataScope> getDataScopes(Integer id) {
		HashMap<Integer, DataScope> dataScopes = new HashMap<>();
		List<Permission> permissions = roleRepo.findDataScopeByUserId(id);
		for (Permission p : permissions) {
			dataScopes.put((Integer) p.getId(), p.getDataScope());
		}
		return dataScopes;
	}

	public List<Permission> getUserPermissions(int userId, Platform platform) {
		List<Permission> permissionList = roleRepo.findPermissionsForUser(userId,platform.getIndex());
		return permissionList;
	}

}

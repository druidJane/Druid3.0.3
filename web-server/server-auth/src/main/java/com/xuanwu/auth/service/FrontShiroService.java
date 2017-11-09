package com.xuanwu.auth.service;

import com.xuanwu.auth.repo.RoleRepo;
import com.xuanwu.auth.repo.UserRepo;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.config.ShiroRealmService;
import com.xuanwu.mos.domain.entity.GsmsUser;
import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.exception.AccountLockdException;
import com.xuanwu.mos.exception.IncorrectCredentialsTimesException;
import com.xuanwu.mos.exception.PasswdIsNullException;
import com.xuanwu.mos.utils.Constants;
import com.xuanwu.mos.utils.SessionUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangz
 */
@Service
public class FrontShiroService extends ShiroRealmService {

	private static final Logger LOG = LoggerFactory.getLogger(FrontShiroService.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private RoleService roleService;
	private static final Platform PLATFORM = Platform.FRONTKIT;

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

	@Override
	public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		String passwd = String.valueOf(upToken.getPassword());

		SimpleUser user = this.getLoginUser(username, PLATFORM);
		if (user == null) {
			throw new UnknownAccountException("Can't find user by name:" + username);
		}

		if (StringUtils.isBlank(user.getSecondPassword())) {
			throw new PasswdIsNullException("first login in need to change passwd");
		}

		if (user.getLoginErrorTimes() >= Constants.PASSWD_ALLOW_ERROR_TIMES) {
			//被锁定后距离上次登录时间没有超过15分钟，或者超过了15分钟，但是密码仍然错误
			if (System.currentTimeMillis() <= Constants.PASSWD_ERR_LOCK_TIME + user.getLastLoginTime().getTime() || !passwd.equals(user.getSecondPassword())) {
				this.updateLastLoginTime(user.getId(),true);
				throw new AccountLockdException("has more than 3 times passwd err");
			}
		} else {
			if (!passwd.equals(user.getSecondPassword())) {
				this.updateLastLoginTime(user.getId(),true);
				throw new IncorrectCredentialsTimesException(2-user.getLoginErrorTimes(),"passwd err");
			}
		}

		this.updateLastLoginTime(user.getId(),false);


		if (user.getState() != UserState.NORMAL) {
			throw new DisabledAccountException("The user is locked by name:" + username);
		}

		SessionUtil.setCurUser(user);

		AuthenticationInfo info = new SimpleAuthenticationInfo(username, user.getSecondPassword().toCharArray(), getName());
		return info;
	}

	@Override
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}
		SimpleUser user = SessionUtil.getCurUser();
		if (null == user) {
			LOG.error("can't find the user in session");
			String username = (String) getAvailablePrincipal(principals);
			user = this.getLoginUser(username, PLATFORM);
		}
		if (null == user) {
			LOG.error("can't find the user in db and session");
		}
		Set<String> permissions = new HashSet<String>();
		HashMap<String, DataScope> dataScopes = new HashMap<>();
		permissions.add(Constants.FILE_TASK);
		permissions.add(Constants.COMMON);
		try {
			Map<Integer,Permission> allPermissionMap = roleService.findAllPermissionMap(PLATFORM);
			List<Permission> permissionList = this.getUserPermissions(user.getId(), PLATFORM);
			//根据用户-角色-权限获取该用户的datascope,以这个为准
			for (Permission permission : permissionList) {
				DataScope pre = dataScopes.get(permission.getPermURL());
				if (pre == null || pre != null && pre.getIndex() < permission.getDataScope().getIndex()) {
					dataScopes.put(permission.getPermURL(),permission.getDataScope());
				}
			}
			//获取权限，并且补全datascope
			for (Permission outPermission : permissionList) {
				Permission permission = outPermission;
				do {
					if (StringUtils.isNotBlank(permission.getPermURL())) {
						DataScope pre = dataScopes.get(permission.getPermURL());
						//如果个人用户的datascope不存在则补全
						if (pre == null) {
							dataScopes.put(permission.getPermURL(),permission.getDataScope());
						}
						permissions.add(permission.getPermURL());
					}
				} while ((permission = allPermissionMap.get(permission.getParentId())) != null);
			}
		} catch (Exception e) {
			LOG.error("Authorization error: ", e);
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permissions);
		// 设置数据范围
		SessionUtil.setDataSope(dataScopes);
		return info;
	}
}

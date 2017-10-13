/*
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.security;

import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.exception.AccountLockdException;
import com.xuanwu.mos.exception.IncorrectCredentialsTimesException;
import com.xuanwu.mos.exception.PasswdIsNullException;
import com.xuanwu.mos.service.RoleService;
import com.xuanwu.mos.service.ShiroService;
import com.xuanwu.mos.utils.Constants;
import com.xuanwu.mos.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 用户登录和权限领域验证
 *
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @version 1.0.0
 * @date 2016-08-24
 */
public class ShiroRealm extends AuthorizingRealm {

	private static final Logger LOG = LoggerFactory.getLogger(ShiroRealm.class);

	// warning: 不能注入其它service，否则会导致该service的事务注解无效!!!

	@Autowired
	private ShiroService shiroService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PlatformMode platformMode;

	public ShiroRealm() {
		// 授权缓存
		String authorizationCacheName = "mos_authorizationCache";
		Map<Object, AuthorizationInfo> authorizationMap = new HashMap<Object, AuthorizationInfo>();
		Cache<Object, AuthorizationInfo> authorizationCache = new MapCache<Object, AuthorizationInfo>(
				authorizationCacheName, authorizationMap);
		super.setAuthorizationCache(authorizationCache);
		super.setAuthorizationCacheName(authorizationCacheName);
	}

	/**
	 * 身份验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		String passwd = String.valueOf(upToken.getPassword());

		SimpleUser user = shiroService.getLoginUser(username, platformMode.getPlatform());
		if (user == null) {
			throw new UnknownAccountException("Can't find user by name:" + username);
		}

		if (StringUtils.isBlank(user.getSecondPassword())) {
            throw new PasswdIsNullException("first login in need to change passwd");
		}

		if (user.getLoginErrorTimes() >= Constants.PASSWD_ALLOW_ERROR_TIMES) {
			//被锁定后距离上次登录时间没有超过15分钟，或者超过了15分钟，但是密码仍然错误
			if (System.currentTimeMillis() <= Constants.PASSWD_ERR_LOCK_TIME + user.getLastLoginTime().getTime() || !passwd.equals(user.getSecondPassword())) {
				shiroService.updateLastLoginTime(user.getId(),true);
				throw new AccountLockdException("has more than 3 times passwd err");
			}
		} else {
			if (!passwd.equals(user.getSecondPassword())) {
				shiroService.updateLastLoginTime(user.getId(),true);
				throw new IncorrectCredentialsTimesException(2-user.getLoginErrorTimes(),"passwd err");
			}
		}

		shiroService.updateLastLoginTime(user.getId(),false);


		if (user.getState() != UserState.NORMAL) {
			throw new DisabledAccountException("The user is locked by name:" + username);
		}

		SessionUtil.setCurUser(user);

		AuthenticationInfo info = new SimpleAuthenticationInfo(username, user.getSecondPassword().toCharArray(), getName());
		return info;
	}


	/**
	 * 权限验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}
		SimpleUser user = SessionUtil.getCurUser();
		if (null == user) {
			LOG.error("can't find the user in session");
			String username = (String) getAvailablePrincipal(principals);
			user = shiroService.getLoginUser(username, platformMode.getPlatform());
		}
		if (null == user) {
			LOG.error("can't find the user in db and session");
		}
		Set<String> permissions = new HashSet<String>();
		HashMap<String, DataScope> dataScopes = new HashMap<>();
		permissions.add(Constants.FILE_TASK);
		permissions.add(Constants.COMMON);
		try {
			Map<Integer,Permission> allPermissionMap = roleService.findAllPermissionMap(platformMode.getPlatform());
			List<Permission> permissionList = shiroService.getUserPermissions(user.getId(), platformMode.getPlatform());
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



	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		return SecurityUtils.getSubject().getSession().getId();
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		AuthorizationInfo info = getAuthorizationInfo(principals);
		Collection<String> perms = info.getStringPermissions();
		if (perms != null && !perms.isEmpty()) {
			for (String perm : perms) {
				if (permission.startsWith(perm)) {
					return true;
				}
			}
		}
		return false;
	}


}

/*
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.security;

import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.config.ShiroRealmService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.xuanwu.mos.config.Platform.FRONTKIT;

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
	private ShiroRealmService shiroRealmService;
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
		if(FRONTKIT.getIndex() == platformMode.getPlatform().getIndex()){
			return shiroRealmService.doGetAuthenticationInfo(token);
		}else{
			return null;
		}
	}


	/**
	 * 权限验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if(FRONTKIT.getIndex() == platformMode.getPlatform().getIndex()){
			return shiroRealmService.doGetAuthorizationInfo(principals);
		}else{
			return null;
		}
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

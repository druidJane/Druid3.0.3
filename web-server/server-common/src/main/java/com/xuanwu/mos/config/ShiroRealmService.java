package com.xuanwu.mos.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author by zhangz on 2017/11/9.
 */
public abstract class ShiroRealmService extends AuthorizingRealm {
    @Override
    public abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token);

    @Override
    public abstract AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals);

}

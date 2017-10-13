package com.xuanwu.mos.security;

import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.security.filter.ShiroAnonymousFilter;
import com.xuanwu.mos.security.filter.ShiroAuthorizationFilter;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

/**
 * 用户登录和权限验证框架配置
 *
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @version 1.0.0
 * @date 2016-08-24
 */
@Configuration
public class ShiroConfiguration {
	@Bean
	public RedisDao redisDao() {
		return new RedisDao();
	}
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistration;
	}

	@Bean
	@DependsOn(value = "lifecycleBeanPostProcessor")
	public ShiroRealm shiroRealm() {
		return new ShiroRealm();
	}

	@Bean
	public EhCacheManager cacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return cacheManager;
	}

	@Bean
	@Lazy
	public SessionManager sessionManager(PlatformMode platformMode) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		SimpleCookie cookie = new SimpleCookie(platformMode.getPlatform() + "_SESSIONID");
		cookie.setHttpOnly(true);
		sessionManager.setSessionIdCookie(cookie);
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionDAO(redisDao());
		return sessionManager;
	}

	@Bean
	public DefaultWebSecurityManager securityManager(ShiroRealm realm, CacheManager cacheManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setSessionManager(sessionManager);
		securityManager.setRealm(realm);
		securityManager.setCacheManager(cacheManager);
		return securityManager;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager,
			ShiroConfig config) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl(config.getLoginUrl());
		shiroFilterFactoryBean.setUnauthorizedUrl(config.getUnauthorizedUrl());
		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		filters.put("anonymous", new ShiroAnonymousFilter());
		filters.put("authorization", new ShiroAuthorizationFilter());
		shiroFilterFactoryBean.setFilters(filters);
		shiroFilterFactoryBean.setFilterChainDefinitionMap(config.getFilters());
		return shiroFilterFactoryBean;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

}

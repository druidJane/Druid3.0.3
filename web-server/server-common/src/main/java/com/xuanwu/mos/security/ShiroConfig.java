package com.xuanwu.mos.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户登录和权限过滤器配置项
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-24
 * @version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "shiro")
@ConditionalOnProperty(prefix = "shiro" , name = "enabled" , havingValue = "true")
public class ShiroConfig {

	private String loginUrl = "/login";
	private String unauthorizedUrl = "/login/unauthorized";
	private Map<String, String> filters = new LinkedHashMap<String, String>();

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
}

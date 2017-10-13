package com.xuanwu.mos.config;

import com.xuanwu.mos.utils.SessionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 读取application.yml里面的server节点的配置
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-24
 * @version 1.0.0
 */

@Component
public class ServerConfig {

	@Autowired
	private ServerProperties serverProperties;

	@PostConstruct
	public void setSessionTimeout() {
		// Session timeout in seconds.
		SessionUtil.setSessionTimeout(serverProperties.getSession().getTimeout());
	}

	public ServerProperties getServerProperties() {
		return serverProperties;
	}

}

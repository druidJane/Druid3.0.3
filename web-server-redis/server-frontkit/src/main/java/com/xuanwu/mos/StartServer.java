/*
 * Copyright (c) 2016 by XuanWu Wireless Technology Co,. Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @Description StartServer
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-07-07
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages="com.xuanwu")
@ServletComponentScan
@ImportResource(locations = "classpath:applicationContext.xml")
public class StartServer {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(StartServer.class);
		/*Set<Object> set = new HashSet<Object>();
		set.add("classpath:applicationContext.xml");
		app.setSources(set);
		app.run(args);*/
		app.run();
	}
}

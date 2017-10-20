package com.xuanwu.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages="com.xuanwu",exclude={DataSourceAutoConfiguration.class})
@ServletComponentScan
public class ServerAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerAuthApplication.class, args);
	}
}

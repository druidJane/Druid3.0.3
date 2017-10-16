package com.xuanwu;

import com.google.common.util.concurrent.RateLimiter;

import com.xuanwu.filter.AccessFilter;
import com.xuanwu.filter.RateLimiterFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class ServerGatewayApplication {

	@Value("${zuul.ratelimit.qps:5000}")
	private Long QPS;
	public static void main(String[] args) {
		SpringApplication.run(ServerGatewayApplication.class, args);
	}
	@Bean
	public AccessFilter accessFilter(){
		return new AccessFilter();
	}
	@Bean
	public RateLimiterFilter rateLimiterFilter(){
		return new RateLimiterFilter();
	}
	@Bean
	public RateLimiter rateLimiter(){
		return RateLimiter.create(QPS);
	}
}

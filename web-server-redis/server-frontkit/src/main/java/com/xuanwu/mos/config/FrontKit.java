package com.xuanwu.mos.config;

import org.springframework.stereotype.Component;

@Component
public class FrontKit implements PlatformMode {

	@Override
	public Platform getPlatform() {
		return Platform.FRONTKIT;
	}
	
	@Override
	public String getName() {
		return "mos";
	}

	@Override
	public String getVersion() {
		return "5.0";
	}
}

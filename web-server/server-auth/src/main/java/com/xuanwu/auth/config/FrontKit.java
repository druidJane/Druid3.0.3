package com.xuanwu.auth.config;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.config.PlatformMode;

import org.springframework.stereotype.Component;

@Component(value = "frontKit")
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

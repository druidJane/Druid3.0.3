/*
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.config;

/**
 * @Description 平台模式接口，实际平台必须实现
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-9-27
 * @Version 1.0.0
 */
public interface PlatformMode {

	/**
	 * 获取平台
	 * @return
	 */
	public Platform getPlatform();
	
	/**
	 * 获取平台名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 获取版本号
	 * @return
	 */
	public String getVersion();
	
}

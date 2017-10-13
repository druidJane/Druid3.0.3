/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.cache.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-2-15
 * @Version 1.0.0
 */
public class ConfigRecord {
	private String key;
	private String value;
	private String type;
	private int version;
	private int platformId;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPlatformId() {
		return platformId;
	}
	
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
}

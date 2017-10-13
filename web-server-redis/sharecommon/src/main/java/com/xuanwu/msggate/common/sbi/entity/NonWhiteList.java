/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-11-1
 * @Version 1.0.0
 */
public class NonWhiteList {

	private String phone;
	private Integer channelId;

	public NonWhiteList(String phone, Integer channelId) {
		this.phone = phone;
		this.channelId = channelId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

}

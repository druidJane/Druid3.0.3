/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.cache.entity;

import com.xuanwu.msggate.common.sbi.entity.Carrier;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-2-15
 * @Version 1.0.0
 */
public class PhoneSegment {
	private Carrier carrier;
	private String phoneSegment;
	private int version;

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(int index) {
		this.carrier = Carrier.getType(index);
	}

	public String getPhoneSegment() {
		return phoneSegment;
	}

	public void setPhoneSegment(String phoneSegment) {
		this.phoneSegment = phoneSegment;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}

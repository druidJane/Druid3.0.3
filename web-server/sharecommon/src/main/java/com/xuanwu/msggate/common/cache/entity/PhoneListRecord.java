/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache.entity;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-27
 * @Version 1.0.0
 */
public class PhoneListRecord {
	private long phone;
	private byte type;
	private int target;
	private boolean isRemoved;

	/**
	 * Get phone
	 * 
	 * @return the phone
	 */
	public long getPhone() {
		return phone;
	}

	/**
	 * Set phone
	 * 
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(long phone) {
		this.phone = phone;
	}

	/**
	 * Get type
	 * 
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * Set type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * Get target
	 * 
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * Set target
	 * 
	 * @param target
	 *            the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}

	/**
	 * Get isRemoved
	 * 
	 * @return the isRemoved
	 */
	public boolean isRemoved() {
		return isRemoved;
	}

	/**
	 * Set isRemoved
	 * 
	 * @param isRemoved
	 *            the isRemoved to set
	 */
	public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * The commit information of the commit info of the carrier gate.
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-18
 * @Version 1.0.0
 */
public class PCommitFrameItem implements CommitFrameItem {
	/***
	 * The phone number
	 */
	String phone;

	/**
	 * unique code to identity the commit
	 */
	String uCode;

	/**
	 * Commit result
	 */
	Integer result;

	/**
	 * Get phone
	 * 
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set phone
	 * 
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Get uCode * @return the uCode
	 */
	public String getuCode() {
		return uCode;
	}

	/**
	 * Set uCode
	 * 
	 * param uCode the uCode to set
	 */
	public void setuCode(String uCode) {
		this.uCode = uCode;
	}

	/**
	 * Get result
	 * 
	 * urn the result
	 */
	public Integer getResult() {
		return result;
	}

	/**
	 * Set result
	 * 
	 * @paresult the result to set
	 */
	public void setResult(Integer result) {
		this.result = result;
	}

}

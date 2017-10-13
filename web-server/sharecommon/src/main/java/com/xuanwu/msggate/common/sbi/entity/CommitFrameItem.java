/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-19
 * @Version 1.0.0
 */
public interface CommitFrameItem {

	/**
	 * Get phone
	 * 
	 * @return the phone
	 */
	public String getPhone();

	/**
	 * Set phone
	 * 
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone);

	/**
	 * Get uCode
	 * 
	 * @return the uCode
	 */
	public String getuCode();

	/**
	 * Set uCode
	 * 
	 * @param uCode
	 *            the uCode to set
	 */
	public void setuCode(String uCode);

	/**
	 * Get result
	 * 
	 * @return the result
	 */
	public Integer getResult();

	/**
	 * Set result
	 * 
	 * @param result
	 *            the result to set
	 */
	public void setResult(Integer result);

}

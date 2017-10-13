/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache.dao;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-27
 * @Version 1.0.0
 */
public interface InitListCallBack {
	/**
	 * Insert the list to the store
	 * 
	 * @param phone
	 * @param type
	 * @param target
	 */
	public void insertList(long phone, int type, int target);
}

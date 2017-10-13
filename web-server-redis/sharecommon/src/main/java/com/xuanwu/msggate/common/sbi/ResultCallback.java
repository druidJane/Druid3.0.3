/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi;

import com.xuanwu.msggate.common.sbi.entity.Result;

/**
 * Result call back
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-19
 * @Version 1.0.0
 */
public interface ResultCallback {
	/**
	 * Push the handled result to queue.
	 * 
	 * @param result
	 * @throws Exception
	 */
	public void pushResult(Result result);

	public void setFetchSize(int size);

	public int getFetchSize();
	
	public int getTicketSize();
}

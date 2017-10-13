/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi;

import com.xuanwu.msggate.common.sbi.exception.NotExistException;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-19
 * @Version 1.0.0
 */
public interface MTOHandler extends FrameHandler, MOHandler {
	/**
	 * Fetch waiting message count by channel identity
	 * @param mtoIdentity
	 * @return
	 */
	public int fetchWaitMsgCount(String mtoIdentity) throws NotExistException, Exception;
}

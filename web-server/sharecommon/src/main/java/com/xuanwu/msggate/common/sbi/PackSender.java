/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi;

import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.exception.DuplicateException;

/**
 * Interface used to send message pack to the centrum server
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-1
 * @Version 1.0.0
 */
public interface PackSender {

	/**
	 * Send the pack to the Msg-centrum server.
	 * <p>
	 * <b>Notice: this method doesn't prevent the thread,improve the performance
	 * to the client</b>
	 * 
	 * @throws DuplicateException
	 *             the pack was duplicated
	 * @throws Exception
	 *             other exception
	 */
	void send(MsgPack pack) throws DuplicateException, Exception;

}

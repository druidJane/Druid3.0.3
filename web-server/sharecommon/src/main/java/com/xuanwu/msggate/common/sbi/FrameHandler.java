/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi;

import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.exception.NotExistException;

import java.util.List;
import java.util.UUID;

/**
 * Interface of the frame fetch,used by MTO Server client to retrieve the frame.
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-8
 * @Version 1.0.0
 */
public interface FrameHandler {
	/**
	 * Fetch the mto info associated with the mto server
	 * 
	 * @param requestUUID
	 * @param mtoIdentity
	 * @return
	 * @throws NotExistException
	 * @throws Exception
	 */
	CarrierChannel fetchChannel(UUID requestUUID, String mtoIdentity)
			throws NotExistException, Exception;

	/**
	 * Fetch the frame
	 * 
	 * @param requestUUID
	 * @param mtoIdentity
	 * @param fetchSize
	 *            TODO
	 * @param callback
	 *            call back function
	 * @throws Exception
	 */
	public void fetchFrame(UUID requestUUID, String mtoIdentity, int fetchSize,
			int ticketSize, ResultCallback callback) throws NotExistException,
			Exception;

	/**
	 * Commit frame.
	 * <p>
	 * after all the messages of the frame were committed by carrier gate,notify
	 * the centrum server.
	 * </p>
	 * 
	 * @param request
	 *            uuid
	 * @param mtoIdentity
	 *            TODO
	 * @param commitFrame
	 *            TODO
	 * @param commitItem
	 *            commit message information
	 * @throws Exception
	 */
	public void commitHandledFrame(UUID uuid, String mtoIdentity,
			List<MsgFrame> commitFrame) throws NotExistException, Exception;

	/**
	 * @param tranBuilder2UUID
	 * @param identity
	 * @param frameIDsList
	 */
	void reportFrameState(UUID tranBuilder2UUID, String identity,long handleFrameTime,
			List<Long> handleFrameIDs);
}

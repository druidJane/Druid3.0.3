/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.List;
import java.util.UUID;

import com.xuanwu.msggate.common.sbi.entity.impl.FetchFrame;


/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-19
 * @Version 1.0.0
 */
public interface Result {

	/**
	 * MTO request result
	 */
	public enum MTOResultState {
		SUCCESS, INNER_ERROR, MTO_NO_EXIST
	}

	/**
	 * Get the mto info
	 * 
	 * @return
	 */
	public CarrierChannel getChannel();

	/**
	 * Get the result
	 * 
	 * @return
	 */
	public MTOResultState getResult();

	/**
	 * Get the response message
	 * 
	 * @return
	 */
	public String getMessage();

	/**
	 * Get the associated request uuid
	 * 
	 * @return
	 */
	public UUID getRequestUUID();

	/**
	 * Get the data of the result
	 * 
	 * @return
	 */
	public List<FetchFrame> getFrames();
}

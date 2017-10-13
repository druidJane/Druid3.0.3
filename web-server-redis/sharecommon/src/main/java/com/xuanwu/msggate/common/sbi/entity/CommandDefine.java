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
public class CommandDefine {

	/**
	 * MTO Request command
	 * 
	 * @author <a href="mwanglianguang@139130.netg@139130.net">LianGuang
	 *         Wang</a>
	 * @Data 2010-5-19
	 * @Version 1.0.0
	 */
	public enum MTOCommand {
		FETCH_FRAME, COMMIT_FRAME, STATE_REPORT, MO_REPORT, FETCH_MTO_INFO, REPORT_FRAME_STATE, SUSPEND_FETCH_FRAME, RESUME_FETCH_FRAME, FETCH_WAIT_MSG_COUNT
	}

}

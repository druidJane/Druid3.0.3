/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.logic;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

public interface WordFragmentRepos {

	public String encodeContent(String content, String signature,
			MsgType msgType, boolean sigLocation, boolean isSignal,
			int mixLength, int extendSpace);

	public int getNormalSmsExtendSpace(String content, String signature,
			int mixLength);
}

/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.ArrayList;
import java.util.List;

import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame.BizForm;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-11-1
 * @Version 1.0.0
 */
public class MsgSingleBox {
	private MsgSingle msgSingle;
	private List<Integer> specNumIDs = new ArrayList<Integer>();
	private boolean isBypass;
	
	public MsgSingleBox(MsgSingle msgSingle) {
		this.msgSingle = msgSingle;
	}

	public MsgSingle getMsgSingle() {
		return msgSingle;
	}

	public void setMsgSingle(MsgSingle msgSingle) {
		this.msgSingle = msgSingle;
	}
	
	public Carrier getCarrier(){
		return msgSingle.getCarrier();
	}

	public boolean isBypass() {
		return isBypass;
	}

	public void setBypass(boolean isBypass) {
		this.isBypass = isBypass;
	}

	public MsgType getMsgType(){
		return msgSingle.getType();
	}
	
	public void setMsgType(MsgType msgType){
		msgSingle.setType(msgType);
	}
	
	public List<Integer> getSpecNumIDs() {
		return specNumIDs;
	}

	public void addSpecNumID(Integer specNumID) {
		specNumIDs.add(specNumID);
	}

	public Long tran2Key(long specNumsCode, BizForm bizForm, Carrier carrier, MsgType msgType) {
		return Long.valueOf((specNumsCode << 16)
				| ((bizForm.getIndex() & 0xFF) << 8)
				| ((carrier.getIndex() & 0xF) << 4)
				| (msgType.getIndex() & 0xF));
	}
}

/*
\ * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem;
import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.Region;

/**
 * Centrum message single entity
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-7
 * @Version 1.0.0
 */
public class PMsgSingle implements MsgSingle {
	/**
	 * Message type
	 */
	private MsgContent.MsgType type;
	/**
	 * The phone number
	 */
	private String phone;
	/**
	 * Content of the message
	 */
	private MsgContent content;
	/**
	 * The custom message id
	 */
	private String customMsgID;

	private String customNum;

	private boolean vip;

	private Carrier carrier;
	
	private Region region;
	
	private String orgCode;
	
	private int msgFmt;

	public PMsgSingle(MsgType msgType, String phone, MsgContent content,
			String customMsgID, String customNum, boolean vip,int carrier,String orgCode,int msgFmt) {
		this.type = msgType;
		this.phone = phone;
		this.content = content;
		this.customMsgID = customMsgID;
		this.customNum = customNum;
		this.vip = vip;
		this.carrier = Carrier.getType(carrier);
		this.orgCode = orgCode;
		this.msgFmt = msgFmt;
	}

	public PMsgSingle(MsgType msgType, String phone, MsgContent content,
			String customMsgID, String customNum, boolean vip,int carrier) {
		this.type = msgType;
		this.phone = phone;
		this.content = content;
		this.customMsgID = customMsgID;
		this.customNum = customNum;
		this.vip = vip;
		this.carrier = Carrier.getType(carrier);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MsgContent getContent() {
		return content;
	}
	
	@Override
	public void setContent(MsgContent content){
		this.content = content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public MsgType getType() {
		return type;
	}

	@Override
	public String getCustomMsgID() {
		return customMsgID;
	}

	@Override
	public String getCustomNum() {
		return customNum;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean isVip() {
		return vip;
	}

	@Override
	public Carrier getCarrier() {
		return carrier;
	}

	@Override
	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}
	
	@Override
	public Region getRegion() {
		return region;
	}

	@Override
	public void setRegion(Region region) {
		this.region = region;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Override
	public void setType(MsgType type) {
		this.type = type;
	}

	@Override
	public int getMsgFmt() {
		return msgFmt;
	}
	
	@Override
	public void setMsgFmt(int msgFmt) {
		this.msgFmt = msgFmt;
	}

	@Override
	public MsgItem build() {
		MsgItem.Builder builder = MsgItem.newBuilder();
		builder.setPhone(phone);
		if(content != null)
			builder.setContent(content.build());
		builder.setCustomMsgID((customMsgID == null) ? "" : customMsgID);
		builder.setCustomNum((customNum == null) ? "" : customNum);
		builder.setVipFlag(vip);
		builder.setCarrier(carrier.getIndex());
		builder.setOrgCode(orgCode);
		builder.setMsgFmt(msgFmt);
		return builder.build();
	}

	@Override
	public String toString() {
		return "PMsgSingle [content=" + content + ", customMsgID="
				+ customMsgID + ", customNum=" + customNum + ", phone=" + phone
				+ ", type=" + type + ", vip=" + vip + ", orgCode=" + orgCode + 
				", msgFmt=" + msgFmt + "]";
	}


}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

/**
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-5
 * @Version 1.0.0
 */
public class RedSpecNumResult {

	private Integer id;
	private String phone;
	private Integer specsvsNumID;
	private SpecSVSNumber specSVSNum;
	private MsgType msgType;
	private Double price;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getSpecsvsNumID() {
		return specsvsNumID;
	}

	public void setSpecsvsNumID(Integer specsvsNumID) {
		this.specsvsNumID = specsvsNumID;
	}

	public SpecSVSNumber getSpecSVSNum() {
		return specSVSNum;
	}

	public CarrierChannel getCarrierChannel() {
		return specSVSNum.getCarrierChannel();
	}

	public void setSpecSVSNum(SpecSVSNumber specSVSNum) {
		this.specSVSNum = specSVSNum;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = MsgType.getType(msgType);
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public RedSpecNumResult duplitBasicResult() {
		RedSpecNumResult result = new RedSpecNumResult();
		result.id = getId();
		result.phone = getPhone();
		result.specsvsNumID = getSpecsvsNumID();
		result.specSVSNum = getSpecSVSNum();
		result.msgType = getMsgType();
		result.price = getPrice();
		return result;
	}
}

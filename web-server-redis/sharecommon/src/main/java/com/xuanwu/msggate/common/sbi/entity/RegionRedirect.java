/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-12-12
 * @Version 1.0.0
 */
public class RegionRedirect {

	private Integer regionID;

	private Integer specsvsNumID;

	private Integer priRedirectID;

	private Integer minorRedirectID;

	private Integer msgType;

	private Double price;

	public Integer getRegionID() {
		return regionID;
	}

	public void setRegionID(Integer regionID) {
		this.regionID = regionID;
	}

	public Integer getSpecsvsNumID() {
		return specsvsNumID;
	}

	public void setSpecsvsNumID(Integer specsvsNumID) {
		this.specsvsNumID = specsvsNumID;
	}

	public Integer getPriRedirectID() {
		return priRedirectID;
	}

	public void setPriRedirectID(Integer priRedirectID) {
		this.priRedirectID = priRedirectID;
	}

	public Integer getMinorRedirectID() {
		return minorRedirectID;
	}

	public void setMinorRedirectID(Integer minorRedirectID) {
		this.minorRedirectID = minorRedirectID;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public BindSpecNumResult buildBindResult() {
		BindSpecNumResult result = new BindSpecNumResult();
		result.setMsgType(msgType);
		result.setPrice(price);
		result.setRegionID(regionID);
		result.setSpecsvsNumID(priRedirectID);
		return result;
	}
}

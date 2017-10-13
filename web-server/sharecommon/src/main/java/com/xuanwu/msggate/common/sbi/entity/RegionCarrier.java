/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;


/**
 * 地区运营商
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2010-7-8
 * @Version 1.0.0
 */
public class RegionCarrier {
	/**
	 * Identity
	 */
	private Integer id;

	/**
	 * Location of the carrier
	 */
	private Integer regionID;

	/**
	 * Region of the carrier
	 */
	private Region region;

	/**
	 * 运营商类型
	 */
	private Carrier carrier;

	/**
	 * 地区转发映射
	 */
	//List<RegionRedirect> regionRedirects = new ArrayList<RegionRedirect>();

	/**
	 * Get id
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(int index) {
		this.carrier = Carrier.getType(index);
	}

//	public List<RegionRedirect> getRegionRedirects() {
//		return regionRedirects;
//	}
//
//	public void setRegionRedirects(List<RegionRedirect> regionRedirects) {
//		this.regionRedirects = regionRedirects;
//	}

	public Integer getRegionID() {
		return regionID;
	}

	public void setRegionID(Integer region) {
		this.regionID = region;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}

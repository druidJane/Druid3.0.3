/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * Region code
 * 
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-8-4
 * @Version 1.0.0
 */
public class Region {

	public enum RegionType {
		PROVINCE, CITY
	}

	private Integer id;
	private Integer parentID;
	private RegionType type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
		if (parentID == null) {
			type = RegionType.PROVINCE;
		} else
			type = RegionType.CITY;
	}

	public RegionType getType() {
		return type;
	}
}

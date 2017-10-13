/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * Virtual channel map
 * 
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-11
 * @Version 1.0.0
 */
public class VirtualChannelNumMap {
    private SpecSVSNumber specNum;
    private Integer specNumID;
    private Integer virtualChannelID;

    public SpecSVSNumber getSpecNum() {
        return specNum;
    }

    public void setSpecNum(SpecSVSNumber specNum) {
        this.specNum = specNum;
    }

    public Integer getSpecNumID() {
        return specNumID;
    }

    public void setSpecNumID(Integer specNumID) {
        this.specNumID = specNumID;
    }

	public Integer getVirtualChannelID() {
		return virtualChannelID;
	}

	public void setVirtualChannelID(Integer virtualChannelID) {
		this.virtualChannelID = virtualChannelID;
	}
    
}

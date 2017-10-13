/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Pack deduct
 * 
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-8-17
 * @Version 1.0.0
 */
public class PackDeduct {
    /**
     * Identity
     */
    private long id;
    /**
     * Associated message pack
     */
    private MsgPack pack;
    /**
     * Amount of the deduct
     */
    private double amount;
    /**
     * Occurred time
     */
    private Date time;
    /**
     * frame deducts
     */
    private List<FrameDeduct> subDeducts = new ArrayList<FrameDeduct>();
    
    private PackDeduct parent;

    public PackDeduct(MsgPack pack) {
        this.pack = pack;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MsgPack getPack() {
        return pack;
    }

    public void setPack(MsgPack pack) {
        this.pack = pack;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<FrameDeduct> getSubDeducts() {
        return subDeducts;
    }

    public void setSubDeducts(List<FrameDeduct> subDeducts) {
        this.subDeducts = subDeducts;
    }

    public void addFrameDeduct(FrameDeduct frameDeduct) {
        subDeducts.add(frameDeduct);
    }

    public PackDeduct getParent() {
		return parent;
	}

	public void setParent(PackDeduct parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "PackDeduct [id=" + id + ", pack=" + pack + ", amount=" + amount
				+ ", time=" + time + ", subDeducts=" + subDeducts + ", parent="
				+ parent + "]";
	}
}

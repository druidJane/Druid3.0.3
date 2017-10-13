/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;

/**
 * Frame deduct
 * 
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-8-17
 * @Version 1.0.0
 */
public class FrameDeduct {
	/**
	 * Identity
	 */
	private long id;

	/**
	 * Parent id
	 */
	private long parentID;
	/**
	 * Associated message pack
	 */
	private MsgFrame frame;
	/**
	 * Amount of the deduct
	 */
	private int amount;

	/**
	 * Price
	 */
	private double price;
	/**
	 * Occurred time
	 */
	private Date time;

	public FrameDeduct(MsgFrame frame, int amount, double price) {
		super();
		this.frame = frame;
		this.amount = amount;
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MsgFrame getFrame() {
		return frame;
	}

	public void setFrame(MsgFrame frame) {
		this.frame = frame;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getTotalAmount() {
		return amount * price;
	}

	public long getParentID() {
		return parentID;
	}

	public void setParentID(long parentID) {
		this.parentID = parentID;
	}

}

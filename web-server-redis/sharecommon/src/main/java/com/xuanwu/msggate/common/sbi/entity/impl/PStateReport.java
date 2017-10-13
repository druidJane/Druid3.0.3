/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import java.util.Date;

import com.xuanwu.msggate.common.sbi.entity.StateReport;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-19
 * @Version 1.0.0
 */
public class PStateReport implements StateReport {

	/**
	 * Message identity
	 */
	private String msgID;
	
	/** 
	 * Post time
	 */
	private Date postTime;
	
	/**
	 * Submit time
	 */
	private Date submitTime;
	/**
	 * Complete time
	 */
	private Date doneTime;

	/**
	 * Destination phone
	 */
	private String destPhone;

	/**
	 * Sequence number
	 */
	private long smscSequence;

	private long id;
	/**
	 * Delivery state
	 */
	private StateReportResult state;
	
	private String originResult;
	
	private int userID;
	
    private int enterpriseID;
	
	private String packID;
	
	private int channelID;
	
	private String customMsgID;
	
	private String suffix;

	/**
	 * mto接受到运营商report的时间点
	 */
	private Date createTime;

	public PStateReport() {
	}

	public PStateReport(String msgID, long smscSequence, String destTerminalId,
	        Date submitTime, Date doneTime, StateReportResult state) {
		this.msgID = msgID;
		this.smscSequence = smscSequence;
		this.destPhone = destTerminalId;
		this.submitTime = submitTime;
		this.doneTime = doneTime;
		this.state = state;
	}
	
	public PStateReport(String msgID, long smscSequence, String destTerminalId,
			Date submitTime, Date doneTime, StateReportResult state, String originResult) {
		this.msgID = msgID;
		this.smscSequence = smscSequence;
		this.destPhone = destTerminalId;
		this.submitTime = submitTime;
		this.doneTime = doneTime;
		this.state = state;
		this.originResult = originResult;
	}

	@Override
	public String getMsgID() {
		return msgID;
	}

	@Override
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	
	@Override
	public Date getPostTime() {
		return postTime;
	}

	@Override
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	/**
	 * Get submitTime
	 *
	 * @return the submitTime
	 */
	public Date getSubmitTime() {
		return submitTime;
	}

	/**
	 * Set submitTime
	 *
	 * @param submitTime
	 *            the submitTime to set
	 */
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	/**
	 *
	 * Get doneTime
	 *
	 * @return the doneTime
	 */
	public Date getDoneTime() {
		return doneTime;
	}

	/**
	 * Set doneTime
	 *
	 * @param doneTime
	 *            the doneTime to set
	 */
	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}

	@Override
	public void setState(StateReportResult state) {
		this.state = state;
	}

	@Override
	public StateReportResult getState() {
		return state;
	}

	@Override
	public long getSmscSequence() {
		return smscSequence;
	}

	@Override
	public void setSmscSequence(long smscSequence) {
		this.smscSequence = smscSequence;
	}

	@Override
	public String getDestPhone() {
		return destPhone;
	}

	@Override
	public void setDestPhone(String destPhone) {
		this.destPhone = destPhone;
	}

	@Override
	public String toString() {
		return "PStateReport [msgID=" + msgID + ", submitTime=" + submitTime
		        + ", doneTime=" + doneTime + ", destPhone=" + destPhone
		        + ", smscSequence=" + smscSequence + ", state=" + state + "]";
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void setStateIndex(int index) {
		this.state = StateReportResult.getType(index);
	}

	@Override
	public String getOriginResult() {
		return originResult;
	}

	@Override
	public void setOriginResult(String originResult) {
		this.originResult = originResult;
	}

	@Override
	public int getUserID() {
		return userID;
	}

	@Override
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	@Override
	public int getEnterpriseID() {
		return enterpriseID;
	}

	@Override
	public void setEnterpriseID(int enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	@Override
	public String getPackID() {
		return packID;
	}

	@Override
	public void setPackID(String packID) {
		this.packID = packID;
	}

	@Override
	public int getChannelID() {
		return channelID;
	}

	@Override
	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}
	
	@Override
	public String getCustomMsgID() {
		return customMsgID;
	}
	
	@Override
	public void setCustomMsgID(String customMsgID) {
		this.customMsgID = customMsgID;
	}
	
	@Override
	public String getSuffix() {
		return suffix;
	}

	@Override
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}
}

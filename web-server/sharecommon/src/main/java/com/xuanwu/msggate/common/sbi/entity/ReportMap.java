package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;

public class ReportMap {

	private long id;

	private String msgID;

	private int userID;

	private int enterpriseID;

	private String packID;

	private int channelID;
	
	private String customMsgID;
	
	private Date postTime;
	
	private Date submitTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getEnterpriseID() {
		return enterpriseID;
	}

	public void setEnterpriseID(int enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	public String getPackID() {
		return packID;
	}

	public void setPackID(String packID) {
		this.packID = packID;
	}

	public int getChannelID() {
		return channelID;
	}

	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}
	
	public String getCustomMsgID() {
		return customMsgID;
	}
	
	public void setCustomMsgID(String customMsgID) {
		this.customMsgID = customMsgID;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	@Override
	public String toString() {
		return "ReportMap [id=" + id + ", msgID=" + msgID + ", userID="
				+ userID + ", enterpriseID=" + enterpriseID + ", packID="
				+ packID + ", channelID=" + channelID + ", customMsgID="
				+ customMsgID + "]";
	}
}

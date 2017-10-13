package com.xuanwu.msggate.common.sbi.entity;

public class UserBind {

	private int userID;
	private int parentID;
	private int entID;
	private boolean entIdentifyExist = false;
	private String entIdentify = "";
	private String userIdentify = "";
	private long matchDeadline = System.currentTimeMillis() + 600000;// 10 minute
	private int channelId;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public int getEntID() {
		return entID;
	}

	public void setEntID(int entID) {
		this.entID = entID;
	}

	public String getEntIdentify() {
		return entIdentify;
	}

	public void setEntIdentify(String entIdentify) {
		this.entIdentify = (entIdentify == null ? "" : entIdentify);
	}

	public String getUserIdentify() {
		return userIdentify;
	}

	public void setUserIdentify(String userIdentify) {
		this.userIdentify = (userIdentify == null ? "" : userIdentify);
	}

	public boolean isEntIdentifyExist() {
		return entIdentifyExist;
	}

	public void setEntIdentifyExist(boolean entIdentifyExist) {
		this.entIdentifyExist = entIdentifyExist;
	}

	public long getMatchDeadline() {
		return matchDeadline;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

}

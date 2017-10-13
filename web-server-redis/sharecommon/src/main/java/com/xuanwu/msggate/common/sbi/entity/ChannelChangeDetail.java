package com.xuanwu.msggate.common.sbi.entity;

public class ChannelChangeDetail {
	private int id;
	private int enterpriseSpecnumBindId;
	private int oldSpecnumId;
	private int specsvsNumId;
	private int changeId;
	private int targetChannelId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEnterpriseSpecnumBindId() {
		return enterpriseSpecnumBindId;
	}
	public void setEnterpriseSpecnumBindId(int enterpriseSpecnumBindId) {
		this.enterpriseSpecnumBindId = enterpriseSpecnumBindId;
	}
	public int getOldSpecnumId(){
		return oldSpecnumId;
	}
	public void setOldSpecnumId(int oldSpecnumId){
		this.oldSpecnumId = oldSpecnumId;
	}
	public int getSpecsvsNumId() {
		return specsvsNumId;
	}
	public void setSpecsvsNumId(int specsvsNumId) {
		this.specsvsNumId = specsvsNumId;
	}
	public int getChangeId() {
		return changeId;
	}
	public void setChangeId(int changeId) {
		this.changeId = changeId;
	}
	public int getTargetChannelId() {
		return targetChannelId;
	}
	public void setTargetChannelId(int targetChannelId) {
		this.targetChannelId = targetChannelId;
	}
	@Override
	public String toString() {
		return "ChannelChangeDetail [id=" + id + ", enterpriseSpecnumBindId="
				+ enterpriseSpecnumBindId +", oldSpecnumId="+ oldSpecnumId + ", specsvsNumId=" + specsvsNumId
				+ ", changeId=" + changeId + ", targetChannelId="
				+ targetChannelId + "]";
	}
}

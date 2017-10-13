package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgFrame.BizForm;

public class ChannelChange {
	private int id;
	private int sourcesChannelId;
	private String targetchannelId;
	private int state;
	private ChannelChangeType type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSourcesChannelId() {
		return sourcesChannelId;
	}
	public void setSourcesChannelId(int sourcesChannelId) {
		this.sourcesChannelId = sourcesChannelId;
	}
	public String getTargetchannelId() {
		return targetchannelId;
	}
	public void setTargetchannelId(String targetchannelId) {
		this.targetchannelId = targetchannelId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public ChannelChangeType getType() {
		return type;
	}
	public void setType(int index) {
		this.type = ChannelChangeType.getType(index);
	}
	@Override
	public String toString() {
		return "ChannelChange [id=" + id + ", sourcesChannelId="
				+ sourcesChannelId + ", targetchannelId=" + targetchannelId
				+ ", state=" + state + ", type=" + type + "]";
	}
	
	public enum ChannelChangeType{
		ENTERPRISECHANGE(0),
		CHANNELCHANGE(2);
		private final int index;
		private ChannelChangeType(int index){
			this.index = index;
		}
		public int getIndex(){
			return index;
		}
		public static ChannelChangeType getType(int index) {
			ChannelChangeType[] types = ChannelChangeType.values();
			for (ChannelChangeType type : types) {
				if(type.getIndex() == index)
					return type;
			}
			return ENTERPRISECHANGE; 
		}
		public static BizForm parseEnum(String str) {
			return BizForm.valueOf(str);
		}
	}
}

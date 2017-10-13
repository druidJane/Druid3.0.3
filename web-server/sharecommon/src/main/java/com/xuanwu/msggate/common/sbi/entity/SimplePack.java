package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgPack.PackState;

import java.util.Date;


/**
 * 简单批次实体
 * @Description 
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2013-12-6
 * @Version 1.0.0
 */
public class SimplePack {

	private String id;
	
	private Date postTime;
	
	private int msgType;
	
	private int count = 0;
	
	private PackState state;
	
	private Date commitTime;
	
	private String tableSuffix = "";
	
	public SimplePack() {
		
	}
	
	public SimplePack(String id, int msgType, Date postTime, int count){
		this.id = id;
		this.msgType = msgType;
		this.postTime = postTime;
		this.count = count;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
	public int getCount() {
		return count;
	}
	
	/**
	 * 数量变化
	 * @param count
	 * @return 是否需要更新状态
	 */
	public boolean changeCount(int count){
		PackState prev = this.state;
		this.count += count;
		if(this.count > 0){
			this.state = PackState.HANDLE;
		} else if(this.count == 0){
			this.state = PackState.OVER;
		}
		return prev != this.state;
	}
	
	public void setHandleOver(){
		this.state = PackState.OVER;
	}
	
	public PackState getState() {
		return state;
	}
	
	public Date getCommitTime() {
		return commitTime;
	}
	
	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
	
	public String getTableSuffix() {
		return tableSuffix;
	}
	
	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}
}

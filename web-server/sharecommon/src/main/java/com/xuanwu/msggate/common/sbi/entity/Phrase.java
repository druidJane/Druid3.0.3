/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @Description: 模板实体类
 * @Author <a href="jiji@javawind.com">XueFang Xu</a>
 * @Date 2013-03-12
 * @Version 1.0.0
 */
public class Phrase {

	private int id;
	private byte[] content;
	private int userId;
	private String title;
	private int msgType;
	private Date lastUpdateTime;
	private String identify;
	private int type;
	private int bizType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getContentStr() {
		if (content != null) {
			return new String(content, Charset.forName("UTF-8"));
		}
		return null;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBizType() {
		return bizType;
	}

	public void setBizType(int bizType) {
		this.bizType = bizType;
	}


	@Override
	public String toString() {
		return "Phrase [id=" + id + ", content=" + getContentStr()
				+ ", userId=" + userId + ", title=" + title + ", msgType="
				+ msgType + ", lastUpdateTime=" + lastUpdateTime
				+ ", identify=" + identify + ", type=" + type + ", bizType="
				+ bizType + "]";
	}

}

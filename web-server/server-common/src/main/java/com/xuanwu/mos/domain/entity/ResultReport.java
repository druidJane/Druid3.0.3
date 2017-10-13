package com.xuanwu.mos.domain.entity;

import java.util.Date;

/**
 * 
 * @Description: 状态报告统计
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-12-7
 * @Version 1.0.0
 */
public class ResultReport {
	private Integer id;
	private int bizType;
	private int userId;
	private int enterpriseId;
	private int carrier;
	private int channelId;
	private int state;
	private int msgType;
	private int count = 1;
	
	private Date statDate;
	private Date genDate;
	

	public ResultReport() {

	}

	public ResultReport(int bizType, int userId, int enterpriseId, int carrier,
			int channelId, int state, int msgType) {
		this.bizType = bizType;
		this.userId = userId;
		this.enterpriseId = enterpriseId;
		this.carrier = carrier;
		this.channelId = channelId;
		this.state = state;
		this.msgType = msgType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getBizType() {
		return bizType;
	}

	public int getUserId() {
		return userId;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public int getCarrier() {
		return carrier;
	}

	public int getChannelId() {
		return channelId;
	}

	public int getState() {
		return state;
	}

	public int getMsgType() {
		return msgType;
	}

	public int getCount() {
		return count;
	}

	public void setBizType(int bizType) {
		this.bizType = bizType;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public void setCarrier(int carrier) {
		this.carrier = carrier;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public Date getGenDate() {
		return genDate;
	}

	public void setGenDate(Date genDate) {
		this.genDate = genDate;
	}

	public void increment() {
		this.count += 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bizType;
		result = prime * result + carrier;
		result = prime * result + channelId;
		result = prime * result + enterpriseId;
		result = prime * result + msgType;
		result = prime * result + state;
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultReport other = (ResultReport) obj;
		if (bizType != other.bizType)
			return false;
		if (carrier != other.carrier)
			return false;
		if (channelId != other.channelId)
			return false;
		if (enterpriseId != other.enterpriseId)
			return false;
		if (msgType != other.msgType)
			return false;
		if (state != other.state)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
}

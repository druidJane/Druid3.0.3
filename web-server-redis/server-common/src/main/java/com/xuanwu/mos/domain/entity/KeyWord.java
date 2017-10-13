package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.DateUtil.DateTimeType;

import java.util.Date;

public class KeyWord extends AbstractEntity {
	private int id;
	private String keywordName;
	private Date handleTime;
	private boolean isRemoved;
	private int userId;
	private int type;
	private int targetId;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeywordName() {
		return keywordName;
	}

	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public boolean isRemoved() {
		return isRemoved;
	}

	public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"keywordName\":\"").append(keywordName).append('\"');
		sb.append(",\"handleTime\":\"")
				.append(DateUtil.format(handleTime, DateTimeType.DateTime))
				.append('\"');
		sb.append(",\"isRemoved\":\"").append(isRemoved).append('\"');
		sb.append(",\"userId\":\"").append(userId).append('\"');
		sb.append(",\"type\":\"").append(type).append('\"');
		sb.append(",\"targetId\":\"").append(targetId).append('\"');
		sb.append('}');
		return sb.toString();
	}
}

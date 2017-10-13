package com.xuanwu.mos.domain.entity;


import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.NoticeMsgType;
import com.xuanwu.mos.domain.enums.NoticeScope;
import com.xuanwu.mos.domain.enums.NoticeState;

import java.util.Date;

/**
 * Created by zengzel 消息通知
 */
public class Notice extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer enterpriseId;
	private Integer createUser;
	private Integer readUser;
	private NoticeMsgType messageType;
	private Integer readPermission;
	private NoticeScope scope;
	private String objectId;
	private String messageTitle;
	private NoticeState state;
	private Date createTime;
	private Date pushTime;


	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public NoticeMsgType getMessageType() {
		return messageType;
	}

	public void setMessageType(NoticeMsgType messageType) {
		this.messageType = messageType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public NoticeState getState() {
		return state;
	}

	public void setState(NoticeState state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public Integer getReadUser() {
		return readUser;
	}

	public void setReadUser(Integer readUser) {
		this.readUser = readUser;
	}

	public Integer getReadPermission() {
		return readPermission;
	}

	public void setReadPermission(Integer readPermission) {
		this.readPermission = readPermission;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public NoticeScope getScope() {
		return scope;
	}

	public void setScope(NoticeScope scope) {
		this.scope = scope;
	}

}

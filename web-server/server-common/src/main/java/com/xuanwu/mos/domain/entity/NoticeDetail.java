package com.xuanwu.mos.domain.entity;


import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.NoticeState;

/**
 * Created by zengzel 用户消息通知
 */
public class NoticeDetail extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private NoticeState state;
	private Integer userId;
	private Integer messageId;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NoticeState getState() {
		return state;
	}

	public void setState(NoticeState state) {
		this.state = state;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
}

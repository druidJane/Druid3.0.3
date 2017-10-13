package com.xuanwu.mos.domain.entity;


/**
 * @Description 失败话单对象
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2013-9-17
 * @Version 1.0.0
 */
public class FailTicket extends BaseEntity {
	
	private long id;
	
	private int userId;
	
	private int bizTypeId;
	
	private String packId;
	
	private String frameId;
	
	private String phone;
	
	private String content;
	
	private String customMsgId;
	
	@Override
	public String toJSON() {
		return null;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getBizTypeId() {
		return bizTypeId;
	}
	
	public void setBizTypeId(int bizTypeId) {
		this.bizTypeId = bizTypeId;
	}
	
	public String getFrameId() {
		return frameId;
	}
	
	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCustomMsgId() {
		return customMsgId;
	}
	
	public void setCustomMsgId(String customMsgId) {
		this.customMsgId = customMsgId;
	}
}

/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;


/**
 * 彩信附件实体类
 * @author <a href="mailto:liangyuanming@139130.net>liangyuanming</a>
 * @Data 2011-5-18
 * @Version 1.0.0
 */
public class MmsAttachment {
	/**
	 * 附件ID
	 */
	private long id;
	/**
	 * 附件类型
	 */
	private String attachmentType;
	/**
	 * 上传附件的企业
	 */
	private Account account;
	/**
	 * 保存路径
	 */
	private String localSavePath;
	/**
	 * 远程保存路径
	 */
	private String remoteSavePath;
	/**
	 * 附件文件名
	 */
	private String attachmentName;
	/**
	 * 附件MD5
	 */
	private String attachmentMd5;
	/**
	 * 附件长度
	 */
	private long attachmentLength;
	/**
	 * 上传时间
	 */
	private Date handleTime;
	/**
	 * 失效时间
	 */
	private Date expiryTime;
	/**
	 * 状态
	 */
	private int status;
	
	public MmsAttachment(){}
	
	public MmsAttachment(long id, String attachmentType, Account account,
			String localSavePath, String remoteSavePath, String attachmentName,
			String attachmentMd5, int attachmentLength, Date handleTime,
			Date expiryTime, int status) {
		super();
		this.id = id;
		this.attachmentType = attachmentType;
		this.account = account;
		this.localSavePath = localSavePath;
		this.remoteSavePath = remoteSavePath;
		this.attachmentName = attachmentName;
		this.attachmentMd5 = attachmentMd5;
		this.attachmentLength = attachmentLength;
		this.handleTime = handleTime;
		this.expiryTime = expiryTime;
		this.status = status;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getLocalSavePath() {
		return localSavePath;
	}
	public void setLocalSavePath(String localSavePath) {
		this.localSavePath = localSavePath;
	}
	public String getRemoteSavePath() {
		return remoteSavePath;
	}
	public void setRemoteSavePath(String remoteSavePath) {
		this.remoteSavePath = remoteSavePath;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getAttachmentMd5() {
		return attachmentMd5;
	}
	public void setAttachmentMd5(String attachmentMd5) {
		this.attachmentMd5 = attachmentMd5;
	}
	public long getAttachmentLength() {
		return attachmentLength;
	}
	public void setAttachmentLength(long attachmentLength) {
		this.attachmentLength = attachmentLength;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "MmsAttachment [id=" + id + ", attachmentType=" + attachmentType
				+ ", account=" + account.getName() + ", localSavePath=" + localSavePath
				+ ", remoteSavePath=" + remoteSavePath + ", attachmentName="
				+ attachmentName + ", attachmentLength=" + attachmentLength
				+ ", attachmentMd5=" + attachmentMd5 + ", handleTime="
				+ handleTime + ", expiryTime=" + expiryTime + ", status="
				+ status + "]";
	}
	
}

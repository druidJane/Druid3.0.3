/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;

import com.xuanwu.msggate.common.encode.EncodeType;

/**
 * 上行消息
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-9
 * @Version 1.0.0
 */
public class MOTicket implements Cloneable{

	/**
	 * 上行短信类型
	 * 
	 * @author <a href="mwanglianguang@139130.net">LianGuang Wang</a>
	 * @Data 2010-6-9
	 * @Version 1.0.0
	 */
	public enum MOTicketType {
		/* 普通短信 */NS(0),
		/* 长短信 */LS(1),
		/* 彩信 */MMS(2),
		/* Wap push */WP(3),
		/* 不完整的长短信(Part missed long message ) */PMLS(4);
		private final int index;

		private MOTicketType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static MOTicketType getType(int index) {
			switch (index) {
			case 0:
				return NS;
			case 1:
				return LS;
			case 2:
				return MMS;
			case 3:
				return WP;
			case 4:
				return PMLS;
			default:
				return NS;
			}
		}
	}

	private int id;

	private MOTicketType type;

	/**
	 * 短信消息总数 <br>
	 * <b>用于长短信中</b>
	 */
	private int total = 1;

	/**
	 * 短信消息的序列号 <br>
	 * <b>用于长短信中</b>
	 */
	private int number = 1;

	/**
	 * 组ID <br>
	 * <b>用于长短信中</b>
	 */
	private int groupID;

	/**
	 * 话单唯一标识
	 */
	private String msgId;
	/**
	 * 目标特服号码
	 */
	private String specNumber;
	/**
	 * 业务类型
	 */
	private String serviceType;
	/**
	 * TP PID
	 */
	private int tpPid;
	/**
	 * TP UDHI
	 */
	private int tpUdhi;
	/**
	 * 信息格式
	 */
	private int msgFmt = EncodeType.GB.getIndex();
	/**
	 * 提交号码
	 */
	private String phone;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 保留
	 */
	private String reserved;

	/**
	 * 上行时间
	 */
	private Date postTime;

	private int bindUser;
	private int bindEnterprise;

	private Integer regionId;// 所属地区id
	private int channelId;// 通道id

	public MOTicket() {
	}

	/**
	 * Default constructor
	 */
	public MOTicket(MOTicketType type, int total, int number, int groupID,
			String msgId, String specNumber, String serviceType, int tpPid,
			int tpUdhi, String phone, String content, String reserved) {
		this.type = type;
		this.total = total;
		this.number = number;
		this.groupID = groupID;
		this.msgId = msgId;
		this.specNumber = specNumber;
		this.serviceType = serviceType;
		this.tpPid = tpPid;
		this.tpUdhi = tpUdhi;
		this.phone = phone;
		this.content = content;
		this.reserved = reserved;
		this.postTime = new Date();
	}
	
	/**
	 * Default constructor(带 msgFmt)
	 */
	public MOTicket(MOTicketType type, int total, int number, int groupID,
			String msgId, String specNumber, String serviceType, int tpPid,
			int tpUdhi, int msgFmt, String phone, String content, String reserved) {
		this.type = type;
		this.total = total;
		this.number = number;
		this.groupID = groupID;
		this.msgId = msgId;
		this.specNumber = specNumber;
		this.serviceType = serviceType;
		this.tpPid = tpPid;
		this.tpUdhi = tpUdhi;
		this.msgFmt = msgFmt;
		this.phone = phone;
		this.content = content;
		this.reserved = reserved;
		this.postTime = new Date();
	}
	
	/**
	 * Default constructor
	 */
	public MOTicket(MOTicketType type, String msgId, String specNumber,
			String serviceType, String phone, String content, Date postTime) {
		this.type = type;
		this.msgId = msgId;
		this.specNumber = specNumber;
		this.serviceType = serviceType;
		this.phone = phone;
		this.content = content;
		this.postTime = postTime;
	}

	/**
	 * Default constructor(带 msgFmt)
	 */
	public MOTicket(MOTicketType type, String msgId, String specNumber,
			String serviceType, String phone, String content, Date postTime, int msgFmt) {
		this.type = type;
		this.msgId = msgId;
		this.specNumber = specNumber;
		this.serviceType = serviceType;
		this.phone = phone;
		this.content = content;
		this.postTime = postTime;
		this.msgFmt = msgFmt;
	}
	/**
	 * 新建普通短信上传
	 */
	public static MOTicket createNormalTicket(String msgId, String specNumber,
			String serviceType, int tpPid, int tpUdhi, String phone,
			String content, String reserved) {
		return new MOTicket(MOTicketType.NS, 1, 1, 0, msgId, specNumber,
				serviceType, tpPid, tpUdhi, phone, content, reserved);
	}

	/**
	 * 新建长短信
	 */
	public static MOTicket createLSTicket(String msgId, int total, int number,
			int groupID, String specNumber, String serviceType, int tpPid,
			int tpUdhi, String phone, String content, String reserved) {
		return new MOTicket(MOTicketType.LS, total, number, groupID, msgId,
				specNumber, serviceType, tpPid, tpUdhi, phone, content,
				reserved);
	}

	/**
	 * 新建普通短信上传(带msgFmt)
	 */
	public static MOTicket createNormalTicket(String msgId, String specNumber, String serviceType, int tpPid, int tpUdhi, int msgFmt, String phone, String content, String reserved) {
		return new MOTicket(MOTicketType.NS, 1, 1, 0, msgId, specNumber, serviceType, tpPid, tpUdhi, msgFmt, phone, content, reserved);
	}

	/**
	 * 新建长短信带(带msgFmt)
	 */
	public static MOTicket createLSTicket(String msgId, int total, int number, int groupID, String specNumber, String serviceType, int tpPid, int tpUdhi, int msgFmt, String phone, String content, String reserved) {
		return new MOTicket(MOTicketType.LS, total, number, groupID, msgId, specNumber, serviceType, tpPid, tpUdhi, msgFmt, phone, content, reserved);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get total
	 * 
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Set total
	 * 
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Get number
	 * 
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Set number
	 * 
	 * @param number
	 *            the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Get msgId
	 * 
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * Set msgId
	 * 
	 * @param msgId
	 *            the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * Get specNumber
	 * 
	 * @return the specNumber
	 */
	public String getSpecNumber() {
		return specNumber;
	}

	/**
	 * Set specNumber
	 * 
	 * @param specNumber
	 *            the specNumber to set
	 */
	public void setSpecNumber(String specNumber) {
		this.specNumber = specNumber;
	}

	/**
	 * Get serviceType
	 * 
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * Set serviceType
	 * 
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * Get tpPid
	 * 
	 * @return the tpPid
	 */
	public int getTpPid() {
		return tpPid;
	}

	/**
	 * Set tpPid
	 * 
	 * @param tpPid
	 *            the tpPid to set
	 */
	public void setTpPid(int tpPid) {
		this.tpPid = tpPid;
	}

	/**
	 * Get tpUdhi
	 * 
	 * @return the tpUdhi
	 */
	public int getTpUdhi() {
		return tpUdhi;
	}

	/**
	 * Set tpUdhi
	 * 
	 * @param tpUdhi
	 *            the tpUdhi to set
	 */
	public void setTpUdhi(int tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public int getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(int msgFmt) {
		this.msgFmt = msgFmt;
	}

	/**
	 * Get phone
	 * 
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set phone
	 * 
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Get content
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set content
	 * 
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get reserved
	 * 
	 * @return the reserved
	 */
	public String getReserved() {
		return reserved;
	}

	/**
	 * Set reserved
	 * 
	 * @param reserved
	 *            the reserved to set
	 */
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	/**
	 * Get type
	 * 
	 * @return the type
	 */
	public MOTicketType getType() {
		return type;
	}

	/**
	 * Get type index
	 * 
	 * @return the type index
	 */
	public int getTypeIndex() {
		return type.ordinal();
	}

	public void setTypeIndex(int index) {
		type = MOTicketType.getType(index);
	}

	/**
	 * Set type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(MOTicketType type) {
		this.type = type;
	}

	/**
	 * Get groupID
	 * 
	 * @return the groupID
	 */
	public int getGroupID() {
		return groupID;
	}

	/**
	 * Set groupID
	 * 
	 * @param groupID
	 *            the groupID to set
	 */
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public int getBindUser() {
		return bindUser;
	}

	public void setBindUser(int bindUser) {
		this.bindUser = bindUser;
	}

	public int getBindEnterprise() {
		return bindEnterprise;
	}

	public void setBindEnterprise(int bindEnterprise) {
		this.bindEnterprise = bindEnterprise;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	
	@Override
	public MOTicket clone() throws CloneNotSupportedException{
		MOTicket newNO = null;
		try {
			newNO = (MOTicket) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newNO;
	}

	@Override
	public String toString() {
		return "MOTicket [type=" + type + ", total=" + total + ", number="
				+ number + ", groupID=" + groupID + ", msgId=" + msgId
				+ ", specNumber=" + specNumber + ", ServiceType=" + serviceType
				+ ", tpPid=" + tpPid + ", tpUdhi=" + tpUdhi + ", msgFmt=" + msgFmt + ", phone="
				+ phone + ", content=" + content + ", reserved=" + reserved
				+ ", postTime=" + postTime + ", bindEnterprise="
				+ bindEnterprise + "]";
	}
}

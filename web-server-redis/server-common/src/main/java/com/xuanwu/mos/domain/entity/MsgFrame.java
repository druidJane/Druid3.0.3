/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgPack.SendType;

import java.util.Date;
import java.util.List;

/**
 * @Description: 短信帧实体类
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2012-9-26
 * @Version 1.0.0
 */
public class MsgFrame extends AbstractEntity {
	private long id;
	private int bizType;// 业务类型ID
	private String bizTypeName;// 业务类型名称
	private int state;// 状态(0:等待发送,1:处理中,2:处理完成,3:其它不确定状态,4:被抛弃不会发送出去)
	private String msgPackId;// 当前帧的父包
	private int enterpriseId;// 企业ID
	private String assignSpecsvsId;// 分配发送端口号ID (虚拟特服号)
	private int actualSpecsvsId;// 实际发送端口号ID (实际通道)
	private String customNum;// 用户自扩展特服号
	private MsgType msgType;// 信息类型
	private SendType sendType;// 发送方式
	private int bizForm;// 信息帧业务类型(0:初始状态,1:黑名单,2:红名单,3:内容包含敏感字,4—区域指定发送帧,5:固定分配端口发送帧,6:系统分配端口发送帧,7:发送端指定端口发送帧
						// ,8:区域限制通道转发帧
						// ,9:非白名单通道转发帧,10:端口选择失败帧,11:号码非法帧,12:信息已过期帧,13:非白名单信息帧)
	private int msgCount;// 包含短消息条数(当前帧)
	private byte[] content;// 短信帧二进制数据 longblob
	private int level;// 运营商短信网关优先级
	private int priority;// 帧优先级1-5 级，黙认为3级
	private boolean stateReport;// 是否需要状态报告
	private Date postTime;// 用户提交时间
	private Date retrieveTime;// MTO取回处理时间点
	private Date commitTime;// 帧处理结束时间
	private Date deadline;// 处理时间期限
	private Date scheduleTime;// 计划发送时间
	private Date boeTime;// 发送时间段 开始
	private Date eoeTime;// 发送时间段 结束
	private String parameters;// 扩展属性
	private int version;//
	private int userId;// 用户ID
	private String userName;// 用户名
	private String smilContent;// 彩信的smil内容 longtext
	private String title;// 彩信标题
	private List<MsgTicket> tickets;// 当前帧短信话单

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBizType() {
		return bizType;
	}

	public void setBizType(int bizType) {
		this.bizType = bizType;
	}

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMsgPackId() {
		return msgPackId;
	}

	public void setMsgPackId(String msgPackId) {
		this.msgPackId = msgPackId;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getAssignSpecsvsId() {
		return assignSpecsvsId;
	}

	public void setAssignSpecsvsId(String assignSpecsvsId) {
		this.assignSpecsvsId = assignSpecsvsId;
	}

	public int getActualSpecsvsId() {
		return actualSpecsvsId;
	}

	public void setActualSpecsvsId(int actualSpecsvsId) {
		this.actualSpecsvsId = actualSpecsvsId;
	}

	public String getCustomNum() {
		return customNum;
	}

	public void setCustomNum(String customNum) {
		this.customNum = customNum;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public void setMsgTypeId(int msgTypeId) {
		this.msgType = MsgType.getType(msgTypeId);
	}

	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}

	public void setSendTypeId(int sendTypeId) {
		this.sendType = SendType.getType(sendTypeId);
	}

	public int getBizForm() {
		return bizForm;
	}

	public void setBizForm(int bizForm) {
		this.bizForm = bizForm;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isStateReport() {
		return stateReport;
	}

	public void setStateReport(boolean stateReport) {
		this.stateReport = stateReport;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getRetrieveTime() {
		return retrieveTime;
	}

	public void setRetrieveTime(Date retrieveTime) {
		this.retrieveTime = retrieveTime;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Date getBoeTime() {
		return boeTime;
	}

	public void setBoeTime(Date boeTime) {
		this.boeTime = boeTime;
	}

	public Date getEoeTime() {
		return eoeTime;
	}

	public void setEoeTime(Date eoeTime) {
		this.eoeTime = eoeTime;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSmilContent() {
		return smilContent;
	}

	public void setSmilContent(String smilContent) {
		this.smilContent = smilContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<MsgTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<MsgTicket> tickets) {
		this.tickets = tickets;
	}

}

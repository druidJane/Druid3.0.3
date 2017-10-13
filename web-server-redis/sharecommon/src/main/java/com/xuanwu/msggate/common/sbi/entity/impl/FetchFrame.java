/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import static com.xuanwu.msggate.common.util.DateUtil.tranDate;

import java.util.Date;
import java.util.UUID;

import com.google.protobuf.ByteString;
import com.xuanwu.msggate.common.protobuf.CommonItem.BizMeta;
import com.xuanwu.msggate.common.protobuf.mto.MTOResponse.OMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame.FrameState;
import com.xuanwu.msggate.common.sbi.entity.MsgPack.SendType;
import com.xuanwu.msggate.common.sbi.entity.SpecSVSNumber;
import com.xuanwu.msggate.common.util.UUIDUtil;

/**
 * Fetch frame temp
 * 
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-12
 * @Version 1.0.0
 */
public class FetchFrame {
	private Long id;
	private String packId;
	private int userId;
	private int enterpriseId;
	private int assignChannelId;
	private int assignSpecsvsId;
	private int retrieveChannelId;
	private int actualChannelId;
	private int actualSpecsvsId;

	private String customNum;
	private int msgCount;
	private SendType sendType;
	private MsgType msgType;
	private byte[] content;
	private int priority;
	private int level;
	private FrameState state;
	private boolean stateReport;

	private Date retrieveTime;
	private Date deadline;
	private Date scheduleTime;
	private Date boeTime;
	private Date eoeTime;
	private Date postTime;
	private Date commitTime;
	private String parameters;
	private int version;
	private int bizType;
	
	private String suffix;
	
	/**
	 * The actual special number to send
	 */
	private String specNumber;
	
	private SpecSVSNumber actualSpecNum;

	public FetchFrame() {
	}

	public FetchFrame(OMsgFrame frame) {
		id = frame.getFrameID();
		packId = UUIDUtil.tranBuilder2UUID(frame.getPackID()).toString();
		userId = frame.getUserID();
		enterpriseId = frame.getEnterpriseID();
	    actualChannelId = frame.getActualChannelId();
		initMeta(frame.getMeta());
		content = frame.getPack().toByteArray();
	}

	private void initMeta(BizMeta meta) {
		sendType = SendType.values()[meta.getSendType()];
		msgType = MsgType.getType(meta.getMsgType());
		msgCount = meta.getMsgCount();
		specNumber = meta.getSpecNum();
		level = meta.getLevel();
		customNum = meta.getCustomNum();
		bizType = meta.getBizType();

		retrieveTime = tranDate(meta.getRetrieveTime());
		postTime = tranDate(meta.getPostTime());
		scheduleTime = tranDate(meta.getScheduleTime());
		deadline = tranDate(meta.getDeadline());
		boeTime = tranDate(meta.getBoeTime());
		eoeTime = tranDate(meta.getEoeTime());
		stateReport = meta.getReportState();
		parameters = meta.getParameters();
	}

	public OMsgFrame buildFrame() {
		OMsgFrame.Builder builder = OMsgFrame.newBuilder();
		builder.setFrameID(id);
		builder.setPackID(UUIDUtil.tranUUID2Builder(UUID.fromString(packId)));
		builder.setUserID(userId);
		builder.setEnterpriseID(enterpriseId);
		builder.setActualChannelId(actualChannelId);
		builder.setMeta(buildMeta());
		builder.setPack(ByteString.copyFrom(content));
		return builder.build();
	}

	private BizMeta buildMeta() {
		BizMeta.Builder build = BizMeta.newBuilder();
		build.setSendType(sendType.ordinal());
		build.setMsgType(msgType.getIndex());
		build.setMsgCount(getMsgCount());
		if (specNumber == null) {
			throw new RuntimeException(
					"A frame Assigned none special service number when send frame");
		}
		build.setSpecNum(specNumber);
		build.setLevel(level);
		build.setBizType(bizType);

		build.setCustomNum((customNum == null) ? "" : customNum);
		build.setPostTime(postTime == null ? 0 : postTime.getTime());
		build.setScheduleTime(scheduleTime == null ? 0 : scheduleTime
						.getTime());
		build.setDeadline(deadline == null ? 0 : deadline.getTime());
		build.setBoeTime(boeTime == null ? 0 : boeTime.getTime());
		build.setEoeTime(eoeTime == null ? 0 : eoeTime.getTime());
		build.setRetrieveTime(retrieveTime == null ? 0 : retrieveTime
						.getTime());

		build.setReportState(stateReport);
		build.setParameters(parameters);
		return build.build();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public int getAssignChannelId() {
		return assignChannelId;
	}

	public void setAssignChannelId(int assignChannelId) {
		this.assignChannelId = assignChannelId;
	}

	public int getAssignSpecsvsId() {
		return assignSpecsvsId;
	}

	public void setAssignSpecsvsId(int assignSpecsvsId) {
		this.assignSpecsvsId = assignSpecsvsId;
	}

	public String getCustomNum() {
		return customNum;
	}

	public void setCustomNum(String customNum) {
		this.customNum = customNum;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public Date getRetrieveTime() {
		return retrieveTime;
	}

	public void setRetrieveTime(Date retrieveTime) {
		this.retrieveTime = retrieveTime;
	}

	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = SendType.getType(sendType);
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = MsgType.getType(msgType);
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public FrameState getState() {
		return state;
	}

	public void setStateIndex(int state) {
		this.state = FrameState.getState(state);
	}

	public void setState(FrameState state) {
		this.state = state;
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

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
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

	public String getSpecNumber() {
		return specNumber;
	}

	public void setSpecNumber(String specNumber) {
		this.specNumber = specNumber;
	}

	public int getRetrieveChannelId() {
		return retrieveChannelId;
	}

	public void setRetrieveChannelId(int retrieveChannelId) {
		this.retrieveChannelId = retrieveChannelId;
	}

	public int getActualChannelId() {
		return actualChannelId;
	}

	public void setActualChannelId(int actualChannelId) {
		this.actualChannelId = actualChannelId;
	}

	public int getActualSpecsvsId() {
		return actualSpecsvsId;
	}

	public void setActualSpecsvsId(int actualSpecsvsId) {
		this.actualSpecsvsId = actualSpecsvsId;
	}

	public boolean isStateReport() {
		return stateReport;
	}

	public void setStateReport(boolean stateReport) {
		this.stateReport = stateReport;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public int getUserId() {
		return userId;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void incrementVersion() {
		version += 1;
	}

	public int getBizType() {
		return bizType;
	}

	public void setBizType(int bizType) {
		this.bizType = bizType;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public SpecSVSNumber getActualSpecNum() {
		return actualSpecNum;
	}

	public void setActualSpecNum(SpecSVSNumber actualSpecNum) {
		this.actualSpecNum = actualSpecNum;
	}
}

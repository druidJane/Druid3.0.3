/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import com.xuanwu.msggate.common.protobuf.CommonItem.BizMeta;
import com.xuanwu.msggate.common.protobuf.CommonItem.GroupFrame;
import com.xuanwu.msggate.common.protobuf.CommonItem.MassFrame;
import com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem;
import com.xuanwu.msggate.common.protobuf.mt.MTRequest.TRequest;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame.FrameState;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.NonWhiteList;
import com.xuanwu.msggate.common.util.DateUtil;
import com.xuanwu.msggate.common.util.UUIDUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Centrum message pack entity
 * 
 * @author <a href="mailto:wanglianguang@139130.net">Guang Wang</a>
 * @Data 2010-5-7
 * @Version 1.0.0
 */
public class PMsgPack implements MsgPack {
	private PackType packPType;

	@Override
	public PackType getPackType() {
		return packPType;
	}

	@Override
	public void setPackType(PackType packType) {
		this.packPType = packType;
	}

	public UUID uuid;

	// public UUID batchID;

	/**
	 * The message typeHandler
	 */
	private MsgContent.MsgType msgType;

	/**
	 * The send type
	 */
	private SendType type;

	/**
	 * The post time
	 */
	private Date postTime;
	/**
	 * The schedule time
	 */
	private Date scheduleTime;
	/**
	 * Deadline
	 */
	private Date deadline;

	/**
	 * Special service number
	 */
	private String specNum;

	/**
	 * Extend service number
	 */
	private String customNum;

	/**
	 * BOE time
	 */
	private Date boeTime;

	/**
	 * EOE time
	 */
	private Date eoeTime;

	/**
	 * The priority
	 */
	private int priority;
	/**
	 * The level of the message for the carrier
	 */
	private int level;

	/**
	 * The frames of the pack
	 */
	private List<MsgFrame> frames = new ArrayList<MsgFrame>();

	/**
	 * Version
	 */
	private int version;

	/**
	 * pack state
	 */
	private PackState state = PackState.INIT;
	private PackState oldState = PackState.INIT;

	private List<com.xuanwu.msggate.common.sbi.entity.MediaItem> mmsAttachments;

	private MmsNeedApprovalType mmsNeedApproval;

	private int userID;

	private int enterpriseID;

	private int bizType;

	private String batchName;

	private boolean distinct;

	private String from;

	private String remark;

	private List<NonWhiteList> nonWhiteLists;

	private long offerTime;

	private String userName;

	private String password;

	private boolean isAudit;

	private int failTime;
	
	/**
	 * Parameters associated with the frame
	 */
	private Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * Default constructor
	 */
	public PMsgPack() {
	}

	/**
	 * Default MTRequest constructor
	 * 
	 * @param request
	 */
	public PMsgPack(TRequest request) {
		// batchID = UUIDUtil.tranBuilder2UUID(request.getBatchID());
		packPType = PackType.MESSAGE;
		uuid = UUIDUtil.tranBuilder2UUID(request.getUuid());
		type = SendType.values()[request.getInfo().getSendType()];
		msgType = MsgContent.MsgType.getType(request.getInfo().getMsgType());

		specNum = request.getSpecNum();
		customNum = request.getCustomNum();

		scheduleTime = DateUtil.tranDate(request.getInfo().getScheduleTime());
		// deadline = DateUtil.tranDate(request.getInfo().getDeadline());
		deadline = null;// TODO: Cancel set deadline
		eoeTime = DateUtil.tranDate(request.getInfo().getEoeTime());
		boeTime = DateUtil.tranDate(request.getInfo().getBoeTime());
		priority = request.getInfo().getPriority();
		level = request.getInfo().getLevel();
		bizType = request.getInfo().getBizType();
		batchName = request.getBatchName();
		distinct = request.getDistinctFlag();
		from = request.getFrom();
		remark = request.getRemark();

		setMmsAttachmentsProtobuf(request.getMmsAttachmentList());
		if (type == SendType.MASS) {
			addFrame(new MassMsgFrame(request.getInfo(), request.getMassMsg()));
		} else {
			addFrame(new GroupMsgFrame(request.getInfo(), request.getGroupMsg()));
		}
	}

	public List<MsgFrame> splitGroupFrames(SendType sType,
			MsgContent.MsgType mType, List<GroupFrame> groupFramesList) {
		List<MsgFrame> temp = new ArrayList<MsgFrame>();
		for (Iterator<GroupFrame> iterator = groupFramesList.iterator(); iterator
				.hasNext();) {
			GroupFrame groupFrame = (GroupFrame) iterator.next();

			temp.add(new GroupMsgFrame(groupFrame.getPid(), groupFrame
					.getMeta(), groupFrame.getPack()));
		}
		return temp;
	}

	public List<MsgFrame> splitMassFrames(SendType sType,
			MsgContent.MsgType mType, List<MassFrame> massFramesList) {
		List<MsgFrame> temp = new ArrayList<MsgFrame>();
		for (Iterator<MassFrame> iterator = massFramesList.iterator(); iterator
				.hasNext();) {
			MassFrame massFrame = (MassFrame) iterator.next();
			temp.add(new MassMsgFrame(massFrame.getPid(), massFrame.getMeta(),
					massFrame.getPack()));
		}
		return temp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MsgFrame> getFrames() {
		return frames;
	}

	@Override
	public void removeFrame(MsgFrame frame) {
		frames.remove(frame);
	}

	@Override
	public SendType getSendType() {
		return type;
	}

	public void setSendTypeIndex(int index) {
		this.type = SendType.getType(index);
	}

	@Override
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public void setUuid(UUID uid) {
		this.uuid = uid;
	}

	@Override
	public Date getPostTime() {
		return postTime;
	}

	@Override
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date line) {
		this.deadline = line;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int getFrameCount() {
		return frames.size();
	}

	@Override
	public int getMsgCount() {
		int count = 0;
		for (MsgFrame frame : frames) {
			count += frame.getMsgCount();
		}
		return count;
	}

	@Override
	public MsgContent.MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgContent.MsgType type) {
		this.msgType = type;
	}

	public void setMsgTypeIndex(int msgTypeIndex) {
		this.msgType = MsgContent.MsgType.getType(msgTypeIndex);
	}

	@Override
	public Date getScheduleTime() {
		return scheduleTime;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void addFrame(MsgFrame frame) {
		frame.setPackUUID(uuid);
		frames.add(frame);
	}

	@Override
	public String getCustomNum() {
		return customNum;
	}

	public void setCustomNum(String customNum) {
		this.customNum = customNum;
	}

	@Override
	public String getSpecNum() {
		return specNum;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setFrames(List<MsgFrame> fames) {
		for (MsgFrame msgFrame : fames) {
			msgFrame.setPackUUID(uuid);
		}
		this.frames = fames;
	}

	@Override
	public BizMeta buildMeta() {
		// TODO: 这个可能不需要了
		BizMeta.Builder build = BizMeta.newBuilder();
		build.setSendType(type.ordinal());
		build.setMsgType(msgType.getIndex());
		build.setPostTime(postTime == null ? 0 : postTime.getTime());
		build.setBoeTime(boeTime == null ? 0 : boeTime.getTime());
		build.setEoeTime(eoeTime == null ? 0 : eoeTime.getTime());
		build.setScheduleTime(scheduleTime == null ? 0 : scheduleTime.getTime());
		build.setDeadline(deadline == null ? 0 : deadline.getTime());
		return build.build();
	}

	/**
	 * Set type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(SendType type) {
		this.type = type;
	}

	public void setTypeIndex(int typeIndex) {
		this.type = MsgPack.SendType.getType(typeIndex);
	}

	@Override
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	/**
	 * Set scheduleTime
	 * 
	 * @param scheduleTime
	 *            the scheduleTime to set
	 */
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	/**
	 * Set specNum
	 * 
	 * @param specNum
	 *            the specNum to set
	 */
	public void setSpecNum(String specNum) {
		this.specNum = specNum;
	}

	/**
	 * Set extNum
	 * 
	 * @param extNum
	 *            the extNum to set
	 */
	public void setExtNum(String extNum) {
		this.customNum = extNum;
	}

	/**
	 * Set version
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
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

	@Override
	public int getUserID() {
		return userID;
	}

	@Override
	public void setUserID(int userID) {
		this.userID = userID;
	}

	@Override
	public int getEnterpriseID() {
		return enterpriseID;
	}

	@Override
	public void setEnterpriseID(int enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	@Override
	public int getValidFrameCount() {
		int i = 0;
		for (MsgFrame frame : frames) {
			if (frame.getState() != FrameState.ABANDON)
				i++;
		}
		return i;
	}

	@Override
	public int getValidTicketCount() {
		int i = 0;
		for (MsgFrame frame : frames) {
			if (frame.getState() != FrameState.ABANDON)
				i += frame.getMsgCount();
		}
		return i;
	}

	@Override
	public int getInValidTicketCount() {
		int i = 0;
		for (MsgFrame frame : frames) {
			if (frame.getState() == FrameState.ABANDON)
				i += frame.getMsgCount();
		}
		return i;
	}

	@Override
	public PackState getState() {
		return state;
	}

	@Override
	public void setState(PackState state) {
		this.state = state;
	}

	public void setStateIndex(int stateIndex) {
		this.state = PackState.getState(stateIndex);
	}

	@Override
	public PackState getOldState() {
		return oldState;
	}

	@Override
	public void setOldState(PackState oldState) {
		this.oldState = oldState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xuanwu.msggate.common.sbi.entity.MsgPack#getBatchID()
	 */
	/*
	 * @Override public UUID getBatchID() { // TODO Auto-generated method stub
	 * return this.batchID; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xuanwu.msggate.common.sbi.entity.MsgPack#setBatchID(java.util.UUID)
	 */
	/*
	 * @Override public void setBatchID(UUID batchID) { this.batchID = batchID;
	 * }
	 */

	@Override
	public void setMmsAttachmentsProtobuf(List<MediaItem> mmsAttachments) {
		List<com.xuanwu.msggate.common.sbi.entity.MediaItem> newmMediaItems = new ArrayList<com.xuanwu.msggate.common.sbi.entity.MediaItem>();
		for (MediaItem mediaItem : mmsAttachments) {
			PMediaItem pMediaItem = new PMediaItem(mediaItem.getMediaType(),
					mediaItem.getMeta(), mediaItem.getData().toByteArray());
			newmMediaItems.add(pMediaItem);
		}
		this.mmsAttachments = newmMediaItems;
	}

	@Override
	public List<com.xuanwu.msggate.common.sbi.entity.MediaItem> getMmsAttachments() {
		return mmsAttachments;
	}

	@Override
	public void setMmsAttachments(
			List<com.xuanwu.msggate.common.sbi.entity.MediaItem> mmsAttachments) {
		this.mmsAttachments = mmsAttachments;
	}

	@Override
	public MmsNeedApprovalType getMmsNeedApproval() {
		return mmsNeedApproval;
	}

	@Override
	public void setMmsApprovalStatus(int type) {
		this.mmsNeedApproval = MmsNeedApprovalType.getType(type);
	}

	@Override
	public void setBizType(int bizType) {
		this.bizType = bizType;
	}

	@Override
	public int getBizType() {
		return bizType;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	@Override
	public String getBatchName() {
		return batchName;
	}

	@Override
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	@Override
	public String getFrom() {
		return from;
	}

	public List<NonWhiteList> getNonWhiteLists() {
		return nonWhiteLists;
	}

	public void setNonWhiteLists(List<NonWhiteList> nonWhiteLists) {
		this.nonWhiteLists = nonWhiteLists;
	}

	@Override
	public long getOfferTime() {
		return offerTime;
	}

	@Override
	public void setOfferTime(long offerTime) {
		this.offerTime = offerTime;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public void addParameter(String key, String value) {
		parameters.put(key, value);
	}

	@Override
	public Object getParameter(String key) {
		return parameters.get(key);
	}

	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(Map<String, Object> map) {
		for (String s : map.keySet()) {
			parameters.put(s, map.get(s));
		}
	}

	public boolean isAudit() {
		return isAudit;
	}

	public void setAudit(boolean isAudit) {
		this.isAudit = isAudit;
	}

	public int getFailTime() {
		return failTime;
	}

	public void setFailTime(int failTime) {
		this.failTime = failTime;
	}
	
	public void addFailTime(){
		failTime++;
	}
	
	@Override
	public String toString() {
		return "PMsgPack [uuid=" + uuid + ", msgType=" + msgType + ", type="
				+ type + ", postTime=" + postTime + ", scheduleTime="
				+ scheduleTime + ", deadline=" + deadline + ", specNum="
				+ specNum + ", customNum=" + customNum + ", boeTime=" + boeTime
				+ ", eoeTime=" + eoeTime + ", priority=" + priority
				+ ", level=" + level + ", frames=" + frames + ", version="
				+ version + ", state=" + state + ", mmsAttachments="
				+ mmsAttachments + ", mmsNeedApproval=" + mmsNeedApproval
				+ ", enterpriseID=" + enterpriseID + ", bizType=" + bizType
				+ ", batchName=" + batchName + ", distinct=" + distinct + "]";
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

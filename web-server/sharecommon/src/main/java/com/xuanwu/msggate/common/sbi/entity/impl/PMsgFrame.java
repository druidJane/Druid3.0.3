/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import com.xuanwu.msggate.common.protobuf.CommonItem.BizMeta;
import com.xuanwu.msggate.common.sbi.entity.BindSpecNumResult;
import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack.SendType;
import com.xuanwu.msggate.common.sbi.entity.SpecSVSNumber;
import com.xuanwu.msggate.common.sbi.entity.Ticket;
import com.xuanwu.msggate.common.util.DateUtil;
import com.xuanwu.msggate.common.util.ListUtil;
import com.xuanwu.msggate.common.util.XmlUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-12
 * @Version 1.0.0
 */
public abstract class PMsgFrame implements MsgFrame {

	/**
	 * Logger for this class
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger(PMsgFrame.class);

	/**
	 * Parent pack UUID
	 */
	protected UUID packUUID;

	protected int userID;

	protected int enterpriseID;
	
	protected int actualChannelId;

	/**
	 * Identity of the frame
	 */
	protected Long id;
	/**
	 * The message type
	 */
	protected MsgContent.MsgType msgType;

	/**
	 * Extend number
	 */
	protected String customNum;

	/**
	 * The post time of the frame
	 */
	protected Date postTime;

	/**
	 * Commit the send time
	 */
	protected Date commitTime;

	/**
	 * The schedule time
	 */
	protected Date scheduleTime;

	/**
	 * The time of retrieve
	 */
	protected Date retrieveTime;

	/**
	 * The deadline of the frame
	 */
	protected Date deadline;

	/**
	 * The priority
	 */
	protected int priority;

	protected BizForm bizForm = BizForm.NORMAL;

	/**
	 * The state of the frame
	 */
	protected FrameState state = FrameState.WAIT;

	/**
	 * Begin of time range
	 */
	protected Date boeTime;

	/**
	 * End of time range
	 */
	protected Date eoeTime;

	/**
	 * State report state
	 */
	protected boolean isStateReport;

	/**
	 * Version
	 */
	protected int version;
	
	protected int bizType;
	
	/**
	 * Parameters associated with the frame
	 */
	protected Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * Template attributes
	 */
	protected Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * mms smil content
	 */
	protected String smil;
	/**
	 * mms title
	 */
	protected String title;
	
	protected boolean isPreCommit = false;
	
	public PMsgFrame() {
	}

	/**
	 * Default constructor
	 * 
	 * @param fUUID
	 * @param sType
	 * @param mType
	 * @param meta
	 */
	public PMsgFrame(Long pid, MsgContent.MsgType mType, BizMeta meta) {
		this.id = pid;
		this.msgType = mType;
		this.customNum = meta.getCustomNum();
		this.postTime = DateUtil.tranDate(meta.getPostTime());
		this.scheduleTime = DateUtil.tranDate(meta.getScheduleTime());
//		this.deadline = DateUtil.tranDate(meta.getDeadline());
		this.deadline = null;
		this.boeTime = DateUtil.tranDate(meta.getBoeTime());
		this.eoeTime = DateUtil.tranDate(meta.getEoeTime());
		this.state = FrameState.WAIT;
		this.isStateReport = meta.getReportState();
		this.priority = meta.getPriority();
		this.level = meta.getLevel();
		this.bizType = meta.getBizType();

		if (StringUtils.isNotEmpty(meta.getParameters())) {
			this.parameters = (Map<String, Object>) XmlUtil.fromXML(meta
					.getParameters());
			this.parameters.put("originPackId", this.packUUID.toString());
		}
	}

	public PMsgFrame(FetchFrame frame) {
		this.id = frame.getId();
		this.msgType = frame.getMsgType();
		this.specNumber = frame.getSpecNumber();
		this.customNum = frame.getCustomNum();
		this.retrieveTime = frame.getRetrieveTime();
		this.postTime = frame.getPostTime();
		this.scheduleTime = frame.getScheduleTime();
//		this.deadline = frame.getDeadline();
		this.deadline = null;
		this.boeTime = frame.getBoeTime();
		this.eoeTime = frame.getBoeTime();
		this.state = frame.getState();
		this.isStateReport = frame.isStateReport();
		this.priority = frame.getPriority();
		this.level = frame.getLevel();
		this.bizType = frame.getBizType();
		this.userID = frame.getUserId();
		this.enterpriseID = frame.getEnterpriseId();
		this.actualChannelId = frame.getActualChannelId();
		this.packUUID = UUID.fromString(frame.getPackId());
		//this.batchID = UUID.fromString(frame.getBatchId());
		if (StringUtils.isNotEmpty(frame.getParameters())) {
			this.parameters = (Map<String, Object>) XmlUtil.fromXML(frame
					.getParameters());
			this.parameters.put("originPackId", this.packUUID.toString());
		}
	}

	@Override
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MsgContent.MsgType getMsgType() {
		return msgType;
	}

	@Override
	public void setMsgType(MsgContent.MsgType type) {
		this.msgType = type;
	}

	public void setMsgTypeIndex(int index) {
		this.msgType = MsgContent.MsgType.getType(index);
	}

	@Override
	public Date getPostTime() {
		return postTime;
	}

	@Override
	public void setPostTime(Date time) {
		this.postTime = time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date time) {
		this.scheduleTime = time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FrameState getState() {
		return state;
	}

	@Override
	public void setState(FrameState state) {
		this.state = state;
	}

	public void setStateIndex(int index) {
		this.state = FrameState.getState(index);
	}

	@Override
	public Date getRetrieveTime() {
		return retrieveTime;
	}

	@Override
	public void setRetrieveTime(Date time) {
		this.retrieveTime = time;
	}

	@Override
	public String getCustomNum() {
		return customNum;
	}

	@Override
	public void setCustomNum(String extendNum) {
		this.customNum = extendNum;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	protected BizMeta.Builder buildMeta(SendType type) {
		BizMeta.Builder build = BizMeta.newBuilder();
		build.setSendType(type.ordinal());
		build.setMsgType(msgType.getIndex());
		build.setMsgCount(getMsgCount());
		if (StringUtils.isBlank(assignSpecIDs) || StringUtils.isBlank(assignChannelIDs)) {
			throw new RuntimeException("A frame Assigned none special service number when send frame");
		}
		
		build.setAssignSpecIDs(assignSpecIDs);
		build.setAssignChannelIDs(assignChannelIDs);

		build.setPriority(priority);
		build.setLevel(level);
		build.setCustomNum((customNum == null) ? "" : customNum);

		build.setPostTime(postTime == null ? 0 : postTime.getTime());
		build.setScheduleTime(scheduleTime == null ? 0 : scheduleTime
						.getTime());
		build.setDeadline(deadline == null ? 0 : deadline.getTime());
		build.setBoeTime(boeTime == null ? 0 : boeTime.getTime());
		build.setEoeTime(eoeTime == null ? 0 : eoeTime.getTime());
		build.setReportState(isStateReport);
		build.setBizType(bizType);
		
		build.setParameters(XmlUtil.toXML(parameters));
		return build;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param proto
	 */
	protected void copyBasicInfo(PMsgFrame target) {
		target.setBindSpecResults(getBindSpecResults());
		target.setBizForm(getBizForm());
		target.setBoeTime(getBoeTime());
		target.setCustomNum(getCustomNum());
		target.setDeadline(getDeadline());
		target.setEoeTime(getEoeTime());
		target.setReportState(getReportState());
		target.setLevel(getLevel());
		target.setMsgType(getMsgType());
		target.setPackUUID(getPackUUID());
		target.setParameters(getParameters());
		target.setPostTime(getPostTime());
		target.setPriority(getPriority());
		target.setScheduleTime(getScheduleTime());
		target.setState(getState());
		target.setVersion(getVersion());
		target.setBizType(getBizType());
		target.setSmil(getSmil());
		target.setTitle(getTitle());
	}

	@Override
	public UUID getPackUUID() {
		return packUUID;
	}

	@Override
	public void setPackUUID(UUID packUUID) {
		this.packUUID = packUUID;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
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
	public boolean getReportState() {
		return isStateReport;
	}

	@Override
	public void setReportState(boolean flag) {
		this.isStateReport = flag;
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
		this.parameters = map;
	}

	/**
	 * Tickets associated with the frame
	 */
	protected List<Ticket> tickets = new ArrayList<Ticket>();

	/**
	 * The level of the message for the carrier
	 */
	protected int level;

	/**
	 * The assigned special service number
	 */
	protected List<SpecSVSNumber> assignSpecNums;
	
	protected List<CarrierChannel> assignChannels = new ArrayList<CarrierChannel>();

	/**
	 * The actual send special service number
	 */
	protected SpecSVSNumber actualSpecNum;

	/**
	 * The whole special service number
	 */
	protected String specNumber;

	/**
	 * bind special number
	 */
	protected List<BindSpecNumResult> bindSpecResults;
	
	protected String assignSpecIDs;
	
	protected String assignChannelIDs;

	@Override
	public List<Ticket> getTickets() {
		return tickets;
	}

	@Override
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public Date getCommitTime() {
		return commitTime;
	}

	@Override
	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	@Override
	public BizForm getBizForm() {
		return bizForm;
	}

	@Override
	public void setBizForm(BizForm form) {
		this.bizForm = form;
	}
	
	public void setBizFormIndex(int index){
		this.bizForm = BizForm.getForm(index);
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setAssignSpecNums(List<SpecSVSNumber> specNums) {
		this.assignSpecNums = specNums;
		if(specNums == null || specNums.size() == 0){
			this.assignSpecIDs = null;
			this.assignChannelIDs = null;
		} else {
			StringBuilder sb = new StringBuilder();
			StringBuilder sbCh = new StringBuilder();
			List<Integer> channelIDs = new ArrayList<Integer>(specNums.size());
			ListUtil.clearList(assignChannels);
			for(SpecSVSNumber spec : specNums){
				Integer retrieveChannelID = spec.getCarrierChannel().getRetrieveId();
				if(!channelIDs.contains(retrieveChannelID)){
					channelIDs.add(retrieveChannelID);
					assignChannels.add(spec.getCarrierChannel());
					sb.append(spec.getId()).append(SPEC_IDS_SPLIT_STR);
					sbCh.append(retrieveChannelID).append(SPEC_IDS_SPLIT_STR);
				}
			}
			this.assignSpecIDs = sb.substring(0, sb.length() - 1);
			this.assignChannelIDs = sbCh.substring(0, sbCh.length() - 1);
		}
	}

	public void setBindSpecResults(List<BindSpecNumResult> bindSpecResults) {
		this.bindSpecResults = bindSpecResults;
		if(bindSpecResults == null){
			setAssignSpecNums(null);
		} else {
			List<SpecSVSNumber> specs = new ArrayList<SpecSVSNumber>();
			for(BindSpecNumResult bindSpec : bindSpecResults){
				if(bindSpec.getSpecSVSNum() != null)
					specs.add(bindSpec.getSpecSVSNum());
			}
			setAssignSpecNums(specs);
		}
	}

	public List<BindSpecNumResult> getBindSpecResults() {
		return bindSpecResults;
	}

	public List<SpecSVSNumber> getAssignSpecNums() {
		return assignSpecNums;
	}

	@Override
	public List<CarrierChannel> getAssignChannels() {
		/*if (assignSpecNums != null && assignSpecNums.size() > 0){
			List<CarrierChannel> chs = new ArrayList<CarrierChannel>();
			for(SpecSVSNumber spec : assignSpecNums){
				chs.add(spec.getCarrierChannel());
			}
			return chs;
		}
		return Collections.emptyList();*/
		return assignChannels;
	}

	@Override
	public void setActualSpecNum(SpecSVSNumber specNum) {
		this.actualSpecNum = specNum;
	}

	@Override
	public SpecSVSNumber getActualSpecNum() {
		return actualSpecNum;
	}

	@Override
	public String getSpecNumber() {
		return specNumber;
	}

	@Override
	public void setSpecNumber(String specNumber) {
		this.specNumber = specNumber;
	}

	@Override
	public String getWholeSpecNumber() {
		return specNumber + customNum;
	}

	@Override
	public boolean isAssignedSpecNumber() {
		return bindSpecResults == null ? false : true;
	}

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
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
	public int getUserID() {
		 return userID;
	}

	@Override
	public void setUserID(int userID) {
         this.userID = userID;		
	}

	@Override
	public int getBizType() { 
		return bizType;
	}

	@Override
	public void setBizType(int bizType) {
		this.bizType = bizType;
	}

	@Override
	public String getAssignSpecIDs() {
		return assignSpecIDs;
	}

	public void setAssignSpecIDs(String assignSpecIDs) {
		this.assignSpecIDs = assignSpecIDs;
	}

	@Override
	public String getAssignChannelIDs() {
		return assignChannelIDs;
	}

	public void setAssignChannelIDs(String assignChannelIDs) {
		this.assignChannelIDs = assignChannelIDs;
	}
	
	@Override
	public int getActualChannelId() {
		return actualChannelId;
	}

	@Override
	public void setActualChannelId(int actualChannelId) {
		this.actualChannelId = actualChannelId;
	}
	
	@Override
	public String getSmil(){
		return smil;
	}
	
	@Override
	public void setSmil(String smil){
		this.smil = smil;
	}
	
	@Override
	public String getTitle(){
		return title;
	}
	
	@Override
	public void setTitle(String title){
		this.title = title;
	}
	
	@Override
	public boolean isPreCommit() {
		return isPreCommit;
	}
	
	@Override
	public void setPreCommit(boolean isPreCommit) {
		this.isPreCommit = isPreCommit;
	}

	@Override
	public String toString() {
		return "PMsgFrame [packUUID=" + packUUID + ", enterpriseID="
				+ enterpriseID + ", id=" + id + ", msgType=" + msgType
				+ ", customNum=" + customNum + ", postTime=" + postTime
				+ ", commitTime=" + commitTime + ", scheduleTime="
				+ scheduleTime + ", retrieveTime=" + retrieveTime
				+ ", deadline=" + deadline + ", priority=" + priority
				+ ", bizForm=" + bizForm + ", state=" + state + ", boeTime="
				+ boeTime + ", eoeTime=" + eoeTime + ", isStateReport="
				+ isStateReport + ", version=" + version + ", bizType="
				+ bizType + ", parameters=" + parameters + ", attributes="
				+ attributes + ", tickets=" + tickets + ", level=" + level
				+ ", assignSpecNums=" + assignSpecNums + ", actualSpecNum="
				+ actualSpecNum + ", specNumber=" + specNumber
				+ ", bindSpecResults=" + bindSpecResults + ", assignSpecIDs="
				+ assignSpecIDs + ", assignChannelIDs=" + assignChannelIDs
				+ "]";
	}
}

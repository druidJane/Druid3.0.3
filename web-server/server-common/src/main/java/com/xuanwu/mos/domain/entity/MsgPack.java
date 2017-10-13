/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.DateUtil.DateTimeType;
import com.xuanwu.mos.utils.StringUtil;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgPack.SendType;

import java.util.Date;
import java.util.List;

/**
 * @Description: 批次(短信包)实体类
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2012-9-26
 * @Version 1.0.0
 */
public class MsgPack extends BaseEntity {
	private String id;
	private int bizType;// 业务类型ID
	private String bizTypeName;// 业务类型名称
	private int state;// 0:INIT(待发送);1:AUDITING(待审核);2:ABANDON(被丢弃);3:CANCEL(取消)
	private String batchName;// 批次名称
	private int enterpriseId;// 企业ID
	private String enterpriseName;// 企业名称
	private int deptId;// 部门ID
	private String deptName;// 部门名称
	private SendType sendType;// 发送类型,0:群发;1:组发
	private MsgType msgType;// 信息类型,1:短信;2:彩信;3:WAP_PUSH;8:语音通知；9:语音验证码
	private int validFrameCount;// 有效信息帧数量
	private String specNum;// 指定的特服号码
	private String customNum;// 用户扩展特服号码
	private Date scheduleTime;// 计划发送时间
	private Date deadline;// 处理时间期限
	private Date boeTime;// 发送时间段开始时间
	private Date eoeTime;// 发送时间段结束时间
	private Date postTime;// 包提交时间
	private Date commitTime;// 包处理完成时间
	private int version;//
	private String commitFrom;// 标识是由SDK,ump提交过来的
	private String remark;// 备注
	private int userId;// 用户ID
	private String userName;// 用户名
	private int validTickets;// 信息包有效短信话单数
	private int invalidTickets;// 信息包无效短信话单数
	private int sendState;// 发送状态(0:待发送,1:发送中,2:发送完成,3:发送取消,5:待审核,6:暂停)
	private String sendStateStr;// 发送状态
	private String msgTypeStr;//信息类型
	private int sendedCount;// 已发送短信数
	private Date doneTime;// 完成时间
	private MsgFrameState frameState;// 帧属性值
	private List<MsgFrame> simpleFrames;// 帧列表
	private boolean isSelfSend;// 是否自己发送的
	private boolean multiAudit;// 是否需要多级审核
	private boolean auditRole;// 是否有审核权限
	private boolean showPass = true;//是否显示通过审核
	private String  sendDeptName; // 发送部门


	private int entId;
	private Date beginTime;
	private Date endTime;
	private boolean deptSub;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public void setMsgTypeId(int msgTypeId) {
		this.msgType = MsgType.getType(msgTypeId);
	}

	public int getValidFrameCount() {
		return validFrameCount;
	}

	public void setValidFrameCount(int validFrameCount) {
		this.validFrameCount = validFrameCount;
	}

	public String getSpecNum() {
		return specNum;
	}

	public void setSpecNum(String specNum) {
		this.specNum = specNum;
	}

	public String getCustomNum() {
		return customNum;
	}

	public void setCustomNum(String customNum) {
		this.customNum = customNum;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCommitFrom() {
		return commitFrom;
	}

	public void setCommitFrom(String commitFrom) {
		this.commitFrom = commitFrom;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public int getValidTickets() {
		return validTickets;
	}

	public void setValidTickets(int validTickets) {
		this.validTickets = validTickets;
	}

	public int getInvalidTickets() {
		return invalidTickets;
	}

	public void setInvalidTickets(int invalidTickets) {
		this.invalidTickets = invalidTickets;
	}

	public int getSendState() {
		if (state == 2) {// 发送取消
			sendState = 2;
		} else if (state == 3) {
			sendState = 3;
		} else if (state == 1) {// 待审核
			sendState = 7;
		} else if (state == 5) {// 第二次待审核
			sendState = 5;
		} else if (state == 4) {
			sendState = 6;
		}
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public String getSendStateStr() {
		if (sendStateStr == null) {
			int s = getSendState();
			switch (s) {
			case 0:
				sendStateStr = "待发送";
				break;
			case 1:
				sendStateStr = "发送中";
				break;
			case 2:
				sendStateStr = "发送完成";
				break;
			case 3:// 被取消的批次
				sendStateStr = "取消发送";
				break;
			case 5:
				sendStateStr = "待审核";
				break;
			case 6:
				sendStateStr = "暂停";
				break;
			case 7:
				sendStateStr = "待审核";
				break;
			default:
				break;
			}
		}
		return sendStateStr;
	}

	public void setSendStateStr(String sendStateStr) {
		this.sendStateStr = sendStateStr;
	}

	public String getMsgTypeStr() {
		if(msgTypeStr==null){
			int s = getMsgType().getIndex();
			switch(s){
				case 1:msgTypeStr = "短信";break;
				case 2:msgTypeStr = "彩信";break;
				case 3:msgTypeStr = "WAP_PUSH";break;
                case 8:msgTypeStr = "语音通知";break;
				case 9:msgTypeStr = "语音验证码";break;
				default: break;
			}
		}
		return msgTypeStr;
	}

	public void setMsgTypeStr(String msgTypeStr) {
		this.msgTypeStr = msgTypeStr;
	}

	public int getSendedCount() {
		return sendedCount;
	}

	public void setSendedCount(int sendedCount) {
		this.sendedCount = sendedCount;
	}

	public Date getDoneTime() {
		if (getSendState() == 3 || getSendState() == 2) {
			if (doneTime == null) {
				return postTime;
			}
		}
		return doneTime;
	}

	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}

	public MsgFrameState getFrameState() {
		return frameState;
	}

	public void setFrameState(MsgFrameState frameState) {
		if (frameState != null) {
			int min = frameState.getMinState();
			int max = frameState.getMaxState();
			if (min == 0 && max == 0) {
				this.sendState = 0;// 待发送
			} else if (min < 2 && max > 0) {
				this.sendState = 1;// 发送中
			} else if (min == 2) {
				this.sendState = 2;// 发送完成
				this.doneTime = frameState.getDoneTime();
			}
			this.sendedCount = frameState.getDoneCount();
		}
		this.frameState = frameState;
	}

	public List<MsgFrame> getSimpleFrames() {
		return simpleFrames;
	}

	public void setSimpleFrames(List<MsgFrame> simpleFrames) {
		this.simpleFrames = simpleFrames;
	}

	public boolean isSelfSend() {
		return isSelfSend;
	}

	public void setSelfSend(boolean isSelfSend) {
		this.isSelfSend = isSelfSend;
	}

	public boolean isMultiAudit() {
		return multiAudit;
	}

	public void setMultiAudit(boolean multiAudit) {
		this.multiAudit = multiAudit;
	}

	public boolean isAuditRole() {
		return auditRole;
	}

	public void setAuditRole(boolean auditRole) {
		this.auditRole = auditRole;
	}	

	public boolean isShowPass() {
		return showPass;
	}

	public void setShowPass(boolean showPass) {
		this.showPass = showPass;
	}

	public boolean isDeptSub() {
		return deptSub;
	}

	public void setDeptSub(boolean deptSub) {
		this.deptSub = deptSub;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSendDeptName() {
		return sendDeptName;
	}

	public void setSendDeptName(String sendDeptName) {
		this.sendDeptName = sendDeptName;
	}

	public String toJSON() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"id\":\"" + id + "\"");
		sb.append(",\"bizType\":" + bizType);
		sb.append(",\"bizTypeName\":\"" + StringUtil.fixJsonStr(bizTypeName)
				+ "\"");
		sb.append(",\"state\":" + state);
		sb.append(",\"batchName\":\"" + StringUtil.replaceHtml(StringUtil.fixJsonStr(batchName)) + "\"");
		sb.append(",\"enterpriseId\":" + enterpriseId);
		sb.append(",\"enterpriseName\":\""
				+ StringUtil.fixJsonStr(enterpriseName) + "\"");
		sb.append(",\"deptId\":\"" + deptId + "\"");
		sb.append(",\"deptName\":\"" + StringUtil.fixJsonStr(deptName) + "\"");
		sb.append(",\"sendType\":" + sendType.getIndex());
		sb.append(",\"msgType\":" + msgType.getIndex());
		sb.append(",\"msgTypeStr\":\"" + getMsgTypeStr() + "\"");
		sb.append(",\"validFrameCount\":" + validFrameCount);
		sb.append(",\"specNum\":\"" + specNum + "\"");
		sb.append(",\"customNum\":\"" + customNum + "\"");
		sb.append(",\"scheduleTime\":\""
				+ DateUtil.format(scheduleTime, DateTimeType.DateTime) + "\"");
		sb.append(",\"deadline\":\""
				+ DateUtil.format(deadline, DateTimeType.DateTime) + "\"");
		sb.append(",\"boeTime\":\""
				+ DateUtil.format(boeTime, DateTimeType.Time) + "\"");
		sb.append(",\"eoeTime\":\""
				+ DateUtil.format(eoeTime, DateTimeType.Time) + "\"");
		sb.append(",\"postTime\":\""
				+ DateUtil.format(postTime, DateTimeType.DateTime) + "\"");
		sb.append(",\"commitTime\":\""
				+ DateUtil.format(commitTime, DateTimeType.DateTime) + "\"");
		sb.append(",\"version\":" + version);
		sb.append(",\"commitFrom\":\"" + StringUtil.fixJsonStr(commitFrom)
				+ "\"");
		sb.append(",\"remark\":\"" + StringUtil.fixJsonStr(remark) + "\"");
		sb.append(",\"userId\":" + userId);
		sb.append(",\"userName\":\"" + StringUtil.fixJsonStr(userName) + "\"");
		sb.append(",\"validTickets\":" + validTickets);
		sb.append(",\"invalidTickets\":" + invalidTickets);
		sb.append(",\"sendState\":" + getSendState());
		sb.append(",\"sendStateStr\":\"" + getSendStateStr() + "\"");
		sb.append(",\"sendedCount\":" + sendedCount);
		sb.append(",\"doneTime\":\""
				+ DateUtil.format(getDoneTime(), DateTimeType.DateTime) + "\"");
		sb.append(",\"isSelfSend\":" + isSelfSend + "");
		sb.append(",\"multiAudit\":" + multiAudit + "");
		sb.append(",\"auditRole\":" + auditRole + "");
		sb.append(",\"showPass\":" + showPass + "");
		sb.append("}");
		return sb.toString();
	}

}

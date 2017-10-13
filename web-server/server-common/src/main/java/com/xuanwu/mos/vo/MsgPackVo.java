package com.xuanwu.mos.vo;

import java.util.Date;

/**
 * 短信包vo-使用审核，发送详情，发送记录
 * Created by 郭垚辉 on 2017/5/17.
 */
public class MsgPackVo {
    // id
    private String id;
    // 信息包id
    private String packId;
    // 信息帧id
    private String frameId;
    // 单条信息id
    private String ticketId;
    // 批次名称
    private String batchName;
    // 批次来源
    private String commitFrom;
    // 批次备注
    private String packRemark;
    // 包提交时间
    private Date postTime;
    // 包处理结束时间
    private Date doneTime;
    // 发送用户的名字
    private String sendUserName;
    // 发送用户的id
    private int userId;
    // 发送部门的名称
    private String sendDeptName;
    // 发送包状态
    private String packState;
    // 包中的ticket总数
    private int totalTickets = 0;
    // vo中的invalidTickets的意思和数据库中的invalidTickets的意思不一样
    // vo中的意思：短信包中被过滤的号码数、审核不通过的号码数、取消发送的号码数
    private int invalidTickets = 0;
    // 短信包中被过滤的号码数
    private int filterTickets = 0;
    // 包中有效的ticket总数(最终发送的)
    private int validTickets = 0;
    // mto没有成功发送到运营商的号码数
    private int failedTickets = 0;
    // 成功送达用户的号码数
    private int successTickets = 0;
    // 已经发送的号码数--发送到运营商的网关
    private int sendedTickets = 0; // 已发送号码数
    // 重复号码数
    private int repeatTickets = 0;
    // 非法关键字
    private int illegalKeyTickets = 0;
    // 非法号码
    private int illegalTickets = 0;
    // 黑名单
    private int blackTickets = 0;
    // 业务类型名称
    private String bizTypeName;
    // 业务类型id
    private String bizTypeId;
    // 彩信标题
    private String mmsTitle;
    // 包定时发送时间
    private Date scheduleTime;
    // 信息类型
    private int msgType;
    // 前台审核人名字
    private String frontAuditUser;
    // 前台审核时间
    private Date frontAuditTime;
    // 后台审核状态
    private String frontAuditStateName;
    // 前台审核意见
    private String frontAuditRemark;
    // 前台审核人名称
    private String backAuditUser;
    // 后台审核时间
    private Date backAuditTime;
    // 后台审核状态
    private  String backAuditStateName;
    // 后台审核意见
    private String backAuditRemark;

    // pack_uuid
    private String uuid;

    //region 前台参数
    private String auditRemark;
    private Boolean checkState;
    //endregion
    //前台参数
    //是否过滤重复号码
    private Boolean distinct;

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    //by jiangziyuan
    private String smsContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getCommitFrom() {
        return commitFrom;
    }

    public void setCommitFrom(String commitFrom) {
        this.commitFrom = commitFrom;
    }

    public String getPackRemark() {
        return packRemark;
    }

    public void setPackRemark(String packRemark) {
        this.packRemark = packRemark;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Date doneTime) {
        this.doneTime = doneTime;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPackState() {
        return packState;
    }

    public void setPackState(String packState) {
        this.packState = packState;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getInvalidTickets() {
        return invalidTickets;
    }

    public void setInvalidTickets(int invalidTickets) {
        this.invalidTickets = invalidTickets;
    }

    public int getFilterTickets() {
        return filterTickets;
    }

    public void setFilterTickets(int filterTickets) {
        this.filterTickets = filterTickets;
    }

    public int getValidTickets() {
        return validTickets;
    }

    public void setValidTickets(int validTickets) {
        this.validTickets = validTickets;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getBizTypeId() {
        return bizTypeId;
    }

    public void setBizTypeId(String bizTypeId) {
        this.bizTypeId = bizTypeId;
    }

    public String getMmsTitle() {
        return mmsTitle;
    }

    public void setMmsTitle(String mmsTitle) {
        this.mmsTitle = mmsTitle;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getSendDeptName() {
        return sendDeptName;
    }

    public void setSendDeptName(String sendDeptName) {
        this.sendDeptName = sendDeptName;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Boolean getCheckState() {
        return checkState;
    }

    public void setCheckState(Boolean checkState) {
        this.checkState = checkState;
    }

    public int getFailedTickets() {
        return failedTickets;
    }

    public void setFailedTickets(int failedTickets) {
        this.failedTickets = failedTickets;
    }

    public int getSuccessTickets() {
        return successTickets;
    }

    public void setSuccessTickets(int successTickets) {
        this.successTickets = successTickets;
    }

    public int getSendedTickets() {
        return sendedTickets;
    }

    public void setSendedTickets(int sendedTickets) {
        this.sendedTickets = sendedTickets;
    }

    public int getRepeatTickets() {
        return repeatTickets;
    }

    public void setRepeatTickets(int repeatTickets) {
        this.repeatTickets = repeatTickets;
    }

    public int getIllegalKeyTickets() {
        return illegalKeyTickets;
    }

    public void setIllegalKeyTickets(int illegalKeyTickets) {
        this.illegalKeyTickets = illegalKeyTickets;
    }

    public int getIllegalTickets() {
        return illegalTickets;
    }

    public void setIllegalTickets(int illegalTickets) {
        this.illegalTickets = illegalTickets;
    }

    public int getBlackTickets() {
        return blackTickets;
    }

    public void setBlackTickets(int blackTickets) {
        this.blackTickets = blackTickets;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getFrontAuditUser() {
        return frontAuditUser;
    }

    public void setFrontAuditUser(String frontAuditUser) {
        this.frontAuditUser = frontAuditUser;
    }

    public Date getFrontAuditTime() {
        return frontAuditTime;
    }

    public void setFrontAuditTime(Date frontAuditTime) {
        this.frontAuditTime = frontAuditTime;
    }

    public String getFrontAuditRemark() {
        return frontAuditRemark;
    }

    public void setFrontAuditRemark(String frontAuditRemark) {
        this.frontAuditRemark = frontAuditRemark;
    }

    public String getBackAuditUser() {
        return backAuditUser;
    }

    public void setBackAuditUser(String backAuditUser) {
        this.backAuditUser = backAuditUser;
    }

    public Date getBackAuditTime() {
        return backAuditTime;
    }

    public void setBackAuditTime(Date backAuditTime) {
        this.backAuditTime = backAuditTime;
    }

    public String getBackAuditRemark() {
        return backAuditRemark;
    }

    public void setBackAuditRemark(String backAuditRemark) {
        this.backAuditRemark = backAuditRemark;
    }

    public String getFrontAuditStateName() {
        return frontAuditStateName;
    }

    public void setFrontAuditStateName(String frontAuditStateName) {
        this.frontAuditStateName = frontAuditStateName;
    }

    public String getBackAuditStateName() {
        return backAuditStateName;
    }

    public void setBackAuditStateName(String backAuditStateName) {
        this.backAuditStateName = backAuditStateName;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

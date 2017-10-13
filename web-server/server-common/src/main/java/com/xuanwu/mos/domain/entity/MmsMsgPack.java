package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.AuditStateEnum;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.domain.enums.PackStateEnum;
import com.xuanwu.mos.domain.enums.SendTypeEnum;

import java.util.Date;

/**
 * 审核包----对应gsms_msg_pack_verify
 * Created by 郭垚辉 on 2017/4/20.
 */
public class MmsMsgPack extends AbstractEntity {

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
    // 在pack中是用commit_time来表示的
    private Date doneTime;
    // 发送用户的id
    private int userId;
    // 企业id
    private int enterpriseId;
    // 发送用户的名字
    private String sendUserName;
    // 包中的ticket总数
    private int totalTickets=0;
    // 包中过滤掉的ticket总数
    private int invalidTickets=0;
    // 包中有效的ticket总数(最终发送的)
    private int validTickets=0;
    // 业务类型名称
    private String bizTypeName;
    // 业务类型id
    private String bizTypeId;
    // 彩信标题
    private String mmsTitle;
    // 短信内容(部分，不是全部)
    private String smsContent;
    // 包定时发送时间
    private Date scheduleTime;
    // 信息类型
    private MsgTypeEnum msgType;
    // 包审核提交的时间
    private Date postAuditTime;
    // 帧优先级
    private int priority;
    // 包状态
    private PackStateEnum packState;
    // 发送类型(群发，组发)
    private SendTypeEnum sendType;
    // 部门名称
    private String sendDeptName;
    // mto没有成功发送到运营商的号码数
    private int failedTickets = 0;
    // 成功送达用户的号码数
    private int success = 0;
    // 已发送的号码数--提交到运营商的网关
    private int sendedTickets = 0; // 已发送号码数
    // 重复号码数
    private int repeatTickets = 0;
    // 非法关键字
    private int illegalKeyTickets = 0;
    // 非法号码
    private int illegalTickets = 0;
    // 黑名单
    private int blackTickets = 0;
    // 前台审核人名字
    private String frontAuditUser;
    // 前台审核时间
    private Date frontAuditTime;
    // 后台审核状态
    private  AuditStateEnum frontAuditState;
    // 前台审核意见
    private String frontAuditRemark;
    // 前台审核人名称
    private String backAuditUser;
    // 后台审核时间
    private Date backAuditTime;
    // 后台审核状态
    private  AuditStateEnum backAuditState;
    // 后台审核意见
    private String backAuditRemark;
    //
    private String uuid;

    private byte[] frameContent;

    public byte[] getFrameContent() {
        return frameContent;
    }

    public void setFrameContent(byte[] frameContent) {
        this.frameContent = frameContent;
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public MsgTypeEnum getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgTypeEnum msgType) {
        this.msgType = msgType;
    }

    public Date getPostAuditTime() {
        return postAuditTime;
    }

    public void setPostAuditTime(Date postAuditTime) {
        this.postAuditTime = postAuditTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public PackStateEnum getPackState() {
        return packState;
    }

    public void setPackState(PackStateEnum packState) {
        this.packState = packState;
    }

    public SendTypeEnum getSendType() {
        return sendType;
    }

    public void setSendType(SendTypeEnum sendType) {
        this.sendType = sendType;
    }

    public String getSendDeptName() {
        return sendDeptName;
    }

    public void setSendDeptName(String sendDeptName) {
        this.sendDeptName = sendDeptName;
    }

    public int getFailedTickets() {
        return failedTickets;
    }

    public void setFailedTickets(int failedTickets) {
        this.failedTickets = failedTickets;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
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

    public AuditStateEnum getBackAuditState() {
        return backAuditState;
    }

    public void setBackAuditState(AuditStateEnum backAuditState) {
        this.backAuditState = backAuditState;
    }

    public String getBackAuditRemark() {
        return backAuditRemark;
    }

    public void setBackAuditRemark(String backAuditRemark) {
        this.backAuditRemark = backAuditRemark;
    }

    public AuditStateEnum getFrontAuditState() {
        return frontAuditState;
    }

    public void setFrontAuditState(AuditStateEnum frontAuditState) {
        this.frontAuditState = frontAuditState;
    }

    public String getFrontAuditRemark() {
        return frontAuditRemark;
    }

    public void setFrontAuditRemark(String frontAuditRemark) {
        this.frontAuditRemark = frontAuditRemark;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "MmsMsgPack{" +
                "id='" + id + '\'' +
                ", packId='" + packId + '\'' +
                ", frameId='" + frameId + '\'' +
                ", ticketId='" + ticketId + '\'' +
                ", batchName='" + batchName + '\'' +
                ", commitFrom='" + commitFrom + '\'' +
                ", packRemark='" + packRemark + '\'' +
                ", postTime=" + postTime +
                ", doneTime=" + doneTime +
                ", userId=" + userId +
                ", enterpriseId=" + enterpriseId +
                ", sendUserName='" + sendUserName + '\'' +
                ", totalTickets=" + totalTickets +
                ", invalidTickets=" + invalidTickets +
                ", validTickets=" + validTickets +
                ", bizTypeName='" + bizTypeName + '\'' +
                ", bizTypeId='" + bizTypeId + '\'' +
                ", mmsTitle='" + mmsTitle + '\'' +
                ", smsContent='" + smsContent + '\'' +
                ", scheduleTime=" + scheduleTime +
                ", msgType=" + msgType +
                ", postAuditTime=" + postAuditTime +
                ", priority=" + priority +
                ", packState=" + packState +
                ", sendType=" + sendType +
                ", sendDeptName='" + sendDeptName + '\'' +
                ", failedTickets=" + failedTickets +
                ", success=" + success +
                ", sendedTickets=" + sendedTickets +
                ", repeatTickets=" + repeatTickets +
                ", illegalKeyTickets=" + illegalKeyTickets +
                ", illegalTickets=" + illegalTickets +
                ", blackTickets=" + blackTickets +
                ", frontAuditUser='" + frontAuditUser + '\'' +
                ", frontAuditTime=" + frontAuditTime +
                ", frontAuditState=" + frontAuditState +
                ", frontAuditRemark='" + frontAuditRemark + '\'' +
                ", backAuditUser='" + backAuditUser + '\'' +
                ", backAuditTime=" + backAuditTime +
                ", backAuditState=" + backAuditState +
                ", backAuditRemark='" + backAuditRemark + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}

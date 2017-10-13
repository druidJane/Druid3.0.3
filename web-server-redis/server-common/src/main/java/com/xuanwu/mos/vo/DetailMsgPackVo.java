package com.xuanwu.mos.vo;

import java.util.Date;

/**
 * Created by 郭垚辉 on 2017/4/27.
 */
public class DetailMsgPackVo {

    // 彩信标题
    private String title;
    // 提交时间
    private Date commitTime;
    // 定时时间
    private Date scheduleTime;
    // 业务类型
    private String bizType;
    // 批次来源：UMP，MOS，SDK
    private String batchFrom;
    // 批次名称
    private String batchName;
    // 批次备注
    private String batchRemark;
    // 发送部门
    private String deptName;
    // 发送用户
    private String sendUser;
    // 审批人
    private String auditUser;
    //审批时间
    private Date auditTime;
    // 审批结果
    private String auditResult;
    // 审批意见
    private String auditRemark;
    // 已知成功送达数目
    private int success ;
    // 已知失败数
    private int failed;
    // 重复的号码数
    private int repeatPhones;
    // 已经发送的号码数
    private int sendedPhones;
    // 非法关键字
    private int illegalKeys;
    // 黑名单
    private int blackPhones;
    // 非法号码数目
    private int illegalPhones;

    public DetailMsgPackVo() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBatchFrom() {
        return batchFrom;
    }

    public void setBatchFrom(String batchFrom) {
        this.batchFrom = batchFrom;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBatchRemark() {
        return batchRemark;
    }

    public void setBatchRemark(String batchRemark) {
        this.batchRemark = batchRemark;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getRepeatPhones() {
        return repeatPhones;
    }

    public void setRepeatPhones(int repeatPhones) {
        this.repeatPhones = repeatPhones;
    }

    public int getSendedPhones() {
        return sendedPhones;
    }

    public void setSendedPhones(int sendedPhones) {
        this.sendedPhones = sendedPhones;
    }

    public int getIllegalKeys() {
        return illegalKeys;
    }

    public void setIllegalKeys(int illegalKeys) {
        this.illegalKeys = illegalKeys;
    }

    public int getBlackPhones() {
        return blackPhones;
    }

    public void setBlackPhones(int blackPhones) {
        this.blackPhones = blackPhones;
    }

    public int getIllegalPhones() {
        return illegalPhones;
    }

    public void setIllegalPhones(int illegalPhones) {
        this.illegalPhones = illegalPhones;
    }
}

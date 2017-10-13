package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.util.Date;

/** 彩信管理--> 发送记录 --> 号码记录
 * Created by 郭垚辉 on 2017/3/31.
 */
public class PhoneHistoryDTO extends AbstractEntity {

    /**
     * 序号
     */
    private Long id;

    /**
     * 批次id(就是pack_id)
     */
    private Long batchId;

    /**
     * 批次名称
     */
    private String batchName;

    /**
     * 信息帧id
     */
    private Long frameId;

    /**
     * 信息id
     */
    private Long msgId;

    /**
     * 收费类型
     */
    private String billingType;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 用户(或部门)归属的上级id
     */
    private Long parentId;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 短信标题
     */
    private String title;

    /**
     * 发送用户名
     */
    private String userName;

    /**
     * 彩信发送时间
     */
    private Date sendTime;

    /**
     * 提交时间
     */
    private Date postTime;

    /**
     * 原始返回结果
     */
    private String originResult;

    /**
     * 状态报告(短信发送到用户机上，运营商才会发送该报告)
     */
    private String stateReport;

    /**
     * 提交报告(提交到运营商网关收到的报告)
     */
    private String submitReport;

    /**
     * 发送结果
     */
    private String sendResult;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Long getFrameId() {
        return frameId;
    }

    public void setFrameId(Long frameId) {
        this.frameId = frameId;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getOriginResult() {
        return originResult;
    }

    public void setOriginResult(String originResult) {
        this.originResult = originResult;
    }

    public String getStateReport() {
        return stateReport;
    }

    public void setStateReport(String stateReport) {
        this.stateReport = stateReport;
    }

    public String getSubmitReport() {
        return submitReport;
    }

    public void setSubmitReport(String submitReport) {
        this.submitReport = submitReport;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
    }
}

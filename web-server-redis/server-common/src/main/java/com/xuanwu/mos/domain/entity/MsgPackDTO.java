package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.util.Date;

/** dto类 --> 审核彩信包查询
 *  Created by 郭垚辉 on 2017/3/28.
 */
public class MsgPackDTO extends AbstractEntity {
    private Long id;
    /**
     * 是否进行审核
     */
    private boolean isAudit;

    /**
     * 是否取消
     */
    private boolean isCancel;

    /**
     * 审核数目
     */
    private int auditingNum;
    /**
     * 批次名称
     */
    private String batchName;

    /**
     * 批次提交状态
     */
    private int batchState;

    /**
     * 批次提交状态的字符表达
     */
    private String batchStateString;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 部门id
     */
    private int departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 客户提交短信的时间
     */
    private Date postTime;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 提交总数
     */
    private int postAllCount;

    /**
     * 发送总数
     */
    private String sendAllCount;

    /**
     * 信息类型:sms(0)   mms(1)
     */
    private int msgType;

    /**
     * 发送用户的id
     */
    private Long userId;

    /**
     * 短信(彩信)提交运营商时间
     */
    private Date submitTime;

    private SendCountDTO sendCountDTO;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAudit() {
        return isAudit;
    }

    public void setAudit(boolean audit) {
        isAudit = audit;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public int getAuditingNum() {
        return auditingNum;
    }

    public void setAuditingNum(int auditingNum) {
        this.auditingNum = auditingNum;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getBatchState() {
        return batchState;
    }

    public void setBatchState(int batchState) {
        this.batchState = batchState;
    }

    public String getBatchStateString() {
        return batchStateString;
    }

    public void setBatchStateString(String batchStateString) {
        this.batchStateString = batchStateString;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPostAllCount() {
        return postAllCount;
    }

    public void setPostAllCount(int postAllCount) {
        this.postAllCount = postAllCount;
    }

    public String getSendAllCount() {
        return sendAllCount;
    }

    public void setSendAllCount(String sendAllCount) {
        this.sendAllCount = sendAllCount;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SendCountDTO getSendCountDTO() {
        return sendCountDTO;
    }

    public void setSendCountDTO(SendCountDTO sendCountDTO) {
        this.sendCountDTO = sendCountDTO;
    }
}

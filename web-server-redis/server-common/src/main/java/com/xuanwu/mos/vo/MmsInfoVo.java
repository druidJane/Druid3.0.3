package com.xuanwu.mos.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gyh65 on 2017/5/15.
 */
public class MmsInfoVo {

    // 彩信内容
    private String mms;
    // 批次名称
    private String batchName;
    // 定时发送时间
    private String scheduledTime;
    // 备注
    private String remark;
    // 退订
    private boolean clearSent;
    // 是否清除重复号码
    private boolean distinct;
    // 发送联系人个数
    private Integer sendCount;
    // 业务类型id
    private Integer bizTypeId;
    // 账户余额
    private BigDecimal balance;
    // 可发送数量
    private double restSendNum;
    // 引用模板id
    private Integer templateId;
    // 是否开启关键校验
    private Boolean isEnableKeywordFilter;
    // 企业签名位置，用于设置企业信息
    private Integer entSigLocation;
    // 用户签名位置
    private Integer userSigLocation;
    // 用户签名
    private String userSignature;
    // 企业签名
    private String entSignature;
    // 当前的业务类型下，
    private Integer mmsMaxLength;

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public Integer getEntSigLocation() {
        return entSigLocation;
    }

    public void setEntSigLocation(Integer entSigLocation) {
        this.entSigLocation = entSigLocation;
    }

    public Integer getUserSigLocation() {
        return userSigLocation;
    }

    public void setUserSigLocation(Integer userSigLocation) {
        this.userSigLocation = userSigLocation;
    }

    public String getEntSignature() {
        return entSignature;
    }

    public void setEntSignature(String entSignature) {
        this.entSignature = entSignature;
    }

    public Boolean getEnableKeywordFilter() {
        return isEnableKeywordFilter;
    }

    public void setEnableKeywordFilter(Boolean enableKeywordFilter) {
        isEnableKeywordFilter = enableKeywordFilter;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public double getRestSendNum() {
        return restSendNum;
    }

    public void setRestSendNum(double restSendNum) {
        this.restSendNum = restSendNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // 发送用户id
    private Integer userId;

    // 发送的号码、文件名称
    private List<ContactVo> contactItem;

    public MmsInfoVo() {
    }

    public String getMms() {
        return mms;
    }

    public void setMms(String mms) {
        this.mms = mms;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isClearSent() {
        return clearSent;
    }

    public void setClearSent(boolean clearSent) {
        this.clearSent = clearSent;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getBizTypeId() {
        return bizTypeId;
    }

    public void setBizTypeId(Integer bizTypeId) {
        this.bizTypeId = bizTypeId;
    }

    public List<ContactVo> getContactItem() {
        return contactItem;
    }

    public void setContactItem(List<ContactVo> contactItem) {
        this.contactItem = contactItem;
    }

    public Integer getMmsMaxLength() {
        return mmsMaxLength;
    }

    public void setMmsMaxLength(Integer mmsMaxLength) {
        this.mmsMaxLength = mmsMaxLength;
    }
}
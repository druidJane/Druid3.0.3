package com.xuanwu.mos.vo;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/** 用于发送详情的数据展示
 * Created by 郭垚辉 on 2017/5/18.
 */
public class MsgTicketVo {
    // ticket id
    private String id;
    // ticket id
    private Long ticketId;
    // 手机号码
    private String phone;
    // 短信内容
    private String content;
    // 彩信标题
    private String mmsTitle;
    // 发送用户
    private String sendUserName;
    // 提交到玄武网关的时间
    private Date sendTime;
    // 发送结果(ticket表中的result字段)
    private String sendResult;
    // 提交报告(ticket表中的origin_result字段)
    private String ticketOriginResult;
    // 状态报告(statereport表中的origin_result)
    private String stateReportOriginResult;
    // 批次名称
    private String batchName;
    // 发送到运营商网关时间
    private Date commitTime;
    // 过滤原因（如果bizForm为0，说明该帧是正常帧）
    private String bizFormName;
    // 发送端口
    private String specNumber;
    // 当前ticket的packId的值
    private String packId;

    public String getSpecNumber() {
        return specNumber;
    }

    public void setSpecNumber(String specNumber) {
        this.specNumber = specNumber;
    }

    //by jiangziyuan
    private String preContent;// 前置内容
    private String sufContent;// 后置内容
    private String smsContent;// 信息内容(含签名) longtext
    private String smsContentSkipSign;// 信息内容(不含签名)
    private int smsType;// 信息类型: 0,普通短信; 1,超长父短信(不会发送); 2,超长子短信; 3,wappush;
    // 4,彩信; 5,彩信父短信; 6,彩信子短信；8：语音通知；9：语音验证码

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMmsTitle() {
        return mmsTitle;
    }

    public void setMmsTitle(String mmsTitle) {
        this.mmsTitle = mmsTitle;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
    }

    public String getTicketOriginResult() {
        return ticketOriginResult;
    }

    public void setTicketOriginResult(String ticketOriginResult) {
        this.ticketOriginResult = ticketOriginResult;
    }

    public String getStateReportOriginResult() {
        return stateReportOriginResult;
    }

    public void setStateReportOriginResult(String stateReportOriginResult) {
        this.stateReportOriginResult = stateReportOriginResult;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public String getSmsContent() {
        if (StringUtils.isNotBlank(preContent)) {
            smsContent = preContent + smsContent;
        }
        if (StringUtils.isNotBlank(sufContent)) {
            smsContent = smsContent==null?"":smsContent;
            smsContent = smsContent + sufContent;
        }
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        /*if(StringUtils.isNotBlank(smsContent)){
            if(smsType == 2){
                smsContent = StringEscapeUtils.escapeXml(smsContent);
            } else {
                smsContent = smsContent.replace("<", "&lt;");
                smsContent = smsContent.replace(">", "&gt;");
            }
        }*/
        this.smsContent = smsContent;
        this.smsContentSkipSign = smsContent;
    }

    public int getSmsType() {
        return smsType;
    }

    public void setSmsType(int smsType) {
        this.smsType = smsType;
    }

    public String getSmsContentSkipSign() {
        return smsContentSkipSign;
    }

    public void setSmsContentSkipSign(String smsContentSkipSign) {
        this.smsContentSkipSign = smsContentSkipSign;
    }

    public String getPreContent() {
        return preContent;
    }

    public void setPreContent(String preContent) {
        this.preContent = preContent;
    }

    public String getSufContent() {
        return sufContent;
    }

    public void setSufContent(String sufContent) {
        this.sufContent = sufContent;
    }


    public String getBizFormName() {
        return bizFormName;
    }

    public void setBizFormName(String bizFormName) {
        this.bizFormName = bizFormName;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }
}

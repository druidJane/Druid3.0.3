package com.xuanwu.mos.vo;

import com.xuanwu.mos.domain.AbstractEntity;

import java.util.Date;

/**
 * gsms_moreply
 * @author <a href="mailto:jiangziyuan@wxchina.com">Ziyuan.Jiang</a>
 * @version 1.0.0
 * @date 2017/5/24.
 */
public class MoReplyVo extends AbstractEntity {
    private Integer id; // 主键
    private int  moTicketId; // 回复的上行ID
    private int replyUserId; // 回复的用户ID
    private Date replyTime; // 回复时间
    private String replyContent; // 回复内容
    private int bizType; // 回复到相应的业务类型
    private int msgType; // 短信类型  1，短信；2，彩信；3，wappush
    private String batchName; // 回复的批次名称
    private int departmentId; // 回复的部门ID
    private String packId; // 回复的信息的批次ID
    private int replyType; // 回复类型 1，上行回复；2，服务调查
    private String phone;//回复号码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String serviceType; // 信息业务类型

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMoTicketId() {
        return moTicketId;
    }

    public void setMoTicketId(int moTicketId) {
        this.moTicketId = moTicketId;
    }

    public int getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        this.replyUserId = replyUserId;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public int getReplyType() {
        return replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}

package com.xuanwu.mos.vo;

import com.xuanwu.mos.domain.AbstractEntity;

import java.util.Date;

/**
 * 用于接收【短信管理——>发送记录——>接收记录】————gsms_moticket
 * @author <a href="mailto:jiangziyuan@wxchina.com">Ziyuan.Jiang</a>
 * @version 1.0.0
 * @date 2017/5/24.
 */
public class MoTicketVo extends AbstractEntity {
    private int id; // 主键
    private int userId; // 用户ID
    private String msgId; // 消息ID
    private String specNumber; // 上行端口号
    private String serviceType; // 信息业务类型
    private String phone; // 上行手机号码
    private String content; // 上行内容
    private int smsType; // 上行消息类型：0,普通短信;1,长短信;2,彩信;3,Wappush;4,缺失的长短信上行
    private Date postTime; // 上行时间
    private int isRead; // 是否已经被读取 0,未读;1,已读
    private int enterpriseId; // 企业ID
    private Boolean hasReply; // 改上行是否已经回复
    private int state; // 0,未处理;1,处理中;2,待审核;3,已完成;4,已转接;5,已忽略;6,已分配
    private int optionId; // 调查服务选项ID
    private int handleUserId; // 当前任务处理人
    private int lastUserId;
    private Date lastModifyDate;
    private String contactName;//上行手机号码 对应联系人名字

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    private String userName; // 用户名
    private String enterpriseName; // 企业名或者部门名
    private int regionId;//所属地区ID
    private String regionName;//所属地区名称
    private String isReadStr;
    private String stateStr;
    private String linkMan;// 电话号码对应的姓名（“号码记录”里面的“姓名”栏对应值）

    private Boolean deptSub;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSpecNumber() {
        return specNumber;
    }

    public void setSpecNumber(String specNumber) {
        this.specNumber = specNumber;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public int getSmsType() {
        return smsType;
    }

    public void setSmsType(int smsType) {
        this.smsType = smsType;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Boolean getHasReply() {
        return hasReply;
    }

    public void setHasReply(Boolean hasReply) {
        this.hasReply = hasReply;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getHandleUserId() {
        return handleUserId;
    }

    public void setHandleUserId(int handleUserId) {
        this.handleUserId = handleUserId;
    }

    public int getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(int lastUserId) {
        this.lastUserId = lastUserId;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getIsReadStr() {
        return isReadStr;
    }

    public void setIsReadStr(String isReadStr) {
        this.isReadStr = isReadStr;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public Boolean getDeptSub() {
        return deptSub;
    }

    public void setDeptSub(Boolean deptSub) {
        this.deptSub = deptSub;
    }
}

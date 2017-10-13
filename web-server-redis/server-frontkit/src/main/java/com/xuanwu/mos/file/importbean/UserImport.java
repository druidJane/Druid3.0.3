package com.xuanwu.mos.file.importbean;

import com.xuanwu.mos.domain.AbstractEntity;

import java.io.Serializable;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 用户导入字段封装体
 * @Data 2017-5-22
 * @Version 1.0.0
 */
public class UserImport extends AbstractEntity {
	/* 账号类型 */
	private String accountType;
	/* 用户账号 */
	private String userName;
	/* 用户名称 */
	private String linkMan;
	/* 发送密码 */
	private String sendPwd;
	/* 透传密码 */
	private String midPwd;
	/* 登录密码 */
	private String loginPwd;
	/* 手机号 */
	private String phone;
	/* 部门编号 */
	private String deptIdentify;
	/* 部门名称 */
	private String deptName;
	/* 用户扩展码 */
	private String userIdentify;
	/* 用户签名 */
	private String signature;
	/* 签名位置 */
	private String sigLocation;
	/* 业务类型编号 */
	private String bizTypeIds;
	/* 协议类型 */
	private String protocolType;
	/* 源端口 */
	private String srcPort;
	/* 回调地址 */
	private String callbackAddress;
	/* 自定义签名 */
	private String customerSignature;
	/* 发送速度 */
	private String sendSpeed;
	/* 链接数 */
	private String linkNum;
	/* 拥有角色 */
	private String roleNames;
	/* 描述 */
	private String remark;
	/* 上行推送 */
	private String upPush;
	/* 状态报告推送 */
	private String statusReportPush;
	/* 上行推送地址 */
	private String pushAddress;
	/* 状态报告推送地址 */
	private String reportPushAddress;

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getSendPwd() {
		return sendPwd;
	}

	public void setSendPwd(String sendPwd) {
		this.sendPwd = sendPwd;
	}

	public String getMidPwd() {
		return midPwd;
	}

	public void setMidPwd(String midPwd) {
		this.midPwd = midPwd;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDeptIdentify() {
		return deptIdentify;
	}

	public void setDeptIdentify(String deptIdentify) {
		this.deptIdentify = deptIdentify;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserIdentify() {
		return userIdentify;
	}

	public void setUserIdentify(String userIdentify) {
		this.userIdentify = userIdentify;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSigLocation() {
		return sigLocation;
	}

	public void setSigLocation(String sigLocation) {
		this.sigLocation = sigLocation;
	}

	public String getBizTypeIds() {
		return bizTypeIds;
	}

	public void setBizTypeIds(String bizTypeIds) {
		this.bizTypeIds = bizTypeIds;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public String getCallbackAddress() {
		return callbackAddress;
	}

	public void setCallbackAddress(String callbackAddress) {
		this.callbackAddress = callbackAddress;
	}

	public String getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	public String getSendSpeed() {
		return sendSpeed;
	}

	public void setSendSpeed(String sendSpeed) {
		this.sendSpeed = sendSpeed;
	}

	public String getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(String linkNum) {
		this.linkNum = linkNum;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpPush() {
		return upPush;
	}

	public void setUpPush(String upPush) {
		this.upPush = upPush;
	}

	public String getStatusReportPush() {
		return statusReportPush;
	}

	public void setStatusReportPush(String statusReportPush) {
		this.statusReportPush = statusReportPush;
	}

	public String getPushAddress() {
		return pushAddress;
	}

	public void setPushAddress(String pushAddress) {
		this.pushAddress = pushAddress;
	}

	public String getReportPushAddress() {
		return reportPushAddress;
	}

	public void setReportPushAddress(String reportPushAddress) {
		this.reportPushAddress = reportPushAddress;
	}

	@Override
	public Serializable getId() {
		return 0;
	}
}

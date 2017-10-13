package com.xuanwu.mos.file.mapbean;

import java.util.regex.Pattern;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription
 * @Data 2017-4-20
 * @Version 1.0.0
 */
public class UserMap {
	/* 账号类型 */
	private int accountType;
	/* 用户账号 */
	private int userName;
	/* 用户名称 */
	private int linkMan;
	/* 发送密码 */
	private int sendMsgPwd;
	/* 透传密码 */
	private int midPwd;
	/* 登录密码 */
	private int loginPwd;
	private int phone;
	/* 部门编号 */
	private int deptIdentify;
	/* 部门名称 */
	private int deptName;
	/* 用户扩展码 */
	private int userIdentify;
	/* 用户签名 */
	private int signature;
	/* 签名位置 */
	private int sigLocation;
	/* 业务类型编号 */
	private int bizIds;
	/* 协议类型 */
	private int protocolType;
	/* 源端口 */
	private int srcPort;
	/* 回调地址 */
	private int callbackAddress;
	/* 报备签名 */
	private int customerSignature;
	/* 发送速度 */
	private int sendSpeed;
	/* 链接数 */
	private int linkNum;
	/* 角色 */
	private int roleNames;
	/* 上行推送  1启用，0停用，默认停用  */
	private int upPush;
	/* 状态报告推送   1启用，0停用，默认停用  */
	private int statusReportPush;
	/* 上行推送地址   接收推送的URL地址，支持任意输入，最长100位   */
	private int pushAddress;
	/* 状态报告推送的URL地址，支持任意输入，最长100位  */
	private int reportPushAddress;

	private int remark;

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public int getMidPwd() {
		return midPwd;
	}

	public void setMidPwd(int midPwd) {
		this.midPwd = midPwd;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	public int getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(int srcPort) {
		this.srcPort = srcPort;
	}

	public int getCallbackAddress() {
		return callbackAddress;
	}

	public void setCallbackAddress(int callbackAddress) {
		this.callbackAddress = callbackAddress;
	}

	public int getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(int customerSignature) {
		this.customerSignature = customerSignature;
	}

	public int getSendSpeed() {
		return sendSpeed;
	}

	public void setSendSpeed(int sendSpeed) {
		this.sendSpeed = sendSpeed;
	}

	public int getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(int linkNum) {
		this.linkNum = linkNum;
	}

	public int getBizIds() {
		return bizIds;
	}

	public void setBizIds(int bizIds) {
		this.bizIds = bizIds;
	}

	public int getSigLocation() {
		return sigLocation;
	}

	public void setSigLocation(int sigLocation) {
		this.sigLocation = sigLocation;
	}

	public int getSendMsgPwd() {
		return sendMsgPwd;
	}

	public void setSendMsgPwd(int sendMsgPwd) {
		this.sendMsgPwd = sendMsgPwd;
	}

	public int getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(int loginPwd) {
		this.loginPwd = loginPwd;
	}

	public int getUserName() {
		return userName;
	}

	public void setUserName(int userName) {
		this.userName = userName;
	}

	public int getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(int linkMan) {
		this.linkMan = linkMan;
	}

	public int getDeptIdentify() {
		return deptIdentify;
	}

	public void setDeptIdentify(int deptIdentify) {
		this.deptIdentify = deptIdentify;
	}

	public int getDeptName() {
		return deptName;
	}

	public void setDeptName(int deptName) {
		this.deptName = deptName;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public int getUserIdentify() {
		return userIdentify;
	}

	public void setUserIdentify(int userIdentify) {
		this.userIdentify = userIdentify;
	}

	public int getSignature() {
		return signature;
	}

	public void setSignature(int signature) {
		this.signature = signature;
	}

	public int getRemark() {
		return remark;
	}

	public void setRemark(int remark) {
		this.remark = remark;
	}

	public int getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(int roleNames) {
		this.roleNames = roleNames;
	}

	public int getUpPush() {
		return upPush;
	}

	public void setUpPush(int upPush) {
		this.upPush = upPush;
	}

	public int getStatusReportPush() {
		return statusReportPush;
	}

	public void setStatusReportPush(int statusReportPush) {
		this.statusReportPush = statusReportPush;
	}

	public int getPushAddress() {
		return pushAddress;
	}

	public void setPushAddress(int pushAddress) {
		this.pushAddress = pushAddress;
	}

	public int getReportPushAddress() {
		return reportPushAddress;
	}

	public void setReportPushAddress(int reportPushAddress) {
		this.reportPushAddress = reportPushAddress;
	}

	public String tran2Params() {
		StringBuilder sb = new StringBuilder();
		sb.append(accountType).append(";");
		sb.append(userName).append(";");
		sb.append(linkMan).append(";");
		sb.append(sendMsgPwd).append(";");
		sb.append(midPwd).append(";");
		sb.append(loginPwd).append(";");
		sb.append(phone).append(";");
		sb.append(deptIdentify).append(";");
		sb.append(deptName).append(";");
		sb.append(userIdentify).append(";");
		sb.append(signature).append(";");
		sb.append(sigLocation).append(";");
		sb.append(bizIds).append(";");
		sb.append(protocolType).append(";");
		sb.append(srcPort).append(";");
		sb.append(callbackAddress).append(";");
		sb.append(customerSignature).append(";");
		sb.append(sendSpeed).append(";");
		sb.append(linkNum).append(";");
		sb.append(roleNames).append(";");

		sb.append(upPush).append(";");
		sb.append(statusReportPush).append(";");
		sb.append(pushAddress).append(";");
		sb.append(reportPushAddress).append(";");

		sb.append(remark).append(";");
		sb.deleteCharAt(sb.toString().length() - 1);
		return sb.toString();
	}

	public static UserMap parseFrom(String str) {
		String[] temp = str.split(Pattern.quote(";"));
		UserMap userMap = new UserMap();
		userMap.setAccountType(Integer.valueOf(temp[0]));
		userMap.setUserName(Integer.valueOf(temp[1]));
		userMap.setLinkMan(Integer.valueOf(temp[2]));
		userMap.setSendMsgPwd(Integer.valueOf(temp[3]));
		userMap.setMidPwd(Integer.valueOf(temp[4]));
		userMap.setLoginPwd(Integer.valueOf(temp[5]));
		userMap.setPhone(Integer.valueOf(temp[6]));
		userMap.setDeptIdentify(Integer.valueOf(temp[7]));
		userMap.setDeptName(Integer.valueOf(temp[8]));
		userMap.setUserIdentify(Integer.valueOf(temp[9]));
		userMap.setSignature(Integer.valueOf(temp[10]));
		userMap.setSigLocation(Integer.valueOf(temp[11]));
		userMap.setBizIds(Integer.valueOf(temp[12]));
		userMap.setProtocolType(Integer.valueOf(temp[13]));
		userMap.setSrcPort(Integer.valueOf(temp[14]));
		userMap.setCallbackAddress(Integer.valueOf(temp[15]));
		userMap.setCustomerSignature(Integer.valueOf(temp[16]));
		userMap.setSendSpeed(Integer.valueOf(temp[17]));
		userMap.setLinkNum(Integer.valueOf(temp[18]));
		userMap.setRoleNames(Integer.valueOf(temp[19]));

		userMap.setUpPush(Integer.valueOf(temp[20]));
		userMap.setStatusReportPush(Integer.valueOf(temp[21]));
		userMap.setPushAddress(Integer.valueOf(temp[22]));
		userMap.setReportPushAddress(Integer.valueOf(temp[23]));

		userMap.setRemark(Integer.valueOf(temp[24]));
		return userMap;
	}
}

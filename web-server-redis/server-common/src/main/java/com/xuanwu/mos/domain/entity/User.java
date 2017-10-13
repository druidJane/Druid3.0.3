package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.enums.UserAccountType;
import com.xuanwu.mos.domain.enums.UserProtocolType;
import com.xuanwu.mos.domain.enums.UserType;

import java.util.Date;
import java.util.List;

/**
 * @Description 用户
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-9-6
 * @Version 1.0.0
 */
public class User extends GsmsUser {

	private String userName;

	private String password;
	private String secondPassword;
	private boolean hasSendMessage;

	private boolean isFirstTimeLogin;

	private Date lastLoginTime;

	private int priority;

	private String capitalAccountName;

	/* 绑定的计费账户 */
	private List<CapitalAccount> capitalAccounts;

	/* 用户账号类型 */
	private UserAccountType accountType;
	/* 用户账号类型-页面展示使用 */
	private String showAccountType;
	/* 用户协议类型 */
	private UserProtocolType protocolType;
	/* 用户协议类型-页面展示使用 */
	private String showProtocolType;
	/* 源端口 */
	private String srcPort;
	/* 是否是自定义签名 */
	private int customerSignature;
	/* 回调地址 */
	private String callbackAddress;
	/* 常用业务类型 */
	private Integer commonBizTypeId;
	/* 发送速度 */
	private int sendSpeed;
	/* 链接数 */
	private int linkNum;
	/* 透传密码 */
	private String midPassword;
	/* 用户发送同号限制时间间隔（分） */
	private int userTimeInterval;
	/* 用户发送限制同号码发送条数 */
	private int userSendNum;
	/* 用户发送同号同内容限制时间间隔 */
	private int userContentInterval;
	/* 同号同内容时间间隔内同一号码发送条数 */
	private int contentSendnum;
	/* 上行推送1启用，0停用，默认停用  */
	private int upPush;
	/* 状态报告推送1启用，0停用，默认停用  */
	private int statusReportPush;
	/* 接收推送的URL地址，支持任意输入，最长100位   */
	private String pushAddress;
	/* 状态报告推送的URL地址，支持任意输入，最长100位  */
	private String reportPushAddress;

	public int getUserTimeInterval() {
		return userTimeInterval;
	}

	public void setUserTimeInterval(int userTimeInterval) {
		this.userTimeInterval = userTimeInterval;
	}

	public int getUserSendNum() {
		return userSendNum;
	}

	public void setUserSendNum(int userSendNum) {
		this.userSendNum = userSendNum;
	}

	public int getUserContentInterval() {
		return userContentInterval;
	}

	public void setUserContentInterval(int userContentInterval) {
		this.userContentInterval = userContentInterval;
	}

	public String getMidPassword() {
		return midPassword;
	}

	public void setMidPassword(String midPassword) {
		this.midPassword = midPassword;
	}

	public int getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(int linkNum) {
		this.linkNum = linkNum;
	}

	public int getSendSpeed() {
		return sendSpeed;
	}

	public void setSendSpeed(int sendSpeed) {
		this.sendSpeed = sendSpeed;
	}

	public Integer getCommonBizTypeId() {
		return commonBizTypeId;
	}

	public void setCommonBizTypeId(Integer commonBizTypeId) {
		this.commonBizTypeId = commonBizTypeId;
	}

	public String getCallbackAddress() {
		return callbackAddress;
	}

	public void setCallbackAddress(String callbackAddress) {
		this.callbackAddress = callbackAddress;
	}

	public String getShowAccountType() {
		if (accountType != null) {
			switch (accountType) {
				case WEB:
					return "web";
				case INTERFACE:
					return "接口";
				case OSPF:
					return "透传";
				default: return null;
			}
		}
		return null;
	}

	public void setShowAccountType(String showAccountType) {
		this.showAccountType = showAccountType;
	}

	public String getShowProtocolType() {
		if (protocolType != null) {
			switch (protocolType) {
				case CMPP2:
					return "cmpp2.0";
				case CMPP3:
					return "cmpp3.0";
				case SGIP:
					return "sgip1.2";
				case SMGP:
					return "smgp3.0";
				default: return null;
			}
		}
		return null;
	}

	public void setShowProtocolType(String showProtocolType) {
		this.showProtocolType = showProtocolType;
	}

	public int getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(int customerSignature) {
		this.customerSignature = customerSignature;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public UserProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(UserProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public UserAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(UserAccountType accountType) {
		this.accountType = accountType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<CapitalAccount> getCapitalAccounts() {
		return capitalAccounts;
	}

	public void setCapitalAccounts(List<CapitalAccount> capitalAccounts) {
		this.capitalAccounts = capitalAccounts;
	}

	public String getCapitalAccountName() {
		return capitalAccountName;
	}

	public void setCapitalAccountName(String capitalAccountName) {
		this.capitalAccountName = capitalAccountName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isHasSendMessage() {
		return hasSendMessage;
	}

	public void setHasSendMessage(boolean hasSendMessage) {
		this.hasSendMessage = hasSendMessage;
	}

	public String getSecondPassword() {
		return secondPassword;
	}

	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}

	@Override
	public String getFullPath() {
		return this.path;
	}

	@Override
	public UserType getType() {
		return UserType.PERSONAL;
	}

	@Override
	public boolean hasChild() {
		return false;
	}

	@Override
	public void setHasChild(boolean hasChild) {

	}

	public boolean isFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	public void setFirstTimeLogin(boolean firstTimeLogin) {
		isFirstTimeLogin = firstTimeLogin;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getContentSendnum() {
		return contentSendnum;
	}

	public void setContentSendnum(int contentSendnum) {
		this.contentSendnum = contentSendnum;
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

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"userId\":").append(id);
		sb.append(",\"userName\":\"").append(userName).append("\"");
		sb.append(",\"linkMan\":\"").append(linkMan).append("\"");
		sb.append(",\"phone\":\"").append(phone).append("\"");
		sb.append(",\"deptName\":\"").append(enterpriseName).append("\"");
		sb.append(",\"roleNames\":\"").append(roleNames).append("\"");
		sb.append(",\"state\":").append(state);
		sb.append('}');
		return sb.toString();
	}
}

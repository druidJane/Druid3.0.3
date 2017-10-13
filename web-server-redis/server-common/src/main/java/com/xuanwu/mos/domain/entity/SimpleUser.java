package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.UserState;

import java.util.Date;

/**
 * @Description 简单用户对象，当前登录用户
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-24
 * @version 1.0.0
 */
public class SimpleUser extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;// id主键
	private String username;// 用户账号
	private String password;// 密码
	private String secondPassword;// 密码
	private String sendMd5Password;// 前台短信发送密码(基于登录密码进行md5）
	private String transmitPassword;//透传密码(DES加密）
	private String linkMan;// 用户名称
	private String phone;// 用户手机

	private Date lastLoginTime;// 最后登录时间
	private UserState state;// 用户状态,只有NORMAL才能登录
	private String showState;//用户状态 页面显示使用
	private int enterpriseId;// 企业ID
	private int parentId;
	private int platformId;
	private String enterpriseName;// 企业名称
	private String domain;// 企业域
	private String path;

	private int deptId;
	private String deptName;
	private String deptPath;
	private int loginErrorTimes;

	private int adminId;
	private int capitalAccountId;
	private String capitalAccountName;
	private boolean isFirstTimeLogin;
	private int validDay;//修改密码的有效期，如果不为null则定期更新

	private String remark;
	private String identify;
	private String signature;
	private int sigLocation;
	private double balance;
	private double balanceremind;
	private Date lastUpdateTime;//上次修改密码时间
	private String testenddate;//测试账号到期时间
	private int istest;//区分企业是签约还是测试 1：签约,0：测试
    private boolean remindflag =false;// 是否给出提醒
    private int remindDays;// 测试账号到期剩余天数

	public int getRemindDays() {
		return remindDays;
	}

	public void setRemindDays(int remindDays) {
		this.remindDays = remindDays;
	}

	public boolean getRemindflag() {
		return remindflag;
	}

	public void setRemindflag(boolean remindflag) {
		this.remindflag = remindflag;
	}

	public String getTestenddate() {
		return testenddate;
	}

	public void setTestenddate(String testenddate) {
		this.testenddate = testenddate;
	}

	public int getIstest() {
		return istest;
	}

	public void setIstest(int istest) {
		this.istest = istest;
	}

	public int getCapitalAccountId() {
		return capitalAccountId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCapitalAccountId(int capitalAccountId) {
		this.capitalAccountId = capitalAccountId;
	}

	public String getCapitalAccountName() {
		return capitalAccountName;
	}

	public void setCapitalAccountName(String capitalAccountName) {
		this.capitalAccountName = capitalAccountName;
	}

	public String getShowState() {
		if (state != null) {
			switch (state) {
				case NORMAL:
					return "启用";
				default:
					return "停用";
			}
		}
		return null;
	}

	public void setShowState(String showState) {
		this.showState = showState;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecondPassword() {
		return secondPassword;
	}

	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setStateIdx(int stateIdx) {
		this.state = UserState.getState(stateIdx);
	}

	public UserState getState() {
		return state;
	}

	public void setState(UserState state) {
		this.state = state;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptPath() {
		return deptPath;
	}

	public void setDeptPath(String deptPath) {
		this.deptPath = deptPath;
	}


	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public int getLoginErrorTimes() {
		return loginErrorTimes;
	}

	public void setLoginErrorTimes(int loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}

	public boolean isFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	public void setFirstTimeLogin(boolean firstTimeLogin) {
		isFirstTimeLogin = firstTimeLogin;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getSigLocation() {
		return sigLocation;
	}

	public void setSigLocation(int sigLocation) {
		this.sigLocation = sigLocation;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public double getBalanceremind() {
		return balanceremind;
	}

	public void setBalanceremind(double balanceremind) {
		this.balanceremind = balanceremind;
	}

	public String getSendMd5Password() {
		return sendMd5Password;
	}

	public void setSendMd5Password(String sendMd5Password) {
		this.sendMd5Password = sendMd5Password;
	}

	public int getValidDay() {
		return validDay;
	}

	public void setValidDay(int validDay) {
		this.validDay = validDay;
	}

	public String getTransmitPassword() {
		return transmitPassword;
	}

	public void setTransmitPassword(String transmitPassword) {
		this.transmitPassword = transmitPassword;
	}

	@Override
	public String toString() {
		return "SimpleUser [id=" + id + ", username=" + username + ", password=" + password + ", linkMan=" + linkMan
				+ ", lastLoginTime=" + lastLoginTime + ", state=" + state + ", enterpriseId=" + enterpriseId
				+ ", enterpriseName=" + enterpriseName + ", domain=" + domain + ", path=" + path + ", deptId=" + deptId
				+ ", deptName=" + deptName + ", deptPath=" + deptPath + "]";
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public boolean isAdmin() {
		return id == adminId;
	}

	public boolean isIsAdmin() {
		return id == adminId;
	}
}

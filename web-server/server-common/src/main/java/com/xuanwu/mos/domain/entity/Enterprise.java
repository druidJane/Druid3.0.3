package com.xuanwu.mos.domain.entity;


import com.xuanwu.mos.domain.enums.UserType;

import java.math.BigDecimal;

/**
 * @Description 企业
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-9-6
 * @Version 1.0.0
 */
public class Enterprise extends GsmsUser {
	
	/** 0：预付费，1:后付费 */
	private int paymentType;
	
	/** 企业下的用户的默认初始密码 */
	private String initPassword;
	
	/** 是否需要审核,0-不审核 */
	private boolean auditingFlag;
	
	/** 审核基数 */
	private int auditingNum;
	
	/** 是否启用黑名单过滤,1--启用,0--禁用 */
	private boolean enableBlackListFilter;
	
	/** 是否启用关键字过滤,1--启用,0--禁用 */
	private boolean enableKeywordFilter;
	
	/** 标识企业在分配端口时是否允许企业可以自定义扩展 */
	private boolean extend;
	
	/** 标识企业在分配端口时是否允许企业可以切换通道 */
	private boolean shift;
	
	/** 标识该企业下的用户是否发送过信息  该字段只更新一次 */
	private boolean hasSendMessage;
	
	/** 是否需要彩信审核,0-不审核 */
	private boolean auditingMmsFlag;
	
	/** 彩信审核基数 */
	private int auditingMmsNum;
	
	/** 是否启用彩信模块 */
	private boolean enableMms;
	
	/** 行业ID */
	private int industryId;
	
	/** 地区ID */
	private int regionId;
	
	/** 所属销售 */
	private int salemanId;
	
	/** 是否显示端口号 1--是, 0--否 */
	private boolean showSpecnumFlag;
	
	/** 是否显示状态报告1--是, 0--否 */
	private boolean showStatereportFlag;
	
	/** 工号 */
	private String jobNumber;
	
	/** 是否启用客户审核,0--否，1--是 */
	private boolean auditingCustomFlag;
	
	/** 企业扣费类型: 0--按接收量扣费, 1--按提交量扣费,2--按报告返回扣费 */
	private int billingType;

	/** 是否禁用全局关键字过滤: 0表示禁用，1表示开启*/
	private boolean disableGlobalKeyWorld;

	/** 是否警示前台关键字过滤 */
	private boolean isWarningKeyWord;
	
	
	private boolean hasChild;
	/** 是否需要素材审核,0-不审核 */
	private boolean auditingMaterialFlag;
	private String monthlyStatStart;
	private String monthlyStatEnd;
	private String balanceRemind;
	private String deptNoPrefix;
	private int defaultMoUserId;
	/* 充值日期/天 */
	private int chargeDay;
	private String moUserName;
	private Boolean trustFlag;
	private Boolean enabledExport;
	private Boolean test;
	/* 是否是透传 */
	private Boolean transparentSend;
	private BigDecimal smsPrice;
	private BigDecimal mmsPrice;

	public BigDecimal getSmsPrice() {
		return smsPrice;
	}

	public void setSmsPrice(BigDecimal smsPrice) {
		this.smsPrice = smsPrice;
	}

	public BigDecimal getMmsPrice() {
		return mmsPrice;
	}

	public void setMmsPrice(BigDecimal mmsPrice) {
		this.mmsPrice = mmsPrice;
	}

	public Boolean getTransparentSend() {
		return transparentSend;
	}

	public void setTransparentSend(Boolean transparentSend) {
		this.transparentSend = transparentSend;
	}

	public int getDefaultMoUserId() {
		return defaultMoUserId;
	}

	public void setDefaultMoUserId(int defaultMoUserId) {
		this.defaultMoUserId = defaultMoUserId;
	}

	public String getDeptNoPrefix() {
		return deptNoPrefix;
	}

	public void setDeptNoPrefix(String deptNoPrefix) {
		this.deptNoPrefix = deptNoPrefix;
	}

	public String getBalanceRemind() {
		return balanceRemind;
	}

	public void setBalanceRemind(String balanceRemind) {
		this.balanceRemind = balanceRemind;
	}

	public String getMonthlyStatStart() {
		return monthlyStatStart;
	}

	public void setMonthlyStatStart(String monthlyStatStart) {
		this.monthlyStatStart = monthlyStatStart;
	}

	public String getMonthlyStatEnd() {
		return monthlyStatEnd;
	}

	public void setMonthlyStatEnd(String monthlyStatEnd) {
		this.monthlyStatEnd = monthlyStatEnd;
	}

	public boolean isAuditingMaterialFlag() {
		return auditingMaterialFlag;
	}

	public void setAuditingMaterialFlag(boolean auditingMaterialFlag) {
		this.auditingMaterialFlag = auditingMaterialFlag;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public String getInitPassword() {
		return initPassword;
	}

	public void setInitPassword(String initPassword) {
		this.initPassword = initPassword;
	}

	public boolean isAuditingFlag() {
		return auditingFlag;
	}

	public void setAuditingFlag(boolean auditingFlag) {
		this.auditingFlag = auditingFlag;
	}

	public int getAuditingNum() {
		return auditingNum;
	}

	public void setAuditingNum(int auditingNum) {
		this.auditingNum = auditingNum;
	}

	public boolean getExtend() {
		return extend;
	}

	public void setExtend(boolean extend) {
		this.extend = extend;
	}

	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	public boolean isHasSendMessage() {
		return hasSendMessage;
	}

	public void setHasSendMessage(boolean hasSendMessage) {
		this.hasSendMessage = hasSendMessage;
	}

	public boolean isAuditingMmsFlag() {
		return auditingMmsFlag;
	}

	public void setAuditingMmsFlag(boolean auditingMmsFlag) {
		this.auditingMmsFlag = auditingMmsFlag;
	}

	public int getAuditingMmsNum() {
		return auditingMmsNum;
	}

	public void setAuditingMmsNum(int auditingMmsNum) {
		this.auditingMmsNum = auditingMmsNum;
	}

	public boolean isEnableMms() {
		return enableMms;
	}

	public void setEnableMms(boolean enableMms) {
		this.enableMms = enableMms;
	}

	public int getIndustryId() {
		return industryId;
	}

	public void setIndustryId(int industryId) {
		this.industryId = industryId;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public int getSalemanId() {
		return salemanId;
	}

	public void setSalemanId(int salemanId) {
		this.salemanId = salemanId;
	}

	public boolean isShowSpecnumFlag() {
		return showSpecnumFlag;
	}

	public void setShowSpecnumFlag(boolean showSpecnumFlag) {
		this.showSpecnumFlag = showSpecnumFlag;
	}

	public boolean isShowStatereportFlag() {
		return showStatereportFlag;
	}

	public void setShowStatereportFlag(boolean showStatereportFlag) {
		this.showStatereportFlag = showStatereportFlag;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public boolean isAuditingCustomFlag() {
		return auditingCustomFlag;
	}

	public void setAuditingCustomFlag(boolean auditingCustomFlag) {
		this.auditingCustomFlag = auditingCustomFlag;
	}

	public int getBillingType() {
		return billingType;
	}

	public void setBillingType(int billingType) {
		this.billingType = billingType;
	}

	public int getChargeDay() {
		return chargeDay;
	}

	public void setChargeDay(int chargeDay) {
		this.chargeDay = chargeDay;
	}

	public String getMoUserName() {
		return moUserName;
	}

	public void setMoUserName(String moUserName) {
		this.moUserName = moUserName;
	}

	public Boolean getTrustFlag() {
		return trustFlag;
	}

	public void setTrustFlag(Boolean trustFlag) {
		this.trustFlag = trustFlag;
	}

	public Boolean getEnabledExport() {
		return enabledExport;
	}

	public void setEnabledExport(Boolean enabledExport) {
		this.enabledExport = enabledExport;
	}

	public Boolean getTest() {
		return test;
	}

	public void setTest(Boolean test) {
		this.test = test;
	}

	@Override
	public String getFullPath() {
		return this.path;
	}
	
	@Override
	public UserType getType() {
		return UserType.ENTERPRISE;
	}
	
	@Override
	public boolean hasChild() {
		return hasChild;
	}
	
	@Override
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	
	public boolean isEnableBlackListFilter() {
		return enableBlackListFilter;
	}

	public void setEnableBlackListFilter(boolean enableBlackListFilter) {
		this.enableBlackListFilter = enableBlackListFilter;
	}

	public boolean isEnableKeywordFilter() {
		return enableKeywordFilter;
	}

	public void setEnableKeywordFilter(boolean enableKeywordFilter) {
		this.enableKeywordFilter = enableKeywordFilter;
	}

	public boolean isDisableGlobalKeyWorld() {
		return disableGlobalKeyWorld;
	}

	public void setDisableGlobalKeyWorld(boolean disableGlobalKeyWorld) {
		this.disableGlobalKeyWorld = disableGlobalKeyWorld;
	}

	public boolean isWarningKeyWord() {
		return isWarningKeyWord;
	}

	public void setWarningKeyWord(boolean warningKeyWord) {
		isWarningKeyWord = warningKeyWord;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"pId\":").append(0);
		sb.append(",\"type\":").append(UserType.ENTERPRISE.getIndex());
		sb.append(",\"name\":\"").append(enterpriseName).append('\"');
		sb.append(",\"path\":\"").append(path).append('\"').append('}');
		return sb.toString();
	}
}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.msggate.common.sbi.entity.*;
import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-4-27
 * @Version 1.0.0
 */
public abstract class Account {
//	public enum AccountType {
//		NORMAL(0), MAIN(1), SUB(2), MAIN_AGENT(3), SUB_AGENT(4);
//		private final int index;
//
//		private AccountType(int index) {
//			this.index = index;
//		}
//
//		public int getIndex() {
//			return index;
//		}
//
//		public static AccountType getType(int index) {
//			switch (index) {
//			case 0:
//				return NORMAL;
//			case 1:
//				return MAIN;
//			case 2:
//				return SUB;
//			case 3:
//				return MAIN_AGENT;
//			case 4:
//				return SUB_AGENT;
//			default:
//				return NORMAL;
//			}
//		}
//	}

	public enum AccountState {
		NORMAL(0), SUSPEND(1), TERMINAL(2), NOT_LIMIT(-1) ;
		private final int index;

		private AccountState(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static AccountState getState(int index) {
			switch (index) {
			case 0:
				return NORMAL;
			case 1:
				return SUSPEND;
			case 2:
				return TERMINAL;
			default:
				return SUSPEND;
			}
		}
	}

	public enum MmsSignedType {
		NOTSIGNED(0), SIGNED(1), UNDEFINE(3);
		private final int index;

		private MmsSignedType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static MmsSignedType getMmsSignedType(int index) {
			switch (index) {
			case 0:
				return NOTSIGNED;
			case 1:
				return SIGNED;
			default:
				return UNDEFINE;
			}
		}
	}

	/**
	 * Primary identity
	 */
	protected int id;
	/**
	 * Account name
	 */
	protected String account;

	/**
	 * 企业名称
	 */
	protected String name;

	/**
	 * 企业签名
	 */
	protected String signature = "";

	/**  */
	protected int signLen;

	/**
	 * Added Signature location
	 */
	protected boolean sigLocation;

	/**
	 * Enterprise state
	 */
	protected AccountState state;

	/**
	 * Priority
	 */
	protected int priority;

	/**
	 * Enterprise identify
	 */
	protected String identify = "";

	/**
	 * Capital account
	 */
	private com.xuanwu.msggate.common.sbi.entity.CapitalAccount capitalAccount;

	/**
	 * Bind special number
	 */
	protected BindSpecialNum bindSpecNum = new BindSpecialNum();

	/**
	 * Red bind special number
	 */
	protected RedSpecNum redSpecNum;

	/**
	 * The version of user
	 */
	protected int userVersion;
	
	/**
	 * The version of special service number tree
	 */
	protected int specVersion;
	
	protected Integer bizType;

	/**
	 * 企业彩信签约类型
	 */
	protected MmsSignedType mmsSignedType;

	/**
	 * 可发送信息的类型
	 */
	private Map<MsgType,Boolean> allowSendType = new HashMap<MsgType,Boolean>();

	/**
	 * 是否短信免审
	 */
	private boolean isFreeAudit = false;
	
	/**
	 * 是否彩信免审
	 */
	private boolean isMmsFreeAudit = false;
	
	/** 平台ID：0:Backend; 1:UMP; 2:FrontKit **/
	private int platformID;
	
	/** CarrierMsgTypePrice map */
	protected Map<Integer, Double> carrierPriceMap = new HashMap<Integer, Double>();
	
	protected int defaultBizTypeId;
	
	protected int istest;
	
	protected Date testEndDate;
	
	protected Date commercialTime;

	private String email;// 登录邮箱

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 测试用户标识符，0为测试用户，1为签约用户
	 * 
	 */
	public int getIstest() {
		return istest;
	}

	public void setIstest(int istest) {
		this.istest = istest;
	}

	public Date getCommercialTime() {
		return commercialTime;
	}

	public void setCommercialTime(Date commercialTime) {
		this.commercialTime = commercialTime;
	}

	

	public Date getTestEndDate() {
		return testEndDate;
	}

	public void setTestEndDate(Date testEndDate) {
		this.testEndDate = testEndDate;
	}

	

	/**
	 * Is allowed request
	 * 
	 * @return
	 */
	public abstract boolean isAllowedRequest();

	/**
	 * Is allowed custom extensions
	 * 
	 * @return
	 */
	public abstract boolean isCustomExtend();

	/**
	 * Get account
	 * 
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Set account
	 * 
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Get id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get signature
	 * 
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Set signature
	 * 
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
		if (StringUtils.isNotBlank(signature))
			this.signLen = signature.codePointCount(0, signature.length());
		else
			this.signature = "";
	}

	/**
	 * Get sigLocation
	 * 
	 * @return the sigLocation
	 */
	public boolean isSigLocation() {
		return sigLocation;
	}

	/**
	 * Set sigLocation
	 * 
	 * @param sigLocation
	 *            the sigLocation to set
	 */
	public void setSigLocation(boolean sigLocation) {
		this.sigLocation = sigLocation;
	}

	/**
	 * Get state
	 * 
	 * @return the state3
	 */
	public AccountState getState() {
		return state;
	}

	/**
	 * Set state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(AccountState state) {
		this.state = state;
	}

	public void setStateIndex(int index) {
		this.state = AccountState.getState(index);
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		if (StringUtils.isNotBlank(identify))
			this.identify = identify;
		else 
			this.identify = "";
	}

	public BindSpecialNum getBindSpecNum() {
		return bindSpecNum;
	}

	public List<BindSpecNumResult> getFixedSpecNumBind() {
		return bindSpecNum.getFixedSpecNumBind();
	}

	public List<BindSpecNumResult> getSysSpecNumBind() {
		return bindSpecNum.getSysSpecNumBind();
	}

	public List<BindSpecNumResult> getRegionSpecNumBind() {
		return bindSpecNum.getRegionSpecNumBind();
	}

	public void setBindSpecNum(BindSpecialNum bindSpecNum) {
		this.bindSpecNum = bindSpecNum;
	}

	public RedSpecNum getRedSpecNum() {
		return redSpecNum;
	}

	public void setRedSpecNum(RedSpecNum redSpecNum) {
		this.redSpecNum = redSpecNum;
	}

	public int getUserVersion() {
		return userVersion;
	}

	public void setUserVersion(int userVersion) {
		this.userVersion = userVersion;
	}

	public int getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(int specVersion) {
		this.specVersion = specVersion;
	}
	
	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public com.xuanwu.msggate.common.sbi.entity.CapitalAccount getCapitalAccount() {
		return capitalAccount;
	}

	public void setCapitalAccount(com.xuanwu.msggate.common.sbi.entity.CapitalAccount capitalAccount) {
		this.capitalAccount = capitalAccount;
	}

	public void setMmsSignedType(int type) {
		this.mmsSignedType = MmsSignedType.getMmsSignedType(type);
	}

	public MmsSignedType getMmsSignedType() {
		return mmsSignedType;
	}

	/**
	 * 是否允许发送信息
	 * @return
	 */
	public Map<MsgType,Boolean> getAllowSendType() {
		return allowSendType;
	}

	public void setAllowSendType(Map<MsgType,Boolean> allowSendType) {
		this.allowSendType = allowSendType;
	}

	/**
	 * 是否短信免审
	 * @return
	 */
	public boolean isFreeAudit() {
		return isFreeAudit;
	}

	public void setFreeAudit(boolean isFreeAudit) {
		this.isFreeAudit = isFreeAudit;
	}
	
	/**
	 * 是否彩信免审
	 * @return
	 */
	public boolean isMmsFreeAudit() {
		return isMmsFreeAudit;
	}

	public void setMmsFreeAudit(boolean isMmsFreeAudit) {
		this.isMmsFreeAudit = isMmsFreeAudit;
	}

	public void initBindResult(BizTypeInfo userDefaultType) {
		bindSpecNum.initBindResult(userDefaultType);
	}

	public int getSignLen() {
		return signLen;
	}

	public void setSignLen(int signLen) {
		this.signLen = signLen;
	}

	/**
	 * 是否开启黑名单过滤
	 * @return
	 */
	public boolean isEnableBlackListFilter() {
		return true;
	}

	/**
	 * 是否开启关键字过滤
	 * @return
	 */
	public boolean isEnableKeywordFilter() {
		return true;
	}
	
	/**
	 * 是否禁用所有黑名单过滤
	 * @return
	 */
	public abstract boolean isDisableGBlacklistFilter();
	
	public abstract boolean isDisableGKeyWordFilter();

	/**
	 * 是否开启审核
	 * @return
	 */
	public boolean isEnableAuditing() {
		return false;
	}

	/**
	 * 如果需要审核，将返回实际的审核基数
	 * @return
	 */
	public Integer getAuditingNum() {
		return null;
	}
	
	/**
	 * 是否开启彩信审核
	 * @return
	 */
	public boolean isEnableMmsAuditing() {
		return false;
	}

	/**
	 * 如果需要彩信审核，将返回实际的彩信审核基数
	 * @return
	 */
	public Integer getMmsAuditingNum() {
		return null;
	}
	
	/**
	 * 是否发送过信息
	 * @return
	 */
	public boolean isHasSentMessage(){
		return false;
	}

	/**
	 * 取得父企业
	 * 
	 * @return
	 */
	public abstract Account getParent();

	protected RegionSpecNum regionSpecNum;

	public RegionSpecNum getRegionSpecNum() {
		return regionSpecNum;
	}

	public void setRegionSpecNum(RegionSpecNum regionSpecNum) {
		this.regionSpecNum = regionSpecNum;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPlatformID() {
		return platformID;
	}
	
	public void setPlatformID(int platformID) {
		this.platformID = platformID;
	}
	
	public void setCarrierPrices(List<CarrierMsgTypePrice> cmtPrices) {
		if(cmtPrices == null) return;
		
		for(CarrierMsgTypePrice cmtPrice : cmtPrices){
			if(cmtPrice.getMsgType() == MsgType.LONGSMS){
				carrierPriceMap.put(tran2key(cmtPrice.getCarrier(), MsgType.SMS), cmtPrice.getPrice());
			}
			carrierPriceMap.put(tran2key(cmtPrice.getCarrier(), cmtPrice.getMsgType()), cmtPrice.getPrice());
		}
	}
	
	/**
     * 获得计费账户运营信息类型单价
     * @return
     */
	public Double getPrice(com.xuanwu.msggate.common.sbi.entity.Carrier carrier, MsgType msgType) {
		return carrierPriceMap.get(tran2key(carrier, msgType));
	} 
	
	private int tran2key(Carrier carrier, MsgType msgType){
		return ((carrier.getIndex() << 4 )| (msgType.getIndex() & 0xF));
	}
	
	/**
	 * @return the defaultBizTypeId
	 */
	public int getDefaultBizTypeId() {
		return defaultBizTypeId;
	}
	
	/**
	 * @param defaultBizTypeId the defaultBizTypeId to set
	 */
	public void setDefaultBizTypeId(int defaultBizTypeId) {
		this.defaultBizTypeId = defaultBizTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id != other.id)
			return false;
		return true;
	}
}

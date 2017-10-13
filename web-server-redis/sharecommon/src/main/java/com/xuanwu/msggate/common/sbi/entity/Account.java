/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.util.EditeTrustIps;

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
		NORMAL(0), SUSPEND(1), TERMINAL(2);
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
	private CapitalAccount capitalAccount;

	/**
	 * Bind special number
	 */
	protected BindSpecialNum bindSpecNum = new BindSpecialNum();

	/**
	 * Red bind special number
	 */
	protected RedSpecNum redSpecNum;

	/**
	 * The version of special service number tree
	 */
	private int specVersion;

	/**
	 * 企业彩信签约类型
	 */
	protected MmsSignedType mmsSignedType;

	/**
	 * 可发送信息的类型
	 */
	private Map<MsgType, Boolean> allowSendType = new HashMap<MsgType, Boolean>();

	/**
	 * 是否短信免审
	 */
	private boolean isFreeAudit = false;

	/**
	 * 是否彩信免审
	 */
	private boolean isMmsFreeAudit = false;

	/** 是否发送信息 */
	private boolean hasSentMessage;

	/** 平台ID：0:Backend; 1:UMP; 2:FrontKit **/
	private int platformID;
	
	private String trustIps;
	
	private String loginTypes;

	protected int defaultBizTypeId;

	public String getLoginTypes() {
		return loginTypes;
	}

	public void setLoginTypes(String loginTypes) {
		this.loginTypes = loginTypes;
	}

	public String getTrustIps() {
		try{
			String trustIpsAll = EditeTrustIps.takeTrustIps(trustIps);
			return trustIpsAll;
		}catch(Exception e){
			return trustIps;
		}
	}

	public void setTrustIps(String trustIps) {
		this.trustIps = trustIps;
	}

	/**
	 * 企业绑定的单价，如果是被代理企业，须取得父企业的单价
	 * 
	 * @param carrier
	 * @param msgType
	 * @return
	 */
	public abstract Double getPrice(Carrier carrier, MsgType msgType);

	/**
	 * 如果企业为被代理企业，这个是我们分配给企业的单价
	 * 
	 * @param carrier
	 * @param specNumID
	 * @param msgType
	 * @return
	 */
	public abstract Double getParentPrice(Carrier carrier, MsgType msgType);

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

	public int getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(int specVersion) {
		this.specVersion = specVersion;
	}

	public CapitalAccount getCapitalAccount() {
		return capitalAccount;
	}

	public void setCapitalAccount(CapitalAccount capitalAccount) {
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
	
	public boolean isHasSentMessage() {
		return hasSentMessage;
	}

	public void setHasSentMessage(boolean hasSentMessage) {
		this.hasSentMessage = hasSentMessage;
	}

	/**
	 * @return the defaultBizTypeId
	 */
	public int getDefaultBizTypeId() {
		return defaultBizTypeId;
	}

	/**
	 * @param defaultBizTypeId
	 *            the defaultBizTypeId to set
	 */
	public void setDefaultBizTypeId(int defaultBizTypeId) {
		this.defaultBizTypeId = defaultBizTypeId;
	}

}

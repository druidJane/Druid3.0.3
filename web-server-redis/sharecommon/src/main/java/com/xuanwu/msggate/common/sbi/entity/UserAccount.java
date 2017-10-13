/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;


/**
 * User, a actor
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2011-10-21
 * @Version 1.0.0
 */
public class UserAccount extends Account {
	
	private EnterpriseAccount parent;

	private String password;

	@Override
	public boolean isAllowedRequest() {
		if (parent.state == AccountState.NORMAL && state == AccountState.NORMAL)
			return true;
		return false;
	}

	@Override
	public boolean isCustomExtend() {
		return true;
	}

	@Override
	public EnterpriseAccount getParent(){
		return parent;
	}
	
	public void setParent(EnterpriseAccount parent){
		this.parent = parent;
	}

	@Override
	public Double getPrice(Carrier carrier, MsgType msgType) {
		return parent.getPrice(carrier, msgType);
	}
	
	@Override
	public Double getParentPrice(Carrier carrier, MsgType msgType) {
		return parent.getParentPrice(carrier, msgType);
	}
	
	@Override
	public boolean isEnableAuditing() {
		return parent.isEnableAuditing();
	}
	
	@Override
	public Integer getAuditingNum() {
		return parent.getAuditingNum();
	}
	
	@Override
	public boolean isEnableMmsAuditing() {
		return parent.isEnableMmsAuditing();
	}
	
	@Override
	public Integer getMmsAuditingNum() {
		return parent.getMmsAuditingNum();
	}
	
	@Override
	public boolean isEnableBlackListFilter() {
		return parent.isEnableBlackListFilter();
	}
	
	@Override
	public boolean isEnableKeywordFilter() {
		return parent.isEnableKeywordFilter();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}

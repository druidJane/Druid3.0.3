package com.xuanwu.msggate.common.sbi.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

/**
 * Enterprise, just like a domain
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2011-10-19
 * @Version 1.0.0
 */
public class EnterpriseAccount extends Account {
	
	/**
	 * 企业扣费类型
	 *
	 */
	public enum BillingType {
		
		RECEIVER(0), /*按接收量扣费 */
		SUBMIT(1), /*按提交量扣费 */
		REPORT(2);/*按状态报告扣费*/
		
		private final int index;
	
		private BillingType(int index) {
			this.index = index;
		}
	
		public int getIndex() {
			return index;
		}
	
		public static BillingType getType(int index) {
			switch (index) {
			case 0:
				return RECEIVER;
			case 1:
				return SUBMIT;
			case 2:
				return REPORT;
			default:
				return RECEIVER;
			}
		}
	}
	
	private boolean enableAuditing;
	
	private int auditingNum;
	
	private boolean enableMmsAuditing;
	
	private int mmsAuditingNum;
	
	private boolean enableBlackListFilter;
	
	private boolean enableKeywordFilter;

	private BillingType billingType;
	
	/** CarrierMsgTypePrice map */
	private Map<Integer, Double> carrierPriceMap = new HashMap<Integer, Double>();
	
	/** 如果该企业账号为被代理企业，那么其父企业才是我们管理的企业 */
	private EnterpriseAccount parent;
	
	@Override
	public boolean isAllowedRequest() {
		if(state == AccountState.NORMAL){
			return true;
		}
		return false;
	}

	@Override
	public boolean isCustomExtend() {
		return true;
	}

	@Override
	public boolean isEnableAuditing() {
		return enableAuditing;
	}

	@Override
	public boolean isEnableMmsAuditing() {
		return enableMmsAuditing;
	}

	@Override
	public Integer getAuditingNum() {
		return auditingNum;
	}
	
	@Override
	public Integer getMmsAuditingNum() {
		return mmsAuditingNum;
	}

	@Override
	public boolean isEnableBlackListFilter() {
		return enableBlackListFilter;
	}

	@Override
	public boolean isEnableKeywordFilter() {
		return enableKeywordFilter;
	}
	
	public void setEnableAuditing(boolean enableAuditing) {
		this.enableAuditing = enableAuditing;
	}

	public void setAuditingNum(int auditingNum) {
		this.auditingNum = auditingNum;
	}

	public void setEnableMmsAuditing(boolean enableMmsAuditing) {
		this.enableMmsAuditing = enableMmsAuditing;
	}

	public void setMmsAuditingNum(int mmsAuditingNum) {
		this.mmsAuditingNum = mmsAuditingNum;
	}
	
	public void setEnableBlackListFilter(boolean enableBlackListFilter) {
		this.enableBlackListFilter = enableBlackListFilter;
	}

	public void setEnableKeywordFilter(boolean enableKeywordFilter) {
		this.enableKeywordFilter = enableKeywordFilter;
	}
	
	public BillingType getBillingType() {
		return billingType;
	}

	public void setBillingType(int billingType) {
		this.billingType = BillingType.getType(billingType);
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
	
	@Override
	public Double getPrice(Carrier carrier, MsgType msgType) {
		return carrierPriceMap.get(tran2key(carrier, msgType));
	}
	
	@Override
	public Double getParentPrice(Carrier carrier, MsgType msgType) {
		if(parent == null){
			return null;
		}
		return parent.getPrice(carrier, msgType);
	}
	
	private int tran2key(Carrier carrier, MsgType msgType){
		return ((carrier.getIndex() << 4 )| (msgType.getIndex() & 0xF));
	}

	@Override
	public EnterpriseAccount getParent() {
		return parent;
	}

	public void setParent(EnterpriseAccount parent) {
		this.parent = parent;
	}
}

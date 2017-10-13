/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

import java.util.ArrayList;
import java.util.List;

/**
 * Template used to handle binding special service number
 * 
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-5
 * @Version 1.0.0
 */
public class BindSpecNumResult {
	private Integer id;
	
	/**
	 * Enterprise ID
	 */
	private Integer enterpriseID;
	
	/**
	 * Bind type
	 */
	private BindSpecNumType type;
	/**
	 * Special service number id
	 */
	private Integer specsvsNumID;
	/**
	 * Special service number
	 */
	private SpecSVSNumber specSVSNum;
	/**
	 * Region id
	 */
	private Integer regionID;
	/**
	 * Region
	 */
	private Region region;
	/**
	 * The message type associated with type
	 */
	private MsgType msgType;
	/**
	 * Price
	 */
	private Double price;
	/**
	 * Ratio
	 */
	private Float ratio;

	/**
	 * 信息业务类型ID
	 */
	private Integer bizType;

	/**
	 * 信息业务类型
	 */
	private BizTypeInfo bizInfo;
	
	private Carrier carrier;

	private Carrier channelCarrier;
	
	private boolean isBypass;

	public BindSpecNumType getType() {
		return type;
	}

	public void setChannelCarrier(Carrier channelCarrier) {
		this.channelCarrier = channelCarrier;
	}

	/**
	 * 获取通道所属运营商
	 * 
	 * @return
	 */
	public Carrier getChannelCarrier() {
		return (channelCarrier == null) ? specSVSNum.getCarrierChannel()
				.getRegionCarrier().getCarrier() : channelCarrier;
	}

	/**
	 * 获取通道可发送运营商
	 * 
	 * @return
	 */
	public Carrier getCarrier(){
		return carrier;
	}
	
	public void setCarrier(Integer carrier){
		this.carrier = Carrier.getType(carrier);
	}

	public CarrierChannel getCarrierChannel() {
		return specSVSNum.getCarrierChannel();
	}

	public void setType(int type) {
		this.type = BindSpecNumType.getType(type);
	}

	public Integer getSpecsvsNumID() {
		return specsvsNumID;
	}

	public void setSpecsvsNumID(Integer specsvsNumID) {
		this.specsvsNumID = specsvsNumID;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = MsgType.getType(msgType);
	}

	public Integer getId() {
		return id;
	}

	public Integer getRegionID() {
		return regionID;
	}

	public void setRegionID(Integer regionID) {
		this.regionID = regionID;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}

	public SpecSVSNumber getSpecSVSNum() {
		return specSVSNum;
	}

	public void setSpecSVSNum(SpecSVSNumber specSVSNum) {
		this.specSVSNum = specSVSNum;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public BizTypeInfo getBizInfo() {
		return bizInfo;
	}

	public void setBizInfo(BizTypeInfo bizInfo) {
		this.bizInfo = bizInfo;
	}
	
	public Integer getEnterpriseID() {
		return enterpriseID;
	}

	public void setEnterpriseID(Integer enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	public void setBypass(boolean isBypass) {
		this.isBypass = isBypass;
	}

	public boolean isBypass() {
		return isBypass;
	}

	public BindSpecNumResult duplitBasicResult() {
		BindSpecNumResult result = new BindSpecNumResult();
		result.carrier = getCarrier();
		result.bizType = getBizType();
		result.bizInfo = getBizInfo();
		result.price = getPrice();
		result.ratio = getRatio();
		result.region = getRegion();
		result.regionID = getRegionID();
		result.specSVSNum = getSpecSVSNum();
		result.specsvsNumID = getSpecsvsNumID();
		result.msgType = getMsgType();
		result.enterpriseID = getEnterpriseID();
		result.isBypass = isBypass();
		return result;
	}
	
	public List<BindSpecNumResult> duplitWithCarrierAndMsgType() {
		BindSpecNumResult orgResult = this;
		
		List<BindSpecNumResult> tempResults = new ArrayList<BindSpecNumResult>();
		if (orgResult.getCarrierChannel().isSms()) {
			BindSpecNumResult destResult = orgResult.duplitBasicResult();
			destResult.setMsgType(MsgType.SMS.getIndex());
			tempResults.add(destResult);
		}
		if (orgResult.getCarrierChannel().isLongSms()) {
			BindSpecNumResult destResult = orgResult.duplitBasicResult();
			destResult.setMsgType(MsgType.LONGSMS.getIndex());
			tempResults.add(destResult);
		}
		if (orgResult.getCarrierChannel().isMms()) {
			BindSpecNumResult destResult = orgResult.duplitBasicResult();
			destResult.setMsgType(MsgType.MMS.getIndex());
			tempResults.add(destResult);
		}
		 
		List<BindSpecNumResult> targetResults = new ArrayList<BindSpecNumResult>();
		for (BindSpecNumResult result : tempResults) {
			for (Integer val : result.getCarrierChannel().getSupportCarriers()) {
				BindSpecNumResult destResult = result.duplitBasicResult();
				destResult.setCarrier(val);
				targetResults.add(destResult);
			}
		}
		
		return targetResults;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bizType == null) ? 0 : bizType.hashCode());
		result = prime * result + ((carrier == null) ? 0 : carrier.hashCode());
		result = prime * result + ((msgType == null) ? 0 : msgType.hashCode());
		result = prime * result + ((regionID == null) ? 0 : regionID.hashCode());
		result = prime * result + ((specsvsNumID == null) ? 0 : specsvsNumID.hashCode());
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
		BindSpecNumResult other = (BindSpecNumResult) obj;
		if (bizType == null) {
			if (other.bizType != null)
				return false;
		} else if (!bizType.equals(other.bizType))
			return false;
		if (carrier == null) {
			if (other.carrier != null)
				return false;
		} else if (!carrier.equals(other.carrier))
			return false;
		if (msgType == null) {
			if (other.msgType != null)
				return false;
		} else if (!msgType.equals(other.msgType))
			return false;
		if (regionID == null) {
			if (other.regionID != null)
				return false;
		} else if (!regionID.equals(other.regionID))
			return false;
		if (specsvsNumID == null) {
			if (other.specsvsNumID != null)
				return false;
		} else if (!specsvsNumID.equals(other.specsvsNumID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BindSpecNumResult [carrierId="+carrier.getIndex()+", type=" + type + ", specsvsNumID="
				+ specsvsNumID + ", regionID=" + regionID + ", msgType="
				+ msgType + ", bizType=" + bizType + "]";
	}
}

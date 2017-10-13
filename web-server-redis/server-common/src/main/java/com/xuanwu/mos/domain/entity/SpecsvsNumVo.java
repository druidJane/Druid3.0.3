package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-10-30
 * @Version 1.0.0
 */
public class SpecsvsNumVo extends AbstractEntity {
	private Integer id;
	/**
	 * 端口号
	 */
	private String basicNumber;
	
	/**
	 * 企业可扩展位数
	 */
	private Integer extendSize;
	/**
	 * 可发送运营商
	 */
	private String sendCarrier;
	/**
	 * 可发送运营商ID
	 */
	private Integer carrierId;
	/**
	 * 通道ID
	 */
	private Integer channelId;

	private String channelName;

	/**
	 * 当前通道中彩信最大容量
	 */
	private Integer mmsMaxLength;

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * 可发送运营商集合，用于页面展示
	 */
	private String carrierName;

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	/**
	 * 单价
	 */
	private BigDecimal price; 
	
	/**
	 * 可发送信息类型
	 */
	private Integer msgType;

	private Integer bizTypeId;

	public Integer getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(Integer bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	private Integer enterpriseId;
	/**
	 * '0:固定分配,指定分配端口号，系统不会添加其它端口号; 1:企业标识,系统会自动在端口号后添加企业识别码',
	 */
	private Integer assignType;

	public Integer getAssignType() {
		return assignType;
	}

	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"basicNumber\":").append(basicNumber);
		sb.append(",\"extendSize\":").append(extendSize);
		sb.append(",\"sendCarrier\":\"").append(sendCarrier).append("\"");
		sb.append(",\"price\":").append(price);
		sb.append(",\"msgType\":").append(msgType);
		sb.append(",\"enterpriseId\":").append(enterpriseId);
		sb.append('}');
		return sb.toString();
	}


	public String getBasicNumber() {
		return basicNumber;
	}


	public void setBasicNumber(String basicNumber) {
		this.basicNumber = basicNumber;
	}


	public Integer getExtendSize() {
		return extendSize;
	}


	public void setExtendSize(Integer extendSize) {
		this.extendSize = extendSize;
	}


	

	public String getSendCarrier() {
		return sendCarrier;
	}


	public void setSendCarrier(String sendCarrier) {
		this.sendCarrier = sendCarrier;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public Integer getMsgType() {
		return msgType;
	}


	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}


	public Integer getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getMmsMaxLength() {
		return mmsMaxLength;
	}

	public void setMmsMaxLength(Integer mmsMaxLength) {
		this.mmsMaxLength = mmsMaxLength;
	}
}

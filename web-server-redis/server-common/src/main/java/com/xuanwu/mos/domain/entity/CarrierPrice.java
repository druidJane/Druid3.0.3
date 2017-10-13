package com.xuanwu.mos.domain.entity;
/**
 * 运营商价格信息
 * @author <a href="lizongxian@139130.net">ZongXian.Li</a>
 *
 */
public class CarrierPrice {
	private int id;
	/** 运营商ID */
	private int carrier_id;
	/** 单价，0到0.9999 */
	private double price;
	/** 企业ID */
	private int enterprise_id;
	/** 信息类型1：短信，2：彩信，3.WAP_PUSH */
	private int msgType;
	/** 运营商名称 **/
	private String carrierName;

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCarrier_id() {
		return carrier_id;
	}
	public void setCarrier_id(int carrier_id) {
		this.carrier_id = carrier_id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getEnterprise_id() {
		return enterprise_id;
	}
	public void setEnterprise_id(int enterprise_id) {
		this.enterprise_id = enterprise_id;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
	
}

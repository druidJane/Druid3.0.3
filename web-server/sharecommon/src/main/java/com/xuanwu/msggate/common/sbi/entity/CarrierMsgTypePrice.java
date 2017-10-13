package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

/**
 * Carrier message type price
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2011-10-19
 * @Version 1.0.0
 */
public class CarrierMsgTypePrice {

	private Integer id;
	
	private Carrier carrier;
	
	private MsgType msgType;
	
	private Double price;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(int index) {
		this.carrier = Carrier.getType(index);
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(int index) {
		this.msgType = MsgType.getType(index);
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "AccountCarrierPrice [carrier=" + carrier + ", msgType="
				+ msgType + ", price=" + price + "]";
	}
}

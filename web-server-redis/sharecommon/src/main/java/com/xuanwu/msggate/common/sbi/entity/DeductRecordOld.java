/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;

/**
 * 
 * <p><pre>
 * Description:
 * </pre></p>
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2010-8-13
 * @Version 1.0.0
 * <p><pre>
 * 修改日期 修改人 修改内容
 * </pre></p>
 */
public class DeductRecordOld {
	
	public enum Type{
		PACK,FRAME
	}
	
	private long id;
	private long parentRcordId;
	private int capitalAccountId;
	private String msgPackId;
	private long msgFrameId;
	private float deductAmount;
	private float price;
	private Date deductTime;
	
	private Type type;

	public DeductRecordOld() {
		
	}
	
	public DeductRecordOld(int capitalAccountId,String msgPackId, float deductAmount){
		this.capitalAccountId = capitalAccountId;
		this.msgPackId = msgPackId;
		this.deductAmount = deductAmount;
	}
	
	public DeductRecordOld(int capitalAccountId,long msgFrameId, float deductAmount,float price){
		 
		this.capitalAccountId = capitalAccountId;
		this.msgFrameId = msgFrameId;
		this.deductAmount = deductAmount;
		this.price = price;
	 
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentRcordId() {
		return parentRcordId;
	}

	public void setParentRcordId(long parentRcordId) {
		this.parentRcordId = parentRcordId;
	}

	public int getCapitalAccountId() {
		return capitalAccountId;
	}

	public void setCapitalAccountId(int capitalAccountId) {
		this.capitalAccountId = capitalAccountId;
	}

	public String getMsgPackId() {
		return msgPackId;
	}

	public void setMsgPackId(String msgPackId) {
		this.msgPackId = msgPackId;
	}

	public long getMsgFrameId() {
		return msgFrameId;
	}

	public void setMsgFrameId(long msgFrameId) {
		this.msgFrameId = msgFrameId;
	}

	public float getDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(float deductAmount) {
		this.deductAmount = deductAmount;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getDeductTime() {
		return deductTime;
	}

	public void setDeductTime(Date deductTime) {
		this.deductTime = deductTime;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public int getTypeIndex() {
		return type.ordinal();
	}

}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

/**
 * Interface delegate to one sms or mms
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-4-30
 * @Version 1.0.0
 */
public interface MsgSingle {
	/**
	 * 获得当前短信的类型
	 * 
	 * @returnO
	 */
	public MsgType getType();
	
	/**
	 * 设置当前短信的类型
	 * 
	 * @param type
	 */
	public void setType(MsgType type);

	/**
	 * 获得当前短信的电话号码
	 * 
	 * @return the number
	 */
	public String getPhone();
	
	/**
	 * 设置当前短信的电话号码
	 * @return
	 */
	public void setPhone(String phone);

	/**
	 * 获得当前短信的内容
	 * 
	 * @return the content
	 */
	public MsgContent getContent();
	
	/**
	 * 设置当前短信的内容
	 * @param content
	 */
	public void setContent(MsgContent content);

	/**
	 * 获得当前客户附带的消息ID
	 * @return
	 */
	public String getCustomMsgID();
	/**
	 * 获取是否为 vip 号码
	 * @return
	 */
	public boolean isVip();
	
	/**
	 * 获取自定义消息扩展码
	 * @return
	 */
	public String getCustomNum();
	
	/**
	 * 获取手机号所属运营商
	 * @return
	 */
	public Carrier getCarrier();
	
	/**
	 * 设置手机号所属运营商
	 * @param carrier
	 */
	public void setCarrier(Carrier carrier);
	
	/**
	 * 设置手机号所属地区
	 * @param region
	 */
	public void setRegion(Region region);
	
	/**
	 * 获取手机号所属地区
	 * @param region
	 * @return
	 */
	public Region getRegion();
	
	
	/**
	 * 设置机构码
	 * @param region
	 */
	public void setOrgCode(String orgCode);
	
	/**
	 * 获取机构码
	 * @param region
	 * @return
	 */
	public String getOrgCode();
	
	/**
	 * 设置信息格式
	 * @param region
	 */
	public void setMsgFmt(int msgFmt);
	
	/**
	 * 获取信息格式
	 * @param region
	 * @return
	 */
	public int getMsgFmt();
	
	
	/**
	 * Build the protobuf data
	 * 
	 * @return
	 */
	public com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem build();
}

/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import java.io.Serializable;


/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-12-3
 * @Version 1.0.0
 */
public class ChannelStatistics extends BaseStatistics {

	/** 通道ID */
	private int channelId;

	/** 通道号 */
	private String channelNum;

	/** 通道名称 */
	private String channelName;

	/** 运营商ID */
	private int carrierId;

	/** 运行商名称 */
	private String carrierName;

	/** 状态报告标识 */
	private boolean stateReport;
	
	/** 状态报告标识 */
	private String stateReportStr;

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(int carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public boolean isStateReport() {
		return stateReport;
	}

	public void setStateReport(boolean stateReport) {
		this.stateReport = stateReport;
	}

	public String getStateReportStr() {
		return stateReportStr;
	}

	public void setStateReportStr(String stateReportStr) {
		this.stateReportStr = stateReportStr;
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}

}

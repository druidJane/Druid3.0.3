/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import java.util.Comparator;

import com.xuanwu.mos.domain.AbstractEntity;

/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-11-30
 * @Version 1.0.0
 */
public abstract class BaseStatistics extends AbstractEntity implements Comparator{

	/** 企业ID */
	protected int enterpriseId;
	
	/** 父ID */
	protected int parentId;

	/** 路径，查询时以避免递归 */
	protected String path;

	/** 短信类型 */
	protected int smsType;

	/** 操作类型 */
	protected int operType;

	/** 开始时间 */
	protected String beginTime;

	/** 结束时间 */
	protected String endTime;

	/** 开始月份 */
	protected String beginMonth;

	/** 结束月份 */
	protected String endMonth;

	/** 总接收量 */
	protected long allReceiveSum;

	/** 总提交量 */
	protected long allSubmitSum;

	/** 移动提交数量 */
	protected long submitSumYD;

	/** 联通提交数量 */
	protected long submitSumLT;

	/** 电信小灵通提交数量 */
	protected long submitSumXLT;

	/** 电信CDMA提交数量 */
	protected long submitSumCDMA;

	/** 子部门或者用户标志 */
	protected boolean subFlag;

	/** 当天时间 */
	protected String currentDate;

	/** 总发送量 */
	protected long allSendSum;

	/** 移动总发送量 */
	protected long ydSendSum;
	
	/** 联通总发送量 */
	protected long ltSendSum;
	
	/** 电信CMDA总发送量 */
	protected long cmdaSendSum;
	
	/** 电信小灵通总发送量 */
	protected long xltSendSum;
	
	/** 总成功量 */
	protected long allSuccessSum;

	/** 移动成功量 */
	protected long successSumYD;

	/** 联通成功量 */
	protected long successSumLT;

	/** 电信小灵通成功量 */
	protected long successSumXLT;

	/** 电信CDMA成功量 */
	protected long successSumCDMA;

	/** 所有失败数量 */
	protected long allFailSum;

	/** 运营商处理中数量 */
	protected long allCarrierSum;

	/** 成功率 */
	protected String successRate;

	/** 统计日期 */
	protected String statDate;

	/** 统计日期字符串 */
	protected String statDateStr;

	public String allReceiveSumStr;
	
	public String allSendSumStr;
	
	public String allSuccessSumStr;
	
	public String successSumYDStr;
	
	public String successSumLTStr;
	
	public String successSumXLTStr;
	
	public String successSumCDMAStr;
	
	private String succesRate; // 成功率
	
	/** 业务类型名称 */
	private String bizTypeName;	

	/** 用户名称 */
	private String userName;
	
	private String deptName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public String getSuccesRate() {
		return succesRate;
	}

	public void setSuccesRate(String succesRate) {
		this.succesRate = succesRate;
	}
	
	public String getAllReceiveSumStr() {
		return allReceiveSumStr;
	}

	public void setAllReceiveSumStr(String allReceiveSumStr) {
		this.allReceiveSumStr = allReceiveSumStr;
	}

	public String getAllSendSumStr() {
		return allSendSumStr;
	}

	public void setAllSendSumStr(String allSendSumStr) {
		this.allSendSumStr = allSendSumStr;
	}

	public String getAllSuccessSumStr() {
		return allSuccessSumStr;
	}

	public void setAllSuccessSumStr(String allSuccessSumStr) {
		this.allSuccessSumStr = allSuccessSumStr;
	}

	public String getSuccessSumYDStr() {
		return successSumYDStr;
	}

	public void setSuccessSumYDStr(String successSumYDStr) {
		this.successSumYDStr = successSumYDStr;
	}

	public String getSuccessSumLTStr() {
		return successSumLTStr;
	}

	public void setSuccessSumLTStr(String successSumLTStr) {
		this.successSumLTStr = successSumLTStr;
	}

	public String getSuccessSumXLTStr() {
		return successSumXLTStr;
	}

	public void setSuccessSumXLTStr(String successSumXLTStr) {
		this.successSumXLTStr = successSumXLTStr;
	}

	public String getSuccessSumCDMAStr() {
		return successSumCDMAStr;
	}

	public void setSuccessSumCDMAStr(String successSumCDMAStr) {
		this.successSumCDMAStr = successSumCDMAStr;
	}
	public int getEnterpriseId() {
		return enterpriseId;
	}
	
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSmsType() {
		return smsType;
	}

	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBeginMonth() {
		return beginMonth;
	}

	public void setBeginMonth(String beginMonth) {
		this.beginMonth = beginMonth;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public long getAllReceiveSum() {
		return allReceiveSum;
	}

	public void setAllReceiveSum(long allReceiveSum) {
		this.allReceiveSum = allReceiveSum;
	}

	public long getAllSubmitSum() {
		return allSubmitSum;
	}

	public void setAllSubmitSum(long allSubmitSum) {
		this.allSubmitSum = allSubmitSum;
	}

	public long getSubmitSumYD() {
		return submitSumYD;
	}

	public void setSubmitSumYD(long submitSumYD) {
		this.submitSumYD = submitSumYD;
	}

	public long getSubmitSumLT() {
		return submitSumLT;
	}

	public void setSubmitSumLT(long submitSumLT) {
		this.submitSumLT = submitSumLT;
	}

	public long getSubmitSumXLT() {
		return submitSumXLT;
	}

	public void setSubmitSumXLT(long submitSumXLT) {
		this.submitSumXLT = submitSumXLT;
	}

	public long getSubmitSumCDMA() {
		return submitSumCDMA;
	}

	public void setSubmitSumCDMA(long submitSumCDMA) {
		this.submitSumCDMA = submitSumCDMA;
	}

	public boolean isSubFlag() {
		return subFlag;
	}

	public void setSubFlag(boolean subFlag) {
		this.subFlag = subFlag;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public long getAllSendSum() {
		return allSendSum;
	}

	public void setAllSendSum(long allSendSum) {
		this.allSendSum = allSendSum;
	}

	public long getAllSuccessSum() {
		return allSuccessSum;
	}

	public void setAllSuccessSum(long allSuccessSum) {
		this.allSuccessSum = allSuccessSum;
	}

	public long getSuccessSumYD() {
		return successSumYD;
	}

	public void setSuccessSumYD(long successSumYD) {
		this.successSumYD = successSumYD;
	}

	public long getSuccessSumLT() {
		return successSumLT;
	}

	public void setSuccessSumLT(long successSumLT) {
		this.successSumLT = successSumLT;
	}

	public long getSuccessSumXLT() {
		return successSumXLT;
	}

	public void setSuccessSumXLT(long successSumXLT) {
		this.successSumXLT = successSumXLT;
	}

	public long getSuccessSumCDMA() {
		return successSumCDMA;
	}

	public void setSuccessSumCDMA(long successSumCDMA) {
		this.successSumCDMA = successSumCDMA;
	}

	public long getAllFailSum() {
		return allFailSum;
	}

	public void setAllFailSum(long allFailSum) {
		this.allFailSum = allFailSum;
	}

	public long getAllCarrierSum() {
		return allCarrierSum;
	}

	public void setAllCarrierSum(long allCarrierSum) {
		this.allCarrierSum = allCarrierSum;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public String getStatDateStr() {
		return statDateStr;
	}

	public void setStatDateStr(String statDateStr) {
		this.statDateStr = statDateStr;
	}

	public void add(BaseStatistics o){
		this.allCarrierSum += o.allCarrierSum;
		this.allFailSum += o.allFailSum;
		this.allReceiveSum += o.allReceiveSum;
		this.allSendSum += o.allSendSum;
		this.ydSendSum += o.ydSendSum;
		this.ltSendSum += o.ltSendSum;
		this.cmdaSendSum += o.cmdaSendSum;
		this.xltSendSum += o.xltSendSum;
		this.allSubmitSum += o.allSubmitSum;
		this.allSuccessSum += o.allSuccessSum;
		this.submitSumCDMA += o.submitSumCDMA;
		this.submitSumLT += o.submitSumLT;
		this.submitSumXLT += o.submitSumXLT;
		this.submitSumYD += o.submitSumYD;
		this.successSumCDMA += o.successSumCDMA;
		this.successSumLT += o.successSumLT;
		this.successSumXLT += o.successSumXLT;
		this.successSumYD += o.successSumYD;
	}

	public long getYdSendSum() {
		return ydSendSum;
	}

	public void setYdSendSum(long ydSendSum) {
		this.ydSendSum = ydSendSum;
	}

	public long getLtSendSum() {
		return ltSendSum;
	}

	public void setLtSendSum(long ltSendSum) {
		this.ltSendSum = ltSendSum;
	}

	public long getCmdaSendSum() {
		return cmdaSendSum;
	}

	public void setCmdaSendSum(long cmdaSendSum) {
		this.cmdaSendSum = cmdaSendSum;
	}

	public long getXltSendSum() {
		return xltSendSum;
	}

	public void setXltSendSum(long xltSendSum) {
		this.xltSendSum = xltSendSum;
	}
}

package com.xuanwu.mos.domain.entity;

import java.io.Serializable;

import com.xuanwu.mos.domain.AbstractEntity;

public class BillingAcountInfo extends AbstractEntity{

	private String accountName;
	private String deductTime;
	private String deductTimeStr;
	private String deductTime4Grid;
	public String getDeductTime4Grid() {
		return deductTime4Grid;
	}

	public void setDeductTime4Grid(String deductTime4Grid) {
		this.deductTime4Grid = deductTime4Grid;
	}

	private double sumConsume;
	private double smsConsume;
	private double mmsConsume;
	private String sumConsumeStr;
	private String smsConsumeStr;
	private String mmsConsumeStr;
	private int parentId;
	
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getSumConsumeStr() {
		return sumConsumeStr;
	}

	public void setSumConsumeStr(String sumConsumeStr) {
		this.sumConsumeStr = sumConsumeStr;
	}

	public String getSmsConsumeStr() {
		return smsConsumeStr;
	}

	public void setSmsConsumeStr(String smsConsumeStr) {
		this.smsConsumeStr = smsConsumeStr;
	}

	public String getMmsConsumeStr() {
		return mmsConsumeStr;
	}

	public void setMmsConsumeStr(String mmsConsumeStr) {
		this.mmsConsumeStr = mmsConsumeStr;
	}

	private int userId;
	private int entId;
	private int captialAccuntId;
	private String beginDate;
	private String endDate;
	private String beginMonth;
	private String endMonth;
	private int operType;
	private String userName;
	private String linkMan;
	private String entName;
	private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

	public int getCaptialAccuntId() {
		return captialAccuntId;
	}

	public void setCaptialAccuntId(int captialAccuntId) {
		this.captialAccuntId = captialAccuntId;
	}

	public String getDeductTimeStr() {
		return deductTimeStr;
	}

	public void setDeductTimeStr(String deductTimeStr) {
		this.deductTimeStr = deductTimeStr;
	}
	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDeductTime() {
		return deductTime;
	}

	public void setDeductTime(String deductTime) {
		this.deductTime = deductTime;
	}

	public double getSumConsume() {
		return sumConsume;
	}

	public void setSumConsume(double sumConsume) {
		this.sumConsume = sumConsume;
	}

	public double getSmsConsume() {
		return smsConsume;
	}

	public void setSmsConsume(double smsConsume) {
		this.smsConsume = smsConsume;
	}

	public double getMmsConsume() {
		return mmsConsume;
	}

	public void setMmsConsume(double mmsConsume) {
		this.mmsConsume = mmsConsume;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.xuanwu.msggate.common.sbi.entity;

/**
 * Count account
 * 
 * @author <a href="mailto:jiji@javawind.com">Xuefang Xu</a>
 * @Data 2013-06-24
 * @Version 1.0.0
 */
public class CountAccount {
	private int id;// 计费帐号ID
	private CountAccount parentAccount;// 计费父帐号
	private int smsLimit;// 短信限制数量
	private int smsRemain;// 短信剩余数量
	private int mmsLimit;// 彩信限制数量
	private int mmsRemain;// 彩信剩余数量
	private int limitType;// 限制类型(1:短彩信合一共用短信限制，2:短彩信分开限制)
	private int smsHasSend;//每日短信已发送量
	private int mmsHasSend;//每日彩信已发送量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CountAccount getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(CountAccount parentAccount) {
		this.parentAccount = parentAccount;
	}

	public int getSmsLimit() {
		return smsLimit;
	}

	public void setSmsLimit(int smsLimit) {
		this.smsLimit = smsLimit;
	}

	public int getSmsRemain() {
		return smsRemain;
	}

	public void setSmsRemain(int smsRemain) {
		this.smsRemain = smsRemain;
	}

	public int getMmsLimit() {
		return mmsLimit;
	}

	public void setMmsLimit(int mmsLimit) {
		this.mmsLimit = mmsLimit;
	}

	public int getMmsRemain() {
		return mmsRemain;
	}

	public void setMmsRemain(int mmsRemain) {
		this.mmsRemain = mmsRemain;
	}

	public int getLimitType() {
		return limitType;
	}

	public void setLimitType(int limitType) {
		this.limitType = limitType;
	}

	public int getSmsHasSend() {
		return smsHasSend;
	}

	public void setSmsHasSend(int smsHasSend) {
		this.smsHasSend = smsHasSend;
	}

	public int getMmsHasSend() {
		return mmsHasSend;
	}

	public void setMmsHasSend(int mmsHasSend) {
		this.mmsHasSend = mmsHasSend;
	}

}

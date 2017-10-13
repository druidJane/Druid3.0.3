/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;

/**
 * State Report
 *
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-19
 * @Version 1.0.0
 */
public interface StateReport {
	public enum StateReportResult {
		DELIVERED(0), EXPIRED(1), UNDELIVERABLE(2), REJECTED(3), UNKNOWN(4), DELETED(
		        5), WAIT_SEND(6),
		        /*新增模拟状态报告类型*/
		        BLACK(10),ILLEGALKEY(11),REGION_LIMIT(12),NULL_SPECNUM(13),ILLEGAL_PHONE(14),
		        NON_WHITELIST(15),ILLEGAL_MMS(16),MMS_OVER_LENGTH(17),MMS_OVER_TYPE(18),
		        REPEAT_PHONE(19),OUTOF_CARRIER(20),BLANK_CONTENT(21),
		        NUMBER_LIMIT(22),ILLEGAL_VOICE_CODE(23),AUDIT_DOES_NOT_PASS(24),CANCEL(25);
		private final int index;

		private StateReportResult(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static StateReportResult getType(int index) {
			switch (index) {
			case 0:
				return DELIVERED;
			case 1:
				return EXPIRED;
			case 2:
				return UNDELIVERABLE;
			case 3:
				return REJECTED;
			case 5:
				return DELETED;
			case 6:
				return WAIT_SEND;
			default:
				return UNKNOWN;
			}
		}
		
		public static StateReportResult getSimulateReportType(int index){
			switch (index) {
			case 1:
				return BLACK;
			case 3:
				return ILLEGALKEY;
			case 4:
				return REGION_LIMIT;
			case 5:
				return CANCEL;
			case 10:
				return NULL_SPECNUM;
			case 11:
				return ILLEGAL_PHONE;
			case 13:
				return NON_WHITELIST;
			case 14:
				return ILLEGAL_MMS;
			case 16:
				return MMS_OVER_LENGTH;
			case 17:
				return MMS_OVER_TYPE;
			case 18:
				return REPEAT_PHONE;
			case 20:
				return OUTOF_CARRIER;
			case 21:
				return AUDIT_DOES_NOT_PASS;
			case 22:
				return BLANK_CONTENT;
			case 23:
				return NUMBER_LIMIT;
			case 24:
				return ILLEGAL_VOICE_CODE;
			default:
				return UNKNOWN;
			}
		}
	}

	public String getMsgID();

	public void setMsgID(String msgID);
	
	public Date getPostTime();

	public void setPostTime(Date postTime);

	public Date getSubmitTime();

	public void setSubmitTime(Date submitTime);

	public Date getDoneTime();

	public void setDoneTime(Date doneTime);

	public StateReportResult getState();

	public void setState(StateReportResult state);

	public void setStateIndex(int index);

	public abstract void setSmscSequence(long smscSequence);

	public abstract long getSmscSequence();

	public abstract void setDestPhone(String destPhone);

	public abstract String getDestPhone();

	public abstract void setId(long id);

	public abstract long getId();
	
	public String getOriginResult();

	public void setOriginResult(String originResult);
	
	public int getUserID();
	
	public void setUserID(int userID);
	
	public int getEnterpriseID();
	
	public void setEnterpriseID(int enterpriseID);
	
	public String getPackID();
	
	public void setPackID(String packID);
    
	public int getChannelID();

	public void setChannelID(int channelID);
	
	public String getCustomMsgID();
	
	public void setCustomMsgID(String customMsgID);
	
	public void setSuffix(String suffix);
	
	public String getSuffix();

	public void setCreateTime(Date createTime);

	public Date getCreateTime();

}

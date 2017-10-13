/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame.BizForm;

import java.io.Serializable;
import java.util.Date;


public class PackStatInfo extends AbstractEntity {

	/** 提交结果 */
	private Integer sendResult;
	/** 状态报告号码数 */
	private Integer reportResult;
	/** 信息帧业务类型 */
	private BizForm bizForm;
	/** 统计数量 */
	private Integer statCount;

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	private Date scheduleTime;

	public Integer getSendResult() {
		return sendResult;
	}

	public void setSendResult(Integer sendResult) {
		this.sendResult = sendResult;
	}

	public Integer getReportResult() {
		return reportResult;
	}

	public void setReportResult(Integer reportResult) {
		this.reportResult = reportResult;
	}

	public BizForm getBizForm() {
		return bizForm;
	}

	public void setBizForm(Integer index) {
		this.bizForm = BizForm.getForm(index);
	}

	public Integer getStatCount() {
		return statCount;
	}

	public void setStatCount(Integer statCount) {
		this.statCount = statCount;
	}

	@Override
	public Serializable getId() {
		return null;
	}
}

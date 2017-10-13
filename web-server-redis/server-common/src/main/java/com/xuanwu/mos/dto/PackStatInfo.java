/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.dto;

import com.xuanwu.msggate.common.sbi.entity.MsgFrame.BizForm;

/**
 * @Description:
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-11-1
 * @Version 1.0.0
 */
public class PackStatInfo {

	/** 提交结果 */
	private Integer sendResult;
	/** 状态报告结果 */
	private Integer reportResult;
	/** 信息帧业务类型 */
	private BizForm bizForm;
	/** 统计数量 */
	private Integer statCount;

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

}

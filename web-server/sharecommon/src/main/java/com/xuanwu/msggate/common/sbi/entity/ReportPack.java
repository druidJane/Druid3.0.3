/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.protobuf.CommonItem.StateItem;
import com.xuanwu.msggate.common.sbi.entity.StateReport.StateReportResult;
import com.xuanwu.msggate.common.sbi.entity.impl.PStateReport;
import com.xuanwu.msggate.common.util.DateUtil;
import com.xuanwu.msggate.common.zip.ZipUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-8-17
 * @Version 1.0.0
 */
public class ReportPack {
	private Long id;
	private int size;
	private byte[] reports;
	private Date submitTime;

	public ReportPack() {
	}

	public ReportPack(List<StateReport> reports) throws IOException {
		com.xuanwu.msggate.common.protobuf.CommonItem.ReportPack.Builder reportPack = com.xuanwu.msggate.common.protobuf.CommonItem.ReportPack
				.newBuilder();
		for (StateReport report : reports) {
			StateItem.Builder itemBuilder = StateItem.newBuilder();
			itemBuilder.setMsgID(report.getMsgID());
			itemBuilder.setState(report.getState().ordinal());
			itemBuilder.setSubmitTime(DateUtil
					.parseDate(report.getSubmitTime()));
			itemBuilder.setDoneTime(DateUtil.parseDate(report.getDoneTime()));
			itemBuilder.setPostTime(DateUtil.parseDate(report.getPostTime()));
			itemBuilder.setDestPhone(report.getDestPhone());
			itemBuilder.setSmscSequence(report.getSmscSequence());
			itemBuilder.setState(report.getState().ordinal());
			itemBuilder.setOriginResult((report.getOriginResult() == null) ? ""
					: report.getOriginResult());
			itemBuilder.setUserID(report.getUserID());
			itemBuilder.setEnterpriseID(report.getEnterpriseID());
			itemBuilder.setChannelID(report.getChannelID());
			itemBuilder.setCustomMsgID(report.getCustomMsgID() == null ? "" :  report.getCustomMsgID());
			// itemBuilder.setBatchID(UUIDUtil.tranUUID2Builder(UUID.fromString(report.getPackID())));
			itemBuilder.setReserve((report.getPackID() == null) ? "" : report
					.getPackID());// faster
			itemBuilder.setCreateTime(DateUtil.parseDate(report.getCreateTime()));
			reportPack.addStateItem(itemBuilder);
		}

		this.reports = ZipUtil.zipByteArray(reportPack.build().toByteArray());
		this.size = reports.size();
		this.submitTime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getReports() {
		return reports;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public void setReports(byte[] reports) {
		this.reports = reports;
	}

	private com.xuanwu.msggate.common.protobuf.CommonItem.ReportPack build() {
		try {
			return com.xuanwu.msggate.common.protobuf.CommonItem.ReportPack
					.parseFrom(reports);
		} catch (Exception e) {
			throw new RuntimeException(
					"Parse ReportPack exception occured, cause by:", e);
		}
	}

	public List<StateReport> getReportList() {
		List<StateReport> reports = new ArrayList<StateReport>();

		com.xuanwu.msggate.common.protobuf.CommonItem.ReportPack pack = build();
		List<StateItem> stateItemList = pack.getStateItemList();

		for (StateItem stateItem : stateItemList) {
			StateReport report = new PStateReport(stateItem.getMsgID(),
					stateItem.getSmscSequence(), stateItem.getDestPhone(),
					DateUtil.tranDate(stateItem.getSubmitTime()), DateUtil
							.tranDate(stateItem.getDoneTime()),
					StateReportResult.values()[stateItem.getState()], stateItem
							.getOriginResult());
			report.setUserID(stateItem.getUserID());
			report.setEnterpriseID(stateItem.getEnterpriseID());
			report.setChannelID(stateItem.getChannelID());
			report.setCustomMsgID(stateItem.getCustomMsgID());
			// report.setPackID(UUIDUtil.tranBuilder2UUID(stateItem.getBatchID()).toString());
			report.setPackID(stateItem.getReserve());
			Date postTime = DateUtil.tranDate(stateItem.getPostTime());
			report.setPostTime((postTime == null) ? Calendar.getInstance().getTime() : postTime);
			report.setCreateTime(DateUtil.tranDate(stateItem.getCreateTime()));
			reports.add(report);
		}

		return reports;
	}
}

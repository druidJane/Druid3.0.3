/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.sbi;

import com.xuanwu.msggate.common.sbi.entity.MOTicket;
import com.xuanwu.msggate.common.sbi.entity.ReportPack;
import com.xuanwu.msggate.common.sbi.entity.StateReport;
import com.xuanwu.msggate.common.sbi.exception.NotExistException;

import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-14
 * @Version 1.0.0
 */
public interface MOHandler {
	
	/**
	 * Report the send state from the carrier gate.
	 *
	 * @param requestUUID
	 * @param mtoIdentity
	 * @param reportPacks
	 *
	 * @throws Exception
	 */
	public void commitReportPack(UUID requestUUID, String mtoIdentity,
			List<ReportPack> reportPacks) throws NotExistException, Exception;
	
	/**
	 * Report the send state from the carrier gate.
	 *
	 * @param requestUUID
	 * @param mtoIdentity
	 * @param stateReports
	 *
	 * @throws Exception
	 */
	public void commitStateReports(UUID requestUUID, String mtoIdentity,
			List<StateReport> reportPacks) throws NotExistException, Exception;;

	/**
	 * MO message report
	 *
	 * @param moMessage
	 *
	 * @throws Exception
	 */
	public void moReport(UUID requestUUID, String mtoIdentity, List<MOTicket> moMessage)
			throws NotExistException, Exception;;
}

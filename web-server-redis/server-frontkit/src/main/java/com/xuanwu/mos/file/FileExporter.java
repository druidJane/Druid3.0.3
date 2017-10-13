package com.xuanwu.mos.file;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.file.exporter.BaseExporter;
import com.xuanwu.mos.file.exporter.ExportInfo;
import com.xuanwu.mos.file.handler.FileHandler;
import com.xuanwu.mos.rest.service.*;
import com.xuanwu.mos.rest.service.msgservice.MoTicketService;
import com.xuanwu.mos.rest.service.statistics.BillingCountStatisticsService;
import com.xuanwu.mos.rest.service.statistics.StatisticsService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.vo.MoTicketVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 林泽强 on 2016/8/25. 文件导出器
 */
@Component
public class FileExporter extends BaseExporter {

	private static final Logger logger = LoggerFactory.getLogger(FileExporter.class);
	@Autowired
	private ContactService cs;
	@Autowired
	private FileTaskService fileTaskService;
	@Autowired
	private MoTicketService moTicketService;
	@Autowired
	private KeyWordService keyWordService;
	@Autowired
	private DataConverter converter;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private BlackListService blackListService;
	@Autowired
	private StatisticsService statService;
	@Autowired
	private UserMgrService userMgrService;
	@Autowired
	private BillingCountStatisticsService billStatService;

	public ExportInfo batchExport(FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {
		int fileRecords = config.getPerFileRecords(fileType);
		String delimiter = task.getParameter("delimiter");
		// String platform = task.getParameter("platform");//
		// 0:Backend，2：FrontKit
		// boolean isBackend = platform != null && platform.equals("0");
		if ("\\t".equals(delimiter)) {
			delimiter = "\t";
		}
		ExportInfo exportInfo = new ExportInfo(fileType, task.getDataType(), config.getContextPath(), fileRecords);
		switch (task.getDataType()) {
		case Blacklist:
			exportBlacklist(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case Keyword:
			exprotKeyWord(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case ChargeAccount:
			exportChargeRecord(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case Contact:
			exportContact(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case DeptSendStat:
			exportDeptStat(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case UsersSendStat:
			exportUserSendStat(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case BizTypeSendStat:
			exportBizTypeStat(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case BillCountsStat:
			exportBillCountStat(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case UserSendDetailStat:
			exportUserSendDetailStat(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case BillCountDetailStat:
			exportBillCountDetailStat(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case User:
			exportUser(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		case MoTicket:
			exportMoTicker(fileRecords, delimiter, exportInfo, task, fileType, fileHandler);
			break;
		default:
			break;
		}
		return exportInfo;
	}

	private void exportMoTicker(int fileRecords, String delimiter, ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {
		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		for(Map.Entry<String, String> entry :task.getParamsMap().entrySet()){
			queryParameters.addParam(entry.getKey(),entry.getValue());
		}
		if(null != queryParameters.getParams().get("userIds")){
			String userIds = (String) queryParameters.getParams().get("userIds");
			userIds = userIds.replaceAll("\\[","").replaceAll("\\]","");
			String[] userId = userIds.split(Delimiters.COMMA);
			if(userId.length>0){
				List<Integer> ids = new ArrayList<>();
				for(String id :userId){
					ids.add(Integer.valueOf(id.trim()));
				}
				queryParameters.addParam("userIds",ids);
			}
		}
		queryParameters.addSort("id", "asc");
		int total = moTicketService.findMoTicketsCount(queryParameters);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<MoTicketVo> moTicketList = ListUtil.map2List(moTicketService.findMoTickets(queryParameters));
			wirteDataToFile(delimiter, FileHeader.MOTICKET, task, exportInfo, fileHandler, moTicketList);
		}
		logger.info("Export MoTicker total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);
	}

	private void exportUser(int fileRecords, String delimiter, ExportInfo exportInfo,
							FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {
		long start = System.currentTimeMillis();
		Integer dataScopeId = Integer.valueOf(task.getParameter("dataScopeId"));
		String userPath = task.getParameter("userPath");
		QueryParameters params = new QueryParameters();
		for (Map.Entry<String, String> entry : task.getParamsMap().entrySet()) {
			if ("showAllChild".equals(entry.getKey())) {
				params.addParam("showAllChild", Boolean.valueOf(entry.getValue()));
			} else {
				params.addParam(entry.getKey(), entry.getValue());
			}
		}
		if (params.getParams().get("path") == null) {
			if (DataScope.DEPARTMENT.equals(DataScope.getScope(dataScopeId))) {
				params.addParam("path", userPath);
			}
		}
		int total = userMgrService.countUser(params);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			params.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<User> users = userMgrService.listUsers(params);
			wirteDataToFile(delimiter, FileHeader.USER_MGR_FRONTKIT_EXPORT, task, exportInfo, fileHandler, users);
		}
		logger.info("Export user total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);
	}

	private void exportChargeRecord(int fileRecords, String delimiter,
									ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {
		long start = System.currentTimeMillis();
		QueryParameters params = new QueryParameters();
		for (Map.Entry<String, String> entry : task.getParamsMap().entrySet()) {
			params.addParam(entry.getKey(), entry.getValue());
		}
		int total = chargeRecordService.count(params);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			params.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<ChargeRecord> records = chargeRecordService.list(params);
			wirteDataToFile(delimiter, FileHeader.CHARGE_RECORD_EXPORT, task, exportInfo, fileHandler, records);
		}
		logger.info("Export charge record total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);
	}

	private void exportContact(int fileRecords, String delimiter, ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {
		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("_lk_name", task.getParameter("_lk_name"));
		queryParameters.addParam("_lk_phone", task.getParameter("_lk_phone"));
		queryParameters.addParam("sex", task.getParameter("sex"));
		String showChild = (String)task.getParameter("showChild");
		boolean showChildboo = Boolean.valueOf(showChild);
		queryParameters.addParam("showChild", showChildboo);
		queryParameters.addParam("_gt_beginDate", task.getParameter("_gt_beginDate"));
		queryParameters.addParam("_lt_endDate", task.getParameter("_lt_endDate"));
		queryParameters.addParam("vip", task.getParameter("vip"));
		queryParameters.addParam("groupId", task.getParameter("groupId"));
		queryParameters.addParam("path", task.getParameter("path"));
		queryParameters.addParam("_lk_identifier", task.getParameter("_lk_identifier"));
		//导出个人通讯录，type = 0
		queryParameters.addParam("userId", task.getUserId());
		queryParameters.addParam("type", 0);
		queryParameters.addSort("id", "asc");
		int total = cs.findContactsCount(queryParameters);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<Contact> contactList = ListUtil.map2List(cs.findContacts(queryParameters));
			wirteDataToFile(delimiter, FileHeader.CONTACT, task, exportInfo, fileHandler, contactList);
		}
		logger.info("Export Contact total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);
	}

	private void exprotKeyWord(int fileRecords, String delimiter, ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {
		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("_lk_keywordName", task.getParameter("_lk_keywordName"));
		queryParameters.addParam("targetId", task.getParameter("targetId"));
		queryParameters.addSort("id", "asc");
		int total = keyWordService.findKeywordCount(queryParameters);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<KeyWord> keyWordList = ListUtil.map2List(keyWordService.findKeywords(queryParameters));
			wirteDataToFile(delimiter, FileHeader.KEYWORD, task, exportInfo, fileHandler, keyWordList);
		}
		logger.info("Export KeyWord total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);
	}


	private void exportBlacklist(int fileRecords, String delimiter, ExportInfo exportInfo, FileTask task,
			FileType fileType, FileHandler fileHandler) throws Exception {
		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("type", task.getParameter("type"));
		queryParameters.addParam("targetName", task.getParameter("targetName"));
		queryParameters.addParam("phone", task.getParameter("phone"));
		queryParameters.addParam("handleFrom", task.getPlatformId());
		queryParameters.addSort("id", "asc");
		int total = blackListService.findBlacklistCount(queryParameters);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<BlackList> blacklists = ListUtil.map2List(blackListService.findBlacklistsDetail(queryParameters));
			wirteDataToFile(delimiter, FileHeader.BLACKLIST, task, exportInfo, fileHandler, blacklists);
		}
		logger.info("Export Blacklists total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);

	}
	private void exportDeptStat(int fileRecords, String delimiter,
			ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {

		long start = System.currentTimeMillis();
		Integer operType = Integer.valueOf(task.getParameter("operType"));
		Integer deptId = Integer.valueOf(task.getParameter("deptId"));
		String beginDate =(String) task.getParameter("beginDate");
		String endDate =(String) task.getParameter("endDate");
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("enterpriseId", task.getParameter("enterpriseId"));
		queryParameters.addParam("deptId",deptId);
		queryParameters.addParam("operType",operType);
		queryParameters.addParam("beginDate", task.getParameter("beginDate"));
		queryParameters.addParam("endDate", task.getParameter("endDate"));
		queryParameters.addParam("smsType", task.getParameter("smsType"));
		queryParameters.addParam("path", task.getParameter("path"));
		queryParameters.addParam("handleFrom", task.getPlatformId());
		List<DepartmentStatistics> deptStatisticsList = null;
		int total = 0;		
		if(operType ==1){	
			queryParameters.addParam("querysplit", "true");
		    total = statService.findDeptSendCount(queryParameters);
			if (total <= 0) {
				exportInfo.setResult(ExportResult.EMPTY_DATA);
				return;
			}		
			task.setTotal(total);
			PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
			int pages = page.getPages();
			exportInfo.createDirectory();
			for (int i = 1; i <= pages; i++) {
				queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
				deptStatisticsList = statService.deptHistoryStatistics(queryParameters);
				wirteDataToFile(delimiter, FileHeader.DEPT_SEND_STAT, task, exportInfo, fileHandler, deptStatisticsList);
			}
		}else{
			queryParameters.addParam("beginDate", DateUtil.toMonthFirstDay(beginDate));
			queryParameters.addParam("endDate", DateUtil.toMonthLastDay(endDate));
			deptStatisticsList  = statService.deptHistoryStatistics(queryParameters);
			if(ListUtil.isNotBlank(deptStatisticsList)){
				total = deptStatisticsList.size();
				task.setTotal(total);
				PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
				int pages = page.getPages();
				exportInfo.createDirectory();
				for (int i = 1; i <= pages; i++) {
					queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
					List<DepartmentStatistics> tempList = null;
					tempList = deptStatisticsList.subList(queryParameters.getPage().getFrom(), queryParameters.getPage().getTo());
					wirteDataToFile(delimiter, FileHeader.DEPT_SEND_STAT, task, exportInfo, fileHandler, tempList);
				}
			}else{
				return;
			}
		}
		logger.info("Export deptStatlists total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);

	}
	/**
	 * 导出业务类型统计数据
	 * @param fileRecords
	 * @param delimiter
	 * @param exportInfo
	 * @param task
	 * @param fileType
	 * @param fileHandler
	 * @throws Exception
	 */
	private void exportBizTypeStat(int fileRecords, String delimiter,
			ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {

		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("bizType", task.getParameter("bizType"));
		queryParameters.addParam("operType", Integer.valueOf(task.getParameter("operType")));
		queryParameters.addParam("beginDate", task.getParameter("beginDate"));
		queryParameters.addParam("endDate", task.getParameter("endDate"));
		queryParameters.addParam("smsType", task.getParameter("smsType"));
		queryParameters.addParam("format", task.getParameter("format"));
		queryParameters.addParam("enterpriseId", task.getParameter("enterpriseId"));
		queryParameters.addParam("handleFrom", task.getPlatformId());
		
		int operType = Integer.valueOf(task.getParameter("operType"));
		String beginDate =(String) task.getParameter("beginDate");
		String endDate =(String) task.getParameter("endDate");
		int total =0;
		if(operType ==1){	
		    total = statService.bizTypeStatisticsCount(queryParameters);
			if (total <= 0) {
				exportInfo.setResult(ExportResult.EMPTY_DATA);
				return;
			}
			task.setTotal(total);
			PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
			int pages = page.getPages();
			exportInfo.createDirectory();
			for (int i = 1; i <= pages; i++) {
				queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
				List<BizTypeStatistics> bizStatlists = statService.bizTypeStatistics(queryParameters);
				wirteDataToFile(delimiter, FileHeader.BIZTYPE_SEND_STAT, task, exportInfo, fileHandler, bizStatlists);
			}
		}else{
			queryParameters.addParam("beginDate", DateUtil.toMonthFirstDay(beginDate));
			queryParameters.addParam("endDate", DateUtil.toMonthLastDay(endDate));
			List<BizTypeStatistics> bizStatlists  = statService.bizTypeStatistics(queryParameters);
			if(ListUtil.isNotBlank(bizStatlists)){
				total = bizStatlists.size();
				task.setTotal(total);
				PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
				int pages = page.getPages();
				exportInfo.createDirectory();
				for (int i = 1; i <= pages; i++) {
					queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
					List<BizTypeStatistics> tempList = null;
					tempList = bizStatlists.subList(queryParameters.getPage().getFrom(), queryParameters.getPage().getTo());
					wirteDataToFile(delimiter, FileHeader.DEPT_SEND_STAT, task, exportInfo, fileHandler, tempList);
				}
			}else{
				return;
			}
		}
		logger.info("Export bizStatlists total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);

	}
	/**
	 * 导出用户发送量统计数据
	 * @param fileRecords
	 * @param delimiter
	 * @param exportInfo
	 * @param task
	 * @param fileType
	 * @param fileHandler
	 * @throws Exception
	 */
	private void exportUserSendStat(int fileRecords, String delimiter,
			ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {

		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("path", task.getParameter("path"));
		queryParameters.addParam("userId", task.getParameter("userId"));
		queryParameters.addParam("beginDate", task.getParameter("beginDate"));
		queryParameters.addParam("endDate", task.getParameter("endDate"));
		queryParameters.addParam("smsType", task.getParameter("smsType"));
		queryParameters.addParam("handleFrom", task.getPlatformId());

//		queryParameters.addSort("id", "asc");
		int total = statService.findUserHistorySendCount(queryParameters);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<UserStatistics> userStatlists = ListUtil.map2List(statService.userHistoryStatistics(queryParameters));
			wirteDataToFile(delimiter, FileHeader.USERS_SEND_STAT, task, exportInfo, fileHandler, userStatlists);
		}

		logger.info("Export userStatlists total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);

	}
	/**
	 * 导出计费账户统计数据
	 * @param fileRecords
	 * @param delimiter
	 * @param exportInfo
	 * @param task
	 * @param fileType
	 * @param fileHandler
	 * @throws Exception
	 */
	private void exportBillCountStat(int fileRecords, String delimiter,
			ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {

		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("accountName", task.getParameter("accountName"));
		queryParameters.addParam("beginDate", task.getParameter("beginDate"));
		queryParameters.addParam("endDate", task.getParameter("endDate"));
		queryParameters.addParam("entId", task.getParameter("entId"));
		queryParameters.addParam("handleFrom", task.getPlatformId());
		queryParameters.addParam("format", task.getParameter("format"));
//		queryParameters.addSort("id", "asc");
		int total = billStatService.findBillingAccountInfoByDaysCount(queryParameters);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<BillingAcountInfo> bizStatlists = billStatService.findBillingAccountInfoByDaysList(queryParameters);
			wirteDataToFile(delimiter, FileHeader.BILLCOUNTS_SEND_STAT, task, exportInfo, fileHandler, bizStatlists);
		}

		logger.info("Export bizStatlists total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);

	}

	private void exportBillCountDetailStat(int fileRecords, String delimiter,
			ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {

		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("accuntId", task.getParameter("accuntId"));
		queryParameters.addParam("endId", task.getParameter("endId"));
		queryParameters.addParam("format", task.getParameter("format"));
		queryParameters.addParam("beginDate", task.getParameter("beginDate"));
		queryParameters.addParam("endDate", task.getParameter("endDate"));		
		queryParameters.addParam("operType", Integer.valueOf(task.getParameter("operType")));
		queryParameters.addParam("handleFrom", task.getPlatformId());

//		queryParameters.addSort("id", "asc");
		int total = billStatService.queryCountAccountByEntID(queryParameters);
		if (total <= 0) {
			exportInfo.setResult(ExportResult.EMPTY_DATA);
			return;
		}
		task.setTotal(total);
		PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
		int pages = page.getPages();
		exportInfo.createDirectory();
		for (int i = 1; i <= pages; i++) {
			queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
			List<BillingAcountInfo> bizStatlists = billStatService.queryUserListAccountByEntID(queryParameters);
			wirteDataToFile(delimiter, FileHeader.BILLCOUNT_SENDDETAIL_STAT, task, exportInfo, fileHandler, bizStatlists);
		}

		logger.info("Export bizStatlists total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);

	}
	private void exportUserSendDetailStat(int fileRecords, String delimiter,
			ExportInfo exportInfo, FileTask task, FileType fileType, FileHandler fileHandler) throws Exception {

		long start = System.currentTimeMillis();
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("userId", task.getParameter("userId"));
		queryParameters.addParam("format", task.getParameter("format"));
		queryParameters.addParam("smsType", task.getParameter("smsType"));
		queryParameters.addParam("beginDate", task.getParameter("beginDate"));
		queryParameters.addParam("endDate", task.getParameter("endDate"));
		queryParameters.addParam("enterpriseId", task.getParameter("enterpriseId"));
		queryParameters.addParam("operType", Integer.valueOf(task.getParameter("operType")));
		queryParameters.addParam("handleFrom", task.getPlatformId());

		int total=0;

			List<UserStatistics> sumUserList4Count = statService.selectedUserStatisticsList(queryParameters);
			if(ListUtil.isNotBlank(sumUserList4Count)){		
			    total=sumUserList4Count.size();
				task.setTotal(total);
				PageInfo page = new PageInfo(1, config.getMaxPerExportRecords(), total);
				int pages = page.getPages();
				exportInfo.createDirectory();
				for (int i = 1; i <= pages; i++) {
					queryParameters.setPage(new PageInfo(i, config.getMaxPerExportRecords(), total));
					List<UserStatistics> tempList = null;
					tempList = sumUserList4Count.subList(queryParameters.getPage().getFrom(), queryParameters.getPage().getTo());
					wirteDataToFile(delimiter, FileHeader.USER_SENDDETAIL_STAT, task, exportInfo, fileHandler, tempList);
				}
				
			}else{
				exportInfo.setResult(ExportResult.EMPTY_DATA);
				return;
			}
		
		logger.info("Export userDetailStatlists total:{} eplase time:{}", total, (System.currentTimeMillis() - start));
		exportInfo.setResult(ExportResult.SUCCESS);
	}
	private <E> void wirteDataToFile(String delimiter, String[] fileHead, FileTask task, ExportInfo exportInfo,
									 FileHandler fileHandler, List<E> list) throws Exception {
		if (ListUtil.isBlank(list)) {
			return;
		}
		Integer platformId = task.getPlatformId();// 0:Backend，2：FrontKit
		boolean isBackend = platformId != null && platformId == Platform.FRONTKIT.getIndex();

		List<String[]> cellsList = null;
		if(task.getDataType().getIndex() == BizDataType.BillCountsStat.getIndex()){
			cellsList = converter.getCellsList(list, false);
		}else if(task.getType() == TaskType.Export && BizDataType.Contact == task.getDataType()){
			cellsList = converter.getCellsList(list, false);
		}else{
			cellsList = converter.getCellsList(list, isBackend);
		}

		File file = exportInfo.getCurrentFile(cellsList.size(), task);
		boolean isNew = exportInfo.isNewFile();
		if (isNew) {
			cellsList.add(0, fileHead);
			exportInfo.setNewFile(false);
		}
		if (exportInfo.getFileType() == FileType.Csv) {
			fileHandler.writeFile(file, delimiter, isNew, cellsList, Charset.forName("gbk"));
		} else {
			fileHandler.writeFile(file, delimiter, isNew, cellsList, Charset.forName("UTF-8"));
		}
		task.setFileName(file.getAbsolutePath());
		commitHandlingFileTask(task, list.size());

	}
	private void commitHandlingFileTask(FileTask task, int count) {
		task.setState(TaskState.Handle);
		task.addHandledCount(count);
		task.setHanldePercent(task.getCurPercent());
		fileTaskService.commitHandlingFileTask(task);
	}
	@Override
	@Autowired
	public void setConfig(Config config) {
		super.config = config;
	}
}

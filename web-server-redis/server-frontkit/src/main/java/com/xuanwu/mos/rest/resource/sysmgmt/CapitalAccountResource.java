package com.xuanwu.mos.rest.resource.sysmgmt;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.file.FileHeader;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.HeadDMapToF;
import com.xuanwu.mos.file.HeadInfo;
import com.xuanwu.mos.file.mapbean.ChargeAccountMap;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.request.ImportRequest;
import com.xuanwu.mos.rest.service.CapitalAccountService;
import com.xuanwu.mos.rest.service.ChargeRecordService;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.service.RoleService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 计费账户管理
 * @Data 2017-3-24
 * @Version 1.0.0
 */
@Component
@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR)
public class CapitalAccountResource {

	private static final Logger logger = LoggerFactory.getLogger(CapitalAccountResource.class);
	@Autowired
	private CapitalAccountService accountService;
	@Autowired
	private ChargeRecordService recordService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private FileImporter fileImporter;
	@Autowired
	private Config config;
	@Autowired
	private FileTaskExecutor taskExecutor;
	@Autowired
	private FileTaskService taskService;
	@Autowired
	private UserMgrService userMgrService;	
	@Autowired
    private SysLogService sysLogService;

	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_INDEX)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getBalanceInfo() {
		SimpleUser user = SessionUtil.getCurUser();
		CapitalAccount capitalAccount = accountService.
				getDifferenceByEnterpriseId(user.getEnterpriseId());
		return JsonResp.success(capitalAccount);
	}

	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_ACCOUNTLIST)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp capitalAccountList(@Valid PageReqt req){
		SimpleUser user = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		params.addParam("entId", user.getEnterpriseId());
		int total = accountService.count(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(pageInfo);
		List<CapitalAccount> capitalAccounts = accountService.list(params);
		return PageResp.success(total, capitalAccounts);
	}

	/**
	 * 新增计费账户
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_ADDACCOUNT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp capitalAccountAdd(CapitalAccount account) throws RepositoryException {
		SimpleUser user = SessionUtil.getCurUser();
		if (ChargeWay.HAND_CHARGE.equals(account.getChargeWay())) {
			account.setAutoChargeMoney(new BigDecimal(0));
		}
		account.setAccountName(account.getAccountName().trim());
		CapitalAccount parentAccount = accountService.
				findParentAccountInfo(user.getEnterpriseId(), null);
		account.setEnterpriseId(user.getEnterpriseId());
		if (!accountService.isExistAccountName(account)) {
			if (parentAccount != null) {
				account.setParentId(parentAccount.getId());
				account.setUserId(user.getId());
				if (accountService.addCapitalAccount(account)) {
					sysLogService.addLog(user,OperationType.NEW,"【计费账户】","Public","Login","【" + account.getAccountName() + "】"); //添加访问日志
					return JsonResp.success();
				} else {
					return JsonResp.fail(Messages.SYSTEM_ERROR);
				}
			} else {
				return JsonResp.fail(Messages.ENTERPRISE_NOT_FOUNT_FOR_ADD);
			}
		} else {
			return JsonResp.fail("已存在该计费账户名称，请输入其他名称！");
		}
	}

	/**
	 * 计费账户充值
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGING)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp doRecharge(CapitalAccount capitalAccount) throws RepositoryException {
		SimpleUser user = SessionUtil.getCurUser();
		CapitalAccount parentAccount = accountService.
				findParentAccountInfo(user.getEnterpriseId(), UserState.NORMAL);
		if (parentAccount != null && parentAccount.getId() > 0) {
			List<CapitalAccount> childAccounts = accountService.
					findChildAccountForCharging(user.getEnterpriseId(), parentAccount.getId());
			if (ListUtil.isNotBlank(childAccounts)) {
				boolean isExist = false;
				for (CapitalAccount childAccount : childAccounts) {
					if (childAccount.getId().equals(capitalAccount.getId())) {
						childAccount.setChargeMoney(capitalAccount.getChargeMoney());
						isExist = true;
						break;
					}
				}
				if (isExist) {
					
					sysLogService.addLog(user,OperationType.NEW,"【计费账户】","Public","Login","【" + capitalAccount.getAccountName() + "】"); //添加访问日志

					return accountService.charging(childAccounts,
							parentAccount, capitalAccount.getId());
				} else {
					return JsonResp.fail(Messages.CAPITAL_CHILD_ACCOUNT_NOT_FOUNT2.
							replace("{accountName}", capitalAccount.getAccountName()));
				}
			} else {
				return JsonResp.fail(Messages.CAPITAL_CHILD_ACCOUNT_NOT_FOUNT);
			}
		} else {
			return JsonResp.fail(Messages.ENTERPRISE_NOT_FOUNT_FOR_CHARGE);
		}
	}

	/**
	 * 上传
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_IMPORTINGCHARGING)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp upload(@Context HttpServletRequest request){
		UploadResult result = fileImporter.upload(BizDataType.ChargeAccount, request);
		StatusCode statusCode = result.getStatusCode();
		if (statusCode != StatusCode.Success) {
			return JsonResp.fail(statusCode.getStateDesc());
		}
		return JsonResp.success(result);
	}

	/**
	 * 导入充值
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_IMPORTINGCHARGING)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp doImport(@Valid ImportRequest req) {
		try {
			if (!req.isCorrectFileHead()) {
				return JsonResp.fail(-1, Messages.INCORRECT_FILE_COLUMN);
			}
			SimpleUser user = SessionUtil.getCurUser();
			//构建文件映射列
			List<HeadDMapToF> headDMapToFList = req.getHeadDMapToFList();
			ChargeAccountMap accountMap = new ChargeAccountMap();
			for (HeadDMapToF headDMapToF : headDMapToFList) {
				HeadInfo headInfo = headDMapToF.getDataHeadInfo();
				if (headInfo.getName().equals(FileHeader.CHARGE_ACCOUNT[0])) {
					accountMap.setAccountName(headInfo.getIndex());
				}
				if (headInfo.getName().equals(FileHeader.CHARGE_ACCOUNT[1])) {
					accountMap.setChargeMoney(headInfo.getIndex());
				}
			}

			FileTask task = new FileTask();
			task.setFileName(req.getFileName());
			task.setTaskName("计费账户充值导入");
			task.setType(TaskType.Import);
			task.setDataType(BizDataType.ChargeAccount);
			task.setPostTime(new Date());
			task.setUserId(user.getId());
			task.setFileSize(req.getFileSize());
			task.setPlatformId(config.getPlatformId());

			Map<String, String> params = new HashMap<>();
			params.put("userId", String.valueOf(user.getId()));
			params.put("entId", String.valueOf(user.getEnterpriseId()));
			params.put("delimeter", req.getDelimiter());
			params.put("accountMap", accountMap.tran2Params());

			task.setParamsMap(params);
			task.setHanldePercent(0);
			task.setState(TaskState.Wait);
			taskService.save(task);
			taskExecutor.putTask2Queue(task);
			
			sysLogService.addLog(user,OperationType.IMPORT,"【计费账户】","Public","Login",""); //添加访问日志
            
		} catch (RepositoryException e) {
			logger.error("Import charging failed：", e);
			JsonResp.fail(Messages.SYSTEM_ERROR);
		}
		return JsonResp.success();
	}

	@GET
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_IMPORTINGCHARGING)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getFileHeader() {
		List<HeadDMapToF> headDMapToFList = new ArrayList<>();
		for (int i = 0; i < FileHeader.CHARGE_ACCOUNT.length; i++) {
			HeadDMapToF headDMapToF = new HeadDMapToF();
			headDMapToF.setDataHeadInfo(new HeadInfo(i, FileHeader.CHARGE_ACCOUNT[i]));
			headDMapToFList.add(headDMapToF);
		}
		return JsonResp.success(headDMapToFList);
	}

	/**
	 * 计费账户查看->账户基本信息
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp capitalAccountDetail(CapitalAccount account){
		return getAccountDetail(account);
	}

	/**
	 * 计费账户查看->用户列表查询
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL_USERS)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp detailUsers(@Valid PageReqt req){
		return getDetailUsers(req);
	}

	public JsonResp getAccountDetail(CapitalAccount account) {
		SimpleUser user = SessionUtil.getCurUser();
		CapitalAccount accountInfo = accountService.
				findCapitalAccountInfo(user.getEnterpriseId(), account.getId());
		return JsonResp.success(accountInfo);
	}

	public JsonResp getDetailUsers(PageReqt req) {
		SimpleUser user = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		params.addParam("platformId", config.getPlatformId());
		params.addParam("enterpriseId", user.getEnterpriseId());
		int total = userService.findChargeUserCount(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo page = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(page);
		List<SimpleUser> users = userService.findChargeUsers(params);
		return PageResp.success(total, users);
	}

	/**
	 * 充值记录中账户名称链接查看->账户基本信息
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_DETAIL)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp recordAccountDetail(CapitalAccount account){
		return getAccountDetail(account);
	}

	/**
	 * 充值记录中账户名称链接查看->用户列表查询
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_USERS_DETAIL)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp recordUsersDetail(@Valid PageReqt req){
		return getDetailUsers(req);
	}

	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR_USERS)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp listSelectorUsers(@Valid PageReqt req){
		SimpleUser simpleUser = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		params.addParam("platformId", config.getPlatformId());
		params.addParam("enterpriseId", simpleUser.getEnterpriseId());
		if (params.getParams().get("path") == null) {
			//获取企业根部门节点
			Department rootDept = userMgrService.
					getDeptByEntId(simpleUser.getEnterpriseId());
			params.addParam("rootPath", rootDept.getId() + Delimiters.DOT);
		}
		int total = userService.findSelectorUserCount(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo page = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(page);
		List<SimpleUser> users = userService.findSelectorUsers(params);
		return PageResp.success(total, users);
	}

	/**
	 * 包含/取消包含用户
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp doSelector(Map<String, String> params) throws RepositoryException {
		SimpleUser user = SessionUtil.getCurUser();
		int capitalAccountId = Integer.valueOf(params.get("capitalAccountId"));
		int includedType = Integer.valueOf(params.get("includedType"));
		//这里正好相反
		boolean isInclude = includedType == 1 ? false : true;
		String ids = params.get("userIds");
		if (capitalAccountId > 0) {
			boolean bindResult = accountService.editUserAccountBind(isInclude,
					capitalAccountId, user.getEnterpriseId(), ids.split(","));
			if (!bindResult) {
				JsonResp.fail("网络故障，无法更新!");
			}
		} else {
			JsonResp.fail("无法获取账户信息，无法更新!");
		}
		return JsonResp.success();
	}

	/**
	 * 计费账户修改
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_UPDATEACCOUNT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp updateCapitalAccount(CapitalAccount account) throws RepositoryException {
		SimpleUser user = SessionUtil.getCurUser();
		if (ChargeWay.HAND_CHARGE.equals(account.getChargeWay())) {
			account.setAutoChargeMoney(new BigDecimal(0));
		}
		account.setEnterpriseId(user.getEnterpriseId());
		if (!accountService.isExistAccountName(account)) {
			account.setUserId(user.getId());
			if (accountService.updateCapitalAccount(account)) {
				
				sysLogService.addLog(user,OperationType.MODIFY,"【计费账户】","Public","Login","【"+account.getAccountName()+"】"); //添加访问日志
	            
				return JsonResp.success();
			}
			return JsonResp.fail();
		} else {
			return JsonResp.fail("已存在该计费账户名称，请输入其他名称！");
		}
	}

	/**
	 * 计费账户删除
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_DELETEACCOUNT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp delCapitalAccount(Integer[] ids) throws RepositoryException {
		SimpleUser user = SessionUtil.getCurUser();
		String logContent = accountService.getDelAccountName(ids);
		if (accountService.delCapitalAccount(ids, user.getEnterpriseId())) {
			sysLogService.addLog(user,OperationType.DELETE,"【计费账户】",
					"Public","Login","【" + LogContentUtil.format(logContent) + "】"); //添加访问日志
			return JsonResp.success();
		}
		return JsonResp.fail();
	}

	/**
	 * 充值记录
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp chargeRecordList(@Valid PageReqt req){
		SimpleUser user = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		params.addParam("entId", user.getEnterpriseId());
		params.addParam("endTime", params.getParams().get("endTime") + " 23:59:59");
		int total = recordService.count(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo page = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(page);
		List<ChargeRecord> records = recordService.list(params);
		return PageResp.success(total, records);
	}

	/**
	 * 导出
	 */
	@POST
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_EXPORTRECHARGE)
	public JsonResp doExport(@Valid PageReqt req) {
		SimpleUser curUser = SessionUtil.getCurUser();
		try {
			Map<String, Object> params = req.getParams();
			String fileName = (String) params.get("fileName");
			params.put("entId", curUser.getEnterpriseId());
			params.put("endTime", params.get("endTime") + " 23:59:59");
			FileTask task = new FileTask();
			task.setFileName(fileName);
			task.setTaskName("充值记录导出");
			String taskName = (String) params.get("name");
			if (StringUtils.isNotBlank(taskName)) {
				task.setTaskName(task.getTaskName() + "(" + taskName + ")");
			}
			task.setType(TaskType.Export);
			task.setDataType(BizDataType.ChargeAccount);
			task.setPostTime(new Date());
			task.setUserId(curUser.getId());
			task.setFileSize(0l);
			task.setHanldePercent(0);
			task.setState(TaskState.Wait);
			task.setPlatformId(config.getPlatformId());
			task.setParameters(XmlUtil.toXML(params));
			taskService.save(task);
			taskExecutor.putTask2Queue(task);
			
			sysLogService.addLog(curUser,OperationType.EXPORT,"【计费账户】","Public","Login",""); //添加访问日志
            
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResp.fail();
		}
		return JsonResp.success();
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path(Keys.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGFORAUTOCHARGING)
	public JsonResp forAutoCharging(Map<String, String> params) throws RepositoryException {
		SimpleUser user = SessionUtil.getCurUser();
		QueryParameters parameters = new QueryParameters();
		parameters.addParam("enterpriseId", user.getEnterpriseId());
		boolean isCurrentMonth = Boolean.valueOf(params.get("isCurrentMonth"));
		if (isCurrentMonth) {
			parameters.addParam("startTime", DateUtil.getCurMonthFirstDayTime());
			parameters.addParam("endTime", DateUtil.getCurMonthLastDayTime());
		} else {
			parameters.addParam("startTime", DateUtil.getPreMonthFirstDate());
			parameters.addParam("endTime", DateUtil.getPreMonthLastDate());
		}
		int success = 0;
		List<ChargeRecord> failRecords = recordService.findFailChargingRecords(parameters);
		if (ListUtil.isNotBlank(failRecords)) {
			CapitalAccount parentAccount = accountService.
					findParentAccountInfo(user.getEnterpriseId(), UserState.NORMAL);
			if (parentAccount != null) {
				List<CapitalAccount> childAccounts = accountService.
						findChildAccountForCharging(user.getEnterpriseId(), parentAccount.getId());
				if (ListUtil.isNotBlank(childAccounts)) {
					for (ChargeRecord record : failRecords) {
						if(accountService.handToAutoCharging(childAccounts, parentAccount, record)) success++;
					}
					return JsonResp.success(failRecords.size() + "," + success + "," + (failRecords.size() - success));
				} else {
					return JsonResp.fail("找不到任何计费子账户信息，无法完成充值!");
				}
			} else {
				return JsonResp.fail("找不到企业总账户信息，无法完成充值!");
			}
		} else {
			return JsonResp.fail(params.get("month") + "月没有自动充值失败的记录!");
		}
	}

}

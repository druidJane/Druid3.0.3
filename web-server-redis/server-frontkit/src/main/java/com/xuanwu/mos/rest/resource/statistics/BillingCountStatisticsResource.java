package com.xuanwu.mos.rest.resource.statistics;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.BillingAcountInfo;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.TaskState;
import com.xuanwu.mos.domain.enums.TaskType;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.rest.service.statistics.BillingCountStatisticsService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.XmlUtil;

@Component
@Path(value=Keys.STATISTICS_BILLINGACCOUNTSTATISTICS)
public class BillingCountStatisticsResource {
	private Logger logger = LoggerFactory
			.getLogger(BillingCountStatisticsResource.class);

	@Autowired
	private BillingCountStatisticsService billStatService;
	
	@Autowired
    private FileTaskExecutor executor;
	
    @Autowired
    private Config config;

    @Autowired
    private FileTaskService fileTaskService;
    
    @Autowired
    private SysLogService sysLogService;
	
    /**
     * 用户消费情况
     */    
    @POST
	@Path(value=Keys.STATISTICS_BILLINGACCOUNTSTATISTICS_USERDETAIL)
	@Produces({ MediaType.APPLICATION_JSON })
    public JsonResp queryUserDetail(@Valid PageReqt req){
    	QueryParameters params = new QueryParameters(req);
		SimpleUser loginUser = SessionUtil.getCurUser();
		int entId = loginUser.getEnterpriseId();
		params.addParam("entId", entId);
		params.addParam("format", "%Y-%m-%d");
		// 
		int total = billStatService.userDetailCount(params);
		if(total > 0){	
			// 获取详情列表数据
			PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
			params.setPage(pageInfo);
			List<BillingAcountInfo> billAccountList = billStatService.userDetailList(params);
			return PageResp.success(total, billAccountList);
		}else {
			return PageResp.emptyResult();
		}
    }
	/**
	 * 查询计费详情
	 * @return
	 */
	@POST
	@Path(value=Keys.STATISTICS_BILLINGACCOUNTSTATISTICS_GETDETAILSTAT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp queryUserList(@Valid PageReqt req){
		QueryParameters param = new QueryParameters(req);
		SimpleUser loginUser = SessionUtil.getCurUser();
		int entId = loginUser.getEnterpriseId();
		param.addParam("endId", entId);
		Integer operType = (Integer)param.getParams().get("operType");
		String beginDate = (String)param.getParams().get("beginDate");
		String endDate = (String)param.getParams().get("endDate");
		 if(operType==1){
			 
		 }else{
			 param.addParam("format", "%Y-%m");
		 }
		// 
		int total = billStatService.queryCountAccountByEntID(param);
		if(total > 0){	
			// 获取详情列表数据
			PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
			param.setPage(pageInfo);
			List<BillingAcountInfo> billAccountList = billStatService.queryUserListAccountByEntID(param);
			// 获取总数统计数据
			BillingAcountInfo sumdata = billStatService.querySumAccountbyAccountId(param);
		    String name = billAccountList.get(0).getAccountName();
		    Map<String,Object>  status = getBillStatChartData(billStatService.copyBillData(billAccountList),name);
			Map data = new HashMap();
			data.put("d1", sumdata);
			data.put("d2", billAccountList);
			data.put("d3", status);
			return PageResp.success(total, data);
		}else {
			return PageResp.emptyResult();
		}
	}
	/**
	 * 查询计费帐户列表数据
	 * @param req
	 * @return
	 */
	@POST
	@Path(Keys.STATISTICS_BILLINGACCOUNTSTATISTICS_GETBILLINGACCOUNTSTATISTICS)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp list(@Valid PageReqt req){
		SimpleUser loginUser = SessionUtil.getCurUser();
		int entId = loginUser.getEnterpriseId();
		QueryParameters params = new QueryParameters(req);		
		params.addParam("entId", entId);	
	
		Integer operType = (Integer)params.getParams().get("operType");
		 if(operType==1){
			 params.addParam("format", "%Y-%m-%d");
		 }else{
			 params.addParam("format", "%Y-%m");
		 }
		int total = billStatService.findBillingAccountInfoByDaysCount(params);
		if(total > 0){
			PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
			params.setPage(pageInfo);
			List<BillingAcountInfo> billAccountList = billStatService.findBillingAccountInfoByDaysList(params);
			return PageResp.success(total,billAccountList);
		}else {
			return PageResp.emptyResult();
		}
	}
	private String[] getLegends() {		
		String [] legends4Bill = { "短信消费", "彩信消费", "总消费"};
		return legends4Bill;		
	}
	/**
	 * 计费账户图表数据
	 * @param tempList
	 * @param name
	 * @return
	 */
	public Map<String,Object> getBillStatChartData(List<BillingAcountInfo> tempList,String name){
		Map<String,Object> data = new HashMap<>();
		String[] legends = getLegends();

		List<List<String>> datas = new ArrayList<>();
		List<String> series4SMSConsume = new ArrayList<>();
		List<String> series4MMSConsume = new ArrayList<>();
		List<String> series4SumConsume = new ArrayList<>();
		List<Integer> types = new ArrayList<>();
		List<String> axis =  new ArrayList<>();
		double temp = 0;
		for(BillingAcountInfo billinfo:tempList){
			if(temp <= billinfo.getSumConsume() ){
				temp = billinfo.getSumConsume();
			}
			axis.add(billinfo.getDeductTime4Grid());
				
			series4SMSConsume.add(billStatService.sub4Decimail(billinfo.getSmsConsume()));
			series4MMSConsume.add(billStatService.sub4Decimail(billinfo.getMmsConsume()));
			series4SumConsume.add(billStatService.sub4Decimail(billinfo.getSumConsume()));
		}
		types.add(2);
		types.add(2);
		types.add(1);
		datas.add(series4SMSConsume);
		datas.add(series4MMSConsume);
		datas.add(series4SumConsume);
		data.put("name", name);
		data.put("title", name);
		data.put("legend", legends);
		data.put("datas", datas);
		data.put("xAxis", axis);
		data.put("types", types);
		if(temp<5){
			data.put("yMax", 5);
			int tempInt = new BigDecimal(temp).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			data.put("interval", tempInt>0?tempInt/5:0);
		}else{
			int tempInt = new BigDecimal(temp).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			data.put("yMax", tempInt);
			data.put("interval", tempInt>0?tempInt/5:0);
		}
		
		
		
		return data;
	}

	@POST
	@Path(value="Index/GetAccount")
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getAccount(@Valid PageReqt req){
		SimpleUser loginUser = SessionUtil.getCurUser();
		int entId = loginUser.getEnterpriseId();
		QueryParameters param = new QueryParameters(req);
		param = new QueryParameters();
		param.addParam("entId", entId);
		
		int total = billStatService.findBillingAccountInfoByDaysCount(param);
		if(total > 0){
			PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
			param.setPage(pageInfo);
			List<BillingAcountInfo> billAccountList = billStatService.findBillingAccountInfoByDaysList(param);
			return PageResp.success(total,billAccountList);
		}else {
			return PageResp.emptyResult();
		}
	}
	/**
	 * 导出计费帐户列表数据
	 * @param model
	 * @param beginTime
	 * @param endTime
	 * @param accountName
	 * @return
	 */
	@POST
	@Path(value="/ExportBillingAccountStatistics")
	public JsonResp export(@Valid PageReqt req){
		 SimpleUser curUser = SessionUtil.getCurUser();
		 sysLogService.addLog(curUser,OperationType.EXPORT,"【计费帐户统计】","BillingAccountStatistics","ExportBillingAccountStatistics",""); //添加访问日志

	        try {

	            Map<String, Object> map = req.getParams();
	            String fileName = (String) map.get("fileName");
	            // 构造参数，String类型默认值为""，数字默认为-1
	            String delimiter = (String) map.get("delimiter");	           
	            Map<String, Object> params = new HashMap<>();
	            if (map.get("beginDate") != null) {
	                params.put("beginDate", map.get("beginDate"));
	            } else {
	                params.put("beginDate", "");
	            }
	            if (map.get("endDate") != null) {
	                params.put("endDate", map.get("endDate"));
	            } else {
	                params.put("endDate", "");
	            }
	            if (map.get("accountName") != null) {
	                params.put("accountName", map.get("accountName"));
	            } else {
	                params.put("accountId", "");
	            }
	            Integer operType = (Integer)map.get("operType");
		   		 if(operType==1){
		   			 params.put("format", "%Y-%m-%d");
		   		 }else{
		   			 params.put("format", "%Y-%m");
		   		 }
	            params.put("entId", curUser.getEnterpriseId());
	            params.put("delimiter", delimiter);
				params.put("name", map.get("name"));
	            FileTask task = new FileTask();
	            task.setFileName(fileName);
	   	        if(map.get("name")!=null && !map.get("name").equals("")){
	   	        	 task.setTaskName("计费账户导出"+"("+map.get("name")+")");
	   	        }else{
	   	        	task.setTaskName("计费账户导出");
	   	        }  
	          
	            task.setType(TaskType.Export);
	            task.setDataType(BizDataType.BillCountsStat);
	            task.setPostTime(new Date());
	            task.setUserId(curUser.getId());
	            task.setFileSize(0l);
	            task.setHanldePercent(0);
	            task.setState(TaskState.Wait);
	            task.setPlatformId(config.getPlatformId());
	            task.setParameters(XmlUtil.toXML(params));
	            fileTaskService.save(task);
	            executor.putTask2Queue(task);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return JsonResp.fail();
	        }
	        return JsonResp.success();
	}
	/**
	 * 业务类型详情导出
	 * @param req
	 * @return
	 */
	@POST
	@Path(Keys.STATISTICS_BILLINGACCOUNTSTATISTICS_EXPORTDETAIL)
	public JsonResp exportDetail(@Valid PageReqt req){
		 SimpleUser curUser = SessionUtil.getCurUser();
		 sysLogService.addLog(curUser,OperationType.EXPORT,"【计费帐户详情】","BillingAccountStatistics","Index/export",""); //添加访问日志

	        try {

	            Map<String, Object> map = req.getParams();
	            String fileName = (String) map.get("fileName");
	            // 构造参数，String类型默认值为""，数字默认为-1
	            String delimiter = (String) map.get("delimiter");
	            Map<String, Object> params = new HashMap<>();
	    		Integer operType = (Integer)map.get("operType");
	    		params.put("operType", map.get("operType"));
		   		 if(operType==1){
		   			 params.put("format", "%Y-%m-%d");
		   			 
		   		 }else{
		   			 params.put("format", "%Y-%m");
		   		 }
	            if (map.get("beginDate") != null) {
	                params.put("beginDate", map.get("beginDate"));
	            } else {
	                params.put("beginDate", "");
	            }
	            if (map.get("endDate") != null) {
	                params.put("endDate", map.get("endDate"));
	            } else {
	                params.put("endDate", "");
	            }
	            if (map.get("accuntId") != null) {
	                params.put("accuntId", map.get("accuntId"));
	            } else {
	                params.put("accuntId", "");
	            }
	            params.put("endId", curUser.getEnterpriseId());
	            params.put("delimiter", delimiter);
				params.put("name", map.get("name"));
	            FileTask task = new FileTask();
	            task.setFileName(fileName);
	        
	   	        if(map.get("name")!=null && !map.get("name").equals("")){
	   	        	 
	   	        	if(operType==1){
	   	        		task.setTaskName("计费账户日趋势统计导出"+"("+map.get("name")+")");
			   		 }else{
			   			task.setTaskName("计费账户月趋势统计导出"+"("+map.get("name")+")");
			   		 }
	   	        }else{
		   	     	if(operType==1){
	   	        		task.setTaskName("计费账户日趋势统计导出");
			   		 }else{
			   			task.setTaskName("计费账户月趋势统计导出");
			   		 }
	   	        }  
	            task.setType(TaskType.Export);
	            task.setDataType(BizDataType.BillCountDetailStat);
	            task.setPostTime(new Date());
	            task.setUserId(curUser.getId());
	            task.setHanldePercent(0);
	            task.setState(TaskState.Wait);
	            task.setPlatformId(config.getPlatformId());
	            task.setParameters(XmlUtil.toXML(params));
	            fileTaskService.save(task);
	            executor.putTask2Queue(task);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return JsonResp.fail();
	        }
	        return JsonResp.success();
	}
}

package com.xuanwu.mos.rest.resource.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.BaseStatistics;
import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.BizTypeStatistics;
import com.xuanwu.mos.domain.entity.Department;
import com.xuanwu.mos.domain.entity.DepartmentStatistics;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.UserStatistics;
import com.xuanwu.mos.domain.entity.UserTask;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.MosBizDataType;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.TaskState;
import com.xuanwu.mos.domain.enums.TaskType;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.rest.service.BizTypeService;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.rest.service.UserTaskService;
import com.xuanwu.mos.rest.service.statistics.StatisticsService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.UrlResourceUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.utils.WebConstants.StatResultCode;
import com.xuanwu.mos.utils.XmlUtil;
import com.xuanwu.mos.vo.UserTaskParameter;


@Component
@Path(value=Keys.STATISTICS_SUMSTATISTICS)
public class SumStatisticsResource {
	
	private static final Logger logger = LoggerFactory
			.getLogger(SumStatisticsResource.class);
	
	@Autowired
	private StatisticsService statService;
	@Autowired
	private BizTypeService bizTypeService;
	
	@Autowired
    private FileTaskExecutor executor;
	
    @Autowired
    private Config config;

    @Autowired
    private FileTaskService fileTaskService;
    
	@Autowired
	private UserMgrService userMgrService;
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private UserTaskService userTaskService;
    
    @Autowired
    private SysLogService sysLogService;
    
    private static final FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
    // 部门统计日查询数据列表总数内存副本
    private DepartmentStatistics sumDeptCopy = new DepartmentStatistics();
    
    private BizTypeStatistics totalstatCopy = new BizTypeStatistics();
    
    private int total4BizStat =0;
    
    // 部门统计月度查询数据列表数据内存副本
    private List<DepartmentStatistics> monthDeptList = null;
    
    // 用户详情列表总数内存副本
    private int detailCount;
	/**
	 * 部门统计查询
	 * @param req
	 * @return
	 */
	@Path(Keys.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT)
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp sumDeptList(@Context HttpServletRequest request,@Valid PageReqt req){
		try{
			QueryParameters params = new QueryParameters(req);
			Integer operType = (Integer)req.getParams().get("operType");
			String deptName =(String)params.getParams().get("deptName");
			String beginDate =(String) params.getParams().get("beginDate");
			String endDate =(String) params.getParams().get("endDate");
			String url = UrlResourceUtil.handleUrlResource(Keys.STATISTICS_SUMSTATISTICS, Keys.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT);
			handleDataScope(params,url,null,1);		
			List<DepartmentStatistics> deptStatisticsList = null;
			List<DepartmentStatistics> tempList = null;
			List<DepartmentStatistics> dayCountList = null;
			List<DepartmentStatistics> tempList4chart = null;
			DepartmentStatistics sumDept = null;
			if(operType ==1){

				int total = statService.findDeptSendCount(params);
				if(total > 0){
					// 查询总量数据	
					sumDept = statService.getDeptStatSendSUM(params);
					if(params.getSorts().size()==0){
						params.getSorts().put("allReceiveSum", "desc");
					}
					params.addParam("querysplit", "true");					
					PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
				    params.setPage(pageInfo);
				    deptStatisticsList = statService.deptHistoryStatistics(params);

					Map<String,Object> data = new HashMap<>();
					data.put("d1", sumDept);
					data.put("d2", deptStatisticsList);
					Map<String,Object>  status = getDeptStatChartData(statService.mergeData(deptStatisticsList),deptName!=null?deptName:"本部门");
					data.put("d3", status);
					return PageResp.success(total,data);
				}else{
					return PageResp.emptyResult();
				}
			}else{
				int total = 0;
				params.addParam("beginDate", DateUtil.toMonthFirstDay(beginDate));
				params.addParam("endDate", DateUtil.toMonthLastDay(endDate));
				deptStatisticsList = statService.deptHistoryStatistics(params);			
			    sumDept = (DepartmentStatistics)statService.getSumStatistics(deptStatisticsList,null,null,1);
				Map<String,Object> data = new HashMap<>();
				if(ListUtil.isNotBlank(deptStatisticsList)){		
					total = deptStatisticsList.size();
					PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
				    params.setPage(pageInfo);
					tempList = deptStatisticsList.subList(params.getPage().getFrom(), params.getPage().getTo());
					
					data.put("d1", sumDept);
					data.put("d2", tempList);
					Map<String,Object>  status = getDeptStatChartData(statService.mergeData(tempList),deptName!=null?deptName:"本部门");
					data.put("d3", status);
					return PageResp.success(total,data);
				}else{
					return PageResp.emptyResult();
				}
			}
			

		}catch(Exception e){
			logger.error("Query department total statistics failed: ", e);
		}
		return PageResp.success();
	}
	public void handleDataScope(QueryParameters params,String url,Map<String,Object> reportParam,int flag){
		SimpleUser loginUser = SessionUtil.getCurUser();
		DataScope ds = SessionUtil.getDataSope(url);
		Integer deptId = 0;
		if(flag ==1){
			deptId = (Integer)params.getParams().get("deptId");
		}else{
			deptId = (Integer)reportParam.get("deptId");
		}

		String path="";
		
        //获取企业根部门节点
		 Department rootDept = userMgrService.getDeptByEntId(loginUser.getEnterpriseId());
	     int rootId =rootDept.getId(); 
		 if(DataScope.GLOBAL == ds || DataScope.NONE == ds){
			
		        if(rootId == deptId){
		        	deptId = loginUser.getEnterpriseId();
		        }
				if(deptId == loginUser.getEnterpriseId()){
					//是企业帐号，不做任何处理
				}else{
					 path = userService.findPathById(deptId);
						if (StringUtils.isBlank(path)) {
							path = deptId + Delimiters.DOT;
						} else {
							path = path + deptId + Delimiters.DOT;
						}
			 }
		}else if(DataScope.DEPARTMENT == ds){
			if(rootId == deptId){
				path = userService.findPathById(loginUser.getParentId());
				if (StringUtils.isBlank(path)) {
					path = loginUser.getParentId() + Delimiters.DOT;
				} else {
					path = path + loginUser.getParentId() + Delimiters.DOT;
				}
			}else{
				path = userService.findPathById(deptId);
				if (StringUtils.isBlank(path)) {
					path = deptId + Delimiters.DOT;
				} else {
					path = path + deptId + Delimiters.DOT;
				}
			}
		}
		if(flag ==1){
			params.addParam("path", path);	
			params.addParam("enterpriseId", loginUser.getEnterpriseId());
		}else{
			reportParam.put("path", path);
			reportParam.put("deptId", deptId);
			reportParam.put("enterpriseId", loginUser.getEnterpriseId());
		}
		
	}
	/**
	 * 部门统计图表数据
	 * @param tempList
	 * @param name
	 * @return
	 */
	public Map<String,Object> getDeptStatChartData(List<DepartmentStatistics> tempList,String name){
		Map<String,Object> data = new HashMap<>();
		String[] legends = getLegends();

		List<List<Long>> datas = new ArrayList<>();
		List<Long> series4AllSubmitSum = new ArrayList<>();
		List<Long> series4AllSendSum = new ArrayList<>();
		List<Long> series4AllSuccessSum = new ArrayList<>();
		long temp= 0;
		List<String> axis =  new ArrayList<>();
		for(DepartmentStatistics departMent:tempList){
			if(temp <= departMent.getAllReceiveSum()){
				temp = departMent.getAllReceiveSum();
			}
			axis.add(departMent.getStatDate());
			series4AllSubmitSum.add(departMent.getAllReceiveSum());
			series4AllSendSum.add(departMent.getAllSendSum());
			series4AllSuccessSum.add(departMent.getAllSuccessSum());
		}
		
		datas.add(series4AllSubmitSum);
		datas.add(series4AllSendSum);
		datas.add(series4AllSuccessSum);
		data.put("name", name);
		data.put("title", name);
		data.put("legend", legends);
		data.put("datas", datas);
		data.put("xAxis", axis);
		
	
		if(temp<5){
			data.put("yMax", 5);
		}else{
			data.put("yMax", temp);
		}		

		data.put("interval", temp>0?temp/5:0);
		return data;
	}
	/**
	 * 业务类型统计图表数据
	 * @param tempList
	 * @param name
	 * @return
	 */
	public Map<String,Object> getBizStatChartData(List<BizTypeStatistics> tempList,String name){
		Map<String,Object> data = new HashMap<>();
		String[] legends = getLegends();

		List<List<Long>> datas = new ArrayList<>();
		List<Long> series4AllSubmitSum = new ArrayList<>();
		List<Long> series4AllSendSum = new ArrayList<>();
		List<Long> series4AllSuccessSum = new ArrayList<>();
		long temp= 0;
		List<String> axis =  new ArrayList<>();
		for(BizTypeStatistics bizType:tempList){
			if(temp <= bizType.getAllReceiveSum()){
				temp = bizType.getAllReceiveSum();
			}
			axis.add(bizType.getStatDate());
			series4AllSubmitSum.add(bizType.getAllReceiveSum());
			series4AllSendSum.add(bizType.getAllSendSum());
			series4AllSuccessSum.add(bizType.getAllSuccessSum());
		}
		
		datas.add(series4AllSubmitSum);
		datas.add(series4AllSendSum);
		datas.add(series4AllSuccessSum);
		data.put("name", name);
		data.put("title", name);
		data.put("legend", legends);
		data.put("datas", datas);
		data.put("xAxis", axis);
	
		if(temp<5){
			data.put("yMax", 5);
		}else{
			data.put("yMax", temp);
		}		

		data.put("interval", temp>0?temp/5:0);
		return data;
	}
	/**
	 * 用户统计图表数据
	 * @param tempList
	 * @param name
	 * @return
	 */
	public Map<String,Object> getUserStatChartData(List<UserStatistics> tempList,String name){
		Map<String,Object> data = new HashMap<>();
		String[] legends = getLegends();

		List<List<Long>> datas = new ArrayList<>();
		List<Long> series4AllSubmitSum = new ArrayList<>();
		List<Long> series4AllSendSum = new ArrayList<>();
		List<Long> series4AllSuccessSum = new ArrayList<>();
		
		List<String> axis =  new ArrayList<>();
		long temp = 0;
		for(UserStatistics user:tempList){	
			if(temp <= user.getAllReceiveSum() ){
				temp = user.getAllReceiveSum();
			}
			axis.add(user.getStatDate());
			series4AllSubmitSum.add(user.getAllReceiveSum());
			series4AllSendSum.add(user.getAllSendSum());
			series4AllSuccessSum.add(user.getAllSuccessSum());
		}
		
		datas.add(series4AllSubmitSum);
		datas.add(series4AllSendSum);
		datas.add(series4AllSuccessSum);
		data.put("name", name);
		data.put("title", name);
		data.put("legend", legends);
		data.put("datas", datas);
		data.put("xAxis", axis);
		if(temp<5){
			data.put("yMax", 5);
		}else{
			data.put("yMax", temp);
		}		
		data.put("interval", temp>0?temp/5:0);
		return data;
	}
	private String[] getLegends() {
	
		String [] legends4Dept = { "总提交", "总发送", "总成功"};
		return legends4Dept;
		
	}

	/**
	 * 用户统计-列表查询
	 * @param request
	 * @param req
	 * @return
	 */
	@Path(Keys.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT)
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp sumUserList(@Context HttpServletRequest request,@Valid PageReqt req){
		
		try{
			QueryParameters params = new QueryParameters(req);	
			String deptId = (String)params.getParams().get("deptId");
			String userId = (String)params.getParams().get("userId");
			SimpleUser loginUser = SessionUtil.getCurUser();
			DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
			String path = "";
			List<UserStatistics> sumUserList =null;
	
			if(DataScope.GLOBAL == ds || DataScope.NONE == ds){
				if(userId !=null ){
					params.addParam("userId", userId);
				}
				if(deptId != null ){
					Department rootDept = userMgrService.
				                getDeptByEntId(loginUser.getEnterpriseId());
			        int rootId =rootDept.getId(); 
			        if(rootId != Integer.valueOf(deptId)){
			        	path=statService.handlePath(deptId);
			        	params.addParam("path", path);
			        }	
				}
			}else if(DataScope.DEPARTMENT == ds){	
				if(deptId!=null ){
					path=statService.handlePath(deptId);
					params.addParam("path", path);
				}
				if(userId !=null ){
					params.addParam("userId", userId);
				}	
				if(deptId==null && userId==null){					
					path=statService.handlePath(String.valueOf(loginUser.getParentId()));
					params.addParam("path", path);
				}
			}else if(DataScope.PERSONAL == ds){
				params.addParam("userId", loginUser.getId());
			}				
			params.addParam("enterpriseId", loginUser.getEnterpriseId());
			int total = statService.findUserHistorySendCount(params);	
			if (total > 0) {	
				PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
			    params.setPage(pageInfo);
			    sumUserList = statService.userHistoryStatistics(params);

			    return PageResp.success(total, sumUserList);
			} else {
				return PageResp.emptyResult();
			}			
		}catch(Exception e){
			logger.error("Query user total statistics failed:", e);
		}
		return PageResp.emptyResult();
	}
	/**
	 * 用户统计-具体用户的统计详情
	 * @param userStatistics
	 * @return
	 */
	@POST
	@Path(Keys.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSERLIST)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp getUserStatListByUserId(@Context HttpServletRequest request,@Valid PageReqt req){		
		
		QueryParameters params = new QueryParameters(req);	
		Integer operType = (Integer)params.getParams().get("operType");
		String beginDate =(String) req.getParams().get("beginDate");
		String endDate =(String) req.getParams().get("endDate");
		SimpleUser loginUser = SessionUtil.getCurUser();
		params.addParam("enterpriseId", loginUser.getEnterpriseId());
		List<UserStatistics> sumUserList = null;
		List<UserStatistics> sumUserList4Count = null;
		List<UserStatistics> sumUserList4Chart = null;
		if(operType==1){
			int total = statService.findSelectedUserSendCount(params);	
			if (total > 0) {
				sumUserList4Count = statService.selectedUserStatisticsList(params);
				
				UserStatistics totalstat = (UserStatistics)statService.getSumStatistics(null,null,sumUserList4Count,2);		
			    String userName = sumUserList4Count.get(0).getUserName();
			    String deptName = sumUserList4Count.get(0).getDeptName();
			    String name = deptName+":"+userName;
			    total = sumUserList4Count.size();
				PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
			    params.setPage(pageInfo);
			    sumUserList = sumUserList4Count.subList(params.getPage().getFrom(), params.getPage().getTo());
			    sumUserList4Chart = statService.copyUserData(sumUserList);
			    Map<String,Object>  status = getUserStatChartData(sumUserList4Chart,name!=null?name:"");
			    Map<String,Object> data = new HashMap<>();
			    data.put("d1", sumUserList);
				data.put("d2", status);
			    return PageResp.success(total, data);
			} else {
				return PageResp.emptyResult();
			}		
		}else{
			 	params.addParam("beginDate", DateUtil.toMonthFirstDay(beginDate));
				params.addParam("endDate", DateUtil.toMonthLastDay(endDate));
				sumUserList4Count = statService.selectedUserStatisticsList(params);
				UserStatistics totalstat = (UserStatistics)statService.getSumStatistics(null,null,sumUserList4Count,2);		
				Map<String,Object> data = new HashMap<>();
				if(ListUtil.isNotBlank(sumUserList4Count)){		
					int total = sumUserList4Count.size();
						PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
					    params.setPage(pageInfo);
					    sumUserList = sumUserList4Count.subList(params.getPage().getFrom(), params.getPage().getTo());
					    sumUserList4Chart = statService.copyUserData(sumUserList);
					    String userName = sumUserList.get(0).getUserName();
					    String deptName = sumUserList.get(0).getDeptName();
					    String name = deptName+":"+userName;
					    Map<String,Object>  status = getUserStatChartData(sumUserList4Chart,name!=null?name:"");					  
					    data.put("d1", sumUserList);
						data.put("d2", status);
					return PageResp.success(total,data);
				}else{
					return PageResp.emptyResult();
				} 
		}
			
		
	}
	
	
	/**
	 * 业务类型统计查询方法
	 * @param req
	 * @param biztype
	 * @return
	 */
	@Path(Keys.STATISTICS_SUMSTATISTICS_GETSUMBUSINESSTYPE)
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp businessList(@Valid PageReqt req) {
		int total=0;
		List<BizTypeStatistics> bizTypeStatisticsList = null;
		List<BizTypeStatistics> bizTypeStatisticsList4Count = null;
		List<BizTypeStatistics> bizList4ChartTmp = null;
		String beginDate =(String) req.getParams().get("beginDate");
		String endDate =(String) req.getParams().get("endDate");
		Integer operType = (Integer)req.getParams().get("operType");
		BizTypeStatistics totalstat = null;
		try {
			if(req.getSorts()!=null && req.getSorts().size()==0){
				Map<String,String> sorts = new HashMap<>();
				sorts.put("allReceiveSum", "desc");
				req.setSorts(sorts);
			}
			SimpleUser loginUser = SessionUtil.getCurUser();	
			QueryParameters params = new QueryParameters(req);
			params.addParam("enterpriseId", loginUser.getEnterpriseId());

			if(req.getParams().get("bizType")==null){
				int defaultBizType = statService.getEntpriseDefaultBizType(loginUser.getEnterpriseId());
				params.addParam("bizType", defaultBizType);
			}
			
			 if(operType==1){
				 total = statService.bizTypeStatisticsCount(params);				 
				 if(total > 0){	    	    
					    totalstat  = statService.getBizStatSendSUM(params);		    
						PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
					    params.setPage(pageInfo);
					    params.addParam("querysplit", "true");
					    bizTypeStatisticsList = statService.bizTypeStatistics(params);
			    	    bizList4ChartTmp = statService.copyData(bizTypeStatisticsList);
			    	    String bizTypeName = bizTypeStatisticsList.get(0).getBizTypeName();
			    	    Map<String,Object> data = new HashMap<>();
						data.put("d1", totalstat);
						data.put("d2", bizTypeStatisticsList);
						Map<String,Object>  status = getBizStatChartData(bizList4ChartTmp,bizTypeName!=null?bizTypeName:"全部业务类型");
						data.put("d3", status);		    	    
			    	    return PageResp.success(total, data);
					}else{
						return PageResp.emptyResult();
					}	
			 }else{
				    params.addParam("beginDate", DateUtil.toMonthFirstDay(beginDate));
					params.addParam("endDate", DateUtil.toMonthLastDay(endDate));
					bizTypeStatisticsList4Count = statService.bizTypeStatistics(params);
				    totalstat = (BizTypeStatistics)statService.getSumStatistics(null,bizTypeStatisticsList4Count,null,3);		
					Map<String,Object> data = new HashMap<>();
					if(ListUtil.isNotBlank(bizTypeStatisticsList4Count)){		
						total = bizTypeStatisticsList4Count.size();
						PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
					    params.setPage(pageInfo);
					    bizTypeStatisticsList = bizTypeStatisticsList4Count.subList(params.getPage().getFrom(), params.getPage().getTo());
					    bizList4ChartTmp = statService.copyData(bizTypeStatisticsList);
					    String bizTypeName = bizTypeStatisticsList.get(0).getBizTypeName();
						data.put("d1", totalstat);
						data.put("d2", bizTypeStatisticsList);
						Map<String,Object>  status = getBizStatChartData(bizList4ChartTmp,bizTypeName!=null?bizTypeName:"全部业务类型");
						data.put("d3", status);
						return PageResp.success(total,data);
					}else{
						return PageResp.emptyResult();
					}
			 }
				
							
			
		} catch (Exception e) {
			logger.error("Forward channel total statistics list page failed: ", e);
		}
		return PageResp.success();
	}
	
	/**
	 * 业务类型统计页面下拉框数据初始化查询
	 * @return
	 */
	@GET
	@Path(Keys.STATISTICS_SUMSTATISTICS_BUSITYPE)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getAllBusiType(){
		Map<Integer, String> typeMap = new HashMap<>();
		SimpleUser loginUser = SessionUtil.getCurUser();
		List<BizType> types= bizTypeService.findBizTypeByEntId4BizStat(loginUser.getEnterpriseId());	
		return JsonResp.success(types);
	}
	/**
	 * 部门统计导出
	 * @param req
	 * @return
	 */
    @POST
    @Path(Keys.STATISTICS_SUMSTATISTICS_EXPORTDEPARTMENT)
    public JsonResp export4Dept(@Context HttpServletRequest request,@Valid PageReqt req){

    	 SimpleUser curUser = SessionUtil.getCurUser();
         sysLogService.addLog(curUser,OperationType.EXPORT,"【部门统计】","SumStatistics","ExportDepartment",""); //添加访问日志
 	        try {
 	        	
 	            Map<String, Object> map = req.getParams();
 	            String fileName = (String) map.get("fileName");
 	            // 构造参数，String类型默认值为""，数字默认为-1
 	            String delimiter = (String) map.get("delimiter");
 	            Map<String, Object> params = new HashMap<>();
 	    		String operType = String.valueOf(map.get("operType"));
 	    		params.put("operType", operType);
 				String url = UrlResourceUtil.handleUrlResource(Keys.STATISTICS_SUMSTATISTICS, Keys.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT);
 				handleDataScope(null,url,map,2);	
 
 	            FileTask task = new FileTask();
 	            task.setFileName(fileName);
	   	        if(map.get("name")!=null && !map.get("name").equals("")){
	   	        	if(operType.equals("1")){
	   	        		task.setTaskName("部门日趋势统计导出"+"("+map.get("name")+")");
	   	        	}else{
	   	        		task.setTaskName("部门月趋势统计导出"+"("+map.get("name")+")");
	   	        	}	 
	   	        }else{
	   	        	if(operType.equals("1")){
	   	        		task.setTaskName("部门日趋势统计导出");
	   	        	}else{
	   	        		task.setTaskName("部门月趋势统计导出");
	   	        	}	    	        	
	   	        }  
 	            task.setType(TaskType.Export);
 	            task.setDataType(BizDataType.DeptSendStat);
 	            task.setPostTime(new Date());
 	            task.setUserId(curUser.getId());
 	            task.setHanldePercent(0);
 	            task.setState(TaskState.Wait);
 	            task.setPlatformId(config.getPlatformId());
 	            task.setParameters(XmlUtil.toXML(map));
 	            fileTaskService.save(task);
 	            executor.putTask2Queue(task);
 	        } catch (Exception e) {
 	            e.printStackTrace();
 	            return JsonResp.fail();
 	        }
 	        return JsonResp.success();
    }
	 /**
	 * 用户统计报表导出 
	 * @param req
	 * @return
	 */
    @POST
    @Path(Keys.STATISTICS_SUMSTATISTICS_EXPORTACCOUNT)
    public JsonResp export4User(@Context HttpServletRequest request,@Valid PageReqt req){
        try {
        	Integer operType = (Integer)req.getParams().get("operType");
  
         	userTaskService.addExportTask(MosBizDataType.Front_SumAccount_Exp,convertExport4User(request,req));
        	SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.EXPORT,"【用户统计】","SumStatistics","ExportAccount",""); //添加访问日志

            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
  
    /**
     * 用户统计导出查询参数
     * @param req
     * @return
     */
    private UserTask convertExport4User(HttpServletRequest request,PageReqt req) {
        SimpleUser user = SessionUtil.getCurUser();
        String deptId = (String)req.getParams().get("deptId");
        Integer operType = (Integer)req.getParams().get("operType");
        String beginDate = (String)req.getParams().get("beginDate");
        String today = sdf.format(new Date());
        String smsType = (String)req.getParams().get("smsType");
        String userId = (String)req.getParams().get("userId");
        
        DataScope ds = SessionUtil.getDataSope("/"+Keys.STATISTICS_SUMSTATISTICS+"/"+Keys.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT);
        Integer data_permission = 0;
        
		if(DataScope.GLOBAL == ds || DataScope.NONE == ds){
			data_permission = 3;
			if(deptId != null ){}else{
				Department rootDept = userMgrService.getDeptByEntId(user.getEnterpriseId()); 
            	deptId = String.valueOf(rootDept.getId());
			}
		}else if(DataScope.DEPARTMENT == ds){	
			data_permission = 2;
			if(deptId!=null ){}else{
            	deptId = String.valueOf(user.getParentId());
			}

		}else if(DataScope.PERSONAL == ds){
			deptId = String.valueOf(user.getParentId());
			data_permission = 1;
		}
   
        Integer enterpriseId = user.getEnterpriseId();
        List<UserTaskParameter> list = new ArrayList<>();
        list.add(new UserTaskParameter("mstype", smsType));
        list.add(new UserTaskParameter("operationType",null));
        list.add(new UserTaskParameter("channel_name",null));
        list.add(new UserTaskParameter("startTime",(String) req.getParams().get("beginDate")));
        list.add(new UserTaskParameter("endTime",(String) req.getParams().get("endDate")));
        list.add(new UserTaskParameter("departmentid",String.valueOf(deptId)));    
        
        list.add(new UserTaskParameter("userName",null));
        list.add(new UserTaskParameter("business_type_id",null));
        list.add(new UserTaskParameter("channel_num",null));
        list.add(new UserTaskParameter("carrier_id",null));
        list.add(new UserTaskParameter("name",null));
        if(today.equals(beginDate)){
        	list.add(new UserTaskParameter("isonTime","true"));// 这个是导出当天实时数据
        }else if(!today.equals(beginDate)){
        	list.add(new UserTaskParameter("isonTime","false"));// 这个是导出历史数据      	
        }
        
        //list.add(new UserTaskParameter("_user_id",String.valueOf(userId)));
        list.add(new UserTaskParameter("_enterprise_id",String.valueOf(enterpriseId)));
        list.add(new UserTaskParameter("_parent_id",null));
        list.add(new UserTaskParameter("user_id",userId));
        //list.add(new UserTaskParameter("_id",userId));
        list.add(new UserTaskParameter("subDepartmentParentId",null));
        list.add(new UserTaskParameter("billing_type","1"));
        list.add(new UserTaskParameter("data_permission",String.valueOf(data_permission)));
        String parameterStr = JSONObject.toJSON(list).toString();
        UserTask userTask = new UserTask();
        userTask.setMosParameters(parameterStr);
        userTask.setCreateUser(user.getId());
        userTask.setTaskName((String)req.getParams().get("name"));
        String fileName = (String) req.getParams().get("fileName");
        userTask.setFileType(fileName.substring(fileName.lastIndexOf("."),fileName.length()));
        userTask.setFileSeparator((String) req.getParams().get("delimiter"));
        return userTask;
    }

    /**
	 * 用户详情统计报表导出 
	 * @param req
	 * @return
	 */
	 @POST
	 @Path(Keys.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSEREXPORT)
	 public JsonResp exportUserDetailDate(@Context HttpServletRequest request,@Valid PageReqt req){

//	            userTaskService.addExportTask(MosBizDataType.Front_SumAccount_Exp,convertExport4User(request,req));
			   SimpleUser curUser = SessionUtil.getCurUser();
	           sysLogService.addLog(curUser,OperationType.EXPORT,"【用户详情统计】","SumStatistics","SumAccount/singleUserExport",""); //添加访问日志
	   	        try {

	   	            Map<String, Object> map = req.getParams();
	   	            String fileName = (String) map.get("fileName");
	   	            // 构造参数，String类型默认值为""，数字默认为-1
	   	            String delimiter = (String) map.get("delimiter");
	   	            Map<String, Object> params = new HashMap<>();
	   	    		String operType = String.valueOf(map.get("operType"));
	   	    		params.put("operType", operType);
	   		   		 if(operType.equals("1")){
	   		  
	   		   			 params.put("beginDate", map.get("beginDate")); 
	   		   			 params.put("endDate", map.get("endDate"));
	   		   		 }else{

	   		   			 params.put("beginDate", DateUtil.toMonthFirstDay((String)map.get("beginDate")));
	   		   			 params.put("endDate", DateUtil.toMonthLastDay((String)map.get("endDate")));
		
	   		   		 }
	   	            
	   	            params.put("userId", map.get("userId"));	   	           
	   	            params.put("enterpriseId", curUser.getEnterpriseId());
	   	            params.put("smsType", map.get("smsType"));
	   	            params.put("delimiter", delimiter);
					params.put("name", map.get("name"));
	   	            FileTask task = new FileTask();
	   	            task.setFileName(fileName);
		   	        if(map.get("name")!=null && !map.get("name").equals("")){
		   	        	if(operType.equals("1")){
		   	        		task.setTaskName("用户日趋势统计导出"+"("+map.get("name")+")");
		   	        	}else{
		   	        		task.setTaskName("用户月趋势统计导出"+"("+map.get("name")+")");
		   	        	}
		   	        	 
		   	        }else{
		   	        	if(operType.equals("1")){
		   	        		task.setTaskName("用户日趋势统计导出");
		   	        	}else{
		   	        		task.setTaskName("用户月趋势统计导出");
		   	        	}
		   	        }  
	   	            task.setType(TaskType.Export);
	   	            task.setDataType(BizDataType.UserSendDetailStat);
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
	   
	 /**
	  * 业务类型统计报表导出 
	  * @param req
	  * @return
	 */
	 @POST
	 @Path(Keys.STATISTICS_SUMSTATISTICS_EXPORTBUSINESSTYPE)
	 public JsonResp exportBizDate(@Valid PageReqt req){
		
//	            userTaskService.addExportTask(MosBizDataType.Back_Trend_Ent_Exp,convertExport4BizType(req));
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.EXPORT,"【业务类型统计】","SumStatistics","ExportBusinessType",""); //添加访问日志

            try {

   	            Map<String, Object> map = req.getParams();
   	            String fileName = (String) map.get("fileName");
   	            // 构造参数，String类型默认值为""，数字默认为-1
   	            String delimiter = (String) map.get("delimiter");
   	            Map<String, Object> params = new HashMap<>();
				String operType = String.valueOf(map.get("operType"));
				params.put("operType", operType);
				params.put("beginDate", map.get("beginDate"));
				params.put("endDate", map.get("endDate"));
				params.put("bizType", map.get("bizType"));
				params.put("enterpriseId", loginUser.getEnterpriseId());
				params.put("smsType", map.get("smsType"));
				params.put("delimiter", delimiter);
				params.put("name", map.get("name"));
   	            FileTask task = new FileTask();
   	            task.setFileName(fileName);
   	            
	   	        if(map.get("name")!=null && !map.get("name").equals("")){
	   	        	if(operType.equals("1")){
	   	        		System.out.println("name:"+map.get("name"));
	   	        		task.setTaskName("业务类型日趋势统计导出"+"("+map.get("name")+")");
	   	        	}else{
	   	        		task.setTaskName("业务类型月趋势统计导出"+"("+map.get("name")+")");
	   	        	}
	   	        	
	   	        }else{
	   	        	if(operType.equals("1")){
	   	        		task.setTaskName("业务类型日趋势统计导出");
	   	        	}else{
	   	        		task.setTaskName("业务类型月趋势统计导出");
	   	        	}
	   	        }  

   	            task.setType(TaskType.Export);
   	            task.setDataType(BizDataType.BizTypeSendStat);
   	            task.setPostTime(new Date());
   	            task.setUserId(loginUser.getId());
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
     * 自动用户账号
     */
    @GET
    @Path(Keys.STATISTICS_SUMSTATISTICS_USERSELECT)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteUserName(@QueryParam("userName") String userName,@Context HttpServletRequest request) {
        SimpleUser user = SessionUtil.getCurUser();
        DataScope dataScope = SessionUtil.getDataSope("/"+Keys.STATISTICS_SUMSTATISTICS+"/"+Keys.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT);
        QueryParameters params = new QueryParameters();
        String path ="";
        List<Map<String, String>> map = null;
        if(DataScope.GLOBAL == dataScope || DataScope.NONE == dataScope){  
	        params.addParam("userName",userName);
	        params.addParam("enterpriseId",user.getEnterpriseId());
	        params.addParam("platformId",Platform.FRONTKIT.getIndex());
	         map = statService.autoCompleteUserName(params);
		}else if(DataScope.DEPARTMENT == dataScope){					
		   // path = user.getParentId()+Delimiters.DOT;
		    path = userService.findPathById(user.getParentId()) + user.getParentId();
		    params.addParam("path",path);
		    params.addParam("userName",userName);
	        params.addParam("enterpriseId",user.getEnterpriseId());
	        params.addParam("platformId",Platform.FRONTKIT.getIndex());
	        map = statService.autoCompleteUserName(params);
		}else if(DataScope.PERSONAL == dataScope){
			params.addParam("userName",userName);
			params.addParam("enterpriseId",user.getEnterpriseId());
			params.addParam("userId",user.getId());
			params.addParam("platformId",Platform.FRONTKIT.getIndex());
			map = statService.autoCompleteUserName(params);
		}  
        return JsonResp.success(map);
    }
    /**
     * 获取企业下部门(去掉根部门)
     */
    @GET
    @Path(Keys.STATISTICS_SUMSTATISTICS_DEPTSELECT)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteDepartment(@QueryParam("deptName") String deptName) {
    	SimpleUser user = SessionUtil.getCurUser();
        DataScope dataScope = SessionUtil.getDataSope("/"+Keys.STATISTICS_SUMSTATISTICS+"/"+Keys.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT);
       
        QueryParameters parmas = new QueryParameters();
        String path = null;
        Department rootDept = null;
        List<Department> childDepartments = null;
        if(DataScope.GLOBAL == dataScope || DataScope.NONE == dataScope){
        	 //获取企业根部门节点
            rootDept = userMgrService.getDeptByEntId(user.getEnterpriseId());
            path = rootDept.getId() + Delimiters.DOT;
            parmas.addParam("deptName",deptName);
            parmas.addParam("path",path);
            parmas.addParam("selectALL", "true");
            childDepartments = userMgrService.autoCompleteDepartments(parmas);
            if(rootDept.getDeptName() != null && rootDept.getDeptName().toLowerCase().indexOf(deptName.toLowerCase()) >=0 ) {
            	childDepartments.add(0, rootDept);
            }
        }else if(DataScope.DEPARTMENT == dataScope){
        	//path = user.getParentId()+Delimiters.DOT;
        	path = userService.findPathById(user.getParentId()) + user.getParentId();
        	parmas.addParam("deptName",deptName);
            parmas.addParam("path",path);
            parmas.addParam("selectALL", "true");
           // childDepartments = userMgrService.autoCompleteDepartments(parmas);
            childDepartments = userMgrService.getDeptIncludeChildDept4UserStat(path, user.getParentId());
        }else if(DataScope.PERSONAL == dataScope){
        	path = userService.findPathById(user.getParentId()) + user.getParentId();
        	parmas.addParam("deptName",deptName);
            parmas.addParam("selectALL", "true");
            parmas.addParam("userId", user.getId());
            parmas.addParam("parentId", user.getParentId());
            parmas.addParam("enterpriseId", user.getEnterpriseId());
            childDepartments = userMgrService.autoCompleteDepartments(parmas);     	
        }

        List<Map<String ,String>> result = new ArrayList<>();
        for(Department department : childDepartments){
            Map<String,String> item = new HashMap<>();
            item.put("id",String.valueOf(department.getId()));
            item.put("name",department.getDeptName());
            item.put("path",department.getPath());
            result.add(item);
        }
        return JsonResp.success(result);
    }
    /**
     * 获取企业下部门树（获取的是企业全局的部门树）
     */ 
    @POST
    @Path(Keys.STATISTICS_SUMSTATISTICS_GETDEPTTREE)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getDeptTree() {

        SimpleUser user = SessionUtil.getCurUser();
        SimpleUser dept = userService.findDeptNameById(user.getId());
        String currentPath = dept.getPath();
        Department rootDept = null;
        String path = null;
        int level = 0;
        List<Department> childDepartments = null;
        if(currentPath!=null && currentPath!=""){
        	String [] arr = currentPath.split("\\"+Delimiters.DOT);
        	level = arr.length;
        }
        DataScope dataScope = SessionUtil.getDataSope("/"+Keys.STATISTICS_SUMSTATISTICS+"/"+Keys.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT);

        if(DataScope.GLOBAL == dataScope || DataScope.NONE == dataScope){
       	 //获取企业根部门节点
           rootDept = userMgrService.getDeptByEntId(user.getEnterpriseId());
           path = rootDept.getId() + Delimiters.DOT;          
           childDepartments = userMgrService.
           		getAllChildDepartments(path);
           
           childDepartments.add(0, rootDept);
       }else if(DataScope.DEPARTMENT == dataScope){
       		
    	   path = userService.findPathById(user.getParentId()) + user.getParentId();
       		rootDept = userMgrService.
                    getDeptByEntId(user.getEnterpriseId());           
            childDepartments = userMgrService.getDeptIncludeChildDept4UserStat(path, user.getParentId());
            // childDepartments.add(0, rootDept);
       }else if(DataScope.PERSONAL == dataScope){
       		return PageResp.emptyResult();
       }
        //获取企业根部门节点
        
        Map<String,Object> data = new HashMap<>();
        data.put("id", dept.getId());
        data.put("deptName", dept.getEnterpriseName());
        data.put("level", level);
        data.put("tree", childDepartments);
        return JsonResp.success(data);
    }

}

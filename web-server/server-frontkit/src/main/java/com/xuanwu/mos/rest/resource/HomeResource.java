package com.xuanwu.mos.rest.resource;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.BaseStatistics;
import com.xuanwu.mos.domain.entity.DepartmentStatistics;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.UserStatistics;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.service.CapitalAccountService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.rest.service.statistics.StatisticsService;
import com.xuanwu.mos.service.NoticeService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.UrlResourceUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by zhangz on 2017/3/30.
 */
@Component
@Path(Keys.HOME_INDEX)
public class HomeResource {
    private static final Logger LOG = LoggerFactory.getLogger(HomeResource.class);
	@Autowired
	private StatisticsService statService;
	@Autowired
	private UserService userService;
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private CapitalAccountService accountService;
	@Autowired
	private UserMgrService userMgrService;
	
	private static final FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
	
	private static final int MIN_DAY_HOUR = 0;
	private static final int MAX_DAY_HOUR = 23;
	
	@Path(Keys.HOME_STATISTICS)
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp redirectIndex(int permissionFlag){
	
		
		QueryParameters params = new QueryParameters();
		// 组装查询参数
		params.addParam("beginDate", sdf.format(getPrefixStartDate(6)));
		params.addParam("endDate", sdf.format(new Date()));
		
		
		Map<String,Object>  status = null;
		List<String> axis =  null;
		List<DepartmentStatistics> deptStatisticsList = null;
		List<BaseStatistics> chartdeptList = null;
		axis = getAxis();
		List<UserStatistics> userstatisticsList = null;
		Map<String,Object> data = new HashMap<>();
		// 没有统计报表任意权限时，则显示0
		if(permissionFlag ==4){
			UserStatistics userStat = new UserStatistics();
			userStat.setAllReceiveSum(0);
			userStat.setAllSendSum(0);
			userStat.setAllSuccessSum(0);
			userStat.setSuccesRate("0.00%");		
			status = getUserStatChartData(null,2,axis);
			data.put("sendStat", userStat);
			data.put("chartDate", status);
		
		//  查部门统计数据
		}else if(permissionFlag ==1){
			String url = UrlResourceUtil.handleUrlResource(Keys.STATISTICS_SUMSTATISTICS, Keys.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT);
			getDataScope(url,params);
			userstatisticsList = statService.findIndexRepot4User(params);
			UserStatistics allData = statService.getSumStatistics4Index(userstatisticsList);
			status = getUserStatChartData(userstatisticsList,2,axis);
			
			data.put("sendStat", allData);
			data.put("chartDate", status);
			
		// 查用户统计数据
		}else if(permissionFlag ==2){
			String url = UrlResourceUtil.handleUrlResource(Keys.STATISTICS_SUMSTATISTICS, Keys.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT);
			getDataScope(url,params);
			userstatisticsList = statService.findIndexRepot4User(params);
			UserStatistics allData = statService.getSumStatistics4Index(userstatisticsList);
			status = getUserStatChartData(userstatisticsList,2,axis);
			
			data.put("sendStat", allData);
			data.put("chartDate", status);
			
		// 查业务类型统计数据
		}else if(permissionFlag ==3){
			userstatisticsList = statService.findIndexRepot4User(params);
			UserStatistics allData = statService.getSumStatistics4Index(userstatisticsList);
			status = getUserStatChartData(userstatisticsList,2,axis);
			
			data.put("sendStat", allData);
			data.put("chartDate", status);
			
		}
		return PageResp.success(data);
	}
	public List<String> getAxis(){
		List<String> axis =  new ArrayList<>();
		for(int i=6;i>=0;i--){
			axis.add(sdf.format(getPrefixStartDate(i)));
		}
		return axis;
	}
	public void getDataScope(String url,QueryParameters params){
		// 数据权限 path 处理
		String path;
		DataScope ds = (DataScope) SessionUtil.getDataSope(url);
		SimpleUser loginUser = SessionUtil.getCurUser();
		if(DataScope.GLOBAL == ds || DataScope.NONE == ds){
			params.addParam("enterpriseId", loginUser.getEnterpriseId());
		}else if(DataScope.DEPARTMENT == ds){	
			params.addParam("enterpriseId", loginUser.getEnterpriseId());
			path = userService.findPathById(loginUser.getParentId());
			if (StringUtils.isBlank(path)) {
				path = loginUser.getParentId() + Delimiters.DOT;
			} else {
				path = path + loginUser.getParentId() + Delimiters.DOT;
			}
		    params.addParam("path", path);
		}else if(DataScope.PERSONAL == ds){
			params.addParam("enterpriseId", loginUser.getEnterpriseId());
			params.addParam("userId", loginUser.getId());
		}
	}
	/**
	 * 取得几天前的时间
	 */
	public static Date getPrefixStartDate(Integer day) {
		Date date = new Date();
		if (day == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);	
		date = cal.getTime();
		return date;
	}
	public static Date getPrefixStartHoure(Integer houres) {
		Date date = new Date();
		if (houres == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -houres);
		date = cal.getTime();
		
		return date;
	}
	public List<List<Long>> getNullDatas(List<List<Long>> datas,List<Long> series4AllSubmitSum,List<Long> series4AllSendSum,List<Long> series4AllSuccessSum){
		for(int i=0;i<7;i++){
			series4AllSubmitSum.add(new Long(0));
			series4AllSendSum.add(new Long(0));
			series4AllSuccessSum.add(new Long(0));
		}
		datas.add(series4AllSubmitSum);
		datas.add(series4AllSendSum);
		datas.add(series4AllSuccessSum);
		return datas;
	}

	
	public  Map<String,Object> getUserStatChartData(List<UserStatistics> tempList,int operType,List<String> axis){
		String[] legends = { "总提交量", "总发送量", "总成功量"};

		Map<String,Object> data = new HashMap<>();
		
		List<List<Long>> datas = new ArrayList<>();
		List<Long> series4AllSubmitSum = new ArrayList<>();
		List<Long> series4AllSendSum = new ArrayList<>();
		List<Long> series4AllSuccessSum = new ArrayList<>();
		long max =0;
		List<String> axisnew =  new ArrayList<>();
		if(tempList == null || tempList.size()<1){
			for(String date : axis){
				axisnew.add(DateUtil.convertDate(date, true));
			}
			data.put("name", "");
			data.put("title", "");
			data.put("legend", legends);
			data.put("datas", getNullDatas(datas,series4AllSubmitSum,series4AllSendSum,series4AllSuccessSum));
			data.put("xAxis", axisnew);
			data.put("yMax", 1);
			data.put("interval", max>0?max/7:0);
			return data;
		}
        for(String date:axis){
        	boolean flag = false;
        	int i=0;
        	for(;i<tempList.size();i++){
        		String statDate = tempList.get(i).getStatDate();
        		String tmpDate ="";
       
        		if(operType==1){
        			String dates[] = statDate.split("-");
        			if(dates[dates.length-1].startsWith("0")){
        				tmpDate = dates[dates.length-1].substring(1, 2);
        			}else{
        				tmpDate = dates[dates.length-1];
        			}
        			if(date.equals(tmpDate)){
        				
            			series4AllSubmitSum.add(tempList.get(i).getAllReceiveSum());
            			series4AllSendSum.add(tempList.get(i).getAllSendSum());
            			series4AllSuccessSum.add(tempList.get(i).getAllSuccessSum());
            			flag = true;
            			break;
            		}else{
            			
            		}
        		}else{
        			if(date.equals(statDate)){
            			series4AllSubmitSum.add(tempList.get(i).getAllReceiveSum());
            			series4AllSendSum.add(tempList.get(i).getAllSendSum());
            			series4AllSuccessSum.add(tempList.get(i).getAllSuccessSum());
            			flag = true;
            			break;
            		}else{
            			
            		}
        		}       		
    				
    		}
        	if(flag ==false){
        		series4AllSubmitSum.add(new Long(0));
    			series4AllSendSum.add(new Long(0));
    			series4AllSuccessSum.add(new Long(0));
        	}
        	
        }		
		
		datas.add(series4AllSubmitSum);
		datas.add(series4AllSendSum);
		datas.add(series4AllSuccessSum);
		data.put("name", "");
		data.put("title", "");
		data.put("legend", legends);
		data.put("datas", datas);
		
		for(String date : axis){
			axisnew.add(DateUtil.convertDate(date, true));
		}
		for(Long value:series4AllSubmitSum){
			if(max <=value){
				max = value;
			}
		}
		data.put("xAxis", axisnew);
		data.put("yMax", max);
		data.put("interval", max>0?max/7:0);
		return data;
	} 
//	private   List<String> getAxis(int startHour, int endHour) {
//		List<String> axis = new ArrayList<>();
//		if (endHour > startHour) {
//			for (int i = MIN_DAY_HOUR; startHour + i <= endHour; i++) {
//				axis.add(String.valueOf(startHour + i));
//			}
//		} else {
//			for (int i = MIN_DAY_HOUR; startHour + i <= MAX_DAY_HOUR; i++) {
//				axis.add(String.valueOf(startHour + i));
//			}
//			for (int i = MIN_DAY_HOUR; i <= endHour; i++) {
//				axis.add(String.valueOf(i));
//			}
//		}
//		return axis;
//	}
}

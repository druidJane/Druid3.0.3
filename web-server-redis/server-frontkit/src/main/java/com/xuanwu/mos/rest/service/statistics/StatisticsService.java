/**
 * 
 */
package com.xuanwu.mos.rest.service.statistics;

import com.xuanwu.mos.domain.entity.BaseStatistics;
import com.xuanwu.mos.domain.entity.BizTypeStatistics;
import com.xuanwu.mos.domain.entity.CarrierChannel;
import com.xuanwu.mos.domain.entity.DepartmentStatistics;
import com.xuanwu.mos.domain.entity.GsmsUser;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.UserStatistics;
import com.xuanwu.mos.domain.repo.UserRepo;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.CarrierChannelRepo;
import com.xuanwu.mos.rest.repo.statistics.BillAccountStatisticsRepo;
import com.xuanwu.mos.rest.repo.statistics.StatisticsRepo;
import com.xuanwu.mos.rest.service.BizDataService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.WebConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 */
@Service
public class StatisticsService {
	
	private final static Logger logger = LoggerFactory.getLogger(StatisticsService.class);
	
	@Autowired
	private BillAccountStatisticsRepo billAccountRepo;

	@Autowired
	private CarrierChannelRepo carrierChannelRepo;
	
	@Autowired
	private StatisticsRepo statisticsRepo;
	
    @Autowired
    private UserRepo userRepo;
	
//	@Autowired
//	private UserRepo userRepo;
	@Autowired
	private UserService userService;
	
	@Autowired
	private BizDataService bizDataService;


	private class BizComparator implements Comparator<BaseStatistics> {
		@Override
		public int compare(BaseStatistics o1, BaseStatistics o2) {
			return o1.getStatDate().compareTo(o2.getStatDate());
		}
	}

	private class HtyDeptComparator implements Comparator<DepartmentStatistics> {
		@Override
		public int compare(DepartmentStatistics o1, DepartmentStatistics o2) {
			return o1.getStatDate().compareTo(o2.getStatDate());
		}
	}
	private class ComparatorByReceiveNumAsc implements Comparator<BaseStatistics> {
		@Override
		public int compare(BaseStatistics o1, BaseStatistics o2) {
//			return String.valueOf(o1.getAllReceiveSum()).compareTo(String.valueOf(o2.getAllReceiveSum()));
			return (int)(o1.getAllReceiveSum() - o2.getAllReceiveSum());
		}
	}
	private class ComparatorByReceiveNumDesc implements Comparator<BaseStatistics> {
		@Override
		public int compare(BaseStatistics o1, BaseStatistics o2) {
			int flag=0;
			int compare = (int)(o1.getAllReceiveSum() - o2.getAllReceiveSum());
			if(compare > 0){
				flag = -1;
			}else{
				flag =1;
			}
			return flag;
		}
	}
	private class ComparatorBySuccessNumAsc implements Comparator<BaseStatistics> {
		@Override
		public int compare(BaseStatistics o1, BaseStatistics o2) {
//			return String.valueOf(o1.getAllSuccessSum()).compareTo(String.valueOf(o2.getAllSuccessSum()));
			return (int)(o1.getAllSuccessSum() - o2.getAllSuccessSum());
		}
	}
	private class ComparatorBySuccessNumDesc implements Comparator<BaseStatistics> {
		@Override
		public int compare(BaseStatistics o1, BaseStatistics o2) {
			int flag=0;int compare = (int)(o1.getAllSuccessSum() - o2.getAllSuccessSum());
			if(compare>0){
				flag = -1;
			}else{
				flag =1;
			}
			return flag;
		}
	}
	private class ComparatorBySendNumAsc implements Comparator<BaseStatistics> {
		@Override
		public int compare(BaseStatistics o1, BaseStatistics o2) {
//			return String.valueOf(o1.getAllSendSum()).compareTo(String.valueOf(o2.getAllSendSum()));
			return (int)(o1.getAllSendSum() - o2.getAllSendSum());
		}
	}
	private class ComparatorBySendNumDesc implements Comparator<BaseStatistics> {
		@Override
		public int compare(BaseStatistics o1, BaseStatistics o2) {
			int flag=0;
			if((int)(o1.getAllSendSum() - o2.getAllSendSum())>0){
				flag = -1;
			}else{
				flag =1;
			}
			return flag;
		}
	}
	private List<DepartmentStatistics> preHandleDeptStat(QueryParameters params, 
			List<DepartmentStatistics> stats, boolean withStatDate){
		if(ListUtil.isBlank(stats))
			return stats;
		String splitStr = Pattern.quote(Delimiters.DOT);
		String[] arr = null;
		DepartmentStatistics temp;
		GsmsUser dept;
		String key;
		int deptId=0;
		boolean isDays=false;
		
		if((Integer)params.getParams().get("operType") == 1){
			isDays = true;
		}
		for(DepartmentStatistics stat : stats){		
			key = stat.getPath() + (withStatDate ? stat.getStatDate() : "");
			if(stat.getPath()==null){
				deptId = (Integer)params.getParams().get("deptId");
			}else{
				arr = stat.getPath().split(splitStr);
			    deptId = Integer.parseInt(arr[arr.length - 1]);
			}
			
			
			dept = findDept(stat.getEnterpriseId(), deptId);
			stat.setDeptId(deptId);
			stat.setStatDateStr(convertDate(stat.getStatDate(),isDays,"-"));
			stat.setStatDate(convertDate2(stat.getStatDate(),isDays));
			stat.setDeptName(dept.getEnterpriseName());
		}
		return stats;
	}
	public String convertDate(String date,boolean isDays,String splitChar){
		String [] dateArr = null;
		String dateNew = "";
		if(date!=null && !"".equals(date)){
			dateArr = date.split(splitChar);
			if(isDays == true){
				dateNew = dateArr[0]+"年"+dateArr[1]+"月"+dateArr[2]+"日";
			}else{
				dateNew = dateArr[0]+"年"+dateArr[1]+"月";
			}
			
		}
		return dateNew;
	}
	public String convertDate2(String date,boolean isDays){
		String [] dateArr = null;
		String dateNew = "";
		if(date!=null && !"".equals(date)){
			dateArr = date.split("-");
			if(isDays == true){
				dateNew = dateArr[0]+"/"+dateArr[1]+"/"+dateArr[2];
			}else{
				dateNew = dateArr[0]+"/"+dateArr[1];
			}
		}
		return dateNew;
	}
	public GsmsUser findDept(int entId,int deptId){
		GsmsUser dept = bizDataService.getDeptById(entId, deptId);
		if(dept == null){
			dept = userService.findUserById(deptId);
		}
		return dept;		
	}
	/**
	 * 部门统计查询方法 
	 * @param params
	 * @return
	 */
	public List<DepartmentStatistics> deptHistoryStatistics(
			QueryParameters params) {
   	 	String endDate = (String)params.getParams().get("endDate");
		 String beginDate = (String)params.getParams().get("beginDate");
		 String today = sdf.format(new Date());
		 Integer operType = (Integer)params.getParams().get("operType");
		List<DepartmentStatistics> tempList = null;
		if(operType==1){
			// 只查实时数据
			if(DateUtil.compare_date(beginDate,today)>=0){
				tempList = statisticsRepo.findDeptRealTimeSend(params);	
			// 查历史和实时数据的并集
			}else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
				tempList = statisticsRepo.findDeptAllSend(params);	
			// endDate小于当天,查历史数据
			}else if(DateUtil.compare_date(endDate,today)<0 ){
				tempList = statisticsRepo.findDeptHistorySend(params);	
			}
		}else{
//			tempList = statisticsRepo.findDeptHistorySend(params);	
			tempList = mergeData4Dept(statisticsRepo.findDeptHistorySend(params));
		}
		
		tempList = preHandleDeptStat(params, tempList, false);

//		Collections.sort(tempList, new ComparatorByReceiveNumAsc());
		checkOrderFlag(tempList,null,null,params.getSorts());
		return tempList;
	}
	public void checkOrderFlag(List<DepartmentStatistics> deptList,List<BizTypeStatistics> bizList,List<UserStatistics> userList,Map<String, String> sorts){
		
		if(sorts.size()>0){
			if(sorts.get("allReceiveSum")!=null && sorts.get("allReceiveSum").equals("asc")){
				if(deptList!=null){
					Collections.sort(deptList, new ComparatorByReceiveNumAsc());
				}else if(bizList!=null){
					Collections.sort(bizList, new ComparatorByReceiveNumAsc());
				}else if(userList!=null){
					Collections.sort(userList, new ComparatorByReceiveNumAsc());
				}
				
			}else if(sorts.get("allReceiveSum")!=null && sorts.get("allReceiveSum").equals("desc")){
				if(deptList!=null){
					Collections.sort(deptList, new ComparatorByReceiveNumDesc());
				}else if(bizList!=null){
					Collections.sort(bizList, new ComparatorByReceiveNumDesc());
				}else if(userList!=null){
					Collections.sort(userList, new ComparatorByReceiveNumDesc());
				}
			}else if(sorts.get("allSendSum")!=null && sorts.get("allSendSum").equals("asc")){
				if(deptList!=null){
					Collections.sort(deptList, new ComparatorBySendNumAsc());
				}else if(bizList!=null){
					Collections.sort(bizList, new ComparatorBySendNumAsc());
				}else if(userList!=null){
					Collections.sort(userList, new ComparatorBySendNumAsc());
				}
			}else if(sorts.get("allSendSum")!=null && sorts.get("allSendSum").equals("desc")){
				if(deptList!=null){
					Collections.sort(deptList, new ComparatorBySendNumDesc());
				}else if(bizList!=null){
					Collections.sort(bizList, new ComparatorBySendNumDesc());
				}else if(userList!=null){
					Collections.sort(userList, new ComparatorBySendNumDesc());
				}
			}else if(sorts.get("allSuccessSum")!=null && sorts.get("allSuccessSum").equals("asc")){
				if(deptList!=null){
					Collections.sort(deptList, new ComparatorBySuccessNumAsc());
				}else if(bizList!=null){
					Collections.sort(bizList, new ComparatorBySuccessNumAsc());
				}else if(userList!=null){
					Collections.sort(userList, new ComparatorBySuccessNumAsc());
				}
			}else if(sorts.get("allSuccessSum")!=null && sorts.get("allSuccessSum").equals("desc")){
				if(deptList!=null){
					Collections.sort(deptList, new ComparatorBySuccessNumDesc());
				}else if(bizList!=null){
					Collections.sort(bizList, new ComparatorBySuccessNumDesc());
				}else if(userList!=null){
					Collections.sort(userList, new ComparatorBySuccessNumDesc());
				}
			}
		}else{
			if(deptList!=null){
				Collections.sort(deptList, new ComparatorByReceiveNumDesc());
			}else if(bizList!=null){
				Collections.sort(bizList, new ComparatorByReceiveNumDesc());
			}else if(userList!=null){
				Collections.sort(userList, new ComparatorByReceiveNumDesc());
			}
		}
	}
	/**
	 * 业务类型数据拷贝,同时按统计日期排序
	 */
	public List<BizTypeStatistics> copyData(List<BizTypeStatistics> dataList){
		List<BizTypeStatistics> tempList = new ArrayList<>();
		if(dataList!=null){
			for(BizTypeStatistics bizData:dataList){
				BizTypeStatistics biz = new BizTypeStatistics();
				long allReceiveSum = bizData.getAllReceiveSum();
				long allSendSum = bizData.getAllSendSum();
				long allSuccessSum = bizData.getAllSuccessSum();
				long successSumLT = bizData.getSuccessSumLT();
				long successSumYD = bizData.getSuccessSumYD();
				long successSumXLT = bizData.getSuccessSumXLT();
				long successSumCMDA = bizData.getSuccessSumCDMA();
				String statDate = bizData.getStatDate();
				String bizTypeName = bizData.getBizTypeName();
				biz.setAllReceiveSum(allReceiveSum);
				biz.setAllSendSum(allSendSum);
				biz.setAllSuccessSum(allSuccessSum);
				biz.setStatDate(statDate);
				biz.setBizTypeName(bizTypeName);
				biz.setSuccessSumYD(successSumYD);
				biz.setSuccessSumLT(successSumLT);
				biz.setSuccessSumXLT(successSumXLT);
				biz.setSuccessSumCDMA(successSumCMDA);
				tempList.add(biz);
			}
		}		
		Collections.sort(tempList, new BizComparator());
		return tempList;
	}
	/**
	 * 用户数据拷贝,同时按统计日期排序
	 */
	public List<UserStatistics> copyUserData(List<UserStatistics> dataList){
		List<UserStatistics> tempList = new ArrayList<>();
		if(dataList!=null){
			for(UserStatistics bizData:dataList){
				UserStatistics biz = new UserStatistics();
				long allReceiveSum = bizData.getAllReceiveSum();
				long allSendSum = bizData.getAllSendSum();
				long allSuccessSum = bizData.getAllSuccessSum();
				long successSumLT = bizData.getSuccessSumLT();
				long successSumYD = bizData.getSuccessSumYD();
				long successSumXLT = bizData.getSuccessSumXLT();
				long successSumCMDA = bizData.getSuccessSumCDMA();
				String statDate = bizData.getStatDate();
				String userName = bizData.getUserName();
				String deptName = bizData.getDeptName();
				biz.setAllReceiveSum(allReceiveSum);
				biz.setAllSendSum(allSendSum);
				biz.setAllSuccessSum(allSuccessSum);
				biz.setStatDate(statDate);
				biz.setUserName(userName);
				biz.setDeptName(deptName);
				biz.setSuccessSumYD(successSumYD);
				biz.setSuccessSumLT(successSumLT);
				biz.setSuccessSumXLT(successSumXLT);
				biz.setSuccessSumCDMA(successSumCMDA);
				tempList.add(biz);
			}
		}		
		Collections.sort(tempList, new BizComparator());
		return tempList;
	}
	/**
	 * 部门统计合并数据
	 * @param dataList
	 * @return
	 */
	public static List<DepartmentStatistics> mergeData4Dept(List<DepartmentStatistics> dataList){
		Map<String,DepartmentStatistics> deptMap = new HashMap<>();
		List<DepartmentStatistics> tempList = new ArrayList<>();
		List<DepartmentStatistics> mergeList = new ArrayList<>();
		for(DepartmentStatistics deptData:dataList){
			DepartmentStatistics dept = new DepartmentStatistics();
			long allReceiveSum = deptData.getAllReceiveSum();
			long allSendSum = deptData.getAllSendSum();
			long allSuccessSum = deptData.getAllSuccessSum();
			String path = deptData.getPath();
			long successSumLT = deptData.getSuccessSumLT();
			long successSumYD = deptData.getSuccessSumYD();
			long successSumXLT = deptData.getSuccessSumXLT();
			long successSumCMDA = deptData.getSuccessSumCDMA();
			String statDate = deptData.getStatDate();
			dept.setAllReceiveSum(allReceiveSum);
			dept.setAllSendSum(allSendSum);
			dept.setAllSuccessSum(allSuccessSum);
			dept.setStatDate(statDate);
			dept.setPath(path);
			dept.setSuccessSumYD(successSumYD);
			dept.setSuccessSumLT(successSumLT);
			dept.setSuccessSumXLT(successSumXLT);
			dept.setSuccessSumCDMA(successSumCMDA);
			tempList.add(dept);
		}
		for(DepartmentStatistics tempdept:tempList){
			String date = tempdept.getStatDate().substring(0, 7);
			String key = date+"_"+tempdept.getPath();
			if(deptMap.get(key)==null){
				deptMap.put(key, tempdept);
			}else{
				DepartmentStatistics temp = deptMap.get(key);
				temp.setAllReceiveSum(temp.getAllReceiveSum()+tempdept.getAllReceiveSum());
				temp.setAllSuccessSum(temp.getAllSuccessSum()+tempdept.getAllSuccessSum());
				temp.setAllSendSum(temp.getAllSendSum()+tempdept.getAllSendSum());
				temp.setSuccessSumLT(temp.getSuccessSumLT()+tempdept.getSuccessSumLT());
				temp.setSuccessSumYD(temp.getSuccessSumYD()+tempdept.getSuccessSumYD());
				temp.setSuccessSumXLT(temp.getSuccessSumXLT()+tempdept.getSuccessSumXLT());
				temp.setSuccessSumCDMA(temp.getSuccessSumCDMA()+tempdept.getSuccessSumCDMA());
				deptMap.put(key, temp);
			}
		}
		for(Map.Entry<String, DepartmentStatistics> mm : deptMap.entrySet()){
			
			mergeList.add(mm.getValue());
		}           
		return mergeList;
	}
	/**
	 * 业务类型统计合并数据
	 * @param dataList
	 * @return
	 */
	public static List<BizTypeStatistics> mergeData4Biz(List<BizTypeStatistics> dataList){
		Map<String,BizTypeStatistics> bizMap = new HashMap<>();
		List<BizTypeStatistics> tempList = new ArrayList<>();
		List<BizTypeStatistics> mergeList = new ArrayList<>();
		for(BizTypeStatistics deptData:dataList){
			BizTypeStatistics biz = new BizTypeStatistics();
			long allReceiveSum = deptData.getAllReceiveSum();
			long allSendSum = deptData.getAllSendSum();
			long allSuccessSum = deptData.getAllSuccessSum();
			String bizTypeName = deptData.getBizTypeName();
			long successSumLT = deptData.getSuccessSumLT();
			long successSumYD = deptData.getSuccessSumYD();
			long successSumXLT = deptData.getSuccessSumXLT();
			long successSumCMDA = deptData.getSuccessSumCDMA();
			String statDate = deptData.getStatDate();
			biz.setAllReceiveSum(allReceiveSum);
			biz.setAllSendSum(allSendSum);
			biz.setAllSuccessSum(allSuccessSum);
			biz.setStatDate(statDate);
			biz.setBizTypeName(bizTypeName);
			biz.setSuccessSumYD(successSumYD);
			biz.setSuccessSumLT(successSumLT);
			biz.setSuccessSumXLT(successSumXLT);
			biz.setSuccessSumCDMA(successSumCMDA);
			tempList.add(biz);
		}
		for(BizTypeStatistics tempdept:tempList){
			String date = tempdept.getStatDate().substring(0, 7);
			String key = date+"_"+tempdept.getBizTypeName();
			if(bizMap.get(key)==null){
				bizMap.put(key, tempdept);
			}else{
				BizTypeStatistics temp = bizMap.get(key);
				temp.setAllReceiveSum(temp.getAllReceiveSum()+tempdept.getAllReceiveSum());
				temp.setAllSuccessSum(temp.getAllSuccessSum()+tempdept.getAllSuccessSum());
				temp.setAllSendSum(temp.getAllSendSum()+tempdept.getAllSendSum());
				temp.setSuccessSumLT(temp.getSuccessSumLT()+tempdept.getSuccessSumLT());
				temp.setSuccessSumYD(temp.getSuccessSumYD()+tempdept.getSuccessSumYD());
				temp.setSuccessSumXLT(temp.getSuccessSumXLT()+tempdept.getSuccessSumXLT());
				temp.setSuccessSumCDMA(temp.getSuccessSumCDMA()+tempdept.getSuccessSumCDMA());
				bizMap.put(key, temp);
			}
		}
		for(Map.Entry<String, BizTypeStatistics> mm : bizMap.entrySet()){
			
			mergeList.add(mm.getValue());
		}           
		return mergeList;
	}
	/**
	 * 用户详情统计合并数据
	 * @param dataList
	 * @return
	 */
	public static List<UserStatistics> mergeData4User(List<UserStatistics> dataList){
		Map<String,UserStatistics> userMap = new HashMap<>();
		List<UserStatistics> tempList = new ArrayList<>();
		List<UserStatistics> mergeList = new ArrayList<>();
		for(UserStatistics userData:dataList){
			UserStatistics user = new UserStatistics();
			long allReceiveSum = userData.getAllReceiveSum();
			long allSendSum = userData.getAllSendSum();
			long allSuccessSum = userData.getAllSuccessSum();
			String userName = userData.getUserName();
			String deptName = userData.getDeptName();
			long successSumLT = userData.getSuccessSumLT();
			long successSumYD = userData.getSuccessSumYD();
			long successSumXLT = userData.getSuccessSumXLT();
			long successSumCMDA = userData.getSuccessSumCDMA();
			int enterpriseId = userData.getEnterpriseId();
			int deptId = userData.getDeptId();
			String statDate = userData.getStatDate();
			user.setAllReceiveSum(allReceiveSum);
			user.setAllSendSum(allSendSum);
			user.setAllSuccessSum(allSuccessSum);
			user.setStatDate(statDate);
			user.setUserName(userName);
			user.setDeptName(deptName);
			user.setSuccessSumYD(successSumYD);
			user.setSuccessSumLT(successSumLT);
			user.setSuccessSumXLT(successSumXLT);
			user.setSuccessSumCDMA(successSumCMDA);
			user.setEnterpriseId(enterpriseId);
			user.setDeptId(deptId);
			tempList.add(user);
		}
		for(UserStatistics tempdept:tempList){
			String date = tempdept.getStatDate().substring(0, 7);
			String key = date+"_"+tempdept.getUserName()+"_"+tempdept.getDeptName();
			if(userMap.get(key)==null){
				userMap.put(key, tempdept);
			}else{
				UserStatistics temp = userMap.get(key);
				temp.setAllReceiveSum(temp.getAllReceiveSum()+tempdept.getAllReceiveSum());
				temp.setAllSuccessSum(temp.getAllSuccessSum()+tempdept.getAllSuccessSum());
				temp.setAllSendSum(temp.getAllSendSum()+tempdept.getAllSendSum());
				temp.setSuccessSumLT(temp.getSuccessSumLT()+tempdept.getSuccessSumLT());
				temp.setSuccessSumYD(temp.getSuccessSumYD()+tempdept.getSuccessSumYD());
				temp.setSuccessSumXLT(temp.getSuccessSumXLT()+tempdept.getSuccessSumXLT());
				temp.setSuccessSumCDMA(temp.getSuccessSumCDMA()+tempdept.getSuccessSumCDMA());
				userMap.put(key, temp);
			}
		}
		for(Map.Entry<String, UserStatistics> mm : userMap.entrySet()){
			
			mergeList.add(mm.getValue());
		}           
		return mergeList;
	}
	/**
	 * 合并数据
	 * @param dataList
	 * @return
	 */
	public List<DepartmentStatistics> mergeData(List<DepartmentStatistics> dataList){
		Map<String,DepartmentStatistics> deptMap = new HashMap<>();
		List<DepartmentStatistics> tempList = new ArrayList<>();
		List<DepartmentStatistics> mergeList = new ArrayList<>();
		for(DepartmentStatistics deptData:dataList){
			DepartmentStatistics dept = new DepartmentStatistics();
			long allReceiveSum = deptData.getAllReceiveSum();
			long allSendSum = deptData.getAllSendSum();
			long allSuccessSum = deptData.getAllSuccessSum();
			String statDate = deptData.getStatDate();
			dept.setAllReceiveSum(allReceiveSum);
			dept.setAllSendSum(allSendSum);
			dept.setAllSuccessSum(allSuccessSum);
			dept.setStatDate(statDate);
			tempList.add(dept);
		}
		for(DepartmentStatistics tempdept:tempList){
			String statDate = tempdept.getStatDate();
			if(deptMap.get(statDate)==null){
				deptMap.put(statDate, tempdept);
			}else{
				DepartmentStatistics temp = deptMap.get(statDate);
				temp.setAllReceiveSum(temp.getAllReceiveSum()+tempdept.getAllReceiveSum());
				temp.setAllSuccessSum(temp.getAllSuccessSum()+tempdept.getAllSuccessSum());
				temp.setAllSendSum(temp.getAllSendSum()+tempdept.getAllSendSum());
				deptMap.put(statDate, temp);
			}
		}
		for(Map.Entry<String, DepartmentStatistics> mm : deptMap.entrySet()){			
			mergeList.add(mm.getValue());
		}  
		if(mergeList.size()>1){
			Collections.sort(mergeList, new HtyDeptComparator());
		}
		
		return mergeList;
	}
	public List<DepartmentStatistics> copyDeptData(List<DepartmentStatistics> dataList){
		List<DepartmentStatistics> tempList = new ArrayList<>();
		for(DepartmentStatistics deptData:dataList){
			DepartmentStatistics dept = new DepartmentStatistics();
			long allReceiveSum = deptData.getAllReceiveSum();
			long allSendSum = deptData.getAllSendSum();
			long allSuccessSum = deptData.getAllSuccessSum();
			long successSumYD = deptData.getSuccessSumYD();
			long successSumLT = deptData.getSuccessSumLT();
			long successSumXLT = deptData.getSuccessSumXLT();
			long successSumCDMA = deptData.getSuccessSumCDMA();
			String deptName = dept.getDeptName();
			String statDate = deptData.getStatDate();
			dept.setAllReceiveSum(allReceiveSum);
			dept.setAllSendSum(allSendSum);
			dept.setAllSuccessSum(allSuccessSum);
			dept.setStatDate(statDate);
			dept.setSuccessSumYD(successSumYD);
			dept.setSuccessSumLT(successSumLT);
			dept.setSuccessSumXLT(successSumXLT);
			dept.setSuccessSumCDMA(successSumCDMA);
			dept.setDeptName(deptName);
			tempList.add(dept);
		}
		return tempList;
	}
	
	public Object getSumStatistics(List<DepartmentStatistics> dataList,List<BizTypeStatistics> dataList4Biz,List<UserStatistics> dataList4User,int flag){
		DepartmentStatistics dept = null;
		UserStatistics user = null;
		BizTypeStatistics biz = null;
		if(flag == 1){
		    dept = new DepartmentStatistics();
			long allReceiveSum = 0;
			long allSendSum = 0;
			long allSuccessSum = 0;
			for(DepartmentStatistics deptData:dataList){
				allReceiveSum += deptData.getAllReceiveSum();
				allSendSum += deptData.getAllSendSum();
				allSuccessSum += deptData.getAllSuccessSum();
			}
			dept.setAllReceiveSum(allReceiveSum);
			dept.setAllSuccessSum(allSuccessSum);
			dept.setAllSendSum(allSendSum);
			dept.setSuccesRate(getSuccessRate(allReceiveSum,allSuccessSum));
			return dept;
		}else if(flag ==2){
			user = new UserStatistics();
			long allReceiveSum = 0;
			long allSendSum = 0;
			long allSuccessSum = 0;
			for(UserStatistics userData:dataList4User){
				allReceiveSum += userData.getAllReceiveSum();
				allSendSum += userData.getAllSendSum();
				allSuccessSum += userData.getAllSuccessSum();
			}
			user.setAllReceiveSum(allReceiveSum);
			user.setAllSuccessSum(allSuccessSum);
			user.setAllSendSum(allSendSum);
			user.setSuccesRate(getSuccessRate(allReceiveSum,allSuccessSum));
			return user;
		}else if(flag ==3){
			biz = new BizTypeStatistics();
			long allReceiveSum = 0;
			long allSendSum = 0;
			long allSuccessSum = 0;
			for(BizTypeStatistics bizData:dataList4Biz){
				allReceiveSum += bizData.getAllReceiveSum();
				allSendSum += bizData.getAllSendSum();
				allSuccessSum += bizData.getAllSuccessSum();
			}
			biz.setAllReceiveSum(allReceiveSum);
			biz.setAllSuccessSum(allSuccessSum);
			biz.setAllSendSum(allSendSum);
			biz.setSuccesRate(getSuccessRate(allReceiveSum,allSuccessSum));
			return biz;
		}
		return null;
	}
	public DepartmentStatistics getSumStatistics4DaysQuery(QueryParameters params){
		return statisticsRepo.getSumStatistics(params);
	}
	public UserStatistics getSumStatistics4Index(List<UserStatistics> dataList){
		UserStatistics dept = new UserStatistics();
		long allReceiveSum = 0;
		long allSendSum = 0;
		long allSuccessSum = 0;
		for(UserStatistics deptData:dataList){
			allReceiveSum += deptData.getAllReceiveSum();
			allSendSum += deptData.getAllSendSum();
			allSuccessSum += deptData.getAllSuccessSum();
		}
		dept.setAllReceiveSum(allReceiveSum);
		dept.setAllSuccessSum(allSuccessSum);
		dept.setAllSendSum(allSendSum);
		dept.setSuccesRate(getSuccessRate(allReceiveSum,allSuccessSum));
		return dept;
	}
	public static String getSuccessRate(long allreceiveSum,long allsucsssSum){
		double SuccesRate = (double)allreceiveSum>0?(double)allsucsssSum/(double)allreceiveSum:0;
		String sucRateStr = null;
		if(SuccesRate == 0){
			sucRateStr = "0.00%"; 
			return sucRateStr;
		}
	    double sucRate =  SuccesRate * 100;
	    DecimalFormat df = new DecimalFormat("#.00");
	    df.format(sucRate);
	    sucRateStr = String.valueOf(df.format(sucRate))+"%";		
		return sucRateStr;
	}

	public static void main(String[] args) {
        // TODO Auto-generated method stub
		System.out.println(getSuccessRate(191052,191052));
     }
	 public UserStatistics getUserSendStatic(QueryParameters params){
		 UserStatistics userStat =  statisticsRepo.getUserSendStatic(params);
		
		 return  userStat;
	 }
	 
	 	 
	 public List<UserStatistics> userPersonalRealtimeStatistics(UserStatistics sumUser){
		 if (sumUser.getUserId() <= 0)
				return Collections.emptyList();

			sumUser.setPath(null);
			sumUser.setUserName(null);
			QueryParameters params = new QueryParameters();
			PageInfo pageInfo = new PageInfo(1, 1, 1);
		    params.setPage(pageInfo);
			
			return userRealtimeStatistics(sumUser, params, true);
	 }

	 public List<UserStatistics> userRealtimeStatistics(UserStatistics sumUser,
			 QueryParameters param, boolean flag) {
		 	param.addParam("smsType", sumUser.getSmsType());
			param.addParam("currentDate", sumUser.getCurrentDate());
			param.addParam("path", sumUser.getPath());
			param.addParam("userId", sumUser.getUserId());
			List<UserStatistics> sumUserList = statisticsRepo.findUserRealtimeSend(param);	
			if (ListUtil.isNotBlank(sumUserList)) {
				UserStatistics tempUser = new UserStatistics();
				tempUser.setDeptId(WebConstants.STAT_SUM_ID);
				tempUser.setUserName(WebConstants.STAT_CUR_PAGE);
				GsmsUser dept;
				for(UserStatistics user : sumUserList) {
//					dept = findDept(sumUser.getEnterpriseId(), user.getDeptId());
//					user.setDeptName(dept.getEnterpriseName());
					tempUser.add(user);
				}
				if(flag) sumUserList.add(tempUser);
			}
			return sumUserList;
	 }
	 private static final FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
	 public int findUserHistorySendCount(QueryParameters params){
		 String endDate = (String)params.getParams().get("endDate");
		 String beginDate = (String)params.getParams().get("beginDate");
		 String today = sdf.format(new Date());
		 int count =0;
		 // 如果是查询当天的报表数据，则查实时表
		 if(beginDate.equals(today) && endDate.equals(today)){
			 count =  statisticsRepo.findUserRealTimeSendCount(params);
		 // 如果是查询截至到当天的数据，那就是历史记录和实时记录都要查
		 }else if(endDate.equals(today) && !beginDate.equals(today)){
			 count = statisticsRepo.findUserAllSendCount(params);
		 // 如果查非当天的记录那就查历史记录表
		 }else if(!endDate.equals(today)){
			 count =  statisticsRepo.findUserHistorySendCount(params);
		 }
		 return count;
	 }
	 public static long sumGroup(String groupStr){
		String group[] =  groupStr.split(",");
		Long data = new Long(0);
		for(int i=0;i<group.length;i++){
			data+=Long.valueOf(group[i]);
		}
		return data;		
	 }
	 /**
	  * 首页报表统计栏数据查询
	  * @param params
	  * @return
	  */
	 public List<UserStatistics> findIndexRepot4User(QueryParameters params){
		 return statisticsRepo.findIndexRepot4User(params);
	 }
     public String handlePath(String deptId){
    	 String path = "";
    	 path = userService.findPathById(Integer.valueOf(deptId));
			if (StringUtils.isBlank(path)) {
				path = deptId + Delimiters.DOT;
			} else {
				path = path + deptId + Delimiters.DOT;
			}
		return path;
     }
	 public List<UserStatistics> userHistoryStatistics(QueryParameters params){
		 String endDate = (String)params.getParams().get("endDate");
		 String beginDate = (String)params.getParams().get("beginDate");
		 String today = sdf.format(new Date());
		List<UserStatistics> sumUserList = null;
		GsmsUser dept;
		//String path = (String) params.getParams().get("path");
		
		if(beginDate.equals(today) && endDate.equals(today)){
			sumUserList = statisticsRepo.findUserRealtimeSend(params);
			checkOrderFlag(null,null,sumUserList,params.getSorts());
			if(ListUtil.isBlank(sumUserList)){
				return Collections.emptyList();
			}

			for (UserStatistics user : sumUserList) {
				dept = findDept(user.getEnterpriseId(), user.getDeptId());
				user.setDeptName(dept.getEnterpriseName());	
				
			}
		
		}else if(endDate.equals(today) && !beginDate.equals(today)){
			sumUserList = statisticsRepo.findUserAllSend(params);
			
			if(ListUtil.isBlank(sumUserList)){
				return Collections.emptyList();
			}else{
				for(UserStatistics userStatistics:sumUserList){
					dept = findDept(userStatistics.getEnterpriseId(), userStatistics.getDeptId());
					userStatistics.setDeptName(dept.getEnterpriseName());	
					long allReceiveSum = sumGroup(userStatistics.getAllReceiveSumStr());
					userStatistics.setAllReceiveSum(allReceiveSum);
					long allSendSum = sumGroup(userStatistics.getAllSendSumStr());
					userStatistics.setAllSendSum(allSendSum);
					long allSuccessSum = sumGroup(userStatistics.getAllSuccessSumStr());
					userStatistics.setAllSuccessSum(allSuccessSum);
					long successSumYD = sumGroup(userStatistics.getSuccessSumYDStr());
					userStatistics.setSuccessSumYD(successSumYD);
					long successSumLT = sumGroup(userStatistics.getSuccessSumLTStr());
					userStatistics.setSuccessSumLT(successSumLT);
					long successSumXLT = sumGroup(userStatistics.getSuccessSumXLTStr());
					userStatistics.setSuccessSumXLT(successSumXLT);
					long successSumCDMA = sumGroup(userStatistics.getSuccessSumCDMAStr());
					userStatistics.setSuccessSumCDMA(successSumCDMA);
				}
				checkOrderFlag(null,null,sumUserList,params.getSorts());
			}
			
		}else if(!endDate.equals(today)){
			sumUserList = statisticsRepo.findUserHistorySend(params);
			checkOrderFlag(null,null,sumUserList,params.getSorts());
			if(ListUtil.isBlank(sumUserList)){
				return Collections.emptyList();
			}
			for (UserStatistics user : sumUserList) {
				dept = findDept(user.getEnterpriseId(), user.getDeptId());
				user.setDeptName(dept.getEnterpriseName());	
				
			}
		}
		return sumUserList;
	 }
	 /**
	  * 查询业务类型统计列表数据
	  * @param biztype
	  * @param param
	  * @return
	  */
     public List<BizTypeStatistics> bizTypeStatistics(QueryParameters params){
    	 String endDate = (String)params.getParams().get("endDate");
		 String beginDate = (String)params.getParams().get("beginDate");
		 String today = sdf.format(new Date());
		 Integer operType = (Integer)params.getParams().get("operType");
		 List<BizTypeStatistics> sumBizTypeList = null;
		 
		 boolean isDays=false;

			// 如果是查询当天的报表数据，则查实时表
			 if(operType==1){
				 isDays = true;
				 // 只查实时数据
				 if(DateUtil.compare_date(beginDate,today)>=0){
					 sumBizTypeList = statisticsRepo.bizTypeRealTimeStatistics(params);
					 checkOrderFlag(null,sumBizTypeList,null,params.getSorts());
					 for(BizTypeStatistics bizType:sumBizTypeList){
//						if(bizType.getState()==2){
//							bizType.setBizTypeName(bizType.getBizTypeName()+"【已删除】");
//						}
						 bizType.setStatDateStr(convertDate(bizType.getStatDate(),isDays,"-"));
						 bizType.setStatDate(convertDate2(bizType.getStatDate(),isDays));
					 }
				 // 查历史和实时数据的并集
				 }else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
					 sumBizTypeList = statisticsRepo.bizTypeALLStatistics(params);
					 checkOrderFlag(null,sumBizTypeList,null,params.getSorts());
					 if(ListUtil.isBlank(sumBizTypeList)){
							return Collections.emptyList();
						}else{
							for(BizTypeStatistics bizType:sumBizTypeList){
//								if(bizType.getState()==2){
//									bizType.setBizTypeName(bizType.getBizTypeName()+"【已删除】");
//								}
								bizType.setStatDateStr(convertDate(bizType.getStatDate(),isDays,"-"));
								bizType.setStatDate(convertDate2(bizType.getStatDate(),isDays));
							}
						}
				 // endDate小于当天,查历史数据
				 }else if(DateUtil.compare_date(endDate,today)<0 ){
					 sumBizTypeList = statisticsRepo.bizTypeStatistics(params);
					 checkOrderFlag(null,sumBizTypeList,null,params.getSorts());
					 for(BizTypeStatistics bizType:sumBizTypeList){
//						if(bizType.getState()==2){
//							bizType.setBizTypeName(bizType.getBizTypeName()+"【已删除】");
//						}
						 bizType.setStatDateStr(convertDate(bizType.getStatDate(),isDays,"-"));
						 bizType.setStatDate(convertDate2(bizType.getStatDate(),isDays));
					 }
				 }			 
			 }else{
				 sumBizTypeList = mergeData4Biz(statisticsRepo.bizTypeStatistics(params));
				 checkOrderFlag(null,sumBizTypeList,null,params.getSorts());
				 for(BizTypeStatistics bizType:sumBizTypeList){
//					if(bizType.getState()==2){
//						bizType.setBizTypeName(bizType.getBizTypeName()+"【已删除】");
//					}
					 bizType.setStatDateStr(convertDate(bizType.getStatDate(),isDays,"-"));
					 bizType.setStatDate(convertDate2(bizType.getStatDate(),isDays));
				 }
			 }
			 if(ListUtil.isBlank(sumBizTypeList)){
					return Collections.emptyList();
			 }
    	 return sumBizTypeList;
     }
     /**
	  * 查询业务类型统计列表数据总数
	  * @param biztype
	  * @return
	  */
	 public int bizTypeStatisticsCount(QueryParameters params){
		 String endDate = (String)params.getParams().get("endDate");
		 String beginDate = (String)params.getParams().get("beginDate");
		 String today = sdf.format(new Date());
		 Integer operType = (Integer)params.getParams().get("operType");
		 int count =0;
		// 如果是查询当天的报表数据，则查实时表
		 if(operType==1){
			 // 只查实时数据
			 if(DateUtil.compare_date(beginDate,today)>=0){
				 count = statisticsRepo.bizTypeRealTimeCount(params);
			 // 查历史和实时数据的并集
			 }else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
				 count = statisticsRepo.bizTypeAllSendCount(params);
			 // endDate小于当天,查历史数据
			 }else if(DateUtil.compare_date(endDate,today)<0 ){
				 count = statisticsRepo.bizTypeStatisticsCount(params);
			 }			 
		 }else{
			 count = statisticsRepo.bizTypeStatisticsCount(params);
		 }	
		 return count;
	 }

	 public int getCount4UserStatListByUserId(UserStatistics userStatistics){
		 return statisticsRepo.getCount4UserStatListByUserId(userStatistics);
	 }

	public List<CarrierChannel> findAllChannel(){
		return carrierChannelRepo.findAllChannel();
	}
	/**
	 * 指定用户详情数据条数查询
	 * @param params
	 * @return
	 */
	public int findSelectedUserSendCount(QueryParameters params){
		Integer operType = (Integer)params.getParams().get("operType");
		String endDate = (String)params.getParams().get("endDate");
		String beginDate = (String)params.getParams().get("beginDate");
		String today = sdf.format(new Date());
		int count =0;
		 // 如果是查询当天的报表数据，则查实时表
		 if(operType==1){
			 // 只查实时数据
			 if(DateUtil.compare_date(beginDate,today)>=0){
				 count = statisticsRepo.findSelectedUserRealTimeSendCount(params);
			 // 查历史和实时数据的并集
			 }else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
				 count = statisticsRepo.findSelectedUserAllSendCount(params);
			 // endDate小于当天,查历史数据
			 }else if(DateUtil.compare_date(endDate,today)<0 ){
				 count = statisticsRepo.findSelectedUserSendCount(params);
			 }			 
		 }else{
			 count = statisticsRepo.findSelectedUserSendCount(params);
		 }	
		return count;
	}
	/**
	 * 指定用户详情数据列表查询
	 * @param params
	 * @return
	 */
	public List<UserStatistics> selectedUserStatisticsList(QueryParameters params){
		Integer operType = (Integer)params.getParams().get("operType");
		String endDate = (String)params.getParams().get("endDate");
		String beginDate = (String)params.getParams().get("beginDate");
		String today = sdf.format(new Date());
		List<UserStatistics> sumUserList = null;
		GsmsUser dept;
		boolean isDays=false;
		// 如果是查询当天的报表数据，则查实时表
		 if(operType==1){
			 isDays =true;
			 // 只查实时数据
			 if(DateUtil.compare_date(beginDate,today)>=0){
				 sumUserList = statisticsRepo.selectedUserRealTimeList(params);
				 checkOrderFlag(null,null,sumUserList,params.getSorts());
				 for(UserStatistics userStatistics:sumUserList){
					 dept = findDept(userStatistics.getEnterpriseId(), userStatistics.getDeptId());
					 userStatistics.setDeptName(dept.getEnterpriseName());
					 userStatistics.setStatDateStr(convertDate(userStatistics.getStatDate(),isDays,"-"));
					 userStatistics.setStatDate(convertDate2(userStatistics.getStatDate(),isDays));
				 }
				
			 // 查历史和实时数据的并集
			 }else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
				 sumUserList = statisticsRepo.selectedUserAllSendList(params);
				 checkOrderFlag(null,null,sumUserList,params.getSorts());
				 if(ListUtil.isBlank(sumUserList)){
						return Collections.emptyList();
					}else{
						for(UserStatistics userStatistics:sumUserList){
							dept = findDept(userStatistics.getEnterpriseId(), userStatistics.getDeptId());
							userStatistics.setDeptName(dept.getEnterpriseName());
							userStatistics.setStatDateStr(convertDate(userStatistics.getStatDate(),isDays,"-"));
							userStatistics.setStatDate(convertDate2(userStatistics.getStatDate(),isDays));
						}
					}
			 // endDate小于当天,查历史数据
			 }else if(DateUtil.compare_date(endDate,today)<0 ){
				 sumUserList = statisticsRepo.selectedUserStatisticsList(params);
				 checkOrderFlag(null,null,sumUserList,params.getSorts());
				 for(UserStatistics userStatistics:sumUserList){
					 dept = findDept(userStatistics.getEnterpriseId(), userStatistics.getDeptId());
					 userStatistics.setDeptName(dept.getEnterpriseName());
					 userStatistics.setStatDateStr(convertDate(userStatistics.getStatDate(),isDays,"-"));
					 userStatistics.setStatDate(convertDate2(userStatistics.getStatDate(),isDays));
				 }
			 }			 
		 }else{
			 sumUserList =mergeData4User(statisticsRepo.selectedUserStatisticsList(params));
			 checkOrderFlag(null,null,sumUserList,params.getSorts());
			 for(UserStatistics userStatistics:sumUserList){
				 dept = findDept(userStatistics.getEnterpriseId(), userStatistics.getDeptId());
				 userStatistics.setDeptName(dept.getEnterpriseName());
				 userStatistics.setStatDateStr(convertDate(userStatistics.getStatDate(),isDays,"-"));
				 userStatistics.setStatDate(convertDate2(userStatistics.getStatDate(),isDays));
			 }
		 }
		 if(ListUtil.isBlank(sumUserList)){
				return Collections.emptyList();
		 }
		 
		return sumUserList;
	}
	
	 public List<Map<String, String>> autoCompleteUserName(QueryParameters parmas) {       
	        List<SimpleUser> userList = userRepo.autoCompleteUserName4UserStat(parmas);
	        List<Map<String ,String>> result = new ArrayList<>();
	        for(SimpleUser user : userList){
	            Map<String,String> item = new HashMap<>();
	            item.put("id",String.valueOf(user.getId()));
	            item.put("name",user.getUsername());
	            result.add(item);
	        }
	        return result;
	    }

	public int getEntpriseDefaultBizType(int entpriseId){
		return statisticsRepo.getEntpriseDefaultBizType(entpriseId);
	}	
	
	/**
	 * 部门统计列表查询总数据量方法
	 * @param params
	 * @return
	 */
	public int findDeptSendCount(QueryParameters params){
		Integer operType = (Integer)params.getParams().get("operType");
		String endDate = (String)params.getParams().get("endDate");
		String beginDate = (String)params.getParams().get("beginDate");
		String today = sdf.format(new Date());
		int count =0;
		 // 如果是查询当天的报表数据，则查实时表
		 if(operType==1){
			 // 只查实时数据
			 if(DateUtil.compare_date(beginDate,today)>=0){
				 count = statisticsRepo.findDeptRealTimeSendCount(params);
			 // 查历史和实时数据的并集
			 }else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
				 count = statisticsRepo.findDeptRealTimeSendCount(params) + statisticsRepo.deptCount4History(params);
			 // endDate小于当天,查历史数据
			 }else if(DateUtil.compare_date(endDate,today)<0 ){
				 count = statisticsRepo.deptCount4History(params);
			 }			 
		 }else{
			 count = statisticsRepo.findDeptHistorySendCount(params);
		 }	
		return count;
	}
	/**
	 * 获取部门统计页面汇总数据
	 */
	public DepartmentStatistics getDeptStatSendSUM(QueryParameters params){
		Integer operType = (Integer)params.getParams().get("operType");
		String endDate = (String)params.getParams().get("endDate");
		String beginDate = (String)params.getParams().get("beginDate");
		String today = sdf.format(new Date());
		DepartmentStatistics deptsum = null;
		DepartmentStatistics deptReal = null;
		DepartmentStatistics deptHistory = null;
		if(operType==1){
			 // 只查实时数据
			 if(DateUtil.compare_date(beginDate,today)>=0){
				 deptsum = statisticsRepo.deptStatSum4RealTime(params);
			 // 查历史和实时数据的并集
			 }else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
				 deptReal =  statisticsRepo.deptStatSum4RealTime(params);
				 deptHistory =  statisticsRepo.deptStatSUM4History(params);
				 deptsum = new DepartmentStatistics();
				 deptsum.setAllReceiveSum(deptReal.getAllReceiveSum()+deptHistory.getAllReceiveSum());
				 deptsum.setAllSendSum(deptReal.getAllSendSum()+deptHistory.getAllSendSum());
				 Long hisSucSum = deptHistory.getAllSuccessSum();
				 if(hisSucSum!=null){
					 deptsum.setAllSuccessSum(deptReal.getAllSuccessSum()+deptHistory.getAllSuccessSum());
				 }				 
			 // endDate小于当天,查历史数据
			 }else if(DateUtil.compare_date(endDate,today)<0 ){
				 deptsum = statisticsRepo.deptStatSUM4History(params);
			 }			 
		 }else{
			 params.addParam("beginDate", params.getParams().get("beginMonth"));
			 params.addParam("endDate", params.getParams().get("endMonth"));
			 deptsum = statisticsRepo.deptStatSUM4History(params);
		 }
		return deptsum;
	}
	/**
	 * 获取业务类型统计页面汇总数据
	 */
	public BizTypeStatistics getBizStatSendSUM(QueryParameters params){
		Integer operType = (Integer)params.getParams().get("operType");
		String endDate = (String)params.getParams().get("endDate");
		String beginDate = (String)params.getParams().get("beginDate");
		String today = sdf.format(new Date());
		BizTypeStatistics deptsum = null;
		BizTypeStatistics deptReal = null;
		BizTypeStatistics deptHistory = null;
		if(operType==1){
			 // 只查实时数据
			 if(DateUtil.compare_date(beginDate,today)>=0){
				 deptsum = statisticsRepo.getBizStatSumData4RealTime(params);
			 // 查历史和实时数据的并集
			 }else if(DateUtil.compare_date(endDate,today)>=0 && DateUtil.compare_date(beginDate,today)<0){
				 deptReal =  statisticsRepo.getBizStatSumData4RealTime(params);
				 deptHistory =  statisticsRepo.getBizStatSumData4History(params);
				 deptsum = new BizTypeStatistics();
				 
				 deptsum.setAllReceiveSum(deptReal.getAllReceiveSum()+deptHistory.getAllReceiveSum());
				 deptsum.setAllSendSum(deptReal.getAllSendSum()+deptHistory.getAllSendSum());
				 Long hisSucSum = deptHistory.getAllSuccessSum();
				 if(hisSucSum!=null){
					 deptsum.setAllSuccessSum(deptReal.getAllSuccessSum()+deptHistory.getAllSuccessSum());
				 }				 
			 // endDate小于当天,查历史数据
			 }else if(DateUtil.compare_date(endDate,today)<0 ){
				 deptsum = statisticsRepo.getBizStatSumData4History(params);
			 }			 
		 }
		return deptsum;
	}
}

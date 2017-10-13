package com.xuanwu.mos.rest.service.statistics;

import com.xuanwu.mos.domain.entity.BillingAcountInfo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.statistics.BillAccountStatisticsRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class BillingCountStatisticsService {

	private final static Logger logger = LoggerFactory.getLogger(BillingCountStatisticsService.class);
	
	private class BillCompare implements Comparator<BillingAcountInfo> {
		@Override
		public int compare(BillingAcountInfo o1, BillingAcountInfo o2) {
			return o1.getDeductTime().compareTo(o2.getDeductTime());
		}
	}
	@Autowired
	private BillAccountStatisticsRepo billAccountRepo;
	
	public String convertDate(String date,boolean isDays){
		String [] dateArr = null;
		String dateNew = "";
		if(date!=null && !"".equals(date)){
			dateArr = date.split("-");
			if(isDays == true){
				dateNew = dateArr[0]+"年"+dateArr[1]+"月"+dateArr[2]+"日";
			}else{
				dateNew = dateArr[0]+"年"+dateArr[1]+"月";
			}
			
		}
		return dateNew;
	}
	
	// 计费账户总数统计
	 public int findBillingAccountInfoByDaysCount(QueryParameters params){
		 
		 return billAccountRepo.findBillingAccountInfoByDaysCount(params);
	
	 }
	 // 计费账户统计列表查询
	 public List<BillingAcountInfo> findBillingAccountInfoByDaysList(QueryParameters params){
		 List<BillingAcountInfo> billAcountList = null;
		 billAcountList = billAccountRepo.findBillingAccountInfoByDaysList(params);
		 for(BillingAcountInfo billingAcount:billAcountList){

			 billingAcount.setSumConsumeStr(sub4Decimail(billingAcount.getSumConsume()));
			 billingAcount.setSmsConsumeStr(sub4Decimail(billingAcount.getSmsConsume()));
			 billingAcount.setMmsConsumeStr(sub4Decimail(billingAcount.getMmsConsume()));
			 if(billingAcount.getParentId()==0){
				 billingAcount.setAccountName("企业统一账户");
			 }
		 }
		 
		  return billAcountList;
	 }
	 
	// 计费账户总数统计
	 public int findBillingAccountInfoByMonthsCount(QueryParameters params){
		 return billAccountRepo.findBillingAccountInfoByMonthsCount(params);
	 }
	 // 计费账户统计日趋势列表查询
	 public List<BillingAcountInfo> findBillingAccountInfoByMonthsList(QueryParameters params){
		 return billAccountRepo.findBillingAccountInfoByMonthsList(params);
	 }

		/**
		 * 计费账户数据拷贝,同时按统计日期排序
		 */
		public List<BillingAcountInfo> copyBillData(List<BillingAcountInfo> dataList){
			List<BillingAcountInfo> tempList = new ArrayList<>();
			if(dataList!=null){
				for(BillingAcountInfo billData:dataList){
					BillingAcountInfo bill = new BillingAcountInfo();
					double smsConsume = billData.getSmsConsume();
					double mmsConsume = billData.getMmsConsume();
					double sumConsume = billData.getSumConsume();
					String deductTime = billData.getDeductTime();
					String deductTime4Grid = billData.getDeductTime4Grid();
					bill.setSmsConsume(smsConsume);
					bill.setMmsConsume(mmsConsume);
					bill.setSumConsume(sumConsume);
					bill.setDeductTime(deductTime);
					bill.setDeductTime4Grid(deductTime4Grid);
					tempList.add(bill);
				}
			}		
			Collections.sort(tempList, new BillCompare());
			return tempList;
		}
	 // 查计费账户统计用户列表
	 public List<BillingAcountInfo> queryUserListAccountByEntID(QueryParameters params){
		 List<BillingAcountInfo> detailBillingList = null;
		 Integer operType = (Integer)params.getParams().get("operType");
		 boolean isDays = false;
			if(operType ==1){
				isDays = true;
			}
		 detailBillingList = billAccountRepo.queryUserListAccountByEntID(params);
		 for(BillingAcountInfo billingAcount:detailBillingList){				
			  billingAcount.setSumConsumeStr(sub4Decimail(billingAcount.getSumConsume()));
			  billingAcount.setSmsConsumeStr(sub4Decimail(billingAcount.getSmsConsume()));
			  billingAcount.setMmsConsumeStr(sub4Decimail(billingAcount.getMmsConsume()));	
			  billingAcount.setDeductTimeStr(convertDate(billingAcount.getDeductTime(),isDays));
			  billingAcount.setDeductTime4Grid(convertDate2(billingAcount.getDeductTime(),isDays));
			  if(billingAcount.getParentId()==0){
				  billingAcount.setAccountName("企业统一账户");
			  }
		  }
		 return detailBillingList;
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
	 public String sub4Decimail(double preValue){
		 String newValue="0.0000";
 		 DecimalFormat df =new DecimalFormat("#####0.0000");
		 if(preValue>0){
			 newValue = df.format(preValue);
		 }
		 return newValue;
	 }
	 public double sub4Decimailnew(double preValue){
		 double   newValue   =   0.0000;  
		 BigDecimal   b   =   new   BigDecimal(preValue);  
		    newValue   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
		    
		    
		 return newValue;
	 }
	 // 查询计费账户详情数据总数
	 public int queryCountAccountByEntID(QueryParameters params){
		 return billAccountRepo.queryCountAccountByEntID(params);
	 }
	 // 查询计费账户详情数据列表
	 public List<BillingAcountInfo> queryListByAccountId(QueryParameters params){
		 return billAccountRepo.queryListByAccountId(params);
	 }
	 
	 // 查询详情页中总消费、短信消费、彩信消费汇总数据
	 public BillingAcountInfo querySumAccountbyAccountId(QueryParameters params){
		 return billAccountRepo.querySumAccountbyAccountId(params);
	 }
	 // 查询用户消费情况总数
	 public int userDetailCount(QueryParameters params){
		 return billAccountRepo.userDetailCount(params);
	 }
	 // 查询用户消费情况列表
	 public List<BillingAcountInfo> userDetailList(QueryParameters params){
		 return billAccountRepo.userDetailList(params);
	 }
}

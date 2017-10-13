package com.xuanwu.mos.rest.repo.statistics;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.BillingAcountInfo;
import com.xuanwu.mos.domain.entity.DepartmentStatistics;
import com.xuanwu.mos.dto.QueryParameters;
@Repository
public class BillAccountStatisticsRepo extends GsmsMybatisEntityRepository<BillingAcountInfo> {

	@Override
	protected String namesapceForSqlId() {
		// TODO Auto-generated method stub
		return "com.xuanwu.mos.mapper.StatisticsMapper";
	}
	// 计费账户总数统计
	 public int findBillingAccountInfoByDaysCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("findBillingAccountInfoByDaysCount"), params);
		 }
	 }
	 // 计费账户统计日趋势列表查询
	 public List<BillingAcountInfo> findBillingAccountInfoByDaysList(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
			 return sqlsession.selectList(fullSqlId("findBillingAccountInfoByDaysList"), params);
			
		 }
	 }
	 
	// 计费账户总数统计
	 public int findBillingAccountInfoByMonthsCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
			 return sqlsession.selectOne(fullSqlId("findBillingAccountInfoByMonthsCount"), params);
			
		 }
	 }
	 // 计费账户统计日趋势列表查询
	 public List<BillingAcountInfo> findBillingAccountInfoByMonthsList(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
			 return sqlsession.selectList(fullSqlId("findBillingAccountInfoByMonthsList"), params);
			
		 }
	 }
	 
	 // 查计费账户统计用户列表
	 public List<BillingAcountInfo> queryUserListAccountByEntID(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
			 return sqlsession.selectList(fullSqlId("queryUserListAccountByEntID"), params);
			
		 }
	 }
	 // 查询计费账户详情数据总数
	 public int queryCountAccountByEntID(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectOne(fullSqlId("queryCountAccountByEntID"), params);
			
		 }
	 }
	 // 查询计费账户详情数据列表
	 public List<BillingAcountInfo> queryListByAccountId(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectList(fullSqlId("queryListByAccountId"), params);			
		 }
	 }
	 
	 // 查询详情页中总消费、短信消费、彩信消费汇总数据
	 public BillingAcountInfo querySumAccountbyAccountId(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectOne(fullSqlId("querySumAccountbyAccountId"), params);			
		 }
	 }
	 public int userDetailCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectOne(fullSqlId("userDetailCount"), params);			
		 }
	 }
	 public List<BillingAcountInfo> userDetailList(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectList(fullSqlId("userDetailList"), params);			
		 }
	 }
}

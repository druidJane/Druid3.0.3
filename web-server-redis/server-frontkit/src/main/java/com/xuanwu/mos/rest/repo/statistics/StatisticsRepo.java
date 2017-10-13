package com.xuanwu.mos.rest.repo.statistics;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.BillingAcountInfo;
import com.xuanwu.mos.domain.entity.BizTypeStatistics;
import com.xuanwu.mos.domain.entity.DepartmentStatistics;
import com.xuanwu.mos.domain.entity.UserStatistics;
import com.xuanwu.mos.dto.QueryParameters;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class StatisticsRepo extends GsmsMybatisEntityRepository<BillingAcountInfo> {

	@Override
	protected String namesapceForSqlId() {
		// TODO Auto-generated method stub
		return "com.xuanwu.mos.mapper.StatisticsMapper";
	}
	 
	 public int queryCountAccountByEntID(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
			 return sqlsession.selectOne(fullSqlId("queryCountAccountByEntID"), params);
			
		 }
	 }
	 

	 public List<DepartmentStatistics> findDeptRealtimeSend(DepartmentStatistics sumDept){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 HashMap params = new HashMap();
			 params.put("smsType", sumDept.getSmsType());
			 params.put("currentDate", sumDept.getCurrentDate());
			 params.put("path", sumDept.getPath());
			 return sqlsession.selectList(fullSqlId("findDeptRealtimeSend"), params);			
		 }
	 }
	 /**
	  * 部门统计查询历史发送记录以及月统计发送记录
	  * @param params
	  * @return
	  */
	 public List<DepartmentStatistics> findDeptHistorySend(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			
			 return sqlsession.selectList(fullSqlId("findDeptHistorySend"), params);			
		 }
	 }
	 /**
	  * 部门统计查询实时发送记录
	  * @param params
	  * @return
	  */
	 public List<DepartmentStatistics> findDeptRealTimeSend(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectList(fullSqlId("findDeptRealTimeSend"), params);			
		 }
	 }
	 /**
	  * 部门统计查询日统计全量发送记录(包括实时和历史)
	  * @param params
	  * @return
	  */
	 public List<DepartmentStatistics> findDeptAllSend(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectList(fullSqlId("findDeptAllSend"), params);			
		 }
	 }
	 public DepartmentStatistics getSumStatistics(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
			 return sqlsession.selectOne(fullSqlId("getSumStatistics"), params);			
		 }
	 }
	 /**
	  * 首页报表统计栏数据查询
	  * @param params
	  * @return
	  */
	 public List<UserStatistics> findIndexRepot4User(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){			
			 return sqlsession.selectList(fullSqlId("findIndexRepot4User"), params);			
		 }
	 }
	 /**
	  * 用户统计列表条数(历史 数据不包括当天) 
	  * @param param
	  * @return
	  */ 
	 public int findUserHistorySendCount(QueryParameters params){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){

			return sqlsession.selectOne(fullSqlId("findUserHistorySendCount"),params);
		} 
	 }
	 /**
	  * 用户统计数据列表(历史 数据不包括当天) 
	  * @param param
	  * @return
	  */ 
	 public List<UserStatistics> findUserHistorySend(QueryParameters param){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			return sqlsession.selectList(fullSqlId("findUserHistorySend"), param);
		 }
	 }
	 /**
	  * 用户统计列表条数(当天实时数据) 
	  * @param param
	  * @return
	  */
	 public int findUserRealTimeSendCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			return sqlsession.selectOne(fullSqlId("findUserRealTimeSendCount"),params);
		 } 
	 }
	public List<UserStatistics> findUserRealtimeSend(QueryParameters param) {
		try (SqlSession sqlsession = sqlSessionFactory.openSession()){
			return sqlsession.selectList(fullSqlId("findUserRealtimeSend"), param);
		} 
	}
	 /**
	  * 用户全部统计列表条数(包括实时和历史数据) 
	  * @param param
	  * @return
	  */
	 public int findUserAllSendCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			return sqlsession.selectOne(fullSqlId("findUserAllSendCount"),params);
		 } 
	 }
	 public List<UserStatistics> findUserAllSend(QueryParameters param) {
		try (SqlSession sqlsession = sqlSessionFactory.openSession()){
			return sqlsession.selectList(fullSqlId("findUserAllSend"), param);
		} 
	 }
	 /**
	  * 指定用户详情数据条数(历史记录不包括当天实时记录)
	  * @param param
	  * @return
	  */
	 public int findSelectedUserSendCount(QueryParameters param){
		 if(param==null){
			 param = new QueryParameters();
		 }
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
				return sqlsession.selectOne(fullSqlId("findSelectedUserSendCount"),param);
			}  
	 }
	 /**
	  * 指定用户详情数据条数(当天实时记录)
	  * @param param
	  * @return
	  */
	 public int findSelectedUserRealTimeSendCount(QueryParameters param){
		 if(param==null){
			 param = new QueryParameters();
		 }
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
				return sqlsession.selectOne(fullSqlId("findSelectedUserRealTimeSendCount"),param);
			}  
	 }
	 /**
	  * 指定用户详情数据条数(包括历史和实时记录)
	  * @param param
	  * @return
	  */
	 public int findSelectedUserAllSendCount(QueryParameters param){
		 if(param==null){
			 param = new QueryParameters();
		 }
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				
				return sqlsession.selectOne(fullSqlId("findSelectedUserAllSendCount"),param);
			}  
	 }

	 /**
	  * 指定用户详情数据列表(历史记录不包括当天实时记录)
	  * @param param
	  * @return
	  */
	 public  List<UserStatistics> selectedUserStatisticsList(QueryParameters param){

		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				return sqlsession.selectList(fullSqlId("selectedUserStatisticsList"), param);
		 }
	 }
	 /**
	  * 指定用户详情数据列表(当天实时记录)
	  * @param param
	  * @return
	  */
	 public  List<UserStatistics> selectedUserRealTimeList(QueryParameters param){

		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				return sqlsession.selectList(fullSqlId("selectedUserRealTimeList"), param);
		 }
	 }
	 /**
	  * 指定用户详情数据列表(包括历史和实时记录)
	  * @param param
	  * @return
	  */
	 public  List<UserStatistics> selectedUserAllSendList(QueryParameters param){

		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
				return sqlsession.selectList(fullSqlId("selectedUserAllSendList"), param);
		 }
	 }
	 /**
	  * 业务类型统计数据列表(历史数据不包括实时数据)
	  * @param param
	  * @return
	  */ 
	 public List<BizTypeStatistics> bizTypeStatistics(QueryParameters param){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectList(fullSqlId("bizTypeStatisticsList"), param);
		 }
	 }
	 /**
	  * 业务类型统计数据列表(实时数据)
	  * @param param
	  * @return
	  */ 
	 public List<BizTypeStatistics> bizTypeRealTimeStatistics(QueryParameters param){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectList(fullSqlId("bizTypeRealTimeStatistics"), param);
		 }
	 }
	 /**
	  * 业务类型统计数据列表(全部数据包括历史和实时数据)
	  * @param param
	  * @return
	  */ 
	 public List<BizTypeStatistics> bizTypeALLStatistics(QueryParameters param){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectList(fullSqlId("bizTypeALLStatistics"), param);
		 }
	 }

	 /**
	  * 业务类型统计数据总条数
	  * @param param
	  * @return
	  */
	 public int bizTypeStatisticsCount(QueryParameters param){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("bizTypeStatisticsCount"), param);
		 }
	 }
	 /**
	  * 业务类型统计数据总条数(当天实时数据)
	  * @param param
	  * @return
	  */
	 public int bizTypeRealTimeCount(QueryParameters param){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("bizTypeRealTimeCount"), param);
		 }
	 }
	 /**
	  * 业务类型统计数据总条数(包括历史和当天实时数据)
	  * @param param
	  * @return
	  */
	 public int bizTypeAllSendCount(QueryParameters param){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("bizTypeAllSendCount"), param);
		 }
	 }
	 public int getCount4UserStatListByUserId(UserStatistics userStatistics){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("getCount4UserStatListByUserId"), userStatistics);
		 }
	 }
	 /**
	  * 首页统计汇总量12小时(包括总接受量,提交总量,总成功量)
	  * @param param
	  * @return
	  */
	 public UserStatistics getUserSendStatic(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("getUserSendStatic"), params);
		 }
	 }

	 public int getEntpriseDefaultBizType(int entpriseId){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 Map<String, Integer> param = new HashMap<>();
			 param.put("entpriseId", entpriseId);
			 return sqlsession.selectOne(fullSqlId("getEntpriseDefaultBizType"),param);
		 }
	 }
	 public int findDeptHistorySendCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("findDeptHistorySendCount"), params);
		 }
	 }
	 public int findDeptRealTimeSendCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("findDeptRealTimeSendCount"), params);
		 }
	 }
	 public int findDeptAllSendCount(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("findDeptAllSendCount"), params);
		 }
	 }
	 public int deptCount4History(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("deptCount4History"), params);
		 }
	 }
	 public DepartmentStatistics deptStatSUM4History(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("deptStatSUM4History"), params);
		 } 
	 }
	 public DepartmentStatistics deptStatSum4RealTime(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("deptStatSum4RealTime"), params);
		 } 
	 }
	 public BizTypeStatistics getBizStatSumData4History(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("getBizStatSumData4History"), params);
		 } 
	 }
	 public BizTypeStatistics getBizStatSumData4RealTime(QueryParameters params){
		 try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			 return sqlsession.selectOne(fullSqlId("getBizStatSumData4RealTime"), params);
		 } 
	 }
}

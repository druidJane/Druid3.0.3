package com.xuanwu.msggate.common.sbi.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

/**
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-06-08
 * @Version 1.0.0
 */
public class DestBindSpecNum extends AbstractDestBind {
	private Integer srcSpecNumdID;
	
	private Map<Integer, BindSpecNumResult> whiteResultMap;
	private Map<Integer, List<BindSpecNumResult>> changeResultsMap;
	private Map<Integer, Map<Integer,BindSpecNumResult>> regionResultMap;
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public Integer getSrcSpecNumdID() {
		return srcSpecNumdID;
	}

	public void setSrcSpecNumdID(Integer srcSpecNumdID) {
		this.srcSpecNumdID = srcSpecNumdID;
	}

	public BindSpecNumResult getWhiteResult(Integer enterpriseID, MsgSingle msg){
		if(whiteResultMap == null)
			return null;
		lock.readLock().lock();
		try{
			Integer key = tran2Key(enterpriseID, msg.getType(), msg.getCarrier());
			 
			BindSpecNumResult result = whiteResultMap.get(key);
			if (result != null)
				return result;
			if (msg.getType() == MsgType.LONGSMS) {
				key = tran2Key(enterpriseID, MsgType.SMS, msg.getCarrier());
				result = whiteResultMap.get(key);
				if (result != null) {
					msg.setType(MsgType.SMS);
					return result;
				}
			}
			return result;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public BindSpecNumResult getRegionResult(Integer regionID, Integer enterpriseID, MsgSingle msg){
		if(regionResultMap == null)
			return null;
		lock.readLock().lock();
		try{
			Map<Integer,BindSpecNumResult> tempMap = regionResultMap.get(regionID);
			if(tempMap == null)
				return null;
			
			Integer key = tran2Key(enterpriseID, msg.getType(), msg.getCarrier());
			 
			BindSpecNumResult result = tempMap.get(key);
			if (result != null)
				return result;
			if (msg.getType() == MsgType.LONGSMS) {
				key = tran2Key(regionID, MsgType.SMS, msg.getCarrier());
				result = tempMap.get(key);
				if (result != null) {
					msg.setType(MsgType.SMS);
					return result;
				}
			}
			return result;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public List<BindSpecNumResult> getChangeResults(Integer enterpriseID, MsgSingleBox msg){
		if(changeResultsMap == null)
			return Collections.emptyList();
		lock.readLock().lock();
		try{
			Integer key = tran2Key(enterpriseID, msg.getMsgType(), msg.getCarrier());
	
			List<BindSpecNumResult> targets = changeResultsMap.get(key);
			if (targets != null && !targets.isEmpty()) {
				msg.setBypass(targets.get(0).isBypass());
				return targets;
			}
			if (msg.getMsgType() == MsgType.LONGSMS) {
				key = tran2Key(enterpriseID, MsgType.SMS, msg.getCarrier());
				targets = changeResultsMap.get(key);
				if (targets != null && !targets.isEmpty()) {
					msg.setBypass(targets.get(0).isBypass());
					msg.setMsgType(MsgType.SMS);
					return targets;
				}
			}
			return Collections.emptyList();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public void initMap(){
		lock.writeLock().lock();
		try{
			if(whiteDestResult != null && !whiteDestResult.isEmpty()){
				Map<Integer, BindSpecNumResult> tempMap = new HashMap<Integer, BindSpecNumResult>();
				List<BindSpecNumResult> duplitResults = duplitBasicResult(whiteDestResult.getResults());
				for(BindSpecNumResult result : duplitResults){
					Integer key = tran2Key(result.getEnterpriseID(), result.getMsgType(), result.getCarrier());
					tempMap.put(key, result);
				}
				setWhiteRedirect(true);
				whiteResultMap = tempMap;
				whiteDestResult.getResults().clear();
			}
			
			if(regionDestResult != null && !regionDestResult.isEmpty()){
				Map<Integer, Map<Integer,BindSpecNumResult>> tempMap = new HashMap<Integer, Map<Integer,BindSpecNumResult>>();
				List<BindSpecNumResult> duplitResults = duplitBasicResult(regionDestResult.getResults());
				for(BindSpecNumResult result : duplitResults){
					result.setRegionID(result.getRegionID());
					Map<Integer,BindSpecNumResult> temp = tempMap.get(result.getRegionID());
					if(temp == null){
						temp = new HashMap<Integer, BindSpecNumResult>();
						tempMap.put(result.getRegionID(), temp);
					}
					Integer key = tran2Key(result.getEnterpriseID(), result.getMsgType(), result.getCarrier());
					temp.put(key, result);
				}
				setRegionRedirect(true);
				regionResultMap = tempMap;
				regionDestResult.getResults().clear();
			}
			
			if(changeDestResult != null && !changeDestResult.isEmpty()){
				Map<Integer, List<BindSpecNumResult>> tempMap = new HashMap<Integer, List<BindSpecNumResult>>();
				List<BindSpecNumResult> duplitResults = duplitBasicResultByDestBind(changeDestResult);
				for (BindSpecNumResult result : duplitResults) {
					Integer key = tran2Key(result.getEnterpriseID(), result
							.getMsgType(), result.getCarrier());
					List<BindSpecNumResult> temp = tempMap.get(key);
					if (temp == null) {
						temp = new ArrayList<BindSpecNumResult>();
						tempMap.put(key, temp);
					}
					temp.add(result);
				}
				setChangeRedirect(true);
				changeResultsMap = tempMap;
				changeDestResult.clear();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}
}

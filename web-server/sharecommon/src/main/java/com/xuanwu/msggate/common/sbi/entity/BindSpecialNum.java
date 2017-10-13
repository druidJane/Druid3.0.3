/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.core.ConfigDefs;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-12-01
 * @Version 1.0.0
 */
public class BindSpecialNum {

	private List<BindSpecNumResult> results = new ArrayList<BindSpecNumResult>();
	private Map<Integer, BizTypeInfo> bizTypeInfoMap = new HashMap<Integer, BizTypeInfo>();
	private Map<Integer, Map<Long, List<BindSpecNumResult>>> bizTypSpecNumBindMap = new HashMap<Integer, Map<Long, List<BindSpecNumResult>>>();

	private BizTypeInfo userDefaultType = null; // 用户默认业务类型
	private BizTypeInfo entDefaultType = null; // 企业默认业务类型

	public BindSpecialNum() {
	}

	public List<BindSpecNumResult> getResults() {
		return results;
	}

	public void setResults(List<BindSpecNumResult> results) {
		this.results = results;
	}

	public static List<BindSpecNumResult> preHandleBindResult(Collection<BindSpecNumResult> originResults){
		List<BindSpecNumResult> tempResults = preHandleBindResultWithCarrier(originResults);
		List<BindSpecNumResult> targetResults = new ArrayList<BindSpecNumResult>();
		for(BindSpecNumResult tempResult : tempResults){
			for (Integer val : tempResult.getCarrierChannel().getSupportCarriers()) {
				BindSpecNumResult temp = tempResult.duplitBasicResult();
				temp.setCarrier(val);
				targetResults.add(temp);
			}
		}
		return targetResults;
	}
	
	public static List<BindSpecNumResult> preHandleBindResultWithCarrier(Collection<BindSpecNumResult> originResults){
		List<BindSpecNumResult> targetResults = new ArrayList<BindSpecNumResult>();
		for(BindSpecNumResult originResult : originResults){
			if(originResult.getMsgType() == MsgType.LONGSMS){
				if (originResult.getCarrierChannel().isSms()) {
					BindSpecNumResult result = originResult.duplitBasicResult();
					result.setMsgType(MsgType.SMS.getIndex());
					targetResults.add(result);
				}
				if (originResult.getCarrierChannel().isLongSms()) {
					BindSpecNumResult result = originResult.duplitBasicResult();
					result.setMsgType(MsgType.LONGSMS.getIndex());
					targetResults.add(result);
				}
                if (originResult.getCarrierChannel().isVoiceNotice()) {
                    BindSpecNumResult result = originResult.duplitBasicResult();
                    result.setMsgType(MsgType.VOICE_NOTICE.getIndex());
                    targetResults.add(result);
                }
                if (originResult.getCarrierChannel().isVoiceCode()) {
                    BindSpecNumResult result = originResult.duplitBasicResult();
                    result.setMsgType(MsgType.VOICE_CODE.getIndex());
                    targetResults.add(result);
                }
			} else {
				targetResults.add(originResult);
			}
		}
		return targetResults;
	}
	
	public void initBindResult(BizTypeInfo userDefaultType) {
		this.userDefaultType = userDefaultType;
		
		Set<BindSpecNumResult> tempSet = new HashSet<BindSpecNumResult>();
		tempSet.addAll(results);

		for (BindSpecNumResult specResult : tempSet) {
			if (bizTypSpecNumBindMap.get(specResult.getBizType()) == null) {
				Map<Long, List<BindSpecNumResult>> tempMap = new HashMap<Long, List<BindSpecNumResult>>();
				bizTypSpecNumBindMap.put(specResult.getBizType(), tempMap);
			}

			if (bizTypeInfoMap.get(specResult.getBizType()) == null) {
				bizTypeInfoMap.put(specResult.getBizType(), specResult
						.getBizInfo());
			}

			if (specResult.getBizInfo().getType() == ConfigDefs.DEFAULT_BIZ_TYPE) {
				entDefaultType = specResult.getBizInfo();
			}
		}
		results = preHandleBindResultWithCarrier(tempSet);

		for (BindSpecNumResult specResult : results) {
			Map<Long, List<BindSpecNumResult>> tempMap = bizTypSpecNumBindMap
					.get(specResult.getBizType());

			Long key = tran2CarrierKey(specResult.getCarrier(), 
					specResult.getMsgType());
			List<BindSpecNumResult> tempList = tempMap.get(key);
			if (tempList == null) {
				tempList = new ArrayList<BindSpecNumResult>();
				tempMap.put(key, tempList);
			}
			tempList.add(specResult);
		}
	}

	public BizTypeInfo getBizTypeInfo(Integer id) {
		BizTypeInfo bizTypeInfo = bizTypeInfoMap.get(id);
		if(bizTypeInfo == null)
			bizTypeInfo = userDefaultType;
		else 
			return bizTypeInfo;
		
		if(bizTypeInfo == null)
			bizTypeInfo = entDefaultType;
		
		return bizTypeInfo;
	}

	public Map<Long, List<BindSpecNumResult>> getSpecNumBindMap(
			Integer bizType) {
		Map<Long, List<BindSpecNumResult>> tempMap = bizTypSpecNumBindMap.get(bizType);

		if ((tempMap == null || tempMap.isEmpty())
				&& userDefaultType != null)
			tempMap = bizTypSpecNumBindMap.get(userDefaultType.getId());
		 
		if ((tempMap == null || tempMap.isEmpty())
				&& entDefaultType != null)
			tempMap = bizTypSpecNumBindMap.get(entDefaultType.getId());
		
		return tempMap;
	}

	/**
	 * Get region bind special number
	 * 
	 * @return
	 */
	public List<BindSpecNumResult> getRegionSpecNumBind() {
		List<BindSpecNumResult> tmp = new ArrayList<BindSpecNumResult>();
		for (BindSpecNumResult bindSpecNumResult : results) {
			if (bindSpecNumResult.getType() == BindSpecNumType.REGION_BIND)
				tmp.add(bindSpecNumResult);
		}
		return tmp;
	}

	/**
	 * Get the fixed special number
	 * 
	 * @return
	 */
	public List<BindSpecNumResult> getFixedSpecNumBind() {
		List<BindSpecNumResult> tmp = new ArrayList<BindSpecNumResult>();
		for (BindSpecNumResult bindSpecNumResult : results) {
			if (bindSpecNumResult.getType() == BindSpecNumType.FIXED_BIND)
				tmp.add(bindSpecNumResult);
		}
		return tmp;
	}

	/**
	 * Get sytem defined special service number
	 * 
	 * @return
	 */
	public List<BindSpecNumResult> getSysSpecNumBind() {
		List<BindSpecNumResult> tmp = new ArrayList<BindSpecNumResult>();
		for (BindSpecNumResult bindSpecNumResult : results) {
			if (bindSpecNumResult.getType() == BindSpecNumType.SYS_BIND)
				tmp.add(bindSpecNumResult);
		}
		return tmp;
	}
	
	public Long tran2CarrierKey(Carrier carrier, MsgType msgType) {
		return Long.valueOf((carrier.getIndex() << 4)
				| (msgType.getIndex() & 0xF));
	}
}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

/**
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-5
 * @Version 1.0.0
 */
public class RedSpecNum {
	private boolean initMapFlag = false;
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Red specNum result list
	 */
	private List<RedSpecNumResult> results;
	/**
	 * Map result
	 */
	private Map<Long, BindSpecNumResult> resultMap = new HashMap<Long, BindSpecNumResult>();

	public RedSpecNum(List<RedSpecNumResult> results) {
		this.results = results;
	}

	public List<RedSpecNumResult> getResults() {
		return results;
	}

	public void setResults(List<RedSpecNumResult> results) {
		this.results = results;
	}

	/**
	 * Get the red bind special service number by the phone
	 * 
	 * @param phone
	 * @param msgType
	 * @return
	 */
	public BindSpecNumResult getRedBindResult(MsgSingle msg) {
		if (!initMapFlag)
			initMap();
		Long key = tran2Key(msg.getPhone(), msg.getType(), msg.getCarrier());
		BindSpecNumResult result = resultMap.get(key);
		if (result != null)
			return result;
		if (msg.getType() == MsgType.LONGSMS) {
			key = tran2Key(msg.getPhone(), MsgType.SMS, msg.getCarrier());
			result = resultMap.get(key);
			if (result != null) {
				msg.setType(MsgType.SMS);
				return result;
			}
		}
		return result;
	}

	private void initMap() {
		lock.lock();
		try {
			if (!initMapFlag) {
				if (results == null)
					return;

				List<RedSpecNumResult> targetResults = new ArrayList<RedSpecNumResult>();
				for (RedSpecNumResult redResult : results) {
					if (redResult.getMsgType() == MsgType.LONGSMS) {
						if (redResult.getCarrierChannel().isSms()) {
							RedSpecNumResult copyResult = redResult.duplitBasicResult();
							copyResult.setMsgType(MsgType.SMS.getIndex());
							targetResults.add(copyResult);
						}

						if (redResult.getCarrierChannel().isLongSms()) {
							RedSpecNumResult copyResult = redResult.duplitBasicResult();
							copyResult.setMsgType(MsgType.LONGSMS.getIndex());
							targetResults.add(copyResult);
						}
					} else {
						targetResults.add(redResult);
					}
				}
				results = targetResults;
				for (RedSpecNumResult redResult : results) {
					BindSpecNumResult bindResult = new BindSpecNumResult();
					bindResult.setSpecsvsNumID(redResult.getSpecsvsNumID());
					bindResult.setSpecSVSNum(redResult.getSpecSVSNum());
					bindResult.setMsgType(redResult.getMsgType().getIndex());
					bindResult.setPrice(redResult.getPrice());
					
					for (Integer val : bindResult.getCarrierChannel().getSupportCarriers()) {
						BindSpecNumResult temp = bindResult.duplitBasicResult();
						temp.setCarrier(val);
						resultMap.put(tran2Key(redResult.getPhone(), temp.getMsgType(), temp.getCarrier()), temp);
					}
				}
				initMapFlag = true;
			}
		} finally {
			lock.unlock();
		}
	}

	private Long tran2Key(String phone, MsgType msgType, Carrier carrier) {
		return (Long.valueOf(phone) << 8)
			| ((msgType.getIndex() & 0xF) << 4) 
			| (carrier.getIndex() & 0xF);
	}
}

/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-11-3
 * @Version 1.0.0
 */
public class WhiteRedirectSpecNum {
	private boolean initMapFlag = false;
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Bind specNum result list
	 */
	private List<BindSpecNumResult> results;
	/**
	 * Map result
	 */
	private Map<Integer, BindSpecNumResult> resultMap = new HashMap<Integer, BindSpecNumResult>();

	public void setResults(List<BindSpecNumResult> results) {
		this.results = results;
	}

	public List<BindSpecNumResult> getResults() {
		return results;
	}

	public BindSpecNumResult getRedirectResult(MsgSingle msg) {
		if (!initMapFlag)
			initMap();
		Integer key = tran2Key(msg.getType(), msg.getCarrier());
		BindSpecNumResult result = resultMap.get(key);
		if (result != null)
			return result;
		if (msg.getType() == MsgType.LONGSMS) {
			key = tran2Key(MsgType.SMS, msg.getCarrier());
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
				results = BindSpecialNum.preHandleBindResult(results);
				for (BindSpecNumResult result : results) {
					resultMap.put(tran2Key(result.getMsgType(), result.getCarrier()), result);
				}
				initMapFlag = true;
			}
		} finally {
			lock.unlock();
		}
	}

	private Integer tran2Key(MsgType msgType, Carrier carrier) {
		return Integer.valueOf((msgType.getIndex() << 4) 
				| (carrier.getIndex() & 0xF));
	}
}

package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class RegionSpecNum {
	private boolean initMapFlag = false;
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Bind specNum result list
	 */
	private List<BindSpecNumResult> results = new ArrayList<BindSpecNumResult>();
	/**
	 * Map result
	 */
	private Map<Integer, BindSpecNumResult> resultMap = new HashMap<Integer, BindSpecNumResult>();

	public void addResult(BindSpecNumResult result) {
		if (!results.contains(result))
			results.add(result);
	}

	public List<BindSpecNumResult> getResults() {
		return results;
	}

	public BindSpecNumResult getRegionResult(Integer regionID, MsgSingle msg) {
		if (!initMapFlag)
			initMap();
		Integer key = tran2Key(regionID, msg.getType(), msg.getCarrier());
		BindSpecNumResult result = resultMap.get(key);
		if (result != null)
			return result;
		if (msg.getType() == MsgType.LONGSMS) {
			key = tran2Key(regionID, MsgType.SMS, msg.getCarrier());
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
					resultMap.put(tran2Key(result.getRegionID(), result.getMsgType(), result.getCarrier()), result);
				}
				initMapFlag = true;
			}
		} finally {
			lock.unlock();
		}
	}

	private Integer tran2Key(Integer regionID, MsgType msgType, Carrier carrier) {
		return Integer.valueOf((regionID << 8) | ((msgType.getIndex() & 0xF) << 4) | (carrier.getIndex() & 0xF));
	}
}

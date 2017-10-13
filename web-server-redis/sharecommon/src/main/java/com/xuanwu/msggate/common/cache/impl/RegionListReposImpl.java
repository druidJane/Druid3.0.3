/*   
* Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
*             All rights reserved                         
*/
package com.xuanwu.msggate.common.cache.impl;

import com.xuanwu.msggate.common.cache.RegionCarrierResult;
import com.xuanwu.msggate.common.cache.RegionRepos;
import com.xuanwu.msggate.common.cache.SpecSVSRepos;
import com.xuanwu.msggate.common.cache.SyncTask;
import com.xuanwu.msggate.common.cache.dao.CacheDao;
import com.xuanwu.msggate.common.core.Config;
import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.util.PhoneUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:  
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-12-3
 * @Version 1.0.0
 */
public class RegionListReposImpl implements RegionRepos, SyncTask {
	private static final Logger logger = LoggerFactory
			.getLogger(RegionListReposImpl.class);
	/**
	 * Map current id
	 */
	private int regionCarrierVersion = -1;
	
	/**
	 * Region carrier map
	 */
	private Map<String, Long> regionCarrierMap = new HashMap<String, Long>();
	
	/**
	 * Cache dao
	 */
	private CacheDao dao;
	/**
	 * Special service repository
	 */
	private SpecSVSRepos specRepos;
	
	@Override
	public RegionCarrierResult getRegionCarrier(String phone) {
		Long result = regionCarrierMap.get(PhoneUtil.subPhoneRegionCode(phone));
		if (result == null)
			return null;
		return new RegionCarrierResult(specRepos
				.getRegionByID((int) (result >> 32) & 0xFFFFFFFF), Carrier
				.getType(result.intValue()));
	}

	public void init(){
		syncRegionCarrierMap();
	}
	
	@Override
	public void run() {
		try {
			syncRegionCarrierMap();
		} catch (Exception e) {
			logger.error("Sync SpecNum exception ocurred: ", e);
		}
	}
	
	private void syncRegionCarrierMap() {
		logger.info("Begin to sync the region list...");		 
		List<Integer> result = dao.fetchSyncVersion();
	    int newVersion = result.get(2);
	    if(newVersion > regionCarrierVersion){
			regionCarrierVersion = dao.fetRegionCarrierMap(regionCarrierVersion,
					regionCarrierMap);
	    }
		logger.info("End to sync the region list, preVersion=" + regionCarrierVersion);
	}
	
	public void setDao(CacheDao dao) {
		this.dao = dao;
	}
	
	public void setSpecRepos(SpecSVSRepos specRepos) {
		this.specRepos = specRepos;
	}

	@Override
	public long getPeriod() {
		return Config.listSyncDelay;
	}
}

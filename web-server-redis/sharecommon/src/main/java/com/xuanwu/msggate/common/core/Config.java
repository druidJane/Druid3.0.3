/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.core;

import com.xuanwu.msggate.common.cache.SyncTask;
import com.xuanwu.msggate.common.cache.dao.BizDataFetchDao;
import com.xuanwu.msggate.common.cache.entity.ConfigRecord;
import com.xuanwu.msggate.common.sbi.entity.PlatformMode;
import com.xuanwu.msggate.common.util.ListUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-8-3
 * @Version 1.0.0
 */
public class Config implements ConfigDefs, SyncTask {

	private static final Logger logger = LoggerFactory.getLogger(Config.class);
	private static final long ONE_MINUTE = 60 * 1000;

	private int sysConfigVersion = -1;

	/**
	 * Business data fetch dao
	 */
	private BizDataFetchDao dao;

	public static long configSyncDelay = ONE_MINUTE;
	public static long specNumSyncDelay = ONE_MINUTE;
	public static long listSyncDelay = ONE_MINUTE;	
	public static String notAuditKey = "_noAudit";
	
	private int maxPriority;
	private int minPriority;
	private int mixMaxLength;
	private int maxLongSmsLen;
	private int updateLevelTime;
	private String highlightHtmlTagStrat;
	private String highlightHtmlTagEnd;
	private int maxSMSPackSize;
	private int maxMMSMassPackSize;
	private int maxMMSGroupPackSize;
	private int sqlUpdateSuccessVal = 1;
	private String smsFrameWaitTag;
	private String mmsFrameWaitTag;
	private String smsPackWaitTag;
	private String mmsPackWaitTag;
	private boolean shardingTableOn;
	
	private String gatewayIPAddress;
	private int gatewayPort;
	
	private int maxCmdLength = 20;
	
	private PlatformMode gateMode;

	public int getMaxPriority() {
		return maxPriority;
	}

	public int getMinPriority() {
		return minPriority;
	}

	public int getMixMaxLength() {
		return mixMaxLength;
	}

	public int getMaxLongSmsLen() {
		return maxLongSmsLen;
	}

	public int getUpdateLevelTime() {
		return updateLevelTime;
	}

	public String getHighlightHtmlTagStrat() {
		return highlightHtmlTagStrat;
	}
	
	public String getHighlightHtmlTagEnd(){
		return highlightHtmlTagEnd;
	}
	
	public int getMaxMMSMassPackSize() {
		return maxMMSMassPackSize;
	}

	public int getMaxMMSGroupPackSize(){
		return maxMMSGroupPackSize;
	}
	
	public int getMaxSMSPackSize() {
		return maxSMSPackSize;
	}
	
	public boolean isUmpMode(){
		return (gateMode == PlatformMode.UMP);
	}
	
	public boolean isMosMode(){
		return (gateMode == PlatformMode.MOS);
	}
	
	// platformID: 0-Backend,1-UMP,2-Frontkit
	public int getPlatformID(){
		return (gateMode == PlatformMode.MOS) ? 2 : 1;
	}
	
	public String getMmsFrameWaitTag() {
		return mmsFrameWaitTag;
	}
	
	public String getSmsFrameWaitTag() {
		return smsFrameWaitTag;
	}
	
	public String getSmsPackWaitTag() {
		return smsPackWaitTag;
	}
	
	public String getMmsPackWaitTag() {
		return mmsPackWaitTag;
	}
	
	public boolean shardingTableOn() {
		return shardingTableOn;
	}
	
	public String getGatewayIPAddress() {
		return gatewayIPAddress;
	}

	public int getGatewayPort() {
		return gatewayPort;
	}
	
	public int getMaxCmdLength() {
		return maxCmdLength;
	}
	/**
	 * SQL update success value: mysql(1), oracle(-2)....
	 * @return
	 */
	public int getSqlUpdateSuccessVal(){
		return sqlUpdateSuccessVal;
	}

	public void init() {
		initPlatformMode();
		sqlUpdateSuccessVal = dao.getUpdateSuccessVal();
		syncService();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(configSyncDelay);
		} catch (InterruptedException e) {
			logger.error("Sync configuration sleep interrupted: ", e);
		}
		try {
			syncService();
		} catch (Exception e) {
			logger.error("Sync system configuration failed, cause by: ", e);
		}
	}
	
	private void initPlatformMode(){
		gateMode = PlatformMode.getType(dao.fetchPlatformMode());
	}

	private void syncService() {
		List<Integer> vers = dao.fetchAllVersions();
		logger.info("Begin to sync system configuration, current version[{}], new version[{}]...", sysConfigVersion, vers.get(2));
		syncSysConfig(vers.get(2));
		logger.info("End to sync system configuration.");
	}

	private void syncSysConfig(int newVersion) {
		if (!(newVersion > sysConfigVersion))
			return;
		Map<String, String> defsMap = new HashMap<String, String>();
		int platformID = 2;
		List<ConfigRecord> configRecords = dao.fetchAllConfigRecords(platformID);
		if (ListUtil.isBlank(configRecords))
			return;

		for (ConfigRecord configRecord : configRecords) {
			defsMap.put(configRecord.getKey(), configRecord.getValue());
		}

		configSyncDelay = ONE_MINUTE
				* Integer.parseInt(defsMap.get(CONFIG_SYNC_PERIOD));
		configSyncDelay = configSyncDelay < ONE_MINUTE ? ONE_MINUTE
				: configSyncDelay;

		specNumSyncDelay = ONE_MINUTE
				* Integer.parseInt(defsMap.get(CACHE_SPECNUM_SYNC_PERIOD));
		specNumSyncDelay = specNumSyncDelay < ONE_MINUTE ? ONE_MINUTE
				: specNumSyncDelay;

		listSyncDelay = ONE_MINUTE
				* Integer.parseInt(defsMap.get(CACHE_LIST_SYNC_PERIOD));
		listSyncDelay = listSyncDelay < ONE_MINUTE ? ONE_MINUTE : listSyncDelay;

		maxPriority = Integer.parseInt(defsMap.get(MSG_MAX_PRIORITY));
		minPriority = Integer.parseInt(defsMap.get(MSG_MIN_PRIORITY));
		mixMaxLength = Integer.parseInt(defsMap.get(MSG_MIX_MAXLENGTH));
		maxLongSmsLen = Integer.parseInt(defsMap.get(MSG_MAX_LONGSMSLEN));
		updateLevelTime = Integer.parseInt(defsMap.get(UPDATE_LEVEL_TIME));
		highlightHtmlTagStrat = "<" + defsMap.get(HIGHLIGHTHTMLTAG) + ">";
		highlightHtmlTagEnd = "</" + defsMap.get(HIGHLIGHTHTMLTAG) + ">";
		//gateMode = PlatformMode.getType(Integer.parseInt(defsMap.get(PLATFORM_MODE)));
		maxMMSMassPackSize = Integer.parseInt(defsMap.get(MSG_MAX_MMS_MASS_PACK_SIZE));
		maxMMSGroupPackSize = Integer.parseInt(defsMap.get(MSG_MAX_MMS_GROUP_PACK_SIZE));
		maxSMSPackSize = Integer.parseInt(defsMap.get(MAX_SMS_PACK_SIZE));
		smsFrameWaitTag = defsMap.get(SMS_FRAME_WAIT_TAG);
		mmsFrameWaitTag = defsMap.get(MMS_FRAME_WAIT_TAG);
		smsPackWaitTag = defsMap.get(SMS_PACK_WAIT_TAG);
		mmsPackWaitTag = defsMap.get(MMS_PACK_WAIT_TAG);
		shardingTableOn = Integer.parseInt(defsMap.get(SHARDING_TABLE_ON)) > 0;
		gatewayIPAddress = defsMap.get(GATEWAY_IPADDRESS);
		gatewayPort = Integer.parseInt(defsMap.get(GATEWAY_PORT));
		sysConfigVersion = newVersion;
		logger.info("Sync system configuration success, update to version[{}]", sysConfigVersion);
	}
	
	
	
	public void updateConfig(ConfigRecord config){
		config.setPlatformId(getPlatformID());
		dao.updateConfig(config);
	}

	public void setDao(BizDataFetchDao dao) {
		this.dao = dao;
	}

	@Override
	public long getPeriod() {
		return configSyncDelay;
	}
}

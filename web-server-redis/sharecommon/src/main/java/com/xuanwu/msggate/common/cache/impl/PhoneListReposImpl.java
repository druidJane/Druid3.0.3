/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuanwu.msggate.common.cache.BizHandleRepos;
import com.xuanwu.msggate.common.cache.RegionCarrierResult;
import com.xuanwu.msggate.common.cache.SpecSVSRepos;
import com.xuanwu.msggate.common.cache.SyncTask;
import com.xuanwu.msggate.common.cache.dao.CacheDao;
import com.xuanwu.msggate.common.cache.engine.LongHashCache;
import com.xuanwu.msggate.common.cache.engine.PhoneCache;
import com.xuanwu.msggate.common.core.Config;
import com.xuanwu.msggate.common.sbi.entity.Account;
import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.util.KeyFilter;
import com.xuanwu.msggate.common.util.PhoneUtil;
import com.xuanwu.msggate.common.util.KeyFilter.KewordType;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-27
 * @Version 1.0.0
 */
public class PhoneListReposImpl implements BizHandleRepos, SyncTask {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PhoneListReposImpl.class);

	/**
	 * Read wirete lock
	 */
	ReadWriteLock keywordLock = new ReentrantReadWriteLock();

	/**
	 * Current last list id
	 */
	private int phoneListVersion = -1;

	/**
	 * Cache of the phone
	 */
	private PhoneCache cache;

	/**
	 * Current key word last id
	 */

	private int keyWordVersion = -1;
	/**
	 * Key word filter
	 */
	private KeyFilter keyFilter;

	/**
	 * Map current id
	 */
	private int regionCarrierVersion = -1;

	/**
	 * Region carrier map
	 */
	private Map<String, Long> regionCarrierMap = new HashMap<String, Long>();

	/**
	 * initial capacity of the cache
	 */
	private int capacity = 30000000;
	/**
	 * Load factory
	 */
	private float loadFactory = 0.75f;

	/**
	 * Cache dao
	 */
	private CacheDao dao;

	/**
	 * Special service repository
	 */
	private SpecSVSRepos specRepos;

	private Config config;
	
	/** 是否加载号码列表，如黑名单、白名单 */
	private boolean loadPhoneList = true;
	
	/** 是否加载关键字 */
	private boolean loadKeyword = true;
	
	/** 是否加载区域运营商 */
	private boolean loadRegionMap = true;

	/**
	 * Default constructor
	 */
	public PhoneListReposImpl() {
	}

	public void initList() {
		cache = new LongHashCache(capacity, loadFactory);
		logger.info("Begin to sync the phone list...");
		clearSyncEffectTime();
		syncPhoneList();
		syncKeyWord();
		syncRegionCarrierMap();
		logger.info("End to sync the phone list.");
	}

	private void syncRegionCarrierMap() {
		if (!loadRegionMap) {
			return;
		}
		int preVersion = regionCarrierVersion;
		regionCarrierVersion = dao.fetRegionCarrierMap(regionCarrierVersion,
				regionCarrierMap);
		if(regionCarrierVersion > 0)
			dao.insertRegionCarrierMapEffectTime(preVersion, regionCarrierVersion);
	}

	@Override
	public void run() {
			logger.info("Begin to sync the phone list...");
			int result = 0;
			try{
				result = checkUpdateVersion(phoneListVersion, keyWordVersion,
						regionCarrierVersion);
			}catch (Exception e) {
				logger.error("Sync phone list checkUpdateVersion error, cause by: ", e);
			}
			if (result != 0) {
				try {
					if ((result & PHONE_LIST) != 0)
						syncPhoneList();
					if ((result & KEY_WORD) != 0)
						syncKeyWord();
					if ((result & REGION_CARRIER_MAP) != 0)
						syncRegionCarrierMap();
				} catch (Exception e) {
					logger.error("Sync phone list failed, cause by: ", e);
				}
			}
			logger.info("End to sync the phone list.");
	}

	private int checkUpdateVersion(int phoneListVersion2, int keyWordVersion2,
			int regionCarrierVersion2) {
		List<Integer> result = dao.fetchSyncVersion();
		int version = 0;
		if (phoneListVersion2 < result.get(0))
			version = version | PHONE_LIST;
		if (keyWordVersion2 < result.get(1))
			version = version | KEY_WORD;
		if (regionCarrierVersion2 < result.get(2))
			version = version | REGION_CARRIER_MAP;
		
		logger.info("phoneList[previous: " + phoneListVersion2 + ",current:" + result.get(0)
				+ "],keyword[previous: " + keyWordVersion2 + ",current:" + result.get(1)
				+ "],regionCarrier[previous: " + regionCarrierVersion2 + ",current:" + result.get(2)+"]");
		
		return version;
	}

	/**
	 * Synchronized the phone list
	 */
	public void syncPhoneList() {
		if (!loadPhoneList) {
			return;
		}
		int preVersion = phoneListVersion;
		phoneListVersion = dao.fetchPhoneList(phoneListVersion, cache);
		if (phoneListVersion > 0) {
			dao.insertPhoneListEffectTime(preVersion, phoneListVersion);
		}
	}

	/**
	 * Synchronized the key word
	 */
	public void syncKeyWord() {
		if (!loadKeyword) {
			return;
		}
		keywordLock.writeLock().lock();
		try{
			int preVersion = keyWordVersion;
			keyWordVersion = dao.fetchKeyWord(keyWordVersion, keyFilter);
			if (keyWordVersion <= 0) 
				return;
			
			if (preVersion < 0)
				dao.insertKeywordEffectTime(keyWordVersion);
			else
				dao.updateKeywordEffectTime(keyWordVersion);
		} finally {
			keywordLock.writeLock().unlock();
		}
	}

	/**
	 * Set capacity
	 * 
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Set loadFactory
	 * 
	 * @param loadFactory
	 *            the loadFactory to set
	 */
	public void setLoadFactory(float loadFactory) {
		this.loadFactory = loadFactory;
	}

	/**
	 * Set dao
	 * 
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(CacheDao dao) {
		this.dao = dao;
	}

	@Override
	public boolean isInBlackList(int bizType, String phone, Account account) {
		// The global blacklist and backend enterprise blacklist filter
		if (config.isMosMode()) {
			if (cache.containPhonePara(phone, ListType.GBLACK.getIndex(), 0)
					|| cache.containPhonePara(phone,ListType.ENT_BACKEND_BLACK.getIndex(), account.getParent().getId()))
				return true;
		}

		// The enterprise blacklist filter if it's enabled
		if (account.isEnableBlackListFilter()) {
			return (cache.containPhonePara(phone, ListType.ENT_BLACK.getIndex(), account.getParent().getId())
					|| cache.containPhonePara(phone, ListType.BLACK.getIndex(), account.getId())
					|| cache.containPhonePara(phone, ListType.BIZTYPE_BLACK.getIndex(), bizType));
		}
		return false;
	}
	
	@Override
	public boolean isInBlackList(String phone, CarrierChannel carrierChannel){
		return cache.containPhonePara(phone, ListType.CHANNEL_BLACK.getIndex(),
				carrierChannel.getId());
	}

	@Override
	public boolean isInWhiteList(String phone, CarrierChannel carrierChannel) {
		return cache.containPhonePara(phone, ListType.WHITE.getIndex(),
				carrierChannel.getId());
	}

	@Override
	public boolean isLegalKey(MsgContent msgContent, Account account) {
		keywordLock.readLock().lock();
		try {
			// The global keyword filter
			if (config.isMosMode()
					&& !keyFilter.isLegal(msgContent, KewordType.GBKEYWORD
							.getIndex(), 0))
				return false;
			// The enterprise keyword filter if it's enabled
			if (account.isEnableKeywordFilter()) {
				return keyFilter.isLegal(msgContent, KewordType.ENT_KEYWORD
						.getIndex(), account.getParent().getId());
			}
			return true;
		} finally {
			keywordLock.readLock().unlock();
		}
	}
	
	@Override
	public boolean isLegalKey(MsgContent msgContent, CarrierChannel carrierChannel){
		keywordLock.readLock().lock();
		try {
			return keyFilter.isLegal(msgContent, KewordType.CHANNEL_KEYWORD
						.getIndex(), carrierChannel.getId());
		} finally {
			keywordLock.readLock().unlock();
		}
	}

	@Override
	public RegionCarrierResult getRegionCarrier(String phone) {
		Long result = regionCarrierMap.get(PhoneUtil.subPhoneRegionCode(phone));
		if (result == null)
			return null;
		return new RegionCarrierResult(specRepos
				.getRegionByID((int) (result >> 32) & 0xFFFFFFFF), Carrier
				.getType(result.intValue()));
	}

	protected Long getRegionCarrierMeta(String phone) {
		return regionCarrierMap.get(phone);
	}
	
	/**
	 * Clear cachedata effect time
	 */
	private void clearSyncEffectTime() {
		dao.clearSyncEffectTime();
	}
	

	@Autowired
	public void setKeyFilter(KeyFilter keyFilter) {
		this.keyFilter = keyFilter;
	}

	@Autowired
	public void setSpecRepos(SpecSVSRepos specRepos) {
		this.specRepos = specRepos;
	}

	@Autowired
	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * 添加黑名单
	 * 
	 * @param phones
	 * @param target
	 * @param userName
	 */
	@Override
	public void addBlackLists(String[] phones, int target, String userName) {
		ListType listType = ListType.BLACK;

		// -1 表示全局黑名单
		if (target == -1) {
			target = 0;
			listType = ListType.GBLACK;
		}

		insertPhones(phones, listType, target, userName);
	}

	private void insertPhones(String[] phones, ListType listType, int target,
			String userName) {
		dao.insertPhones(phones, listType, target, userName, false, null,
				cache);
	}

	/**
	 * 批量删除黑名单
	 * 
	 * @param phones
	 * @param target
	 * @param userName
	 */
	@Override
	public void deleteBlackLists(String[] phones, int target, String userName) {
		ListType listType = ListType.BLACK;

		// -1 表示全局黑名单
		if (target == -1) {
			target = 0;
			listType = ListType.GBLACK;
		}

		removePhones(phones, listType, target, userName);
	}

	/**
	 * 批量添加白名单
	 */
	@Override
	public void addWhiteLists(String[] phones, int specSVSNumberID,
			String userName) {
		insertPhones(phones, ListType.WHITE, specSVSNumberID, userName);
	}

	/**
	 * 批量删除白名单
	 */
	@Override
	public void deleteWhiteLists(String[] phones, int specSVSNumberID,
			String userName) {
		removePhones(phones, ListType.WHITE, specSVSNumberID, userName);
	}

	private void removePhones(String[] phones, ListType listType, int target,
			String userName) {
		dao.removePhones(phones, listType, target, userName, cache);
	}

	/**
	 * 批量删除关键字
	 */
	@Override
	public void removeKeywords(String[] keywords, String user) {
		dao.removeKeywords(keywords, user, keyWordVersion, keyFilter);
	}

	/**
	 * 批量添加关键字
	 */
	@Override
	public void insertKeywords(String[] keywords, String user) {
		dao.insertKeywords(keywords, user, keyWordVersion, keyFilter);
	}

	@Override
	public long getPeriod() {
		return Config.listSyncDelay;
	}
	
	public void setLoadKeyword(boolean loadKeyword) {
		this.loadKeyword = loadKeyword;
	}
	
	public void setLoadPhoneList(boolean loadPhoneList) {
		this.loadPhoneList = loadPhoneList;
	}
	
	public void setLoadRegionMap(boolean loadRegionMap) {
		this.loadRegionMap = loadRegionMap;
	}
}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache.dao;

import com.xuanwu.msggate.common.cache.BizHandleRepos.ListType;
import com.xuanwu.msggate.common.cache.engine.PhoneCache;
import com.xuanwu.msggate.common.util.KeyFilter;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-27
 * @Version 1.0.0
 */
public interface CacheDao {

    /**
     * Fetch the phone list
     * 
     * @param currentLastID
     * @return TODO
     */
    public int fetchPhoneList(int currentLastID, PhoneCache cache);

    /**
     * Fetch the key word
     * 
     * @param currentKeyID
     * @param keyFilter
     * @return
     */
    public int fetchKeyWord(int currentKeyID, KeyFilter keyFilter);

    /**
     * Fetch the region carrier map
     * 
     * @param currentMapID
     * @param regionCarrierMap
     * @return
     */
    public int fetRegionCarrierMap(int currentMapID,
            Map<String, Long> regionCarrierMap);

    /**
     * Fetch the cache target version<br/>
     * <b>Attention the sequence of the result</b>
     * 
     * @return
     */
    public List<Integer> fetchSyncVersion();
    
//	[mos2.0-115][Leason][2012/04/12][start]    
    /**
     * insert current patch RegionCarrierMap updated effect time
     * @param minId current patch first RegionCarrierMap id
     * @param maxId current patch last RegionCarrierMap id
     */
	public void insertRegionCarrierMapEffectTime(long minId, long maxId);
	
	
	/**
	 * insert current patch PhoneList updated effect time
	 * @param minId
	 * @param maxId
	 */
	public void insertPhoneListEffectTime(long minId, long maxId);
	
	/**
	 * insert all Keyword updated effect time
	 * @param maxId
	 */
	public	void insertKeywordEffectTime(long maxId);
	
	
	/**
	 * update all Keyword updated effect time
	 * @param maxId
	 */
	public void updateKeywordEffectTime(long maxId);
	
	/**
	 * clear cache Date effectTime
	 */
	public void clearSyncEffectTime();
//	[mos2.0-115][Leason][2012/04/12][end]
    
    /**
     * 批量添加黑名单/白名单
     * @param phones
     * @param listType
     * @param target
     * @param user
     * @param removed
     * @param removeID
     * @param cache
     */
    public void insertPhones(String[] phones, ListType listType, Integer target, String user, Boolean removed, Long removeID, PhoneCache cache);
    
    /**
     * 批量删除黑名单/白名单
     * @param phones
     * @param listType
     * @param target
     * @param user
     * @param cache
     */
    public void removePhones(String[] phones, ListType listType, Integer target, String user, PhoneCache cache);
    
    /**
     * 批量删除关键字
     * @param keywords
     * @param user
     * @param keyWordVersion
     * @param keyFilter
     */
    public void removeKeywords(String[] keywords, String user, int keyWordVersion, KeyFilter keyFilter);
    
    /**
     * 批量添加关键字
     * @param keywords
     * @param user
     * @param keyWordVersion
     * @param keyFilter
     */
    public void insertKeywords(String[] keywords, String user, int keyWordVersion, KeyFilter keyFilter);
}

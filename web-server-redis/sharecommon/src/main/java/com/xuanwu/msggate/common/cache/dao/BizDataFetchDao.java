/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.cache.dao;

import com.xuanwu.msggate.common.cache.entity.ConfigRecord;
import com.xuanwu.msggate.common.cache.entity.PhoneSegment;
import com.xuanwu.msggate.common.cache.entity.Priority;
import com.xuanwu.msggate.common.sbi.entity.Account;
import com.xuanwu.msggate.common.sbi.entity.BizTypeInfo;
import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.DestBindResult;
import com.xuanwu.msggate.common.sbi.entity.EnterpriseAccessRecord;
import com.xuanwu.msggate.common.sbi.entity.EnterpriseBind;
import com.xuanwu.msggate.common.sbi.entity.Phrase;
import com.xuanwu.msggate.common.sbi.entity.Region;
import com.xuanwu.msggate.common.sbi.entity.RegionCarrier;
import com.xuanwu.msggate.common.sbi.entity.RegionRedirect;
import com.xuanwu.msggate.common.sbi.entity.SpecSVSNumber;
import com.xuanwu.msggate.common.sbi.entity.UserBind;
import com.xuanwu.msggate.common.sbi.entity.WhiteRedirectSpecNum;

import java.util.List;

/**
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-3
 * @Version 1.0.0
 */
public interface BizDataFetchDao {

	/**
	 * Fetch all the special service numbers
	 *
	 * @return
	 */
	List<SpecSVSNumber> fetchAllSpecSVSNums();
	
	/**
	 * Fetch all the region redirect
	 * @return
	 */
	List<RegionRedirect> fetchAllRegionRedirects();

	/**
	 * Fetch all the channels
	 *
	 * @return
	 */
	List<CarrierChannel> fetchAllChannels();

	/**
	 * Fetch all the region carriers
	 *
	 * @return
	 */
	List<RegionCarrier> fetchAllRegionCarriers();

	/**
	 * Fetch all the regions
	 *
	 * @return
	 */
	List<Region> fetchAllRegions();
	
	/**
	 * Fetch all the BizTypeInfo
	 * @return
	 */
    List<BizTypeInfo> fetchAllBizTypes();
    
    /**
     * Fetch all the SmsPhrase
     * @return
     */
    List<Phrase> fetchAllSmsPhrase();
	
	/**
     * Fetch all the global priority
     * @return
     */
    List<Priority> fetchAllPrioritys();
    
    /**
     * Fetch all the telephone segment
     * @return
     */
    List<PhoneSegment> fetchAllPhoneSegments();
    
    /**
     * Fetch all the system config record
     * @return
     */
    List<ConfigRecord> fetchAllConfigRecords(int platformID);

	/**
	 * Fetch all bind enterprise
	 *
	 * @return
	 */
	List<EnterpriseBind> fetchAllBindEnterprise();

	/**
	 * Fetch all bind user
	 * @return
	 */
	List<UserBind> fetchAllBindUser();
	
	/**
	 * Fetch version of the special service number
	 *
	 * @return
	 */
	int fetchVersion();
	
	int fetchPriorityVersion();
	
	int fetchPlatformMode();
	
	/**
	 * Fetch all the versions
	 * @return
	 */
	List<Integer> fetchAllVersions();
	
	List<DestBindResult> fetchAllSpecNumRedirect();
	
	List<DestBindResult> fetchAllChannelRedirect();
	
	List<EnterpriseAccessRecord> fetchAllEntAccessRecord();
	
	WhiteRedirectSpecNum fetchGlobalWhiteRedirect();
	
	int getUpdateSuccessVal();
	
	int updateConfig(ConfigRecord config);
	
	List<Account> fetchAllAuditAccounts();
}

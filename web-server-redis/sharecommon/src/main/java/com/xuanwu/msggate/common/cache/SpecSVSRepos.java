/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache;

import java.util.List;
import java.util.Map;

import com.xuanwu.msggate.common.sbi.entity.Account;
import com.xuanwu.msggate.common.sbi.entity.BindSpecNumResult;
import com.xuanwu.msggate.common.sbi.entity.BizTypeInfo;
import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.EnterpriseAccessRecord;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.Region;
import com.xuanwu.msggate.common.sbi.entity.SpecSVSNumber;
import com.xuanwu.msggate.common.sbi.entity.UserBind;

/**
 * Special service number repository interface
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-7-23
 * @Version 1.0.0
 */
public interface SpecSVSRepos {
    /**
     * Get the special service number
     * 
     * @param id
     * @return
     */
    public SpecSVSNumber getSpecNumberByID(Integer id);

    /**
     * Get the Region by the id
     * 
     * @param id
     * @return
     */
    public Region getRegionByID(Integer id);
    
    /**
     * Get the BizTypeInfo by the id
     * @param id
     * @return
     */
    public BizTypeInfo getBizType(Integer id);

    /**
     * Get the tree version
     * 
     * @return
     */
    public int getVersion();

    /**
     * Synchronized the special service number
     */
    public void syncSpecNumTree();

    /**
     * Get the root special service number
     * 
     * @return
     */
    public List<SpecSVSNumber> getRootSpecNums();
    
    /**
     * Get the virtual root special service number
     * @return
     */
    public List<SpecSVSNumber> getVirtualRootSpecNums();

    /**
     * Get Channel by identity
     * @param identity TODO
     * 
     * @return
     */
    public CarrierChannel getChannelByIdentity(String identity);
    
    public CarrierChannel getChannelById(Integer channelId);

	public Map<String, CarrierChannel> getChannelInfo();
	
	public Carrier getCarrierType(String phone);
	
	/**
	 * Get user bind identify
	 * @param identify enterprise identify + user identify
	 * @return
	 */
	public UserBind getUserByIdentify(String identify);
	
	public String getUserById(Integer userID);
	
	public SpecSVSNumber getSpecNumByNumer(Carrier carrier, String specNumber);
	
	public Map<String, UserBind> getMoMatchResultMap();
	
	public Map<String, EnterpriseAccessRecord> getEnterpriseAccessMap();
	
	public BindSpecNumResult getGlobalWhiteRedirect(MsgSingle msg);
	
	public int getAdminID(int entID);	
	
	public int getActualAdminID(int entID);
	
	public Account getAuditAccount(Integer id);
	
	public void syncAccountBizData(Account account);
}

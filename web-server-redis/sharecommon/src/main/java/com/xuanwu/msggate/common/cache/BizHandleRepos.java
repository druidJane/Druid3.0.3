/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache;

import com.xuanwu.msggate.common.sbi.entity.Account;
import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;

/**
 * Biz data handle repository
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-28
 * @Version 1.0.0
 */
public interface BizHandleRepos {
    /**
     * Phone List
     */
    public final static int PHONE_LIST = 1;

    /**
     * Key Word List
     */
    public final static int KEY_WORD = 2;

    /**
     * Region Carrier Map
     */
    public final static int REGION_CARRIER_MAP = 4;

    /**
     * 名单类别
     */
    public enum ListType {
    	/*0--用户黑名单,1--白名单,2--企业黑名单,3--全局黑名单,4--通道黑名单,5--业务类型黑名单,6--企业后台黑名单*/
        BLACK(0), WHITE(1), ENT_BLACK(2), GBLACK(3), CHANNEL_BLACK(4), BIZTYPE_BLACK(5), ENT_BACKEND_BLACK(6);
        private final int index;

        private ListType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static ListType getType(int index) {
            switch (index) {
            case 0:
                return BLACK;
            case 1:
                return WHITE;
            case 2:
                return ENT_BLACK;
            case 3:
                return GBLACK;
            case 4:
            	return CHANNEL_BLACK;
            case 5:
            	return BIZTYPE_BLACK;
            case 6:
            	return ENT_BACKEND_BLACK;
            default:
                return BLACK;
            }
        }
    }

    /**
     * 是否在黑名单中，包括全局黑名单,企业黑名单，用户黑名单,业务类型黑名单
     * @param bizType 
     * @param phone
     * @param account
     * @return
     */
    public boolean isInBlackList(int bizType, String phone, Account account);
    
    /**
     * 是否存在于通道黑名单中
     * @param phone
     * @param carrierChannel
     * @return
     */
    public boolean isInBlackList(String phone, CarrierChannel carrierChannel);

    /**
     * 是否存在于白名单之中
     * 
     * @param phone
     * @param specNumber
     *            根特服号码
     * @return
     */
    public boolean isInWhiteList(String phone, CarrierChannel carrierChannel);

    /**
     * 是否为合法的短信，即无非法关键字
     * 
     * @param content
     * @param account
     * @return
     */
    public boolean isLegalKey(MsgContent msgContent, Account account);
    
    public boolean isLegalKey(MsgContent msgContent, CarrierChannel carrierChannel);

    /**
     * 通过地区代码获取地区运营商对象
     * 
     * @param phone
     * @return
     */
    public RegionCarrierResult getRegionCarrier(String phone);
    
    /**
     * 添加黑名单
     * @param phones
     * @param target
     * @param userName
     */
    public void addBlackLists(String[] phones, int target, String userName);
    
    /**
     * 批量删除黑名单
     * @param phones
     * @param target
     * @param userName
     */
    public void deleteBlackLists(String[] phones, int target, String userName);
    
    /**
     * 批量添加白名单
     * @param phones
     * @param specSVSNumberID
     * @param userName
     */
    public void addWhiteLists(String[] phones, int specSVSNumberID, String userName);
    
    /**
     * 批量删除白名单
     * @param phones
     * @param specSVSNumberID
     * @param userName
     */
    public void deleteWhiteLists(String[] phones, int specSVSNumberID, String userName);
    
    /**
     * 批量删除关键字
     * @param keywords
     * @param user
     */
    public void removeKeywords(String[] keywords, String user);
    
    /**
     * 批量添加关键字
     * @param keywords
     * @param user
     */
    public void insertKeywords(String[] keywords, String user);
    
    
//    [RD20-65][Leason][2010/12/29][Synchronized implement update phone list and keyword list][start]
    /**
     * Synchronized phone list to cache
     * @author Leason
     */
    public void syncPhoneList();
    
    /**
     * Synchronized keyword to cache
     * @author Leason
     */
    public void syncKeyWord();
//    [RD20-65][Leason][2010/12/29][Synchronized implement update phone list and keyword list][end]
}

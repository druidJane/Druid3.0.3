/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.core;

/**
 * Definition of config property strings.
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-8-3
 * @Version 1.0.0
 */
public interface ConfigDefs {

	public static final String USER_SIGN = "UserSign";
	public static final String USER_SIGN_LOCATION = "UserSignLocation";
	public static final String ENT_SIGN = "EntSign";
	public static final String ENT_IDENTIFY = "EntIdentify";
	public static final String ENT_SIGN_LOCATION = "EntSignLocation";
	public static final String CHANNEL_SIGN = "ChannelSign";
	public static final String SPEC_NUM_EXT_SIZE = "ExtendSize";
	
	public static final String IS_ERASE_SIGN = "IsEraseSign";
	public static final String IS_NEED_ENT_SIGN = "IsNeedEntSign";
	public static final String IS_EXTEND_ENABLE = "IsExtend";
	
	public static final String MSG_MIN_PRIORITY = "MinPriority";
	public static final String MSG_MAX_PRIORITY = "MaxPriority";
	public static final String MSG_MIX_MAXLENGTH = "MixMaxLength";
	public static final String MSG_MAX_LONGSMSLEN = "MaxLongSmsLen";
	public static final String MSG_MAX_MMS_MASS_PACK_SIZE = "MaxMMSMassPackSize";
	public static final String MSG_MAX_MMS_GROUP_PACK_SIZE = "MaxMMSGroupPackSize";
	public static final String MAX_SMS_PACK_SIZE = "MaxSMSPackSize";
	
	public static final String UPDATE_LEVEL_TIME = "UpdateLevelTime";
	
	public static final String CACHE_LIST_SYNC_PERIOD = "ListSyncTimePeriod";
	public static final String CACHE_SPECNUM_SYNC_PERIOD = "GsmsSyncTimePeriod";
	
	/* 系统配置信息同步周期 */
	public static final String CONFIG_SYNC_PERIOD = "SettingsCheckingInterval";
	
	public static final String HIGHLIGHTHTMLTAG = "HighlightHtmlTag";
	
	/** 表切分配置项  */
	public static final String SMS_FRAME_WAIT_TAG = "SmsFrameWaitTag";
	
	public static final String MMS_FRAME_WAIT_TAG = "MmsFrameWaitTag";
	
	public static final String SMS_PACK_WAIT_TAG = "SmsPackWaitTag";
	
	public static final String MMS_PACK_WAIT_TAG = "MmsPackWaitTag";
	
	public static final String SHARDING_TABLE_ON = "ShardingTableOn";
	
	public static final String GATEWAY_IPADDRESS = "GatewayIPAddress";
	public static final String GATEWAY_PORT = "GatewayPort";
	
	public static final String ADD_BLACKLIST_CMD = "AddBlacklistCmd";
	public static final String REMOVE_BLACKLIST_CMD = "RemoveBlacklistCmd";
	
	/* 默认的业务类型 */
	public static final int DEFAULT_BIZ_TYPE = 0;
	
	public static final int SMS_HEAD_LENGTH = 6;
	
	public static final int SMS_HEAD_LONG_LENGTH = 8;
	
	public static final String ENCODING_UTF_16BE = "UTF-16BE";
	
	public static final String KEY_GENERATOR = "keygenerator";
	public static final String KEY_FRAME_BUILDER = "framebuilder";
	
	public static final String KEY_CUR_MAX_RESP_ID = "maxRespID";
	public static final String KEY_CUR_MAX_REPORT_ID = "maxReportID";
	
	public static final String ZH_CN_CODE = "86";
	public static final String ZH_CN_CODE_PLUS = "+86";
	
	public static final int LONG_SMS_HEAD_LENGTH = 6;
	public static final int TELECOM_SMS_MAX_LENGTH = 90;
	public static final String ZERO_HEAD = "0";
	
	public static final String KEY_FRAME_USER_NAME = "_user_";
}

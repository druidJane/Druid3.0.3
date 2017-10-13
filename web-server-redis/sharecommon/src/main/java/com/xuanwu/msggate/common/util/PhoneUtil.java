package com.xuanwu.msggate.common.util;

import org.apache.commons.lang.StringUtils;

import com.xuanwu.msggate.common.core.ConfigDefs;

/**
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-2-29
 * @Version 2.0
 */
public class PhoneUtil {
	
	/**
	 * 移除+86、86前缀
	 * 
	 * @param phone
	 * @return
	 */
	public static String removeZhCnCode(String phone) {
		if (StringUtils.isBlank(phone))
			return "";
	
		if (phone.startsWith(ConfigDefs.ZH_CN_CODE))
			return phone.substring(ConfigDefs.ZH_CN_CODE.length());
	
		if (phone.startsWith(ConfigDefs.ZH_CN_CODE_PLUS))
			return phone.substring(ConfigDefs.ZH_CN_CODE_PLUS.length());

		return phone;
	}
	
	/**
	 * 移除号码首位的 0
	 * 
	 * @param phone
	 * @return
	 */
	public static String removeZeroHead(String phone) {
		if (StringUtils.isBlank(phone))
			return "";
		
		if (phone.startsWith(ConfigDefs.ZERO_HEAD) && phone.length() == 12)
			return phone.substring(1);
		
		return phone;
	}
	
	public static String subPhoneRegionCode(String phone){
		if (StringUtils.isBlank(phone))
			return "";
		if (phone.startsWith("01") || phone.startsWith("02"))
			return phone.substring(0, 3);
	    else if (phone.startsWith("0"))
			return phone.substring(0, 4);
		else
			return phone.substring(0, 7);
	}
}

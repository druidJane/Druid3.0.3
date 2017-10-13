package com.xuanwu.mos.utils;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.User;
import com.xuanwu.mos.domain.enums.UserAccountType;
import com.xuanwu.mos.domain.enums.UserProtocolType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 用户导入特殊字段转换类
 * @Data 2017-5-19
 * @Version 1.0.0
 */
public class ConverterUser {

	private static final Logger logger = LoggerFactory.getLogger(ConverterUser.class);

	public static UserAccountType convertAccountType(String name) {
		switch (name) {
			case "web":
				return UserAccountType.WEB;
			case "接口":
				return UserAccountType.INTERFACE;
			case "透传":
				return UserAccountType.OSPF;
			default: return null;
		}
	}

	public static int convertSigLocation(String sigLocation) {
		if (sigLocation.equals("")) {
			return 0;
		}
		sigLocation = StringUtils.trim(sigLocation);
		int location = -1;
		try {
			location = Integer.valueOf(sigLocation);
		} catch (NumberFormatException e) {
			if ("前置".equals(sigLocation)) {
				location = 1;
			} else if ("后置".equals(sigLocation)) {
				location = 0;
			}
		}
		return location;
	}

	public static UserProtocolType convertProtocolType(String name) {
		switch (name) {
			case "cmpp2.0":
				return UserProtocolType.CMPP2;
			case "cmpp3.0":
				return UserProtocolType.CMPP3;
			case "sgip1.2":
				return UserProtocolType.SGIP;
			case "smgp3.0":
				return UserProtocolType.SMGP;
			default: return null;
		}
	}

	public static Integer convertCusSignature(String cusSignature) {
		if ("是".equals(cusSignature)) {
			return 1;
		} else if ("否".equals(cusSignature)) {
			return 0;
		}
		return -1;
	}

	public static int convertSendSpeed(String sendSpeed) {
		int speed = -1;
		try {
			speed = Integer.valueOf(StringUtils.trim(sendSpeed));
		} catch (NumberFormatException e) {}
		return speed;
	}

	public static int convertLinkNum(String linkNum) {
		if ("".equals(linkNum)) {
			return 1;
		}
		int num = -1;
		try {
			num = Integer.valueOf(StringUtils.trim(linkNum));
		} catch (NumberFormatException e) {}
		return num;
	}

	public static void encryptPassword(User user, Config config) {
		String encryptPwd = null;
		//发送密码
		if (StringUtils.isNotBlank(user.getPassword())) {
			encryptPwd = Md5Utils.mixLoginPasswd(user.getPassword(), config.getMixKey());
			user.setPassword(encryptPwd);
		}
		//登录密码
		if (StringUtils.isNotBlank(user.getSecondPassword())) {
			encryptPwd = Md5Utils.mixLoginPasswd(user.getSecondPassword(), config.getMixKey());
			user.setSecondPassword(encryptPwd);
		}
		//透传密码
		if (StringUtils.isNotBlank(user.getMidPassword())) {
			try {
				encryptPwd = AuthSecurityUtil.encrypt(user.getMidPassword());
				user.setMidPassword(encryptPwd);
			} catch (Exception e) {
				logger.error("Encrypt user password failed, cause is:{}", e);
			}
		}
	}

	/* 上行推送--仅支持“启用”/“停用”， 为其他值或为空时默认都为：停用 by jiangziyuan*/
	public static Integer convertUpPush(String upPush) {
		if ("启用".equals(upPush)) {
			return 1;
		} else {
			return 0;
		}
	}
}

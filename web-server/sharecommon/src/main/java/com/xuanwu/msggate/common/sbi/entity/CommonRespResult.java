/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.protobuf.CommonItem.Result;

import java.util.regex.Pattern;

/**
 * 下行信息提交响应
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-1-14
 * @Version 1.0.0
 */
public class CommonRespResult {
	
	public static final String RESP_SUCCESS = "成功";
	public static final String RESP_OTHER_ERROR = "其它错误";
	public static final String RESP_NO_SAVE_ACCOUNT = "余额不足";
	public static final String RESP_INNER_ERROR = "系统内部错误";
	public static final String RESP_INVALID_PARAM = "参数无效";
	public static final String RESP_INVALID_ACCOUNT = "用户无效";
	public static final String RESP_NOT_ALLOW_SEND = "账号没有发送权限";
	public static final String RESP_TOO_LARGE_PACK = "包号码数量超过最大限制";
	public static final String RESP_CAPITAL_ACCOUNT_NOT_EXIST = "资金账户不存在";
	public static final String RESP_ACCOUNT_NOT_EXIST_OR_WRONG_PIN = "用户名密码错误";
	public static final String RESP_UNAUTHORIZED = "登录网关失败";
	public static final String RESP_UN_IDENTIFIED_REQUEST = "不可识别的请求";
	public static final String RESP_NOT_BIND_SPEC_NUM = "请检查账号是否绑定了发送端口";

	private static final String SEPARATOR = "{xw}";
	private static final String MSG_TAG = ": ";

	private Result result;
	private String message;

	public CommonRespResult(Result result, String message) {
		this.result = result;
		this.message = message;
	}

	public Result getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}

	public static CommonRespResult parseResult(String msg) {
		if (msg == null)
			return createOtherErrorResp("");

		if (!msg.contains(SEPARATOR))
			return createOtherErrorResp(MSG_TAG + msg);

		String[] strArray = msg.split(Pattern.quote(SEPARATOR));
		if (strArray.length != 2)
			return createOtherErrorResp(MSG_TAG + msg);

		try {
			Result result = Enum.valueOf(Result.class, strArray[0]);
			String message = getMessage(result.getNumber());
			return new CommonRespResult(result, result == Result.INVALID_PARAM ? (message + strArray[1]) : message);
		} catch (Exception e) {
			return createOtherErrorResp(msg);
		}
	}

	private static CommonRespResult createOtherErrorResp(String msg) {
		return new CommonRespResult(Result.OTHER_ERROR,
				CommonRespResult.RESP_OTHER_ERROR);
	}

	private static String getMessage(int index) {
		switch (index) {
		case 0:
			return RESP_SUCCESS;
		case -1:
			return CommonRespResult.RESP_INVALID_ACCOUNT;
		case -2:
			return CommonRespResult.RESP_INVALID_PARAM;
		case -6:
			return CommonRespResult.RESP_ACCOUNT_NOT_EXIST_OR_WRONG_PIN;
		case -9:
			return CommonRespResult.RESP_CAPITAL_ACCOUNT_NOT_EXIST;
		case -11:
			return CommonRespResult.RESP_TOO_LARGE_PACK;
		case -12:
			return CommonRespResult.RESP_NO_SAVE_ACCOUNT;
		case -13:
			return CommonRespResult.RESP_NOT_ALLOW_SEND;
		case -99:
			return CommonRespResult.RESP_INNER_ERROR;
		case -100:
			return CommonRespResult.RESP_OTHER_ERROR;
		default:
			return CommonRespResult.RESP_OTHER_ERROR;
		}
	}

	@Override
	public String toString() {
		return result.name() + SEPARATOR + message;
	}
}

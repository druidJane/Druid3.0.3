/*   
* Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
*             All rights reserved                         
*/
package com.xuanwu.msggate.common.sbi.entity;


/**
 * @Description:
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-10-11
 * @Version 1.0.0
 */
public enum MTResult {
	/**
	 * 成功
	 */
	SUCCESS(0),
	/**
	 * 账号无效（停用或过期）
	 */
	INVALID_ACCOUNT(-1),
	/**
	 * 参数无效
	 */
	INVALID_PARAM(-2),
	/**
	 * 无法连接到服务器
	 */
	CONNECT_TO_SERVER_FAIL(-3),
	/**
	 * 密码错误
	 */
	ACCOUNT_NOT_EXIST_OR_WRONG_PIN(-6),
	/**
	 * 账号不存在
	 */
	CAPITAL_ACCOUNT_NOT_EXIST(-9),
	/**
	 * 包号码数量超过最大限制
	 */
	TOO_LARGE_PACK(-11),
	/**
	 * 余额不足
	 */
	NO_SAVE_ACCOUNT(-12),
	/**
	 * 不允许发送信息
	 */
	NOT_ALLOW_SEND(-13),
	/**
	 * 系统内部错误
	 */
	INNER_ERROR(-99),
	/**
	 * 其它错误
	 */
	OTHER_ERROR(-100);

	private final int index;

	private MTResult(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static MTResult getResult(int index) {
		for(MTResult result : MTResult.values()) {
			if(result.getIndex() == index)
				return result;
		}
		return OTHER_ERROR;
	}
}

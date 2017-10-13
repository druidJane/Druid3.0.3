package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 
 * 
 * @Description 验证码类型
 * @author <a href="mailto:miaojiepu@wxchina.com">Jiepu.Miao</a>
 * @date 2016年8月29日
 * @version 1.0.0
 */
public enum VerificationCodeType implements HasIndexValue {

	SMS(1), VOICE(2), EMAIL(3);// 1-短信验证码 2-语音验证码 3-邮箱验证码

	private int index;

	private VerificationCodeType(int index) {
		this.index = index;
	}

	public static VerificationCodeType getType(int index) {
		for (VerificationCodeType type : VerificationCodeType.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}

	public static VerificationCodeType getType(String typeStr) {
		for (VerificationCodeType type : VerificationCodeType.values()) {
			if (type.name().equals(typeStr)) {
				return type;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return index;
	}

}

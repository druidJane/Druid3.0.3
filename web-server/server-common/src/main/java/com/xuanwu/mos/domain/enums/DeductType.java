package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 账户扣费类型
 * @Data 2017-3-31
 * @Version 1.0.0
 */
public enum DeductType implements HasIndexValue {

	/** 0：自定义单价，1：按企业单价 **/
	CUSTOM_PRICE(0), ENTERPRISE_PRICE(1);

	private int index;

	DeductType(int index) {
		this.index = index;
	}

	public static DeductType getType(int index) {
		for (DeductType type : DeductType.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}

package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 角色类型
 * @Data 2017-5-5
 * @Version 1.0.0
 */
public enum IndustryType implements HasIndexValue{

	BASE_ROLE(0), CUSTOMER_ROLE(1);

	private int index;

	IndustryType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}

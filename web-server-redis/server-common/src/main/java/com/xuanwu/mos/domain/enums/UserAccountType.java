package com.xuanwu.mos.domain.enums;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 用户账号类型
 * @Data 2017-5-17
 * @Version 1.0.0
 */
public enum UserAccountType implements HasIndexValue {

	/* 1:web， 2:接口，3:透传 */
	WEB(1), INTERFACE(2), OSPF(3);

	private int index;

	private UserAccountType(int index) {
		this.index = index;
	}

	public static UserAccountType getType(int index) {
		for (UserAccountType userAccountType : UserAccountType.values()) {
			if (userAccountType.getIndex() == index) {
				return userAccountType;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}

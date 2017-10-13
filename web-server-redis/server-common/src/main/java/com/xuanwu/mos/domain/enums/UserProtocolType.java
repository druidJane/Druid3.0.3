package com.xuanwu.mos.domain.enums;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 用户协议类型
 * @Data 2017-5-17
 * @Version 1.0.0
 */
public enum UserProtocolType implements HasIndexValue {

	CMPP2(1), CMPP3(2), SGIP(3), SMGP(4), OTHER(0);

	private int index;

	private UserProtocolType(int index) {
		this.index = index;
	}

	public static UserProtocolType getType(int index) {
		for (UserProtocolType userProtocolType : UserProtocolType.values()) {
			if (userProtocolType.getIndex() == index) {
				return userProtocolType;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}

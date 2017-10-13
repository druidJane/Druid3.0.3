package com.xuanwu.msggate.common.sbi.entity;

public enum Carrier {
	/**
	 * 移动
	 */
	MOBILE(1),
	/**
	 * 联通
	 */
	UNICOM(2),
	/**
	 * 电信小灵通
	 */
	TELECOM(3),
	/**
	 * 电信CDMA
	 */
	TELECOM_CDMA(4),

	UNDEFINDED(5);

	private final int index;

	private Carrier(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static Carrier getType(int index) {
		switch (index) {
		case 1:
			return MOBILE;
		case 2:
			return UNICOM;
		case 3:
			return TELECOM;
		case 4:
			return TELECOM_CDMA;
		case 5:
			return UNDEFINDED;
		default:
			return MOBILE;
		}
	}
}
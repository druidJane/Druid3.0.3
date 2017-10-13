package com.xuanwu.mos.domain.enums;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 付费方式
 * @Data 2017-3-31
 * @Version 1.0.0
 */
public enum PayingWay implements HasIndexValue {

	/** 0：预付费， 1：后付费 **/
	PRE_PAID(0), AFTER_PAID(1);

	private int index;

	private PayingWay(int index) {
		this.index = index;
	}

	public static PayingWay getWay(int index) {
		for (PayingWay payingWay : PayingWay.values()) {
			if (payingWay.getIndex() == index) {
				return payingWay;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}

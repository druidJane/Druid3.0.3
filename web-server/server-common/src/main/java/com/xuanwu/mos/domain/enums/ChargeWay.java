package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 充值方式
 * @Data 2017-3-31
 * @Version 1.0.0
 */
public enum ChargeWay implements HasIndexValue {

	/**
	 * 0：手动充值，1：每月自动充值，2：系统自动返还，3：赠送，4：转入
	 * 5：撤销，6：转出，7：月短信扣费，8：失败自动退款，9：人工返还
	 **/
	HAND_CHARGE(0), AUTO_CHARGE(1), SYS_AUTO_REPAY(2), DONATE(3), ROLL_IN(4),
	CANCEL(5), ROLL_OUT(6), MONTH_DEDUCT(7), FAIL_AUTO_REPAY(8), HAND_REPAY(9);

	private int index;

	private ChargeWay(int index) {
		this.index = index;
	}

	public static ChargeWay getWay(int index) {
		for (ChargeWay chargeWay : ChargeWay.values()) {
			if (chargeWay.getIndex() == index) {
				return chargeWay;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}

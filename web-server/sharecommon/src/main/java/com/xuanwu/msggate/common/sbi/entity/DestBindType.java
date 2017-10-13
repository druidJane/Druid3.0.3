package com.xuanwu.msggate.common.sbi.entity;

public enum DestBindType {
	/* 白名单备用 */
	WHITE_REDIRECT(0),
	/* 区域优先 */
	REGION_PRIOR(1),
	/* 通道切换 */
	CHANNEL_CHANGE(2);

	private int index;

	private DestBindType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public static DestBindType getType(int index) {
		switch (index) {
		case 0:
			return WHITE_REDIRECT;
		case 1:
			return REGION_PRIOR;
		case 2:
			return CHANNEL_CHANGE;
		default:
			throw new IllegalArgumentException("(DestBindType: 参数值无效，有效值为0、1、2)");
		}
	}
}
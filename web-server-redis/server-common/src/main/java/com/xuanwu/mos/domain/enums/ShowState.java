package com.xuanwu.mos.domain.enums;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 状态显示
 * @Data 2017-6-8
 * @Version 1.0.0
 */
public enum ShowState implements HasIndexValue {

	/* 显示(1), 隐藏(0) */
	SHOW(1), HIDE(0);

	private int index;

	private ShowState(int index) {
		this.index = index;
	}

	public static ShowState getState(int index) {
		for (ShowState state : ShowState.values()) {
			if (state.getIndex() == index) {
				return state;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}

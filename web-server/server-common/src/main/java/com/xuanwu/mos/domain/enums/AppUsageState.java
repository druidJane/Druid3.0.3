package com.xuanwu.mos.domain.enums;


import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 应用的启停状态， 由运营人员在后台进行操作 , 如在判断应用存在判断恶意行为的时候，可以应用的状态设置为停用 即使开发者 应用实际使用状态为运营中，
 * 应该也会停止 （取决其impl）
 * 
 * @author <a href="kangqinghua@wxchina.com">Qinghua Kang</a>
 * @version 1.0.0
 * @date 2016/9/14 11:24:01
 */
public enum AppUsageState implements HasIndexValue {

	// 0:已停用,1:已启用
	OFF(0), ON(1);

	private int index;

	AppUsageState(int index) {
		this.index = index;
	}

	public static AppUsageState getState(int index) {
		for (AppUsageState state : AppUsageState.values()) {
			if (state.getIndex() == index) {
				return state;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return index;
	}

}

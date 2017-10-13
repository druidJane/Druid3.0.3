package com.xuanwu.mos.domain.enums;

/**
 * 用户使用状态
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
public enum UserState implements HasIndexValue {

	// 0:正常,1:暂停,2:到期,3:无效
	NORMAL(0), SUSPEND(1), TERMINATE(2);

	private final int index;

	private UserState(int index) {
		this.index = index;
	}

	public static UserState getState(int index) {
		for (UserState state : UserState.values()) {
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

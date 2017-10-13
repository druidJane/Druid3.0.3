package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * Created by 林泽强 on 2016/8/25. 文件任务状态枚举
 */
public enum TaskState implements HasIndexValue {
	/**
	 * Wait to handle
	 */
	Wait(0),
	/**
	 * Handling
	 */
	Handle(1),
	/**
	 * Handled
	 */
	Over(2),
	/**
	 * Unknown state
	 */
	Other(9);

	private int index;

	private TaskState(int index) {
		this.index = index;
	}

	public static TaskState getState(int index) {
		for (TaskState state : TaskState.values()) {
			if (state.getIndex() == index) {
				return state;
			}
		}
		return Other;
	}

	@Override
	public int getIndex() {
		return index;
	}
}

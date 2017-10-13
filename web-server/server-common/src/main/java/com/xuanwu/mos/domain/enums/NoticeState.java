package com.xuanwu.mos.domain.enums;


import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * Created by zengzl
 */
public enum NoticeState implements HasIndexValue {

	UNREAD(0),

	READED(1);

	private int index;

	private NoticeState(int index) {
		this.index = index;
	}

	public static NoticeState getState(int index) {
		for (NoticeState state : NoticeState.values()) {
			if (state.index == index) {
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

package com.xuanwu.mos.domain.enums;


/**
 * Created by zengzl
 */
public enum NoticeMsgType implements HasIndexValue {

	SMS_SEND_NOTICE(1),

	MMS_SEND_NOTICE(2),

	SMS_AUDIT_NOTICE(3),

	MMS_AUDIT_NOTICE(4),

	SYS_NOTICE(5);

	private int index;

	private NoticeMsgType(int index) {
		this.index = index;
	}

	public static NoticeMsgType getState(int index) {
		for (NoticeMsgType state : NoticeMsgType.values()) {
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

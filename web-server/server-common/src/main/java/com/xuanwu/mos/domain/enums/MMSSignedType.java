package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

//
public enum MMSSignedType implements HasIndexValue {

	// 企业彩信签约类型, 0:非签约企业; 1:签约企业
	NO_ENTERPRISE_SIGNED(0), ENTERPRISE_SIGNED(1);

	private int index;

	private MMSSignedType(int index) {
		this.index = index;
	}

	public static MMSSignedType getType(int index) {
		for (MMSSignedType type : MMSSignedType.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return index;
	}
}

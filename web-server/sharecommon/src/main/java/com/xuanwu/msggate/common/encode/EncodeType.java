/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.encode;

/**
 * Encode type
 *
 * @author <a href="mailto:wanglianguang@139130.net>LianGuang Wang</a>
 * @Data 2010-6-5
 * @Version 1.0.0
 */
public enum EncodeType {
	ASC(0), WRITE_CARD(3), GB(15), UCS2(8), BINARY(4), USIM(246);
	private final int index;

	private EncodeType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static EncodeType getType(int index) {
		switch (index) {
		case 0:
			return ASC;
		case 3:
			return WRITE_CARD;
		case 15:
			return GB;
		case 8:
			return UCS2;
		case 4:
			return BINARY;
		case 246:
			return USIM;
		default:
			return ASC;
		}
	}
}

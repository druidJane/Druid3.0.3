/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-10-28
 * @Version 1.0.0
 */
public enum PlatformMode {
	MOS(0), UMP(1);
	private final int index;

	private PlatformMode(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static PlatformMode getType(int index) {
		switch (index) {
		case 0:
			return MOS;
		case 1:
			return UMP;
		default:
			return UMP;
		}
	}
}

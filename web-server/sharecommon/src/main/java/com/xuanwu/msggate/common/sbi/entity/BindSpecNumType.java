/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-5
 * @Version 1.0.0
 */
public enum BindSpecNumType {
	FIXED_BIND(0), REGION_BIND(1), SYS_BIND(2);

	private final int index;

	private BindSpecNumType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static BindSpecNumType getType(int index) {
		switch (index) {
		case 0:
			return FIXED_BIND;
		case 1:
			return REGION_BIND;
		default:
			return SYS_BIND;
		}
	}

}

package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 性别
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
public enum Sex implements HasIndexValue {

	// 0:男,1:女
	MAN(0), WOMAN(1);

	private int index;

	private Sex(int index) {
		this.index = index;
	}

	public static Sex getSex(int index) {
		for (Sex sex : Sex.values()) {
			if (sex.getIndex() == index) {
				return sex;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return index;
	}
}

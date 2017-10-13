package com.xuanwu.mos.domain.enums;


import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 应用类型
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
public enum AppType implements HasIndexValue {

	DEFAULT(0), // 默认应用
	CLIENT(1); // 客户应用

	private final int index;

	private AppType(int index) {
		this.index = index;
	}

	public static AppType getType(int index) {
		for (AppType type : AppType.values()) {
			if (type.index == index) {
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

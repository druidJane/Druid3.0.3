package com.xuanwu.mos.config;

/**
 * 平台类型
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
public enum Platform {

	// 0:MOS后台,1:UMP,2:MOS前台
	BACKEND(0), UMP(1), FRONTKIT(2);

	private int index;

	private Platform(int index) {
		this.index = index;
	}

	public static Platform getType(int index) {
		for (Platform type : Platform.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}

	public int getIndex() {
		return index;
	}
}
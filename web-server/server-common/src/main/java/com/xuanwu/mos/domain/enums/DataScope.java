package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @Description 数据范围
 * @Data 2016-10-08
 * @Version 1.0.0
 */
public enum DataScope implements HasIndexValue {

	/**
	 * 仅个人
	 */
	PERSONAL(1),

	/**
	 * 仅本部门及子部门
	 */
	DEPARTMENT(2),

	/**
	 * 全局
	 */
	GLOBAL(3),

	/**
	 * 不控制
	 */
	NONE(9);

	private int index;

	private DataScope(int index) {
		this.index = index;
	}

	public static DataScope getScope(int index) {
		for (DataScope scope : DataScope.values()) {
			if (scope.getIndex() == index) {
				return scope;
			}
		}
		return NONE;
	}

	@Override
	public int getIndex() {
		return index;
	}
}

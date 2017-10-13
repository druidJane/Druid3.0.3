package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 用户账号类型
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
public enum UserType implements HasIndexValue {

	// 0:企业,1:部门,2:个人
	ENTERPRISE(0), DEPARTMENT(1), PERSONAL(2);

	private int index;

	private UserType(int index) {
		this.index = index;
	}

	public static UserType getType(int index) {
		for (UserType type : UserType.values()) {
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

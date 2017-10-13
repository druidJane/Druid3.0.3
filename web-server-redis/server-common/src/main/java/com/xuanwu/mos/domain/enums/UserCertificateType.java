package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 企业认证类型
 * 
 * @author <a href="mailto:jiangpeng@wxchina.com">Peng.Jiang</a>
 * @version 1.0.0
 * @date 2016/8/26
 */
public enum UserCertificateType implements HasIndexValue {

	// 证件类型: 1-三证合一(一照一码), 2-三证合一, 3-三证分离

	AAA(1), ABC(2), A_B_C(3);

	private int index;

	private UserCertificateType(int index) {
		this.index = index;
	}

	public static UserCertificateType getType(int index) {
		for (UserCertificateType type : UserCertificateType.values()) {
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
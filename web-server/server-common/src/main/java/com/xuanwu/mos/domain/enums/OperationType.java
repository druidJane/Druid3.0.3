package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 日志 操作类型
 * @Data 2017-3-30
 * @Version 1.0.0
 */
public enum OperationType implements HasIndexValue {

	NEW(0, "新增"), MODIFY(1, "修改"), DELETE(2, "删除"), SEND(3, "发送"), AUDIT(4, "审核"),
	IMPORT(5, "导入"), EXPORT(6, "导出"), LOGIN(7, "登录"), LOGOUT(8, "退出"), RESEND(10, "失败重发"),
	RESET_LOGIN_PWD(20, "重置登录密码"), RESET_SEND_PWD(21, "重置发送密码"),RESET_TRANSMIT_PWD(22, "重置透传密码"),
	VALID_PHONE(23, "动态验证码登录");

	private int index;

	private String operationName;

	private OperationType(int index, String operationName) {
		this.operationName = operationName;
		this.index = index;
	}

	public static OperationType getOperationType(int index) {
		for (OperationType type : OperationType.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	public String getOperationName() {
		return this.operationName;
	}
}

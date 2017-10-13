package com.xuanwu.msggate.shard.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public enum ColumnType {
	INT("int"), STRING("string"), DATE("date");

	private final String TYPE_CODE;
	private static Map<String, ColumnType> codeLookup = new HashMap<String, ColumnType>();

	static {
		for (ColumnType type : ColumnType.values()) {
			codeLookup.put(type.TYPE_CODE, type);
		}
	}

	private ColumnType(String code) {
		this.TYPE_CODE = code;
	}

	public String getTypeCode() {
		return TYPE_CODE;
	}

	public static ColumnType forCode(String code) {
		return codeLookup.get(code);
	}
}

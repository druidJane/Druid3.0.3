package com.xuanwu.msggate.shard.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public enum ItemType {
	INTEGER("int"), STRING("string"), DATE("date"), ENUM("enum");

	private final String TYPE_CODE;
	private static Map<String, ItemType> codeLookup = new HashMap<String, ItemType>();

	static {
		for (ItemType type : ItemType.values()) {
			codeLookup.put(type.TYPE_CODE, type);
		}
	}

	private ItemType(String code) {
		this.TYPE_CODE = code;
	}

	public String getTypeCode() {
		return TYPE_CODE;
	}

	public static ItemType forCode(String code) {
		return codeLookup.get(code);
	}
}

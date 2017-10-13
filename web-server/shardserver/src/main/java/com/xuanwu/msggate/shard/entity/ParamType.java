package com.xuanwu.msggate.shard.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public enum ParamType {
	CLAZZ("class"), MAP("map"),CLASSMAP("classmap");

	private String TYPE_CODE;

	private ParamType(String code) {
		this.TYPE_CODE = code;
	}

	public String getTypeCode() {
		return TYPE_CODE;
	}

	public static ParamType forCode(String code) {
		if (code.equalsIgnoreCase("class"))
			return CLAZZ;
		else if (code.equalsIgnoreCase("map"))
			return MAP;
		else if(code.equalsIgnoreCase("classmap"))
			return CLASSMAP;
		return MAP;
	}
}

package com.xuanwu.msggate.common.sbi.entity;

/**
 * @Description: use for json of template message
 * @Author <a href="jiji@javawind.com">XueFang Xu</a>
 * @Date 2013-03-13
 * @Version 1.0.0
 */

public class KeyValue {
	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValue [key=" + key + ", value=" + value + "]";
	}
}

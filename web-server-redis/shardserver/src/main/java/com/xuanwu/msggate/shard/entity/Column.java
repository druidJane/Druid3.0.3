package com.xuanwu.msggate.shard.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class Column {
	private String name;
	private ColumnType type;
	private String format;
	private String spilt = "";

	private Map<String, String> aliasMap = new HashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColumnType getType() {
		return type;
	}

	public void setType(ColumnType type) {
		this.type = type;
	}

	public String getSpilt() {
		return spilt;
	}

	public void setSpilt(String spilt) {
		this.spilt = spilt;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void putAlias(String value, String alias) {
		aliasMap.put(value, alias);
	}

	public String getAlias(String value) {
		return aliasMap.get(value);
	}
}

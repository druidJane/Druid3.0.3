package com.xuanwu.msggate.shard.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class ParamItem {
	private String name;
	private String columnName;
	private ItemType type;
	private String enumIndex;
	private String replace;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getEnumIndex() {
		return enumIndex;
	}

	public void setEnumIndex(String enumIndex) {
		this.enumIndex = enumIndex;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}
}

package com.xuanwu.msggate.shard.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class ParamInfo {
	private String name;
	private ParamType type;
	private Map<String, ParamItem> itemMap = new HashMap<String, ParamItem>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParamType getType() {
		return type;
	}

	public void setType(ParamType type) {
		this.type = type;
	}

	public void putItem(ParamItem item){
		itemMap.put(item.getColumnName(), item);
	}
	
	public ParamItem getItem(String columnName){
		return itemMap.get(columnName);
	}
}

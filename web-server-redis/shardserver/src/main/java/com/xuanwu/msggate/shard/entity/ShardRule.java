package com.xuanwu.msggate.shard.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class ShardRule {
	private Map<String, Table> tableMap = new HashMap<String, Table>();

	public Map<String, Table> getTableMap() {
		return tableMap;
	}

	public void setTableMap(Map<String, Table> tableMap) {
		this.tableMap = tableMap;
	}

	public void putTable(Table table){
		tableMap.put(table.getName(), table);
	}
	
	public Table getTable(String tableName){
		return tableMap.get(tableName);
	}
}

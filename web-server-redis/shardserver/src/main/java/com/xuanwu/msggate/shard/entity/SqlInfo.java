package com.xuanwu.msggate.shard.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class SqlInfo {
	private String id;
	private ParamInfo param;
	private List<String> refTables = new ArrayList<String>();
	
	private boolean cache = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ParamInfo getParam() {
		return param;
	}

	public void setParam(ParamInfo param) {
		this.param = param;
	}

	public List<String> getRefTables() {
		return refTables;
	}

	public void addRefTable(String table) {
		refTables.add(table);
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}
}

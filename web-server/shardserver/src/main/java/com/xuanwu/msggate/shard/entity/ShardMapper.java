package com.xuanwu.msggate.shard.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class ShardMapper {
	private String namespace;
	private List<SqlInfo> sqlInfos = new ArrayList<SqlInfo>();

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public List<SqlInfo> getSqlInfos() {
		return sqlInfos;
	}

	public void setSqlInfos(List<SqlInfo> sqlInfos) {
		this.sqlInfos = sqlInfos;
	}
	
	public void addSqlInfo(SqlInfo sqlInfo){
		sqlInfos.add(sqlInfo);
	}
}

package com.xuanwu.msggate.shard.engine;

import com.xuanwu.msggate.shard.entity.ReplaceItem;
import com.xuanwu.msggate.shard.util.ReflectionUtil;

import org.apache.ibatis.mapping.BoundSql;

import java.util.List;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class SqlRouter {
	public void replaceBoundSql(List<ReplaceItem> replaceItems, BoundSql boundSql){
		for(ReplaceItem item : replaceItems) {
			StringBuilder sb = new StringBuilder();
			sb.append("(?i)").append(item.getSrcName());
			String newSql = boundSql.getSql().replaceAll(sb.toString(), item.getTargetName());
			ReflectionUtil.setTarget(boundSql, newSql, "sql");
		}
	}
}

package com.xuanwu.msggate.shard.engine;

import com.xuanwu.msggate.common.util.StringUtil;
import com.xuanwu.msggate.shard.config.Configuration;
import com.xuanwu.msggate.shard.entity.Column;
import com.xuanwu.msggate.shard.entity.ColumnType;
import com.xuanwu.msggate.shard.entity.ParamInfo;
import com.xuanwu.msggate.shard.entity.ParamItem;
import com.xuanwu.msggate.shard.entity.ReplaceItem;
import com.xuanwu.msggate.shard.entity.SqlInfo;
import com.xuanwu.msggate.shard.entity.Table;
import com.xuanwu.msggate.shard.util.ReflectionUtil;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class SqlResolver {
	private static final Logger logger = LoggerFactory.getLogger(SqlResolver.class);
			
	private Configuration config;
	
	private Date shardTableStartTime;

	public boolean isSharded(String sqlID) {
		return config.containsSqlID(sqlID);
	}

	public SqlInfo getSqlInfo(String sqlID) {
		return config.getSqlInfo(sqlID);
	}

	public Table getTable(String tableName) {
		return config.getShardRule().getTable(tableName);
	}

	public List<ReplaceItem> parseSqlInfo(String sqlID, Object paramObject, boolean isRoute) {
		boolean replace = true;
		List<ReplaceItem> replaceItems = new ArrayList<ReplaceItem>();
		try{
			SqlInfo sqlInfo = getSqlInfo(sqlID);
			switch (sqlInfo.getParam().getType()) {
			case MAP:
				replace = parseMap(sqlInfo, paramObject, replaceItems, isRoute);
				break;
			case CLAZZ:
				replace = parseClass(sqlInfo, paramObject, replaceItems, isRoute);
				break;
			case CLASSMAP:
				replace = parseClassMap(sqlInfo, paramObject, replaceItems, isRoute);
				break;
			}
			if(!replace)
				return Collections.emptyList();
		}catch (Exception e) {
			logger.error("Parse sql info failed,cause by:", e);
		}
		return replaceItems;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean parseMap(SqlInfo sqlInfo, 
			Object paramObject, List<ReplaceItem> replaceItems, boolean isRoute) {
		ParamInfo param = sqlInfo.getParam();
		Map map = (Map)paramObject;
		
		Object paramVal = null;
		for (String tableName : sqlInfo.getRefTables()) {
			Table table = getTable(tableName);
			StringBuilder newTable = new StringBuilder();
			if(isRoute)
				newTable.append(tableName);
			
			for(Column column : table.getColumns()){
				ParamItem item = param.getItem(column.getName());
				paramVal = map.get(item.getName());
				boolean isAppend = appendAlias(newTable, column, paramVal);
				if(!isAppend)
					return false;
				
				if(!isRoute || StringUtil.isBlank(item.getReplace()))
					continue;
				replaceItems.add(new ReplaceItem(item.getReplace(), item.getColumnName()));
			}
			replaceItems.add(new ReplaceItem(tableName, newTable.toString()));
		}
		return true;
	}

	private boolean parseClass(SqlInfo sqlInfo,
			Object paramObject, List<ReplaceItem> replaceItems, boolean isRoute) throws Exception {
		ParamInfo param = sqlInfo.getParam();
		return reflectClass(param, sqlInfo, paramObject, replaceItems, isRoute);
	}

	@SuppressWarnings("rawtypes")
	private boolean parseClassMap(SqlInfo sqlInfo,
			Object paramObject, List<ReplaceItem> replaceItems, boolean isRoute) throws Exception {
		ParamInfo param = sqlInfo.getParam();
		Map map = (Map)paramObject;
	
		Object clazz = map.get(param.getName());
		return reflectClass(param, sqlInfo, clazz, replaceItems, isRoute);
	}
	
	private boolean reflectClass(ParamInfo param, SqlInfo sqlInfo, 
			Object clazz, List<ReplaceItem> replaceItems, boolean isRoute)  throws Exception {
		for (String tableName : sqlInfo.getRefTables()) {
			Table table = getTable(tableName);
			StringBuilder newTable = new StringBuilder();
			if(isRoute)
				newTable.append(tableName);
			
			for(Column column : table.getColumns()){
				ParamItem item = param.getItem(column.getName());
				Object paramVal = getParamValue(item, clazz);
				boolean isAppend = appendAlias(newTable, column, paramVal);
				if(!isAppend)
					return false;
				
				if(!isRoute || StringUtil.isBlank(item.getReplace()))
					continue;
				replaceItems.add(new ReplaceItem(item.getReplace(), item.getColumnName()));
			}
			
			replaceItems.add(new ReplaceItem(tableName, newTable.toString()));
		}
		return true;
	}
	
	private boolean appendAlias(StringBuilder newTable, Column column, Object paramVal) {
		newTable.append(column.getSpilt());
		if(column.getType() != ColumnType.DATE) {
			newTable.append(column.getAlias(paramVal.toString()));
		} else {
			Date postDate = (Date)paramVal;
			if(shardTableStartTime != null && postDate.before(shardTableStartTime))
				return false;
			newTable.append(DateFormatUtils.format(postDate, column.getFormat()));
		} 
		return true;
	}
	
	private Object getParamValue(ParamItem item, Object paramObj) throws Exception {
		switch(item.getType()){
		case INTEGER:
			return ReflectionUtil.findTarget(Integer.class, paramObj, item.getName());
		case STRING:
			return ReflectionUtil.findTarget(String.class, paramObj, item.getName());
		case DATE:
			return ReflectionUtil.findTarget(Date.class, paramObj, item.getName());
		case ENUM:
			Object enumObj = ReflectionUtil.findTarget(Enum.class, paramObj, item.getName());
			return ReflectionUtil.findTarget(Integer.class, enumObj, item.getEnumIndex());
		default:
			throw new RuntimeException("Illegal item type:" + item.getType());
		}
	}
	
	public void setShardTableStartTime(String shardTableStartTime) {
		if(shardTableStartTime == null)
			return;
		String[] strs = shardTableStartTime.split("-");
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]) - 1, Integer.parseInt(strs[2]));
		this.shardTableStartTime = cal.getTime();
	}
	
	public void setConfig(Configuration config) {
		this.config = config;
	}
}

package com.xuanwu.msggate.shard.config;

import com.xuanwu.msggate.shard.entity.ShardMapper;
import com.xuanwu.msggate.shard.entity.ShardRule;
import com.xuanwu.msggate.shard.entity.SqlInfo;
import com.xuanwu.msggate.shard.util.Delimiters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class Configuration {
	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	private ShardRule shardRule;
	private Map<String, SqlInfo> sqlMap;

	public void init() {
		long start = System.currentTimeMillis();
		logger.info("Begin to load shard config.");
		try {
			this.sqlMap = new HashMap<String, SqlInfo>();
			this.shardRule = XMLConfigReader.loadShardConfig();
			
			List<ShardMapper> mappers = XMLConfigReader.loadMapperConfig();
			for(ShardMapper mapper : mappers){
				for(SqlInfo sqlInfo : mapper.getSqlInfos()){
					String sqlID = mapper.getNamespace() + Delimiters.CH_DOT + sqlInfo.getId();
					sqlMap.put(sqlID, sqlInfo);
				}
			}
		} catch (Exception e) {
			logger.error("Load shard config failed,cause:{}", e);
		}
		long end = System.currentTimeMillis();
		logger.info("End to load shard config. eplase time:{}", (end - start));
	}

	public ShardRule getShardRule() {
		return shardRule;
	}

	public void setShardRule(ShardRule shardRule) {
		this.shardRule = shardRule;
	}

	public boolean containsSqlID(String sqlID){
		return sqlMap.containsKey(sqlID);
	}
	
	public SqlInfo getSqlInfo(String sqlID){
		return sqlMap.get(sqlID);
	}
}

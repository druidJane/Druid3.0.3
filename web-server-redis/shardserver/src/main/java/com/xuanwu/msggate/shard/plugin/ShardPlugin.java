package com.xuanwu.msggate.shard.plugin;

import com.xuanwu.msggate.shard.config.Configuration;
import com.xuanwu.msggate.shard.engine.SqlResolver;
import com.xuanwu.msggate.shard.engine.SqlRouter;
import com.xuanwu.msggate.shard.entity.ReplaceItem;
import com.xuanwu.msggate.shard.util.ReflectionUtil;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
@Intercepts( { @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class, Integer.class  })
})
public class ShardPlugin implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(ShardPlugin.class);
	
	private static final String DELEGATE = "delegate";
	private static final String MAPPED_STATEMENT = "mappedStatement";
	
	private SqlRouter router;
	private SqlResolver resolver;
	
	private Properties properties;
	
	private volatile boolean initFlag = false;
	private ReentrantLock lock = new ReentrantLock();
	
	public void init() {
		lock.lock();
		try {
			if(initFlag) 
				return;
			Configuration config = new Configuration();
			SqlRouter router = new SqlRouter();
			SqlResolver resolver = new SqlResolver();
			
			resolver.setConfig(config);
			resolver.setShardTableStartTime((String)properties.get("shardTableStartTime"));
			setRouter(router);
			setResolver(resolver);
			config.init();
			initFlag = true;
			
			logger.info("Shard Plugin was init succeed");
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(!initFlag)
			init();
		
		long start = 0;
		if(logger.isDebugEnabled())
			start = System.currentTimeMillis();
		try{
			RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
			BaseStatementHandler delegate = ReflectionUtil.findTarget(BaseStatementHandler.class, handler, DELEGATE);
			MappedStatement mappedStmt = ReflectionUtil.findTarget(MappedStatement.class, delegate, MAPPED_STATEMENT);
			
			if(!resolver.isSharded(mappedStmt.getId()))
				return invocation.proceed();
			
			List<ReplaceItem> replaceItems = resolver.parseSqlInfo(
					mappedStmt.getId(), delegate.getBoundSql().getParameterObject(), true);
			router.replaceBoundSql(replaceItems, delegate.getBoundSql());
		}catch (Exception e) {
			logger.error("Shard plugin intercept failed,cause by:", e);
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Shard plugin proceed time is:{}", (System.currentTimeMillis() - start));
		return invocation.proceed();
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public void setRouter(SqlRouter router) {
		this.router = router;
	}

	public void setResolver(SqlResolver resolver) {
		this.resolver = resolver;
	}
}

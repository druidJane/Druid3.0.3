package com.xuanwu.mos.db.datasource;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @Description DataSourceConfig
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-12
 * @version 1.0.0
 */
@Configuration
public class ListDataSourceConfig {

	@Value("${mybatis.config-location}")
	private String mybatisConfigLocation;

	@Autowired
	private ListConfig config;

	@Bean(name = "listDataSource")
	@ConfigurationProperties(prefix = "datasource.list")
	public DataSource listDataSource() {
		// System.out.println(config);
		BasicDataSource ds = (BasicDataSource) DataSourceBuilder.create().type(BasicDataSource.class).build();
		ds.setMaxTotal(config.getMaxTotal());
		ds.setMaxIdle(config.getMaxIdle());
		ds.setMinIdle(config.getMinIdle());
		ds.setInitialSize(config.getInitialSize());
		ds.setValidationQuery(config.getValidationQuery());
		ds.setTestWhileIdle(config.isTestWhileIdle());
		ds.setTestOnBorrow(config.isTestOnBorrow());
		ds.setTestOnReturn(config.isTestOnReturn());
		ds.setMaxWaitMillis(config.getMaxWaitMillis());
		// ds.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
		// ds.setNumTestsPerEvictionRun(config.getNumTestsPerEvictionRun());
		// ds.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());
		ds.setDefaultAutoCommit(false);
		return ds;
	}

	@Bean(name = "listTransactionManager")
	public DataSourceTransactionManager listTransactionManager(@Qualifier("listDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "listSqlSessionFactory")
	public SqlSessionFactory listSqlSessionFactory(@Qualifier("listDataSource") DataSource dataSource)
			throws Exception {
		String path = mybatisConfigLocation.replace("classpath:", "/");
		ClassPathResource resource = new ClassPathResource(path);

		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setConfigLocation(resource);
		return factory.getObject();
	}

	@Bean(name = "listSqlSessionTemplate")
	public SqlSessionTemplate listSqlSessionTemplate(
			@Qualifier("listSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}

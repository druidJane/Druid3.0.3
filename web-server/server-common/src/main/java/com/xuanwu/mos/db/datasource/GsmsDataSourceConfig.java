package com.xuanwu.mos.db.datasource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Description DataSourceConfig
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-12
 * @version 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = DsConfig.DB_GSMS_PREFIX, name = "enabled", havingValue = "true")
public class GsmsDataSourceConfig {

	@Value("${mybatis.config-location}")
	private String mybatisConfigLocation;

	@Autowired
	private GsmsConfig config;

	@Primary
	@Bean(name = "gsmsDataSource")
	@ConfigurationProperties(prefix = "datasource.gsms")
	public DataSource gsmsDataSource() {
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

	@Bean(name = "gsmsTransactionManager")
	public DataSourceTransactionManager gsmsTransactionManager(@Qualifier("gsmsDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "gsmsSqlSessionFactory")
	public SqlSessionFactory gsmsSqlSessionFactory(@Qualifier("gsmsDataSource") DataSource dataSource)
			throws Exception {
		String path = mybatisConfigLocation.replace("classpath:", "/");
		ClassPathResource resource = new ClassPathResource(path);

		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setConfigLocation(resource);
		return factory.getObject();
	}

	@Bean(name = "gsmsSqlSessionTemplate")
	public SqlSessionTemplate gsmsSqlSessionTemplate(
			@Qualifier("gsmsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}

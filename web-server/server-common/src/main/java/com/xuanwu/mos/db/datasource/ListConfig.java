package com.xuanwu.mos.db.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ListConfig
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-23
 * @version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "datasource.list")
public class ListConfig extends DsConfig {

}

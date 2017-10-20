package com.xuanwu.mos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by zhangz on 2017/8/17.
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "spring.redis" , name = "enabled" , havingValue = "true")
public class RedisConfig extends CachingConfigurerSupport {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @SuppressWarnings("rawtypes")
    @Bean(name ="redisCacheManager")
    public CacheManager cacheManager() {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate());
        //设置缓存过期时间
        rcm.setDefaultExpiration(60);//秒
        return rcm;
    }

    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
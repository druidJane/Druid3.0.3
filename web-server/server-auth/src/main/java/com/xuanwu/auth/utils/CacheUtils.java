package com.xuanwu.auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

/**
 * @author by zhangz on 2017/11/8.
 */
@Component
@ConditionalOnProperty(prefix = "spring.redis", name = "enabled", havingValue = "true")
public class CacheUtils {
    private static final String VERIFY_CODE_PRE = "VerifyCode_";
    private static final String TOKEN_PRE = "Token_";
    @Autowired
    private RedisTemplate<String, String> redisStringTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setImageVerifyCode(String sessionId, String imageVerifyCode) {
        redisStringTemplate.boundValueOps(VERIFY_CODE_PRE + sessionId).set(imageVerifyCode, 30, TimeUnit.MINUTES);
    }
    public String getImageVerifyCode(String sessionId) {
        return redisStringTemplate.boundValueOps(VERIFY_CODE_PRE + sessionId).get();
    }

    public void setTokenVerify(String sessionId, PrivateKey key) {
        redisTemplate.boundValueOps(TOKEN_PRE + sessionId).set(key, 30, TimeUnit.MINUTES);
    }
}

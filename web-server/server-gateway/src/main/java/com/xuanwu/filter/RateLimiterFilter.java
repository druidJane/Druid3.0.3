package com.xuanwu.filter;

import com.google.common.util.concurrent.RateLimiter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

/** 用于请求流量限速
 * Created by zhangz on 2017/10/16.
 */
public class RateLimiterFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);
    @Autowired
    private RateLimiter rateLimiter;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        boolean acquireSuccess = rateLimiter.tryAcquire(1);
        if(!acquireSuccess){
            ctx.setResponseStatusCode(TOO_MANY_REQUESTS.value());
            ctx.put("rateLimitExceeded", "true");
            throw new RuntimeException(TOO_MANY_REQUESTS.toString(), null);
        }
        return null;
    }
}

package com.xuanwu.filter;

import com.google.common.util.concurrent.RateLimiter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

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
        // TODO 根据IP，请求URL限速
        RequestContext ctx = RequestContext.getCurrentContext();
        boolean acquireSuccess = rateLimiter.tryAcquire(1);
        if(!acquireSuccess){
            ctx.setResponseStatusCode(TOO_MANY_REQUESTS.value());
            ctx.put("rateLimitExceeded", "true");
            throw new RuntimeException(TOO_MANY_REQUESTS.toString(), null);
        }
        // 网关转发，设置serverName
        ctx.addZuulRequestHeader("Host", toHostHeader(ctx.getRequest()));
        return null;
    }
    private String toHostHeader(HttpServletRequest request) {
        int port = request.getServerPort();
        if ((port == 80 && "http".equals(request.getScheme()))
                || (port == 443 && "https".equals(request.getScheme()))) {
            return request.getServerName();
        }
        else {
            return request.getServerName() + ":" + port;
        }
    }
}

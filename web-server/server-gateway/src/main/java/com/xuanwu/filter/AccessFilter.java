package com.xuanwu.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangz on 2017/10/16.
 */

public class AccessFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
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

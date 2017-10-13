package com.xuanwu.mos.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭垚辉 on 2017/6/28.
 */
public class NetUtil {


    private static final String X_FORWARDED_FOR = "X-Forwarded-For";


    /**
     * 获取发送用户的客户端ip
     * 参考博客：https://yq.aliyun.com/articles/42168
     * nginx中的配置：
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     *
     * 检测客户端的方法详解：检测X-Forwarded-For(有多个取第一个) --> 检测Host
     */
    public static String checkUserTrueIp(HttpServletRequest request) {
        String realUserIp = "";
        String forwardIp = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.isNotBlank(forwardIp)) {
            if (StringUtils.contains(forwardIp, Delimiters.COMMA)) {
                realUserIp = forwardIp.split(Delimiters.COMMA)[0];
            } else {
                realUserIp = forwardIp;
            }
        }
        return realUserIp;
    }
}

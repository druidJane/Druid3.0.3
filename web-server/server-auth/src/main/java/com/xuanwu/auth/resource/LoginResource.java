package com.xuanwu.auth.resource;


import com.xuanwu.auth.utils.CacheUtils;
import com.xuanwu.auth.utils.VerifyCodeUtils;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageDataEncode;
import com.xuanwu.mos.dto.RsaEncode;
import com.xuanwu.mos.utils.SessionUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author zhangz
 * @date 2017/10/17
 */
@Component
@Path("/login")
public class LoginResource {
    private static final Logger logger = LoggerFactory
            .getLogger(LoginResource.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private CacheUtils cacheUtils;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public String login(Map<String, String> reqMap, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        String userName = reqMap.get("username");
        String passWord = reqMap.get("password");
        String imgVerifyCode = reqMap.get("imgVerifyCode");
        Cookie cookie = new Cookie("keyParis", "keyParis");
        response.addCookie(cookie);
        logger.info("username=" + userName + ",passWord=" + passWord + ",imgVerifyCode=" + imgVerifyCode);
        return null;
    }

    /**
     * 自动生成验证码
     */
    @GET
    @Path("autocode")
    @Produces({MediaType.APPLICATION_JSON})
    public String autoCode(@Context HttpServletResponse response, @Context HttpServletRequest request) throws IOException {
        String sessionId = SessionUtil.getSessionId(request);
        if (StringUtils.isBlank(sessionId)) {
            sessionId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(SessionUtil.FRONTKIT_SESSIONID, sessionId);
            response.addCookie(cookie);
        }
        OutputStream os = response.getOutputStream();
        response.setContentType("image/jpg");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "No-cache");
        String imageVerifyCode = VerifyCodeUtils.outputVerifyImage(100, 30, os, VerifyCodeUtils.VERIFY_FOUR);
        os.flush();
        cacheUtils.setImageVerifyCode(sessionId, imageVerifyCode);
        return imageVerifyCode;
    }

    /**
     * 生产令牌
     */
    @POST
    @Path("token")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp token(@Context HttpServletResponse response,@Context HttpServletRequest request) throws IOException {
        Map<String, String> typeMap = new HashMap<>();
        PageDataEncode rsaEncode = new RsaEncode();
        PageDataEncode.RsaParams rsa = rsaEncode.init();
        typeMap.put("exp", rsa.getExponent());
        typeMap.put("mod", rsa.getModulus());
        String sessionId = SessionUtil.getSessionId(request);
        cacheUtils.setTokenVerify(sessionId,rsa.getKey());
        return JsonResp.success(typeMap);
    }
}

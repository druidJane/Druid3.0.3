package com.xuanwu.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xuanwu.utils.CaesarUtil;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**输出数据加密
 * Created by  on 2017/8/18.
 */
public class WriteFilter extends ZuulFilter{
    private Logger logger = LoggerFactory.getLogger(WriteFilter.class);
    private Random random = new Random();
    private static final String  BASE64_RESPONSE_KEY = "Data-base64-Reponse";
    @Value("${base64.key_str_ks}")
    private String KEY_STR_KS;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        
        if(ctx.getResponseStatusCode()!=200){
        	ctx.setSendZuulResponse(false);
        	ctx.setResponseStatusCode(ctx.getResponseStatusCode());  
            ctx.setResponseBody("error page");
            return null;
        }
        //addCookieByUri(ctx.getRequest().getServletPath(),ctx);
        if(request.getMethod().equals("GET")){
            return null;
        }
        /*if (request.getHeader(BASE64_KEY)!=null
                && (request.getHeader(BASE64_KEY) != null && request.getHeader(BASE64_KEY).length()>0 && StringUtils.isNumeric(request.getHeader(BASE64_KEY)))
                && (request.getHeader(CONTENT_KEY) != null && !request.getHeader(CONTENT_KEY).toString().contains("multipart/form-data"))) {*/
            InputStream stream = ctx.getResponseDataStream();
            HttpServletResponse response = ctx.getResponse();
            String dataStr = null;
            try {
                dataStr = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
                logger.debug("加密前的服务器返回数据："+dataStr);
                String encodeStr = Base64Encode(dataStr);
                int keyParm = random.nextInt(10)+1;
                response.addHeader(BASE64_RESPONSE_KEY,keyParm+"");
                ctx.setResponseBody("\""+CaesarUtil.encrypt(encodeStr,keyParm)+"\"");
            } catch (Exception e) {
            	ctx.setSendZuulResponse(false);
            	ctx.setResponseStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
                e.printStackTrace();
            }
        /*}*/
        return null;
    }

    private void addCookieByUri(String requestURL, RequestContext ctx) {
        if(requestURL.startsWith("/mos/service")){
            Cookie[] cookies = ctx.getRequest().getCookies();
            Cookie cookie = new Cookie("FRONTKIT_SESSIONID", UUID.randomUUID().toString());
            cookie.setHttpOnly(true);
            ctx.getResponse().addCookie(cookie);
        }
    }

    private String Base64Encode(String data) {
        try {
            return new String(Base64.encode(data.getBytes("UTF-8")));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }
}

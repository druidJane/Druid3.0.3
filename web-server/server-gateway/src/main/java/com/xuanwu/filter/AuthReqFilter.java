package com.xuanwu.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xuanwu.exception.FilterError;
import com.xuanwu.utils.DateUtil;
import com.xuanwu.utils.Delimiters;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**校验跨站攻击和服务器传输鉴权
 * Created by  on 2017/8/18.
 */
public class AuthReqFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(AuthReqFilter.class);
    private ConcurrentHashMap<String, SoftReference<LimitTimer>> numbersMap = null;
    private String KEY_STR = "";
    
    @Value("${gateway.auth_limit_time}")
    private int LIMIT_TIME = 1;
    @Value("${base64.key_str_config}")
    private String KEY_STR_CONFIG;
    @Value("${gateway.auth_white_list}")
    private String WHITE_LIST;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 15;
    }

    /**
     * 用于校验重复请求
     */
    class LimitTimer {
        private Date time;// 过期时间

        public LimitTimer(Date time) {
            this.time = time;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

    @PostConstruct
    public void initNumbersMap() {
        numbersMap = new ConcurrentHashMap<String, SoftReference<LimitTimer>>();
        new ClearNumbersThread().start();
        KEY_STR = Base64.encodeBase64String(KEY_STR_CONFIG.getBytes());
        
        
        try {
			logger.warn("security_fun_content_key 中 conkey 生成 ：[{}]",initKey(KEY_STR_CONFIG));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private String initKey(String str) throws UnsupportedEncodingException{   
    	if(str==null || str.length()==0){
    		return "";
    	}
    	String bb = Base64.encodeBase64String(str.getBytes());
    	StringBuilder ret = new StringBuilder();
    	byte[] byes = bb.getBytes("utf-8");
    	int len = 0;
    	for(byte i: byes){
    		ret.append((i+(len))).append(",");
    		len++;
    	}
    	return ret.toString().substring(0, ret.length()-1);
    }
    
    /**
     * 删除过时的号码缓存数据
     */
    class ClearNumbersThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Date now = new Date();
                    for (Map.Entry<String, SoftReference<LimitTimer>> entry : numbersMap.entrySet()) {
                        String key = entry.getKey();
                        LimitTimer timer =  entry.getValue().get();
                        if (timer==null) {
                            logger.debug("kill key = "+key);
                            numbersMap.remove(key);
                            continue;
                        }
                        Date limitTime = timer.getTime();
                        if (limitTime.before(now)) {
                            logger.debug("kill key = "+key);
                            numbersMap.remove(key);
                        }
                    }
                    DateUtil.sleepWithoutInterrupte(1000);
                } catch (Exception e) {
                    // ignore
                    logger.error("ClearNumbersThread run error {}",e);
                    DateUtil.sleepWithoutInterrupte(1000);
                }
            }
        }
    }
    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String url = request.getRequestURL().toString();
        String[] whiteLists = WHITE_LIST.split(Delimiters.COMMA);
        for(String whiteList:whiteLists){
            if(StringUtils.isNotEmpty(whiteList) && url.endsWith(whiteList)){
                return false;
            }
        }
        RequestContext ctx = RequestContext.getCurrentContext();
		if(ctx.get("isAuth")!=null){
			return false;
		}
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext(); 
        try{
            validateXslRequest(ctx.getRequest());//跨域验证
            validatePostRequest(ctx.getRequest());//post验证
            ctx.set("isAuth", true);
        }
        catch(FilterError e){
        	ctx.setSendZuulResponse(false);
        	ctx.setResponseStatusCode(e.getStatus());  
            ctx.setResponseBody("{\"result\":\""+e.getMsg()+"\"}"); 
        }
        return null;
    }
    /**
     * 验证post的签名请求
     * @param httpRequest
     * @throws FilterError
     */
    private void validatePostRequest(HttpServletRequest httpRequest) throws FilterError {
        String method = httpRequest.getMethod();
        String ContentType = httpRequest.getHeader("Content-Type");
        logger.debug("url请求："+httpRequest.getRequestURI());
        if (method.equalsIgnoreCase("POST") &&
                (ContentType != null &&!ContentType.toLowerCase().contains("multipart/form-data"))){
            try {
                String authenticationHeader = httpRequest.getHeader("Auth-X");
                logger.debug("签名请求："+authenticationHeader);
                if(authenticationHeader==null || authenticationHeader.length()==0){
                    logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>> 未授权请求");
                    throw new FilterError("未授权", HttpServletResponse.SC_UNAUTHORIZED);
                }
                String enStr = new String(Base64.decodeBase64(authenticationHeader));
                String[] arr = enStr.split("/");
                String hashKey = arr[0];
                String timeKey = arr[1];

                if (!hashKey.equals(DigestUtils.md5Hex(KEY_STR + timeKey))) {
                    logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>> 未授权请求");
                    throw new FilterError("未授权", HttpServletResponse.SC_UNAUTHORIZED);
                }

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, LIMIT_TIME);
                Date limitTime = calendar.getTime();
                Date now = new Date();
                if (isLimited(hashKey,limitTime, now)) { //重复请求
                    logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>> 重复请求");
                    throw new FilterError("未授权 -1000", HttpServletResponse.SC_UNAUTHORIZED);
                }                

            } catch (Exception e) {
                logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>>"+e.getMessage());
                throw new FilterError("未授权", HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
    /**
     * 验证跨域请求
     * // true 站内提交，验证通过 //false 站外提交，验证失败
     * liangjiandong
     * @param request
     * @return
     */
    private void validateXslRequest(HttpServletRequest request) throws FilterError {
        String referer = "";
        boolean referer_sign = true;
        Enumeration headerValues = request.getHeaders("referer");
        while(headerValues.hasMoreElements()) {
            referer = (String) headerValues.nextElement();
        }
        // 判断是否存在请求页面
        if(StringUtils.isBlank(referer)) referer_sign = false;
        else{
            // 判断请求页面和getRequestURI是否相同
            String servername_str = request.getServerName();
            if(StringUtils.isNotBlank(servername_str)) {
                int index = 0;
                if(StringUtils.indexOf(referer, "https://") == 0) {
                    index = 8;
                }
                else if (StringUtils.indexOf(referer, "http://") == 0) {
                    index = 7;
                }
                if(referer.length() - index < servername_str.length()) {
                    referer_sign = false;
                }
                else{
                    String referer_str = referer.substring(index, index + servername_str.length());
                    if(!servername_str.equalsIgnoreCase(referer_str)) referer_sign = false;
                }
            }
            else referer_sign = false;
        }
        if(!referer_sign) {
            throw new FilterError("非法登录", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    private boolean isLimited(String key, Date limitTime, Date now) {
        SoftReference<LimitTimer> softRef = numbersMap.get(key);
        if (softRef != null) {
            LimitTimer limitTimerBef = softRef.get();
            limitTimerBef.setTime(limitTime);
            return true;
        }
        logger.debug("put key = "+key);
        numbersMap.put(key, new SoftReference<LimitTimer>(new LimitTimer(limitTime)));
        return false;
    }
}

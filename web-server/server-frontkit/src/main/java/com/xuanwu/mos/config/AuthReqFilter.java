package com.xuanwu.mos.config;

import com.xuanwu.mos.utils.DateUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * 服务器传输鉴权 过滤器
 * @author liangjiadnong
 *
 */
public class AuthReqFilter implements ContainerRequestFilter{
	private Logger logger = LoggerFactory.getLogger(AuthReqFilter.class);
	private static final String KEY_STR = Base64.encodeBase64String("mos5.0FrontKit".getBytes());
	private static final int LIMIT_TIME = 1;
	
    @Context
    private HttpServletRequest httpRequest;
    
    private ConcurrentHashMap<String, SoftReference<LimitTimer>> numbersMap = null;
    
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
					for (Entry<String, SoftReference<LimitTimer>> entry : numbersMap.entrySet()) {
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
        
    /**
     * 服务端post授权认证处理  需要和客户端请求配套处理
     * 
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	    	
    	try{
    		validateXslRequest(httpRequest);//跨域验证
    		validatePostRequest(httpRequest);//post验证
    	}
    	catch(FilterError e){
    		requestContext.abortWith(Response.status(e.getStatus()).entity(e.getMsg()).build());
    	}    	    
    }
    
    /**
     * 验证post的签名请求
     * @param request
     * @throws FilterError
     */
    private void validatePostRequest(HttpServletRequest request) throws FilterError {
    	String method = httpRequest.getMethod();
    	String ContentType = httpRequest.getHeader("Content-Type");
    	
    	if (method.equalsIgnoreCase("POST") && 
    			(ContentType != null &&!ContentType.toLowerCase().contains("multipart/form-data"))){
			try {
				String authenticationHeader = httpRequest.getHeader("Auth-X");
				if(authenticationHeader==null || authenticationHeader.length()==0){
					logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>> 未授权请求");
					throw new FilterError("未授权", Response.Status.UNAUTHORIZED);
				}
				String enStr = new String(Base64.decodeBase64(authenticationHeader));
				String[] arr = enStr.split("/");
				String hashKey = arr[0];
				String timeKey = arr[1];							

				if (!hashKey.equals(DigestUtils.md5Hex(KEY_STR + timeKey))) {
					logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>> 未授权请求");
					throw new FilterError("未授权", Response.Status.UNAUTHORIZED);
				}
				
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MINUTE, LIMIT_TIME);
				Date limitTime = calendar.getTime();
				Date now = new Date();
				if (isLimited(hashKey,limitTime, now)) { //重复请求
					logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>> 重复请求");
					throw new FilterError("未授权 -1000", Response.Status.UNAUTHORIZED);
				}
				
			} catch (Exception e) {
				logger.warn(httpRequest.getLocalAddr()+"==>>"+httpRequest.getRequestURI().toString()+"==>>"+e.getMessage());
				throw new FilterError("未授权", Response.Status.UNAUTHORIZED);
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
        	throw new FilterError("非法登录", Response.Status.UNAUTHORIZED);
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
    
    /**
     * 过滤异常
     * @author Administrator
     *
     */
    public class FilterError extends Exception{
		private Response.Status status;
		private String msg;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public FilterError(String msg,Response.Status status){
			super(msg);
			this.msg = msg;
			this.status = status;
		}

		public Response.Status getStatus() {
			return status;
		}

		public String getMsg() {
			return msg;
		}		
	}
    
}

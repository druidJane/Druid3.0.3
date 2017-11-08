package com.xuanwu.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.xuanwu.exception.FilterError;
import com.xuanwu.utils.CaesarUtil;
import com.xuanwu.utils.Constant;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求URL带参数，将参数解密，再转发
 */
public class UrlParamFilter extends ZuulFilter {

	private Logger logger = LoggerFactory.getLogger(UrlParamFilter.class);
	@Value("${base64.key_str_ks}")
	private String KEY_STR_KS;
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 5;
	}
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		if (ctx.get("isRoute") != null) {
			return false;
		}
		return true;
	}

	private void dealFun(RequestContext ctx) throws FilterError {
		HttpServletRequest request = ctx.getRequest();
		if (request.getHeader(Constant.URL_PARAM_KEY) != null) {

			try {
				final String urlOrig = request.getRequestURL().toString();
				String key = urlOrig.substring(urlOrig.lastIndexOf("/") + 1, urlOrig.length());
				if(key.indexOf("?")>-1){
					key = key.substring(0,key.indexOf("?"));
				}
				byte[] keyByte = Base64.decode(key);
				key = new String(keyByte);
				String[] keyArr = key.split("_");
				key = CaesarUtil.decrypt(keyArr[0], Integer.parseInt(keyArr[1]));
				keyByte = Base64.decode(key);
				key = new String(keyByte);

				final String queryString = key.substring(2, key.length() - 1);

				ctx.setRequest(new HttpServletRequestWrapper(request) {
					@Override
					public String getQueryString() {
						logger.debug("queryString：" + queryString);
						return queryString;
					}

					@Override
					public String getRequestURI() {
						String urlOrig = super.getRequestURI().toString();
						String newUrl = urlOrig.substring(0, urlOrig.lastIndexOf("/"));
						logger.debug("newUrl：" + newUrl);
						return newUrl;
					}
				});
				ctx.set("isRoute", true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new FilterError("非法请求-url", HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
	}

	@Override
	public Object run() {

		RequestContext ctx = RequestContext.getCurrentContext();
		try {
			dealFun(ctx);
		} catch (FilterError e) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(e.getStatus());
			ctx.setResponseBody("{\"result\":\"" + e.getMsg() + "\"}");
		}
		return null;
	}

}

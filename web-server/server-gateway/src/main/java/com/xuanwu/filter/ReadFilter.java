package com.xuanwu.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.xuanwu.exception.FilterError;
import com.xuanwu.utils.CaesarUtil;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.xuanwu.utils.Constant.BASE64_KEY;
import static com.xuanwu.utils.Constant.CONTENT_KEY;

/**
 * 请求数据解密 Created by on 2017/8/18.
 */
public class ReadFilter extends ZuulFilter {
	private Logger logger = LoggerFactory.getLogger(ReadFilter.class);
	@Value("${base64.key_str_ks}")
	private String KEY_STR_KS;

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
		RequestContext ctx = RequestContext.getCurrentContext();
		if (ctx.get("isRead") != null) {
			return false;
		}
		return true;
	}

	private void dealFun(RequestContext ctx) throws FilterError {
		HttpServletRequest request = ctx.getRequest();
		logger.debug("后：" + request.getRequestURI());
		if (request.getHeader(BASE64_KEY) != null
				&& (request.getHeader(BASE64_KEY) != null && request.getHeader(BASE64_KEY).length() > 0
						&& StringUtils.isNumeric(request.getHeader(BASE64_KEY)))
				&& (request.getHeader(CONTENT_KEY) != null
						&& !request.getHeader(CONTENT_KEY).toString().contains("multipart/form-data"))) {
			if (request.getMethod().equals("GET")) {
				// TODO get 请求,可修改参数
				Map<String, List<String>> qp = RequestContext.getCurrentContext().getRequestQueryParams();
				LinkedList<String> valueList = new LinkedList<String>();
				RequestContext.getCurrentContext().setRequestQueryParams(qp);
				return;
			}
			InputStream oldInput = null;
			try {
				oldInput = (InputStream) ctx.get("requestEntity");
				if (oldInput == null) {
					oldInput = request.getInputStream();
				}
				int keyParm = Integer.valueOf(request.getHeader(BASE64_KEY));
				InputStream in_nocode = BaseToInputStream(oldInput, keyParm);
				String body = StreamUtils.copyToString(in_nocode, Charset.forName("UTF-8"));
				logger.debug("请求数据解密后：" + body);
				ctx.set("isRead", true);
				final byte[] bytes = body.getBytes("UTF-8");
				ctx.setRequest(new HttpServletRequestWrapper(ctx.getCurrentContext().getRequest()) {

					@Override
					public ServletInputStream getInputStream() throws IOException {
						return new ServletInputStreamWrapper(bytes);
					}

					@Override
					public int getContentLength() {
						return bytes.length;
					}

					@Override
					public long getContentLengthLong() {
						return bytes.length;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
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
			ctx.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
			ctx.setResponseBody("{\"result\":\"" + e.getMsg() + "\"}");
		}
		return null;
	}

	private InputStream BaseToInputStream(InputStream base64string, int keyParm) throws FilterError {
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(base64string, "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = bf.readLine()) != null) {
				buffer.append(line);
			}
			String str = buffer.toString();
			byte[] bytes = URLCodec.decodeUrl(str.getBytes());
			str = new String(bytes);
			int start = str.indexOf("\"");
			int end = str.lastIndexOf("\"");
			if (end != -1) {
				str = str.substring(start + 1, end);
			}
			logger.debug("请求数据解密前：" + str);
			String deBase64string = CaesarUtil.decrypt(buffer.toString(), keyParm);
			byte[] newStr = Base64.decode(deBase64string.getBytes());
			return new ByteArrayInputStream(newStr);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new FilterError("非法请求-body", HttpServletResponse.SC_UNAUTHORIZED);
		}
		//return base64string;
	}
}

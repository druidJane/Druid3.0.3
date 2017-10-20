package com.xuanwu.mos.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.utils.CaesarUtil;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Random;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

//import org.glassfish.jersey.internal.util.Base64;

/**
 * post 内容转义 base64
 * 
 * @author liangjiandong
 *
 */
public class Base64Interceptor implements ReaderInterceptor, WriterInterceptor {
	private Logger logger = LoggerFactory.getLogger(Base64Interceptor.class);
	private Random random = new Random();
	private static final String  BASE64_KEY = "Data-base64";
	private static final String  BASE64_RESPONSE_KEY = "Data-base64-Reponse";
	private static final String  CONTENT_KEY = "Content-Type";
	
	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
		MultivaluedMap<String, String> header = context.getHeaders();
		if (header.containsKey(BASE64_KEY) 
				&& (header.get(BASE64_KEY) != null && header.get(BASE64_KEY).size()>0 && StringUtils.isNumeric(header.get(BASE64_KEY).get(0))) 
				&& (header.get(CONTENT_KEY) != null && !header.get(CONTENT_KEY).toString().contains("multipart/form-data"))) {
			InputStream oldInput = context.getInputStream();
			int keyParm = Integer.valueOf(header.get(BASE64_KEY).get(0)) ;
			InputStream in_nocode = BaseToInputStream(oldInput,keyParm);
			context.setInputStream(in_nocode);
		}
		return context.proceed();
	}

	private InputStream BaseToInputStream(InputStream base64string,int keyParm) {

		try {
			
			BufferedReader bf = new BufferedReader(new InputStreamReader(base64string, "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = bf.readLine()) != null) {
				buffer.append(line);
			}
			String deBase64string = CaesarUtil.decrypt(buffer.toString(), keyParm);
			byte[] newStr = Base64.decode(deBase64string.getBytes());
			return new ByteArrayInputStream(newStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64string;
	}

	private String Base64Encode(String data) {
		try {
			return new String(Base64.encode(data.getBytes()));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		
		Object entObj = context.getEntity();
		if (entObj instanceof JsonResp) {			
			ObjectMapper mapper = new ObjectMapper();
			String dataStr = mapper.writeValueAsString(entObj);
			String encodeStr = Base64Encode(dataStr);
			MultivaluedMap<String, Object> headers = context.getHeaders();
			int keyParm = random.nextInt(10)+1;
			if(!encodeStr.isEmpty()){
				headers.add(BASE64_RESPONSE_KEY, keyParm);
				context.setEntity(CaesarUtil.encrypt(encodeStr,keyParm));				
			}
			context.proceed();
		} else {
			final OutputStream out = context.getOutputStream();
			context.setOutputStream(out);
			context.proceed();
		}
	}

}

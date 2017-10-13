/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuanwu.msggate.common.sbi.entity.KeyValue;

/**
 * @Description: Json 操作工具类
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-9-19
 * @Version 1.0.0
 */
public class JsonUtil {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static <T> T deserializeFromBytes(byte[] srcBytes, Class<T> t)
			throws Exception {
		if (srcBytes == null) {
			throw new IllegalArgumentException("srcBytes should not be null");
		}
		return mapper.readValue(srcBytes, 0, srcBytes.length, t);
	}

	public static <T> T deserialize(String src, Class<T> t) throws Exception {
		if (src == null) {
			throw new IllegalArgumentException("src should not be null");
		}
		return mapper.readValue(src, t);
	}

	public static KeyValue[] deserializeToKeyValue(String src) throws Exception {
		if (src == null) {
			throw new IllegalArgumentException("src should not be null");
		}
		return mapper.readValue(src, KeyValue[].class);
	}

	public static byte[] serializeToBytes(Object obj) throws Exception {
		return mapper.writeValueAsBytes(obj);
	}

	public static String serialize(Object obj) throws Exception {
		return mapper.writeValueAsString(obj);
	}
}

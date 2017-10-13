/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuanwu.mos.domain.entity.BaseEntity;
import com.xuanwu.mos.file.FileHead;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public static byte[] serializeToBytes(Object obj) throws Exception {
		return mapper.writeValueAsBytes(obj);
	}

	public static String serialize(Object obj) throws Exception {
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 
	 * @param data
	 *            json data
	 * @param msg
	 *            message
	 * @param ret
	 *            0--true, not 0--false
	 * @return
	 */
	public static String toJSON(String data, String msg, int ret) {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"data\":");
		sb.append(data);
		sb.append(",\"msg\":\"");
		sb.append(msg);
		sb.append("\",\"ret\":");
		sb.append(ret);
		sb.append('}');
		return sb.toString();
	}

	public static String toJSON(String msg, int ret) {
		return toJSON("null", msg, ret);
	}

	public static String toJSON(int ret) {
		String msg = "Error data!";
		if (ret == 0) {
			msg = "ok";
		}
		//此处为RoleController的-9错误码定义输出信息
		if (ret == -9){
			msg = "licensing .....";
		}
		return toJSON("null", msg, ret);
	}

	public static <T extends BaseEntity> String toJSON(List<T> entities,
													   String msg, int ret) {
		return toJSON(toJSON(entities), msg, ret);
	}

	public static <T extends BaseEntity> String toJSON(int page, int total,
			List<T> entities) {
		return toJSON(page, total, entities, null);
	}

	public static <T extends BaseEntity> String toJSON(int page, int total,
			List<T> entities, Map<String, String> extParams) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"page\":" + page + ",\"total\":" + total + ",\"rows\":");
		sb.append(toJSON(entities));

		if (extParams != null && !extParams.isEmpty()) {
			sb.append(",");
			for (Entry<String, String> entry : extParams.entrySet()) {
				sb.append("\"");
				sb.append(entry.getKey());
				sb.append("\":");
				sb.append(entry.getValue()).append(',');
			}
			sb.deleteCharAt(sb.toString().length() - 1);
		}
		sb.append("}");
		return toJSON(sb.toString(), "ok", 0);
	}

	public static <T extends BaseEntity> String toPageJSON(int page, int total,
			List<T> entities) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"page\":" + page + ",\"total\":" + total + ",\"rows\":");
		sb.append(toJSON(entities));
		sb.append("}");
		return sb.toString();
	}

	public static <T extends BaseEntity> String toJSON(List<T> entities) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < entities.size(); i++) {
			sb.append(i == 0 ? "" : ",").append(entities.get(i).toJSON());
		}
		sb.append("]");
		return sb.toString();
	}

	public static String toJSON(String fileName, FileHead fileHead) {
		if (fileHead == null || fileHead.getHeadMap().isEmpty()) {
			return toJSON("文件头或文件内容为空。", -1);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"columns\":[");
		int i = 0;
		for (Entry<Integer, String> entry : fileHead.getHeadMap().entrySet()) {
			sb.append((i++ > 0) ? "," : "");
			sb.append("{\"").append(entry.getKey()).append("\":");
			sb.append("\"").append(entry.getValue()).append("\"}");
		}
		sb.append("]");
		sb.append(",\"fileName\":\"").append(fileName);
		sb.append("\"}");
		return toJSON(sb.toString(), "ok", 0);
	}
}

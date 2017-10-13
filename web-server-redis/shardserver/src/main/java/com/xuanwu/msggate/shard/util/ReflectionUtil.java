package com.xuanwu.msggate.shard.util;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
@SuppressWarnings("unchecked")
public class ReflectionUtil extends ReflectionUtils {
	public static <T> T findTarget(Class<T> clazz, Object declared, String name)
			throws IllegalArgumentException, IllegalAccessException {
		Field field = findField(declared.getClass(), name);
		field.setAccessible(true);
		return (T) getField(field, declared);
	}

	public static void setTarget(Object declared, Object value, String name) {
		Field field = findField(declared.getClass(), name);
		field.setAccessible(true);
		setField(field, declared, value);
	}
}
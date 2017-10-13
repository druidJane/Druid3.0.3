package com.xuanwu.mos.domain.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * value是数据库里面存储的值 ,name是简短的说明，desc是详细的说明
 * 
 * @author <a href="kangqinghua@wxchina.com">Qinghua Kang</a>
 * @version 1.0.0
 * @date 2016/9/13 17:21:01
 */

public class HasIndexValueEnumHandler<E extends Enum<E> & HasIndexValue> extends BaseTypeHandler<E> {

	private Class<E> type;
	private final E[] enums;
	private ConcurrentMap<Integer, E> valueEnumMapping = new ConcurrentHashMap<>();

	public HasIndexValueEnumHandler(Class<E> type) {
		if (type == null)
			throw new IllegalArgumentException("Type argument cannot be null");
		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null)
			throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
		for (int i = 0; i < enums.length; i++) {
			valueEnumMapping.put(enums[i].getIndex(), enums[i]);
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		Integer value = parameter.getIndex();
		ps.setInt(i, value);
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Object value = rs.getObject(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return convertOrException(value);
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Object value = rs.getObject(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		return convertOrException(value);
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Object value = cs.getObject(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		return convertOrException(value);
	}

	private E convertOrException(Object value) throws IllegalArgumentException {
		E e = this.valueEnumMapping.get((Integer) value);
		if (null == e) {
			throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName());
		} else {
			return e;
		}
	}

}

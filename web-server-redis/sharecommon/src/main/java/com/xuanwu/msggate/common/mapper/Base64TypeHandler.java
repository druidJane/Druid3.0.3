package com.xuanwu.msggate.common.mapper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.binary.Base64;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class Base64TypeHandler implements TypeHandler {

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter,
			JdbcType jdbcType) throws SQLException {
		byte[] bytes = (byte[]) parameter;
		String str = Base64.encodeBase64String(bytes);
		ps.setString(i, str);
	}

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		String str = rs.getString(columnName);
		if (str == null) {
			return null;
		}
		return Base64.decodeBase64(str);
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String str = cs.getString(columnIndex);
		if (str == null) {
			return null;
		}
		return Base64.decodeBase64(str);
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		String str = rs.getString(columnIndex);
		if (str == null) {
			return null;
		}
		return Base64.decodeBase64(str);
	}

}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.mapper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @author <a href="mailto:wanglianguang@139130.net>LianGuang Wang</a>
 * @Data 2010-5-10
 * @Version 1.0.0
 */
public class UUIDTypeHandler extends BaseTypeHandler {

	@Override
	public Object getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return UUID.fromString(rs.getString(columnName));
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return UUID.fromString((cs.getString(columnIndex)));
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Object parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, ((UUID) parameter).toString());
	}

	@Override
	public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return UUID.fromString((rs.getString(columnIndex)));
	}
}

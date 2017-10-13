package com.xuanwu.msggate.common.mapper;

import com.xuanwu.msggate.common.sbi.entity.Region;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegionTypeHandler extends BaseTypeHandler<com.xuanwu.msggate.common.sbi.entity.Region>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Region parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getId());
	}

	@Override
	public Region getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		Region region = new Region();
		region.setId(rs.getInt(columnName));
		return region;
	}

	@Override
	public Region getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		Region region = new Region();
		region.setId(rs.getInt(columnIndex));
		return region;
	}

	@Override
	public Region getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Region region = new Region();
		region.setId(cs.getInt(columnIndex));
		return region;
	}

}
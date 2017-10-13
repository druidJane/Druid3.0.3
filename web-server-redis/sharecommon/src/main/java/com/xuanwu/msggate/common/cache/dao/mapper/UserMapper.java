package com.xuanwu.msggate.common.cache.dao.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
	public int existsUser(@Param("name") String name, @Param("password") String password);
}

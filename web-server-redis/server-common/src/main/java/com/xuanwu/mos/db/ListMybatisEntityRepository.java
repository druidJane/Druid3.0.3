package com.xuanwu.mos.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.xuanwu.mos.domain.Entity;

public abstract class ListMybatisEntityRepository<T extends Entity> extends MybatisEntityRepository<T> {

	@Autowired
	@Qualifier("listSqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

}

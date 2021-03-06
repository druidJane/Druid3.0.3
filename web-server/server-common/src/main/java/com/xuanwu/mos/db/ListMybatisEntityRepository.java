package com.xuanwu.mos.db;

import com.xuanwu.mos.domain.Entity;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class ListMybatisEntityRepository<T extends Entity> extends MybatisEntityRepository<T> {

	@Autowired
	@Qualifier("listSqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

}

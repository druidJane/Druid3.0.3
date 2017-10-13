/*
 * Copyright (c) 2015 by XuanWu Wireless Technology Co., Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.db;

import java.io.Serializable;
import java.util.List;

import com.xuanwu.mos.domain.Entity;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

/**
 * 仓储访问接口, 提供通用仓储方法
 * 
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2015年5月27日
 * @Version 1.0.0
 */
public interface EntityRepository<T extends Entity> {

	// 添加一个实体
	public T insert(T t) throws RepositoryException;

	// 保存一个实体
	public int update(T t) throws RepositoryException;

	// 添加一个实体(update and insert)
	public T save(T t) throws RepositoryException;

	// 更新一个实体
	public int updateSpecify(T t) throws RepositoryException;

	// 移除一个实体
	public int delete(T t) throws RepositoryException;

	// 根据实体ID，删除实体
	public int deleteById(Serializable id, Integer enterpriseId) throws RepositoryException;

	// 根据实体ID，查找实体
	public T getById(Serializable id, Integer enterpriseId);

	// 查询符合查询参数的实体结果集数量
	public int findResultCount(QueryParameters param);

	// 查询符合查询参数的实体结果集
	public List<T> findResults(QueryParameters param);

}

/*
 * Copyright (c) 2015 by XuanWu Wireless Technology Co., Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.db;

import com.xuanwu.mos.domain.Entity;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 基于MyBatis的基本仓储实现
 * 
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2015年5月27日
 * @Version 1.0.0
 */
public abstract class MybatisEntityRepository<T extends Entity> implements EntityRepository<T> {

	protected final static Logger logger = LoggerFactory.getLogger(MybatisEntityRepository.class);

	protected SqlSessionFactory sqlSessionFactory;

	protected abstract void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);

	protected abstract String namesapceForSqlId();

	protected String fullSqlId(String sqlId) {
		return namesapceForSqlId() + "." + sqlId;
	}

	@Override
	public T insert(T t) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.insert(fullSqlId("insert"), t);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Insert Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return t;
	}

	@Override
	public int update(T t) throws RepositoryException {
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ret = session.update(fullSqlId("update"), t);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return ret;
	}

	@Override
	public T save(T t) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int ret = 0;
			if (t.getId() != null) {
				ret = session.update(fullSqlId("update"), t);
			}
			if (ret == 0) {
				ret = session.insert(fullSqlId("insert"), t);
			}
			session.commit(true);
		} catch (Exception e) {
			logger.error("Save/Update Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return t;
	}

	@Override
	public int updateSpecify(T t) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.update(fullSqlId("updateSpecify"), t);
			session.commit(true);
		} catch (Exception e) {
			logger.error("UpdateSpecify Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	@Override
	public int deleteById(Serializable id, Integer enterpriseId) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("id", id);
			params.put("enterpriseId", enterpriseId);
			count = session.delete(fullSqlId("deleteById"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Remove Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	@Override
	public int delete(T t) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.delete(fullSqlId("delete"), t);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Remove Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	@Override
	public T getById(Serializable id, Integer enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("id", id);
			params.put("enterpriseId", enterpriseId);
			return session.selectOne(fullSqlId("getById"), params);
		}
	}

	@Override
	public int findResultCount(QueryParameters params) {
		if (params == null) { // 纠错
			params = new QueryParameters();
		}
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findResultCount"), params);
		}
	}

	@Override
	public List<T> findResults(QueryParameters params) {
		if (params == null) { // 纠错
			params = new QueryParameters();
		}
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findResults"), params);
		}
	}

}

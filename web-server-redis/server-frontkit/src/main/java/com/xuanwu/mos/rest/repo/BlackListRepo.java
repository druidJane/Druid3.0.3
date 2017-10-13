package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.ListMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.BlackList;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangz on 2016/8/16.
 */
@Repository
public class BlackListRepo extends ListMybatisEntityRepository<BlackList> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.BlacklistMapper";
	}

	public Integer addCachePhoneList(BlackList blacklist) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.insert(fullSqlId("addCachePhoneList"), blacklist);
			session.commit(true);
			return blacklist.getId();
		} catch (Exception e) {
			logger.error("addCachePhoneList failed: ", e);
			throw new RepositoryException(e);
		}

	}

	public BlackList findCacheBlacklistById(Integer id) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("id", id);
			return session.selectOne(fullSqlId("findCacheBlacklistById"), map);
		}
	}
	public List<BlackList> findCachePhoneLists(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findCachePhoneLists"), params);
		}
	}

	public int findBlacklistCount(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findBlacklistCount"), params);
		}
	}

	public List<BlackList> findBlacklist(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findBlacklist"), params);
		}
	}

	public long findTeleSegCount(String teleSeg) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findTeleSegCount"),teleSeg);
		}
	}

	public Integer isExist(BlackList blacklist) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findBlacklistExist"),blacklist);
		}
	}

	public void addBlacklist(BlackList blackList) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.insert(fullSqlId("addBlackPhone"),blackList);
		}
	}
}

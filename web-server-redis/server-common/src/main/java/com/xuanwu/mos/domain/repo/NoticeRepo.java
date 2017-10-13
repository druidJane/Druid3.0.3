package com.xuanwu.mos.domain.repo;


import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Notice;
import com.xuanwu.mos.domain.enums.NoticeState;
import com.xuanwu.mos.exception.RepositoryException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengzl 2017-4-20  消息通知
 */
@Repository
public class NoticeRepo extends GsmsMybatisEntityRepository<Notice> {

	
	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.NoticeMapper";
	}


	public List<Integer> getUsersByEnterpriseId(Integer enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("getUsersByEnterpriseId"), enterpriseId);
		}
	}

	public List<Integer> getUsersByPermission(Platform platform, String url, Integer entId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("url", url);
			map.put("entId",entId);
			map.put("platformId",platform.getIndex());
			return session.selectList(fullSqlId("getUsersByPermission"), map);
		}
	}

	public int deleteDetailByIds(List<Integer> ids) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map params = new HashMap<String, Object>();
			params.put("ids", ids);
			count = session.delete(fullSqlId("deleteDetailByIds"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Remove Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public Notice getNoticeByObjectId(String objectId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne("getNoticeByObjectId",objectId);
		}
	}

	public List<Integer> getUsersByMessageId(Integer messageId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList("getUsersByMessageId",messageId);
		}
	}

	public int deleteDetailsByMessageId(Integer messageId) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.delete(fullSqlId("deleteDetailsByMessageId"), messageId);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Remove Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int insertDetail(Integer userId, Integer messageId, Integer state) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, Integer> params = new HashMap<String, Integer>();
			params.put("userId", userId);
			params.put("messageId", messageId);
			params.put("state",state);
			count = session.insert(fullSqlId("insertDetail"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("insert Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int insertDetails(List<Integer> userIds, Integer messageId, Integer state) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
			for (Integer userId : userIds) {
				HashMap<String, Integer> params = new HashMap<String, Integer>();
				params.put("userId", userId);
				params.put("messageId", messageId);
				params.put("state",state);
				count += session.insert(fullSqlId("insertDetail"), params);
			}
			session.commit(true);
		} catch (Exception e) {
			logger.error("insert Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int updateDetails(List<Integer> userIds, Integer messageId, Integer state) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
			for (Integer userId : userIds) {
				HashMap<String, Integer> params = new HashMap<String, Integer>();
				params.put("userId", userId);
				params.put("messageId", messageId);
				params.put("state",state);
				count += session.update(fullSqlId("updateDetail"), params);
			}
			session.commit(true);
		} catch (Exception e) {
			logger.error("update Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int updateStateByIds(List<Integer> ids) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String,Object> param = new HashMap<>();
			param.put("ids",ids);
			count = session.update(fullSqlId("updateStateByIds"), param);
			session.commit(true);
		} catch (Exception e) {
			logger.error("updateStateByIds failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int updateNotice(Integer id, Integer user, String title, Date pushTime) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String,Object> param = new HashMap<>();
			param.put("id",id);
			param.put("createUser",user);
			param.put("title",title);
			param.put("pushTime",pushTime);
			count = session.update(fullSqlId("updateNotice"), param);
			session.commit(true);
		} catch (Exception e) {
			logger.error("updateNotice failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}
}

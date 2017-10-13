package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangz on 2016/8/23.
 */
@Repository
public class FileTaskRepo extends GsmsMybatisEntityRepository<FileTask> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.FileTaskMapper";
	}

	public int updateHandlingTask(FileTask task) {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.update(fullSqlId("updateHandlingTask"), task);
			session.commit(true);
		} catch (Exception e) {
			logger.error("updateHandlingTask failed: ", e);
		}
		return count;
	}

	public List<Integer> findResultCountByUserId(Integer userId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findResultCountByUserId"),userId);
		}
	}

	public int updateReadStateByUserId(Integer userId, Integer taskType) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String,Integer> map = new HashMap<>();
			map.put("userId",userId);
			map.put("taskType",taskType);
			count = session.update(fullSqlId("updateReadStateByUserId"), map);
			session.commit(true);
		} catch (Exception e) {
			logger.error("updateReadStateByUserId failed:"+e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int updateHandledTask(FileTask task) {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.update(fullSqlId("updateHandledTask"), task);
			session.commit(true);
		}
		return count;
	}

	public List<Integer> fetchUnHandledTaskIDs(int platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("fetchUnHandledTaskIDs"), platformId);
		}
	}

	public List<FileTask> fetchUnLoadTasks(List<Integer> unLoadTaskIDs) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			QueryParameters queryParameters = new QueryParameters();
			queryParameters.addParam("taskIDs", unLoadTaskIDs);
			return session.selectList(fullSqlId("fetchUnLoadTasks"), queryParameters);
		}
	}

	public List<FileTask> fetchUnHandledTasks(int platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("fetchUnHandledTasks"), platformId);
		}
	}
}

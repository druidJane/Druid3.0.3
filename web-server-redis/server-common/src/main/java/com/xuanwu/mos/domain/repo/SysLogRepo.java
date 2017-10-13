package com.xuanwu.mos.domain.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.SystemLog;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription
 * @Data 2017-3-30
 * @Version 1.0.0
 */
@Repository
public class SysLogRepo extends GsmsMybatisEntityRepository<SystemLog> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.SysLogMapper";
	}

	public void save(List<SystemLog> sub) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			for (SystemLog systemLog : sub) {
				session.insert(fullSqlId("insert"), systemLog);
			}
			session.commit(true);
		} catch (Exception e) {
			logger.error("Save SystemLog failed: ", e);
			throw new RepositoryException(e);
		}
	}

}

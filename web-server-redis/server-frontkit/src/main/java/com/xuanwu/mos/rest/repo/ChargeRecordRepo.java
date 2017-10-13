package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.ChargeRecord;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription
 * @Data 2017-4-7
 * @Version 1.0.0
 */
@Repository
public class ChargeRecordRepo extends GsmsMybatisEntityRepository<ChargeRecord> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.ChargeRecordMapper";
	}

	public List<ChargeRecord> findFailChargingRecords(QueryParameters parameters) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findFailChargingRecords"), parameters);
		}
	}

	public int updateChargingRecord(ChargeRecord record) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count = session.update(fullSqlId("updateChargingRecord"), record);
			session.commit(true);
			return count;
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}
}

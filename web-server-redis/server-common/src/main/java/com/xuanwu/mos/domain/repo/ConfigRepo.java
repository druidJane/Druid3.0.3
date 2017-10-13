package com.xuanwu.mos.domain.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.ConfigRecord;
import com.xuanwu.mos.domain.enums.GsmsSyncVersionType;
import com.xuanwu.mos.exception.RepositoryException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description ConfigRepo
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
@Repository
public class ConfigRepo extends GsmsMybatisEntityRepository<ConfigRecord> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.ConfigMapper";
	}

	public List<ConfigRecord> findAllConfigs(Integer platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String,Integer> map = new HashMap<>();
			map.put("platformId",platformId);
			return session.selectList(fullSqlId("findAllConfigs"),map);
		}
	}
	
	/**
	 * 获取所有配置项，以map返回
	 */
	public Map<String,ConfigRecord> findAllConfigMap(Integer platformId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String,Integer> map = new HashMap<>();
			map.put("platformId",platformId);
			return session.selectMap(fullSqlId("findAllConfigsForMap"),map,"key");
		}
	}

	public int updateConfig(ConfigRecord config) throws RepositoryException {
		int count = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			count = session.update(fullSqlId("update"), config);
			session.commit(true);
		} catch (Exception e) {
			logger.error("UpdateSpecify Entity failed: ", e);
			throw new RepositoryException(e);
		}
		return count;
	}

	public int findGsmsSyncVersion(GsmsSyncVersionType type) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap<String, GsmsSyncVersionType> map = new HashMap<String, GsmsSyncVersionType>();
			map.put("type", type);
			Integer ret = session.selectOne(fullSqlId("findGsmsSyncVersion"), map);
			if (ret == null) {
				return 0;
			}
			return ret.intValue();
		}
	}

}

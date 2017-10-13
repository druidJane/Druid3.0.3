package com.xuanwu.mos.domain.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Announcement;
import com.xuanwu.mos.exception.RepositoryException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 公告管理
 * @Data 2017-4-1
 * @Version 1.0.0
 */
@Repository
public class AnnouncementRepo extends GsmsMybatisEntityRepository<Announcement> {

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.AnnouncementMapper";
	}

	public Announcement findAnnouncementById(Integer announcementId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findAnnouncementById"), announcementId);
		}
	}


	public int updateByIds(Integer[] ids) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			int count= 0;
			Map<String, Object> map = new HashMap<>();
			map.put("ids", ids);
			count = session.update(fullSqlId("updateByIds"), map);
			session.commit();
			return count;
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

	public Map<Integer, Announcement> findAnnouncementByIds(Integer[] ids) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> map = new HashMap<>();
			map.put("ids", ids);
			return session.selectMap(fullSqlId("findAnnouncementByIds"), map, "id");
		}
	}
}

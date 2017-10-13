package com.xuanwu.mos.rest.repo.statistics;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.BaseStatistics;

import org.springframework.stereotype.Repository;
@Repository
public class BaseStatisticsRepo extends GsmsMybatisEntityRepository<BaseStatistics> {

	@Override
	protected String namesapceForSqlId() {
		// TODO Auto-generated method stub
		return null;
	}
	

}

package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.CarrierTeleseg;

import org.springframework.stereotype.Repository;

/**
 * Created by zhangz on 2017/4/21.
 */
@Repository
public class CarrierTelesegRepo extends GsmsMybatisEntityRepository<CarrierTeleseg> {
	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.cmp.mapper.CarrierTelesegMapper";
	}

}

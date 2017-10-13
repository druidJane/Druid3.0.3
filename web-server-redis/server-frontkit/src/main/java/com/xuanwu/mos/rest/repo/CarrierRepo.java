package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Carrier;
import com.xuanwu.mos.domain.entity.CarrierPrice;
import com.xuanwu.mos.domain.entity.CarrierTeleseg;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.utils.ListUtil;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/3/23.
 */
@Repository
public class CarrierRepo extends GsmsMybatisEntityRepository<Carrier> {
	private static final Logger logger = LoggerFactory.getLogger(CarrierRepo.class);
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.CarrierMapper";
    }
    /**
     * 查询所有的运营商
     *
     * @return
     */
    public List<Carrier> findAllCarrier() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findAllCarrier"), null);
        }
    }
/*	public List<Carrier> findCarriersByChannelId(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findCarriersByChannelId"), params);
		}
	}*/
	public List<Carrier> findCarriersByChannelId(Integer channelId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findCarriersByChannelId"), channelId);
		}
	}

	public Carrier findCarrierByName(String carrierName) {
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.addParam("name", carrierName);
		List<Carrier> carrierList = this.findResults(queryParameters);
		if (ListUtil.isNotBlank(carrierList)) {
			return carrierList.get(0);
		}
		return null;
	}
	public long findTeleSegCount(String teleSeg) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findTeleSegCount"),teleSeg);
		}
	}

	public List<CarrierPrice> findCarrierPrice(int enterpriseId, MsgTicket.MsgType msgType) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("enterpriseId", enterpriseId);
			params.put("msgType", msgType);
			return session.selectList(fullSqlId("findCarrierPrice"), params);
		}
	}

	public List<Carrier> findAllCarriers() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllCarriers"));
		}
	}

	/**
	 * 查找所有运营商号码段
	 *
	 * @return
	 * */
	public List<CarrierTeleseg> findAllCarrierTeleseg() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findAllCarrierTeleseg"));
		}
	}
}

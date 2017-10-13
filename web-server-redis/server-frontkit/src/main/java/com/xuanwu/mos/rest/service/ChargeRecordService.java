package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.ChargeRecord;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.ChargeRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 充值记录
 * @Data 2017-4-7
 * @Version 1.0.0
 */
@Service
public class ChargeRecordService {

	@Autowired
	private ChargeRecordRepo recordRepo;

	public int count(QueryParameters params) {
		return recordRepo.findResultCount(params);
	}

	public List<ChargeRecord> list(QueryParameters params) {
		return recordRepo.findResults(params);
	}

	public List<ChargeRecord> findFailChargingRecords(QueryParameters parameters) {
		return recordRepo.findFailChargingRecords(parameters);
	}

	public int updateChargingRecord(ChargeRecord record) throws RepositoryException {
		return recordRepo.updateChargingRecord(record);
	}
}

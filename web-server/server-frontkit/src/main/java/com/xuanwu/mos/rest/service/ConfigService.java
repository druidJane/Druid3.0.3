package com.xuanwu.mos.rest.service;


import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.ConfigRecord;
import com.xuanwu.mos.domain.enums.GsmsSyncVersionType;
import com.xuanwu.mos.domain.repo.ConfigRepo;
import com.xuanwu.mos.exception.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description ConfigService
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
@Service
public class ConfigService {

	@Autowired
	private ConfigRepo configRepo;

	@Autowired
	private Config config;

	private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);

	public List<ConfigRecord> findAllConfigs(Integer platfomId) {
		return configRepo.findAllConfigs(platfomId);
	}



	public int updateConfig(ConfigRecord config) throws RepositoryException {
		return configRepo.updateConfig(config);
	}

	public int findGsmsSyncVersion(GsmsSyncVersionType type) {
		return configRepo.findGsmsSyncVersion(type);
	}


}

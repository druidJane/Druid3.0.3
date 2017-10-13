/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.cache.dao.impl;

import com.xuanwu.msggate.common.cache.dao.BizDataFetchDao;
import com.xuanwu.msggate.common.cache.dao.mapper.BizDataHandleMapper;
import com.xuanwu.msggate.common.cache.entity.ConfigRecord;
import com.xuanwu.msggate.common.cache.entity.PhoneSegment;
import com.xuanwu.msggate.common.cache.entity.Priority;
import com.xuanwu.msggate.common.sbi.entity.Account;
import com.xuanwu.msggate.common.sbi.entity.BizTypeInfo;
import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.DestBindResult;
import com.xuanwu.msggate.common.sbi.entity.EnterpriseAccessRecord;
import com.xuanwu.msggate.common.sbi.entity.EnterpriseBind;
import com.xuanwu.msggate.common.sbi.entity.Phrase;
import com.xuanwu.msggate.common.sbi.entity.Region;
import com.xuanwu.msggate.common.sbi.entity.RegionCarrier;
import com.xuanwu.msggate.common.sbi.entity.RegionRedirect;
import com.xuanwu.msggate.common.sbi.entity.SpecSVSNumber;
import com.xuanwu.msggate.common.sbi.entity.UserBind;
import com.xuanwu.msggate.common.sbi.entity.WhiteRedirectSpecNum;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-3
 * @Version 1.0.0
 */
public class DefaultBizDataFetchDao implements BizDataFetchDao {
	/**
	 * Session Factory
	 */
	private SqlSessionFactory sessionFactory;

	@Override
	public List<SpecSVSNumber> fetchAllSpecSVSNums() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllSpecSVSNums();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<RegionRedirect> fetchAllRegionRedirects(){
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllRegionRedirects();
		} finally {
			session.close();
		}
	}

	@Override
	public List<CarrierChannel> fetchAllChannels() {
		SqlSession session = sessionFactory.openSession();
		try {

			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllChannels();
		} finally {
			session.close();
		}
	}

	@Override
	public List<RegionCarrier> fetchAllRegionCarriers() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllRegionCarriers();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Region> fetchAllRegions() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllRegions();
		} finally {
			session.close();
		}
	}

	@Override
	public List<EnterpriseBind> fetchAllBindEnterprise() {
		SqlSession session = sessionFactory.openSession();
		try {
			List<EnterpriseBind> entBinds = new ArrayList<EnterpriseBind>();
			BizDataHandleMapper mapper = session.getMapper(BizDataHandleMapper.class);
			entBinds.addAll(mapper.fetchSpecNumEntBind());
			entBinds.addAll(mapper.fetchRegionPriEntBind());
			entBinds.addAll(mapper.fetchRedListEntBind());
			entBinds.addAll(mapper.fetchWhiteRedirectEntBind());
			entBinds.addAll(mapper.fetchSpecNumChangeEntBind());
			return entBinds;
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<UserBind> fetchAllBindUser() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllBindUser();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Priority> fetchAllPrioritys() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllPrioritys();
		} finally {
			session.close();
		}
	}

	@Override
	public List<PhoneSegment> fetchAllPhoneSegments() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllPhoneSegments();
		} finally {
			session.close();
		}
	}

	@Override
	public List<ConfigRecord> fetchAllConfigRecords(int platformID) {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllConfigRecords(platformID);
		} finally {
			session.close();
		}
	}

	@Override
	public List<BizTypeInfo> fetchAllBizTypes() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllBizTypes();
		} finally {
			session.close();
		}
	}

	@Override
	public int fetchVersion() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class).fetchVersion();
		} finally {
			session.close();
		}
	}

	@Override
	public int fetchPriorityVersion() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchPriorityVersion();
		} finally {
			session.close();
		}
	}
	
	@Override
	public int fetchPlatformMode(){
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchPlatformMode();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Integer> fetchAllVersions() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllVersions();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<DestBindResult> fetchAllSpecNumRedirect(){
		SqlSession session = sessionFactory.openSession();
		try {
			List<DestBindResult> destResults = new ArrayList<DestBindResult>();
			BizDataHandleMapper mapper = session.getMapper(BizDataHandleMapper.class);
			destResults.addAll(mapper.fetchAllSpecNumWhiteRedirect());
			destResults.addAll(mapper.fetchAllSpecNumRegionRedirect());
			destResults.addAll(mapper.fetchAllSpecNumChangeRedirect());
			return destResults;
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<DestBindResult> fetchAllChannelRedirect() {
		SqlSession session = sessionFactory.openSession();
		try {
			List<DestBindResult> destResults = new ArrayList<DestBindResult>();
			BizDataHandleMapper mapper = session.getMapper(BizDataHandleMapper.class);
			destResults.addAll(mapper.fetchAllChannelWhiteRedirect());
			destResults.addAll(mapper.fetchAllChannelRegionRedirect());
			destResults.addAll(mapper.fetchAllChannelChangeRedirect());
			return destResults;
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<EnterpriseAccessRecord> fetchAllEntAccessRecord() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class).fetchAllEntAccessRecord();
		} finally {
			session.close();
		}
	}
	
	@Override
	public WhiteRedirectSpecNum fetchGlobalWhiteRedirect() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchGlobalWhiteRedirect();
		} finally {
			session.close();
		}
	}
	
	@Override
	public int getUpdateSuccessVal() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.getUpdateSuccessVal();
		} finally {
			session.close();
		}
	}

	/**
	 * Set sessionFactory
	 * 
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SqlSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Phrase> fetchAllSmsPhrase() {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class).fetchAllSmsPhrase();
					
		} finally {
			session.close();
		}
	}
	
	@Override
	public int updateConfig(ConfigRecord config) {
		SqlSession session = sessionFactory.openSession();
		try {
			int ret = session.getMapper(BizDataHandleMapper.class).updateConfig(config);
			session.commit();
			return ret;	
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Account> fetchAllAuditAccounts(){
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(BizDataHandleMapper.class)
					.fetchAllAuditAccounts();
		} finally {
			session.close();
		}
	}
}

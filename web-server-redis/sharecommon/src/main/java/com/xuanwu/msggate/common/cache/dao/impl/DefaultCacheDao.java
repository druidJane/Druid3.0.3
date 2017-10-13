/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache.dao.impl;

import com.xuanwu.msggate.common.cache.BizHandleRepos.ListType;
import com.xuanwu.msggate.common.cache.dao.CacheDao;
import com.xuanwu.msggate.common.cache.dao.mapper.CacheMapper;
import com.xuanwu.msggate.common.cache.engine.PhoneCache;
import com.xuanwu.msggate.common.sbi.entity.CachePhone;
import com.xuanwu.msggate.common.util.KeyFilter;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-27
 * @Version 1.0.0
 */
public class DefaultCacheDao implements CacheDao {
	
	protected enum DataType{
		PHONE_LIST(0),
		KEYWORD(1),
		REGION_CARRIER(2);
		
		private int index;
		
		private DataType(int index){
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	}
	
	private static final String FETCH_KEY_WORD = "fetchKeyWord";

	private static final String GET_SYNC_VERSION = "getSyncVersion";

	private static final String GET_REGION_CARRIER_MAP = "getRegionCarrierMap";

	private static final String GET_PHONE_LIST = "getPhoneList";

	/**
	 * Max fetch size
	 */
	private int maxFetchSize = 3000000;

	/**
	 * Cache session factory
	 */
	private SqlSessionFactory sessionFactory;

	/**
	 * Set sessionFactory
	 * 
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SqlSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setMaxFetchSize(int maxFetchSize) {
		this.maxFetchSize = maxFetchSize;
	}

	/**
	 * 是否在黑名单/白名单中已存在该号码
	 * 
	 * @param phone
	 * @param listType
	 * @return
	 */
	private Boolean existsPhone(Long phone, ListType listType) {
		SqlSession session = sessionFactory.openSession();
		try {

			int result = session.getMapper(CacheMapper.class).existsPhone(
					phone, listType);

			return result > 0;
		} finally {
			session.close();
		}
	}

	/**
	 * 批量删除关键字
	 * 
	 * @param keywords
	 * @param user
	 * @param keyWordVersion
	 * @param keyFilter
	 */
	@Override
	public void removeKeywords(String[] keywords, String user,
			int keyWordVersion, KeyFilter keyFilter) {
		SqlSession session = sessionFactory.openSession(ExecutorType.BATCH);

		try {
			CacheMapper mapper = session.getMapper(CacheMapper.class);
			for (String keyword : keywords) {
				mapper.updateKeyword(keyword, true);
			}
			session.commit();

			fetchKeyWord(keyWordVersion, keyFilter);
		} finally {
			session.close();
		}
	}

	/**
	 * 批量添加关键字
	 * 
	 * @param keywords
	 * @param user
	 * @param keyWordVersion
	 * @param keyFilter
	 */
	@Override
	public void insertKeywords(String[] keywords, String user,
			int keyWordVersion, KeyFilter keyFilter) {
		SqlSession session = sessionFactory.openSession(ExecutorType.BATCH);

		try {
			// [B.RD20-65][Leasonliang][2010/12/20][start][validate new keywords
			// which already exist in the keyword table]
			CacheMapper mapper = session.getMapper(CacheMapper.class);

			if (keywords.length > 0) {
				List<String> exitKeywordList = mapper
						.validateExistKeywords(keywords);

				for (String keyword : keywords) {
					if (CollectionUtils.contains(exitKeywordList.iterator(),
							keyword)) {
						mapper.updateKeyword(keyword, false);
						continue;
					}
					// [Ticket1][Leasonliang][2010/12/20][start][modify the key
					// retrieve function]
					// Long id = getKeywordNextKey();
					Long id = mapper.getKeywordNextKey();
					// [Ticket1][Leasonliang][2010/12/20][end]
					mapper.insertKeyword(id, keyword, user);
				}

				session.commit();

				fetchKeyWord(keyWordVersion, keyFilter);
			}
		} finally {
			session.close();
		}
	}

	private final String phonePattern = "^\\d{11}$";

	/**
	 * 批量删除黑名单/白名单
	 * 
	 * @param phones
	 * @param listType
	 * @param target
	 * @param user
	 * @param cache
	 */
	@Override
	public void removePhones(String[] phones, ListType listType,
			Integer target, String user, PhoneCache cache) {
		SqlSession session = sessionFactory.openSession(ExecutorType.BATCH);
		CacheMapper mapper = session.getMapper(CacheMapper.class);

		try {
			for (String phone : phones) {
				if (false == phone.matches(phonePattern)) {
					continue;
				}

				long tempPhone = Long.parseLong(phone);

				List<CachePhone> cachePhones = getPhones(tempPhone, listType,
						target);
				if (cachePhones.size() == 1) {

					CachePhone cachePhone = cachePhones.get(0);
					cachePhone.setRemoveId(cachePhone.getId());
					// [Ticket1][Leasonliang][2010/12/20][start][modify get the
					// key of sequence]
					cachePhone.setId(mapper.getPhoneNextKey());
					// cachePhone.setId(getPhoneNextKey());
					// cachePhone.setHandleTime(new Date());
					cachePhone.setHandleTime(Calendar.getInstance().getTime());
					// cachePhone.setRemoved(true);
					cachePhone.setRemoved(Boolean.TRUE);
					// [Ticket1][Leasonliang][2010/12/20][end]
					mapper.insertPhone(cachePhone);

					// 删除缓存
					cache.removePhonePara(cachePhone.getZipmes());
				}
			}

			session.commit();
		} finally {
			session.close();
		}
	}

	/**
	 * 取得白名单/黑名单中的电话列表
	 * 
	 * @param Phone
	 * @param listType
	 * @param target
	 * @return
	 */
	private List<CachePhone> getPhones(Long Phone, ListType listType,
			Integer target) {
		SqlSession session = sessionFactory.openSession();
		try {
			return session.getMapper(CacheMapper.class).getPhones(Phone,
					listType, target);
		} finally {
			session.close();
		}

	}

	/**
	 * Add Phone to DB and Cache
	 */
	@Override
	public void insertPhones(String[] phones, ListType listType,
			Integer target, String user, Boolean removed, Long removeID,
			PhoneCache cache) {
		SqlSession session = sessionFactory.openSession(ExecutorType.BATCH);
		CacheMapper mapper = session.getMapper(CacheMapper.class);
		try {
			int index = listType.getIndex();

			for (String phone : phones) {
				if (false == phone.matches(phonePattern)) {
					continue;
				}

				long tempPhone = Long.parseLong(phone);

				if (existsPhone(tempPhone, listType)) {
					continue;
				}
				// [Ticket1][Leasonliang][2010/12/20][start][modify get the key
				// of sequence]
				// Long nextKey = getPhoneNextKey();
				Long nextKey = mapper.getPhoneNextKey();
				// [Ticket1][Leasonliang][2010/12/20][end]
				Long zipmes = cache.tran2Code(phone, index, target);

				CachePhone cachePhone = new CachePhone();
				cachePhone.setId(nextKey);
				cachePhone.setPhone(tempPhone);
				cachePhone.setType(listType);
				cachePhone.setTarget(target);
				cachePhone.setZipmes(zipmes);
				cachePhone.setUser(user);
				cachePhone.setRemoved(removed);
				cachePhone.setRemoveId(removeID);
				cachePhone.setHandleTime(new Date());

				mapper.insertPhone(cachePhone);
				// 加入缓存
				cache.putPhonePara(zipmes);
			}

			session.commit();
		} finally {
			session.close();
		}

	}
	
	@Override
	public int fetchPhoneList(int currentLastID, PhoneCache cache) {
		// uses two connection for MYSQL because it will hang up on writing to
		// net event when one connection fetch data continuously.
		// this part need to be optimized in future.
		SqlSession session1 = sessionFactory.openSession();
		SqlSession session2 = sessionFactory.openSession();
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;

		String fetchSQL = sessionFactory.getConfiguration().getMappedStatement(
				GET_PHONE_LIST).getSqlSource().getBoundSql(null).getSql();

		int idPoint = currentLastID;
		ResultSet result = null;
		boolean flag = true;
		int tempID = 0;
		try {
			statement1 = session1.getConnection().prepareStatement(fetchSQL,
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			statement2 = session2.getConnection().prepareStatement(fetchSQL,
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			while (true) {
				Date s1 = new Date();
				if (flag) {
					statement1.setInt(1, idPoint);
					statement1.setInt(2, maxFetchSize);
					result = statement1.executeQuery();
					flag = false;
				} else {
					statement2.setInt(1, idPoint);
					statement2.setInt(2, maxFetchSize);
					result = statement2.executeQuery();
					flag = true;
				}

				Date e1 = new Date();

				System.out
						.print("Fetch list time:"
								+ (e1.getTime() - s1.getTime()) + " from id:"
								+ idPoint);

				int count = 0;
				while (result.next()) {
					count++;
					tempID = result.getInt(1);
					idPoint = tempID > idPoint ? tempID : idPoint;
					if (result.getBoolean(3))
						cache.removePhonePara(result.getLong(2));
					else
						cache.putPhonePara(result.getLong(2));
				}
				result.close();

				Date e2 = new Date();
				System.out.println(" to id:" + idPoint + " hash input time:"
						+ (e2.getTime() - e1.getTime()) + " whole time is:"
						+ (e2.getTime() - s1.getTime()) + " end time:" + e2);

				if (count == 0)
					break;
			}

			if (statement1 != null)
				statement1.close();
			if (statement2 != null)
				statement2.close();
		} catch (SQLException e) {
			throw new RuntimeException(
					"Sql exception occurred when fetch phone list!", e);
		} finally {
			session1.close();
			session2.close();
		}
		return idPoint;
	}

	@Override
	public int fetchKeyWord(int currentKeyID, KeyFilter keyFilter) {
		SqlSession session = sessionFactory.openSession();
		String fetchSQL = sessionFactory.getConfiguration().getMappedStatement(
				FETCH_KEY_WORD).getSqlSource().getBoundSql(null).getSql();
		try {
			PreparedStatement statement = session.getConnection()
					.prepareStatement(fetchSQL, ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery();
			keyFilter.clear();
			while (result.next()) {
				String keyword = result.getString(2);
				int type = result.getInt(3);
				int target = result.getInt(4);
				keyFilter.addFilterKeyWord(keyword, type, target);
				currentKeyID = result.getInt(1);
			}
			result.close();
			if (statement != null)
				statement.close();
			return currentKeyID;
		} catch (SQLException e) {
			throw new RuntimeException(
					"Sql exception occurred when fetch key word list!");
		} finally {
			session.close();
		}
	}

	@Override
	public int fetRegionCarrierMap(int currentMapID,
			Map<String, Long> regionCarrierMap) {
		SqlSession session = sessionFactory.openSession();
		String fetchSQL = sessionFactory.getConfiguration().getMappedStatement(
				GET_REGION_CARRIER_MAP).getSqlSource().getBoundSql(null)
				.getSql();

		int idPoint = currentMapID;
		ResultSet result = null;
		int tempID = 0;

		try {
			while (true) {
				PreparedStatement statement = session.getConnection()
						.prepareStatement(fetchSQL,
								ResultSet.TYPE_FORWARD_ONLY,
								ResultSet.CONCUR_READ_ONLY);
				statement.setInt(1, idPoint);
				statement.setInt(2, maxFetchSize);
				result = statement.executeQuery();

				int count = 0;
				while (result.next()) {
					count++;
					tempID = result.getInt(1);
					idPoint = tempID > idPoint ? tempID : idPoint;
					if (result.getBoolean(5))
						regionCarrierMap.remove(result.getString(2));
					else {
						regionCarrierMap.put(result.getString(2),
								((long) result.getInt(3)) << 32
										| result.getInt(4));
					}
				}
				result.close();
				if (statement != null)
					statement.close();

				if (count == 0)
					break;
			}
		} catch (SQLException e) {
			throw new RuntimeException(
					"Sql exception occurred when fetch carrier map!");
		} finally {
			session.close();
		}
		return idPoint;
	}

	@Override
	public List<Integer> fetchSyncVersion() {
		SqlSession session = sessionFactory.openSession();
		String fetchSQL = sessionFactory.getConfiguration().getMappedStatement(
				GET_SYNC_VERSION).getSqlSource().getBoundSql(null).getSql();

		List<Integer> temp = new ArrayList<Integer>();

		try {
			PreparedStatement statement = session.getConnection()
					.prepareStatement(fetchSQL, ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				temp.add(result.getInt(1));
			}
			result.close();
			if (statement != null)
				statement.close();
			return temp;
		} catch (SQLException e) {
			throw new RuntimeException(
					"Sql exception occurred when fetch sync version!");
		} finally {
			session.close();
		}
	}
	
//	[mos2.0-115][Leason][2012/04/12][start]
	@Override
	public void insertKeywordEffectTime(long maxId) {
		SqlSession session = sessionFactory.openSession();
		try {
			session.getMapper(CacheMapper.class).insertSyncEffectTime(DataType.KEYWORD.getIndex(), 1, maxId);
			session.commit();
		} finally {
			session.close();
		}
		
	}

	@Override
	public void insertPhoneListEffectTime(long minId, long maxId) {
		SqlSession session = sessionFactory.openSession();
		try {
			session.getMapper(CacheMapper.class).insertSyncEffectTime(DataType.PHONE_LIST.getIndex(), minId, maxId);
			session.commit();
		} finally {
			session.close();
		}
		
	}

	@Override
	public void insertRegionCarrierMapEffectTime(long minId, long maxId) {
		SqlSession session = sessionFactory.openSession();
		try {
			session.getMapper(CacheMapper.class).insertSyncEffectTime(DataType.REGION_CARRIER.getIndex(), minId, maxId);
			session.commit();
		} finally {
			session.close();
		}
		
	}

	@Override
	public void updateKeywordEffectTime(long maxId) {
		SqlSession session = sessionFactory.openSession();
		try {
			session.getMapper(CacheMapper.class).updateKeywordEffectTime(maxId);
			session.commit();
		} finally {
			session.close();
		}		
	}

	@Override
	public void clearSyncEffectTime() {
		SqlSession session = sessionFactory.openSession();
		try {
			session.getMapper(CacheMapper.class).clearSyncEffectTime();
		} finally {
			session.close();
		}
		
	}	
//	[mos2.0-115][Leason][2012/04/12][end]
}

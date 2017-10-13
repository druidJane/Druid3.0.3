/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.cache.impl;

import com.xuanwu.msggate.common.cache.SpecSVSRepos;
import com.xuanwu.msggate.common.cache.SyncTask;
import com.xuanwu.msggate.common.cache.dao.BizDataFetchDao;
import com.xuanwu.msggate.common.cache.entity.PhoneSegment;
import com.xuanwu.msggate.common.core.Config;
import com.xuanwu.msggate.common.sbi.entity.AbstractDestBind;
import com.xuanwu.msggate.common.sbi.entity.Account;
import com.xuanwu.msggate.common.sbi.entity.BindSpecNumResult;
import com.xuanwu.msggate.common.sbi.entity.BizTypeInfo;
import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.sbi.entity.CarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.DestBindCarrierChannel;
import com.xuanwu.msggate.common.sbi.entity.DestBindResult;
import com.xuanwu.msggate.common.sbi.entity.DestBindSpecNum;
import com.xuanwu.msggate.common.sbi.entity.EnterpriseAccessRecord;
import com.xuanwu.msggate.common.sbi.entity.EnterpriseBind;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.RedSpecNum;
import com.xuanwu.msggate.common.sbi.entity.RedSpecNumResult;
import com.xuanwu.msggate.common.sbi.entity.Region;
import com.xuanwu.msggate.common.sbi.entity.RegionCarrier;
import com.xuanwu.msggate.common.sbi.entity.SpecSVSNumber;
import com.xuanwu.msggate.common.sbi.entity.SpecSVSNumber.SpecNumType;
import com.xuanwu.msggate.common.sbi.entity.UserBind;
import com.xuanwu.msggate.common.sbi.entity.VirtualChannelNumMap;
import com.xuanwu.msggate.common.sbi.entity.WhiteRedirectSpecNum;
import com.xuanwu.msggate.common.util.ListUtil;
import com.xuanwu.msggate.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of the special service number
 * 
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-8-2
 * @Version 1.0.0
 */
public class SpecSVSReposImpl implements SpecSVSRepos, SyncTask {

	/**
	 * Special number tree read write lock
	 */
	ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * Phone segments read write lock
	 */
	ReadWriteLock phoneSegLock = new ReentrantReadWriteLock();

	ReadWriteLock userLock = new ReentrantReadWriteLock();

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(SpecSVSReposImpl.class);

	/**
	 * Special service number tree loaded version
	 */
	private int version = -1;

	private int phoneSegVersion = -1;

	private int userVersion = -1;

	/**
	 * Business data fetch dao
	 */
	private BizDataFetchDao dao;
	/**
	 * Repository for the special service number
	 */
	Map<Integer, SpecSVSNumber> numRepos = new HashMap<Integer, SpecSVSNumber>();

	Map<String, SpecSVSNumber> moNumRepos = new HashMap<String, SpecSVSNumber>();

	/**
	 * Repository of region
	 */
	Map<Integer, Region> regionRepos = new HashMap<Integer, Region>();

	/**
	 * Repository of channel
	 */
	Map<Integer, CarrierChannel> channelRepos = new HashMap<Integer, CarrierChannel>();

	/**
	 * Repository of channel
	 */
	Map<String, CarrierChannel> channelIdentityRepos = new HashMap<String, CarrierChannel>();

	/**
	 * Repository BizTypeInfo
	 */
	Map<Integer, BizTypeInfo> bizTypeRepos = new HashMap<Integer, BizTypeInfo>();

	/**
	 * Root special service numbers array<br/>
	 * used to constructor tree
	 */
	private List<SpecSVSNumber> rootSpecNums = new ArrayList<SpecSVSNumber>();

	/**
	 * Virtual root special service numbers
	 */
	private List<SpecSVSNumber> virtualRootSpecNums = new ArrayList<SpecSVSNumber>();

	private Map<String, Carrier> phoneSegMap = new HashMap<String, Carrier>();

	private Map<String, UserBind> userBindMap = new HashMap<String, UserBind>();
	private Map<Integer, String> entIdentifyMap = new HashMap<Integer, String>();
	private Map<String, UserBind> matchedUserMap = new ConcurrentHashMap<String, UserBind>();
	private Map<String, EnterpriseAccessRecord> entAccessMap = new ConcurrentHashMap<String, EnterpriseAccessRecord>();
	private Map<Integer, Integer> entAdminMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> actualEntAdminMap = new HashMap<Integer, Integer>();
	private Map<Integer, Account> auditAccountMap = new HashMap<Integer, Account>();
	private WhiteRedirectSpecNum globalWhiteRedirect;
	
	private boolean loadMoUser = false;

	/**
	 * Get numRepos
	 * 
	 * @return the numRepos
	 */
	public Map<Integer, SpecSVSNumber> getRepos() {
		return numRepos;
	}

	@Override
	public SpecSVSNumber getSpecNumberByID(Integer id) {
		lock.readLock().lock();
		try {
			return numRepos.get(id);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Region getRegionByID(Integer id) {
		lock.readLock().lock();
		try {
			return regionRepos.get(id);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public BizTypeInfo getBizType(Integer id) {
		lock.readLock().lock();
		try {
			return bizTypeRepos.get(id);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * synchronized the special number
	 */
	public void syncService() {
		logger.info("Begin to fetch all version...");
		List<Integer> vers = dao.fetchAllVersions();
		logger.info(
				"Begin to sync the special service number tree, current version[{}], new version[{}]...",
				version, vers.get(0));
		syncSpecNum(vers.get(0));
		logger.info("End to sync the special service number tree.");

		logger.info(
				"Begin to sync the phone segments, current version[{}], new version[{}]...",
				phoneSegVersion, vers.get(1));
		syncPhoneSegs(vers.get(1));
		logger.info("End to sync the phone segments.");

		logger.info(
				"Begin to sync the users, current version [{}], new version[{}]...",
				userVersion, vers.get(4));
		syncUsers(vers.get(4));
		syncAuditUsers(vers.get(4));
		logger.info("End to sync the users.");
	}

	protected void syncSpecNum(int newVersion) {
		lock.writeLock().lock();
		try {
			if (!(newVersion > version))
				return;

			// TODO: This point need to be optimized! pay attention on the size
			// of
			// the special service numbers
			// Load special number
			Map<Integer, SpecSVSNumber> tempNumRepos = new HashMap<Integer, SpecSVSNumber>();
			Map<String, SpecSVSNumber> tempMoNumRepos = new HashMap<String, SpecSVSNumber>();
			List<SpecSVSNumber> specs = dao.fetchAllSpecSVSNums();
			for (SpecSVSNumber numb : specs) {
				tempNumRepos.put(numb.getId(), numb);
			}

			// Load bind of special number with enterprise, with basicNumber
			List<EnterpriseBind> binds = dao.fetchAllBindEnterprise();
			for (EnterpriseBind bind : binds) {
				SpecSVSNumber specSVSNumber = tempNumRepos.get(bind
						.getSpecNumID());
				if (specSVSNumber != null) {
					specSVSNumber.addBindEnterprises(bind);
				}
			}

			// Load channels
			Map<Integer, CarrierChannel> tempChannelRepos = new HashMap<Integer, CarrierChannel>();
			Map<String, CarrierChannel> tempChannelIdentityRepos = new HashMap<String, CarrierChannel>();
			List<CarrierChannel> channels = dao.fetchAllChannels();
			for (CarrierChannel carrierChannel : channels) {
				tempChannelRepos.put(carrierChannel.getId(), carrierChannel);
				if (carrierChannel.isNotChildChannel())
					tempChannelIdentityRepos.put(carrierChannel.getIdentity(),
							carrierChannel);
			}

			// Load region carriers
			Map<Integer, RegionCarrier> tempRegionCarrierRepos = new HashMap<Integer, RegionCarrier>();
			List<RegionCarrier> regionCarriers = dao.fetchAllRegionCarriers();
			for (RegionCarrier regionCarrier : regionCarriers) {
				tempRegionCarrierRepos
						.put(regionCarrier.getId(), regionCarrier);
			}

			// Load regions
			Map<Integer, Region> tempRegionRepos = new HashMap<Integer, Region>();
			List<Region> regions = dao.fetchAllRegions();
			for (Region regionCode : regions) {
				tempRegionRepos.put(regionCode.getId(), regionCode);
			}

			// Load bizTypes
			Map<Integer, BizTypeInfo> tempBizTypeRepos = new HashMap<Integer, BizTypeInfo>();
			List<BizTypeInfo> bizTypes = dao.fetchAllBizTypes();
			for (BizTypeInfo bizType : bizTypes) {
				tempBizTypeRepos.put(bizType.getId(), bizType);
			}

			List<SpecSVSNumber> tempRootSpecNums = new ArrayList<SpecSVSNumber>();
			List<SpecSVSNumber> tempVirtualRootSpecNums = new ArrayList<SpecSVSNumber>();

			for (SpecSVSNumber numb : specs) {
				SpecSVSNumber parent = tempNumRepos.get(numb.getParentID());
				if (parent != null) {
					numb.setParent(parent);
					parent.getSubNums().add(numb);
				}
				numb.setCarrierChannel(tempChannelRepos.get(numb.getChannelID()));
				if (numb.getNumType() == SpecNumType.ROOT)
					tempRootSpecNums.add(numb);
				if (numb.getNumType() == SpecNumType.VRITUAL_ROOT)
					tempVirtualRootSpecNums.add(numb);
			}

			for (CarrierChannel channel : channels) {
				channel.setRegionCarrier(tempRegionCarrierRepos.get(channel
						.getRegionCarrierID()));
				List<VirtualChannelNumMap> vituals = channel.getVirtualSpecs();
				for (VirtualChannelNumMap map : vituals) {
					map.setSpecNum(tempNumRepos.get(map.getSpecNumID()));
					if (map.getSpecNum() != null) {
						map.getSpecNum().addVirtualChannel(channel);
					}
				}
				channel.confMapChannels();
				CarrierChannel parent = tempChannelRepos.get(channel
						.getParentId());
				if (parent != null) {
					channel.setParent(parent);
				}
			}

			for (RegionCarrier carrier : regionCarriers) {
				carrier.setRegion(tempRegionRepos.get(carrier.getRegionID()));
			}

			// just for MO
			for (SpecSVSNumber numb : specs) {
				if (numb.getParentID() == null) // root number ignore
					continue;
				SpecSVSNumber temp = tempMoNumRepos.get(numb.getBasicNumber());
				if (temp != null) {
					for (EnterpriseBind bind : numb.getBindEnterprises()) {
						temp.addBindEnterprises(bind);
					}
					// tempNameNumRepos.put(numb.getBasicNumber(), null);
				} else {
					tempMoNumRepos.put(numb.getBasicNumber(), numb);
				}
			}

			// Load all dest bind SpecNum
			List<DestBindResult> destSpecNumResults = dao
					.fetchAllSpecNumRedirect();
			List<AbstractDestBind> destBinds = new ArrayList<AbstractDestBind>();
			for (DestBindResult dest : destSpecNumResults) {
				SpecSVSNumber specSVSNum = tempNumRepos.get(dest.getSourceID());
				if (specSVSNum != null) {
					DestBindSpecNum destSpecNum = specSVSNum
							.getDestBindSpecNum();
					if (destSpecNum == null) {
						destSpecNum = new DestBindSpecNum();
						specSVSNum.setDestBindSpecNum(destSpecNum);
						destBinds.add(destSpecNum);
					}

					for (BindSpecNumResult result : dest.getResults()) {
						result.setSpecSVSNum(tempNumRepos.get(result
								.getSpecsvsNumID()));
					}

					destSpecNum.setSrcSpecNumdID(dest.getSourceID());
					destSpecNum.setDestResult(dest);
				}
			}

			// Load all channel dest bind spec num
			List<DestBindResult> destChannelResults = dao
					.fetchAllChannelRedirect();
			for (DestBindResult dest : destChannelResults) {
				CarrierChannel carrierChannel = tempChannelRepos.get(dest
						.getSourceID());
				if (carrierChannel != null) {
					DestBindCarrierChannel destChannel = carrierChannel
							.getDestBindChannel();
					if (destChannel == null) {
						destChannel = new DestBindCarrierChannel();
						carrierChannel.setDestBindChannel(destChannel);
						destBinds.add(destChannel);
					}
					for (BindSpecNumResult result : dest.getResults()) {
						result.setSpecSVSNum(tempNumRepos.get(result
								.getSpecsvsNumID()));
					}
					destChannel.setDestResult(dest);
					destChannel.setSrcChannelId(dest.getSourceID());
				}
			}

			for (AbstractDestBind dest : destBinds) {
				dest.initMap();
			}

			// load once is OK
			if (entAccessMap.isEmpty()) {
				List<EnterpriseAccessRecord> tempEntAccessList = dao
						.fetchAllEntAccessRecord();
				for (EnterpriseAccessRecord entAccess : tempEntAccessList) {
					entAccessMap.put(entAccess.getKey(), entAccess);
				}
			}

			WhiteRedirectSpecNum tempRedirect = dao.fetchGlobalWhiteRedirect();
			if (tempRedirect != null && tempRedirect.getResults() != null) {
				for (BindSpecNumResult result : tempRedirect.getResults()) {
					result.setSpecSVSNum(tempNumRepos.get(result
							.getSpecsvsNumID()));
				}
				if (globalWhiteRedirect == null) {
					globalWhiteRedirect = tempRedirect;
				} else {
					if (!equalsResults(tempRedirect.getResults(),
							globalWhiteRedirect.getResults()))
						globalWhiteRedirect = tempRedirect;
				}
			}

			version = newVersion;

			numRepos = tempNumRepos;
			moNumRepos = tempMoNumRepos;
			regionRepos = tempRegionRepos;
			channelRepos = tempChannelRepos;
			bizTypeRepos = tempBizTypeRepos;
			rootSpecNums = tempRootSpecNums;
			virtualRootSpecNums = tempVirtualRootSpecNums;
			channelIdentityRepos = tempChannelIdentityRepos;
			matchedUserMap.clear();
			logger.info(
					"Sync the special service number tree success, update to version[{}]",
					version);
		} catch (Exception e) {
			logger.error(
					"Sync the special service number tree failed, cause by:", e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	protected void syncPhoneSegs(int newVersion) {
		phoneSegLock.writeLock().lock();
		try {
			if (!(newVersion > phoneSegVersion))
				return;
			phoneSegVersion = newVersion;
			List<PhoneSegment> phoneSegments = dao.fetchAllPhoneSegments();
			phoneSegMap.clear();

			if (ListUtil.isBlank(phoneSegments))
				return;

			for (PhoneSegment seg : phoneSegments) {
				phoneSegMap.put(seg.getPhoneSegment(), seg.getCarrier());
			}
			logger.info(
					"Sync the phone segments success, update to version[{}]",
					phoneSegVersion);
		} catch (Exception e) {
			logger.error("Sync the phone segments failed, cause by:", e);
		} finally {
			phoneSegLock.writeLock().unlock();
		}
	}

	/**
	 * Just load users's id and identify for MO
	 * 
	 * @param newVersion
	 */
	protected void syncUsers(int newVersion) {
		userLock.writeLock().lock();
		try {
			if (!(newVersion > userVersion)) {
				return;
			}
			Map<String, UserBind> userBindMap = new HashMap<String, UserBind>();
			Map<Integer, String> entIdentifyMap = new HashMap<Integer, String>();
			Map<String, Integer> entIdMap = new HashMap<String, Integer>();
			Map<Integer, Integer> entAdminMap = new HashMap<Integer, Integer>();
			Map<Integer, Integer> acutalEntAdminMap = new HashMap<Integer, Integer>();
			Set<Integer> entIdSet = new HashSet<Integer>();
			// Load user's identify and id
			List<UserBind> binds = dao.fetchAllBindUser();
			String key = null;
			Integer entID = null;
			for (UserBind bind : binds) {
				entIdentifyMap.put(bind.getUserID(), bind.getEntIdentify());
				if (bind.getParentID() == bind.getEntID()) {// admin user
					entAdminMap.put(bind.getEntID(), bind.getUserID());
				}
			}

			for (UserBind bind : binds) {
				// duplicate enterprise's identify
				entID = entIdMap.get(bind.getEntIdentify());
				if (entID == null) {
					entIdMap.put(bind.getEntIdentify(), bind.getEntID());
				} else if (entID != bind.getEntID()) {
					entIdSet.add(entID);
					entIdSet.add(bind.getEntID());
				}

				if (StringUtil.isBlank(bind.getUserIdentify())) {// set to admin
																	// user id
					bind.setUserID(entAdminMap.get(bind.getEntID()));
				}
				key = bind.getEntIdentify() + bind.getUserIdentify();
				UserBind temp = userBindMap.get(key);
				if (temp == null) {
					userBindMap.put(key, bind);
				} else if (temp.getEntID() == bind.getEntID()) {
					// duplicate user's identify, set to admin user id
					temp.setUserID(entAdminMap.get(bind.getEntID()));
					acutalEntAdminMap.put(bind.getEntID(), bind.getUserID());
				}
			}
			for (UserBind bind : userBindMap.values()) {
				if (entIdSet.contains(bind.getEntID())) {
					bind.setEntIdentifyExist(true);
				}
			}

			this.userVersion = newVersion;
			this.userBindMap = userBindMap;
			this.entIdentifyMap = entIdentifyMap;
			this.entAdminMap = entAdminMap;
			this.actualEntAdminMap = acutalEntAdminMap;
			logger.info("Sync the users success, update to version[{}]",
					userVersion);
		} catch (Exception e) {
			logger.error("Sync the users failed, cause by:", e);
		} finally {
			userLock.writeLock().unlock();
		}
	}

	@Override
	public Carrier getCarrierType(String phone) {
		if (phone == null || phone.length() < 4) {
			return null;
		}
		phoneSegLock.readLock().lock();
		try {
			Carrier carrier = phoneSegMap.get(phone.substring(0, 4));
			if (carrier == null) {
				carrier = phoneSegMap.get(phone.substring(0, 3));
				if (carrier != null && carrier == Carrier.TELECOM) {
					if (phone.length() > 11) {
						carrier = null;
					}
				}
			}
			return carrier;
		} finally {
			phoneSegLock.readLock().unlock();
		}
	}

	@Override
	public UserBind getUserByIdentify(String identify) {
		userLock.readLock().lock();
		try {
			return userBindMap.get(identify);
		} finally {
			userLock.readLock().unlock();
		}
	}

	@Override
	public String getUserById(Integer userID) {
		userLock.readLock().lock();
		try {
			String identify = entIdentifyMap.get(userID);
			return (identify == null) ? "" : identify.trim();
		} finally {
			userLock.readLock().unlock();
		}
	}

	@Override
	public int getAdminID(int entID) {
		userLock.readLock().lock();
		try {
			Integer adminID = entAdminMap.get(entID);
			return adminID == null ? 0 : adminID;
		} finally {
			userLock.readLock().unlock();
		}
	}

	protected void syncAuditUsers(int newVersion){
		if(loadMoUser){
			return;
		}
		if(!(newVersion > userVersion)){
			return;
		}
		userLock.writeLock().lock();
		try{
			if(!(newVersion > userVersion)){
				return;
			}
			Map<Integer, Account> accountMap = new HashMap<Integer, Account>();
			List<Account> accounts = dao.fetchAllAuditAccounts();
			for(Account account : accounts){
				accountMap.put(Integer.valueOf(account.getId()), account);
			}
			this.auditAccountMap = accountMap;
			logger.info("Sync the audit users success, update to version[{}]", userVersion);
		} catch (Exception e){
			logger.error("Sync the audit users failed, cause by:", e);
		} finally {
			userLock.writeLock().unlock();
		}
	}
	
	@Override
	public BindSpecNumResult getGlobalWhiteRedirect(MsgSingle msg) {
		lock.readLock().lock();
		try {
			if (globalWhiteRedirect == null)
				return null;
			return globalWhiteRedirect.getRedirectResult(msg);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	public int getActualAdminID(int entID){
		userLock.readLock().lock();
		try{
			Integer adminID = actualEntAdminMap.get(entID);
			return adminID == null ? 0 : adminID;
		} finally {
			userLock.readLock().unlock();
		}
	}
	
	@Override
	public Account getAuditAccount(Integer id){
		userLock.readLock().lock();
		try{
			return auditAccountMap.get(id);
		} finally {
			userLock.readLock().unlock();
		}
	}

	public void setDao(BizDataFetchDao dao) {
		this.dao = dao;
	}

	@Override
	public void run() {
		try {
			syncService();
		} catch (Exception e) {
			logger.error("Sync SpecNum exception ocurred: ", e);
		}
	}

	@Override
	public int getVersion() {
		lock.readLock().lock();
		try {
			return version;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public SpecSVSNumber getSpecNumByNumer(Carrier carrier, String specNumber) {
		if (specNumber == null) {
			return null;
		}
		lock.readLock().lock();
		try {
			return moNumRepos.get(specNumber);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void syncSpecNumTree() {
		syncService();
	}

	@Override
	public List<SpecSVSNumber> getRootSpecNums() {
		return rootSpecNums;
	}

	@Override
	public CarrierChannel getChannelById(Integer channelId) {
		return channelRepos.get(channelId);
	}

	@Override
	public CarrierChannel getChannelByIdentity(String identity) {
		return channelIdentityRepos.get(identity);
	}

	@Override
	public Map<String, CarrierChannel> getChannelInfo() {
		return channelIdentityRepos;
	}

	@Override
	public List<SpecSVSNumber> getVirtualRootSpecNums() {
		return virtualRootSpecNums;
	}

	@Override
	public Map<String, UserBind> getMoMatchResultMap() {
		return matchedUserMap;
	}

	@Override
	public Map<String, EnterpriseAccessRecord> getEnterpriseAccessMap() {
		return entAccessMap;
	}

	private boolean equalsResults(List<BindSpecNumResult> results1,
			List<BindSpecNumResult> results2) {
		if (results1 == null || results2 == null)
			return false;
		if (results1.size() != results2.size())
			return false;
		for (int i = 0; i < results1.size(); i++) {
			if (!results1.get(i).equals(results2.get(i)))
				return false;
		}
		return true;
	}

	@Override
	public long getPeriod() {
		return Config.specNumSyncDelay;
	}
	
	public void setLoadMoUser(boolean loadMoUser) {
		this.loadMoUser = loadMoUser;
	}

	@Override
	public void syncAccountBizData(Account account) {
		final Map<Integer, Region> regionRepos;
		final Map<Integer, SpecSVSNumber> numRepos;
		final Map<Integer, BizTypeInfo> bizTypeRepos;
		// make sure the account bizData in the same version
		lock.readLock().lock();
		try{
			regionRepos = this.regionRepos;
			numRepos = this.numRepos;
			bizTypeRepos = this.bizTypeRepos;
		} finally {
			lock.readLock().unlock();
		}
		
		// synchronized the bind special service number state
		List<BindSpecNumResult> bindResults = account.getBindSpecNum()
				.getResults();
		for (BindSpecNumResult bind : bindResults) {
			bind.setRegion(regionRepos.get(bind.getRegionID()));
			bind.setSpecSVSNum(numRepos.get(bind.getSpecsvsNumID()));
			bind.setBizInfo(bizTypeRepos.get(bind.getBizType()));
		}

		// synchronized the red special service number state
		RedSpecNum redSpec = account.getParent().getRedSpecNum();
		if (redSpec == null) {
			return;
		}
		for (RedSpecNumResult result : redSpec.getResults()) {
			result.setSpecSVSNum(numRepos.get(result.getSpecsvsNumID()));
		}
		account.initBindResult(bizTypeRepos.get(account.getDefaultBizTypeId()));
	}
}

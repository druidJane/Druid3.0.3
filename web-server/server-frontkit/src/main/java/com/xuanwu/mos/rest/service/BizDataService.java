package com.xuanwu.mos.rest.service;


import com.xuanwu.mos.config.SyncTask;
import com.xuanwu.mos.domain.entity.Carrier;
import com.xuanwu.mos.domain.entity.CarrierTeleseg;
import com.xuanwu.mos.domain.entity.GsmsUser;
import com.xuanwu.mos.domain.enums.GsmsSyncVersionType;
import com.xuanwu.mos.domain.repo.ConfigRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.CarrierRepo;
import com.xuanwu.mos.rest.repo.CarrierTelesegRepo;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.msggate.common.cache.CacheObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

/**
 * Created by 林泽强 on 2016/9/12.
 */
@Component("bizDataService")
public class BizDataService implements SyncTask {
	
	private Logger logger = LoggerFactory.getLogger(BizDataService.class);

	@Autowired
	private CarrierRepo carrierRepo;

	@Autowired
	private CarrierTelesegRepo carrierTelesegRepo;

	@Autowired
	private ConfigRepo configRepo;
	@Autowired
	private UserRepo userRepo;

	private HashMap<Integer, Carrier> carrierMap = new HashMap<Integer, Carrier>();

	private Map<String, Carrier> phoneSegMap = new HashMap<String, Carrier>();

	private int phoneSegVer = -1;

	private ReentrantReadWriteLock phoneSegLock = new ReentrantReadWriteLock();
	
	private Map<Integer, CacheObject<Map<Integer, GsmsUser>>> entMap = new HashMap<Integer, CacheObject<Map<Integer, GsmsUser>>>();

	private long period = 10 * 60 * 1000;
	
	private volatile int userVer = -1;
	
	private ReentrantReadWriteLock userLock = new ReentrantReadWriteLock();

	@PostConstruct
	public void init() {
		QueryParameters params = new QueryParameters();
		List<Carrier> carriers = carrierRepo.findResults(params);
		for (Carrier carrier : carriers) {
			carrierMap.put(carrier.getId(), carrier);
		}
		sync();
	}

	@Override
	public void run() {
		sync();
	}

	public void sync() {
		try {
			syncPhoneSeg();
		} catch (Exception e) {
			logger.error("Sync phone segments fail, ", e);
		}
	}

	public void syncPhoneSeg() {
		int ver = configRepo.findGsmsSyncVersion(GsmsSyncVersionType.PHONE_SEG);
		logger.info("Begin to sync phone segments, current version[{}], new version[{}]...", phoneSegVer, ver);
		if (phoneSegVer >= ver) {
			logger.info("End to sync phone segments.");
			return;
		}
		phoneSegLock.writeLock().lock();
		try {
			if (phoneSegVer >= ver) {
				logger.info("End to sync phone segments.");
				return;
			}
			QueryParameters params = new QueryParameters();
			List<CarrierTeleseg> phoneSegs = carrierTelesegRepo.findResults(params);
			Map<String, Carrier> phoneSegMap = new HashMap<String, Carrier>();
			for (CarrierTeleseg phoneSeg : phoneSegs) {
				phoneSegMap.put(phoneSeg.getPhone(), phoneSeg.getCarrier());
			}
			this.phoneSegMap = phoneSegMap;
			phoneSegVer = ver;
			logger.info("Sync phone segments to version [{}]", ver);
		} finally {
			phoneSegLock.writeLock().unlock();
		}
		logger.info("End to sync phone segments.");
	}
	
	public void syncUser(){
		int ver = configRepo.findGsmsSyncVersion(GsmsSyncVersionType.USER);
		logger.info("Begin to sync user, current version[{}], new version[{}]...", userVer, ver);
		if(userVer >= ver){
			logger.info("End to sync user.");
			return;
		}
		userLock.writeLock().lock();
		try{
			if(userVer >= ver){
				logger.info("End to sync user.");
				return;
			}
			Iterator<Map.Entry<Integer, CacheObject<Map<Integer, GsmsUser>>>> it = entMap.entrySet().iterator();
			while(it.hasNext()){
				CacheObject<Map<Integer, GsmsUser>> obj = it.next().getValue();
				if(System.currentTimeMillis() > obj.getDeadline()){
					it.remove();
				}
			}
			userVer = ver;
			logger.info("Sync user to version [{}]", ver);
		} finally {
			userLock.writeLock().unlock();
		}
		logger.info("End to sync user.");
	}
	private long entCacheTime = 3600;//1 hour
	private CacheObject<Map<Integer, GsmsUser>> loadGsmsUsers(int entId){
		userLock.writeLock().lock();
		try{
			CacheObject<Map<Integer, GsmsUser>> obj = entMap.get(entId);
			if(obj != null && obj.getVersion() >= userVer){
				return obj;
			}
			List<GsmsUser> depts = new ArrayList<GsmsUser>();
			GsmsUser ent = userRepo.findGsmsUser(entId);
			if(ent != null){
				depts.add(ent);
			}
			List<GsmsUser> ret = userRepo.findAllEntDepts(entId);
			if(ListUtil.isNotBlank(ret)){
				depts.addAll(ret);
			}
			Map<Integer, GsmsUser> map = new LinkedHashMap<Integer, GsmsUser>();
			obj = new CacheObject<Map<Integer, GsmsUser>>(map);
			for(GsmsUser dept : depts){
				map.put(dept.getId(), dept);
			}
			obj.setAttachment(depts);
			obj.setAliveTime(entCacheTime, TimeUnit.SECONDS);
			obj.setVersion(userVer);
			entMap.put(entId, obj);
			return obj;
		} finally {
			userLock.writeLock().unlock();
		}
	}
	
	public GsmsUser getDeptById(int entId, int id){
		CacheObject<Map<Integer, GsmsUser>> obj;
		userLock.readLock().lock();
		try{
			obj = entMap.get(entId);
			if(obj != null && obj.getVersion() >= userVer){
				return obj.getObject().get(id);
			}
		} finally {
			userLock.readLock().unlock();
		}
		obj = loadGsmsUsers(entId);
		return obj == null ? null : obj.getObject().get(id);
	}
	public Carrier getCarrierType(String phone) {
		if (phone == null || phone.length() < 4) {
			return null;
		}
		phoneSegLock.readLock().lock();
		try {
			Carrier carrier = phoneSegMap.get(phone.substring(0, 4));
			if (carrier == null) {
				carrier = phoneSegMap.get(phone.substring(0, 3));
			}
			return carrier;
		} finally {
			phoneSegLock.readLock().unlock();
		}
	}
	public List<GsmsUser> getDepts(int entId){
		CacheObject<Map<Integer, GsmsUser>> obj;
		userLock.readLock().lock();
		try{
			obj = entMap.get(entId);
			if(obj != null && obj.getVersion() >= userVer){
				return (List<GsmsUser>)obj.getAttachment();
			}
		} finally {
			userLock.readLock().unlock();
		}
		obj = loadGsmsUsers(entId);
		return obj == null ? null : (List<GsmsUser>)obj.getAttachment();
	}

	public Carrier getCarrierById(int id) {
		return carrierMap.get(id);
	}
	@Override
	public long getPeriod() {
		return period;
	}
}

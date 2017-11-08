package com.xuanwu.mos.service;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.CacheObject;
import com.xuanwu.mos.domain.entity.Carrier;
import com.xuanwu.mos.domain.entity.CarrierTeleseg;
import com.xuanwu.mos.domain.entity.GsmsUser;
import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.domain.entity.Region;
import com.xuanwu.mos.domain.entity.RolePermission;
import com.xuanwu.mos.domain.enums.GsmsSyncVersionType;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.domain.repo.RoleRepo;
import com.xuanwu.mos.domain.repo.UserRepo;
import com.xuanwu.mos.rest.repo.CarrierRepo;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.msggate.common.cache.SyncTask;

import gnu.trove.map.hash.TLongObjectHashMap;

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
 * 默认的基本业务数据服务实现，基于版本号的缓存实现
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
/**@Component("iBizDataService")*/
public class DefaultBizDataService implements IBizDataService, SyncTask {

    private Logger logger = LoggerFactory.getLogger(DefaultBizDataService.class);

    private CarrierRepo carrierDao;

    private RoleRepo roleRepo;

    private UserRepo userDao;

    private HashMap<Integer, Carrier> carrierMap = new HashMap<Integer, Carrier>();

    private Map<Integer, Region> regionMap = new HashMap<Integer, Region>();

    private Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();

    private TLongObjectHashMap<List<RolePermission>> rolePermMap;

    private Map<String, Permission> urlPermMap = new HashMap<String, Permission>();

    private Map<String, Carrier> phoneSegMap = new HashMap<String, Carrier>();

    private Map<Integer, CacheObject<Map<Integer, GsmsUser>>> entMap = new HashMap<Integer, CacheObject<Map<Integer, GsmsUser>>>();

    private int rolePermVer = -1;

    private int phoneSegVer = -1;

    private volatile int userVer = -1;

    private ReentrantReadWriteLock rolePermLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock phoneSegLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock userLock = new ReentrantReadWriteLock();

    private long period = 10 * 60 * 1000;

    @PostConstruct
    public void init(){
        //permission items, load once is OK
        List<Permission> perms = roleRepo.findAllPermissions(Platform.FRONTKIT.getIndex());
        for(Permission perm : perms){
            permissionMap.put((Integer) perm.getId(), perm);
            urlPermMap.put(perm.getPermURL(), perm);
        }
        List<Region> regions = roleRepo.findAllRegions();
        for(Region region : regions){
            regionMap.put(region.getId(), region);
        }
        List<Carrier> carriers = carrierDao.findAllCarriers();
        for(Carrier carrier : carriers){
            carrierMap.put(carrier.getId(), carrier);
        }
        sync();
    }

    @Override
    public void run() {
        sync();
    }

    public void sync() {
        try{
            syncRolePerm();
        } catch (Exception e){
            logger.error("Sync role permission fail, ", e);
        }
        try{
            syncPhoneSeg();
        } catch (Exception e){
            logger.error("Sync phone segments fail, ", e);
        }
        try{
            syncUser();
        } catch (Exception e){
            logger.error("Sync user fail, ", e);
        }
    }

    @Override
    public void syncRolePerm() {
        int ver = roleRepo.findGsmsSyncVersion(GsmsSyncVersionType.ROLE_PERM.getIndex());
        logger.info("Begin to sync role permission, current version[{}], new version[{}]...", rolePermVer, ver);
        if(rolePermVer >= ver){
            logger.info("End to sync role permission.");
            return;
        }
        rolePermLock.writeLock().lock();
        try {
            if(rolePermVer >= ver){
                logger.info("End to sync role permission.");
                return;
            }
            TLongObjectHashMap<List<RolePermission>> rolePermMap = new TLongObjectHashMap<List<RolePermission>>();
            List<RolePermission> rolePerms = roleRepo.findAllRolePermissions();
            for(RolePermission rolePerm : rolePerms){
                Permission perm = permissionMap.get(rolePerm.getPermissionId());
                if(perm == null)
                    continue;
                rolePerm.setPlatform(perm.getPlatform());
                rolePerm.setBaseUrl(perm.getPermURL());
                int key = tranRolePermKey(rolePerm.getRoleId(), rolePerm.getPlatform());
                List<RolePermission> value = rolePermMap.get(key);
                if(value == null){
                    value = new ArrayList<RolePermission>();
                    rolePermMap.put(key, value);
                }
                value.add(rolePerm);
            }
            this.rolePermMap = rolePermMap;
            rolePermVer = ver;
            logger.info("Sync role permission to version [{}]", ver);
        } finally {
            rolePermLock.writeLock().unlock();
        }
        logger.info("End to sync role permission.");
    }

    @Override
    public void syncPhoneSeg(){
        int ver = roleRepo.findGsmsSyncVersion(GsmsSyncVersionType.PHONE_SEG.getIndex());
        logger.info("Begin to sync phone segments, current version[{}], new version[{}]...", phoneSegVer, ver);
        if(phoneSegVer >= ver){
            logger.info("End to sync phone segments.");
            return;
        }
        phoneSegLock.writeLock().lock();
        try {
            if(phoneSegVer >= ver){
                logger.info("End to sync phone segments.");
                return;
            }
            List<CarrierTeleseg> phoneSegs = carrierDao.findAllCarrierTeleseg();
            Map<String, Carrier> phoneSegMap = new HashMap<String, Carrier>();
            for(CarrierTeleseg phoneSeg : phoneSegs){
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

    private long entCacheTime = 3600;//1 hour

    public void syncUser(){
        int ver = roleRepo.findGsmsSyncVersion(GsmsSyncVersionType.USER.getIndex());
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

    private CacheObject<Map<Integer, GsmsUser>> loadGsmsUsers(int entId){
        userLock.writeLock().lock();
        try{
            CacheObject<Map<Integer, GsmsUser>> obj = entMap.get(entId);
            if(obj != null && obj.getVersion() >= userVer){
                return obj;
            }
            List<GsmsUser> depts = new ArrayList<GsmsUser>();
            GsmsUser ent = userDao.findGsmsUser(entId);
            if(ent != null){
                depts.add(ent);
            }
            List<GsmsUser> ret = userDao.findAllEntDepts(entId);
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

    private List<GsmsUser> loadGsmsUsersIncludeDel(int entId){
        List<GsmsUser> depts = new ArrayList<GsmsUser>();
        GsmsUser ent = userDao.findGsmsUser(entId);
        if(ent != null){
            depts.add(ent);
        }
        List<GsmsUser> ret = userDao.findAllEntDeptsIncludeDel(entId);
        if(ListUtil.isNotBlank(ret)){
            depts.addAll(ret);
        }
        return depts;
    }


    @Override
    public Permission getPermission(Integer id) {
        return permissionMap.get(id);
    }

    @Override
    public Permission getPermission(String permUrl) {
        return urlPermMap.get(permUrl);
    }

    @Override
    public List<RolePermission> getRolePermissions(Integer roleID, Platform platform) {
        rolePermLock.readLock().lock();
        try{
            return rolePermMap.get(tranRolePermKey(roleID, platform));
        } finally {
            rolePermLock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
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

    @Override
    public List<GsmsUser> getDeptsIncludeDel(int entId){
        return loadGsmsUsersIncludeDel(entId);
    }

    @Override
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




    @Override
    public boolean updateDept(int entId, GsmsUser dept) {
        userLock.writeLock().lock();
        try{
            CacheObject<Map<Integer, GsmsUser>> obj = entMap.get(entId);
            if(obj == null){
                Map<Integer, GsmsUser> map = new LinkedHashMap<Integer, GsmsUser>();
                obj = new CacheObject<Map<Integer, GsmsUser>>(map);
                entMap.put(entId, obj);
            }
            GsmsUser ret = userDao.findGsmsUser(dept.getId());
            if(ret == null || UserState.NORMAL !=  ret.getState()){
                obj.getObject().remove(dept.getId());
            } else {
                obj.getObject().put(dept.getId(), ret);
            }
            obj.setAttachment(ListUtil.map2List(obj.getObject().values()));
        } finally {
            userLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public Region getRegionById(int id) {
        return regionMap.get(id);
    }

    @Override
    public Carrier getCarrierById(int id) {
        return carrierMap.get(id);
    }

    private int tranRolePermKey(Integer roleID, Platform platform){
        if(roleID == null || platform == null){
            throw new NullPointerException("roleID or platform");
        }
        return roleID << 4 | (platform.getIndex() & 0xF);
    }

    @Override
    public Carrier getCarrierType(String phone) {
        if(phone == null || phone.length() < 4){
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

    @Autowired
    public void setRoleRepo(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Autowired
    public void setCarrierRepo(CarrierRepo carrierDao) {
        this.carrierDao = carrierDao;
    }

    @Autowired
    public void setCarrierDao(CarrierRepo carrierDao) {
        this.carrierDao = carrierDao;
    }

    @Autowired
    public void setUserDao(UserRepo userDao) {
        this.userDao = userDao;
    }

    @Override
    public long getPeriod() {
        return period;
    }
}

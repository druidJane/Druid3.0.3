package com.xuanwu.module.service;

import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.BlackList;
import com.xuanwu.mos.domain.entity.Enterprise;
import com.xuanwu.mos.domain.entity.User;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.BlackListRepo;
import com.xuanwu.msggate.common.cache.engine.LongHashCache;
import com.xuanwu.msggate.common.util.PhoneUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangz on 2017/3/29.
 */
@Service
public class BlackListService {
    @Autowired
    private BlackListRepo blackListRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private BizTypeService bizTypeService;

    @Autowired
    private CarrierService carrierService;
    public List<BlackList> findCachePhonelists(List<Integer> ids, int blackBizType) {
        QueryParameters params = new QueryParameters();
        params.addParam("ids",ids);
        params.addParam("blackBizType",blackBizType);
        return blackListRepo.findCachePhoneLists(params);
    }

    public int findBlacklistCount(QueryParameters params) {
        return blackListRepo.findResultCount(params);
    }
    public  List<BlackList> findBlackList(QueryParameters params) {
        return blackListRepo.findBlacklist(params);
    }
    public List<BlackList> findBlacklistsDetail(QueryParameters params) {
        List<BlackList> list = this.findBlackList(params);
        List<BlackList> resultList = new ArrayList<>();
        for(BlackList item:list){
            switch (BlackList.BlacklistType.getType(item.getType())){
                case User:
                    User user = (User)userService.findUserById(item.getTarget());
                    item.setTargetName(user.getUserName());
                    break;
                case Enterprise:
                    Enterprise enterprise = userService.getEnterpriseUser(item.getTarget());
                    item.setTargetName(enterprise.getEnterpriseName());
                    break;
                case BizType:
                    BizType bizType = bizTypeService.findBizTypeById(item.getTarget());
                    item.setTargetName(bizType.getName());
                    break;
            }
            //所属对象名称模糊匹配
            String targetName = (String) params.getParams().get("targetName");
            if(!org.springframework.util.StringUtils.isEmpty(targetName)){
                if(!item.getTargetName().contains(targetName)){
                    continue;
                }
            }
            resultList.add(item);
        }
        return resultList;
    }
    @Transactional(transactionManager = "listTransactionManager", rollbackFor = Exception.class)
    public int delBlacklist(Integer id) throws RepositoryException {
        int count = 0;
        BlackList exist = blackListRepo.getById(id, null);
        if(exist!=null){
            blackListRepo.delete(exist);
        }
        //插入缓冲表，用于网关更新
        LongHashCache cache = new LongHashCache();
        long zipmes = cache.tran2Code(exist.getPhone(), exist.getType(), exist.getTarget());
        exist.setZipmes(zipmes);
        exist.setHandleTime(new Date());
        exist.setRemove(true);
        exist.setRemoveId(exist.getId());
        blackListRepo.addCachePhoneList(exist);
        return count;
    }

    /**
     * 查找运营商号码段
     * @param blacklist
     * @return
     */
    public boolean findTeleSeg(BlackList blacklist) {
        String phone = blacklist.getPhone();
        String tempPhone = PhoneUtil.removeZhCnCode(phone);
        blacklist.setPhone(tempPhone);
        long count = getTeleSegCount(tempPhone);
        if (count <= 0) {
            tempPhone = PhoneUtil.removeZeroHead(phone);
            blacklist.setPhone(tempPhone);
            count = getTeleSegCount(tempPhone);
        }

        if (count > 0) {
            return true;
        }

        return false;
    }
    public long getTeleSegCount(String phone) {

        if (StringUtils.isEmpty(phone) || phone.length() < 4) {
            return 0;
        }

        String teleSeg = phone.substring(0, 4);
        long count = carrierService.findTeleSegCount(teleSeg);
        if (count <= 0) {
            teleSeg = phone.substring(0, 3);
            count = carrierService.findTeleSegCount(teleSeg);
        }
        return count;
    }

    /**
     * 判断黑名单是否存在
     * @param blacklist
     * @return
     */
    public boolean isExist(BlackList blacklist) {
        int count = blackListRepo.isExist(blacklist);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 新增黑名单，插入缓冲表，黑名单表(desid字段用缓冲表主键）
     * @param blackList
     * @throws RepositoryException
     */
    @Transactional(transactionManager = "listTransactionManager", rollbackFor = Exception.class)
    public void addBlacklist(BlackList blackList) throws RepositoryException{
        blackListRepo.addCachePhoneList(blackList);
        blackList.setDesID(blackList.getId());
        blackListRepo.addBlacklist(blackList);
    }
}

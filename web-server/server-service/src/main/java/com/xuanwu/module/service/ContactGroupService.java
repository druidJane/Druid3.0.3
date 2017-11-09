package com.xuanwu.module.service;

import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.rest.repo.ContactGroupRepo;
import com.xuanwu.mos.utils.Delimiters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangz on 2017/5/8.
 */
@Service
public class ContactGroupService {
    @Autowired
    private ContactGroupRepo contactGroupRepo;
    @Autowired
    private ContactShareGroupService contactShareGroupService;

    public ContactGroup findContactGroupById(int id){
        return contactGroupRepo.getById(id,null);
    }
    public List<ContactGroup> findContactGroup(QueryParameters params){
        return contactGroupRepo.findResults(params);
    }
    public int insertContactGroup(ContactGroup cg) {
        return contactGroupRepo.insertContactGroup(cg);
    }
    public List<ContactGroup> findContactGroupByName(String name, int entId,
                                                     int userId, int type) {
        return contactGroupRepo.findContactGroupByName(name, entId, userId, type);
    }
    public boolean updateContactGroup(ContactGroup cg) {
        return contactGroupRepo.updateContactGroup(cg);
    }
    @Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
    public void deleteContactGroupsByPath(String path,ContactGroup cg) {
        contactGroupRepo.deleteContactGroupsByPath(path);
    }
    public List<ContactGroup> findContactGroupByUserIds(String userIds){
        String[] ids = userIds.split(Delimiters.COMMA);
        List<Integer> list = new ArrayList<>();
        for(String userId:ids){
            list.add(Integer.valueOf(userId));
        }
        QueryParameters params = new QueryParameters();
        params.addParam("userIds", list);
        return this.findContactGroup(params);
    }

    public void delByUserIds(String userIds) {
        if(StringUtils.isEmpty(userIds)){
            throw new BusinessException("userIds cannot be null!");
        }
        String[] ids = userIds.split(",");
        List<Integer> list = new ArrayList<>();
        for(String id : ids){
            list.add(Integer.valueOf(id));
        }
        QueryParameters params = new QueryParameters();
        params.addParam("userIds", list);
        contactGroupRepo.deleteContactGroup(params);
    }
}

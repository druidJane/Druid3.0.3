package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.domain.entity.ContactShareGroup;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.ContactShareGroupRepo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangz on 2017/5/11.
 */
@Service
public class ContactShareGroupService {
    @Autowired
    private ContactShareGroupRepo contactShareGroupRepo;
    @Autowired
    private ContactGroupService contactGroupService;
    @Autowired
    private ContactService contactService;

    public List<ContactShareGroup> findContactShareGroups(QueryParameters params){
        return contactShareGroupRepo.findResults(params);
    }
    public ContactShareGroup findShareContactGroupByPath(QueryParameters params){
        String path = (String) params.getParams().get("path");
        if(StringUtils.isEmpty(path)){
            throw new BusinessException("path cannot be null!");
        }
        return contactShareGroupRepo.findShareContactGroupByPath(path);
    }

    /**
     * 获取关联的个人通讯录群组详情
     * @param params
     * @return
     */
    public List<ContactGroup> findPersonalGroups(QueryParameters params){
        return contactShareGroupRepo.findContactGroups(params);
    }
    /**
     * 获取关联的个人通讯录详情
     */
    public List<Contact> findShareContactDetail(QueryParameters params){
        return contactShareGroupRepo.findShareContactDetail(params);
    }
    /**
     * 根据用户ID 获取企业ID 获取共享通讯录数据，发送短信页面企业级别，共享通讯录页面个人级别
     * @param userId
     * @return
     */
    public ContactGroup findETree(Integer userId,Integer enterpriseId) {
        if(null == userId && null == enterpriseId){
            throw new BusinessException("企业ID，用户ID不能同时为空");
        }
        List<ContactGroup> result = new ArrayList<>();
        ContactGroup rootCg = contactGroupService.findContactGroupById(1);
        rootCg.setName("共享通讯录");
        rootCg.setType(2);
        result.add(rootCg);
        QueryParameters params = new QueryParameters();
        params.addParam("userId",userId);
        params.addParam("enterpriseId",enterpriseId);
        //获取该用户，该企业下的共享通讯录
        List<ContactGroup> csgList = this.findPersonalGroups(params);
        for(ContactGroup shareGroup:csgList){
            params = new QueryParameters();
            params.addParam("type",0);
            //是否共享子组
            List<ContactGroup> childGroup = new ArrayList<>();
            if(shareGroup.isContainChild()){
                params.addParam("path",shareGroup.getPath());
                params.addParam("showChild",true);
                //获取该群组下的子组
                childGroup = contactGroupService.findContactGroup(params);
                childGroup.remove(shareGroup);
                for(ContactGroup child :childGroup){
                    child.setShowContact(shareGroup.getShowContact());
                }
            }
            shareGroup.setChildren(childGroup);
            String isShow = shareGroup.getShowContact()?"显示":"隐藏";
            //通讯录联系人数量
            params.addParam("groupId",shareGroup.getId());
            Integer contactCount = contactService.findContactsCount(params);
            String name = shareGroup.getName()+"("+isShow+","+contactCount+")";
            shareGroup.setName(name);
            shareGroup.setChildCount(contactCount);
            shareGroup.setType(2);//共享为2
        }
        rootCg.setChildren(csgList);
        return rootCg;
    }

    public void insert(ContactShareGroup shareGroup) throws RepositoryException {
        contactShareGroupRepo.insert(shareGroup);
    }

    public void delete(ContactShareGroup del) throws RepositoryException {
        contactShareGroupRepo.delete(del);
    }

    public int findShareContactCount(QueryParameters params) {
        return contactShareGroupRepo.findShareContactCount(params);
    }
    public ContactShareGroup getById(Integer id){
        return contactShareGroupRepo.getById(id,null);
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
        contactShareGroupRepo.deleteByUserIds(params);
    }
}

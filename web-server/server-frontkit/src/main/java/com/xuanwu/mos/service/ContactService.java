package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.domain.entity.DynamicParam;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.file.importer.ImportInfoBuild;

import java.util.Date;
import java.util.List;

/**
 * Created by Jiang.Ziyuan on 2017/3/27.
 */
public interface ContactService {

    List<Contact> findContacts(List<Integer> ids);

    List<Contact> findContacts(List<ContactGroup> cgs, DynamicParam dynParam);

    int findContactsCount(QueryParameters params);

    List<Contact> findContacts(QueryParameters params);

    boolean checkContactExist(String phone, int groupId);

    boolean insertContact(Contact add);

    boolean updateContact(Contact update);

    Contact findContactById(Integer id);

    boolean deleteContact(QueryParameters params);

    void importContactData(List<Contact> contactList, int userId, ImportInfoBuild infoBuild, int entId);

    void delByUserIds(String userIds);

    int findContactsCountByGroupIds(List<Integer> groupIds, String name,
                                    String phone, Integer sex, Date beginDate, Date endDate,
                                    String identifier, Integer vip);
}

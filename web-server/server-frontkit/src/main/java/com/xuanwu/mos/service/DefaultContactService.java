package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.domain.entity.DynamicParam;
import com.xuanwu.mos.domain.enums.CheckResult;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.file.importer.ImportInfoBuild;
import com.xuanwu.mos.rest.repo.ContactGroupRepo;
import com.xuanwu.mos.rest.repo.ContactRepo;
import com.xuanwu.mos.utils.ListUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultContactService implements ContactService {
	@Autowired
	private ContactRepo contactRepo;

	@Autowired
	private ContactGroupService contactGroupService;

	@Autowired
	private ContactGroupRepo contactGroupRepo;

	@Override
	public List<Contact> findContacts(List<Integer> ids) {
		if (ListUtil.isBlank(ids)) {
			return Collections.emptyList();
		}
		return contactRepo.findContactsByIds(ids,0,0);
	}

	@Override
	public List<Contact> findContacts(List<ContactGroup> cgs, DynamicParam dynParam) {
		if (ListUtil.isBlank(cgs)) {
			return Collections.emptyList();
		}
		return contactRepo.findContactsByGroup(cgs, dynParam);
	}

	@Override
	public int findContactsCount(QueryParameters params) {
		return contactRepo.findResultCount(params);
	}
	@Override
	public List<Contact> findContacts(QueryParameters params) {
		return contactRepo.findResults(params);
	}

	@Override
	public boolean checkContactExist(String phone, int groupId) {
		return contactRepo.checkContactExist(phone, groupId) > 0;
	}

	@Override
	public boolean insertContact(Contact add) {
		return contactRepo.insertContact(add)==0?false:true;
	}

	@Override
	public boolean updateContact(Contact update) {
		return contactRepo.updateContact(update)==0?false:true;
	}

	@Override
	public Contact findContactById(Integer id) {
		return contactRepo.findContactById(id);
	}

	@Override
	public boolean deleteContact(QueryParameters params) {
		return contactRepo.deleteContact(params)==0?false:true;
	}
	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public void importContactData(List<Contact> contactList, int userId,ImportInfoBuild infoBuild,int entId){
		int type = 0;
		int parentId = type == 0 ? 1 : 2;
		String path = userId + ".";
		checkPhoneExist(contactList, infoBuild);
		List<Contact> contacts = new ArrayList<Contact>();
		for (Contact c : contactList) {
			int exist = contactRepo.checkContactExist(c.getPhone(),
					c.getGroupId());
			if (exist == 0) {
				contacts.add(c);
				infoBuild.increment(CheckResult.Legal, c);
			} else {// 同组号码重复
				infoBuild.increment(CheckResult.ContactPhoneExists, c);
			}
		}
		//TODO 改batch
		if (ListUtil.isNotBlank(contacts)) {
			for (Contact contact : contacts) {
				contactRepo.insertContact(contact);
			}
		}
	}

	@Override
	public void delByUserIds(String userIds) {
		if(StringUtils.isEmpty(userIds)){
			throw new BusinessException("userIds cannot be null!");
		}
		List<Integer> list = new ArrayList<>();
		List<ContactGroup> groupList = contactGroupService.findContactGroupByUserIds(userIds);
		for(ContactGroup cg:groupList){
			list.add(cg.getId());
		}
		QueryParameters params = new QueryParameters();
		params.addParam("groupIds",list);
		if (list.size() > 0) {
			this.deleteContact(params);
		}
	}

	@Override
	public int findContactsCountByGroupIds(List<Integer> groupIds, String name, String phone, Integer sex, Date beginDate, Date endDate, String identifier, Integer vip) {
		if(groupIds == null || groupIds.isEmpty()){
			throw new BusinessException("groupIds cannot be null!");
		}
		return contactRepo.findContactsCountByGroupIds(groupIds,name,phone,sex,beginDate,endDate,identifier,vip);
	}

	/**
	 * 导入文件有多个相同的重复号码，以最后记录的为准
	 *
	 * @param list
	 */
	private void checkPhoneExist(List<Contact> list, ImportInfoBuild infoBuild) {
		Map<String, Contact> temp = new HashMap<String, Contact>();
		for (Contact contact : list) {
			if (temp.containsKey(contact.getPhone())) {
				infoBuild.increment(CheckResult.ContactPhoneExists,
						temp.get(contact.getPhone()));
			}
			temp.put(contact.getPhone(), contact);
		}
		list.clear();
		for (Map.Entry<String, Contact> entry : temp.entrySet()) {
			list.add(entry.getValue());
		}

	}

}




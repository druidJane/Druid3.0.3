package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.domain.entity.ContactShareGroup;
import com.xuanwu.mos.domain.entity.DynamicParam;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.utils.DateUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
@Repository
public class ContactRepo extends GsmsMybatisEntityRepository<Contact> {

	@Override
	protected String namesapceForSqlId() {
		// TODO Auto-generated method stub
		return "com.xuanwu.mos.mapper.ContactMapper";
	}

	public int findContactsCountByGroupIds(List<Integer> groupIds, String name,
										   String phone, Integer sex, Date beginDate, Date endDate,
										   String identifier, Integer vip){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			HashMap param = new HashMap();
			param.put("groupIds", groupIds);
			param.put("name", name);
			param.put("phone", phone);
			param.put("sex", sex);
			param.put("beginDate", beginDate);
			param.put("endDate", endDate);
			param.put("identifier", identifier);
			param.put("vip", vip);
			return sqlsession.selectOne(fullSqlId("findContactsCountByGroupIds"), param);
		}
	}

	public ContactShareGroup findShareContactGroupByGroupId(int groupId){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			return sqlsession.selectOne(fullSqlId("findShareContactGroupByGroupId"), groupId);
		}
	}

	public int checkContactExist(String phone, int groupId){
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap param = new HashMap();
			param.put("phone", phone);
			param.put("groupId", groupId);
			return session.selectOne(fullSqlId("checkContactExist"), param);
		}
	}

	public int insertContact(Contact contact){
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ret = session.insert(fullSqlId("insertContact"), contact);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Save/Update Entity failed: ", e);
		}
		return ret;
	}
	public List<Contact> findContactsByIds(List<Integer> ids,
										   Integer birthMonth, Integer birthDay){
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap param = new HashMap();
			param.put("ids", ids);
			param.put("birthMonth", birthMonth);
			param.put("birthDay", birthDay);
			return session.selectList(fullSqlId("findContactsByIds"), param);

		}
	}
	public int updateContact(Contact contact){
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ret = session.update(fullSqlId("updateContact"), contact);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Save/Update Entity failed: ", e);
		}
		return ret;
	}

	public int deleteContact(QueryParameters params){
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ret = session.delete(fullSqlId("deleteContact"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete Entity failed: ", e);
		}
		return ret;
	}

	/**
	 * @return
	 */
	public List<ContactGroup> findShareContactGroup(List<Integer> userIds){
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap param = new HashMap();
			param.put("userIds", userIds);
			return session.selectList(fullSqlId("findShareContactGroup"), param);

		}
	}

	public List<ContactGroup> findEnterContactGroup(int entId){
		try (SqlSession session = sqlSessionFactory.openSession()) {
			HashMap param = new HashMap();
			param.put("entId", entId);
			return session.selectList(fullSqlId("findEnterContactGroup"), param);

		}
	}

	public Contact findContactById(int id) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()){
			HashMap params = new HashMap();
			params.put("id", id);
			return sqlSession.selectOne(fullSqlId("findContactById"), params);
		}
	}

	public List<Contact> findContactsByGroup(List<ContactGroup> cgs,
											 DynamicParam dynParam) {
		return findContactsByGroup(cgs, dynParam, null);
	}

	public List<Contact> findContactsByGroup(List<ContactGroup> cgs,
											 DynamicParam dynParam,Date birthday) {
		List<Contact> cs = new ArrayList<Contact>();
		int[] monthDay = DateUtil.getMonthDay(birthday);
		try (SqlSession sqlSession = sqlSessionFactory.openSession()){
			for (ContactGroup cg : cgs) {
				HashMap param = new HashMap();
				param.put("cg", cg);
				param.put("dynParam", dynParam);
				param.put("birthMonth", monthDay[0]);
				param.put("birthDay", monthDay[1]);
				cs.addAll(sqlSession.<Contact>selectList(fullSqlId("findContactsByGroup"), param));
			}
			return cs;
		}
	}
}

package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.dto.QueryParameters;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
@Repository
public class ContactGroupRepo extends GsmsMybatisEntityRepository<ContactGroup> {

	@Override
	protected String namesapceForSqlId() {
		// TODO Auto-generated method stub
		return "com.xuanwu.mos.mapper.ContactGroupMapper";
	}
	public ContactGroup findContactGroupById(int id){

		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			HashMap param = new HashMap();
			param.put("id", id);
			return sqlsession.selectOne(fullSqlId("findContactGroupById"), param);
		}
	}

	public List<ContactGroup> findContactGroupByName(String name, int entId,
													 int userId, int type){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			HashMap param = new HashMap();
			param.put("name", name);
			param.put("entId", entId);
			param.put("userId", userId);
			param.put("type", type);
			return sqlsession.selectList(fullSqlId("findContactGroupByName"), param);
		}
	}

	public List<ContactGroup> findContactGroup(int entId, int userId, int type){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			HashMap param = new HashMap();
			param.put("entId", entId);
			param.put("userId", userId);
			param.put("type", type);
			return sqlsession.selectList(fullSqlId("findContactGroup"), param);
		}
	}



	public List<ContactGroup> findGroupsByPath(String path, int userId,
											   int entId){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			HashMap param = new HashMap();
			param.put("path", path);
			param.put("userId", userId);
			param.put("entId", entId);
			return sqlsession.selectList(fullSqlId("findGroupsByPath"), param);
		}

	}

	public List<ContactGroup> findGroupsByParentId(int parentId, int userId,
												   int entId){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			HashMap param = new HashMap();
			param.put("parentId", parentId);
			param.put("userId", userId);
			param.put("entId", entId);
			return sqlsession.selectList(fullSqlId("findGroupsByParentId"), param);
		}
	}
	public int findGroupChildCount(String path, Integer userId, Integer groupId){
		try(SqlSession sqlsession = sqlSessionFactory.openSession()){
			HashMap param = new HashMap();
			param.put("groupId", groupId);
			param.put("userId", userId);
			param.put("path", path);
			return sqlsession.selectOne(fullSqlId("findGroupChildCount"), param);
		}
	}


	public int insertContactGroup(ContactGroup cg) {
		int id = 0;

		try(SqlSession sqlsession = sqlSessionFactory.openSession()) {
			sqlsession.insert(fullSqlId("insertContactGroup"), cg);
			id = sqlsession.selectOne(fullSqlId("findLastInsertId"));
			cg.setPath(cg.getPath() + id + ".");
			cg.setId(id);
			sqlsession.update(fullSqlId("updateContactGroupPath"), cg);
			sqlsession.commit();
		}
		return id;
	}

	public boolean updateContactGroup(ContactGroup cg){
		int ret = 0;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ret = session.update(fullSqlId("updateContactGroup"), cg);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Save/Update Entity failed: ", e);
		}
		if(ret >0){
			return true;
		}else{
			return false;
		}

	}

	public boolean deleteContactGroupsByPath(String path) {
		SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
		try {
			session.delete(fullSqlId("deleteContactsByPath"),path);
			session.delete(fullSqlId("deleteContactGroupsByPath"),path);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
		return true;
	}

	public List<ContactGroup> findByUserIds(String[] ids) {
		return null;
	}

	public int findLastInsertId() {
		return 0;
	}

	public void deleteContactGroup(QueryParameters params) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.delete(fullSqlId("deleteContactGroup"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Delete Entity failed: ", e);
		}
	}
}

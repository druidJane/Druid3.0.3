package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
@Repository
public class SMSContactRepo extends GsmsMybatisEntityRepository<Contact> {
    private static final Logger logger = LoggerFactory.getLogger(CarrierRepo.class);
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.ContactMapper";
    }

    public ContactGroup findContactGroupById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findContactGroupById"), id);
        }
    }

    public List<ContactGroup> findContactGroupByName(String name, int entId, int userId,int type) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("entId", entId);
            params.put("userId", userId);
            params.put("type", type);
            return session.selectList(fullSqlId("findContactGroupByName"), params);
        }
    }

    public List<ContactGroup> findContactGroup(int entId, int userId,int type) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("entId", entId);
            params.put("userId", userId);
            params.put("type", type);
            return session.selectList(fullSqlId("findContactGroup"), params);
        }
    }

    public List<Contact> findContactsByIds(List<Integer> ids, int birthMonth, int birthDay) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("ids", ids);
            params.put("birthMonth", birthMonth);
            params.put("birthDay", birthDay);
            return session.selectList(fullSqlId("findContactsByIds"), params);
        }
    }

    public List<Contact> findContactsByGroup(ContactGroup cg, DynamicParam dynParam, int birthMonth, int birthDay) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("cg", cg);
            params.put("dynParam", dynParam);
            params.put("birthMonth", birthMonth);
            params.put("birthDay", birthDay);
            return session.selectList(fullSqlId("findContactsByGroup"), params);
        }
    }

    public List<ContactGroup> findGroupsByPath(String path, int userId, int entId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("path", path);
            params.put("userId", userId);
            params.put("entId", entId);
            return session.selectList(fullSqlId("findGroupsByPath"), params);
        }
    }

    public List<ContactGroup> findGroupsByParentId(int parentId, int userId, int entId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", parentId);
            params.put("userId", userId);
            params.put("entId", entId);
            return session.selectList(fullSqlId("findGroupsByParentId"), params);
        }
    }


    public int findContactsCount(int type, int userId, int groupId,boolean showChild,String path,String name, String phone,
                                 int sex, Date beginDate, Date endDate, String identifier,int vip) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("type", type);
            params.put("userId", userId);
            params.put("groupId", groupId);
            params.put("showChild", showChild);
            params.put("path", path);
            params.put("name", name);
            params.put("phone", phone);
            params.put("sex", sex);
            params.put("beginDate", beginDate);
            params.put("endDate", endDate);
            params.put("identifier", identifier);
            params.put("vip", vip);
            return session.selectOne(fullSqlId("findContactsCount"), params);
        }
    }

    public int findContactsCountByGroupIds(List<Integer> groupIds, String name, String phone,
                                 int sex, Date beginDate, Date endDate, String identifier,int vip) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("groupIds", groupIds);
            params.put("name", name);
            params.put("phone", phone);
            params.put("sex", sex);
            params.put("beginDate", beginDate);
            params.put("endDate", endDate);
            params.put("identifier", identifier);
            params.put("vip", vip);
            return session.selectOne(fullSqlId("findContactsCountByGroupIds"), params);
        }
    }

    public int findGroupChildCount(String path, Integer userId, Integer groupId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("path", path);
            params.put("userId", userId);
            params.put("groupId", groupId);
            return session.selectOne(fullSqlId("findGroupChildCount"), params);
        }
    }

    public int findGroupChildCount(Integer userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findGroupChildCount"), userId);
        }
    }

    public List<Contact> findContacts(int type, int userId, int groupId,boolean showChild,String path,String name, String phone,
                                 int sex, Date beginDate, Date endDate, String identifier,int vip,int birthMonth,int birthDay,int offset,int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.addParam("type", type);
            queryParameters.addParam("userId", userId);
            queryParameters.addParam("groupId", groupId);
            queryParameters.addParam("showChild", showChild);
            queryParameters.addParam("path", path);
            queryParameters.addParam("name", name);
            queryParameters.addParam("phone", phone);
            queryParameters.addParam("sex", sex);
            queryParameters.addParam("beginDate", beginDate);
            queryParameters.addParam("endDate", endDate);
            queryParameters.addParam("identifier", identifier);
            queryParameters.addParam("vip", vip);
            queryParameters.addParam("birthMonth", birthMonth);
            queryParameters.addParam("birthDay", birthDay);
            queryParameters.addParam("offset", offset);
            queryParameters.addParam("reqNum", reqNum);
            List<Contact> findContacts = this.findResults(queryParameters);
            return findContacts;
        }
    }

    public List<Contact> findContactsByGroupIds(List<Integer> groupIds, String name, String phone,
                                                int sex, Date beginDate, Date endDate, String identifier,int vip,
                                                int birthMonth,int birthDay,int offset,int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("groupIds", groupIds);
            params.put("name", name);
            params.put("phone", phone);
            params.put("sex", sex);
            params.put("beginDate", beginDate);
            params.put("endDate", endDate);
            params.put("identifier", identifier);
            params.put("vip", vip);
            params.put("birthMonth", birthMonth);
            params.put("birthDay", birthDay);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            return session.selectList(fullSqlId("findContactsByGroupIds"), params);
        }
    }

    public List<Contact> findShareContactsByGroupIds(List<Integer> groupIds, String name, String phone,
                                                int sex, Date beginDate, Date endDate, String identifier,int vip,
                                                int offset,int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("groupIds", groupIds);
            params.put("name", name);
            params.put("phone", phone);
            params.put("sex", sex);
            params.put("beginDate", beginDate);
            params.put("endDate", endDate);
            params.put("identifier", identifier);
            params.put("vip", vip);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            return session.selectList(fullSqlId("findShareContactsByGroupIds"), params);
        }
    }

    /**
     * 查询用户共享组
     *
     * @return
     */
    public List<ContactShareGroup> findShareContactGroup(List<Integer> userIds) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findShareContactGroup"), userIds);
        }
    }

    public ContactShareGroup findShareContactGroupByGroupId(int groupId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findShareContactGroupByGroupId"), groupId);
        }
    }

    public Contact findContactFirstRow(String path,Integer userId, Integer groupId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("path", path);
            params.put("userId", userId);
            params.put("groupId", groupId);
            return session.selectOne(fullSqlId("findContactFirstRow"), params);
        }
    }

    public int insertContactGroup(ContactGroup cg) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.insert(fullSqlId("insertContactGroup"), cg);
        }
    }

    public int updateContactGroup(ContactGroup cg) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ret = session.update(fullSqlId("updateContactGroup"), cg);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update ContactGroup failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    public int updateContactGroupPath(ContactGroup cg) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ret = session.update(fullSqlId("updateContactGroupPath"), cg);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update ContactGroup failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    public int deleteContactGroupsByPath(String path) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("path", path);
            ret = session.delete(fullSqlId("deleteContactGroupsByPath"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete ContactGroup failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    public int deleteContactsByPath(String path) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("path", path);
            ret = session.delete(fullSqlId("deleteContactsByPath"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete Contact failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    public Contact findContactById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findContactById"), id);
        }
    }

    public int checkContactExist(String phone,int groupId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("groupId", groupId);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("checkContactExist"), map);
        }
    }

    public int insertContact(Contact contact) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.insert(fullSqlId("insertContact"), contact);
        }
    }

    public int insertContactList(List<Contact> contacts) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.insert(fullSqlId("insertContactList"), contacts);
        }
    }

    public int updateContact(Contact contact) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ret = session.update(fullSqlId("updateContact"), contact);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update Contact failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    /**
     * 添加共享组
     *
     * @param showContactFlag
     * @return
     */
    public int addShareContactGroup(Integer groupId, boolean showContactFlag, boolean shareChildFlag) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("groupId", groupId);
            map.put("showContactFlag", showContactFlag);
            map.put("shareChildFlag", shareChildFlag);
            return session.insert(fullSqlId("addShareContactGroup"), map);
        }
    }

    /**
     * 移除共享组
     *
     * @param id
     * @return
     */
    public int removeShareContactGroup(int id) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            ret = session.delete(fullSqlId("removeShareContactGroup"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete ShareContactGroup failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    public List<Integer> findShareGroupUserId(int entId, int offset, int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("entId", entId);
            map.put("offset", offset);
            map.put("reqNum", reqNum);
            return session.selectList(fullSqlId("findShareGroupUserId"), map);
        }
    }

    public int deleteContact(List<Integer> ids) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ids", ids);
            ret = session.delete(fullSqlId("deleteContact"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete Contact failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    public int findLastInsertId() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findLastInsertId"));
        }
    }

    public List<Contact> findContactByDrag(String path, Integer userId, Integer groupId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("path", path);
            map.put("userId", userId);
            map.put("groupId", groupId);
            return session.selectList(fullSqlId("findContactByDrag"), map);
        }
    }

    public List<ContactGroup> findEnterContactGroup(int entId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findEnterContactGroup"), entId);
        }
    }
}

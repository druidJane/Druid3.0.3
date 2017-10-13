package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.domain.entity.ContactShareGroup;
import com.xuanwu.mos.dto.QueryParameters;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangz on 2017/5/11.
 */
@Repository
public class ContactShareGroupRepo extends GsmsMybatisEntityRepository<ContactShareGroup> {

    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.ContactShareGroupMapper";
    }


    public List<ContactGroup> findContactGroups(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findContactGroups"), params);
        }
    }

    public List<Contact> findShareContactDetail(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findShareContactDetail"), params);
        }
    }

    public int findShareContactCount(QueryParameters params) {
        if (params == null) { // 纠错
            params = new QueryParameters();
        }
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findShareContactCount"), params);
        }
    }

    public void deleteByUserIds(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete(fullSqlId("deleteByUserIds"),params);
        }
    }
    public ContactShareGroup findShareContactGroupByPath(String path){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findShareContactGroupByPath"), path);
        }
    }

}

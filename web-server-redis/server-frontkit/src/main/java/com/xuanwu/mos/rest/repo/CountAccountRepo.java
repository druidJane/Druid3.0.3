package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.CountAccount;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Repository
public class CountAccountRepo extends GsmsMybatisEntityRepository<CountAccount> {
    private static final Logger logger = LoggerFactory.getLogger(CountAccountRepo.class);
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.CountAccountMapper";
    }

    //这个以后要在对应的service方法里改用事务
    public void addCountAccount(CountAccount countAccount,int userId) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            session.insert(fullSqlId("addCountAccount"), countAccount);
            int countAccountId = session.selectOne("findLastInsertId");
            if(userId>0){
                Map<String, Object> params = new HashMap<>();
                params.put("userId", userId);
                params.put("countAccountId", countAccountId);
                session.insert(fullSqlId("bindUserCountAccount"),params);
            }
            session.commit();
        } finally {
            session.close();
        }
    }

    public CountAccount getCountAccountByUserId(int userId){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne(fullSqlId("getCountAccountByUserId"), userId);
        } finally {
            session.close();
        }
    }

    public void updateCountAccount(CountAccount countAccount){
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            session.update(fullSqlId("updateCountAccount"),countAccount);
            session.commit();
        } finally {
            session.close();
        }
    }

    //这个以后要在对应的service方法里改用事务
    public void deleteByUserIds(List<Integer> userIds){
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            List<Integer> ids = session.selectList(fullSqlId("findCountAccountIdsByUserIds"), userIds);
            session.delete(fullSqlId("batchDelete"), ids);
            session.delete(fullSqlId("batchDeleteMap"), userIds);
            session.commit();
        } finally {
            session.close();
        }
    }

    public CountAccount getCountAccountById(int id) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            return session.selectOne(fullSqlId("getCountAccountById"), id);
        } finally {
            session.close();
        }
    }

    public int findCountAccountCount(Date modifyDate, int chargeType) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("modifyDate", modifyDate);
            params.put("chargeType", chargeType);
            return session.selectOne(fullSqlId("findCountAccountCount"),params);
        } finally {
            session.close();
        }
    }

    public List<CountAccount> findCountAccount(Date modifyDate, int chargeType) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("modifyDate", modifyDate);
            params.put("chargeType", chargeType);
            return session.selectList(fullSqlId("findCountAccount"),params);
        } finally {
            session.close();
        }
    }

    public CountAccount findCountAccountByName(String name) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            return session.selectOne(fullSqlId("findCountAccountByName"),name);
        } finally {
            session.close();
        }
    }
}

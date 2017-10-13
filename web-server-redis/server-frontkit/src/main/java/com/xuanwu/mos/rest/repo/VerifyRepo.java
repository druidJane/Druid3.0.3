package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Verify;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 审核信息
 * Created by Jiang.Ziyuan on 2017/3/30.
 */
@Repository
public class VerifyRepo extends GsmsMybatisEntityRepository<Verify> {
    private static final Logger logger = LoggerFactory.getLogger(VerifyRepo.class);

    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.VerifyMapper";
    }

    public Verify findVerifyByPackId(String packId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findVerifyByPackId"), packId);
        }
    }

    public void storeVerify(Verify verify) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Serializable> params = new HashMap<String, Serializable>();
            session.update(fullSqlId("storeVerify"), verify);
            session.commit(true);
        } catch (Exception e) {
            logger.error(" storeVerify failed: ", e);
            throw new RepositoryException(e);
        }
    }
}

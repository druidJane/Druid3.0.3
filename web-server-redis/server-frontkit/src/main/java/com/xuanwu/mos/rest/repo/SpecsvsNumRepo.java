package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.SpecsvsNumVo;
import com.xuanwu.mos.dto.QueryParameters;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Created by zhangz on 2017/3/23.
 */
@Repository
public class SpecsvsNumRepo extends GsmsMybatisEntityRepository<SpecsvsNumVo> {
    private Logger logger = LoggerFactory.getLogger(SpecsvsNumRepo.class.getName());

    public int findSpecsvsNumCount(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findSpecsvsNumCount"), params);
        }
    }

    public List<SpecsvsNumVo> findSpecsvsNumlists(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findSpecsvsNumLists"), params);
        }
    }
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.SpecsvsNumMapper";
    }
}




package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.db.ListMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.KeyWord;
import com.xuanwu.mos.dto.QueryParameters;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangz on 2017/4/11.
 */
@Repository
public class KeyWordRepo extends ListMybatisEntityRepository<KeyWord> {
    private Logger logger = LoggerFactory.getLogger(KeyWordRepo.class.getName());
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.KeyWordMapper";
    }
}

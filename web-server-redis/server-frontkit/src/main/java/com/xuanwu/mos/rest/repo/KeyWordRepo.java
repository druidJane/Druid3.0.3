package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.ListMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.KeyWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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

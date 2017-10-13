package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.UserTask;

import org.springframework.stereotype.Repository;

/**
 * Created by zhangz on 2017/5/16.
 */
@Repository
public class UserTaskRepo extends GsmsMybatisEntityRepository<UserTask> {
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.UserTaskMapper";
    }
}

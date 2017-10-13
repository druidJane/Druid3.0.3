package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.db.ListMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.WhitePhone;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangz on 2017/4/24.
 */
@Repository
public class WhitePhoneRepo extends GsmsMybatisEntityRepository<WhitePhone> {

    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.WhitePhoneMapper";
    }
}

package com.xuanwu.mos.rest.service.smsmgmt;

import com.xuanwu.mos.domain.entity.Verify;
import com.xuanwu.mos.exception.RepositoryException;

/**
 * 审核信息业务
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public interface VerifyService {
   Verify findVerifyByPackId(String packId);

    void storeVerify(Verify verify) throws RepositoryException;
}

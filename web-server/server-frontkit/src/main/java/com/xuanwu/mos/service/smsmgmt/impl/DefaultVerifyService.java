package com.xuanwu.mos.service.smsmgmt.impl;

import com.xuanwu.mos.domain.entity.Verify;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.VerifyRepo;
import com.xuanwu.mos.rest.service.smsmgmt.VerifyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 审核信息业务实现
 * Created by Jiang.Ziyuan on 2017/3/30.
 */
@Service("verifyService")
public class DefaultVerifyService implements VerifyService {
    private VerifyRepo verifyRepo;

    @Override
    public Verify findVerifyByPackId(String packId) {
        return verifyRepo.findVerifyByPackId(packId);
    }

    @Override
    public void storeVerify(Verify verify) throws RepositoryException {
        verifyRepo.storeVerify(verify);
    }

    @Autowired
    public void setVerifyDao(VerifyRepo verifyRepo) {
        this.verifyRepo = verifyRepo;
    }
}

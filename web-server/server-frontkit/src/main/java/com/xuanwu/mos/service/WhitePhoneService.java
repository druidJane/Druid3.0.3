package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.WhitePhone;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.WhitePhoneRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangz on 2017/4/24.
 */
@Service
public class WhitePhoneService {
    @Autowired
    private WhitePhoneRepo whitePhoneRepo;
    public int findWhitePhoneCount(QueryParameters params) {
        return whitePhoneRepo.findResultCount(params);
    }

    public List<WhitePhone> findWhitePhonelists(QueryParameters params){
        return whitePhoneRepo.findResults(params);
    }

    public void addWhitePhone(WhitePhone whitePhone) throws RepositoryException {
        whitePhoneRepo.insert(whitePhone);
    }

    public void delWhitePhone(Integer id, int enterpriseId) throws RepositoryException {
        whitePhoneRepo.deleteById(id,enterpriseId);
    }
}

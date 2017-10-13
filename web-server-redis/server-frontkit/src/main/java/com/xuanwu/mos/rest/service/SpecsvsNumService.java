package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.SpecsvsNumVo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.SpecsvsNumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangz on 2017/3/23.
 */
@Service
public class SpecsvsNumService {
    @Autowired
    private SpecsvsNumRepo specsvsNumRepo;

    public int findSpecsvsNumCount(QueryParameters params) {
        return specsvsNumRepo.findSpecsvsNumCount(params);
    }

    public List<SpecsvsNumVo> findSpecsvsNumLists(QueryParameters params) {
        return specsvsNumRepo.findSpecsvsNumlists(params);
    }
}

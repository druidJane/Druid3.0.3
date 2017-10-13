package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.CarrierTeleseg;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.CarrierTelesegRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangz on 2017/4/21.
 */
@Service
public class CarrierTelesegService {
    @Autowired
    private CarrierTelesegRepo carrierTelesegRepo;
    public int findCarrierTelesegCount(QueryParameters params) {
        return carrierTelesegRepo.findResultCount(params);
    }

    public List<CarrierTeleseg> findCarrier(QueryParameters params) {
        return carrierTelesegRepo.findResults(params);
    }

}

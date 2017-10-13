package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.Carrier;
import com.xuanwu.mos.domain.entity.CarrierPrice;
import com.xuanwu.mos.domain.entity.CarrierTeleseg;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.CarrierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangz on 2017/3/23.
 */
@Service
public class CarrierService {
    @Autowired
    private CarrierRepo carrierRepo;

    public List<CarrierPrice> findCarrierPrice(int enterpriseId, MsgTicket.MsgType msgType) {
        return carrierRepo.findCarrierPrice(enterpriseId, msgType);
    }
    public List<Carrier> findAllCarrier() {
        return carrierRepo.findAllCarrier();
    }

    public Long findTeleSegCount(String teleSeg){
        return carrierRepo.findTeleSegCount(teleSeg);
    }
}

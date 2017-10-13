package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.SpecsvsNumVo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by zhangz on 2017/4/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartServer.class)
@WebAppConfiguration
public class BizTypeServiceTest {
    @Autowired
    private BizTypeService bizTypeService;
    @Test
    public void testFindById(){
        BizType bizType = bizTypeService.findBizTypeById(32);
        System.out.println(bizType.getName());
    }
    @Test
    public void testGetCarrierDetailByBizId(){
        List<SpecsvsNumVo> list = bizTypeService.getCarrierDetailByBizId(238, null,2);
        System.out.println(list.size());
    }
}

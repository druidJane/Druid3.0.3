package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.entity.SpecsvsNumVo;
import com.xuanwu.mos.dto.QueryParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by zhangz on 2017/4/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartServer.class)
@WebAppConfiguration
public class SpecsvsNumServiceTest {
    @Autowired
    private SpecsvsNumService specsvsNumService;

    @Test
    public void testFindSpecsvsNumLists(){
        QueryParameters params = new QueryParameters();
        params.addParam("enterpriseId",3);
        List<SpecsvsNumVo> list = specsvsNumService.findSpecsvsNumLists(params);
        Assert.assertNotNull(list);
        Assert.assertNotEquals(0,list.size());
    }

}

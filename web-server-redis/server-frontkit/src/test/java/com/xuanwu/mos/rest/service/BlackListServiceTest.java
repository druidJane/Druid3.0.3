package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.entity.BlackList;
import com.xuanwu.mos.dto.QueryParameters;

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
public class BlackListServiceTest {
    @Autowired
    private BlackListService blackListService;

    @Test
    public void testIsExist(){
        BlackList blackList = new BlackList();
        blackList.setType(0);
        blackList.setTarget(0);
        blackList.setPhone("15512344329");
        boolean exist = blackListService.isExist(blackList);
        //Assert.assertTrue(exist);
    }
    @Test
    public void testFindBlacklistsDetail(){
        QueryParameters params = new QueryParameters();
        params.addParam("type",9);
        int total = blackListService.findBlacklistCount(params);
        List<BlackList> list = blackListService.findBlacklistsDetail(params);
    }
}

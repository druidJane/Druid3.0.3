package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.entity.WhitePhone;
import com.xuanwu.mos.dto.QueryParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by zhangz on 2017/4/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartServer.class)
@WebAppConfiguration
public class WhitePhoneServiceTest {
    @Autowired
    private WhitePhoneService whitePhoneService;
    @Test
    public void testFindWhitePhonelists(){
        QueryParameters params = new QueryParameters();
        List<WhitePhone> list = whitePhoneService.findWhitePhonelists(params);
        System.out.println("size="+list.size());
    }
}

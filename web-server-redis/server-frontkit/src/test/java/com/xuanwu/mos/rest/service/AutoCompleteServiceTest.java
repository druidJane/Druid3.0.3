package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangz on 2017/5/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartServer.class)
@WebAppConfiguration
public class AutoCompleteServiceTest {
    @Autowired
    private AutoCompleteService autoCompleteService;
    @Test
    public void testAutoCompleteUserName(){
        List<Map<String, String>> admin = autoCompleteService.autoCompleteUserName("admin", 238);
        System.out.println(admin.size());
    }
}

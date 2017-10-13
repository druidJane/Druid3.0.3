package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.entity.KeyWord;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.utils.DateUtil;

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
public class KeyWordServiceTest {
    @Autowired
    private KeyWordService keyWordService;
    @Test
    public void testFindCount(){
        QueryParameters params = new QueryParameters();
        params.addParam("keywordName","拉333登");
        int keywordCount = keyWordService.findKeywordCount(params);
        Assert.assertNotNull(keywordCount);
        Assert.assertNotEquals(0,keywordCount);
    }
    @Test
    public void testFindList(){
        QueryParameters params = new QueryParameters();
        params.addParam("keywordName","拉333登");
        List<KeyWord> list = keyWordService.findKeywords(params);
        Assert.assertNotNull(list);
        Assert.assertNotEquals(0,list.size());
    }
    @Test
    public void testIsExists(){
        KeyWord query = new KeyWord();
        query.setKeywordName("拉333登");
        KeyWord result = keyWordService.isExists(query);
        Assert.assertTrue(result==null);
    }
    @Test
    public void testAddOrUpdateKeyword() throws RepositoryException {
        KeyWord add = new KeyWord();
        add.setKeywordName("测试插入数据"+ DateUtil.getCurrentDate());
        SimpleUser user = new SimpleUser();
        add.setUserId(555);
        add.setTargetId(555);
        boolean result = keyWordService.addOrUpdateKeyword(add,  null);
        add.setKeywordName("测试更新数据"+ DateUtil.getCurrentDate());
        result = keyWordService.addOrUpdateKeyword(add, 811);
    }
    @Test
    public void testDelKeyword() throws RepositoryException {
        Integer[] id  = new Integer[]{813};
        keyWordService.delKeyword(id);
    }
}

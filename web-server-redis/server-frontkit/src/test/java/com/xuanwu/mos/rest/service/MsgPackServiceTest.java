package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.entity.MmsMsgPack;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.utils.DateUtil;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;


/**
 * Created by zhangz on 2017/7/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartServer.class)
@WebAppConfiguration
public class MsgPackServiceTest {
    @Autowired
    private MsgPackService msgPackService;
    @Test
    public void testFindPackByUuid(){
        MmsMsgPack packByUuid = msgPackService.findPackByUuid(new Date(), MsgContent.MsgType.LONGSMS.getIndex(), "2b297fc0-8539-40ff-9f24-721536787f92");
        System.out.println(packByUuid.getBatchName());
    }
    @Test
    public void testDate(){
        Date date = new Date();
        FastDateFormat yyyyMMddHHmmss = FastDateFormat.getInstance("yyyyMMddHHmmss");
        String format = DateUtil.format(date, DateUtil.DateTimeType.DateTime);
        System.out.println("format="+format);
        System.out.println("yyyyMMddHHmmss="+yyyyMMddHHmmss.format(date));
    }
}

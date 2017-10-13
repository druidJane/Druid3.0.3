package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.rest.service.smsmgmt.impl.SendSmsService;
import com.xuanwu.mos.vo.MmsInfoVo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhangz on 2017/6/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartServer.class)
@WebAppConfiguration
public class SendSmsServiceTest {
    @Autowired
    private SendSmsService sendSmsService;
    @Test
    public void testSendMsg() throws Exception {
        List<Contact> phoneList = new ArrayList<Contact>();
        Contact contact = new Contact();
        contact.setPhone("15588889999");
        phoneList.add(contact);
        MmsInfoVo mmsInfoVo = new MmsInfoVo();
        mmsInfoVo.setBizTypeId(2);
        //sendSmsService.sendMsg("testtest",phoneList,mmsInfoVo, MsgContent.MsgType.LONGSMS);
    }

}
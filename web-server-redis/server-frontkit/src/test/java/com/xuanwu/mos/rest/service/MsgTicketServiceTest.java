package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.StartServer;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.service.msgservice.MsgTicketService;
import com.xuanwu.msggate.common.protobuf.CommonItem;
import com.xuanwu.msggate.common.zip.ZipUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangz on 2017/6/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartServer.class)
@WebAppConfiguration
public class MsgTicketServiceTest {
    @Autowired
    private MsgTicketService msgTicketService;
    @Test
    public void testFindFailedTicketByPackId(){

        QueryParameters query = new QueryParameters();
        query.addParam("packId","6157127");
        Parameters params = new Parameters(query);
        params.setQueryTime(new Date());
        params.setMsgType(MsgTypeEnum.SMS.getIndex());
        List<MsgTicket> list = msgTicketService.findFailedTicketByPackId(params);
        for(MsgTicket msgTicket:list){
            String content = "";
            if(msgTicket.getSmsType() == 0 && msgTicket.getSendType() == 0){
                //群发，拿frame短信内容
                try {
                    byte[] bytes = ZipUtil.unzipByteArray(msgTicket.getFrameContent());
                    CommonItem.MassPack massPack = CommonItem.MassPack.parseFrom(bytes);
                    content = massPack.getContent().getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //组发，拿ticket长短信内容
                content = msgTicket.getSmsContentSkipSign();
            }
            System.out.println(content);
        }
    }
}

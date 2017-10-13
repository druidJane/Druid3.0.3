package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.StartServer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by 郭垚辉 on 2017/4/6.
 */
// SpringJUnit支持，由此引入Spring-Test框架支持！
@RunWith(SpringJUnit4ClassRunner.class)
// 指定我们SpringBoot工程的Application启动类
@SpringApplicationConfiguration(classes = StartServer.class)
// 由于是Web项目，Junit需要模拟ServletContext，
// 因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class MsgPackRepoTest {


//    @Test
//    public void findSendMsgPackByDefault() throws Exception {
//        HashMap<Object, Object> params = new HashMap<>();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE, 1);
//        params.put("beginTime", calendar.getTime());
//        params.put("msgType", 2);
//        List<MsgPackDTO> sendMsgPackByDefault = msgPackRepo.(params);
//        System.out.println(sendMsgPackByDefault);
//    }

    @Test
    public void findSendMsgPackByBatchState() throws Exception {
    }

    /**
     * 使用该方法说明下如何使用shardserver
     * mos库中的四大表是：pack, frame, ticket, statereport,因此对于这四大表我们在使用的时候会使用分表模块(shardserver模块)
     * 使用方法如下：
     * 第一步：使用Parameters类来包装你的参数
     * 第二步：在
     */
//    @Test
//    public void selectCountFromFrame() throws Exception {
//        Parameters params = new Parameters();
//        HashMap<Object, Object> otherParams = new HashMap<>();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE, 1);
//        // 设置我们要查询的表为 gsms_msg_frame_mms
//        params.setMsgType(MsgTypeEnum.MMS.getIndex());
//        //
//        params.setQueryTime(calendar.getTime());
//
//        otherParams.put("packId", 168000);
//        // 如果需要分表的话，必须要使用以下两个
//        otherParams.put("msgType", 2);
//        otherParams.put("beginTime", calendar.getTime());
//        params.setOther(otherParams);
//        SendCountDTO sendCountDTO = msgPackRepo.(params);
//        System.out.println(sendCountDTO);
//    }
    @Test
    public void findMmsPhoneList() throws Exception {
    }

    @Test
    public void findPackRecordCount() throws Exception {
    }

}


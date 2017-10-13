package com.xuanwu.mos.rest.service.msgservice;

import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.entity.PackStatInfo;
import com.xuanwu.mos.dto.QueryParameters;

import java.util.Date;
import java.util.List;

/**
 * 短信话单数据服务
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public interface MsgTicketService {

    List<PackStatInfo> findPackStatInfo(String packId, MsgContent.MsgType msgType, Date postTime);
    /**
     * 查找父短信
     *
     * @param id
     * @return
     */
    List<MsgTicket> findParentMsgTicketsById(long id,int msgType,Date postTime);

    /**
     * 根据条件查询某一天全部的数据的总数
     */
    int findAllMmsNumberRecordCount(Parameters params);

    /**
     * 根据条件查询某一天全部的数据
     */
    List<MsgTicket> findAllMmsNumberRecord(Parameters params);

    /**
     * 查询某一段时间内，每一天的符合query中条件的记录，放在int[]数组中
     * 例如这个时间段是2017-06-18 00:10:00 到 2017-06-22 12:12:12
     * 那么就会分为时间段:
     * 第一段时间： 起始: 2017-06-18 00:00 截止: 2017-06-18 23:59:59 符合数据存放在 int[0]
     * 第二段时间： 起始: 2017-06-19 00:00 截止: 2017-06-19 23:59:59 符合数据存放在 int[1]
     * 第三段时间： 起始: 2017-06-20 00:00 截止: 2017-06-20 23:59:59 符合数据存放在 int[2]
     * 第四段时间： 起始: 2017-06-21 00:00 截止: 2017-06-21 23:59:59 符合数据存放在 int[3]
     * 第五段时间： 起始: 2017-06-22 00:00 截止: 2017-06-22 12:12:12 符合数据存放在 int[4]
     * 还有一个int[5] = int[0] + int[1] + int[2] + int[3] + int[4];
     * 所以这个int型数组会有6位，每一位放一段时间的符合查询的数据，最后一位放前面五段时间的查询数据
     * @param query
     * @param params
     * @return
     */
    int[] findAllMmsNumberRecordCountMultiDays(QueryParameters query, Parameters params);

    /**
     * 查询某一段时间内，每一天的符合query中条件的记录，放在int[]数组中
     * 例如这个时间段是2017-06-18 00:10:00 到 2017-06-22 12:12:12
     * 那么就会分为时间段:
     * 第一段时间： 起始: 2017-06-18 00:00 截止: 2017-06-18 23:59:59 符合数据存放在 int[0]
     * 第二段时间： 起始: 2017-06-19 00:00 截止: 2017-06-19 23:59:59 符合数据存放在 int[1]
     * 第三段时间： 起始: 2017-06-20 00:00 截止: 2017-06-20 23:59:59 符合数据存放在 int[2]
     * 第四段时间： 起始: 2017-06-21 00:00 截止: 2017-06-21 23:59:59 符合数据存放在 int[3]
     * 第五段时间： 起始: 2017-06-22 00:00 截止: 2017-06-22 12:12:12 符合数据存放在 int[4]
     * 还有一个int[5] = int[0] + int[1] + int[2] + int[3] + int[4];
     * 所以这个int型数组会有6位，每一位放一段时间的符合查询的数据，最后一位放前面五段时间的查询数据
     * @param query
     * @param params
     * @return
     */
    List<MsgTicket> findAllMmsNumberRecordMultiDays(QueryParameters query, Parameters params, int offset, int count, int[] totals);

    int findAbandonPackTicketsCount(Parameters params);

    List<MsgTicket> findAbandonPackTickets(Parameters params);

    List<MsgTicket> findTicketsByPackId(String packId, int msgType, Date postTime, MsgTicket.MsgSub msgSub, int fetchSize, String orderRule) throws Exception;

    List<MsgTicket> findFailedTicketByPackId(Parameters params);
}

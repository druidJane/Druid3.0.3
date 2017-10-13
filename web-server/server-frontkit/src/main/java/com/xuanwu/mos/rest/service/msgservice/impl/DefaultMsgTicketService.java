package com.xuanwu.mos.rest.service.msgservice.impl;

import com.xuanwu.mos.config.ShardingSupport;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.entity.PackStatInfo;
import com.xuanwu.mos.domain.repo.MsgPackRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.CarrierChannelRepo;
import com.xuanwu.mos.rest.repo.MsgTicketRepo;
import com.xuanwu.mos.rest.service.msgservice.MsgTicketService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.ListUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 短信话单数据服务
 * Created by Jiang.Ziyuan on 2017/3/30.
 */
@Service("msgTicketService")
public class DefaultMsgTicketService implements MsgTicketService {

    private final static Logger logger =  LoggerFactory.getLogger(DefaultMsgTicketService.class);
    private MsgTicketRepo msgTicketRepo;

    private CarrierChannelRepo carrierChannelRepo;

    @Autowired
    private ShardingSupport shardingSupport;
    @Autowired
    private MsgPackRepo msgPackRepo;

    public List<PackStatInfo> findPackStatInfo(String packId, MsgContent.MsgType msgType, Date postTime) {
        if (StringUtils.isBlank(packId))
            packId = null;
        return msgTicketRepo.findPackStatInfo(packId, msgType, postTime);
    }


    @Autowired
    public void setMsgTicketRepo(MsgTicketRepo msgTicketRepo) {
        this.msgTicketRepo = msgTicketRepo;
    }

    @Autowired
    public void setCarrierChannelRepo(CarrierChannelRepo carrierChannelRepo) {
        this.carrierChannelRepo = carrierChannelRepo;
    }

    //------------------------ add by guoyaohui ----------------------------

    // 根据query中的packId，msgType，，获取该批次所有号码
    public int findAllMmsNumberRecordCount(Parameters params) {
        QueryParameters query = params.getQuery();
        String batchName = (String) query.getParams().get("batchName");
        String packId = (String) query.getParams().get("packId");
        List<String> packIds = Collections.emptyList();

        if (StringUtils.isNotBlank(batchName) && StringUtils.isBlank(packId)) {
            packIds = msgPackRepo.findPackIdsByName(params);
            if (ListUtil.isBlank(packIds)) {
                return 0;
            }
            if (packIds.size() == 1) {
                packId = packIds.get(0);
            }
        }
        query.addParam("packId", packId);
        params.setQuery(query);
        return msgTicketRepo.findAllMmsNumberRecordCount(params);
    }

    public List<MsgTicket> findAllMmsNumberRecord(Parameters params) {
        QueryParameters query = params.getQuery();
        String batchName = (String) query.getParams().get("batchName");
        String packId = (String) query.getParams().get("packId");
        List<String> packIds = Collections.emptyList();

        if (StringUtils.isNotBlank(batchName) && StringUtils.isBlank(packId)) {
            packIds = msgPackRepo.findPackIdsByName(params);
            if (ListUtil.isBlank(packIds)) {
                return Collections.emptyList();
            }
            if (packIds.size() == 1) {
                packId = packIds.get(0);
            }
        }
        query.addParam("packId", packId);
        params.setQuery(query);
        return msgTicketRepo.findAllMmsNumberRecord(params);
    }
    @Override
    public List<MsgTicket> findParentMsgTicketsById(long id,int msgType,Date postTime) {
        return msgTicketRepo.findParentMsgTicketsById(id,msgType,postTime);
    }
    public int[] findAllMmsNumberRecordCountMultiDays(QueryParameters query, Parameters params) {
        Date beginTime = DateUtil.parseDateString((String) query.getParams().get("beginTime"));
        Date endTime = DateUtil.parseDateString((String) query.getParams().get("endTime"));

        List<Date[]> days = shardingSupport.splitByDay(beginTime, endTime);
        int[] counts = new int[days.size() + 1];

        int sum = 0;
        int countCurDay = 0;
        for (int i = 0; i < days.size(); i++) {
            query.addParam("beginTime", days.get(i)[0]);
            query.addParam("endTime", days.get(i)[1]);
            params.setQuery(query);
            params.setQueryTime(days.get(i)[0]);
            countCurDay = findAllMmsNumberRecordCount(params);
            sum += countCurDay;
            counts[i] = countCurDay;
        }
        counts[counts.length - 1] = sum;  //最后一位放总数
        return counts;
    }

    public List<MsgTicket> findAllMmsNumberRecordMultiDays(QueryParameters query, Parameters params, int offset, int count, int[] totals) {
        /*
        Date beginTime = (Date) query.getParams().get("beginTime");
        Date endTime = (Date) query.getParams().get("endTime");
        */
        Date beginTime = DateUtil.parseDateString((String) query.getParams().get("beginTime"));
        Date endTime = DateUtil.parseDateString((String) query.getParams().get("endTime"));

        List<MsgTicket> msgTickets = new ArrayList<>();

        List<Date[]> days = shardingSupport.splitByDay(beginTime, endTime);

        //为避免无效查询，直接从需要查询的日期中查询即可
        for (int i = totals.length - 2; i >= 0; i--) {
            /*
            //如果需要返回的数据不在该天，则直接跳过，记录坐标，累加
            if (totals[i] == 0) {
                continue;
            }
            if (offset > totals[i]) {
                offset -= totals[i];
                continue;
            }*/
            // 覆盖掉PageReqt中的beginTime、endTime
            query.addParam("beginTime", days.get(i)[0]);
            query.addParam("endTime", days.get(i)[1]);

            // 覆盖掉page中的limit中的起始值
            query.getPage().setFrom(offset);
            query.getPage().setSize(count);

            // 修改要查询的数据表
            params.setQuery(query);
            params.setQueryTime(days.get(i)[0]);
            List<MsgTicket> tickets = findAllMmsNumberRecord(params);
            msgTickets.addAll(tickets);
            /*
            if (msgTickets.size() >= count) {  //查询到的结果已经满足条数
                return msgTickets;
            }
            offset = 0;
            count -= msgTickets.size();*/
        }
        return msgTickets;
    }

    @Override
    public int findAbandonPackTicketsCount(Parameters params) {
        return msgTicketRepo.findAbandonPackTicketsCount(params);
    }

    @Override
    public List<MsgTicket> findAbandonPackTickets(Parameters params) {
        return msgTicketRepo.findAbandonPackTickets(params);
    }

    @Override
    public List<MsgTicket> findTicketsByPackId(String packId, int msgType, Date postTime, MsgTicket.MsgSub msgSub, int fetchSize, String
            orderRule) throws Exception {
        return msgTicketRepo.findTicketsByPackId(packId, msgType, postTime, msgSub, fetchSize, orderRule);
    }
    // endregion
    @Override
    public List<MsgTicket> findFailedTicketByPackId(Parameters params) {
        List<MsgTicket> failList = msgTicketRepo.findFailedTicketByPackId(params);
        for(MsgTicket fail:failList){

        }
        return msgTicketRepo.findFailedTicketByPackId(params);
    }
}

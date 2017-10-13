package com.xuanwu.mos.rest.service.msgservice.impl;


import com.xuanwu.mos.config.ShardingSupport;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MmsMsgPack;
import com.xuanwu.mos.domain.entity.MsgPack;
import com.xuanwu.mos.domain.repo.MsgPackRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.utils.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批次(短信包)数据服务
 * Created by Jiang.Ziyuan on 2017/4/11.
 */
@Service("msgPackService")
public class DefaultMsgPackService implements MsgPackService {

    @Autowired
    private MsgPackRepo msgPackDao;
    @Autowired
    private ShardingSupport shardingSupport;

    /**
     * 查找批次(短信包)
     */
    @Override
    public MsgPack findMsgPackById(String id, int msgType, Date postTime) {
        return msgPackDao.findMsgPackById(id, msgType, postTime);
    }

    @Override
    public boolean deleteVerifyPack(String packId) {
        return msgPackDao.deleteVerifyPack(packId);
    }


    @Override
    public MmsMsgPack findVerifyMsgPackById(Parameters params) {
        MmsMsgPack verifyPack = msgPackDao.findVerifyMsgPackById(params);
        if (verifyPack != null) {
            return verifyPack;
        }
        return null;
    }

    // region 郭垚辉修改

    /**
     * 获取多天的短彩信发送记录的总数
     */
    @Override
    public int[] findMmsMsgPacksCountMultiDays(QueryParameters query, Parameters params) {
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
            countCurDay = msgPackDao.findMmsMsgPacksCount(params);
            sum += countCurDay;
            counts[i] = countCurDay;
        }
        counts[days.size()] = sum;
        return counts;
    }

    @Override
    public List<MmsMsgPack> findMmsMsgPacksMultiDays(QueryParameters query, Parameters params, int offset, int count, int[] totals) {

        Date beginTime = DateUtil.parseDateString((String) query.getParams().get("beginTime"));
        Date endTime = DateUtil.parseDateString((String) query.getParams().get("endTime"));
        List<MmsMsgPack> mmsMsgPacks = new ArrayList<>();
        List<Date[]> days = shardingSupport.splitByDay(beginTime, endTime);
        //为避免无效查询，直接从需要查询的日期中查询即可
        for (int i = totals.length - 2; i >= 0; i--) {
            //如果需要返回的数据不在该天，则直接跳过，记录坐标，累加
            /*
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
            List<MmsMsgPack> packs = msgPackDao.findMmsMsgPacks(params);
            mmsMsgPacks.addAll(packs);
            /*
            if (packs.size() >= count) {  //查询到的结果已经满足条数
                return mmsMsgPacks;
            }
            offset = 0;
            count -= packs.size();*/
        }
        return mmsMsgPacks;
    }

    public int findMmsWaitAuditPacksCount(QueryParameters params) {
        return msgPackDao.findMmsWaitAuditPacksCount(params);
    }

    public List<MmsMsgPack> findMmsWaitAuditPacks(QueryParameters params) {
        return msgPackDao.findMmsWaitAuditPacks(params);
    }


    @Override
    public boolean updateAuditPackState(QueryParameters params) {
        return msgPackDao.updateAuditPackState(params);
    }

    @Override
    public MmsMsgPack findFrontAuditRecord(QueryParameters params) {
        return msgPackDao.findFrontAuditRecord(params);
    }

    @Override
    public MmsMsgPack findBackAuditRecord(QueryParameters params) {
        return msgPackDao.findBackAuditRecord(params);
    }

    @Override
    public MmsMsgPack findSuccessCount(Parameters params) {
        return msgPackDao.findSuccessCount(params);
    }

    @Override
    public MmsMsgPack findFailCount(Parameters params) {
        return msgPackDao.findFailCount(params);
    }

    @Override
    public MmsMsgPack findPackByUuid(Date postTime, int msgType, String uuid) {
        Parameters params = new Parameters();
        params.setMsgType(msgType);
        params.setQueryTime(postTime);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.addParam("uuid",uuid);
        params.setQuery(queryParameters);
        return msgPackDao.findMmsMsgPackById(params);
    }

    @Override
    public MmsMsgPack findMmsMsgPackById(Parameters params) {
        return msgPackDao.findMmsMsgPackById(params);
    }
}

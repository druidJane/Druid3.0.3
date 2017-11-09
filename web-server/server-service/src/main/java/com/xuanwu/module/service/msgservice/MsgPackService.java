package com.xuanwu.module.service.msgservice;

import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MmsMsgPack;
import com.xuanwu.mos.domain.entity.MsgPack;
import com.xuanwu.mos.dto.QueryParameters;

import java.util.Date;
import java.util.List;

/**
 * 批次(短信包)数据服务
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public interface MsgPackService {

    /**
     * 查找批次(短信包)
     */
    MsgPack findMsgPackById(String id, int msgType, Date postTime);


    boolean deleteVerifyPack(String packId);

//    ----------------------------add by guoyaohui -------------------------

    /**
     * 跟据packId获取相应的pack包
     */
    MmsMsgPack findVerifyMsgPackById(Parameters params);

    /**
     * 根据packId获取数据包
     * 参数：packId,postTime,msgType
     */
    MmsMsgPack findMmsMsgPackById(Parameters params);


    /**
     * 获取连续多天的发送记录的数量
     */
    int[] findMmsMsgPacksCountMultiDays(QueryParameters query, Parameters params);

    /**
     * 获取连续多天的发送记录的具体数据
     */
    List<MmsMsgPack> findMmsMsgPacksMultiDays(QueryParameters query, Parameters params, int offset, int count, int[] totals);

    /**
     * 获取审核短信的数量
     */
    int findMmsWaitAuditPacksCount(QueryParameters params);

    /**
     * 获取审核彩信的数据
     */
    List<MmsMsgPack> findMmsWaitAuditPacks(QueryParameters params);

    /**
     * 审核彩信，更新状态
     */
    boolean updateAuditPackState(QueryParameters params);

    /**
     * 获取前台审核信息
     */
    MmsMsgPack findFrontAuditRecord(QueryParameters params);

    /**
     * 获取后台审核信息
     */
    MmsMsgPack findBackAuditRecord(QueryParameters params);

    /**
     * 获取批次返回成功状态报告数量
     * @param params
     * @return
     */
    MmsMsgPack findSuccessCount(Parameters params);

    /**
     * 获取批次返回失败状态报告数量
     * @param params
     * @return
     */
    MmsMsgPack findFailCount(Parameters params);

    /**
     * 根据uuid获取pack信息
     * @param postTime
     * @param msgType
     * @param uuid
     * @return
     */
    MmsMsgPack findPackByUuid(Date postTime, int msgType, String uuid);

}

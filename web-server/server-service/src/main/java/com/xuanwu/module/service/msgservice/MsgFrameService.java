package com.xuanwu.module.service.msgservice;

import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgFrame;
import com.xuanwu.mos.domain.entity.PackStatInfo;

import java.util.Date;
import java.util.List;

/**
 * 短信帧数据服务
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public interface MsgFrameService {

    /**
     *
     * @param id
     * @param msgType
     * @param postTime
     * @return
     */
    MsgFrame findMsgFrameById(long id, int msgType, Date postTime);

    /**
     * 帧类型短信总数统计
     *
     * @param packId 批次ID
     * @return only(bizForm, msgCount)
     */
    List<PackStatInfo> findPackStatInfos(String packId, int msgType, Date postTime);



    MsgFrame findSingleMsgFrameByPackId(Parameters params);

    /**
     * 根据packId 获取所有的帧中的号码
     */
    List<MsgFrame> checkRecordDetail(Parameters params);


    /**
     * 通过packId获取该批次短彩信中被过滤掉的条数,
     * 因为如果短彩信是不通过审核的话，该批次的短彩信最终会停留在frame中，而不会到达ticket表
     * 因此需要从frame表中获取总数
     * @param params
     * @return
     */
    int findMsgCountByPackId(Parameters params);

    /**
     * 根据packId 获取所有的未处理的帧
     */
    List<MsgFrame> findNotHandleFrame(String packId, Date postTime, int msgType);

}

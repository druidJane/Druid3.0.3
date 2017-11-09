package com.xuanwu.module.service.msgservice.impl;

import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgFrame;
import com.xuanwu.mos.domain.entity.PackStatInfo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.rest.repo.MsgFrameRepo;
import com.xuanwu.mos.rest.service.msgservice.MsgFrameService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 短信帧数据服务
 * Created by Jiang.Ziyuan on 2017/4/10.
 */
@Service("msgFrameService")
public class DefaultMsgFrameService implements MsgFrameService {

    Logger logger = Logger.getLogger(String.valueOf(DefaultMsgFrameService.class));

    @Autowired
    private MsgFrameRepo msgFrameDao;

    @Override
    public MsgFrame findMsgFrameById(long id, int msgType, Date postTime) {
        return msgFrameDao.findMsgFrameById(id, msgType, postTime);
    }

    /**
     * 帧类型短信总数统计
     *
     * @param packId 批次ID
     * @return only(bizForm, msgCount)
     */
    @Override
    public List<PackStatInfo> findPackStatInfos(String packId, int msgType, Date postTime) {
        return msgFrameDao.findPackStatInfos(packId, msgType, postTime);
    }


    /**
     * 该方法作用：
     * 第一种：获取彩信标题时，虽然一个packId可能会对应多个的frame，
     * 但是这些frame中的彩信标题都是一致的，因此可以只取一个
     */
    @Override
    public MsgFrame findSingleMsgFrameByPackId(Parameters params) {
        return msgFrameDao.findSingleMsgFrameByPackId(params);
    }

    @Override
    public List<MsgFrame> checkRecordDetail(Parameters params) {
        return msgFrameDao.checkRecordDetail(params);
    }

    @Override
    public int findMsgCountByPackId(Parameters params) {
        return msgFrameDao.findMsgCountByPackId(params);
    }

    @Override
    public List<MsgFrame> findNotHandleFrame(String packId,Date postTime,int msgType) {
        if(StringUtils.isEmpty(packId)){
            throw new BusinessException("packId cannot be null!");
        }
        QueryParameters query =  new QueryParameters();
        query.addParam("packId",packId);
        Parameters params = new Parameters(query, msgType, postTime);
        return msgFrameDao.findNotHandleFrame(params);
    }
}

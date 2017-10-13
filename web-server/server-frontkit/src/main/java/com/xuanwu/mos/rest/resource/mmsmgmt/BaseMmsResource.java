package com.xuanwu.mos.rest.resource.mmsmgmt;

import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MmsContent;
import com.xuanwu.mos.domain.entity.MsgFrame;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.service.msgservice.MsgFrameService;
import com.xuanwu.mos.utils.JsonUtil;
import com.xuanwu.mos.utils.MmsUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.msggate.common.protobuf.CommonItem.GroupPack;
import com.xuanwu.msggate.common.protobuf.CommonItem.MassPack;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgContent;
import com.xuanwu.msggate.common.zip.ZipUtil;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by 郭垚辉 on 2017/4/26.
 */
public class BaseMmsResource {

    @Autowired
    protected MsgFrameService msgFrameService;

    /**
     *通过packId来获取彩信预览
     */
    protected JsonResp mmsPreviewByPackId(String packId, Date postTime) {
        QueryParameters query = new QueryParameters();
        query.addParam("packId",packId);
        query.addParam("enterpriseId", SessionUtil.getCurUser().getEnterpriseId());
        Parameters params = new Parameters(query);
        params.setQueryTime(postTime);
        params.setMsgType(MsgTypeEnum.MMS.getIndex());
        MsgFrame frame = msgFrameService.findSingleMsgFrameByPackId(params);
        if (frame != null) {
            return mmsPreviewByFrameId(frame.getId(),postTime);
        }
        return JsonResp.success();
    }

    /**
     * 彩信预览
     */
    protected JsonResp mmsPreviewByFrameId(long frameId,Date postTime) {
        MsgFrame frame = msgFrameService.findMsgFrameById(frameId, MsgContent.MsgType.MMS.getIndex(),postTime);
        try {
            MsgContent.MsgType mms = MsgContent.MsgType.MMS;
            byte[] data = ZipUtil.unzipByteArray(frame.getContent());
            PMsgContent msgContent;
            MmsContent mmsContent = new MmsContent();
            switch (frame.getSendType()) {
                case MASS:
                    MassPack massPack = MassPack.newBuilder().mergeFrom(data).build();
                    msgContent = new PMsgContent(mms, massPack.getContent());
                    mmsContent = MmsUtil.fromFrameContent(msgContent);
                    break;
                case GROUP:
                    GroupPack groupPack = GroupPack.newBuilder().mergeFrom(data).build();
                    msgContent = new PMsgContent(mms, groupPack.getItems(0).getContent());
                    mmsContent = MmsUtil.fromFrameContent(msgContent);
                    break;
            }
            return JsonResp.success(JsonUtil.serialize(mmsContent));
        } catch (Exception e) {
        }
        return JsonResp.fail("彩信预览异常");
    }
}

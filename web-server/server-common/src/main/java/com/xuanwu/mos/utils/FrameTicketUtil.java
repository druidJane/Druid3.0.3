package com.xuanwu.mos.utils;

import com.xuanwu.mos.domain.entity.MsgFrame;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.GroupMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.MassMsgFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信帧和话单操作工具类
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public class FrameTicketUtil {

    /**
     * 解压短信帧中的话单数据
     *
     * @param frame
     * @return
     */
    public static List<MsgTicket> unZipSMSContent(MsgFrame frame) {
        List<MsgTicket> list = new ArrayList<MsgTicket>();

        long frameId = frame.getId();
        int state = frame.getState();
        int bizForm = frame.getBizForm();
        if (frame.getSendType() == MsgPack.SendType.MASS) {
            MassMsgFrame mmf = new MassMsgFrame();
            mmf.setMsgType(frame.getMsgType());
            mmf.setPack(frame.getContent());
            for (MsgSingle msg : mmf.getAllMsgSingle()) {
                MsgTicket t = new MsgTicket();
                t.setFrameId(frameId);
                t.setPhone(msg.getPhone());
                if (frame.getMsgType().getIndex() == MsgTypeEnum.MMS.getIndex()) {
                    t.setMmsTitle(frame.getTitle());
                } else {
                    t.setSmsContent(msg.getContent().getContent());
                }
                t.setCustomMsgId(msg.getCustomMsgID());
                t.setSmsType(mmf.getMsgType().getIndex());
                t.setFrameState(state);
                t.setFrameBizForm(bizForm);
                list.add(t);
            }
        } else {
            GroupMsgFrame gmf = new GroupMsgFrame();
            gmf.setMsgType(frame.getMsgType());
            gmf.setPack(frame.getContent());
            for (MsgSingle msg : gmf.getAllMsgSingle()) {
                MsgTicket t = new MsgTicket();
                t.setFrameId(frameId);
                t.setPhone(msg.getPhone());
                if (frame.getMsgType().getIndex() == MsgTypeEnum.MMS.getIndex()) {
                    t.setMmsTitle(frame.getTitle());
                } else {
                    t.setSmsContent(msg.getContent().getContent());
                }
                t.setCustomMsgId(msg.getCustomMsgID());
                t.setSmsType(gmf.getMsgType().getIndex());
                t.setFrameState(state);
                t.setFrameBizForm(bizForm);
                list.add(t);
            }
        }

        return list;
    }


}

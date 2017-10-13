package com.xuanwu.mos.utils;

import com.xuanwu.mos.domain.entity.MmsMsgPack;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.BizFormEnum;
import com.xuanwu.mos.vo.MsgPackVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 由于在短彩信中共同使用了相同形式的Vo，因此在这里进行了适当的封装，能够直接调用
 *  有利于降低写重复vo的代码。
 * Created by 郭垚辉 on 2017/5/31.
 */
public class VoUtil {

    /**
     * 组装短彩信检核信息vo
     * @param msgPackVo
     * @param mmsMsgPack
     * @return
     */
    public static void assembleCheckMsgInfoVo (MsgPackVo msgPackVo,MmsMsgPack mmsMsgPack){
        msgPackVo.setSuccessTickets(mmsMsgPack.getSuccess());
        msgPackVo.setFailedTickets(mmsMsgPack.getFailedTickets());
        msgPackVo.setSendedTickets(mmsMsgPack.getSendedTickets());
        msgPackVo.setRepeatTickets(mmsMsgPack.getRepeatTickets());
        msgPackVo.setIllegalKeyTickets(mmsMsgPack.getIllegalKeyTickets());
        msgPackVo.setIllegalTickets(mmsMsgPack.getIllegalTickets());
        msgPackVo.setBlackTickets(mmsMsgPack.getBlackTickets());
        msgPackVo.setFrontAuditUser(mmsMsgPack.getFrontAuditUser());
        msgPackVo.setFrontAuditTime(mmsMsgPack.getFrontAuditTime());
        msgPackVo.setFrontAuditRemark(mmsMsgPack.getFrontAuditRemark());
        msgPackVo.setBackAuditRemark(mmsMsgPack.getBackAuditRemark());
        msgPackVo.setBackAuditTime(mmsMsgPack.getBackAuditTime());
        msgPackVo.setBackAuditUser(mmsMsgPack.getBackAuditUser());
        if (mmsMsgPack.getFrontAuditState()!=null){
            msgPackVo.setFrontAuditStateName(mmsMsgPack.getFrontAuditState().getAuditStateName());
        }
        if (mmsMsgPack.getBackAuditState()!=null){
            msgPackVo.setBackAuditStateName(mmsMsgPack.getBackAuditState().getAuditStateName());
        }
    }

    /**
     * typeEnum 等于2表示彩信
     * @param tickets
     * @param typeEnum
     * @return
     */
    public static List<Map<String,Object>> assembleCheckTicketVo(List<MsgTicket> tickets, int typeEnum) {
        Map<String,Object> map;
        List<Map<String,Object>> resultList = new ArrayList<>();
        switch (typeEnum) {
            case 2:
                for (MsgTicket ticket:tickets){
                    map = new HashMap();
                    map.put("packId",ticket.getPackId());
                    map.put("phone",ticket.getPhone());
                    map.put("title",ticket.getMmsTitle());
                    map.put("checkState", formatBizNameResult(ticket.getFrameBizForm()));
                    resultList.add(map);
                }
                break;
            case 1:
                for (MsgTicket ticket:tickets){
                    map = new HashMap();
                    map.put("packId",ticket.getPackId());
                    map.put("phone",ticket.getPhone());
                    map.put("smsContent",ticket.getSmsContentSkipSign());
                    map.put("checkState", formatBizNameResult(ticket.getFrameBizForm()));
                    resultList.add(map);
                }
                break;
                default:
                    break;
        }

        return resultList;
    }

    /**
     * 将部分frame表中的biz_form进行修正
     * @param frameBizForm
     * @return
     */
    private static String formatBizNameResult(int frameBizForm) {
        String resultStr = "";
        if (frameBizForm == BizFormEnum.SYS_BIND.getIndex() || BizFormEnum.VIP_PHONE.getIndex() == frameBizForm) {
            resultStr = BizFormEnum.NORMAL.getBizFormName();
        } else {
            resultStr = BizFormEnum.getBizForm(frameBizForm).getBizFormName();
        }
        return resultStr;
    }
    public static String genBatchName(SimpleUser user){
        return user.getUsername() + Delimiters.UNDERLINE + DateUtil.getDateForBatchName();
    }
}

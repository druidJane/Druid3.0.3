package com.xuanwu.mos.rest.resource;

import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MmsMsgPack;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.enums.BizFormEnum;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.domain.enums.PackStateEnum;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.service.msgservice.MsgFrameService;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.rest.service.msgservice.MsgTicketService;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.vo.MsgPackVo;
import com.xuanwu.mos.vo.MsgTicketVo;
import com.xuanwu.msggate.common.protobuf.CommonItem;
import com.xuanwu.msggate.common.sbi.entity.Ticket;
import com.xuanwu.msggate.common.zip.ZipUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭垚辉 on 2017/6/30.
 */
@Component
public class VoUtilResource {

    @Autowired
    private MsgPackService packService;

    @Autowired
    private MsgFrameService frameService;

    @Autowired
    private MsgTicketService ticketService;

    /**
     * 拼装发送记录中的vo
     * 后期维护人员请注意：
     * 在短彩信的发送记录中有这么一个确定的规则(来源于测试人员的指点):
     * 提交号码数-- 提交到玄武网关的号码数
     * 被过滤号码数 -- 在玄武网关中被过滤的号码数，包括审核不通过和取消发送的号码数
     * 已发送号码数 -- 提交到运营商网关的号码数
     * 结论是: 提交号码数 = 被过滤号码数 + 已发送号码数
     */
    public List<MsgPackVo> assembleSendRecordVo(List<MmsMsgPack> mmsMsgPacks) {
        MsgPackVo vo = null;
        List<MsgPackVo> msgPackVos = new ArrayList<>();
        for (MmsMsgPack msgPack : mmsMsgPacks) {
            vo = new MsgPackVo();
            vo.setPackId(msgPack.getPackId());
            vo.setBatchName(msgPack.getBatchName());
            vo.setCommitFrom(msgPack.getCommitFrom());
            vo.setBizTypeName(msgPack.getBizTypeName());
            vo.setPackRemark(msgPack.getPackRemark());
            vo.setPostTime(msgPack.getPostTime());
            vo.setScheduleTime(msgPack.getScheduleTime());
            vo.setDoneTime(msgPack.getDoneTime());
            vo.setSendUserName(msgPack.getSendUserName());
            vo.setSendDeptName(msgPack.getSendDeptName());
            vo.setPackState(msgPack.getPackState().getPackStateName());
            vo.setTotalTickets(msgPack.getInvalidTickets() + msgPack.getValidTickets());
            vo.setInvalidTickets(msgPack.getInvalidTickets());
            vo.setValidTickets(msgPack.getValidTickets());

            QueryParameters query = new QueryParameters();
            query.addParam("packId", vo.getPackId());
            Parameters params = new Parameters(query, msgPack.getMsgType().getIndex(), vo.getPostTime());

            // 获取发送成功号码数
            MmsMsgPack successCount = packService.findSuccessCount(params);
            if (successCount != null) {
                vo.setSendedTickets(successCount.getSendedTickets());
            } else {
                vo.setSendedTickets(0);
            }

            // 获取失败号码数
            MmsMsgPack failCount = packService.findFailCount(params);
            if (failCount != null) {
                vo.setFailedTickets(failCount.getFailedTickets());
            } else {
                vo.setFailedTickets(0);
            }

            // 获取每个pack中的invalidTickets字段
            if (msgPack.getPackState() == PackStateEnum.ABANDON) {
                // 如果某个pack被设置为ABANDON，那么就说明整个pack都被干掉了(没有一条是发送出去的)
                vo.setPackState(PackStateEnum.OVER.getPackStateName());
                vo.setValidTickets(0);
            }

            // 获取每个pack中的filterTickets字段
            params.getQuery().addParam("filter", 0);
            int filterCount = frameService.findMsgCountByPackId(params);
            vo.setFilterTickets(filterCount);
            vo.setUuid(msgPack.getUuid());
            msgPackVos.add(vo);
        }
        return msgPackVos;
    }

    /**
     * 组装审核短彩信中的pack包
     */
    public List<MsgPackVo> assembleAuditPackVo(List<MmsMsgPack> msgPacks) {
        List<MsgPackVo> voList = new ArrayList<>();
        MsgPackVo vo = null;
        for (MmsMsgPack pack : msgPacks) {
            vo = new MsgPackVo();
            vo.setMsgType(pack.getMsgType().getIndex());
            vo.setPackId(pack.getPackId());
            vo.setPostTime(pack.getPostTime());
            vo.setBatchName(pack.getBatchName());
            vo.setPackRemark(pack.getPackRemark());
            vo.setCommitFrom(pack.getCommitFrom());
            vo.setSendUserName(pack.getSendUserName());
            vo.setSendDeptName(pack.getSendDeptName());
            vo.setScheduleTime(pack.getScheduleTime());
            vo.setPackState(pack.getPackState().getPackStateName());
            vo.setTotalTickets(pack.getInvalidTickets() + pack.getValidTickets());
            vo.setBizTypeName(pack.getBizTypeName());
            vo.setUserId(pack.getUserId());
            if (pack.getMsgType() == MsgTypeEnum.SMS) {
                // 如果是短信的话，则设置其短信内容
                vo.setSmsContent(pack.getSmsContent());
            }
            // 查找过滤的短彩信条数
            QueryParameters query = new QueryParameters();
            query.addParam("packId", vo.getPackId());
            query.addParam("filter", 0);
            Parameters params = new Parameters(query, vo.getMsgType(), vo.getPostTime());
            int msgCount = frameService.findMsgCountByPackId(params);
            vo.setFilterTickets(msgCount);
            vo.setValidTickets(pack.getValidTickets());
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 组合短彩信中号码记录中的vo
     */
    public List<MsgTicketVo> assembleNumberRecordVo(List<MsgTicket> msgTickets, Boolean isSms) {
        List<MsgTicketVo> voList = new ArrayList<>();
        MsgTicketVo vo;
        for (MsgTicket msgTicket : msgTickets) {
            vo = new MsgTicketVo();
            vo.setId(msgTicket.getPackId());
            vo.setPackId(msgTicket.getPackId());
            vo.setPhone(msgTicket.getPhone());
            vo.setSendTime(msgTicket.getPostTime());
            vo.setTicketId(msgTicket.getTicketId());
            vo.setMmsTitle(msgTicket.getMmsTitle());
            vo.setBatchName(msgTicket.getBatchName());
            vo.setSendUserName(msgTicket.getUserName());
            vo.setSpecNumber(msgTicket.getSpecNumber());
            vo.setCommitTime(msgTicket.getSubmitRespTime());
            vo.setTicketOriginResult(msgTicket.getOriginResult());
            vo.setStateReportOriginResult(msgTicket.getStateReportOriginResult());

            //region 检核结果
            if (msgTicket.getBizForm() >= WebConstants.ALL_FRAME_BIZFORM_LENGTH) {
                vo.setBizFormName("发送失败");
            } else if (msgTicket.getBizForm() == 6 || msgTicket.getBizForm() == 19) {
                //按照需求，强行hardcode "系统分配端口" "VIP号码" 为 检核通过
                vo.setBizFormName(BizFormEnum.NORMAL.getBizFormName());
            } else {
                vo.setBizFormName(BizFormEnum.getBizForm(msgTicket.getBizForm()).getBizFormName());
            }
            //endregion
            // 没状态报告
            if (msgTicket.getStateReportOriginResult() == null) {
                //有提交报告，无状态报告，设置发送成功
                if("0".equals(msgTicket.getOriginResult())){
                    vo.setSendResult("0");
                }else{
                    vo.setSendResult("1");
                }
                vo.setStateReportOriginResult("未返回");
            } else {
                String sendResult = String.valueOf(msgTicket.getStateReportResult() + msgTicket.getIsNormal());
                vo.setSendResult(sendResult);
                vo.setStateReportOriginResult(msgTicket.getStateReportOriginResult());
            }
            //长短信只要有一段失败ticket，则将整条改为失败
            if (Ticket.SMSType.LPSMS.getIndex() == msgTicket.getSmsType()) {
                QueryParameters query = new QueryParameters();
                query.addParam("phone", msgTicket.getPhone());
                query.addParam("parentId", msgTicket.getTicketId());
                query.addParam("packId", msgTicket.getPackId());
                Parameters parameters = new Parameters(query, MsgContent.MsgType.LONGSMS.getIndex(), msgTicket.getPostTime());
                List<MsgTicket> failTicket = ticketService.findFailedTicketByPackId(parameters);
                if (failTicket != null && !failTicket.isEmpty()) {
                    MsgTicket fail = failTicket.get(0);
                    vo.setSendResult("1");
                    vo.setStateReportOriginResult(fail.getOriginResult());
                }
            }
            if (isSms) {
                //获取短信内容
                String content = "";
                if (msgTicket.getSendType() == 0) {
                    //群发，拿frame短信内容
                    try {
                        byte[] bytes = ZipUtil.unzipByteArray(msgTicket.getFrameContent());
                        CommonItem.MassPack massPack = CommonItem.MassPack.parseFrom(bytes);
                        content = massPack.getContent().getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //组发，拿ticket长短信内容
                    content = msgTicket.getSmsContentSkipSign();
                }
                vo.setSmsContent(msgTicket.getPreContent() + content + msgTicket.getSufContent());
            }
            voList.add(vo);
        }
        return voList;
    }
}

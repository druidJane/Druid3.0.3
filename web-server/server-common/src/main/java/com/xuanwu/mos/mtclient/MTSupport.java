package com.xuanwu.mos.mtclient;

import com.google.protobuf.ByteString;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.MTResp;
import com.xuanwu.mos.domain.entity.MTResult;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.msggate.common.protobuf.CommonItem;
import com.xuanwu.msggate.common.protobuf.mt.MTRequest;
import com.xuanwu.msggate.common.protobuf.mt.MTResponse;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.MassMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgPack;
import com.xuanwu.msggate.common.util.Constants;
import com.xuanwu.msggate.common.util.UUIDUtil;
import com.xuanwu.msggate.common.util.XmlUtil;
import com.xuanwu.msggate.common.zip.ZipUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MTSupport {

    public static byte[] tran2LoginBytes(Config config, String userName, String password,String remoteIp) {
        MTRequest.TRequest.Builder builder = MTRequest.TRequest.newBuilder();
        builder.setUuid(UUIDUtil.tranUUID2Builder(UUID.randomUUID()));
        builder.setType(MTRequest.TRequestType.LOGIN);
        CommonItem.Account.Builder acc = CommonItem.Account.newBuilder();
        acc.setName(userName);
        acc.setPassword(password);
        builder.setUser(acc);
        builder.setAttributes(
                "<map>" +
                "<entry><string>ACCESS_METHOD</string><string>" + config.getPlatformName() + "</string></entry>" +
                "<entry><string>ACCESS_VERSION</string><string>" + config.getVersion() + "</string></entry>" +
                "<entry><string>remoteIp</string><string>" + remoteIp + "</string></entry>"+
                "</map>"
        );
        return builder.build().toByteArray();
    }

    public static byte[] tran2LoginBytes(Map<String,String> accountMap, Config config) {
        MTRequest.TRequest.Builder builder = MTRequest.TRequest.newBuilder();
        builder.setUuid(UUIDUtil.tranUUID2Builder(UUID.randomUUID()));
        builder.setType(MTRequest.TRequestType.LOGIN);
        CommonItem.Account.Builder acc = CommonItem.Account.newBuilder();
        acc.setName(accountMap.get("username"));
        acc.setPassword(accountMap.get("password"));
        builder.setUser(acc);
        builder.setAttributes(
                "<map>" +
                        "<entry><string>ACCESS_METHOD</string><string>" + config.getPlatformName() + "</string></entry>" +
                        "<entry><string>ACCESS_VERSION</string><string>" + config.getVersion() + "</string></entry>" +
                        "<entry><string>remoteIp</string><string>" + accountMap.get("remoteIp") + "</string></entry>"+
                        "</map>"
        );
        return builder.build().toByteArray();
    }

    public static byte[] tran2AuditingBytes(String packId, String packUuid, int state,int msgType,Date postTime) {
        int packNum = Integer.valueOf(packId);
        String postTimeStr = DateUtil.format(postTime, DateUtil.DateTimeType.Date);
        MTRequest.TRequest.Builder builder = MTRequest.TRequest.newBuilder();
        builder.setUuid(UUIDUtil.tranUUID2Builder(UUID.fromString(packUuid)));
        builder.setType(MTRequest.TRequestType.AUDITING);
        builder.setAuditingState(state);
        builder.setAttributes(
                "<map>" +
                "<entry><string>packLid</string><string>"+packNum+"</string></entry>" +
                "<entry><string>msgType</string><string>" + msgType + "</string></entry>" +
                "<entry><string>postTime</string><string>" + postTimeStr + "</string></entry>" +
                "</map>");
        return builder.build().toByteArray();
    }

    public static byte[] tran2SendBytes(Config config, MsgPack msgPack) throws Exception {
        PMsgPack pack = (PMsgPack) msgPack;
        MTRequest.TRequest.Builder builder = MTRequest.TRequest.newBuilder();
        if (pack.getUuid() == null) {
            pack.setUuid(UUID.randomUUID());
        }
        builder.setUuid(UUIDUtil.tranUUID2Builder(pack.getUuid()));

        if (StringUtils.isBlank(pack.getBatchName())) {
            pack.setBatchName("");
        }
        if (pack.getCustomNum() != null)
            builder.setCustomNum(pack.getCustomNum());
        CommonItem.BizMeta.Builder biz = CommonItem.BizMeta.newBuilder();
        biz.setLevel(0);
        biz.setPriority(0);
        biz.setMsgType(pack.getMsgType().getIndex());
        biz.setSendType(pack.getSendType().ordinal());
        if (pack.getScheduleTime() != null)
            biz.setScheduleTime(pack.getScheduleTime().getTime());
        if (pack.getDeadline() != null)
            biz.setDeadline(pack.getDeadline().getTime());
        if (pack.getRemark() != null) {
            builder.setRemark(pack.getRemark());
        }

        if(pack.getParameter(Constants.TEMPLATE_NO) != null){
            Map<String, Object> attrs = new HashMap<String, Object>();
            attrs.put(Constants.TEMPLATE_NO, pack.getParameter(Constants.TEMPLATE_NO));
            builder.setAttributes(XmlUtil.toXML(attrs));
        }
        biz.setBizType(pack.getBizType());
        builder.setInfo(biz);
        builder.setFrom(config.getPlatformName() + "_" + config.getVersion());
        builder.setType(MTRequest.TRequestType.MESSAGE_SENT);
        if (pack.getBatchName() != null) {
            builder.setBatchName(pack.getBatchName());
        }
        builder.setDistinctFlag(pack.isDistinct());
        if(pack.getMmsAttachments() != null){
            for(com.xuanwu.msggate.common.sbi.entity.MediaItem mItem : pack.getMmsAttachments()){
                CommonItem.MediaItem.Builder temp = CommonItem.MediaItem.newBuilder();
                temp.setMediaType(mItem.getType().ordinal());
                temp.setMeta(mItem.getMeta());
                temp.setData(ByteString.copyFrom(mItem.getData()));
                builder.addMmsAttachment(temp);
            }
        }
        MsgFrame frame = pack.getFrames().get(0);
        switch (pack.getSendType()) {
            case MASS:
                buildMassPack((MassMsgFrame) frame, builder);
                break;
            case GROUP:
                buildGroupPack(frame, builder);
        }
        return builder.build().toByteArray();
    }

    private static void buildMassPack(MassMsgFrame frame,
                                      MTRequest.TRequest.Builder builder) throws Exception {
        CommonItem.MassPack.Builder massmsg = CommonItem.MassPack.newBuilder();
        CommonItem.MsgContent.Builder contentMass = CommonItem.MsgContent.newBuilder();
        contentMass.setContent(frame.getContent().getContent());
        massmsg.setContent(contentMass);

        for (MsgSingle msg : frame.getAllMsgSingle()) {
            CommonItem.MsgItem.Builder item = CommonItem.MsgItem.newBuilder();
            item.setPhone(msg.getPhone());
            if (msg.getCustomMsgID() != null)
                item.setCustomMsgID(msg.getCustomMsgID());
            if (msg.getCustomNum() != null)
                item.setCustomNum(msg.getCustomNum());
            item.setVipFlag(msg.isVip());
            massmsg.addItems(item);
        }
        builder.setMassMsg(ByteString.copyFrom(ZipUtil.zipByteArray(massmsg
                .build().toByteArray())));
    }

    private static void buildGroupPack(MsgFrame frame, MTRequest.TRequest.Builder builder)
            throws Exception {
        CommonItem.GroupPack.Builder groupmsg = CommonItem.GroupPack.newBuilder();
        for (MsgSingle msg : frame.getAllMsgSingle()) {
            CommonItem.MsgItem.Builder item = CommonItem.MsgItem.newBuilder();
            CommonItem.MsgContent.Builder content = CommonItem.MsgContent.newBuilder();
            item.setPhone(msg.getPhone());
            content.setContent(msg.getContent().getContent());
            if(msg.getContent().getMediaItems() != null){
                for(com.xuanwu.msggate.common.sbi.entity.MediaItem mItem : msg.getContent().getMediaItems()){
                    CommonItem.MediaItem.Builder temp = CommonItem.MediaItem.newBuilder();
                    temp.setMediaType(mItem.getType().ordinal());
                    temp.setMeta(mItem.getMeta());
                    temp.setData(ByteString.copyFrom(mItem.getData()));
                    content.addMedias(temp);
                }
            }
            if (msg.getCustomMsgID() != null)
                item.setCustomMsgID(msg.getCustomMsgID());
            if (msg.getCustomNum() != null)
                item.setCustomNum(msg.getCustomNum());
            item.setVipFlag(msg.isVip());
            item.setContent(content);
            groupmsg.addItems(item);
        }
        builder.setGroupMsg(ByteString.copyFrom(ZipUtil.zipByteArray(groupmsg
                .build().toByteArray())));
    }

    public static MTResp buildResp(byte[] recvBytes) throws Exception {
        MTResponse.TResponse.Builder builder = MTResponse.TResponse.newBuilder();
        builder.mergeFrom(recvBytes);
        MTResp resp = new MTResp();
        resp.setMessage(builder.getMessage());
        resp.setResult(MTResult.getResult(builder.getResult().getNumber()));
        resp.setUuid(UUIDUtil.tranBuilder2UUID(builder.getUuid()));
        resp.setAttributes(builder.getAttributes());
        return resp;
    }
}

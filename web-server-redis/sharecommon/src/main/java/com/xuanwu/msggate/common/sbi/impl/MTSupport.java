/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.impl;

import com.google.protobuf.ByteString;

import com.xuanwu.msggate.common.protobuf.CommonItem.Account;
import com.xuanwu.msggate.common.protobuf.CommonItem.BizMeta;
import com.xuanwu.msggate.common.protobuf.CommonItem.GroupPack;
import com.xuanwu.msggate.common.protobuf.CommonItem.MassPack;
import com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem;
import com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent;
import com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem;
import com.xuanwu.msggate.common.protobuf.mt.MTRequest.TRequest;
import com.xuanwu.msggate.common.protobuf.mt.MTRequest.TRequestType;
import com.xuanwu.msggate.common.protobuf.mt.MTResponse.TResponse;
import com.xuanwu.msggate.common.sbi.entity.MTResp;
import com.xuanwu.msggate.common.sbi.entity.MTResult;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.MassMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgPack;
import com.xuanwu.msggate.common.util.Constants;
import com.xuanwu.msggate.common.util.UUIDUtil;
import com.xuanwu.msggate.common.util.XmlUtil;
import com.xuanwu.msggate.common.zip.ZipUtil;

import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-10-11
 * @Version 1.0.0
 */
public class MTSupport {

	public static byte[] tran2LoginBytes(String userName, String password, String platform, String version) {
		TRequest.Builder builder = TRequest.newBuilder();
		builder.setUuid(UUIDUtil.tranUUID2Builder(UUID.randomUUID()));
		builder.setType(TRequestType.LOGIN);
		Account.Builder acc = Account.newBuilder();
		acc.setName(userName);
		acc.setPassword(password);
		builder.setUser(acc);
		builder.setAttributes("<map><entry><string>ACCESS_METHOD</string><string>" + platform + "</string></entry>" +
				"<entry><string>ACCESS_VERSION</string><string>" + version + "</string></entry></map>");
		return builder.build().toByteArray();
	}

	public static byte[] tran2AuditingBytes(String packId, int state,int msgType,Date postTime,int platformId) {
		TRequest.Builder builder = TRequest.newBuilder();
		builder.setUuid(UUIDUtil.tranUUID2Builder(UUID.fromString(packId)));
		builder.setType(TRequestType.AUDITING);
		builder.setAuditingState(state);
		builder.setAttributes("<map><entry><string>platformID</string><string>" + platformId 
				+ "</string></entry><entry><string>msgType</string><string>" + msgType + "</string></entry>" 
				+ "<entry><string>postTime</string><string>" + postTime.getTime() + "</string></entry></map>");
		return builder.build().toByteArray();
	}
	
	public static byte[] tran2SendBytes(String phone, String msg, String params, String platform, String version) throws Exception {
		return tran2SendBytes(UUID.randomUUID(), phone, msg, null, null, params, platform, version);
	}
	
	public static byte[] tran2SendBytes(UUID uuid, String phone, String msg, String customNum, String customMsgId, String params, 
			String platform, String version) throws Exception {
		TRequest.Builder builder = TRequest.newBuilder();
		builder.setUuid(UUIDUtil.tranUUID2Builder(uuid));
		if(StringUtils.isNotBlank(customNum)){
			builder.setCustomNum(customNum);
		}
		BizMeta.Builder biz = BizMeta.newBuilder();
		biz.setLevel(0);
		biz.setPriority(0);
		biz.setMsgType(1);
		biz.setSendType(0);
		biz.setBizType(0);
		builder.setInfo(biz);
		builder.setFrom(platform + "_" + version);
		builder.setType(TRequestType.MESSAGE_SENT);
		builder.setBatchName("");
		builder.setDistinctFlag(false);
		if(params != null){
			builder.setAttributes(params);
		}
		//build message single
		MassPack.Builder massmsg = MassPack.newBuilder();
		MsgContent.Builder contentMass = MsgContent.newBuilder();
		contentMass.setContent(msg);
		massmsg.setContent(contentMass);
		MsgItem.Builder item = MsgItem.newBuilder();
		item.setPhone(phone);
		if(StringUtils.isNotBlank(customMsgId)){
			item.setCustomMsgID(customMsgId);
		}
		massmsg.addItems(item);
		builder.setMassMsg(ByteString.copyFrom(ZipUtil.zipByteArray(massmsg
				.build().toByteArray())));
		return builder.build().toByteArray();
	}

	public static byte[] tran2SendBytes(MsgPack msgPack, String platform, String version) throws Exception {
		PMsgPack pack = (PMsgPack) msgPack;
		TRequest.Builder builder = TRequest.newBuilder();
		if (pack.getUuid() == null) {
			pack.setUuid(UUID.randomUUID());
		}
		builder.setUuid(UUIDUtil.tranUUID2Builder(pack.getUuid()));

		if (StringUtils.isBlank(pack.getBatchName())) {
			pack.setBatchName("");
		}
		if (pack.getCustomNum() != null)
			builder.setCustomNum(pack.getCustomNum());
		BizMeta.Builder biz = BizMeta.newBuilder();
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
		biz.setBizType(pack.getBizType());
		builder.setInfo(biz);
		builder.setFrom(platform + "_" + version);
		builder.setType(TRequestType.MESSAGE_SENT);
		if (pack.getBatchName() != null) {
			builder.setBatchName(pack.getBatchName());
		}
		builder.setDistinctFlag(pack.isDistinct());
		if(pack.getParameter(Constants.TEMPLATE_NO) != null){
			Map<String, Object> attrs = new HashMap<String, Object>();
			attrs.put(Constants.TEMPLATE_NO, pack.getParameter(Constants.TEMPLATE_NO));
			builder.setAttributes(XmlUtil.toXML(attrs));
		}
		if(pack.getMmsAttachments() != null){
			for(com.xuanwu.msggate.common.sbi.entity.MediaItem mItem : pack.getMmsAttachments()){
				MediaItem.Builder temp = MediaItem.newBuilder();
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
			TRequest.Builder builder) throws Exception {
		MassPack.Builder massmsg = MassPack.newBuilder();
		MsgContent.Builder contentMass = MsgContent.newBuilder();
		contentMass.setContent(frame.getContent().getContent());
		massmsg.setContent(contentMass);

		for (MsgSingle msg : frame.getAllMsgSingle()) {
			MsgItem.Builder item = MsgItem.newBuilder();
			item.setPhone(msg.getPhone());
			if (msg.getCustomMsgID() != null)
				item.setCustomMsgID(msg.getCustomMsgID());
			if (msg.getCustomNum() != null)
				item.setCustomNum(msg.getCustomNum());
			item.setVipFlag(msg.isVip());
			item.setOrgCode(msg.getOrgCode());
			item.setMsgFmt(msg.getMsgFmt());
			massmsg.addItems(item);
		}
		builder.setMassMsg(ByteString.copyFrom(ZipUtil.zipByteArray(massmsg
				.build().toByteArray())));
	}

	private static void buildGroupPack(MsgFrame frame, TRequest.Builder builder)
			throws Exception {
		GroupPack.Builder groupmsg = GroupPack.newBuilder();
		for (MsgSingle msg : frame.getAllMsgSingle()) {
			MsgItem.Builder item = MsgItem.newBuilder();
			MsgContent.Builder content = MsgContent.newBuilder();
			item.setPhone(msg.getPhone());
			content.setContent(msg.getContent().getContent());
			if(msg.getContent().getMediaItems() != null){
				for(com.xuanwu.msggate.common.sbi.entity.MediaItem mItem : msg.getContent().getMediaItems()){
					MediaItem.Builder temp = MediaItem.newBuilder();
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
			item.setOrgCode(msg.getOrgCode());
			item.setMsgFmt(msg.getMsgFmt());
			item.setContent(content);
			groupmsg.addItems(item);
		}
		builder.setGroupMsg(ByteString.copyFrom(ZipUtil.zipByteArray(groupmsg
				.build().toByteArray())));
	}

	public static MTResp buildResp(byte[] recvBytes) throws Exception {
		TResponse.Builder builder = TResponse.newBuilder();
		builder.mergeFrom(recvBytes);
		MTResp resp = new MTResp();
		resp.setMessage(builder.getMessage());
		resp.setResult(MTResult.getResult(builder.getResult().getNumber()));
		resp.setUuid(UUIDUtil.tranBuilder2UUID(builder.getUuid()));
		resp.setAttributes(builder.getAttributes());
		return resp;
	}
}

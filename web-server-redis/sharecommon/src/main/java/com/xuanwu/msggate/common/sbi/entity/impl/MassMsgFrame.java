/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import com.google.protobuf.ByteString;

import com.xuanwu.msggate.common.protobuf.CommonItem.BizMeta;
import com.xuanwu.msggate.common.protobuf.CommonItem.MassFrame;
import com.xuanwu.msggate.common.protobuf.CommonItem.MassPack;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack.SendType;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.exception.CoreError;
import com.xuanwu.msggate.common.util.ChangeMsgFmt;
import com.xuanwu.msggate.common.zip.ZipUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mass message frame
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-22
 * @Version 1.0.0
 */
public class MassMsgFrame extends PMsgFrame {

	/**
	 * Message content
	 */
	private MsgContent content;

	private List<MsgSingle> items = new ArrayList<MsgSingle>();

	/**
	 * Clone the type without content
	 * 
	 * @param proto
	 * @param pack
	 */
	public MassMsgFrame() {
	}

	public MassMsgFrame(Long pid, BizMeta meta, ByteString pack) {
		super(pid, MsgType.getType(meta.getMsgType()), meta);
		transPack2Item(tranByte2Pack(pack));
	}

	private MassPack tranByte2Pack(ByteString pack) {
		try {
			return MassPack.newBuilder().mergeFrom(
					ZipUtil.unzipByteArray(pack.toByteArray())).build();
		} catch (Exception e) {
			throw new RuntimeException(
					"Bad meta data when transfer byte to pack");
		}
	}

	public MassMsgFrame(BizMeta meta, ByteString pack) {
		super(null, MsgType.getType(meta.getMsgType()), meta);
		transPack2Item(tranByte2Pack(pack));
	}

	public MassMsgFrame(FetchFrame frame) {
		super(frame);
		transPack2Item(tranByte2Pack(frame.getContent()));
	}

	private MassPack tranByte2Pack(byte[] content) {
		try {
			return MassPack.newBuilder().mergeFrom(
					ZipUtil.unzipByteArray(content)).build();
		} catch (Exception e) {
			throw new RuntimeException(
					"Bad meta data when transfer byte to pack");
		}
	}

	private void transPack2Item(MassPack pack) {
		this.content = new PMsgContent(this.getMsgType(), pack.getContent());
		switch (msgType) {
		case MMS:
			int i = 0;
			for (com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem msgItem : pack
					.getItemsList()) {
				MsgContent msgContent = (i == 0) ? content : null;
				items.add(new PMsgSingle(msgType, msgItem.getPhone(),
						msgContent, msgItem.getCustomMsgID(), msgItem
								.getCustomNum(), msgItem.getVipFlag(), msgItem
								.getCarrier(),msgItem.getOrgCode(),ChangeMsgFmt.changeMsgFmt(msgItem.getMsgFmt())));
				i++;
			}
			break;
		default:
			for (com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem msgItem : pack
					.getItemsList()) {
				items.add(new PMsgSingle(msgType, msgItem.getPhone(), content,
						msgItem.getCustomMsgID(), msgItem.getCustomNum(),
						msgItem.getVipFlag(), msgItem.getCarrier(),msgItem.getOrgCode()
						,ChangeMsgFmt.changeMsgFmt(msgItem.getMsgFmt())));
			}
		}
	}

	@Override
	public byte[] getPack() {
		if(content == null)
			return null;
		try {
			return ZipUtil.zipByteArray(getPackBuilder().toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Zip error when build pack");
		}
	}

	public MassPack getPackBuilder() {
		MassPack.Builder builder = MassPack.newBuilder();
		builder.setContent(content.build());
		for (MsgSingle single : items) {
			builder.addItems(single.build());

		}
		return builder.build();
	}

	@Override
	public List<String> getAllPhones() {
		List<String> phones = new ArrayList<String>();
		for (MsgSingle item : items) {
			phones.add(item.getPhone());
		}
		return phones;
	}

	@Override
	public int getMsgCount() {
		if(items.isEmpty())
			return tickets.size();
		return items.size();
	}

	@Override
	public MsgSingle getMsgSingle(int index) {
		return items.get(index);
	}

	public void setAllPhones(List<String> phones) {
		// this.phones = phones;
	}

	public void setContent(MsgContent content) {
		this.content = content;
	}

	public MsgContent buildContent(String content) {
		com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent.Builder builder = com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent
				.newBuilder();
		builder.setContent(content);
		MsgContent msgContent = new PMsgContent(getMsgType(), builder.build());
		return msgContent;
	}

	@Override
	public MsgFrame duplitBasicFrame() {
		MassMsgFrame newFrame = new MassMsgFrame();
		copyBasicInfo(newFrame);
		newFrame.content = this.content;
		return newFrame;
	}

	@Override
	public List<MsgSingle> getAllMsgSingle() {
		return items;
	}

	@Override
	public void setAllMsgSingle(List<MsgSingle> msgSingles) {
		if (msgSingles != null && msgSingles.size() > 0) {
			if (msgSingles.get(0).getContent() != null)
				this.content = msgSingles.get(0).getContent();
		}
		this.items = msgSingles;
	}

	public MsgContent getContent() {
		return content;
	}

	public String getMsgConent() {
		return content.getContent();
	}

	public MassFrame build() {
		MassFrame.Builder build = MassFrame.newBuilder();
		build.setPid(id);
		build.setUserID(userID);
		build.setEnterpriseID(enterpriseID);
		build.setMeta(buildMeta(SendType.MASS));
		try {
			build.setPack(ByteString.copyFrom(ZipUtil
					.zipByteArray(getPackBuilder().toByteArray())));
		} catch (IOException e) {
			throw new RuntimeException("Error occurred when zip pack content");
		}
		return build.build();
	}

	@Override
	public void setPack(byte[] pack) {
		try {
			transPack2Item(MassPack.parseFrom(ZipUtil.unzipByteArray(pack)));
		} catch (Exception e) {
			throw new CoreError("Binary translate to MassPack error!", e);
		}
	}

	@Override
	public SendType getSendType() {
		return SendType.MASS;
	}

	@Override
	public int getMsgFrameType() {
		return MsgFrame.MASS_FRAME;
	}

	@Override
	public String toString() {
		return "MassMsgFrame [packUUID=" + packUUID + ", id=" + id
				+ ", msgType=" + msgType + ", customNum=" + customNum
				+ ", postTime=" + postTime + ", commitTime=" + commitTime
				+ ", scheduleTime=" + scheduleTime + ", retrieveTime="
				+ retrieveTime + ", deadline=" + deadline + ", priority="
				+ priority + ", bizForm=" + bizForm + ", state=" + state
				+ ", boeTime=" + boeTime + ", eoeTime=" + eoeTime
				+ ", isStateReport=" + isStateReport + ", parameters="
				+ parameters + ", version=" + version + ", tickets=" + tickets
				+ "]";
	}
}

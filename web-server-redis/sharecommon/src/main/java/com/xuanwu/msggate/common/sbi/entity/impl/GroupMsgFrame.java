/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import com.google.protobuf.ByteString;

import com.xuanwu.msggate.common.protobuf.CommonItem.BizMeta;
import com.xuanwu.msggate.common.protobuf.CommonItem.GroupFrame;
import com.xuanwu.msggate.common.protobuf.CommonItem.GroupPack;
import com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem;
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
 * Group message frame
 * 
 * @author <a href="mailto:wanglianguang@139130.net>LianGuang Wang</a>
 * @Data 2010-5-22
 * @Version 1.0.0
 */
public class GroupMsgFrame extends PMsgFrame {
	/**
	 * All the items of the message
	 */
	private List<MsgSingle> items = new ArrayList<MsgSingle>();

	public GroupMsgFrame() {
	}

	public GroupMsgFrame(Long pid, BizMeta meta, ByteString pack) {
		super(pid, MsgType.getType(meta.getMsgType()), meta);
		transPack2Item(tranByte2Pack(pack));
	}

	private GroupPack tranByte2Pack(ByteString pack) {
		try {
			return GroupPack.newBuilder().mergeFrom(
					ZipUtil.unzipByteArray(pack.toByteArray())).build();
		} catch (Exception e) {
			throw new RuntimeException(
					"Bad meta data when transfer byte to pack", e);
		}
	}

	public GroupMsgFrame(BizMeta meta, ByteString pack) {
		super(null, MsgType.getType(meta.getMsgType()), meta);
		transPack2Item(tranByte2Pack(pack));
	}

	public GroupMsgFrame(FetchFrame frame) {
		super(frame);
		transPack2Item(tranByte2Pack(frame.getContent()));
	}

	private GroupPack tranByte2Pack(byte[] content) {
		try {
			return GroupPack.newBuilder().mergeFrom(
					ZipUtil.unzipByteArray(content)).build();
		} catch (Exception e) {
			throw new RuntimeException(
					"Bad meta data when transfer byte to pack", e);
		}
	}

	private void transPack2Item(GroupPack pack) {
		for (com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem msgItem : pack
				.getItemsList()) {
			items.add(new PMsgSingle(msgType, msgItem.getPhone(),
					new PMsgContent(msgType, msgItem.getContent()), msgItem
							.getCustomMsgID(), msgItem.getCustomNum(), msgItem
							.getVipFlag(),msgItem.getCarrier(),msgItem.getOrgCode(),ChangeMsgFmt.changeMsgFmt(msgItem.getMsgFmt())));
		}
	}
	
	private List<com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem> builderItems() {
		List<com.xuanwu.msggate.common.protobuf.CommonItem.MsgItem> buildItems = new ArrayList<MsgItem>();
		for (MsgSingle single : items) {
			buildItems.add(single.build());
		}
		return buildItems;
	}

	@Override
	public byte[] getPack() {
		if(items == null)
			return null;
		try {
			return ZipUtil.zipByteArray(getPackBuilder().toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Zip error when build pack");
		}
	}

	public com.xuanwu.msggate.common.protobuf.CommonItem.GroupPack getPackBuilder() {
		GroupPack.Builder builder = GroupPack.newBuilder();
		builder.addAllItems(builderItems());
		return builder.build();
	}

	/**
	 * Build the frame builder
	 * 
	 * @return
	 */
	public GroupFrame build() {
		GroupFrame.Builder build = GroupFrame.newBuilder();
		build.setPid(id);
		build.setUserID(userID);
		build.setEnterpriseID(enterpriseID);
		build.setMeta(buildMeta(SendType.GROUP));
		try {
			build.setPack(ByteString.copyFrom(ZipUtil
					.zipByteArray(getPackBuilder().toByteArray())));
		} catch (IOException e) {
			throw new RuntimeException("Error occurred when zip pack content");
		}
		return build.build();
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
	public MsgFrame duplitBasicFrame() {
		PMsgFrame newFrame = new GroupMsgFrame();
		copyBasicInfo(newFrame);
		return newFrame;
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

	@Override
	public List<MsgSingle> getAllMsgSingle() {
		return items;
	}

	@Override
	public void setAllMsgSingle(List<MsgSingle> singles) {
		this.items = singles;
	}

	@Override
	public void setPack(byte[] pack) {
		try {
			transPack2Item(GroupPack.parseFrom(ZipUtil.unzipByteArray(pack)));
		} catch (Exception e) {
			throw new CoreError("Binary translate to GroupPack error!", e);
		}
	}

	@Override
	public SendType getSendType() {
		return SendType.GROUP;
	}

	@Override
	public int getMsgFrameType() {
		return MsgFrame.GROUP_FRAME;
	}

	@Override
	public String toString() {
		return "GroupMsgFrame [packUUID=" + packUUID + ", id=" + id
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

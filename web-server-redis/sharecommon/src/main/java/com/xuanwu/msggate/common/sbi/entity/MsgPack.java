/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.xuanwu.msggate.common.protobuf.CommonItem.Result;

/**
 * 请求短信包，代表客户端的一次短信请求
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-4-20
 * @Version 1.0.0
 */
public interface MsgPack {

	public enum PackType {
		MESSAGE(0), CHANNEL_CHANGE(1), BUSINESS_CHANGE(2), CANCEL(3);
		private final int index;

		private PackType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static PackType getPackType(int index) {
			PackType[] packTypes = PackType.values();
			for (PackType packType : packTypes) {
				if (packType.getIndex() == index)
					return packType;
			}
			return MESSAGE;
		}
	}

	public enum PackState {
		INIT(0), AUDITING(1), ABANDON(2), CANCEL(3), SUSPEND(4), SECOND_AUDITING(5), HANDLE(8), OVER(9);
		private final int index;

		private PackState(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static PackState getState(int index) {
			switch (index) {
			case 0:
				return INIT;
			case 1:
				return AUDITING;
			case 2:
				return ABANDON;
			case 3:
				return CANCEL;
			case 4:
				return SUSPEND;
			case 5:
				return SECOND_AUDITING;
			default:
				return INIT;
			}
		}
	}

	/**
	 * Pack type
	 */
	public enum SendType {
		MASS(0), GROUP(1);
		private final int index;

		private SendType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static SendType getType(int index) {
			switch (index) {
			case 0:
				return MASS;
			case 1:
				return GROUP;
			default:
				throw new IllegalArgumentException(
						new CommonRespResult(Result.INVALID_PARAM,
								"（sendType: 参数值无效，有效值为0、1）").toString());
			}
		}
	}

	public enum MmsNeedApprovalType {
		NEED(0), NOTNEED(1);
		private final int index;

		private MmsNeedApprovalType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static MmsNeedApprovalType getType(int index) {
			switch (index) {
			case 0:
				return NEED;
			case 1:
				return NOTNEED;
			default:
				return NEED;
			}
		}
	}

	public PackType getPackType();

	public void setPackType(PackType packType);

	/**
	 * 获得当前短信帧的所有短信帧
	 * 
	 * @return 所有的短信帧
	 */
	public List<MsgFrame> getFrames();

	/**
	 * 删除指定的帧
	 * 
	 * @param frame
	 */
	public void removeFrame(MsgFrame frame);

	/**
	 * 新增一个Frame
	 * 
	 * @param frame
	 */
	public void addFrame(MsgFrame frame);

	/**
	 * 获得子帧数量
	 * 
	 * @return
	 */
	public int getFrameCount();

	/**
	 * 获得当前包的短信类型
	 * 
	 * @return
	 */
	public MsgContent.MsgType getMsgType();

	/**
	 * 获得当前包的短信数据
	 * 
	 * @return
	 */
	public int getMsgCount();

	/**
	 * 获得当前短信包的类型
	 * 
	 * @return
	 */
	public SendType getSendType();

	/**
	 * 获得当前包的特服号码
	 * 
	 * @return
	 */
	public String getSpecNum();

	/**
	 * 获得当前包的扩展号码
	 * 
	 * @return
	 */
	public String getCustomNum();

	/**
	 * 获得当前短信包的UUID
	 * 
	 * @return
	 */
	public UUID getUuid();

	/**
	 * 获得信息批次的 UUID
	 * 
	 * @return
	 */
	// public UUID getBatchID();

	/**
	 * 获得当前短信包的请求时间
	 * 
	 * @return
	 */
	public Date getPostTime();

	/**
	 * 设置提交时间
	 * 
	 * @param time
	 * @return
	 */
	public void setPostTime(Date time);

	/**
	 * 获得计划发送时间
	 * 
	 * @return
	 */
	public Date getScheduleTime();

	/**
	 * 获得发送最后时限
	 * 
	 * @return
	 */
	public Date getDeadline();

	/**
	 * 获得锁版本号
	 * 
	 * @return
	 */
	public int getVersion();

	/**
	 * 重新设置所有的帧
	 * 
	 * @param fames
	 */
	public void setFrames(List<MsgFrame> fames);

	/**
	 * 设置时间范围的开始
	 * 
	 * @param time
	 */
	public void setBoeTime(Date time);

	/**
	 * 获得时间范围的开始
	 * 
	 * @return
	 */
	public Date getBoeTime();

	/**
	 * 设置时间范围的结束
	 * 
	 * @param time
	 */
	public void setEoeTime(Date time);

	/**
	 * 获得时间范围的结束
	 * 
	 * @return
	 */
	public Date getEoeTime();

	/**
	 * build meta info
	 * 
	 * @return
	 */
	public com.xuanwu.msggate.common.protobuf.CommonItem.BizMeta buildMeta();

	/**
	 * Set the uuid
	 * 
	 * @param uid
	 */
	public void setUuid(UUID uid);

	/**
	 * Set the batch id
	 * 
	 * @param batchID
	 */
	// public void setBatchID(UUID batchID);

	/**
	 * 获得当前包内需要发送处理帧的数量<br>
	 * <b>主要用于处理中计算，注意</b>
	 * 
	 * @return
	 */
	public int getValidFrameCount();

	public int getValidTicketCount();

	public int getInValidTicketCount();

	/**
	 * 获得当前包状态
	 * 
	 * @return
	 */
	public PackState getState();

	public int getPriority();

	public int getLevel();

	/**
	 * 设置当前包状态
	 * 
	 * @param state
	 */
	public void setState(PackState state);

	public PackState getOldState();

	public void setOldState(PackState oldState);

	public List<MediaItem> getMmsAttachments();

	public void setMmsAttachments(List<MediaItem> mmsAttachments);

	public void setMmsAttachmentsProtobuf(
			List<com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem> mmsAttachments);

	public MmsNeedApprovalType getMmsNeedApproval();

	public void setMmsApprovalStatus(int type);

	public int getEnterpriseID();

	public void setEnterpriseID(int enterpriseID);

	public int getUserID();

	public void setUserID(int userID);

	public void setBizType(int bizType);

	public int getBizType();

	public String getBatchName();

	public boolean isDistinct();

	public String getFrom();

	public String getRemark();

	public void setRemark(String remark);

	public void addParameter(String key, String value);

	public Object getParameter(String key);

	public Map<String, Object> getParameters();

	public void setParameters(Map<String, Object> map);

	public long getOfferTime();

	public void setOfferTime(long offerTime);
}

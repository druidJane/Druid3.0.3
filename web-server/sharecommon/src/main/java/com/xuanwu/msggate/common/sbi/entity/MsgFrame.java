/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Interface used to delegate one message frame
 * 
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-4-26
 * @Version 1.0.0
 */
public interface MsgFrame {

	public static final int MASS_FRAME = 1;
	public static final int GROUP_FRAME = 2;

	public static final String SPEC_IDS_SPLIT_STR = ",";

	/**
	 * Frame form type
	 */
	public enum BizForm {
		NORMAL(0),
		/**
		 * 黑名单信息帧
		 */
		BLACK(1),
		/**
		 * 红名单信息帧
		 */
		RED(2),
		/**
		 * 内容非法信息帧
		 */
		ILLEGALKEY(3),
		/**
		 * 区域限制
		 */
		REGION_LIMIT(4),
		/**
		 * 发送取消
		 */
		CANCEL(5),
		/**
		 * 系统分配端口发送信息帧
		 */
		SYS_BIND(6),
		/**
		 * 指定端口发送信息帧
		 */
		SPECIAL_ASSIGN(7),
		/**
		 * 区域优先发送信息帧
		 */
		REGION_PRIORITY(8),
		/**
		 * 通道切换
		 */
		CHANNEL_CHANGE(9),
		/**
		 * 端口选择失败信息帧
		 */
		NULL_SPECNUM(10),
		/**
		 * 号码非法
		 */
		ILLEGAL_PHONE(11),
		/**
		 * 信息已过期
		 */
		EXPIRED(12),
		/**
		 * 非白名单信息帧
		 */
		NON_WHITELIST(13),
		/**
		 * 非法彩信帧
		 */
		ILLEGAL_MMS(14),
		/**
		 * 红名单转白名单帧
		 */
		RED_WHITE_FORWARD(15),
		/**
		 * 超过最大容量
		 */
		MMS_OVER_LENGTH(16),
		/**
		 * 不是支持的类型
		 */
		MMS_OVER_TYPE(17),
		/**
		 * 重复号码
		 */
		REPEAT_PHONE(18),
		/**
		 * VIP 号码
		 */
		VIP_PHONE(19),
		/**
		 * 不在运营商号段
		 */
		OUTOF_CARRIER(20),
		/**
		 * 空白内容
		 */
		BLANK_CONTENT(21),
		/**
		 * 审批不通过
		 */
		AUDIT_DOES_NOT_PASS(22),
		/**
		 * 发送限制
		 */
		NUMBER_LIMIT(23),
		/**
		 * 不合法的语音内容
		 */
		ILLEGAL_VOICE_CODE(24),

        /**
         * 不合法的语音通知
         */
        ILLEGAL_VOICE_NOTICE(25);

		private final int index;

		private BizForm(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static BizForm getForm(int index) {
			BizForm[] bizForms = BizForm.values();
			for (BizForm bizForm : bizForms) {
				if (bizForm.getIndex() == index)
					return bizForm;
			}
			return NORMAL;
		}

		public static BizForm parseEnum(String str) {
			return BizForm.valueOf(str);
		}
	}

	/**
	 * The state of the frame
	 */
	public enum FrameState {
		/**
		 * Wait to handle
		 */
		WAIT(0),
		/**
		 * Handling, Retrieved by the mto server
		 */
		HANDLE(1),
		/**
		 * Handled
		 */
		OVER(2),
		/**
		 * Other state, can't be determined
		 */
		OTHER(3),
		/**
		 * ABANDONED frame,not need to be sent
		 */
		ABANDON(4),
		/**
		 * Wait to audit
		 */
		AUDITING(5),
		/**
		 * Auditing not pass
		 */
		AUDITING_NOT_PASS(6),
		/**
		 * Suspend sending
		 */
		SUSPEND(7),
		/**
		 * Second auditing
		 */
		SECOND_AUDITING(8);

		private final int index;

		private FrameState(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static FrameState getState(int index) {
			switch (index) {
			case 0:
				return WAIT;
			case 1:
				return HANDLE;
			case 2:
				return OVER;
			case 3:
				return OTHER;
			case 4:
				return ABANDON;
			case 5:
				return AUDITING;
			case 6:
				return AUDITING_NOT_PASS;
			case 7:
				return SUSPEND;
			case 8:
				return SECOND_AUDITING;
			default:
				return OTHER;
			}
		}
	}

	/**
	 * Get the message type
	 * 
	 * @return
	 */
	public MsgContent.MsgType getMsgType();

	/**
	 * Get all the phones associated with the frame
	 * 
	 * @return
	 */
	public List<String> getAllPhones();

	/**
	 * Get the current form
	 * 
	 * @return
	 */
	public BizForm getBizForm();

	/**
	 * Set the current form
	 * 
	 * @param form
	 */
	public void setBizForm(BizForm form);

	/**
	 * 获得当前帧的优先级
	 * 
	 * @return
	 */
	public int getPriority();

	/**
	 * 设置当前帧的优先级
	 * 
	 * @param priority
	 */
	public void setPriority(int priority);

	/**
	 * 设置短信运营商优先级
	 * 
	 * @param level
	 */
	public void setLevel(int level);

	/**
	 * 获得短信运营商优先级
	 * 
	 * @return
	 */
	public int getLevel();

	/*	*//**
	 * 获得当前帧的特服号码
	 * 
	 * @return
	 */
	/*
	 * public String getSpecNum();
	 *//**
	 * 设置特服号码
	 * 
	 * @param specNum
	 */
	/*
	 * public void setSpecNum(String specNum);
	 */
	/**
	 * 分配发送特服号码
	 * 
	 * @param specNum
	 */
	public void setAssignSpecNums(List<SpecSVSNumber> specNums);

	/**
	 * 获得发送特服号码
	 * 
	 * @return
	 */
	public List<SpecSVSNumber> getAssignSpecNums();

	/**
	 * 获得当前分配特服号码所属的Channel
	 * 
	 * @return
	 */
	public List<CarrierChannel> getAssignChannels();

	/**
	 * 设置实际发送特服号码<br/>
	 * <i>用于虚拟通道发送</i>
	 * 
	 * @param specNum
	 */
	public void setActualSpecNum(SpecSVSNumber specNum);

	/**
	 * 获得实际发送特服号码<br/>
	 * <i>用于虚拟通道发送</i>
	 * 
	 * @return
	 */
	public SpecSVSNumber getActualSpecNum();

	/**
	 * 获得自定义扩展号码
	 * 
	 * @return
	 */
	public String getCustomNum();

	/**
	 * 自定义扩展号码
	 * 
	 * @param extendNum
	 */
	public void setCustomNum(String extendNum);

	/**
	 * 当前帧是否需要状态报告
	 * 
	 * @return
	 */
	public boolean getReportState();

	/**
	 * 设置当前的帧状态报告
	 * 
	 * @param flag
	 */
	public void setReportState(boolean flag);

	/**
	 * 获得当前帧的待发送数据的二进制格式数据 用于数据的传递.
	 * <p>
	 * <b>采用protobuf编码格式</b>
	 * </p>
	 * 
	 * @return
	 */
	public byte[] getPack();

	/**
	 * 设置当前的帧数据<br />
	 * <b><i><u></i>只被用于反向生成实例时使用，其它情况避免使用<i></u></i></b>
	 */
	public void setPack(byte[] pack);

	/**
	 * Get the message count
	 * 
	 * @return
	 */
	public int getMsgCount();

	/**
	 * 获得帧提交时间
	 * 
	 * @return
	 */
	public Date getPostTime();

	/**
	 * 设置帧提交时间
	 * 
	 * @param time
	 */
	public void setPostTime(Date time);

	/**
	 * Get the deadline
	 * 
	 * @return
	 */
	public Date getDeadline();

	/**
	 * Get the schedule time
	 * 
	 * @return
	 */
	public Date getScheduleTime();

	/**
	 * Get the state of this frame
	 * 
	 * @return
	 */
	public FrameState getState();

	/**
	 * Get the single message by the index
	 * 
	 * @param index
	 * @return
	 */
	public MsgSingle getMsgSingle(int index);

	/**
	 * Get the time of retrieving to handle
	 * 
	 * @param time
	 */
	public Date getRetrieveTime();

	/**
	 * Set the retrieve time
	 * 
	 * @param time
	 */
	public void setRetrieveTime(Date time);

	/**
	 * Set the state
	 * 
	 * @param handle
	 */
	public void setState(FrameState handle);

	/**
	 * Create a frame with the frame meta data.
	 * 
	 * @return
	 */
	public MsgFrame duplitBasicFrame();

	/**
	 * Get all the message singles
	 * 
	 * @return
	 */
	public List<MsgSingle> getAllMsgSingle();

	/**
	 * Set all the message singles
	 * 
	 * @param msgSingles
	 */
	public void setAllMsgSingle(List<MsgSingle> msgSingles);

	/**
	 * Set packUUID
	 * 
	 * @param packUUID
	 *            the packUUID to set
	 */
	public void setPackUUID(UUID packUUID);

	/**
	 * Get packUUID
	 * 
	 * @return the packUUID
	 */
	public UUID getPackUUID();

	/**
	 * Get version
	 * 
	 * @return
	 */
	public int getVersion();

	/**
	 * Set version
	 * 
	 * @param version
	 */
	public void setVersion(int version);

	/**
	 * Set message type
	 * 
	 * @param type
	 */
	public void setMsgType(MsgContent.MsgType type);

	/**
	 * 获得发送区间的开始时间
	 * 
	 * @return
	 */
	public Date getBoeTime();

	/**
	 * 设置发送区间的开始时间
	 * 
	 * @param boeTime
	 */
	public void setBoeTime(Date boeTime);

	/**
	 * 获得发送区间的结束时间
	 * 
	 * @return
	 */
	public Date getEoeTime();

	/**
	 * 设置发送区间的结束时间
	 * 
	 * @param eoeTime
	 */
	public void setEoeTime(Date eoeTime);

	/**
	 * 获得当前帧的ID
	 * 
	 * @return
	 */
	public Long getId();

	/**
	 * 设置当前帧的ID
	 * 
	 * @param pid
	 */
	public void setId(Long pid);

	/**
	 * 设置当前Frame的附加参数
	 * 
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, String value);

	/**
	 * 获取当前Frame的附加参数
	 * 
	 * @param key
	 * @return
	 */
	public Object getParameter(String key);

	/**
	 * 获取当前所有的附加参数
	 * 
	 * @return
	 */
	public Map<String, Object> getParameters();

	/**
	 * 设置当前所有的附加参数
	 * 
	 * @param map
	 */
	public void setParameters(Map<String, Object> map);

	/**
	 * 获取当前帧所关联的话单<br>
	 * <b>只被用于MTO Sver的临时使用</b>
	 * 
	 * @return
	 */
	public List<Ticket> getTickets();

	/**
	 * 设置当前帧所关联的话单<br>
	 * <b>只被用于MTO Sver的临时使用</b>
	 * 
	 * @param tickets
	 */
	public void setTickets(List<Ticket> tickets);

	/**
	 * 设置帧发送完成时间
	 * 
	 * @param commitTime
	 */
	public void setCommitTime(Date commitTime);

	/**
	 * 获得帧发送完成时间
	 * 
	 * @return
	 */
	public Date getCommitTime();

	/**
	 * 获取发送类型
	 * 
	 * @return
	 */
	public MsgPack.SendType getSendType();

	/**
	 * Get the whole special number
	 * 
	 * @return
	 */
	public String getSpecNumber();

	/**
	 * Set the whole root special number
	 */
	public void setSpecNumber(String specNumber);

	/**
	 * Get the whole special number<br/>
	 * specNumber + customNumber
	 * 
	 * @return
	 */
	public String getWholeSpecNumber();

	/**
	 * Frame is assigned special service number or not
	 * 
	 * @return
	 */
	public boolean isAssignedSpecNumber();

	/**
	 * Get attribute
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key);

	/**
	 * Set attribute
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value);

	public int getMsgFrameType();

	public int getUserID();

	public void setUserID(int userID);

	public int getEnterpriseID();

	public void setEnterpriseID(int enterpriseID);

	public int getActualChannelId();

	public void setActualChannelId(int actualChannelId);

	public void setBizType(int bizType);

	public int getBizType();

	public String getAssignSpecIDs();

	public String getAssignChannelIDs();

	public String getSmil();

	public String getTitle();

	public void setSmil(String smil);

	public void setTitle(String title);

	public boolean isPreCommit();

	public void setPreCommit(boolean isPreCommit);
}

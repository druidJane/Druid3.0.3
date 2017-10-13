/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;



import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.DateUtil.DateTimeType;
import com.xuanwu.mos.utils.StringUtil;
import com.xuanwu.mos.utils.WebConstants;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Description: 短信话单实体类
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2012-9-28
 * @Version 1.0.0
 */
public class MsgTicket extends BaseEntity {

	private long id;
	private long ticketId;// 信息话单 ID
	private int bizType;// 业务类型ID
	private String packId;// 批次ID
	private String batchName;// 批次名称
	private long frameId;// 信息帧ID
	private int enterpriseId;// 企业ID
	private long parentId;// 信息父ID
	private String msgId;// 消息ID
	private Long sequenceNumber;// 消息序列号
	private int smsType;// 信息类型: 0,普通短信; 1,超长父短信(不会发送); 2,超长子短信; 3,wappush;
						// 4,彩信; 5,彩信父短信; 6,彩信子短信；8：语音通知；9：语音验证码
	private int encodeType;// 编码
	private String specNumber;// 发送端口
	private int total;// 短信的总段数
	private int number;// 短信的第几段
	private String smsContent;// 信息内容(含签名) longtext
	private String smsContentSkipSign;// 信息内容(不含签名)
	private String mmsTitle;// 彩信标题
	private String phone;// 目标号码
	private int state = -1;// 状态(等待发送:0,发送成功:1,被拒绝:2,数据格式错误:3,多次发送失败:4,帧结束标志:5,序列号错误:6,系统拒绝发送:7)--ticket表中的state
	private String stateStr;// 状态
	private String customMsgId;// 自定义消息ID
	private Date submitRespTime;// 提交运营商响应时间
	private String originResult;// 运营商返回的原始结果
	private int channelId;// 发送通道ID
	private String channelNum;// 发送通道号
	private String channelName;// 发送通道名称
	private int userId;// 用户ID
	private String userName;// 用户名
	private String preContent;// 前置内容
	private String sufContent;// 后置内容

	private int frameState;// 所在帧状态
	private int frameBizForm;// 所在帧类型
	private int stateReportResult = -1;// 状态报告
	private String stateReportOriginResult;// 原始状态报告
	private Date doneTime;// 状态报告完成时间
	private Date postTime;//提交时间
	private String msgTypeStr;// 发送状态
	
	private boolean mergeSign;// 是否导出用于重发
	private String linkMan;// 电话号码对应的姓名（“号码记录”里面的“姓名”栏对应值）
    private int bizForm; // 检核结果
    private int packState;// 发送结果{待审核，二次审核}
	private Integer isNormal;//gsms_statereport `is_normal` int(11) DEFAULT '0' COMMENT '是否正常数据0正常1人工',

	private Integer sendType;// '发送方式 群发：0，组发：1',

	private byte[] frameContent;//所在帧内容

	private String pwd;//发送密码

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public byte[] getFrameContent() {
		return frameContent;
	}

	public void setFrameContent(byte[] frameContent) {
		this.frameContent = frameContent;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Integer getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(Integer isNormal) {
		this.isNormal = isNormal;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTicketId() {
		return ticketId;
	}

	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}

	public int getBizType() {
		return bizType;
	}

	public void setBizType(int bizType) {
		this.bizType = bizType;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public long getFrameId() {
		return frameId;
	}

	public void setFrameId(long frameId) {
		this.frameId = frameId;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}	

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getSmsType() {
		return smsType;
	}

	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}

	public int getEncodeType() {
		return encodeType;
	}

	public void setEncodeType(int encodeType) {
		this.encodeType = encodeType;
	}

	public String getSpecNumber() {
		return specNumber;
	}

	public void setSpecNumber(String specNumber) {
		this.specNumber = specNumber;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getSmsContent() {
		if (StringUtils.isNotBlank(preContent)) {
			smsContent = preContent + smsContent;
		}
		if (StringUtils.isNotBlank(sufContent)) {
			smsContent = smsContent==null?"":smsContent;
			smsContent = smsContent + sufContent;
		}
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		/*if(StringUtils.isNotBlank(smsContent)){
			if(smsType == 2){
				smsContent = StringEscapeUtils.escapeXml(smsContent);
			} else {
				smsContent = smsContent.replace("<", "&lt;");
				smsContent = smsContent.replace(">", "&gt;");
			}
		}*/
		this.smsContent = smsContent;
		this.smsContentSkipSign = smsContent;
	}

	public String getSmsContentSkipSign() {
		return smsContentSkipSign;
	}

	public void setSmsContentSkipSign(String smsContentSkipSign) {
		this.smsContentSkipSign = smsContentSkipSign;
	}

	public String getMmsTitle() {
		return mmsTitle;
	}

	public void setMmsTitle(String mmsTitle) {
		this.mmsTitle = mmsTitle;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateStr() {
		if (stateStr == null) {
			int _state = this.state;
			int _result = this.stateReportResult;
			if (_state >= 0 && _state < WebConstants.MSG_TICKET_STATE.length
					&& _result == -1) {
				stateStr = WebConstants.MSG_TICKET_STATE[_state];
			} else if (_result >= 0
					&& _result < WebConstants.STATE_REPORT_RESULT.length) {
				stateStr = WebConstants.STATE_REPORT_RESULT[_result];
			}
		}
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public String getCustomMsgId() {
		return customMsgId;
	}

	public void setCustomMsgId(String customMsgId) {
		this.customMsgId = customMsgId;
	}
	
	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getSubmitRespTime() {
		return submitRespTime;
	}

	public void setSubmitRespTime(Date submitRespTime) {
		this.submitRespTime = submitRespTime;
	}

	public String getOriginResult() {
		return originResult;
	}

	public void setOriginResult(String originResult) {
		this.originResult = originResult;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPreContent() {
		return preContent;
	}

	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}

	public String getSufContent() {
		return sufContent;
	}

	public void setSufContent(String sufContent) {
		this.sufContent = sufContent;
	}

	public int getFrameState() {
		return frameState;
	}

	public void setFrameState(int frameState) {
		this.frameState = frameState;
	}

	public int getFrameBizForm() {
		return frameBizForm;
	}

	public void setFrameBizForm(int frameBizForm) {
		this.frameBizForm = frameBizForm;
	}

	public int getStateReportResult() {
		return stateReportResult;
	}

	public void setStateReportResult(int stateReportResult) {
		this.stateReportResult = stateReportResult;
	}

	public String getStateReportOriginResult() {
		return stateReportOriginResult;
	}

	public void setStateReportOriginResult(String stateReportOriginResult) {
		this.stateReportOriginResult = stateReportOriginResult;
	}

    public int getBizForm() {
        return bizForm;
    }

    public void setBizForm(int bizForm) {
        this.bizForm = bizForm;
    }

    public int getPackState() {
        return packState;
    }

    public void setPackState(int packState) {
        this.packState = packState;
    }

    public Date getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}

	public boolean isMergeSign() {
		return mergeSign;
	}

	public void setMergeSign(boolean mergeSign) {
		this.mergeSign = mergeSign;
	}
	//0,普通短信; 1,超长父短信(不会发送); 2,超长子短信; 3,wappush;
	// 4,彩信; 5,彩信父短信; 6,彩信子短信

	public String getMsgTypeStr() {
		if(msgTypeStr==null){
			int s = getSmsType();
			switch(s){
				case 0:
				case 1:
				case 2:msgTypeStr = "短信";break;		
				case 3:msgTypeStr = "WAP_PUSH";break;
				case 4:
				case 5:
				case 6:msgTypeStr = "彩信";break;
				case 8:msgTypeStr = "语音通知";break;
				case 9:msgTypeStr = "语音验证码";break;
				default: break;
			}
		}
		return msgTypeStr;
	}

	public void setMsgTypeStr(String msgTypeStr) {
		this.msgTypeStr = msgTypeStr;
	}

	public void mergeStateReport(MsgTicket t) {
		this.setStateReportResult(t.getStateReportResult());
		this.setStateReportOriginResult(t.getStateReportOriginResult());
		this.setDoneTime(t.getDoneTime());
	}

	@Override
	public String toString() {
		return "MsgTicket [id=" + id + ", ticketId=" + ticketId + ", bizType="
				+ bizType + ", packId=" + packId + ", frameId=" + frameId
				+ ", enterpriseId=" + enterpriseId + ", parentId=" + parentId
				+ ", msgId=" + msgId + ", sequenceNumber=" + sequenceNumber
				+ ", smsType=" + smsType + ", encodeType=" + encodeType
				+ ", specNumber=" + specNumber + ", total=" + total
				+ ", number=" + number + ", smsContent=" + smsContent
				+ ", phone=" + phone + ", state=" + state + ", customMsgId="
				+ customMsgId + ", submitRespTime=" + submitRespTime
				+ ", originResult=" + originResult + ", channelId=" + channelId
				+ ", userId=" + userId + ", userName=" + userName
				+ ", preContent=" + preContent + ", sufContent=" + sufContent
				+ "]";
	}

	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":").append(id);
		sb.append(",\"ticketId\":").append(ticketId);
		sb.append(",\"parentId\":").append(parentId);
		sb.append(",\"total\":").append(total);
		sb.append(",\"number\":").append(number);
		sb.append(",\"phone\":\"").append(phone).append("\"");
		if (smsType != 2) {// 短信
			sb.append(",\"content\":\"")
					.append(StringUtil.fixJsonStr(getSmsContent()))
					.append("\"");
		} else {// 彩信
			sb.append(",\"mmsTitle\":\"")
					.append(StringUtil.fixJsonStr(mmsTitle)).append("\"");
		}
		sb.append(",\"postTime\":\"")
		.append(DateUtil.format(postTime==null?submitRespTime:postTime, DateTimeType.DateTime))
		.append("\"");
		sb.append(",\"submitRespTime\":\"")
				.append(DateUtil.format(submitRespTime, DateTimeType.DateTime))
				.append("\"");
		sb.append(",\"state\":\"").append(getStateStr()).append("\"");
		sb.append(",\"result\":\"").append(originResult).append("\"");
		sb.append(",\"stateResult\":\"").append(stateReportOriginResult)
				.append("\"");
		sb.append(",\"userName\":\"").append(userName).append("\"");
		sb.append(",\"linkMan\":\"").append(linkMan).append("\"");
		sb.append(",\"channelNum\":\"").append(channelNum).append("\"");
		sb.append(",\"channelName\":\"").append(channelName).append("\"");
		sb.append(",\"batchName\":\"").append(batchName).append("\"");
		sb.append(",\"msgTypeStr\":\"" + getMsgTypeStr() + "\"");
		sb.append("}");
		return sb.toString();
	}

	public enum MsgSub {
		/* 所有 */
		All(0),

		FirstMsg(1),

		/* 父短信 */
		ParentMsg(2),

		/* 子短信 */
		ChildrenMsg(3);

		private int index;

		private MsgSub(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static MsgSub getType(int index) {
			switch (index) {
			case 0:
				return All;
			case 1:
				return FirstMsg;
			case 2:
				return ParentMsg;
			case 3:
				return ChildrenMsg;
			default:
				return All;
			}
		}
	}

	public enum MsgStatus {
		/* 所有 */
		All(0),
		/* 已经提交 */
		Submited(1),
		/* 发送成功 */
		Success(2),
		/* 发送失败 */
		Failure(3);

		private int index;

		private MsgStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static MsgStatus getType(int index) {
			switch (index) {
			case 0:
				return All;
			case 1:
				return Submited;
			case 2:
				return Success;
			case 3:
				return Failure;
			default:
				return All;
			}
		}
	}

	public enum MsgType {
		/* 所有 */
		ALL(0),
		/* 短信 */
		SMS(1),
		/* 彩信 */
		MMS(2),
		/* WapPush */
		WAPPUSH(3),
        /** 语音通知 */
        VOICE_NOTICE(8),
        /** 语音验证码 */
        VOICE_CODE(9);


		private int index;

		private MsgType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static MsgType getType(int index) {
			switch (index) {
                case 0:
                    return ALL;
                case 1:
                    return SMS;
                case 2:
                    return MMS;
                case 3:
                    return WAPPUSH;
                case 8:
                    return VOICE_NOTICE;
                case 9:
                    return VOICE_CODE;
			default:
				return SMS;
			}
		}
	}

}

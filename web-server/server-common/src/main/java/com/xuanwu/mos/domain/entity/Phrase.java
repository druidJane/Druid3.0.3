/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.JsonUtil;
import com.xuanwu.mos.utils.MmsUtil;
import com.xuanwu.mos.utils.StringUtil;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description: 模板实体类
 * @Author Jiang.Ziyuan
 * @Date 2017-04-05
 * @Version 1.0.0
 */
public class Phrase extends AbstractEntity {

	private Integer id;

	/**
	 * db中的blob数据类型的数据
	 */
	private byte[] content;

	/**
	 * 添加短语或模板的用户
	 */
	private int userId;

	/**
	 * 短语或模板标题
	 */
	private String title;

	/**
	 * 信息类型：1，短信；2，彩信；3，WAP_PUSH
	 */
	private int msgType;

	/**
	 * 模板标识
	 */
	private String identify;

	/**
	 * 是否免审，0为普通1为免审
	 */
	private int templateType;

	/**
	 * 企业id
	 */
	private int enterpriseId;

	/**
	 * 审核人id
	 */
	private int auditerId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 审核时间
	 */
	private Date auditTime;

	/**
	 * 审核状态-0:待审核1通过2不通过
	 */
	private int auditState;

	/**
	 * 最近修改时间
	 */
	private Date lastUpdateTime;

	/**
	 * 审核不通过时的内容
	 */
	private String remark;

	// 由于content是一个byte[]，因此添加一个contentStr来显示content的字符串
	private String contentStr;
	private String phraseSMSContent;
	private int phraseType;//模板类型：0为普通模板；1为变量模板

	public Phrase() {
	}

	//by jiangziyuan
	public Phrase(int id, byte[] content, String title, int bizType,
				  String identify,int enterpriseId,int auditerId,int templateType) {
		this.id = id;
		this.setContent(content);
		this.title = title;
		this.lastUpdateTime = new Date();
		this.enterpriseId = enterpriseId;
		this.auditerId = auditerId;
		this.templateType = templateType;
		this.identify = identify;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public int getAuditerId() {
		return auditerId;
	}

	public void setAuditerId(int auditerId) {
		this.auditerId = auditerId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContentStr() {
		if (content != null) {
			return new String(content, Charset.forName("UTF-8"));
		}
		return null;
	}

	//	public String getContentStr() {
//		if (content != null) {
//			return new String(content, Charset.forName("UTF-8"));
//		}
//		return null;
//	}


	public String getPhraseSMSContent() {
		return phraseSMSContent;
	}

	public void setPhraseSMSContent(String phraseSMSContent) {
		this.phraseSMSContent = phraseSMSContent;
	}

	public int getPhraseType() {
		return phraseType;
	}

	public void setPhraseType(int phraseType) {
		this.phraseType = phraseType;
	}

	public String toSmsJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		if (msgType == 1||msgType==8 ||msgType==9) {
			sb.append(",\"content\":\"")
					.append(StringUtil.fixJsonStr(StringUtil.replaceHtml(contentStr))).append("\"");
		} else {
			sb.append(",\"content\":\"\"");
		}
		sb.append(",\"identify\":\"")
				.append(StringUtil.fixJsonStr(StringUtil.trimNull(identify)))
				.append("\"");
		sb.append(",\"userId\":").append(userId);
		sb.append(",\"title\":\"").append(StringUtil.fixJsonStr(title))
				.append("\"");
		sb.append(",\"msgType\":").append(msgType);
		sb.append(",\"lastUpdateTime\":\""
				+ DateUtil.format(lastUpdateTime, DateUtil.DateTimeType.DateTime) + "\"");
		sb.append(",\"auditingState\":").append(auditState);
		sb.append("}");
		return sb.toString();
	}

	public String toMmsJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		if (msgType == 2) {
			try {
				String contentStr = new String(content,Charset.forName("UTF-8"));
				sb.append(",\"content\":").append(JsonUtil.serialize(MmsUtil.fromTemplate(contentStr)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			sb.append(",\"content\":\"\"");
		}
		sb.append(",\"userId\":").append(userId);
		sb.append(",\"title\":\"").append(StringUtil.fixJsonStr(StringUtil.replaceHtml(title)))
				.append("\"");
		sb.append(",\"msgType\":").append(msgType);
		sb.append(",\"identify\":\"").append(StringUtil.fixJsonStr(StringUtil.replaceHtml(identify)))
				.append("\"");
		sb.append(",\"lastUpdateTime\":\""
				+ DateUtil.format(lastUpdateTime, DateUtil.DateTimeType.DateTime) + "\"");
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "Phrase{" +
				"id=" + id +
				", content=" + Arrays.toString(content) +
				", userId=" + userId +
				", title='" + title + '\'' +
				", msgType=" + msgType +
				", identify='" + identify + '\'' +
				", templateType=" + templateType +
				", enterpriseId=" + enterpriseId +
				", auditerId=" + auditerId +
				", createTime=" + createTime +
				", auditTime=" + auditTime +
				", auditState=" + auditState +
				", lastUpdateTime=" + lastUpdateTime +
				", remark='" + remark + '\'' +
				'}';
	}
}

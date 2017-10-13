package com.xuanwu.mos.domain.entity;


import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.StringUtil;

import java.util.Date;

/**
 * 企业白名单，区别于通道白名单实体WhiteList
 */
public class WhitePhone extends BaseEntity{
	
	/** primary id */
	private Integer id;
	
	/** 手机号码 */
	private String telphone;
	
	/** 生效时间 */
	private Date effectiveDate;
	
	/** 企业ID */
	private Integer enterpriseId;
	
	/** 创建时间 */
	private Date createTime;
	
	/** 是否通知客户0为未通知1为已通知 */
	private Integer isNotice;
	
	/** 通知操作人id */
	private Integer operatorId;
	/** 导入类型*/
	public enum NoticeType{
		ENIMPORTED(0,"未导入"),
		IMPORTED(1,"已导入");
		private Integer value;
		private String name;

		private NoticeType(int value,String name) {
			this.name = name;
			this.value = value;
		}
		public Integer getValue(){
			return value;
		}
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(Integer isNotice) {
		this.isNotice=isNotice;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	
	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"telphone\":").append(telphone);
		sb.append(",\"effectiveDate\":\"")
				.append(StringUtil.fixJsonStr(DateUtil.format(effectiveDate,
						DateUtil.DateTimeType.DateTime))).append("\"");
		sb.append(",\"enterpriseId\":\"").append(enterpriseId).append("\"");
		sb.append(",\"createTime\":\"")
				.append(StringUtil.fixJsonStr(DateUtil.format(createTime,
						DateUtil.DateTimeType.DateTime))).append("\"");
		sb.append(",\"isNotice\":\"").append(isNotice).append("\"");
		sb.append(",\"operatorId\":\"").append(operatorId).append("\"");
		sb.append(",\"enterpriseId\":").append(enterpriseId);
		sb.append('}');
		return sb.toString();
	}

}

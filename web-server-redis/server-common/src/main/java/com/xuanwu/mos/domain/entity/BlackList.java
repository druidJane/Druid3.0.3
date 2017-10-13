/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.DateUtil.DateTimeType;
import com.xuanwu.mos.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-10-30
 * @Version 1.0.0
 */
public class BlackList extends AbstractEntity {

	/** primary id */
	private int id;

	/** 缓存phone primary id */
	private long desID;

	/** 企业ID */
	private int enterpriseID;

	/** 手机号码 */
	private String phone;

	/** 创建时间 */
	private Date createTime;

	/** 所属用户 */
	private String user;

	/** 操作平台 Backend:0  Frontkit: 1，沿用mos逻辑，该系统其他表Frontkit都用2，黑名单表Frontkit用1表示*/
	private int handleFrom = 1;

	/** 归属目标 */
	private int target;

	/** 目标名称 */
	private String targetName;

	/** 黑名单类型 */
	private int type = -1;

	private BlacklistType blacklistType;

	private boolean isRemove;
	/** 备注 */
	private String remark;

	private Date handleTime;

	/** 保存压缩后的数据 */
	private long zipmes;

	private String sourcePhone;

	private int sourceType;

	private int sourceTarget;

	private Integer removeId;
	private String tmpTypeName;

	public String getTmpTypeName() {
		return BlacklistType.getTypeName(getType());
	}

	public void setTmpTypeName(String tmpTypeName) {
		this.tmpTypeName = tmpTypeName;
	}

	public enum BlacklistType {
		Illegal(-1), User(0), Enterprise(2), CHANNEL(4), BizType(5);

		private int index;

		private BlacklistType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static BlacklistType getType(int index) {
			switch (index) {
			case 0:
				return User;
			case 2:
				return Enterprise;
			case 4:
				return CHANNEL;
			case 5:
				return BizType;
			default:
				return Illegal;
			}
		}

		public static BlacklistType getType(String typeName) {
			if (StringUtils.isEmpty(typeName)) {
				return Illegal;
			}

			String tempTypeName = typeName.trim();
			if ("用户".equals(tempTypeName)) {
				return User;
			} else if ("企业".equals(tempTypeName)) {
				return Enterprise;
			} else if ("通道".equals(tempTypeName)) {
				return CHANNEL;
			} else if ("业务类型".equals(tempTypeName)) {
				return BizType;
			} else {
				return Illegal;
			}
		}

		public static String getTypeName(int type) {
			switch (type) {
			case 0:
				return "用户";
			case 2:
				return "企业";
			case 4:
				return "通道";
			case 5:
				return "业务类型";
			default:
				return "企业";
			}
		}
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getDesID() {
		return desID;
	}

	public void setDesID(long desID) {
		this.desID = desID;
	}

	public int getEnterpriseID() {
		return enterpriseID;
	}

	public void setEnterpriseID(int enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getHandleFrom() {
		return handleFrom;
	}

	public void setHandleFrom(int handleFrom) {
		this.handleFrom = handleFrom;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BlacklistType getBlacklistType() {
		return blacklistType;
	}

	public void setBlacklistType(BlacklistType blacklistType) {
		this.blacklistType = blacklistType;
		this.type = blacklistType.getIndex();
	}

	/**
	 * @param isRemove
	 *            要设置的 isRemove
	 */
	public void setRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}

	/**
	 * @return isRemove
	 */
	public boolean isRemove() {
		return isRemove;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public long getZipmes() {
		return zipmes;
	}

	public void setZipmes(long zipmes) {
		this.zipmes = zipmes;
	}

	public String getSourcePhone() {
		return sourcePhone;
	}

	public void setSourcePhone(String sourcePhone) {
		this.sourcePhone = sourcePhone;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceTarget() {
		return sourceTarget;
	}

	public void setSourceTarget(int sourceTarget) {
		this.sourceTarget = sourceTarget;
	}

	public Integer getRemoveId() {
		return removeId;
	}

	public void setRemoveId(Integer removeId) {
		this.removeId = removeId;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(desID);
		sb.append(",\"phone\":\"").append(phone).append("\"");
		sb.append(",\"type\":").append(type);
		sb.append(",\"targetName\":\"")
				.append(StringUtil.fixJsonStr(targetName)).append("\"");
		sb.append(",\"createTime\":\"")
				.append(DateUtil.format(createTime, DateTimeType.DateTime))
				.append("\"");
		sb.append(",\"remark\":\"").append(StringUtil.fixJsonStr(remark))
				.append("\"");
		sb.append('}');
		return sb.toString();
	}
}

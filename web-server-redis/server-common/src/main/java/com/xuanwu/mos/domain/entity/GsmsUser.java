package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.domain.enums.UserType;

import java.util.Date;
import java.util.List;

/**
 * @Description 用户实体抽象类 
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-9-7
 * @Version 1.0.0
 */
public abstract class GsmsUser extends AbstractEntity {
	protected int id;
	
	/** 账号状态：NORMAL(0), SUSPEND(1), TERMINATE(2); 0,1可以互转，从0或1转到2后不能再转 */
	protected UserState state;
	protected String showState;
	/** 父ID */
	protected int parentId;
	/** 父唯一标识符 */
	protected String parentIdentify;
	
	/** 企业标识号或用户标识码或部门编号 */
	protected String identify;

	/** 用户签名 */
	protected String signature;
	
	/** 0:后置  1:前置 */
	protected int sigLocation;
	
	/** 联系电话 */
	protected String phone;
	
	/** 联系地址 */
	protected String address;
	
	/** 联系EMAIL */
	protected String email;
	
	/** 创建时间 */
	protected Date createTime;
	
	/** 0:Backend; 1:UMP; 2:FrontKit; */
	protected int platformId;

	/** platformMode */
	protected Platform platform;
	
	/** 联系人 */
	protected String linkMan;
	
	/** 路径，查询时以避免递归 */
	protected String path;
	
	/** 备注 */
	protected String remark;
	
	/** 企业ID */
	protected int enterpriseId;
	
	protected String enterpriseName;
	
	protected String domain;
	
	/** 角色名组合 */
	protected String roleNames;
	
	/** 业务类型名组合 */
	protected String bizNames;

	/* 业务类型编号组合 */
	protected String bizIds;

	/** 分配的角色列表 */
	protected List<Role> roles;
	
	/** 绑定的业务类型列表 */
	protected List<BizType> bizTypes;

	protected Date lastModifyPwdTime;

	/* 0: 企业，1: 部门，2: 个人 */
	protected UserType userType;

	public String getShowState() {
		if (state != null) {
			switch (state) {
				case NORMAL:
					return "启用";
				default:
					return "停用";
			}
		}
		return null;
	}

	public void setShowState(String showState) {
		this.showState = showState;
	}

	public String getBizIds() {
		return bizIds;
	}

	public void setBizIds(String bizIds) {
		this.bizIds = bizIds;
	}

	public Date getLastModifyPwdTime() {
		return lastModifyPwdTime;
	}

	public void setLastModifyPwdTime(Date lastModifyPwdTime) {
		this.lastModifyPwdTime = lastModifyPwdTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public UserState getState() {
		return state;
	}
	
	public void setState(UserState state) {
		this.state = state;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public int getPlatformId() {
		return platformId;
	}
	
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	
	public String getIdentify() {
		return identify;
	}
	
	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getParentIdentify() {
		return parentIdentify;
	}

	public void setParentIdentify(String parentIdentify) {
		this.parentIdentify = parentIdentify;
	}

	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public int getSigLocation() {
		return sigLocation;
	}
	
	public void setSigLocation(int sigLocation) {
		this.sigLocation = sigLocation;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getBizNames() {
		return bizNames;
	}
	
	public void setBizNames(String bizNames) {
		this.bizNames = bizNames;
	}
	
	public String getRoleNames() {
		return roleNames;
	}
	
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public List<BizType> getBizTypes() {
		return bizTypes;
	}
	
	public void setBizTypes(List<BizType> bizTypes) {
		this.bizTypes = bizTypes;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}
	
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	public String getEnterpriseName() {
		return enterpriseName;
	}
	
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 完整路径，包含当前ID
	 * @return
	 */
	public abstract String getFullPath();
	
	public abstract UserType getType();
	
	public abstract boolean hasChild();
	
	public abstract void setHasChild(boolean hasChild);

}

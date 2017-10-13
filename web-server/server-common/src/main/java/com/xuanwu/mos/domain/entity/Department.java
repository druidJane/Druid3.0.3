package com.xuanwu.mos.domain.entity;


import com.xuanwu.mos.domain.enums.UserType;
import com.xuanwu.mos.utils.Delimiters;

import java.util.List;

/**
 * @Description 部门
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-9-6
 * @Version 1.0.0
 */
public class Department extends GsmsUser {
	
	/** 部门名称 */
	private String deptName;

	private String deptNoPrefix;
	
	private boolean hasChild;
	
	private boolean hasUser;

	private boolean prepay;	//是否预付费

	private boolean postpaid;	//是否后付费

	private List<Integer> delRoles;
	
	private List<Integer> delBizs;

	private String parentDeptName;

	/* 绑定的计费账户 */
	private List<CapitalAccount> capitalAccounts;

	/* 企业部门根节点 */
	private boolean baseDept;

	public boolean isBaseDept() {
		return baseDept;
	}

	public void setBaseDept(boolean baseDept) {
		this.baseDept = baseDept;
	}

	public String getParentDeptName() {
		return parentDeptName;
	}

	public void setParentDeptName(String parentDeptName) {
		this.parentDeptName = parentDeptName;
	}

	public List<CapitalAccount> getCapitalAccounts() {
		return capitalAccounts;
	}

	public void setCapitalAccounts(List<CapitalAccount> capitalAccounts) {
		this.capitalAccounts = capitalAccounts;
	}

	public String getDeptName() {
		return deptName;
	}
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public String getDeptNoPrefix() {
		return deptNoPrefix;
	}
	
	public void setDeptNoPrefix(String deptNoPrefix) {
		this.deptNoPrefix = deptNoPrefix;
	}
	
	@Override
	public String getFullPath() {
		return this.path == null ? this.path : (this.path + this.id + Delimiters.DOT);
	}
	
	@Override
	public UserType getType() {
		return UserType.DEPARTMENT;
	}
	
	@Override
	public boolean hasChild() {
		return hasChild;
	}
	
	@Override
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	
	public boolean hasUser() {
		return hasUser;
	}
	
	public boolean isHasUser() {
		return hasUser;
	}

	public void setHasUser(boolean hasUser) {
		this.hasUser = hasUser;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public List<Integer> getDelRoles() {
		return delRoles;
	}

	public void setDelRoles(List<Integer> delRoles) {
		this.delRoles = delRoles;
	}

	public List<Integer> getDelBizs() {
		return delBizs;
	}

	public void setDelBizs(List<Integer> delBizs) {
		this.delBizs = delBizs;
	}

	public boolean isPrepay() {
		return prepay;
	}

	public void setPrepay(boolean prepay) {
		this.prepay = prepay;
	}

	public boolean isPostpaid() {
		return postpaid;
	}

	public void setPostpaid(boolean postpaid) {
		this.postpaid = postpaid;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"pId\":").append(parentId);
		sb.append(",\"type\":").append(UserType.DEPARTMENT.getIndex());
		sb.append(",\"name\":\"").append(deptName).append('\"');
		sb.append(",\"path\":\"").append(path).append('\"');
		sb.append(",\"identify\":\"").append(identify).append('\"');
		sb.append(",\"bizNames\":\"").append(bizNames).append('\"');
		sb.append(",\"roleNames\":\"").append(roleNames).append('\"');
		sb.append('}');
		return sb.toString();
	}

}

/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import java.io.Serializable;


/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-11-30
 * @Version 1.0.0
 */
public class UserStatistics extends BaseStatistics {

	/** 用户ID */
	private int userId;

	/** 用户名称 */
	private String userName;

	/** 部门ID */
	private int deptId;

	/** 部门名称 */
	private String deptName;

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

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}


}

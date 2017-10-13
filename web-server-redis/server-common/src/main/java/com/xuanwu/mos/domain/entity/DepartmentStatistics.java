/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-11-30
 * @Version 1.0.0
 */
public class DepartmentStatistics extends BaseStatistics {

	/** 部门ID */
	private int deptId;
	
	/** 部门ID */
	private String deptIdStr;

	/** 部门名称 */
	private String deptName;

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptIdStr() {
		return deptIdStr;
	}

	public void setDeptIdStr(String deptIdStr) {
		this.deptIdStr = deptIdStr;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return deptId;
	}


	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}





}

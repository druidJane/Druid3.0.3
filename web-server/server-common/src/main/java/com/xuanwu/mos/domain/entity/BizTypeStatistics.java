/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-12-5
 * @Version 1.0.0
 */
public class BizTypeStatistics extends BaseStatistics {

	/** 业务类型ID */
	private int bizTypeId;

	/** 业务类型名称 */
	private String bizTypeName;
	
	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(int bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
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

/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

/**
 * Simple JavaBean domain object with an id property.
 * Used as a base class for objects needing this property.
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-10-11
 * @Version 1.0.0
 */
public abstract class BaseEntity extends AbstractEntity {
/*	protected String extArr1;
	
	public String getExtArr1() {
		return extArr1;
	}
	
	public void setExtArr1(String extArr1) {
		this.extArr1 = extArr1;
	}*/

	public abstract String toJSON();
}

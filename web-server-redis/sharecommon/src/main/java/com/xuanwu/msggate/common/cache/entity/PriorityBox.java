/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.cache.entity;

/**
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-2-15
 * @Version 1.0.0
 */
public class PriorityBox {

	private int index;
	private boolean isLast;
	private Priority priority;

	public PriorityBox(int index, Priority priority, boolean isLast) {
		this.index = index;
		this.priority = priority;
		this.isLast = isLast;
	}

	public int getIndex() {
		return index;
	}

	public boolean isLast() {
		return isLast;
	}

	public Priority getPriority() {
		return priority;
	}

	@Override
	public String toString() {
		return "[index=" + index + ",isLast=" + isLast + ",priority="
				+ priority.toString() + "]";
	}
}

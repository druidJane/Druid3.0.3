/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.cache.entity;

/**
 * 优先级别
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-2-15
 * @Version 1.0.0
 */
public class Priority {

	private int priority;
	private int frameCount;

	public Priority() {

	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	@Override
	public String toString() {
		return "[priority=" + priority + ",frameCount=" + frameCount + "]";
	}

}

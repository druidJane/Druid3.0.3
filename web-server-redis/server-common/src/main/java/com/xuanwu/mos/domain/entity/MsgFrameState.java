/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 批次帧属性值
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2012-9-27
 * @Version 1.0.0
 */
public class MsgFrameState  extends AbstractEntity {
	private Integer minState;// 最小状态值(0~2)
	private Integer maxState;// 最大状态值(0~2)
	private Date doneTime;// 完成时间
	private Integer doneCount;// 完成短信数

	public Integer getMinState() {
		return minState;
	}

	public void setMinState(Integer minState) {
		this.minState = minState == null ? 0 : minState.intValue();
	}

	public Integer getMaxState() {
		return maxState;
	}

	public void setMaxState(Integer maxState) {
		this.maxState = maxState == null ? 0 : maxState.intValue();
	}

	public Date getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}

	public Integer getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(Integer doneCount) {
		this.doneCount = doneCount == null ? 0 : doneCount.intValue();
	}

	@Override
	public Serializable getId() {
		return null;
	}
}

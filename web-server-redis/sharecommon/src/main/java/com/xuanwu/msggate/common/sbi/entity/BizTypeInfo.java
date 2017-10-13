/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-7-21
 * @Version 1.0.0
 */
public class BizTypeInfo implements Cloneable{
	
	private static final Logger logger = LoggerFactory.getLogger(BizTypeInfo.class);

	private Integer id;
	private boolean extendFlag;
	private int priority;
	private String name;
	private int state;
	private Date startTime;
	private Date endTime;
	private int type;
	private int speed;
	private int speedMode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isExtendFlag() {
		return extendFlag;
	}

	public void setExtendFlag(boolean extendFlag) {
		this.extendFlag = extendFlag;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(String source) {
		if (StringUtils.isNotBlank(source)){
			String[] arr = source.split(":");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(1971, 0, 1, Integer.parseInt(arr[0]), 
					Integer.parseInt(arr[1]), 
					Integer.parseInt(arr[2]));
			startTime = cal.getTime();
		}
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(String source) {
		if (StringUtils.isNotBlank(source)){
			String[] arr = source.split(":");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(1971, 0, 1, Integer.parseInt(arr[0]), 
					Integer.parseInt(arr[1]), 
					Integer.parseInt(arr[2]));
			endTime = cal.getTime();
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeedMode() {
		return speedMode;
	}

	public void setSpeedMode(int speedMode) {
		this.speedMode = speedMode;
	}
	
	@Override
	public Object clone()  {
		Object obj = null;		
		 try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("Clone BizTypeInfo error",e);
		}
		 return obj;
	}
	@Override
	public String toString() {
		return "BizTypeInfo [endTime=" + endTime + ", extendFlag=" + extendFlag
				+ ", id=" + id + ", name=" + name + ", priority=" + priority
				+ ", startTime=" + startTime + ", state=" + state + ", speed=" 
				+ speed + ", speedMode=" + speedMode + ", type="
				+ type + "]";
	}

}

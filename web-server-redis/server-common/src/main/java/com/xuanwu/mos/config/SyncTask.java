/*
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.config;

/**
 * @Description 可变周期同步任务，需要同步的任务实现该类
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-11-27
 * @Version 1.0.0
 */
public interface SyncTask extends Runnable {

	/**
	 * 同步周期，更新后，在下一周期生效
	 * 
	 * @return
	 */
	public long getPeriod();

}
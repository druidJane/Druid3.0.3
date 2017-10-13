/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.job;

import com.xuanwu.mos.config.SyncTask;
import com.xuanwu.mos.domain.entity.BlackList;
import com.xuanwu.mos.rest.service.BlackListService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author <a href="fengwei@139130.net">Wei.Feng</a>
 * @Date 2013-2-26
 * @Version 1.0.0
 */
@Component("deleteBlackJob")
public class DeleteBlackJob implements SyncTask {

	private static final Logger logger = LoggerFactory
			.getLogger(DeleteBlackJob.class);

	private String maxPoolTaskSize = "20000";
	private BlockingQueue<BlackList> blackTaskQueue = new ArrayBlockingQueue<BlackList>(
			Integer.parseInt(maxPoolTaskSize));
	private BlackListService blacklistService;

	private int lastRunDay = Calendar.getInstance().get(Calendar.DATE);

	@Override
	public void run() {
		try {
			//TODO 测试
			if (checkRun()||1==1) {
				Date currentTime = new Date();
				boolean isEmpty = blackTaskQueue.isEmpty();
				while (!isEmpty) {
					BlackList task = blackTaskQueue.poll();
					blacklistService.delBlacklist(task.getId());
					isEmpty = blackTaskQueue.isEmpty();
				}
				logger.info("End to deleteBlackJob at time : " + currentTime);
			}
		} catch (Exception e) {
			logger.error("run task failed,cause by:{}", e);
			e.printStackTrace();
		}
	}

	public void putTaskQueue(List<BlackList> task) {
		if (!task.isEmpty()) {
			for (int i = 0; i < task.size(); i++) {
				BlackList black = task.get(i);
				blackTaskQueue.offer(black);
			}
		}
	}

	private boolean checkRun() {
		Calendar curDate = Calendar.getInstance();
		logger.info("Check is need to run deleteBlackJob service.");
		if (curDate.get(Calendar.DATE) != lastRunDay) {
			lastRunDay = curDate.get(Calendar.DATE);
			return true;
		}
		return false;
	}

	@Override
	public long getPeriod() {
		return 60 * 60 * 1000;
	}

	@Autowired
	public void setBlacklistService(BlackListService blacklistService) {
		this.blacklistService = blacklistService;
	}

	@Value("${fileTask.maxPoolSize}")
	public void setMaxPoolTaskSize(String maxPoolTaskSize) {
		this.maxPoolTaskSize = maxPoolTaskSize;
	}

}

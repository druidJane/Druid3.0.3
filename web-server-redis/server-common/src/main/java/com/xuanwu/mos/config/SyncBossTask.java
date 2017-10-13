/*
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.config;

import com.xuanwu.mos.utils.ListUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-11-27
 * @Version 1.0.0
 */
public class SyncBossTask implements Runnable {

	private Logger logger = LoggerFactory.getLogger(SyncBossTask.class);

	private DelayQueue<InnerSyncTask> tasks = new DelayQueue<InnerSyncTask>();

	private Executor executor;

	public void init() {
		executor.execute(this);
	}

	@Override
	public void run() {
		while (true) {
			try {
				InnerSyncTask innerTask = tasks.take();
				executor.execute(innerTask);
			} catch (Throwable t) {
				logger.error("Sync boss task exception: ", t);
			}
		}
	}

	private class InnerSyncTask implements Delayed, Runnable {
		SyncTask task;
		long sTime;
		long delay;

		public InnerSyncTask(SyncTask task) {
			this.task = task;
		}

		@Override
		public void run() {
			try {
				task.run();
			} finally {
				resetDelay();
				tasks.add(this);
			}
		}

		@Override
		public int compareTo(Delayed o) {
			InnerSyncTask other = (InnerSyncTask) o;
			return sTime > other.sTime ? 1 : (sTime < other.sTime ? -1 : 0);
		}

		@Override
		public long getDelay(TimeUnit unit) {
			if (sTime - System.currentTimeMillis() > delay) {
				return 0;
			}
			return unit.convert(sTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}

		public void resetDelay() {
			long period = task.getPeriod();
			this.sTime = System.currentTimeMillis() + period;
			this.delay = period;
		}
	}

	public void setTasks(List<SyncTask> tasks) {
		if (ListUtil.isBlank(tasks))
			return;
		for (SyncTask task : tasks) {
			InnerSyncTask innerTask = new InnerSyncTask(task);
			innerTask.resetDelay();
			this.tasks.add(innerTask);
		}
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
}

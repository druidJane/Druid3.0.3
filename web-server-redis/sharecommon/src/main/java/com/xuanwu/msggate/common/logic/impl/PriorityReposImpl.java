/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.logic.impl;

import com.xuanwu.msggate.common.cache.dao.BizDataFetchDao;
import com.xuanwu.msggate.common.cache.entity.Priority;
import com.xuanwu.msggate.common.cache.entity.PriorityBox;
import com.xuanwu.msggate.common.counter.FrameCounter;
import com.xuanwu.msggate.common.logic.PriorityRepos;
import com.xuanwu.msggate.common.util.DateUtil;
import com.xuanwu.msggate.common.util.ListUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

/**
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-2-15
 * @Version 1.0.0
 */
public class PriorityReposImpl implements PriorityRepos, Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(PriorityReposImpl.class);
	
	int version;
	int totalFrameCount = 0;
	List<Priority> prorityList;
	Map<String, FrameCounter> frameCounterMap;

	/**
	 * Business data fetch dao
	 */
	private BizDataFetchDao dao;
	
	@PostConstruct
	public void init() {
		try {
			prorityList = new ArrayList<Priority>();
			frameCounterMap = new ConcurrentHashMap<String, FrameCounter>();

			loadPrioritys();
		} catch (Exception e) {
			logger.error("Load message priority from DB failed, cause by:{}", e);
		}
	}
	
	@Override
	public void run() {
		syncPrioritys();
	}

	private void syncPrioritys() {
		try {
			int newVersion = dao.fetchPriorityVersion();
			if (!(newVersion > version))
				return;
			version = newVersion;
			syncPrioritys();
		} catch (Exception e) {
			logger.error("sync frame priority from DB failed, cause by:{}", e);
		}
	}
	
	private void loadPrioritys() {
		try {
			System.out.println("Begin load message priority from DB, at:" + DateUtil.getCurrentDate());
			
			List<Priority> tempList = dao.fetchAllPrioritys();

			if (ListUtil.isBlank(tempList)) {
				throw new RuntimeException(
						"The priority of frame is not configuration.");
			}

			prorityList = tempList;

			for (Priority priority : prorityList) {
				totalFrameCount += priority.getFrameCount();
			}

			Set<String> ketSet = frameCounterMap.keySet();
			Iterator<String> iter = ketSet.iterator();
			while (iter.hasNext()) {
				frameCounterMap.get(iter.next()).setMaxFrameCount(
						totalFrameCount);
			}
			
			System.out.println("End load message priority from DB, at:" + DateUtil.getCurrentDate());
		} catch (Exception e) {
			logger.error("Load frame priority from DB failed, cause by:{}", e);
		}
	}

	public PriorityBox getPriority(String identity) {

		putNewFrameCounter(identity);

		FrameCounter counter = frameCounterMap.get(identity);
		int frameCount = counter.incrment();

		int curCount = 0;
		for (int i = 0; i < prorityList.size(); i++) {
			curCount += prorityList.get(i).getFrameCount();

			if (frameCount <= curCount) {
				boolean isLast = (i == (prorityList.size() - 1)) ? true : false;
				return new PriorityBox(i, prorityList.get(i), isLast);
			}
		}

		return new PriorityBox(0, prorityList.get(0), false);
	}

	public PriorityBox getNextPriority(String identity, PriorityBox priorityBox) {
		FrameCounter counter = frameCounterMap.get(identity);

		int curCount = 0;
		for (int i = 0; i <= priorityBox.getIndex(); i++) {
			curCount += prorityList.get(i).getFrameCount();
		}
		counter.setFrameCount(curCount);

		int index = (priorityBox.getIndex() == (prorityList.size() - 1)) ? 0
				: priorityBox.getIndex() + 1;
		boolean isLast = (index == (prorityList.size() - 1)) ? true : false;

		return new PriorityBox(index, prorityList.get(index), isLast);
	}

	public void setPollEndFlag(String identity) {
		FrameCounter counter = frameCounterMap.get(identity);
		counter.setFrameCount(totalFrameCount);
	}

	private void putNewFrameCounter(String identity) {
		if (!frameCounterMap.containsKey(identity)) {
			FrameCounter frameCounter = new FrameCounter(totalFrameCount);
			frameCounterMap.put(identity, frameCounter);
		}
	}

	public void setDao(BizDataFetchDao dao) {
		this.dao = dao;
	}
}

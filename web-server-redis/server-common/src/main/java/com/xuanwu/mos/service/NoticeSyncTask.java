package com.xuanwu.mos.service;


import com.xuanwu.mos.config.SyncTask;
import com.xuanwu.mos.domain.entity.Notice;
import com.xuanwu.mos.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zengzl 2017-4-20  消息通知
 *
 */
@Component
public class NoticeSyncTask implements SyncTask {
	private Logger logger = LoggerFactory.getLogger(NoticeSyncTask.class);
	
	@Autowired
	private NoticeService noticeService;

	private LinkedBlockingDeque<Notice> addQueue = new LinkedBlockingDeque<Notice>();

	private ReentrantReadWriteLock segLock = new ReentrantReadWriteLock();

	public boolean addNotice(Notice notice) {
		return addQueue.add(notice);
	}

	@Override
	public long getPeriod() {
		return 5000;
	}

	@Override
	public void run() {
		if(addQueue.isEmpty()){
			return;
		}
		segLock.writeLock().lock();
		try {
			List<Notice> nlist = new ArrayList<>();
			addQueue.drainTo(nlist);
			for (Notice node : nlist) {
				noticeService.insertNotice(node);
			}
		} catch (RepositoryException e) {
			logger.error("Store notice failed: ", e);
		} finally {
			segLock.writeLock().unlock();
		}
	}
}

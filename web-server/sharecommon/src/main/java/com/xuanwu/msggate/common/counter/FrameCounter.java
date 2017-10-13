/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.counter;

import java.util.concurrent.locks.ReentrantLock;

public class FrameCounter {
	private int frameCount;
	private int maxFrameCount;

	private final ReentrantLock lock = new ReentrantLock();

	public FrameCounter(int maxFrameCount) {
		this.frameCount = 0;
		this.maxFrameCount = maxFrameCount;
	}

	public void setFrameCount(int frameCount) {
		try {
			lock.lock();
			this.frameCount = frameCount;
		} finally {
			lock.unlock();
		}
	}

	public void setMaxFrameCount(int maxFrameCount) {
		try {
			lock.lock();
			this.maxFrameCount = maxFrameCount;
		} finally {
			lock.unlock();
		}
	}

	public int incrment() {
		try {
			lock.lock();

			if (frameCount == maxFrameCount)
				frameCount = 0;

			return ++frameCount;
		} finally {
			lock.unlock();
		}
	}
}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Speed counter
 * 
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-6-8
 * @Version 1.0.0
 */
public class SpeedCounter {
	private static final int MINUTE_AMOUT = 60000;
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(SpeedCounter.class);
	
	/** current amount */
	private int curVal = 0;
	
	/** cur aox index */
	private int idx = 0;

	/**
	 * Average time minute
	 */
	int avgTime = 5;

	int[] aox;

	long anchor;
	
	private int inc(int i){
		return (++i == aox.length ? 0 : i); 
	}

	/**
	 * Average minutes
	 * 
	 * @param avgTime
	 */
	public SpeedCounter(int avgTime) {
		if(avgTime <=0 ){
			throw new IllegalArgumentException("avgTime must be a positive value, but " + avgTime);
		}
		this.avgTime = avgTime;
		aox = new int[avgTime];
		for (int i = 0; i < avgTime; i++) {
			aox[i] = 0;
		}
		anchor = System.currentTimeMillis() / MINUTE_AMOUT;
	}

	/**
	 * Increment counter
	 * 
	 * @param counter
	 */
	public synchronized void incrment(int counter) {
		//logger.debug("Increment the count {} at time:{}", counter, new Date());
		syncTime();
		curVal += counter;
	}

	/**
	 * Get the average speed
	 * 
	 * @return
	 */
	public synchronized int getAverageSpeed() {
		syncTime();
		int total = 0;
		for (int i = 0; i < avgTime; i++)
			total += aox[i];
		return total / avgTime;
	}

	/**
	 * Get the average speed
	 * 
	 * @return
	 */
	public synchronized int getTotalcount() {
		syncTime();
		int total = 0;
		for (int i = 0; i < avgTime; i++)
			total += aox[i];
		return total;
	}

	private void syncTime() {
		long now = System.currentTimeMillis() / MINUTE_AMOUT;
		try {
//			aox = shifteLeftElement(aox, (int) (now - anchor));
			updateAox((int)(now - anchor));
		} catch (Exception e) {
			logger.error("Sync time failed now: " + now + ", anchor:" + anchor
					+ ", cause by:{}", e);
		}
		anchor = now;
	}

	public int getAverageTime() {
		return avgTime;
	}
	
	private void updateAox(int distance){
		if(distance <= 0)
			return;
		/** 先将越过的分钟清零 **/
		for(int i = 0; i < distance; i ++){
			idx = inc(idx);
			aox[idx] = 0;
		}
		
		/** 如果分钟数没有超过指定的时间跨度，那么将当前统计值设置到数组相应位置，否则将此统计值作废  */
		if(distance <= avgTime)
			aox[idx] = curVal;
		
		/** 将当前统计值清零 */
		curVal = 0;
	}

//	private List<Integer> shifteLeftElement(List<Integer> src, int distance) {
//		if (distance == 0)
//			return src;
//		List<Integer> target = new ArrayList<Integer>(averageTime);
//		for (int i = 0; i < averageTime; i++) {
//			target.add(new Integer(0));
//		}
//		if (distance < averageTime)
//			for (int i = distance; i < src.size(); i++)
//				target.set(i - distance, src.get(distance));
//		return target;
//	}
}

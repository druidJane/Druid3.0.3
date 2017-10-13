package com.xuanwu.msggate.common.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Average time counter
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2011-11-17
 * @Version 1.0.0
 */
public class AvgTimeCounter {
	
	private static final int MINUTE_AMOUT = 60000;
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(SpeedCounter.class);
	
	/** current amount */
	private int curVal = 0;
	
	/** current elapsed time */
	private long curTime = 0;
	
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
	public AvgTimeCounter(int avgTime) {
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
	 * Increase count and elapsedTime
	 * @param count
	 * @param elapsedTime
	 */
	public synchronized void incrment(int count, long elapsedTime) {
		syncTime();
		curVal += count;
		curTime += elapsedTime;
	}

	/**
	 * Get the average time
	 * 
	 * @return
	 */
	public synchronized int getAvgTime() {
		syncTime();
		int total = 0;
		for (int i = 0; i < avgTime; i++)
			total += aox[i];
		return total / avgTime;
	}

	private void syncTime() {
		long now = System.currentTimeMillis() / MINUTE_AMOUT;
		try {
			updateAox((int)(now - anchor));
		} catch (Exception e) {
			logger.error("Sync time failed now: " + now + ", anchor:" + anchor
					+ ", cause by:{}", e);
		}
		anchor = now;
	}
	
	private void updateAox(int distance){
		if(distance <= 0)
			return;
		
		int temp = 0;
		if(curVal > 0){
			temp = (int)(curTime / curVal);
		}
		
		/** 先将越过的分钟设置平均值 **/
		for(int i = 0; i < distance; i ++){
			idx = inc(idx);
			aox[idx] = temp;
		}
		
		/** 将当前统计值清零 */
		curVal = 0;
		curTime = 0;
	}
}

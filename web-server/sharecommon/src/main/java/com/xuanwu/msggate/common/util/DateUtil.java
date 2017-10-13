/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.msggate.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-21
 * @Version 1.0.0
 */
public class DateUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DateUtil.class);

	public static final long RAW_OFF_SET = TimeZone.getDefault().getRawOffset();

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd hh:mm:ss";

	/**
	 * Number of milliseconds in a standard second.
	 */
	public static final long MILLIS_PER_SECOND = 1000;

	/**
	 * Number of milliseconds in a standard minute.
	 */
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

	/**
	 * Number of milliseconds in a standard hour.
	 */
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

	/**
	 * Number of milliseconds in a standard day.
	 */
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	public static void main(String[] args) {
		System.out.println(DateUtil.MILLIS_PER_DAY);
	}

	public static Date tranDate(long time) {
		if (time == 0)
			return null;
		return new Date(time);
	}

	public static long parseDate(Date time) {
		return time == null ? 0 : time.getTime();
	}

	public static void sleepWithoutInterrupte(long time) {
		while (true) {
			try {
				Thread.sleep(time);
				break;
			} catch (InterruptedException e) {
				logger.error("Interrupted when sleep! this will be ignored!", e);
			}
		}
	}

	public static void sleepNanoWithoutInterrupte(long nanoseconds) {
		while (true) {
			try {
				TimeUnit.NANOSECONDS.sleep(nanoseconds);
				break;
			} catch (InterruptedException e) {
				logger.error("Interrupted when sleep! this will be ignored!", e);
			}
		}
	}

	public static String getCurrentDate() {
		SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_FORMAT);
		return sf.format(new Date());
	}

	public static int getCurMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static boolean equalsDay(Date time1, Date time2) {
		return (fixTime(time1.getTime()) / DateUtil.MILLIS_PER_DAY == fixTime(time2.getTime()) / DateUtil.MILLIS_PER_DAY);
	}

	public static boolean equalsCurDay(Date time) {
		return (fixTime(System.currentTimeMillis()) / DateUtil.MILLIS_PER_DAY == fixTime(time.getTime()) / DateUtil.MILLIS_PER_DAY);
	}

	public static long fixTime(long time) {
		return time + RAW_OFF_SET;
	}
}

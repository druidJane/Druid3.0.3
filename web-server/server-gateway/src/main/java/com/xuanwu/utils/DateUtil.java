/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}

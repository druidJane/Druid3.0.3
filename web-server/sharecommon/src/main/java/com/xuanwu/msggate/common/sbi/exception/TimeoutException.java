/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.exception;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-19
 * @Version 1.0.0
 */
public class TimeoutException extends CoreException {
	private static final long serialVersionUID = -4216311925646358630L;

	/**
	 * 
	 */
	public TimeoutException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TimeoutException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public TimeoutException(Throwable cause) {
		super(cause);
	}

}

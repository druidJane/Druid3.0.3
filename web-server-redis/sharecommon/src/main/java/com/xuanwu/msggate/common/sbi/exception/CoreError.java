/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.exception;

/**
 * Business core error
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-12
 * @Version 1.0.0
 */
public class CoreError extends RuntimeException {
	private static final long serialVersionUID = -2480150656050010942L;

	/**
	 * 
	 */
	public CoreError() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public CoreError(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public CoreError(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CoreError(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}

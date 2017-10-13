/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.exception;

/**
 * 基本Pack业务异常
 * 
 * @author <a href="mailto:wanglianguang@139130.net">Guang Wang</a>
 * @Data 2010-4-26
 * @Version 1.0.0
 */
public class CoreException extends Exception {
	private static final long serialVersionUID = 6858088859273426125L;

	public CoreException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CoreException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CoreException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CoreException(Throwable cause) {
		super(cause);
	}

}

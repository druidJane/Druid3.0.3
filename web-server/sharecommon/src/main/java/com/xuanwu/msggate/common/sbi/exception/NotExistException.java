/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.exception;


/**
 * Not exist exception
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-14
 * @Version 1.0.0
 */
public class NotExistException extends CoreException {
	private static final long serialVersionUID = 8322842269742347663L;

	/**
	 * 
	 */
	public NotExistException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}

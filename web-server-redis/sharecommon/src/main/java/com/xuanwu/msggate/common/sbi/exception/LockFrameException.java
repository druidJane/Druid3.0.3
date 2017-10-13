/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.exception;

/**
 * Fetch frame lock exception
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-12
 * @Version 1.0.0
 */
public class LockFrameException extends CoreException {
	private static final long serialVersionUID = 3251256448558240433L;

	private Long frameID;

	/**
	 * 
	 */
	public LockFrameException() {
		// TODO Auto-generated constructor stub
	}

	public LockFrameException(Long id, String message) {
		super(message);
		this.frameID = id;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LockFrameException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public LockFrameException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public LockFrameException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public Long getFrameId() {
		return frameID;
	}
}

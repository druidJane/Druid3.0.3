package com.xuanwu.mos.exception;

/**
 * 仓储自定义错误类
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-22
 * @version 1.0.0
 */
public class RepositoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public RepositoryException(Exception e) {
		super(e);
	}

	public RepositoryException(String msg) {
		super(msg);
	}

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}

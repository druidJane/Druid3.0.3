/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.net.InetSocketAddress;
import java.util.Map;


/**
 * The runtime context,Provides a way to identify a user across more than one
 * request
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-4-26
 * @Version 1.0.0
 */
public interface SessionContext {
	/**
	 * Get the client address
	 * 
	 * @return
	 */
	public InetSocketAddress getRemote();

	/**
	 * Set the client address
	 * 
	 * @param address
	 */
	public void setRemote(InetSocketAddress address);

	/**
	 * Get the account associated with the request
	 * 
	 * @return
	 */
	public Account getAccount();

	/**
	 * Set the account
	 */
	public void setAccount(Account account);

	/**
	 * Set attribute,used in the context of connection.
	 * 
	 * @param key
	 * @param value
	 */
	public void setParameter(String key, Object value);

	/**
	 * Get the attribute in the context of runtime.
	 * 
	 * @param key
	 * @return if value of the key doesn't exist,then return null.
	 */
	public Object getParameter(String key);

	/**
	 * Add temporary key-value, will be lost after request.
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value);

	/**
	 * Get temporary value.
	 * 
	 * @param key
	 * @return TODO
	 */
	public Object getAttribute(String key);

	/**
	 * Clear the request temporary state.
	 */
	public void clearRequestState();
	
	public void clearAttributes();

	/**
	 * Get all the attributes
	 * 
	 * @return
	 */
	public Map<String, Object> getAllParameters();
}

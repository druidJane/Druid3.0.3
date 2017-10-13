/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache.engine;

/**
 * Phone cache
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-25
 * @Version 1.0.0
 */
public interface PhoneCache {
	/**
	 * Get the size of the cache
	 * 
	 * @return
	 */
	public abstract int getSize();

	/**
	 * Put phone para to the cache
	 * 
	 * @param phone
	 * @param type
	 * @param target
	 */
	public abstract void putPhonePara(String phone, int type, int target);

	/**
	 * Put phone para to the cache
	 * 
	 * @param phone
	 * @param type
	 * @param target
	 */
	public abstract void putPhonePara(long phone, int type, int target);

	/**
	 * Put phone para to the cache
	 * 
	 * @param zipMes
	 */
	public abstract void putPhonePara(long zipMes);

	/**
	 * Remove para from the cache
	 * 
	 * @param phone
	 * @param type
	 * @param target
	 */
	public abstract void removePhonePara(String phone, int type, int target);

	/**
	 * Remove para from the cache
	 * 
	 * @param phone
	 * @param type
	 * @param target
	 */
	public abstract void removePhonePara(long phone, int type, int target);

	/**
	 * Remove para from the cache
	 * 
	 * @param zipMes
	 *            ziped phone list
	 */
	public abstract void removePhonePara(long zipMes);

	/**
	 * If contain phone para exist in the cache or not.
	 * 
	 * @param phone
	 * @param type
	 * @param target
	 * @return
	 */
	public abstract boolean containPhonePara(String phone, int type, int target);

	/**
	 * If contain phone para exist in the cache or not.
	 * 
	 * @param phone
	 * @param type
	 * @param target
	 * @return
	 */
	public abstract boolean containPhonePara(long phone, int type, int target);

	/**
	 * Clear the cache
	 */
	public abstract void clear();
	
	/**
	 * Get phone zipmes
	 * @param phone
	 * @param type
	 * @param target
	 * @return
	 */
	public abstract long tran2Code(String phone, int type, int target);
}

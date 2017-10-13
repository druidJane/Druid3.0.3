package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.cache.BizHandleRepos.ListType;

import java.util.Date;

public class CachePhone {
	private Long id;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the phone
	 */
	public Long getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(Long phone) {
		this.phone = phone;
	}

	/**
	 * @return the type
	 */
	public ListType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ListType type) {
		this.type = type;
	}
	
	public void setTypeIndex(int index) {
		this.type = ListType.getType(index);
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}

	/**
	 * @return the handle_time
	 */
	public Date getHandleTime() {
		return handleTime;
	}

	/**
	 * @param handle_time the handle_time to set
	 */
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	/**
	 * @return the zipmes
	 */
	public Long getZipmes() {
		return zipmes;
	}

	/**
	 * @param zipmes the zipmes to set
	 */
	public void setZipmes(Long zipmes) {
		this.zipmes = zipmes;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the removed
	 */
	public Boolean getRemoved() {
		return removed;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	/**
	 * @return the removeId
	 */
	public Long getRemoveId() {
		return removeId;
	}

	/**
	 * @param removeId the removeId to set
	 */
	public void setRemoveId(Long removeId) {
		this.removeId = removeId;
	}

	private Long phone;
	
	private ListType type;
	
	private int target;
	
	private Date handleTime;
	
	private Long zipmes;
	
	private String user;
	
	private Boolean removed;
	
	private Long removeId;
}

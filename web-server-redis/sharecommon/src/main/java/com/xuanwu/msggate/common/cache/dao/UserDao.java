package com.xuanwu.msggate.common.cache.dao;

import com.xuanwu.msggate.common.sbi.exception.NotExistException;

public interface UserDao {
	/**
	 * 用户是否存在
	 * @param name
	 * @param password
	 * @return
	 * @throws NotExistException
	 */
	public Boolean existsUser(String name, String password) throws NotExistException;
}

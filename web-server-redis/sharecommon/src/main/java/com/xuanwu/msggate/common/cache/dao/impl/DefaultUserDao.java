package com.xuanwu.msggate.common.cache.dao.impl;

import com.xuanwu.msggate.common.cache.dao.UserDao;
import com.xuanwu.msggate.common.cache.dao.mapper.UserMapper;
import com.xuanwu.msggate.common.sbi.exception.NotExistException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class DefaultUserDao implements UserDao {

    /**
     * Cache session factory
     */
    private SqlSessionFactory sessionFactory;

    /**
     * Set sessionFactory
     * 
     * @param sessionFactory
     *            the sessionFactory to set
     */
    public void setSessionFactory(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	/**
	 * 用户是否存在
	 * @param name
	 * @param password
	 * @return
	 * @throws NotExistException
	 */
	@Override	
    public Boolean existsUser(String name, String password) throws NotExistException {
        
        SqlSession session = sessionFactory.openSession();
        try {

            int result = session.getMapper(UserMapper.class)
                    .existsUser(name, password);
            if (result == 0) {
                throw new NotExistException(
                        "User doesn't exist or password is error!");
            }
            
            return result > 0;
        } finally {
            session.close();
        }
    }

}

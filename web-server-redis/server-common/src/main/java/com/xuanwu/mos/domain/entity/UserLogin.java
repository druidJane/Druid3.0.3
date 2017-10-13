/*   
* Copyright (c) 2016/8/22 by XuanWu Wireless Technology Co., Ltd 
*             All rights reserved  
*/
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.io.Serializable;

/**
 * 用户登录类型
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-10-22
 * @version 1.0.0
 */
public class UserLogin extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String bindAccount;// 绑定账号(手机号/邮箱/用户名-user_name/用户API调用安全标识符-user_sid)
	private String authToken;// 认证令牌,用户API调用认证
	private String password;
	private String secondPassword;
	private String phone;

	public UserLogin() {
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getBindAccount() {
		return bindAccount;
	}

	public void setBind_account(String bindAccount) {
		this.bindAccount = bindAccount;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecondPassword() {
		return secondPassword;
	}

	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}


	@Override
	public Serializable getId() {
		return null;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}

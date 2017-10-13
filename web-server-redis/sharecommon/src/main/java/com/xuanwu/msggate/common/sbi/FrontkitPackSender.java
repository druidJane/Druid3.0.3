/*   
* Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
*             All rights reserved                         
*/
package com.xuanwu.msggate.common.sbi;

import java.util.Date;
import java.util.Map;

import com.xuanwu.msggate.common.sbi.entity.MTResp;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

/**
 * @Description:
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-10-11
 * @Version 1.0.0
 */
public interface FrontkitPackSender {
	/**
	 * 
	 * @param userName 用户帐号
	 * @param password 用户密码
	 * @param msgPack 用户提交的信息包
	 * @return BizResponse 提交相应
	 * @throws Exception
	 */
	public MTResp send(String userName, String password, MsgPack msgPack) throws CoreException;
	
	/**
	 * 审核批次
	 * @param userName 审核用户帐号
	 * @param password 审核用户密码
	 * @param packId 需要审核的批次ID
	 * @param state 审核状态
	 * @return
	 * @throws CoreException
	 */
	public MTResp auditing(String userName, String password, String packId, int state,int msgType,Date postTime) throws CoreException;
	
	/**
	 * 取消批次
	 * @param userName 
	 * @param password
	 * @param packId
	 * @param state
	 * @return
	 * @throws CoreException
	 */
	public MTResp cancel(String userName, String password, String packId, int state,int msgType,Date postTime) throws CoreException;
	
	/**
	 * 业务类型端口绑定变更,端口切换，通道切换处理
	 * @param userName
	 * @param password
	 * @param params
	 * @return
	 * @throws CoreException
	 */
	public MTResp change(String userName, String password, Map<String, Object> params) throws CoreException; 
}

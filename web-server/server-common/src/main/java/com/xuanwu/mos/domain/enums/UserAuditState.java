/*   
* Copyright (c) 2016/9/13 by XuanWu Wireless Technology Co., Ltd 
*             All rights reserved  
*/
package com.xuanwu.mos.domain.enums;

/**
 * 审核状态
 * 
 * @author <a href="mailto:jiangpeng@wxchina.com">Peng.Jiang</a>
 * @version 1.0.0
 * @date 2016/9/13
 */
public enum UserAuditState implements HasIndexValue {
	// 0:待审核,1:通过审核,2:未通过审核,3:取消审核
	WAIT_VERIFY(0), VERIFY_SUCCESS(1), VERIFY_FAIL(2), CANCEL_VERIFY(3);

	private final int index;

	private UserAuditState(int index) {
		this.index = index;
	}

	public static UserAuditState getState(int index) {
		for (UserAuditState state : UserAuditState.values()) {
			if (state.getIndex() == index) {
				return state;
			}
		}
		return null;
	}

	@Override
	public int getIndex() {
		return index;
	}

}

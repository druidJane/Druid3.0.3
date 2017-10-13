package com.xuanwu.mos.domain.enums;


/**
 * Created by zengzl
 */
public enum NoticeScope implements HasIndexValue {
    /**不需要权限，系统公告,针对企业所有用户*/
	NONE(0),
	/**仅个人,如导入导出任务消息*/
	PERSONAL(1),
	/**该企业拥有该权限的所有用户,如短信审核*/
	PERMISSION(2);

	private int index;

	private NoticeScope(int index) {
		this.index = index;
	}

	public static NoticeScope getState(int index) {
		for (NoticeScope state : NoticeScope.values()) {
			if (state.index == index) {
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

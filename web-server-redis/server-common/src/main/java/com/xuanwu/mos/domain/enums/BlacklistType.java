package com.xuanwu.mos.domain.enums;

/**
 * 黑名单类型
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
public enum BlacklistType implements HasIndexValue {

	// 0:用户黑名单,1:白名单,2:企业黑名单,3:全局黑名单,4:通道黑名单,5:业务能力黑名单,6:企业后台黑名单
	Illegal(-1), User(0), Enterprise(2), Global(3), Channel(4), BizType(5), BackendEnterprise(6);

	private Integer index;

	private BlacklistType(int index) {
		this.index = index;
	}

	public static BlacklistType getType(Integer index) {
		switch (index) {
		case 0:
			return User;
		case 2:
			return Enterprise;
		case 3:
			return Global;
		case 4:
			return Channel;
		case 5:
			return BizType;
		case 6:
			return BackendEnterprise;
		default:
			return Illegal;
		}
	}

	@Override
	public int getIndex() {
		return index;
	}

	public static BlacklistType getType(String typeName) {

		if (StringUtils.isEmpty(typeName)) {
			return Illegal;
		}

		String tempTypeName = typeName.trim();
		if ("企业黑名单".equals(tempTypeName)) {
			return BackendEnterprise;
		} else if ("全局黑名单".equals(tempTypeName)) {
			return Global;
		} else {
			return Illegal;
		}
	}

	public static String getTypeName(int type) {
		switch (type) {
		case 2:
			return "企业";
		case 0:
			return "用户";
		default:
			return "业务类型";
		}
	}
}

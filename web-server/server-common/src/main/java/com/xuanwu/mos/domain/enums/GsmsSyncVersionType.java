package com.xuanwu.mos.domain.enums;


/**
 * 系统配置版本类型
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-25
 * @version 1.0.0
 */
public enum GsmsSyncVersionType implements HasIndexValue {

	/** 对应于通道端口等版本号 */
	SPEC_SVS(1),
	/** 对应于号码段版本号 */
	PHONE_SEG(2),
	/** 对应于系统配置版本号 */
	SYS_CONFIG(3),
	/** 对应于优先级版本号 */
	PRIORITY(4),
	/** 对应于用户版本号 */
	USER(5),
	/** 对应于角色权限绑定关系版本号 */
	ROLE_PERM(6);

	private final int index;

	private GsmsSyncVersionType(int index) {
		this.index = index;
	}

	public static GsmsSyncVersionType getType(int index) {
		for (GsmsSyncVersionType type : GsmsSyncVersionType.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		throw new RuntimeException("Unsupport gsms sync version type");
	}

	@Override
	public int getIndex() {
		return index;
	}
}

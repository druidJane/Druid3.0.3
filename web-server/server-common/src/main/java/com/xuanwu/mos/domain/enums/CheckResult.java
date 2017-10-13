package com.xuanwu.mos.domain.enums;

/**
 *
 * Created by 林泽强 on 2016/8/29. 文件导入数据校验结果
 */
public enum CheckResult {
	/**
	 * 合法
	 */
	Legal,
	/**
	 * 手机号码不合法
	 */
	IllegalPhone,
	/**
	 * 分属平台不合法
	 */
	IllegalPlatform,
	/**
	 * email不合法
	 */
	IllegalEmail,
	/**
	 * 【VIP】导入内容系统无法正确识别
	 */
	IllegalVIP,

	/**
	 * 用户扩展码不合法
	 */
	IllegalUserExtNum,
	/**
	 * 部门编码不合法
	 */
	IllegalDeptNo,
	/**
	 * 部门编码不在权限范围内
	 */
	IllegalDeptNoNotInDataScope,
	/**
	 * 角色不合法
	 */
	IllegalRole,
	/**
	 * 用户名称不合法
	 */
	IllegalLinkMan,
	/**
	 * 用户名称不能超过50个字符
	 */
	LinkManTooLong,
	/**
	 * 密码不合法
	 */
	IllegalPassword,
	/**
	 * 用户账号格式错误
	 */
	IllegalUserName,
	/**
	 * 用户账号超出了字符长度限制
	 */
	UserNameTooLong,
	/**
	 * 该用户的权限范围内部门编号不存在
	 */
	DeptNotExsit,
	/**
	 * 已存在相同用户账号
	 */
	UserNameRepate,
	/**
	 * 用户扩展码重复
	 */
	UserExtNumRepate,
	/**
	 * 性别不合法或为空
	 */
	IllegalSex,
	/**
	 * VIP属性不合法或为空
	 */
	IllegalVip,
	/**
	 * 角色不在部门权限范围
	 */
	IllegalUserRoleNames,
	/**
	 * 业务能力不在部门权限范围
	 */
	IllegalUserBizNames,
	/**
	 * 出生日期格式错误/出生日期应小于等于当前系统日期
	 */
	IllegalBirthday,
	/**
	 * 通讯录姓名为空
	 */
	ContactNameEmpty,
	/**
	 * 通讯录手机号码重复
	 */
	ContactPhoneExists,
	/**
	 * 非法通讯录群组名称
	 */
	ContactIllegalGroupName,
	/**
	 * 非法通讯录联系人名称
	 */
	ContactIllegalName,
	/**
	 * 非法通讯录联系人编号
	 */
	ContactIllegalIdentifier,
	/**
	 * 非法通讯录联系人备注
	 */
	ContactIllegalRemark,

	/**
	 * 非法关键字不合法
	 */
	IllegalKeyword,
	/**
	 * 非法关键字已经存在
	 */
	KeywordExists,
	/**
	 * 非法关键字重复
	 */
	KeyWordRepate,
	/**
	 * 非法关键字长度太长
	 */
	KeywordTooLong,
	/**
	 * 非法关键字所属通道不合法
	 */
	IllegalKeywordTarget,
	/**
	 * 非法关键字类型不合法
	 */
	IllegalKeywordType,

	/**
	 * 手机号码为空
	 */
	BlankPhone,
	/**
	 * 手机号码为空
	 */
	WhitePhone,
	/**
	 * 备注信息太长
	 */
	BlacklistRemarkTooLong,
	/**
	 * 状态列信息不合法
	 */
	IllegalBlacklistAddOrDel,
	/**
	 * 黑名单类型不合法
	 */
	IllegalBlacklistType,
	/**
	 * 所属对象不合法
	 */
	IllegalBlacklistTarget,

	/**
	 * 所属企业不存在
	 */
	BlacklistEntNExists,
	/**
	 * 所属业务类型不存在
	 */
	BlacklistBizNExists,
	/**
	 * 所属用户不存在
	 */
	BlacklistUserNExists,
	/**
	 * 黑名单已经存在
	 */
	BlacklistExists,
	/**
	 * 黑名单重复
	 */
	BlacklistRepate,
	/**
	 * 没有符合该规则的黑名单号
	 */
	NoBlacklistPhone,

	/**
	 * 备注信息太长
	 */
	WhitelistRemarkTooLong,
	/**
	 * 白名单通道不存在
	 */
	ChannelNotExsit,
	/**
	 * 白名单已经存在
	 */
	WhitelistExists,

	/**
	 * 查询指令不能为空
	 */
	SelfNameEmpty,
	/**
	 * 查询指令长度不能超过20个字符
	 */
	SelfNameTooLong,
	/**
	 * 查询指令已存在
	 */
	SelfNameExsit,
	/**
	 * 查询指令不合法
	 */
	SelfNameIllegal,
	/**
	 * 信息主题不能超过100个字符
	 */
	SelfSubjectTooLong,
	/**
	 * 信息内容不能为空
	 */
	SelfAutoRepliesEmpty,
	/**
	 * 信息内容不能超过1000个字符
	 */
	SelfAutoRepliesTooLong,
	/**
	 * 信息收发人非有效账号
	 */
	SelfHandleUserNotExsit,

	/**
	 * 企业账号不能为空
	 */
	UserAuditAccountEmpty,
	/**
	 * 企业账号错误,企业账号必须为已生效的且未被删除的企业账号
	 */
	UserAuditAccountErr,
	/**
	 * 短信审核基数格式错误,非空时必须是大于0小于等于100,000的整数
	 */
	UserAuditAuditingNumErr,

	/**
	 * 400号码为空
	 */
	NumberBlank,
	/**
	 * 400号码格式不正确
	 */
	NumberInvalid,
	/**
	 * 400号码重复
	 */
	NumberRepeated,
	/**
	 * 400号码非当前通道
	 */
	NumberNotIn,
	/**
	 * 400号码长度超过通道可扩展长度
	 */
	NumberOutOfExtendSize,

	/**
	 * 区域号段为空
	 */
	RegionSegmtRegionNotExist, RegionSegmtPhoneBlank,
	/**
	 * 区域号段必须是数字。
	 */
	RegionSegmtPhoneNumIllegal,
	/**
	 * 区域号段以0开头，必须为3位或4位。
	 */
	RegionSegmtPhoneZeroIllegal,
	/**
	 * 区域号段以1开头，必须为7位。
	 */
	RegionSegmtPhoneOneIllegal,
	/**
	 * 运营商不存在。
	 */
	RegionSegmtCarrierNotExist,
	/**
	 * 省不存在。
	 */
	RegionSegmtPhoneZeroOrOneIllegal,

	/**
	 * 请以库中存在的运营商号段开头。
	 */
	RegionSegmtPhoneANDCarrierNotMatch,
	/**
	 * 已存在该号码，请重新输入。
	 */
	RegionSegmtRegionPhoneRepeat,
	/**
	 * 发送限制发送数目不为数字 非空时必须是大于0小于等于100,000的整数
	 **/
	SendLimitSendNumNotDigit,
	/**
	 * 发送限制发送时间间隔不为数字
	 **/
	SendLimitSendTimeIntervalNotDigit, SendLimitSendNumThanMaxNum, SendLimitSendTimeIntervalThanMaxNum,
	/*企业账户余额不足,无法充值。*/
	ChargeAccountBalanceNotEnough,
	/*计费账户名称不存在*/
	NotFoundChildAccountName,
	/*找不到企业总账户信息，无法完成充值。*/
	NotFoundParentChargeAccount,
	/* 仅允许输入小于等于【剩余信用额度】且不能为0，精确到小数点后四位 */
	ChargeMoneyIllegal,
	/* 充值金额必须为数值 */
	ChargeMoneyFormatException,
	/* 该计费账户对应的充值方式不是手动充值 */
	ChargeWayNotHandledCharge,

	/**
	 * 用户管理导入
	 */
	/* 密码长度限制为8~16位数字、大写或小写字母组合 */
	PasswordRuleLimit,
	/* 手机号格式错误/手机号不存在运营商号段 */
	PhoneRuleLimit,
	/* 业务类型不能为空 */
	BizTypeNotEmpty,
	/* 该部门编号下不包含该业务类型 */
	DeptExcludeBizType,
	/* 用户扩展码仅允许输入数字字符 */
	UserIndentityBlueLimit,
	/* 用户扩展码已存在 */
	UserIdentityRepeat,
	/* 用户签名不能超过30个字符 */
	UserSignatureTooLong,
	/* 签名位置不合法 */
	IllegalUserSigLocation,
	/* 备注信息不能超过200个字符 */
	RemarkTooLong,
	/* 账号类型为必填/错误 */
	UserAccountTypeIllegal,
	/* 协议类型错误 */
	UserProtocolTypeIllegal,
	/* 源端口格式错误 */
	SrcPortIllegal,
	/* IP地址格式错误/端口号非法 */
	CallbackAddressIllegal,
	/* 报备签名不合法 */
	CustomerSignatureIllegal,
	/* 发送速度导入值非法 */
	SendSpeedIllegal,
	/* 链接数导入值非法 */
	linkNumIllegal,
	/* 部门名称与部门编号没有对应关系 */
	DeptNameIllegal,
	/* mos后台该企业账号的【是否透传】为否，所以不能导入【账号类型】为【透传】的用户 */
	DransparentSendIllegal,
	/* 上行推送地不能超过100个字符 */
	PushAddressTooLong,
	/* 状态报告推送地址不能超过100个字符 */
	ReportPushAddressTooLong,
	/**
	 * 系统异常
	 */
	SYSTEM_ERROR
}

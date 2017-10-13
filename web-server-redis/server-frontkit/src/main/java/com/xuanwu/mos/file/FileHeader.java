package com.xuanwu.mos.file;

/**
 * 文件头列表
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-09-08
 * @version 1.0.0
 */
public class FileHeader {

	public static final String[] USER = new String[] { "登录账号", "用户名称", "性别", "出生日期", "联系方式", "所属部门编号", "拥有角色",
			"备注", "密钥" };

	public static final String[] USER_BACKEND = new String[] { "登录账号", "用户名称", "性别", "出生日期", "联系方式", "所属部门编号", "拥有角色",
			"业务能力", "备注" };

	public static final String[] USER_NOT_BIZTYPE = new String[] { "登录账号", "用户名称", "性别", "出生日期", "联系电话","邮箱", "分属平台","所属部门编号",
			"拥有角色", "备注" };

	public static final String[] USER_NOT_BIZTYPE_BACKEND = new String[] { "登录账号", "用户名称", "性别", "出生日期", "联系电话","邮箱","分属平台",
			"所属部门编号", "拥有角色", "备注" };

	public static final String[] USER_MGR_FRONTKIT = new String[] { "账号类型","用户账号","用户名称","发送密码","透传密码",
			"登录密码","手机号码","部门编号","部门名称","用户扩展码","用户签名","签名位置","业务类型编号","协议类型","源端口",
			"回调地址","报备签名","发送速度","链接数","拥有角色","上行推送","上行推送地址","状态报告推送","状态报告推送地址","描述" };

	public static final String[] USER_MGR_FRONTKIT_EXPORT = new String[] { "账号类型","用户账号","用户名称","手机号码",
			"部门编号","部门名称","用户扩展码","用户签名","协议类型","源端口","回调地址","报备签名","发送速度","链接数","上行推送","上行推送地址","状态报告推送","状态报告推送地址","描述" };

	public static final String[] CONTACT = new String[] { "姓名","手机号码","所属组" , "性别", "出生日期", "编号", "Vip", "备注" };
	//用于联系人导入
	public static final String[] CONTACT_IMPORT = new String[] { "手机号码","姓名" , "性别", "Vip", "出生日期", "编号", "备注" };
	//用于关键字导出
	public static final String[] KEYWORD = new String[] { "非法关键字","最后修改时间"};
	//关键字导入
	public static final String[] KEYWORD_IMPORT = new String[] { "非法关键字"};

	public static final String[] BLACKLIST = new String[] { "手机号码", "黑名单类型", "所属对象","备注" };
	public static final String[] WHITEPHONE = new String[] { "手机号码"};

	public static final String[] WHITELIST = new String[] { "手机号码", "通道名" };

	public static final String[] NONWHITELIST = new String[] { "手机号码", "通道名" };

	public static final String[] NONWHITELIST_ADC = new String[] { "集团编码", "集团名称", "集团产品号码", "业务编码", "业务名称", "手机号码",
			"操作类型", "生效时间", "失效时间" };

	public static final String[] MOTICKET = new String[] { "姓名","手机号码", "接收内容", "接收用户", "接收部门", "接收端口", "接收时间", "回复状态" };

	public static final String[] MOTICKET_BACKEND = new String[] { "手机号码", "接收内容", "接收企业账号", "接收企业名称", "接收用户", "接收时间",
			"接收号码", "是否完整" };

	public static final String[] SMS_BATCH = new String[] { "批次名称", "提交时间", "完成时间", "发送用户", "批次状态", "用户提交号码数", "过滤后号码数",
			"已发送号码数" };

	public static final String[] SMS_BATCH_BACKEND = new String[] { "批次名称", "提交时间", "完成时间", "企业账号", "发送账号", "批次状态",
			"用户提交号码数", "过滤后号码数", "已发送号码数" };

	public static final String[] SMS_NUMBER = new String[] { "手机号码", "发送内容", "发送用户", "发送时间", "发送通道", "发送结果", "提交报告",
			"状态报告", "批次名称" };

	public static final String[] SMS_NUMBER_BACKEND = new String[] { "手机号码", "发送内容", "企业账号", "发送账号", "发送时间", "发送通道",
			"发送结果", "提交报告", "状态报告", "批次名称" };

	public static final String[] MMS_BATCH = new String[] { "批次名称", "提交时间", "完成时间", "发送用户", "批次状态", "用户提交号码数", "过滤后号码数",
			"已发送号码数" };

	public static final String[] MMS_BATCH_BACKEND = new String[] { "批次名称", "提交时间", "完成时间", "企业账号", "发送用户", "批次状态",
			"用户提交号码数", "过滤后号码数", "已发送号码数" };

	public static final String[] MMS_NUMBER = new String[] { "手机号码", "彩信标题", "发送用户", "发送时间", "发送通道", "发送结果", "提交报告",
			"状态报告", "批次名称" };

	public static final String[] MMS_NUMBER_BACKEND = new String[] { "手机号码", "彩信标题", "企业账号", "发送用户", "发送时间", "发送通道",
			"发送结果", "提交报告", "状态报告", "批次名称" };

	public static final String[] SUM_USER_REALTIME = new String[] { "用户账号", "用户名称", "所属部门", "总提交量", "总发送量", "移动发送量",
			"联通发送量", "电信C网发送量", "电信小灵通发送量" };

	public static final String[] SUM_CHANNEL_HISTORY = new String[] { "通道号", "通道名", "所属运营商", "总发送量", "总成功量", "总失败量",
			"运营商处理中", "成功率", "是否支持状态报告" };

	public static final String[] SUM_CHANNEL_TREND = new String[] { "通道号", "通道名", "日期", "所属运营商", "总发送量", "总成功量", "总失败量",
			"运营商处理中", "成功率", "是否支持状态报告" };

	public static final String[] SUM_ENTERPRISE_REALTIME = new String[] { "企业名称", "企业账号", "所属销售", "所属部门", "计费方式",
			"销售方式", "所属部门", "地区", "总提交量", "移动提交量", "联通提交量", "电信小灵通提交量", "电信C网提交量", "总接收量" };
	public static final String[] SUM_ENTERPRISE_HISTORY_BACKEND = new String[] { "企业名称", "企业账号", "所属销售", "所属部门", "计费方式",
			"销售方式", "地区", "总提交量", "移动提交量", "联通提交量", "电信小灵通提交量", "电信C网提交量", "总接收量", "移动接收量", "联通接收量", "电信小灵通接收量",
			"电信C网接收量", "总发送量", "移动发送量", "联通发送量", "电信小灵通发送量", "电信C网发送量", "总成功量", "移动成功量", "联通成功量", "电信小灵通成功量", "电信C网成功量",
			"状态报告成功量", "状态报告移动成功量", "状态报告联通成功量", "状态报告电信小灵通成功量", "状态报告电信C网成功量", "移动单价", "联通单价", "电信小灵通单价", "电信C网单价" };

	public static final String[] SUM_ENTERPRISE_HISTORY_BACKEND_WITHOUTREPORTSHOW = new String[] { "企业名称", "企业账号",
			"所属销售", "所属部门", "计费方式", "销售方式", "地区", "总提交量", "移动提交量", "联通提交量", "电信小灵通提交量", "电信C网提交量", "总接收量", "移动接收量",
			"联通接收量", "电信小灵通接收量", "电信C网接收量", "总发送量", "移动发送量", "联通发送量", "电信小灵通发送量", "电信C网发送量", "总成功量", "移动成功量", "联通成功量",
			"电信小灵通成功量", "电信C网成功量", "移动单价", "联通单价", "电信小灵通单价", "电信C网单价" };

	public static final String[] SUM_ENTERPRISE_TREND_BACKEND = new String[] { "日期", "企业名称", "企业账号", "所属销售", "计费方式",
			"销售方式", "总提交量", "移动提交量", "联通提交量", "电信小灵通提交量", "电信C网提交量", "总接收量", "移动接收量", "联通接收量", "电信小灵通接收量", "电信C网接收量",
			"总发送量", "移动发送量", "联通发送量", "电信小灵通发送量", "电信C网发送量", "总成功量", "移动成功量", "联通成功量", "电信小灵通成功量", "电信C网成功量", "状态报告成功量",
			"状态报告移动成功量", "状态报告联通成功量", "状态报告电信小灵通成功量", "状态报告电信C网成功量", "移动单价", "联通单价", "电信小灵通单价", "电信C网单价" };

	public static final String[] SUM_ENTERPRISE_TREND_BACKEND_WITHOUTREPORTSHOW = new String[] { "日期", "企业名称", "企业账号",
			"所属销售", "计费方式", "销售方式", "总提交量", "移动提交量", "联通提交量", "电信小灵通提交量", "电信C网提交量", "总接收量", "移动接收量", "联通接收量",
			"电信小灵通接收量", "电信C网接收量", "总发送量", "移动发送量", "联通发送量", "电信小灵通发送量", "电信C网发送量", "总成功量", "移动成功量", "联通成功量",
			"电信小灵通成功量", "电信C网成功量", "移动单价", "联通单价", "电信小灵通单价", "电信C网单价" };

	public static final String[] SUM_NUMBER_REALTIME_BACKEND = new String[] { "400号码", "总提交量", "移动提交量", "联通提交量",
			"电信小灵通提交量", "电信C网提交量", "总接收量" };
	public static final String[] SUM_NUMBER_HISTORY_BACKEND = new String[] { "400号码", "总提交量", "移动提交量", "联通提交量",
			"电信小灵通提交量", "电信C网提交量", "总接收量", "移动接收量", "联通接收量", "电信小灵通接收量", "电信C网接收量", "总发送量", "移动发送量", "联通发送量",
			"电信小灵通发送量", "电信C网发送量", "总成功量", "移动成功量", "联通成功量", "电信小灵通成功量", "电信C网成功量", "状态报告成功量", "状态报告移动成功量", "状态报告联通成功量",
			"状态报告电信小灵通成功量", "状态报告电信C网成功量" };
	public static final String[] SUM_NUMBER_TREND_BACKEND = new String[] { "日期", "400号码", "总提交量", "移动提交量", "联通提交量",
			"电信小灵通提交量", "电信C网提交量", "总接收量", "移动接收量", "联通接收量", "电信小灵通接收量", "电信C网接收量", "总发送量", "移动发送量", "联通发送量",
			"电信小灵通发送量", "电信C网发送量", "总成功量", "移动成功量", "联通成功量", "电信小灵通成功量", "电信C网成功量", "状态报告成功量", "状态报告移动成功量", "状态报告联通成功量",
			"状态报告电信小灵通成功量", "状态报告电信C网成功量" };

	public static final String[] SUM_USER_HISTORY = new String[] { "用户账号", "用户名称", "所属部门", "总提交量", "总发送量", "总发送成功量",
			"移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };
	public static final String[] SUM_USER_HISTORY_REPORT = new String[] { "用户账号", "用户名称", "所属部门", "总提交量", "总发送量",
			"总发送成功量", "总成功送达量", "移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };

	public static final String[] SUM_DEPT_HISTROY = new String[] { "部门名称", "总提交量", "总发送量", "总发送成功量", "移动发送量", "联通发送量",
			"电信CDMA发送量", "电信小灵通发送量" };
	public static final String[] SUM_DEPT_HISTROY_REPORT = new String[] { "部门名称", "总提交量", "总发送量", "总发送成功量", "总成功送达量",
			"移动发送量", "联通发送量", "电信CDMA发送量", "电信小灵通发送量" };

	public static final String[] SUM_DEPT_REALTIME = new String[] { "部门名称", "总提交量", "总发送量", "移动发送量", "联通发送量",
			"电信CDMA发送量", "电信小灵通发送量" };

	public static final String[] SUM_NUMBER_REALTIME_FRONTKIT = new String[] { "400号码", "总提交量", "总发送量", "移动发送量",
			"联通发送量", "电信C网发送量", "电信小灵通发送量" };
	public static final String[] SUM_NUMBER_HISTORY_FRONTKIT = new String[] { "400号码", "总提交量", "总发送量", "总发送成功量",
			"移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };

	public static final String[] SUM_NUMBER_HISTORY_STATEREPORT_FRONTKIT = new String[] { "400号码", "总提交量", "总发送量",
			"总发送成功量", "总成功送达量", "移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };

	public static final String[] SUM_NUMBER_HISTORY_AGENT_FRONTKIT = new String[] { "400号码", "企业账号", "企业名称", "总发送量" };

	public static final String[] SUM_NUMBER_TREND_FRONTKIT = new String[] { "日期", "400号码", "总提交量", "总发送量", "总发送成功量",
			"移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };
	public static final String[] SUM_NUMBER_TREND_STATEREPORT_FRONTKIT = new String[] { "日期", "400号码", "总提交量", "总发送量",
			"总发送成功量", "总成功送达量", "移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };
	public static final String[] SUM_NUMBER_TREND_AGENT_FRONTKIT = new String[] { "日期", "400号码", "企业账号", "企业名称",
			"总发送量" };

	public static final String[] SUM_ENTERPRISE_HISTORY_FRONTKIT = new String[] { "企业名称", "企业账号", "总发送量" };

	public static final String[] SUM_ENTERPRISE_TREND_FRONTKIT = new String[] { "企业名称", "企业账号", "日期", "总发送交量" };

	public static final String[] SUM_BIZTYPE_REALTIME = new String[] { "业务能力", "总接收量（条）", "总提交量（条）", "移动提交量（条）",
			"联通提交量（条）", "电信CDMA提交量（条）", "电信小灵通提交量（条）" };

	public static final String[] SUM_BIZTYPE_HISTORY = new String[] { "业务能力", "总接收量（条）", "总提交量（条）", "总成功量（条）",
			"移动成功量（条）", "联通成功量（条）", "电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] TREND_DEPT_HISTORY_REPORT = new String[] { "部门名称", "日期", "总提交量", "总发送量", "总发送成功量",
			"总成功送达量", "移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };
	public static final String[] TREND_DEPT_HISTORY = new String[] { "部门名称", "日期", "总提交量", "总发送量", "总发送成功量", "移动发送量",
			"联通发送量", "电信C网发送量", "电信小灵通发送量" };

	public static final String[] TREND_USER_HISTORY_REPORT = new String[] { "用户名称", "所属部门", "日期", "总提交量", "总发送量",
			"总发送成功量", "总成功送达量", "移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };
	public static final String[] TREND_USER_HISTORY = new String[] { "用户名称", "所属部门", "日期", "总提交量", "总发送量", "总发送成功量",
			"移动发送量", "联通发送量", "电信C网发送量", "电信小灵通发送量" };

	public static final String[] TREND_CHANNEL_HISTORY = new String[] { "通道号", "通道名", "日期", "所属运营商", "总提交量（条）",
			"总成功量（条）", "总失败量（条）", "成功率", "是否支持状态报告" };

	public static final String[] TREND_BIZTYPE_HISTORY = new String[] { "业务能力", "日期", "总接收量（条）", "总提交量（条）", "总成功量（条）",
			"移动成功量（条）", "联通成功量（条）", "电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] SELFIMPORT = new String[] { "查询指令", "信息主题", "信息内容", "信息收发人" };

	public static final String[] SENDLIMITIMPORT = new String[] { "企业账号", "短信审核基数" };

	public static final String[] SELFEXPORT = new String[] { "查询指令", "信息主题", "上行数量" };

	public static final String[] SELFMO = new String[] { "上行时间", "手机号码", "上行内容" };

	public static final String[] VOTE_CONTACT = new String[] { "手机号码", "姓名", "投票选项", "内容", "接收时间" };

	public static final String[] NUMBER = new String[] { "400号码" };

	public static final String[] WECHAT_REPORT_FANSNUM_HISTORY = new String[] { "所属公众账号", "用户数", "日期" };

	public static final String[] WECHAT_REPORT_MSGRECE_HISTORY = new String[] { "所属公众账号", "消息数", "日期" };

	public static final String[] WECHAT_REPORT_MSGRESP_HISTORY = new String[] { "所属公众账号", "消息数", "日期" };

	public static final String[] WECHAT_FANS = new String[] { "姓名", "手机号码", "所属公众账号", "所属组", "性别", "生日", "编号", "备注" };

	public static final String[] VOTE_STATISTICS = new String[] { "排名", "选项指令", "选项内容", "票数", "比例" };

	public static final String[] WLOTTERY_DATA = new String[] { "手机号码", "姓名" };

	public static final String[] REGIONSEGMT = new String[] { "区域号码段", "运营商", "省" };

	public static final String[] CHARGE_ACCOUNT = new String[] { "账户名称", "充值金额" };

	public static final String[] CHARGE_RECORD_EXPORT = new String[] {"账户名称","充值金额","充值方式","充值人员","状态","充值时间","描述"};
	
	public static final String[] DEPT_SEND_STAT = new String[] { "日期","部门", "总提交量", "总发送量", "总成功量","移动成功量", "联通成功量",
		"电信CDMA成功量", "电信小灵通成功量" };
	public static final String[] USERS_SEND_STAT = new String[] { "用户名","部门", "总提交量", "总发送量", "总成功量","移动成功量", "联通成功量",
		"电信CDMA成功量", "电信小灵通成功量" };
	public static final String[] USER_SENDDETAIL_STAT = new String[] {"日期","用户名","部门","总提交量", "总发送量", "总成功量","移动成功量", "联通成功量",
		"电信CDMA成功量", "电信小灵通成功量" };
	public static final String[] BIZTYPE_SEND_STAT = new String[] { "日期","业务类型","总提交量", "总发送量", "总成功量","移动成功量", "联通成功量",
		"电信CDMA成功量", "电信小灵通成功量" };
	public static final String[] BILLCOUNTS_SEND_STAT = new String[] { "计费账户","短信消费(元)", "彩信消费(元)", "总消费(元)" };
	public static final String[] BILLCOUNT_SENDDETAIL_STAT = new String[] { "计费账户","计费时间","短信消费(元)", "彩信消费(元)", "总消费(元)" };
}

package com.xuanwu.mos.utils;

/**
 * 提示语
 * 
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-10-13
 * @version 1.0.0
 */
public class Messages {

	public static final String SYSTEM_ERROR = "系统错误，请联系系统管理员！";

	public static final String SELECT_REMOVE_DATA = "请选择需要删除的数据！";

	public static final String LOGIN_TIMEOUT = "登录超时或未登录！";
	public static final String ACCOUNT_STAT_NOT_NORMAL = "账号已被停用，请联系管理员.";
	public static final String ACCOUNT_LOCK = "您的账号将被锁定15分钟，请稍后再试!";
	public static final String ACCOUNT_OR_PASSWD_BLANK = "用户名或密码不允许为空！";
	public static final String ACCOUNT_NOT_EXIST = "用户账号或密码错误，请重新登录";
	public static final String PASSWD_NEED_CHANGE = "尊敬的客户，为进一步加强客户信息安全保障，我司平台进行了重要升级，请联系我司客服或销售代表，升级您的密码！不便之处，敬请谅解！客服热线：400-1000-566";
	public static final String FIRST_TIME_LOGIN = "您的账号密码为系统初始密码，为了您的信息安全，请修改账号密码。";
	public static final String EXPIRE_TIME_LOGIN = "密码过期，请重新修改您的密码";
	public static final String IP_IS_NOT_ALLOWD = "该登录的ip受限制";

	public static final String IMGVERIFYCODE_EXPIRED = "图形验证码已过期,请重新输入验证码！";
	public static final String IMGVERIFYCODE_BLANK = "请输入图形验证码！";
	public static final String IMGVERIFYCODE_INCORRECT = "图形验证码错误！";

	public static final String SMSVERIFYCODE_EXPIRED = "短信验证码已过期！";
	public static final String SMSVERIFYCODE_REQUEST = "请先获取短信验证码！";
	public static final String SMSVERIFYCODE_INCORRECT = "短信验证码错误！";

	public static final String CODE_ERROR_COUNT_OUT = "验证失败次数过多，请重新获取！";
	public static final String ERROR_COUNT_OUT = "验证失败次数过多，请明天再尝试！";
	public static final String EMAIL_SEND_COUNT_OUT = "邮件发送次数过多，请明天再尝试！";

	public static final String INPUT_NOT_MOBILE = "请输入正确的手机号码！";
	public static final String INPUT_NOT_PHONE = "请输入正确的电话号码！";
	public static final String INPUT_NOT_EMAIL = "请输入正确的邮箱地址！";
	public static final String INPUT_NOT_QQ = "请输入正确的QQ号码！";
	public static final String EMAIL_NOT_EXIST = "邮箱不存在！";

	public static final String INPUT_REGISTERED_MOBILE = "该手机号码已经被注册！";
	public static final String INPUT_REGISTERED_EMAIL = "该邮箱地址已经被注册！";
	public static final String INPUT_REGISTERED_USERNAME = "该用户名已经被注册！";

	public static final String SIGN_IRREGULAR = "企业签名仅允许输入2-15位（不含【】）！";
	public static final String SIGN_COUNT_LIMIT = "该企业签名已满10个，无法再添加新的签名记录";

	public static final String ENTERPRISE_NO_AUDIT = "待审核企业不存在！";
	public static final String ENTERPRISE_NO_APPLY = "待审核记录不存在！";

	public static final String ENTERPRISE_IDENTIFY_EXIST = "该企业标识号已经被占用或存在包含关系！";

	public static final String ENTERPRISE_SPECNUM_BIND = "端口号[{number}]已经被作为主端口！";
	public static final String ENTERPRISE_SPECNUM_REDIRECT_CHANGE = "端口号[{number}]已经被作为白名单备用或端口切换！";
	public static final String ENTERPRISE_SPECNUM_BIZTYPE_BINDED = "端口号[{number}]已经模板或业务能力绑定，不能删除！";
	public static final String ENTERPRISE_SPECNUM_REDIRECT_CHANGE_REMOVE = "端口号[{number}]已有备用或切换端口，请先解除！";

	public static final String PHRASE_IS_DELETED = "模板已被删除！";
	public static final String PHRASE_HAS_UPDATE = "模板信息发生了更新，请刷新页面重新审核！";
	public static final String PHRASE_NEED_SPECNUM = "请为模板绑定至少一个端口！";
	public static final String PHRASE_STATE_NOT_BIND_SPECNUM = "当前模板未审核通过，不允许该操作！";
	public static final String PHRASE_NEED_NUMBER = "请为模板选择至少一条语音通道！";
	public static final String NOT_FOUND_ROOT_SPECNUM = "未能找到该通道扩展的根端口记录！";

	public static final String NO_NEED_ACCREDIT_USER = "请选择需要授权的用户！";
	public static final String NO_NOT_NEED_ACCREDIT_USER = "请选择需要取消授权的用户！";
	public static final String ACCREDIT_ERROR = "授权失败！";
	public static final String CANCEL_ACCREDIT_ERROR = "取消授权失败！";

	public static final String TEST_NUM_COUNT_ERROR = "测试号码不能多于三个！";
	public static final String PHONE_EXIST = "该号码已为测试号码，请重新输入！";

	public static final String VOICE_DISPLAY_NUM_COUNT_ERROR = "语音显号不能多于十个！";

	public static final String OFFLINE_INVOICE = "您属于线下客户，请联系销售索取发票！";
	public static final String ERROR_ACCOUNT = "索取金额错误！";
	public static final String INVOICE_DELETED = "发票已删除，请刷新页面！";
	public static final String INVOICE_PASSED = "该发票已审核通过，请刷新页面！";

	public static final String HAVE_CHILD = "有子部门或用户,不能删除！";

	public static final String EXCEED_MAX_SEND_ACTIVE_MAIL_PERDAY = "激活邮件发送次数过多，请明天再试！";

	public static final String VERIFYCODE_EXPIRED = "验证码已过期！";
	public static final String VERIFYCODE_REQUEST = "请先获取验证码！";
	public static final String VERIFYCODE_INCORRECT = "验证码错误！";

	public static final String OPERATION_EXPIRED="操作超时！";

	public static final String IDENTIFY_ALREADY_USED = "该标识号已被使用，请重新输入！";

	public static final String QUERY_RECORD_FROM_YYPT_FAIL = "查询历史发送记录出错！";

	public static final String UN_ACTIVATED_ACCOUNT = "未激活账号！";

	public static final String PASSWORD_NOT_SAME = "两次密码输入不一致！";
	public static final String PASSWORD_NOT_RIGHT = "您输入的原密码错误！";
	public static final String PASSWORD_NOT_BLANK = "密码不能为空！";

	public static final String OPERATION_FAILED = "操作失败！";

	public static final String ACTIVATION_FAILED_ACTIVATED_ACCOUNT = "账号已激活，请直接登录！";
	public static final String ACTIVATION_FAILED_TOKEN_EXPIRED = "激活失败，token不存在！";
	public static final String ACTIVATION_FAILED_REGISTERED_MOBIL = "该手机号码已经被注册，请直接登录！";
	public static final String ACTIVATION_FAILED_REGISTERED_EMAIL = "该邮箱已经被注册，请直接登录！";

	public static final String ERROR_USER_NAME = "用户名格式错误，请输入您的手机/邮箱！";

	public static final String USER_NOT_EXIST = "不存在该注册用户！";

	public static final String INCORRECT_FILE_COLUMN = "文件列错误！";

	public static final String ALREADY_AUDITED = "已经审核过了";

	public static final String APP_TURN_OFF_ALREADY = "应用已经停用";

	public static final String OUT_OF_MONEY = "可支出金额不足";
	public static final String OUT_OF_MONEY_BECAUSE_INVOICE_TAKEN = "退款金额已大于剩余可索取发票金额";
	public static final String OUT_OF_MONEY_BECAUSE_OF_CASH_BOOK_POSITIVE = "您好，当前企业余额不足退款，请先添加企业充值金额!";

	public static final String COMMON_REQ_PARAM_ERROR = "请求参数错误！";
	public static final String APP_COUNT_LIMIT = "企业应用数超过100！";
	public static final String APP_NAME_REPEAT = "企业应用名称重复！";
	
	public static final String STATE_ERROR = "审核状态错误！";

	public static final String BIZTYPE_EXIST = "业务类型已存在！";
	public static final String DEFAULT_BIZTYPE = "默认业务类型不可以删除！";

	public static final String ANNOUNCEMENT_REPEAT = "公告标题已存在，请重新输入！";
	public static final String ANNOUNCEMENT_TITLE_REQUIRE = "公告标题为必填项";

	public static final String CAPITAL_ACCOUNT_EXIST = "已存在计费账户名称[{accountName}]，请输入其他名称！";
	public static final String ENTERPRISE_NOT_FOUNT_FOR_ADD = "找不到企业账号信息，无法新增计费账户！";
	public static final String ENTERPRISE_NOT_FOUNT_FOR_CHARGE = "找不到企业总账户信息，无法完成充值！";
	public static final String CAPITAL_CHILD_ACCOUNT_NOT_FOUNT = "找不到任何计费子账户信息，无法完成充值！";
	public static final String CAPITAL_CHILD_ACCOUNT_NOT_FOUNT2 = "找不到计费子账户信息[{accountName}],无法完成充值！";
	
	public static final String GROUP_NAME_NULL = "群组名称不能为空";
	public static final String PARENT_GROUP_NAME_NULL = "父群组为空";
	public static final String BEGINDATE_COMPARE_ENDDATE = "开始日期不能大于结束日期";
	public static final String BLACKLIST_EXIST = "黑名单已存在";
	public static final String PHONE_RESULT_NO_CARRIER_TELESEG = "手机号码不在运行商号段";
	public static final String WRONG_PHONE_LENGTH = "手机号码长度错误";

	public static final String WHITEPHONE_EXIST = "企业白名单已存在";

	public static final String WHITEPHONE_IS_NULL = "企业白名单手机号码不能为空";

	public static final String KEYWORD_EXIST = "关键字已存在";


}

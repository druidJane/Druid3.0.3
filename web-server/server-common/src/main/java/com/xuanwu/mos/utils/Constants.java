package com.xuanwu.mos.utils;

/**
 * 系统常量
 *
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-24
 * @version 1.0.0
 */
public class Constants {
	public static final Integer DEFAULT_PAGE_SIZE = 10;
	public static final Integer DEFAULT_MAX_PAGE_SIZE = 50;
	// 大数据量查询的时候，计数时 超过这个数，就不再进行计数，另外再单独进行技术
	public static final Integer FRONTKIT_MAX_COUNT_STOP_LIMIT = 50000;
	public static final Integer BACKEND_MAX_COUNT_STOP_LIMIT = 10000;

	/** 登录用户KEY */
	public static final String KEY_USER = "mos_user";

	/** 登录用户KEY */
	public static final String KEY_ENTERPRISE = "mos_enterprise";

	/**用户登录前台的方式*/
	public static final String KEY_LOGIN_TYPE = "mos_login_type";

	/** session中登录自动图片验证码KEY */
	public static final String IMAGE_VERIFY_CODE = "image_verify_code";
	/** session中登录自动图片验证码的起始时间*/
	public static final String IMAGE_VERIFY_CODE_REQ_TIME = "image_verify_code_req_time";

	/** session中手机验证码KEY */
	public static final String PHONE_VERIFY_CODE = "phone_verify_code";

	/** session中手机验证码KEY起始时间 */
	public static final String PHONE_VERIFY_CODE_REQ_TIME = "phone_verify_code_req_time";
	
	/** session验证令牌 */
	public static final String TOKEN_VERIFY = "token_verify";

	public static final String REQUEST_TIMER = "cmp_request_timer";

	public static final String PERMISSION_DATASCOPE = "permission_datascope";

	/** 请求头状态 */
	public static final String HEADER_ACCESS_STATE = "Access-State";

	public static final String FILE_TASK = "/SystemMgr/TaskMgr";
	public static final String COMMON = "/common";

	/**密码允许错误次数 */
	public static final Integer PASSWD_ALLOW_ERROR_TIMES = 3;

	/**前台load菜单,权限信息 */
	public static final String  MENU_PERMISSIONS = "menu_permissions";

	/* */
	public static final Integer PASSWD_ERR_LOCK_TIME = 15*60000;

	public static final String CHECK_FILTER = "check_filter";

	/**
	 *
	 */
	public static final String TABLETYPE = "msgType";

	public static final String TABLETIME = "queryTime";

	public static final String TABLEPARAMS = "params";

	public static final String TIMEOUT ="timeout";
}

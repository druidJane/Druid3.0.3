package com.xuanwu.mos.utils;

import com.xuanwu.mos.domain.entity.Enterprise;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.LoginTypeEnum;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会话相关的工具类
 *
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @date 2016-08-24
 * @version 1.0.0
 */
public class SessionUtil {

	private static int sessionTimeout = 20 * 60;// session失效时间，单位秒 (默认为20分钟，可以通过setSessionTime去修改,目前在配置中设置)

	private static int verifyCodeTimeout = 60;	//验证码过期时间，单位秒

	private static int phoneCodeTimeout = 90;	//手机验证码过期时间，单位秒

	// 当前会话session
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	// 设置当前登录用户
	public static void setCurUser(SimpleUser user) {
		if (user != null) {
			newRequestTimer();
		}
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.KEY_USER, user);
	}

	// 获取当前登录用户
	public static SimpleUser getCurUser() {
		return (SimpleUser) SecurityUtils.getSubject().getSession(true).getAttribute(Constants.KEY_USER);
	}

	// 设置当前登录企业
	public static void setCurEnterprise(Enterprise enterprise) {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.KEY_ENTERPRISE, enterprise);
	}

	// 获取当前登录企业
	public static Enterprise getCurEnterprise() {
		return (Enterprise) SecurityUtils.getSubject().getSession(true).getAttribute(Constants.KEY_ENTERPRISE);
	}

	// 判断是否登录
	public static boolean isLogin() {
		String state = (String)SecurityUtils.getSubject().getSession(true).getAttribute(Constants.HEADER_ACCESS_STATE);
		return state != null && state.equals("login");
	}


	// 新Session请求时间
	public static void newRequestTimer() {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.REQUEST_TIMER, System.currentTimeMillis());
	}

	// session是否已经过期
	public static boolean isSessionTimeout() {
		Long requestTimer = (Long) SecurityUtils.getSubject().getSession(true).getAttribute(Constants.REQUEST_TIMER);
		if (requestTimer == null) {
			return true;
		}
		return requestTimer < System.currentTimeMillis() - sessionTimeout * 1000;
	}

	// 令牌文件
	public static void setTokenVerify(Object params){
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.TOKEN_VERIFY, params);
	}
	
	// 获取令牌文件
	public static Object getTokenVerify() {
		return SecurityUtils.getSubject().getSession(true).getAttribute(Constants.TOKEN_VERIFY);
	}

	// 设置验证码
	public static void setImageVerifyCode(String imageVerifyCode) {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.IMAGE_VERIFY_CODE, imageVerifyCode);
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.IMAGE_VERIFY_CODE_REQ_TIME, System.currentTimeMillis());
	}

	// 获取一次性验证码
	public static String getImageVerifyCode() {
		//由于系统session有默认超时时间，若超时未登录，该值为null，判验证码超时.
		Long reqTime = (Long)SecurityUtils.getSubject().getSession(true).getAttribute(Constants.IMAGE_VERIFY_CODE_REQ_TIME);
		Long curTime = System.currentTimeMillis();
		if (reqTime == null || curTime - reqTime > verifyCodeTimeout*1000) {
			return null;
		}
		return (String) SecurityUtils.getSubject().getSession(true).getAttribute(Constants.IMAGE_VERIFY_CODE);
	}

	// 设置手机验证码
	public static void setPhoneVerifyCode(String phoneVerifyCode) {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.PHONE_VERIFY_CODE, phoneVerifyCode);
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.PHONE_VERIFY_CODE_REQ_TIME, System.currentTimeMillis());
	}

	public static String getPhoneVerifyCode() {
		Long reqTime = (Long)SecurityUtils.getSubject().getSession(true).getAttribute(Constants.PHONE_VERIFY_CODE_REQ_TIME);
		if (reqTime == null) {
			return null;
		}
		Long curTime = System.currentTimeMillis();
		if (curTime - reqTime > phoneCodeTimeout*1000) {
			return Constants.TIMEOUT;
		}
		return (String)SecurityUtils.getSubject().getSession(true).getAttribute(Constants.PHONE_VERIFY_CODE);
	}

	// 设置数据范围
	public static void setDataSope(HashMap<String, DataScope> dataScopes) {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.PERMISSION_DATASCOPE, dataScopes);
	}

	// 获取数据范围
	public static DataScope getDataSope(String url) {
		HashMap<String, DataScope> dataScopes = (HashMap<String, DataScope>) SecurityUtils.getSubject()
				.getSession(true).getAttribute(Constants.PERMISSION_DATASCOPE);
		if (dataScopes == null) {
			return DataScope.NONE;
		}

		DataScope dataScope = dataScopes.get(url);
		if (dataScope == null) {
			return DataScope.NONE;
		}

		return dataScope;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}

	public static void setLogin() {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.HEADER_ACCESS_STATE, "login");
	}

	//前台菜单加载标识
	public static void setMenus(Map<String, List<?>> menus) {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.MENU_PERMISSIONS, menus);
	}


	public static Map<String, List<?>> getMenus() {
		return (Map<String, List<?>>)SecurityUtils.getSubject().getSession(true).getAttribute(Constants.MENU_PERMISSIONS);
	}

	public static void setSessionTimeout(int sessionTimeout) {
		SessionUtil.sessionTimeout = sessionTimeout;
	}


	/**
	 * 是否有权限
	 *
	 * @param url
	 * @return
	 */
	public static boolean hasPermission(String url) {
		return SecurityUtils.getSubject().isPermitted(url);
	}

	public static void setLoginType(LoginTypeEnum loginType) {
		SecurityUtils.getSubject().getSession(true).setAttribute(Constants.KEY_LOGIN_TYPE, loginType);
	}

	public static LoginTypeEnum getLoginType(){
		return (LoginTypeEnum) SecurityUtils.getSubject().getSession(true).getAttribute(Constants.KEY_LOGIN_TYPE);
	}

}

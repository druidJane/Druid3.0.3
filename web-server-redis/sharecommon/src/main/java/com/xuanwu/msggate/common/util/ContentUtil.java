package com.xuanwu.msggate.common.util;

import java.util.regex.Pattern;

/**
 * 
 * @Description: TODO(用于校验内容的工具)
 * @Author <a href="zhangwu@wxchina.com">Zhang Wu</a>
 * @Date 2016年4月13日
 * @Version 1.0.0
 */
public class ContentUtil {

	private static String vCRegex="^([0-9a-zA-Z!~@#%\\$￥\\^&\\*\\(\\)\\[\\]\\<\\>\\{\\}\\?\\._\\+\\|-]){4,8}$";
	
	
	/**
	 * 内容，为数字0~9，字母a~z，特殊字符!(感叹号)、~(波浪号)、@(at)、#(井号)、%(百分号)、$(美元符)、￥(元符号)、^(
	 * 次方符)、&(与符号)、*(星号)、((左小括号)、)(右小括号)、.(点)、_(下划线)、+(加号)、|(竖线)、-(减号)、
	 * <(左尖括号)、>(右尖括号)、?(问号)、[(左中括号)、](右中括号)、{(左大括号)、}(右大括号)，长度4-8位。说明：字母不区分大小写，
	 * 特殊字符均为英文状态下输入(￥符号除外)。
	 * 
	 * @param content
	 * @return
	 */
	public static boolean voiceCodeContentLegal(String content) {
		boolean isLegal=Pattern.matches(vCRegex, content);
		return isLegal;
	}

}

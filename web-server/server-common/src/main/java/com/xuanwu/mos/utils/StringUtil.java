package com.xuanwu.mos.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class StringUtil {

	public static Integer attempt2Parse(String string){
		try{
			return Integer.parseInt(string);
		}catch (Exception ex){
			return null;
		}
	}


	/**
	 * 格式化json字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String fixJsonStr(String src) {
		if (StringUtils.isBlank(src)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		escapeJson(src, sb);
		return sb.toString();
	}

	/**
	 * 处理sql like查询中的 % _的转义 如查询 含_的字符串，like默认当作单字符处理，匹配任意单字符，因此需要转义成\_
	 * 
	 * @param likeSearchValue
	 * @return
	 */
	public static String escapeLikeSpecialCharacter(String likeSearchValue) {
		if (StringUtils.isNotBlank(likeSearchValue)) {
			return likeSearchValue.replace("\\", "\\\\").replace("%", "\\%");
		} else {
			return likeSearchValue;
		}
	}

	public static Boolean parseBooleanString(String booleanString){
		if(StringUtils.isBlank(booleanString)){
			return Boolean.FALSE;
		}else if("0".equals(booleanString)){
			return Boolean.FALSE;
		}else{
			return Boolean.TRUE;
		}
	}
	/**
	 * 格式化json字符串 不转义/
	 * 
	 * @param src
	 * @return
	 */
	public static String fixJsonStrWithOutSlash(String src) {
		if (StringUtils.isBlank(src)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		escapeJsonWithOutSlash(src, sb);
		return sb.toString();
	}

	/**
	 * @param s
	 *            - Must not be null.
	 * @param sb
	 */
	static void escapeJson(String s, StringBuilder sb) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		} // for
	}

	static void escapeJsonWithOutSlash(String s, StringBuilder sb) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			// case '/':
			// sb.append("\\/");
			// break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		} // for
	}

	/**
	 * 从后面截取字符串
	 * 
	 * @param str
	 *            需要截取的字符串
	 * @param length
	 *            要截取的长度
	 * @return
	 */
	public static String subStringOfAfter(String str, int length) {
		String sub = str.substring(str.length() - length, str.length());
		return sub;
	}

	/**
	 * 转换空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String trimNull(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 截取路径（如：1.3.5.）的前置部分
	 * 
	 * @param src
	 *            原路径
	 * @param ch
	 *            路径分隔字符
	 * @param limit
	 *            路径层次
	 * @return
	 */
	public static String prePath(String src, char ch, int limit) {
		if (src == null)
			return null;
		char[] arr = new char[src.length()];
		int l = 0;
		for (int i = 0; i < src.length(); i++) {
			arr[i] = src.charAt(i);
			if (arr[i] != ch) {
				continue;
			}
			l++;
			if (l == limit) {
				return new String(arr, 0, i + 1);
			}
		}
		return src;
	}

	/**
	 * 去除86，+86及0号码前缀
	 */
	public static String removeZhCnCode(String phone) {
		if (phone == null) {
			return null;
		}
		if (phone.length() < 10) {
			return phone;
		}
		if (phone.startsWith("+86")) {
			return phone.substring(3, phone.length());
		}
		if (phone.startsWith("86")) {
			return phone.substring(2, phone.length());
		}
		if (phone.startsWith("01") && phone.charAt(2) != '0') {
			return phone.substring(1);
		}

		return phone;
	}

	public static String replaceHtml(String str) {
		if (null != str) {
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
			str = str.replaceAll("\\\\'", "&#39;"); // 把json转义后的\'当做一个整体替换
			str = str.replaceAll("\\\\'", "&#39;"); // 把json转义后的\'当做一个整体替换
			str = str.replaceAll("\\\\'", "&#39;"); // 把json转义后的\'当做一个整体替换
			str = str.replaceAll("'", "&#39;");
			str = str.replaceAll("\\\\\"", "&#34;");// 把json转义后的\"当做一个整体替换
			str = str.replaceAll("\"", "&#34;");
		}
		return str;
	}

	/**
	 * 
	 * @param value
	 *            待转义的值
	 * @param escapeHtml
	 *            是否转义html字符
	 * @param escapeJson
	 *            是否转义json字符
	 * @return
	 */
	public static String escapeValue(String value, boolean escapeHtml, boolean escapeJson) {
		if (value == null) {
			return value;
		}
		if (escapeHtml) {
			value = HtmlUtils.htmlEscape(value);
		}
		if (escapeJson) {
			value = JavaScriptUtils.javaScriptEscape(value);
		}
		return value;
	}

	public static String hidePhone(String phone) {
		if (null == phone || phone.length() < 11)
			return phone;

		String start = phone.substring(0, phone.length() - 8);
		String end = phone.substring(phone.length() - 4, phone.length());
		StringBuilder sb = new StringBuilder();
		sb.append(start);
		sb.append("****");
		sb.append(end);

		return sb.toString();
	}

	/**
	 * 科学计数转换成字符
	 */
	public static String fixDoubleStr(double dou) {
		DecimalFormat df = new DecimalFormat("#.####");
		String doubleStr = df.format(dou);
		return doubleStr;
	}

	/**
	 * 数组合并成字符串，并用指定的字符分隔开
	 */
	public static String join(String separator, List<String> values) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.size(); i++) {
			if (i == (values.size() - 1)) {
				sb.append(values.get(i));
			} else {
				sb.append(values.get(i)).append(separator);
			}
		}

		return new String(sb);
	}

	public static void main(String[] args) {
		String a = "\"ttes";
		a = fixJsonStr(a);
		System.out.println(a);
		a = replaceHtml(a);
		System.out.println(a);
	}

	public static String formatNumber(BigDecimal bigDecimal){
		return bigDecimal.stripTrailingZeros().toPlainString();
	}

	public static String delZero(String str){
		if (str.indexOf(".") > 0) {
			str = str.replaceAll("0+?$", "");
			str = str.replaceAll("[.]$", "");
			str = str.replaceAll("0$", "");
		}
		return str;
	}
	
	/**
	 * 滤除content中的危险 HTML 代码, 主要是脚本代码, 滚动字幕代码以及脚本事件处理代码
	 * 
	 * @param content
	 *            需要滤除的字符串
	 * @return 过滤的结果
	 */
	public static String replaceHtmlCode(String content) {
		if (null == content) {
			return null;
		}
		if (0 == content.length()) {
			return "";
		}
		// 需要滤除的脚本事件关键字
		String[] eventKeywords = { "onmouseover", "onmouseout", "onmousedown",
				"onmouseup", "onmousemove", "onclick", "ondblclick",
				"onkeypress", "onkeydown", "onkeyup", "ondragstart",
				"onerrorupdate", "onhelp", "onreadystatechange", "onrowenter",
				"onrowexit", "onselectstart", "onload", "onunload",
				"onbeforeunload", "onblur", "onerror", "onfocus", "onresize",
				"onscroll", "oncontextmenu", "alert", "script","marquee"};
		content = replX(content, "</", "＜／", false);
		content = replX(content, "<", "＜", false);		
		content = replX(content, ">", "＞", false);
		content = replX(content, "'", "＇", false);
		content = replX(content, "\"", "＂", false);
		// 滤除脚本事件代码
		for (int i = 0; i < eventKeywords.length; i++) {
			content = replX(content, eventKeywords[i],
					"_" + eventKeywords[i]+"_", false); // 添加一个"_", 使事件代码无效
		}
		return content;
	}

	/**
	 * 将字符串 source 中的 oldStr 替换为 newStr, 并以大小写敏感方式进行查找
	 * 
	 * @param source
	 *            需要替换的源字符串
	 * @param oldStr
	 *            需要被替换的老字符串
	 * @param newStr
	 *            替换为的新字符串
	 */
	private static String replX(String source, String oldStr, String newStr) {
		return replX(source, oldStr, newStr, true);
	}

	/**
	 * 将字符串 source 中的 oldStr 替换为 newStr, matchCase 为是否设置大小写敏感查找
	 * 
	 * @param source
	 *            需要替换的源字符串
	 * @param oldStr
	 *            需要被替换的老字符串
	 * @param newStr
	 *            替换为的新字符串
	 * @param matchCase
	 *            是否需要按照大小写敏感方式查找
	 */
	private static String replX(String source, String oldStr, String newStr,
			boolean matchCase) {
		if (source == null) {
			return null;
		}
		// 首先检查旧字符串是否存在, 不存在就不进行替换
		if (source.toLowerCase().indexOf(oldStr.toLowerCase()) == -1) {
			return source;
		}
		int findStartPos = 0;
		int a = 0;
		while (a > -1) {
			int b = 0;
			String str1, str2, str3, str4, strA, strB;
			str1 = source;
			str2 = str1.toLowerCase();
			str3 = oldStr;
			str4 = str3.toLowerCase();
			if (matchCase) {
				strA = str1;
				strB = str3;
			} else {
				strA = str2;
				strB = str4;
			}
			a = strA.indexOf(strB, findStartPos);
			if (a > -1) {
				b = oldStr.length();
				findStartPos = a + b;
				StringBuffer bbuf = new StringBuffer(source);
				source = bbuf.replace(a, a + b, newStr) + "";
				// 新的查找开始点位于替换后的字符串的结尾
				findStartPos = findStartPos + newStr.length() - b;
			}
		}
		return source;
	}
	
	/**
	 * XSS跨域攻击字符处理
	 * @param str
	 * @return
	 */
	public static String xssEncode(String str){
		if(null != str){
			str = replaceHtmlCode(str);
			str = escapeLikeSpecialCharacter(str);			
		}
		return str;
	}
}

package com.xuanwu.msggate.common.util;

public class StringUtil {

private static String numericRegx = "\\d+";

	/**
	 * From apache
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
	    int strLen;
	    if (str == null || (strLen = str.length()) == 0) {
	        return true;
	    }
	    for (int i = 0; i < strLen; i++) {
	        if ((Character.isWhitespace(str.charAt(i)) == false)) {
	            return false;
	        }
	    }
	    return true;
	}

    /**
	 * 根据指定的规则校验字符串
	 * 
	 * @param src 源字符串
	 * @param notBlank 是否必须非空
	 * @param limitLenFlag 是否限制长度
	 * @param limitLen 限制的长度
	 * @param numeric 是否必须数字型
	 * @return true - 校验通过
	 */
	public static boolean verifyStr(String src, boolean notBlank, boolean limitLenFlag, int limitLen, boolean numeric){
		if(isBlank(src)){
			return notBlank ? false : true;
		}
		
		if(limitLenFlag){
			int len = src.codePointCount(0, src.length());
			if(len > limitLen){
				return false;
			}
		}
		
		if(numeric){
			return src.matches(numericRegx);
		}
		return true;
	}
	
	public static boolean hasChinese(String str){
		if(isBlank(str)){
			return false;
		}
		
		for(int i = 0; i < str.length(); i ++){
			if(isChinese(str.charAt(i))){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
			|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
			|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
			|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
			|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
}

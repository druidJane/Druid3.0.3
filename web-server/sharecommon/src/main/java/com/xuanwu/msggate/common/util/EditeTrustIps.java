package com.xuanwu.msggate.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
* @Description: 得到可信任的ip字符串集
* @Author <a href="qiaotaosheng@wxchina.com">TaoSheng.Qiao</a>    
* @Date 2016年3月23日 下午2:52:31
*/

public class EditeTrustIps {
	
	/** 点号 '.'*/
	public static final String DOT = ".";
	/** 逗号  ','*/
	public static final String COMMA = ",";
	/** 波浪号 ','*/
	public static final String TILDE = "~";
	/** 转义之后的点号 */
	public static final String ESCAPE_DOT = "[.]";

	private static String ipRegu = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

	public static String takeTrustIps(String originalTrustIps) {
		if(StringUtils.isBlank(originalTrustIps)){
			return "";
		}
		// 得到“~”，第三个第六个“.”的位置，取ip的最后一段
		int indexOdRange = 1;
		int indexOfFirstPoint = 3;
		int indexOfSecondPoint = 6;
		StringBuffer sBuffer = new StringBuffer();

		String[] strings = originalTrustIps.split(COMMA);
		for (String s : strings) {
			if (s.contains(TILDE)) {
				// 分别得到相应字符在字符串中的位置
				int index = getCharacterPosition(s, TILDE, indexOdRange);
				String ip1 = s.substring(0,index);
				String ip2 = s.substring(index + 1);
				if(checkIp(ip1) && checkIp(ip2)){
					int index1 = getCharacterPosition(s, ESCAPE_DOT, indexOfFirstPoint);
					int index2 = getCharacterPosition(s, ESCAPE_DOT, indexOfSecondPoint);
					// 得到ip中最后一部分的值并转换为integer类型
					Integer head = Integer.valueOf(s.substring(index1 + 1, index));
					Integer tail = Integer.valueOf(s.substring(index2 + 1));
					int count = tail - head;
					String firstString = s.substring(0, index1);
					for (int i = 0; i <= count; i++) {
						sBuffer.append(firstString);
						sBuffer.append(DOT);
						sBuffer.append(head++);
						sBuffer.append(COMMA);
					}
				}else {
					sBuffer.append(s);
					sBuffer.append(COMMA);
				}
			} else {
				sBuffer.append(s);
				sBuffer.append(COMMA);
			}
		}
		if(sBuffer.length() > 0){
			sBuffer.deleteCharAt(sBuffer.lastIndexOf(COMMA));
		}
		return sBuffer.toString();
	}

	/**
	 * 判断ip格式是否合法
	 * @param ip
	 * @return
     */
	private static boolean checkIp(String ip){
		if(StringUtils.isNotBlank(ip)){
			Pattern pattern = Pattern.compile(ipRegu);
			Matcher matcher = pattern.matcher(ip);
			return matcher.matches();
		}
		return false;
	}

	private static int getCharacterPosition(String string, String cString, int index) {
		// 这里是获取cString符号的位置
		Matcher slashMatcher = Pattern.compile(cString).matcher(string);
		int mIdx = 0;
		while (slashMatcher.find()) {
			mIdx++;
			if (mIdx == index) {
				break;
			}
		}
		int num = 0;
		try {
			num = slashMatcher.start();
		}catch (Exception e){
			e.printStackTrace();
		}
		return num;
	}

}

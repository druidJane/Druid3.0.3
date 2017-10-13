package com.xuanwu.mos.utils;

import org.springframework.util.Assert;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 日志内容格式化
 * @Data 2017-7-24
 * @Version 1.0.0
 */
public class LogContentUtil {

	public static String format(String content) {
		Assert.notNull(content, "log content is not null...");
		StringBuilder result = new StringBuilder();
		String[] cons = content.split(Delimiters.COMMA);
		switch (cons.length) {
			case 1:
			case 2:
				return content;
			default:
				result.append(cons[0]).append(Delimiters.COMMA).append(cons[1]).append("......");
				return result.toString();
		}
	}

}

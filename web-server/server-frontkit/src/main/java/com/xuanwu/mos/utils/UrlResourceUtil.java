package com.xuanwu.mos.utils;

import com.xuanwu.mos.config.Keys;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 资源路径处理
 * @Data 2017-6-14
 * @Version 1.0.0
 */
public class UrlResourceUtil {

	public static String handleUrlResource(String prefixPath, String suffixPath) {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("/")
				.append(prefixPath)
				.append("/")
				.append(suffixPath);
		return urlBuilder.toString();
	}

	public static void main(String[] args) {
		String resourceUrl = UrlResourceUtil.handleUrlResource(Keys.SYSTEMMGR_ACCOUNTMGR,
				Keys.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST);
		System.out.println(resourceUrl);
	}

}

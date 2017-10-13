/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.wappush;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.xuanwu.msggate.common.encode.EncodeUtil;
import com.xuanwu.msggate.common.encode.WordFragment;

@Component
public class WappushReposImpl implements WappushRepos {

	private final static String HEAD_SINGLE_PACK;
	private final static String HEAD_MUTIL_PACK;

	private final static String BODY_BEFORE_URL;
	private final static String BODY_BEFORE_REMARK;
	private final static String BODY_AFTER_REMARK;

	private final static int SINGLE_HEAD_LENGTH = 14; // 单包头长度
	private final static int MUTIL_HEAD_LENGTH = 24; // 多包头长度

	public static void main(String[] args) throws Exception {
		System.out.println(new String(HEAD_MUTIL_PACK.getBytes("UTF-8"))
				.length());
	}

	static {
		String[] UDH_14 = { "06", "05", "04", "0B84", "23F0" };
		String[] UDH_24 = { "0B", "05", "04", "0B84", "23F0", "00", "03" };

		String[] ARR_BODY_BEFORE_URL = { "06", "01", "AE02", "05", "6A", "00",
				"45", "C6", "0C", "03" };
		String[] ARR_BODY_BEFORE_REMARK = { "00", "01", "03" };
		String[] ARR_BODY_AFTER_REMARK = { "00", "01", "01" };

		HEAD_SINGLE_PACK = spliceStr(UDH_14);
		HEAD_MUTIL_PACK = spliceStr(UDH_24);

		BODY_BEFORE_URL = spliceStr(ARR_BODY_BEFORE_URL);
		BODY_BEFORE_REMARK = spliceStr(ARR_BODY_BEFORE_REMARK);
		BODY_AFTER_REMARK = spliceStr(ARR_BODY_AFTER_REMARK);
	}

	class Wappush {
		String url;
		String remark;

		public Wappush(String content) {
			if (StringUtils.isBlank(content))
				return;
			
			String[] tempArr = content.split(Pattern.quote("{xw}"));
			if (tempArr == null || tempArr.length != 2)
				return;
			url = tempArr[0];
			remark = tempArr[1];

		}

		public String getUrl() {
			return url;
		}

		public String getRemark() {
			return remark;
		}

	}

	private byte sequenceNumber;

	private final ReentrantLock lock = new ReentrantLock();

	@Override
	public List<WordFragment> splitWappushContent(String content, int mixLength) {

		List<WordFragment> wordList = new ArrayList<WordFragment>();
		try {
			String serialNo = getSerialNo();
			Wappush wappush = new Wappush(content);
			String url = convertStrToASCII(wappush.getUrl());
			String remark = convertByteToStr(wappush.getRemark().getBytes(
					"UTF-8"));
			String body = getBody(serialNo, url, remark);

			wordList = EncodeUtil.simpleSpliceContent(body, mixLength,
					SINGLE_HEAD_LENGTH);

			if (wordList.isEmpty())
				return wordList;

			if (wordList.size() == 1) {

				WordFragment word = wordList.get(0);
				String text = HEAD_SINGLE_PACK + word.getContent();
				word.setContent(text);

				return wordList;
			}

			wordList = EncodeUtil.simpleSpliceContent(body, mixLength,
					MUTIL_HEAD_LENGTH);

			serialNo = getSerialNo();
			for (int i = 0; i < wordList.size(); i++) {
				WordFragment word = wordList.get(i);
				String mutilPackHead = mutilPackageHead(serialNo,
						convertIntToTwoStr(wordList.size()),
						convertIntToTwoStr(i));
				String text = mutilPackHead + word.getContent();
				word.setContent(text);
			}

			return wordList;

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	private static String spliceStr(String[] strArr) {
		StringBuffer head = new StringBuffer();
		for (String udh : strArr) {
			head.append(udh);
		}
		return head.toString();
	}

	/**
	 * 分多包发送的 wappush 消息头
	 * 
	 * @param refNum
	 *            包的号码
	 * @param total
	 *            包的总段数
	 * @param curSerial
	 *            包的段号
	 * @return
	 */
	private String mutilPackageHead(String serialNo, String total,
			String curSerial) {
		StringBuffer head = new StringBuffer();

		head.append(HEAD_MUTIL_PACK);
		head.append(serialNo);
		head.append(total);
		head.append(curSerial);

		return head.toString();
	}

	/**
	 * 生成内容体
	 * 
	 * @return
	 */
	private String getBody(String serialNo, String url, String remark) {
		StringBuffer body = new StringBuffer();

		body.append(serialNo);
		body.append(BODY_BEFORE_URL);
		body.append(url);
		body.append(BODY_BEFORE_REMARK);
		body.append(remark);
		body.append(BODY_AFTER_REMARK);

		return body.toString();
	}

	private String getSerialNo() {
		int serialNo = 0;
		try {
			lock.lock();
			serialNo = (sequenceNumber < 0) ? (sequenceNumber + 256)
					: sequenceNumber;

			String serialNoStr = Integer.toHexString(serialNo);
			if (serialNoStr.length() == 1) {
				serialNoStr = "0" + serialNoStr;
			}
			++sequenceNumber;
			return serialNoStr.toUpperCase();
		} finally {
			lock.unlock();
		}
	}

	private String convertByteToStr(byte[] paramBuffer) {
		int length = paramBuffer.length;
		int k = 0;
		String ud = "";
		String temp = new String("");
		for (int i = 0; i < length; i++) {
			k = paramBuffer[i];
			if (k < 0)
				k = k + 256;
			temp = Integer.toHexString(k);
			if (temp.length() == 1)
				temp = "0" + temp;
			ud = ud + temp;
		}
		return ud.toUpperCase();
	}

	private String convertStrToASCII(String src) {
		char[] ch = src.toCharArray();
		if (ch == null) {
			return null;
		}
		String dest = "";
		for (int i = 0; i < ch.length; i++) {
			int temp = (int) ch[i];
			dest += Integer.toHexString(temp);
		}
		return dest.toUpperCase();
	}

	private String convertIntToTwoStr(int val) {
		if (val > 99) {
			throw new RuntimeException(
					"Number of wappush pack fragmentation limit to 100.");
		}

		return (val < 10) ? ("0" + 1) : ("" + 1);
	}
}

/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.logic.impl;

import com.xuanwu.msggate.common.core.Config;
import com.xuanwu.msggate.common.encode.EncodeUtil;
import com.xuanwu.msggate.common.encode.WordFragment;
import com.xuanwu.msggate.common.encode.WordFragmentBox;
import com.xuanwu.msggate.common.logic.WordFragmentRepos;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 普通短信分词处理,针对内容超长的信息分词处理。 处理后的内容编码为 UTF-16BE，格式为
 * eg:(1/3)内容分段1(2/3)内容分段2(3/3)内容分段3
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-1-13
 * @Version 1.0.0
 */
public class WordFragmentReposImpl implements WordFragmentRepos {

	private static final int SMS_HEAD_TEN_BYTE = 10;
	private static final int SMS_HEAD_FOURTEEN_BYTE = 14;

	private static final String ENCODING = "UTF-16BE";

	private Config config;

	@Override
	public String encodeContent(String content, String signature,
			MsgType msgType, boolean sigLocation, boolean isSignal,
			int mixLength, int extendSpace) {

		if (msgType == MsgType.LONGSMS) {
			WordFragmentBox wordBox = new WordFragmentBox(content, signature,
					sigLocation,isSignal);
			return wordBox.encodeContent();
		}

		List<WordFragment> wordList = EncodeUtil.simpleSpliceContent(content,
				mixLength, extendSpace);
		WordFragmentBox wordBox = new WordFragmentBox(signature, sigLocation,isSignal,
				wordList);

		return wordBox.encodeContent();
	}

	@Override
	public int getNormalSmsExtendSpace(String content, String signature,
			int mixLength) {

		if (mixLength == 0) {
			throw new RuntimeException("The splice lenth can't be zero!");
		}

		try {
			byte[] bytes = content.getBytes(ENCODING);
			byte[] byteSignature = signature.getBytes(ENCODING);

			if ((bytes.length + byteSignature.length) <= mixLength) {
				return 0;
			}

			if (bytes.length > config.getMaxLongSmsLen()) {
				throw new RuntimeException(
						"The content length not more than "
								+ config.getMaxLongSmsLen());
			}
			
			// 以 10 个字节的 分段前缀算 eg: (1/9)
			int total = EncodeUtil.spliceUCS2(content, (mixLength
					- SMS_HEAD_TEN_BYTE - byteSignature.length)).length;

			if (total > 9) {
				// 以14 个字节的 分段前缀算 eg: (10/10)
				total = EncodeUtil.spliceUCS2(content, (mixLength
						- SMS_HEAD_FOURTEEN_BYTE - byteSignature.length)).length;

				return SMS_HEAD_FOURTEEN_BYTE + byteSignature.length;
			}

			return SMS_HEAD_TEN_BYTE + byteSignature.length;

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}

	}

	@Autowired
	public void setConfig(Config config) {
		this.config = config;
	}
}

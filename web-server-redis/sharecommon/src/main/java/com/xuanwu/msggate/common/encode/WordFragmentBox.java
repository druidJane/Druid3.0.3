/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.encode;

import java.util.ArrayList;
import java.util.List;

public class WordFragmentBox {
	private String signature;
	private boolean sigLocation;
	private boolean isSignal;

	private List<WordFragment> wordFragmentList = new ArrayList<WordFragment>();
 
	public WordFragmentBox(String content, String signature,
			boolean sigLocation, boolean isSignal) {
		this.signature = signature;
		this.sigLocation = sigLocation;
		this.isSignal = isSignal;
		if (!isSignal) { // 运营商添加签名
			content += addSpaces(signature.length());
		}

		wordFragmentList.add(new WordFragment(EncodeType.UCS2, content));
	}

	private String addSpaces(int count) {
		String spaces = "";
		for (int i = 0; i < count; i++) {
			spaces += " ";
		}
		return spaces;
	}

	public WordFragmentBox(String signature, boolean sigLocation,
			boolean isSignal, List<WordFragment> wordFragmentList) {
		this.signature = signature;
		this.sigLocation = sigLocation;
		this.isSignal = isSignal;
		this.wordFragmentList = wordFragmentList;
	}

	public String encodeContent() {
		String content = "";
		int index = 1;
		for (WordFragment word : wordFragmentList) {
			content += normalSMSContent(word.getContent(), wordFragmentList
					.size(), index);
			index++;
		}
		return content;
	}

	private String normalSMSContent(String content, int totalNum, int currentNum) {
		if (totalNum == 1) {
			if (isSignal) // 加签名
				return (sigLocation) ? (signature + content) // 签名前置
						: (content + signature); // 签名后置
			else
				return content;
		}

		String flag = "(" + currentNum + "/" + totalNum + ")";
		if (isSignal) { // 加签名
			if (sigLocation) { // 签名前置
				return (flag + signature + content);
			}
			// 签名后置
			return (flag + content + signature);
		} else
			return (flag + content);
	}
}

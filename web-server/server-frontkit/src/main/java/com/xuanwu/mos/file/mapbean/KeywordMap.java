package com.xuanwu.mos.file.mapbean;

import java.util.regex.Pattern;

public class KeywordMap {

	private int keywordName;
	private int handleTime;

	public int getKeywordName() {
		return keywordName;
	}

	public void setKeywordName(int keywordName) {
		this.keywordName = keywordName;
	}

	public int getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(int handleTime) {
		this.handleTime = handleTime;
	}

	public String tran2Params() {
		StringBuilder sb = new StringBuilder();
		sb.append(keywordName).append(";");
		sb.append(handleTime).append(";");
		sb.deleteCharAt(sb.toString().length() - 1);
		return sb.toString();
	}

	public static KeywordMap parseFrom(String str) {
		String[] temp = str.split(Pattern.quote(";"));
		KeywordMap keywordMap = new KeywordMap();
		keywordMap.setKeywordName(Integer.valueOf(temp[0]));
		keywordMap.setHandleTime(Integer.valueOf(temp[1]));
		return keywordMap;
	}
}

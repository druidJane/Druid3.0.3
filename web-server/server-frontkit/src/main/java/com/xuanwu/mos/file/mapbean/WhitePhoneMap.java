package com.xuanwu.mos.file.mapbean;

import java.util.regex.Pattern;

public class WhitePhoneMap {
	private int telphone;

	public String tran2Params() {
		StringBuilder sb = new StringBuilder();
		sb.append(telphone).append(";");
		sb.deleteCharAt(sb.toString().length() - 1);
		return sb.toString();
	}

	public static WhitePhoneMap parseFrom(String str) {
		String[] temp = str.split(Pattern.quote(";"));
		WhitePhoneMap whitePhoneMap = new WhitePhoneMap();
		whitePhoneMap.setTelphone(Integer.valueOf(temp[0]));
		return whitePhoneMap;
	}

	public int getTelphone() {
		return telphone;
	}

	public void setTelphone(int telphone) {
		this.telphone = telphone;
	}
	
}

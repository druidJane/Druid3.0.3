package com.xuanwu.mos.file.mapbean;


import java.util.regex.Pattern;

public class ChargeAccountMap {
	private int accountName;
	private int chargeMoney;

	public int getAccountName() {
		return accountName;
	}

	public void setAccountName(int accountName) {
		this.accountName = accountName;
	}

	public int getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(int chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public String tran2Params() {
		StringBuilder sb = new StringBuilder();
		sb.append(accountName).append(";");
		sb.append(chargeMoney).append(";");
		sb.deleteCharAt(sb.toString().length() - 1);
		return sb.toString();
	}

	public static ChargeAccountMap parseFrom(String str) {
		String[] temp = str.split(Pattern.quote(";"));
		ChargeAccountMap  accountMap = new ChargeAccountMap();
		accountMap.setAccountName(Integer.valueOf(temp[0]));
		accountMap.setChargeMoney(Integer.valueOf(temp[1]));
		return accountMap;
	}
}

/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.file.mapbean;

import java.util.regex.Pattern;

/**
 * @Description: 通讯录导入映射
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2012-12-5
 * @Version 1.0.0
 */
public class ContactMap {

	private int name;
	private int phone;
	private int sex;
	private int birthday;
	private int identifier;
	private int vip;
	private int remark;

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getBirthday() {
		return birthday;
	}

	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getRemark() {
		return remark;
	}

	public void setRemark(int remark) {
		this.remark = remark;
	}

	public String tran2Params() {
		StringBuilder sb = new StringBuilder();
		sb.append(phone).append(";");
		sb.append(name).append(";");
		sb.append(sex).append(";");
		sb.append(vip).append(";");
		sb.append(birthday).append(";");
		sb.append(identifier).append(";");
		sb.append(remark).append(";");
		sb.deleteCharAt(sb.toString().length() - 1);
		return sb.toString();
	}

	public static ContactMap parseFrom(String str) {
		String[] temp = str.split(Pattern.quote(";"));
		ContactMap contactMap = new ContactMap();
		contactMap.setPhone(Integer.valueOf(temp[0]));
		contactMap.setName(Integer.valueOf(temp[1]));
		contactMap.setSex(Integer.valueOf(temp[2]));
		contactMap.setVip(Integer.valueOf(temp[3]));
		contactMap.setBirthday(Integer.valueOf(temp[4]));
		contactMap.setIdentifier(Integer.valueOf(temp[5]));
		contactMap.setRemark(Integer.valueOf(temp[6]));
		return contactMap;
	}
}

/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.file.mapbean;



import com.xuanwu.mos.config.Platform;

import java.util.regex.Pattern;

/**
 * @Description:
 * @Author <a href="dflin163@163.com">DaoFen.Lin</a>
 * @Date 2012-11-5
 * @Version 1.0.0
 */
public class BlackListMap {

	private int phone;
	private int type;
	private int target;
	private int addOrDel;
	private int remark;

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getAddOrDel() {
		return addOrDel;
	}

	public void setAddOrDel(int addOrDel) {
		this.addOrDel = addOrDel;
	}
	
	public int getRemark() {
		return remark;
	}

	public void setRemark(int remark) {
		this.remark = remark;
	}
	
	public String tran2Params(Platform platform) {
		StringBuilder sb = new StringBuilder();
		sb.append(phone).append(";");
		sb.append(type).append(";");
		sb.append(target).append(";");
		if (platform == Platform.FRONTKIT) {
			sb.append(addOrDel).append(";");
		}
		sb.append(remark).append(";");
		sb.deleteCharAt(sb.toString().length() - 1);
		return sb.toString();
	}

	public static BlackListMap parseFrom(String str, Platform platform) {
		String[] temp = str.split(Pattern.quote(";"));
		BlackListMap blacklistMap = new BlackListMap();
		blacklistMap.setPhone(Integer.valueOf(temp[0]));
		blacklistMap.setType(Integer.valueOf(temp[1]));
		blacklistMap.setTarget(Integer.valueOf(temp[2]));
		blacklistMap.setRemark(Integer.valueOf(temp[3]));
		return blacklistMap;
	}
}

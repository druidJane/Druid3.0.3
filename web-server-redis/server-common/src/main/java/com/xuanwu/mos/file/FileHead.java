/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.file;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by 林泽强 on 2016/8/22. 文件头
 */
public class FileHead {

	public static final String[] USER = new String[] { "登录账号", "用户名称","性别", "出生日期", "联系方式", "所属部门编号",
			"拥有角色","业务类型","备注" };

	public static final String[] CONTACT = new String[] { "姓名", "手机号码", "所属组",
			"性别", "生日", "编号", "VIP", "备注" };

	public static final String[] KEYWORD = new String[] { "非法关键字", "最后更新时间" };

	public static final String[] BLACKLIST = new String[] { "手机号码", "黑名单类型",
			"所属对象", "备注" };

	public static final String[] WHITELIST = new String[] { "手机号码", "通道名" };

	public static final String[] NONWHITELIST = new String[]  { "手机号码", "通道名" };

	public static final String[] MOTICKET = new String[] { "手机号码", "接收内容",
			"接收用户", "接收部门", "接收端口", "接收时间", "回复状态" };

	public static final String[] SUM_DEPT_REALTIME = new String[] { "部门名称",
			"总接收量（条）", "总提交量（条）", "移动提交量（条）", "联通提交量（条）", "电信CDMA提交量（条）",
			"电信小灵通提交量（条）" };

	public static final String[] SMS_BATCH = new String[] { "批次名称", "提交时间",
			"完成时间", "发送用户", "批次状态", "用户提交号码数", "过滤后号码数", "已发送号码数" };

	public static final String[] SMS_NUMBER = new String[] { "手机号码", "发送内容",
			"发送用户", "发送时间", "发送通道", "发送结果", "提交报告", "状态报告", "批次名称" };

	public static final String[] MMS_BATCH = new String[] { "批次名称", "提交时间",
			"完成时间", "发送用户", "批次状态", "用户提交号码数", "过滤后号码数", "已发送号码数" };

	public static final String[] MMS_NUMBER = new String[] { "手机号码", "彩信标题",
			"发送用户", "发送时间", "发送通道", "发送结果", "提交报告", "状态报告", "批次名称" };

	public static final String[] SUM_DEPT_HISTROY = new String[] { "部门名称",
			"总接收量（条）", "总提交量（条）", "总成功量（条）", "移动成功量（条）", "联通成功量（条）",
			"电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] SUM_USER_REALTIME = new String[] { "用户名称",
			"所属部门", "总接收量（条）", "总提交量（条）", "移动提交量（条）", "联通提交量（条）",
			"电信CDMA提交量（条）", "电信小灵通提交量（条）" };

	public static final String[] SUM_USER_HISTORY = new String[] { "用户名称",
			"所属部门", "总接收量（条）", "总提交量（条）", "总成功量（条）", "移动成功量（条）", "联通成功量（条）",
			"电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] SUM_CHANNEL_HISTORY = new String[] { "通道号",
			"通道名", "所属运营商", "总提交量（条）", "总成功量（条）", "总失败量（条）",
			"成功率", "是否支持状态报告" };

	public static final String[] SUM_BIZTYPE_REALTIME = new String[] { "业务类型",
			"总接收量（条）", "总提交量（条）", "移动提交量（条）", "联通提交量（条）", "电信CDMA提交量（条）",
			"电信小灵通提交量（条）" };

	public static final String[] SUM_BIZTYPE_HISTORY = new String[] { "业务类型",
			"总接收量（条）", "总提交量（条）", "总成功量（条）", "移动成功量（条）", "联通成功量（条）",
			"电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] TREND_DEPT_HISTORY = new String[] { "部门名称",
			"日期", "总接收量（条）", "总提交量（条）", "总成功量（条）", "移动成功量（条）", "联通成功量（条）",
			"电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] TREND_USER_HISTORY = new String[] { "用户名称",
			"所属名称", "日期", "总接收量（条）", "总提交量（条）", "总成功量（条）", "移动成功量（条）",
			"联通成功量（条）", "电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] TREND_CHANNEL_HISTORY = new String[] { "通道号",
			"通道名", "日期", "所属运营商", "总提交量（条）", "总成功量（条）", "总失败量（条）",
			"成功率", "是否支持状态报告" };

	public static final String[] TREND_BIZTYPE_HISTORY = new String[] { "业务类型",
			"日期", "总接收量（条）", "总提交量（条）", "总成功量（条）", "移动成功量（条）", "联通成功量（条）",
			"电信CDMA成功量（条）", "电信小灵通成功量（条）" };

	public static final String[] BILLING_ACCOUNT_INFO = new String[] { "计费账户",
			"计费时间", "总消费", "短信消费", "彩信消费"};

	private LinkedHashMap<Integer, String> headMap = new LinkedHashMap<Integer, String>();

	private boolean isDelimterSucc = true;

	public LinkedHashMap<Integer, String> getHeadMap() {
		return headMap;
	}

	public void putCell(int index, String name) {
		headMap.put(index, name);
	}

	public static String[] getImportFailedHead(String[] src) {
		List<String> list = new ArrayList<String>();
		for (String s : src)
			list.add(s);
		list.add("失败原因");
		return list.toArray(new String[0]);
	}

	public boolean isDelimterSucc() {
		return isDelimterSucc;
	}

	public void setDelimterSucc(boolean isDelimterSucc) {
		this.isDelimterSucc = isDelimterSucc;
	}
}

package com.xuanwu.mos.file.importer;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.Result;

import java.util.Collections;
import java.util.List;

/**
 * Created by 林泽强 on 2016/8/26. 导入信息
 */
public class ImportInfo {

	public enum CheckResult {
		/** 合法 */
		Legal,
		/** 手机号码不合法 */
		IllegalPhone,
		/** 用户扩展码不合法 */
		IllegalUserExtNum,
		/** 部门编码不合法 */
		IllegalDeptNo,
		/** IP地址不合法 */
		IllegalIP,
		/** 用户姓名不合法 */
		IllegalLinkMan,
		/** 用户登录名不合法 */
		IllegalUserName,
		/** 部门编码不存在 */
		DeptNotExsit,
		/** 用户登录名重复 */
		UserNameRepate,
		/** 用户类型不合法 */
		IllegalloginTypes,
		/** 用户签名太长 */
		SignatureTooLong,
		/** 备注太长 */
		RemarkTooLong,
		/** 签名位置无效 */
		IllegalSigLocation,
		/** 用户扩展码重复 */
		UserExtNumRepate,
		/** 性别不合法或为空 */
		IllegalSex,
		/** VIP属性不合法或为空 */
		IllegalVip,
		/** 角色不在部门权限范围 */
		IllegalUserRoleNames,
		/** 业务类型不在部门权限范围 */
		IllegalUserBizNames,
		/** 通讯录姓名为空 */
		ContactNameEmpty,
		/** 通讯录手机号码重复 */
		ContactPhoneExists,
		/** 非法通讯录群组名称 */
		ContactIllegalGroupName,
		/** 非法通讯录联系人名称 */
		ContactIllegalName,
		/** 非法通讯录联系人编号 */
		ContactIllegalIdentifier,
		/** 非法通讯录联系人备注 */
		ContactIllegalRemark,

		/** 非法关键字不合法 */
		IllegalKeyword,
		/** 非法关键字已经存在 */
		KeywordExists,

		/** 手机号码为空 */
		BlankPhone,
		/** 手机号码为空 */
		WhitePhone,
		/** 备注信息太长 */
		BlacklistRemarkTooLong,
		/** 黑名单类型不合法 */
		IllegalBlacklistType,
		/** 所属对象不合法 */
		IllegalBlacklistTarget,
		/** 黑名单已经存在 */
		BlacklistExists,

		/** 备注信息太长 */
		WhitelistRemarkTooLong,
		/** 白名单通道不存在 */
		ChannelNotExsit,
		/** 白名单已经存在 */
		WhitelistExists,

		/** 查询指令不能为空 */
		SelfNameEmpty,
		/** 查询指令长度不能超过20个字符 */
		SelfNameTooLong,
		/** 查询指令已存在 */
		SelfNameExsit,
		/** 查询指令不合法 */
		SelfNameIllegal,
		/** 信息主题不能超过100个字符 */
		SelfSubjectTooLong,
		/** 信息内容不能为空 */
		SelfAutoRepliesEmpty,
		/** 信息内容不能超过900个字符 */
		SelfAutoRepliesTooLong,
		/** 信息收发人非有效账号 */
		SelfHandleUserNotExsit,
		/** 密码格式不正确*/
		PasswordIncorrectFormat,
		/*找不到企业总账户信息，无法完成充值。*/
		NotFoundParentChargeAccount,
		/*找不到任何计费子账户信息，无法完成充值。*/
		NotFoundChildChargeAccount,
		/*企业账户余额不足,无法充值。*/
		ChargeAccountBlanceNotEnough
	}

	private int importedCount;
	private Result result;
	private Object failedItems;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Object getFailedItems() {
		return failedItems;
	}

	public void setFailedItems(Object failedItems) {
		this.failedItems = failedItems;
	}

	@SuppressWarnings("unchecked")
	public List<AbstractEntity> drainTo(List<AbstractEntity> list) {
		if (failedItems == null || result == Result.Failed) {
			return Collections.emptyList();
		} else {
			list.addAll((List<AbstractEntity>) failedItems);
			return list;
		}
	}
}

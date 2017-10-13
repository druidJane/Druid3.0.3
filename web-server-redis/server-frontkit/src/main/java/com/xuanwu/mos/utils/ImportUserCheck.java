package com.xuanwu.mos.utils;

import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.Department;
import com.xuanwu.mos.domain.entity.Role;
import com.xuanwu.mos.domain.entity.User;
import com.xuanwu.mos.domain.enums.CheckResult;
import com.xuanwu.mos.domain.enums.UserAccountType;
import com.xuanwu.mos.domain.enums.UserProtocolType;
import com.xuanwu.mos.file.importbean.UserImport;
import com.xuanwu.mos.file.importer.ImportInfoBuild;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 用户导入校验
 * @Data 2017-4-21
 * @Version 1.0.0
 */
public class ImportUserCheck {

	private static final String userNameRegex = "^\\w+$";
	private static final String pwdRegex = "^[\\da-zA-Z]{8,16}$";
	private static final String userIdentityRegex = "^\\d*$";
	private static final String srcPortRegex = "^\\d{1,20}$";
	private static final String callbackAddressRegex = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]):[1-9]{1}[0-9]{0,5}$";

	//校验用户账号
	public static boolean validUserName(ImportInfoBuild importInfoBuild,
										User user, UserImport userImport, Set<String> tempUserNames, Set<String> dbUserNames) {
		if (!user.getUserName().matches(userNameRegex)) {
			importInfoBuild.increment(CheckResult.IllegalUserName, userImport);
			return false;
		}
		if (tempUserNames.contains(user.getUserName())) {
			importInfoBuild.increment(CheckResult.UserNameRepate, userImport);
			return false;
		} else {
			tempUserNames.add(user.getUserName());
		}
		if (dbUserNames.contains(user.getUserName())) {
			importInfoBuild.increment(CheckResult.UserNameRepate, userImport);
			return false;
		}
		if (user.getAccountType() == UserAccountType.OSPF) {
			if (user.getUserName().length() > 6) {
				importInfoBuild.increment(CheckResult.UserNameTooLong, userImport);
				return false;
			}
		} else {
			if (user.getUserName().length() > 30) {
				importInfoBuild.increment(CheckResult.UserNameTooLong, userImport);
				return false;
			}
		}
		return true;
	}

	//校验用户名
	public static boolean validLinkMan(ImportInfoBuild importInfoBuild,
									   User user, UserImport userImport){
		if (StringUtils.isBlank(user.getLinkMan())) {
			importInfoBuild.increment(CheckResult.IllegalLinkMan, userImport);
			return false;
		}
		if (user.getLinkMan().length() > 50) {
			importInfoBuild.increment(CheckResult.LinkManTooLong, userImport);
			return false;
		}
		return true;
	}

	//校验部门编号,部门名称,角色,业务类型
	public static boolean validDeptIdentityAndRoleAndBizType(User user, UserImport userImport, Map<String, Department> dbDeptMap,
															 ImportInfoBuild importInfoBuild) {
		if (!dbDeptMap.containsKey(user.getParentIdentify())) {
			importInfoBuild.increment(CheckResult.DeptNotExsit, userImport);
			return false;
		}

		Department dbDept = dbDeptMap.get(user.getParentIdentify());
		if (StringUtils.isNotBlank(user.getEnterpriseName())) {
			if (!dbDept.getDeptName().equals(user.getEnterpriseName())) {
				importInfoBuild.increment(CheckResult.DeptNameIllegal, userImport);
				return false;
			}
		}
		//校验角色
		if (StringUtils.isBlank(user.getRoleNames())) {
			importInfoBuild.increment(CheckResult.IllegalRole, userImport);
			return false;
		} else {
			Map<String, Role> dbRoleMap = dbRoleMap(dbDept);
			List<Role> roles = new ArrayList<>();
			Set<String> roleNames = new HashSet<>(Arrays.asList(user.
					getRoleNames().split("&")));
			boolean existDefault = false;
			for (String roleName : roleNames) {
				if (!dbRoleMap.containsKey(roleName)) {
					importInfoBuild.increment(CheckResult.IllegalUserRoleNames, userImport);
					return false;
				}
				Role role = dbRoleMap.get(roleName);
				role.setChecked(true);
				if (role.isDefault()) {
					existDefault = true;
				}
				roles.add(role);
			}
			if (!existDefault) {
				roles.add(dbRoleMap.get("$DEFAULT_ROLE$"));
			}
			user.setRoles(roles);
		}
		//校验业务类型
		return validBusinessType(dbDept, user, userImport, importInfoBuild);
	}

	//登录与发送/透传密码校验
	public static boolean validPassword(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (!user.getSecondPassword().matches(pwdRegex)) {
			importInfoBuild.increment(CheckResult.PasswordRuleLimit, userImport);
			return false;
		}
		if (user.getAccountType() == UserAccountType.OSPF) {
			if (!user.getMidPassword().matches(pwdRegex)) {
				importInfoBuild.increment(CheckResult.PasswordRuleLimit, userImport);
				return false;
			}
		} else {
			if (!user.getPassword().matches(pwdRegex)) {
				importInfoBuild.increment(CheckResult.PasswordRuleLimit, userImport);
				return false;
			}
		}
		return true;
	}

	private static Map<String, Role> dbRoleMap(Department dept) {
		Map<String, Role> map = new HashMap<>();
		for (Role role : dept.getRoles()) {
			map.put(role.getName(), role);
			if (role.isDefault()) {
				role.setChecked(true);
				map.put("$DEFAULT_ROLE$", role);
			}
		}
		return map;
	}

	private static boolean validBusinessType(Department dept, User user,
											 UserImport userImport, ImportInfoBuild importInfoBuild) {
		List<BizType> bizTypes = new ArrayList<>();
		if (StringUtils.isNotBlank(user.getBizIds())) {
			Set<String> bizIds = new HashSet<>(Arrays.asList(user.
					getBizIds().split("&")));
			Map<String, BizType> bizTypeMap = dbBizType(dept);
			for (String bizId : bizIds) {
				if (!bizTypeMap.containsKey(bizId)) {
					importInfoBuild.increment(CheckResult.DeptExcludeBizType, userImport);
					return false;
				}
				bizTypeMap.get(bizId).setBound(true);
				bizTypes.add(bizTypeMap.get(bizId));
			}
		}

		if (bizTypes.size() == 0) {
			BizType defaultBizType = dept.getBizTypes().get(0);
			defaultBizType.setBound(true);
			bizTypes.add(defaultBizType);
		}
		user.setBizTypes(bizTypes);
		return true;
	}

	private static Map<String, BizType> dbBizType(Department dbDept) {
		Map<String, BizType> map = new HashMap<>();
		for (BizType bizType : dbDept.getBizTypes()) {
			map.put(bizType.getId().toString(), bizType);
		}
		return map;
	}

	//校验用户扩展码
	public static boolean validUserIdentity(ImportInfoBuild importInfoBuild,
											User user, UserImport userImport, Set<String> dbUserIdentity,
											Set<String> tempUserIdentity) {
		if (StringUtils.isNotBlank(user.getIdentify()) &&
				tempUserIdentity.contains(user.getIdentify())) {
			importInfoBuild.increment(CheckResult.UserIdentityRepeat, userImport);
			return false;
		} else {
			tempUserIdentity.add(user.getIdentify());
		}
		if (!user.getIdentify().matches(userIdentityRegex)) {
			importInfoBuild.increment(CheckResult.UserIndentityBlueLimit, userImport);
			return false;
		}

		if (StringUtils.isNotBlank(user.getIdentify()) &&
				dbUserIdentity.contains(user.getIdentify())) {
			importInfoBuild.increment(CheckResult.UserIdentityRepeat, userImport);
			return false;
		}
		return true;
	}

	//校验签名位置
	public static boolean validSigLocation(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (user.getSigLocation() < 0) {
			importInfoBuild.increment(CheckResult.IllegalUserSigLocation, userImport);
			return false;
		}
		return true;
	}

	//校验协议类型
	public static boolean validProtocolType(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (user.getAccountType() == UserAccountType.OSPF) {
			if (user.getProtocolType() == null) {
				importInfoBuild.increment(CheckResult.UserProtocolTypeIllegal, userImport);
				return false;
			}
		}
		return true;
	}

	//源端口校验
	public static boolean validSrcPort(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (user.getAccountType() == UserAccountType.OSPF) {
			if (!user.getSrcPort().matches(srcPortRegex)) {
				importInfoBuild.increment(CheckResult.SrcPortIllegal, userImport);
				return false;
			}
		}
		return true;
	}

	//校验回调地址
	public static boolean validCallbackAddress(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (user.getProtocolType() == UserProtocolType.SGIP) {
			if (!user.getCallbackAddress().matches(callbackAddressRegex)) {
				importInfoBuild.increment(CheckResult.CallbackAddressIllegal, userImport);
				return false;
			}
		}
		return true;
	}

	//报备签名校验
	public static boolean validCustomerSignature(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (user.getCustomerSignature() < 0) {
			importInfoBuild.increment(CheckResult.CustomerSignatureIllegal, userImport);
			return false;
		}
		if (user.getAccountType() == UserAccountType.OSPF) {
			if (user.getCustomerSignature() != 1) {
				importInfoBuild.increment(CheckResult.CustomerSignatureIllegal, userImport);
				return false;
			}
		}
		return true;
	}

	public static boolean validSendSpeed(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (user.getAccountType() == UserAccountType.OSPF) {
			if (user.getSendSpeed() < 0 || user.getSendSpeed() > 9999) {
				importInfoBuild.increment(CheckResult.SendSpeedIllegal, userImport);
				return false;
			}
		}
		return true;
	}

	public static boolean validLinkNum(User user, UserImport userImport, ImportInfoBuild importInfoBuild) {
		if (user.getAccountType() == UserAccountType.OSPF) {
			if (user.getLinkNum() < 0 || user.getLinkNum() > 99) {
				importInfoBuild.increment(CheckResult.linkNumIllegal, userImport);
				return false;
			}
		}
		return true;
	}

	//校验上行推送地址-----字符长度限制为100；
	public static boolean validPushAddress(ImportInfoBuild importInfoBuild,
									   User user, UserImport userImport){
		if (user.getPushAddress().length() > 100) {
			importInfoBuild.increment(CheckResult.PushAddressTooLong, userImport);
			return false;
		}
		return true;
	}

	//校验状态报告推送地址-----字符长度限制为100；
	public static boolean validReportPushAddress(ImportInfoBuild importInfoBuild,
									   User user, UserImport userImport){
		if (user.getReportPushAddress().length() > 100) {
			importInfoBuild.increment(CheckResult.ReportPushAddressTooLong, userImport);
			return false;
		}
		return true;
	}

	public static void tranUserImportToUser(UserImport userImport, User user) {
		user.setAccountType(ConverterUser.convertAccountType(userImport.getAccountType()));
		user.setUserName(userImport.getUserName());
		user.setLinkMan(userImport.getLinkMan());
		user.setPassword(userImport.getSendPwd());
		user.setMidPassword(userImport.getMidPwd());
		user.setSecondPassword(userImport.getLoginPwd());
		user.setPhone(userImport.getPhone());
		user.setParentIdentify(userImport.getDeptIdentify());
		user.setEnterpriseName(userImport.getDeptName());
		user.setIdentify(userImport.getUserIdentify());
		user.setSignature(userImport.getSignature());
		user.setSigLocation(ConverterUser.convertSigLocation(userImport.getSigLocation()));
		user.setBizIds(userImport.getBizTypeIds());
		user.setProtocolType(ConverterUser.convertProtocolType(userImport.getProtocolType()));
		user.setSrcPort(userImport.getSrcPort());
		user.setCallbackAddress(userImport.getCallbackAddress());
		user.setCustomerSignature(ConverterUser.convertCusSignature(userImport.getCustomerSignature()));
		user.setSendSpeed(ConverterUser.convertSendSpeed(userImport.getSendSpeed()));
		user.setLinkNum(ConverterUser.convertLinkNum(userImport.getLinkNum()));
		user.setRoleNames(userImport.getRoleNames());
		user.setRemark(userImport.getRemark());
		//by jiangziyuan
		user.setUpPush(ConverterUser.convertUpPush(userImport.getUpPush()));
		user.setStatusReportPush(ConverterUser.convertUpPush(userImport.getStatusReportPush()));
		user.setPushAddress(userImport.getPushAddress());
		user.setReportPushAddress(userImport.getReportPushAddress());
	}
}

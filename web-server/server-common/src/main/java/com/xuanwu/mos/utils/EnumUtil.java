package com.xuanwu.mos.utils;

import com.xuanwu.mos.domain.handler.HasIndexValue;

import java.util.HashMap;
import java.util.Map;

public class EnumUtil {

	private static HashMap<String, String> labelMap = new HashMap<String, String>();

	static {
		// System.out.println("EnumUtil.static");
		labelMap.put("UserAuditState.WAIT_VERIFY", "取消审核");
		labelMap.put("UserAuditState.VERIFY_SUCCESS", "通过审核");
		labelMap.put("UserAuditState.VERIFY_FAIL", "未通过审核");
		labelMap.put("UserAuditState.CANCEL_VERIFY", "取消审核");
		labelMap.put("AuditStateEnum.CANCEL", "取消审核");
		labelMap.put("AuditStateEnum.PASS", "通过审核");
		labelMap.put("AuditStateEnum.REJECT", "未通过审核");
		labelMap.put("AuditStateEnum.WAIT", "待审核");
		labelMap.put("BillingType.SYS_DELIVERY", "按信息送达量计费");
		labelMap.put("BillingType.SYS_SEND", "按系统发送量计费");
		labelMap.put("BillingType.USER_SUBMIT", "按用户提交量计费");
		labelMap.put("BizEnablerType.MMS_NOTIFICATION", "彩信通知");
		labelMap.put("BizEnablerType.MMS_SALE", "营销彩信");
		labelMap.put("BizEnablerType.SMS_NOTIFICATION", "短信通知");
		labelMap.put("BizEnablerType.SMS_SALE", "营销短信");
		labelMap.put("BizEnablerType.SMS_VERIFICATION_CODE", "短信验证码");
		labelMap.put("BizEnablerType.VOICE_NOTIFICATION", "语音通知");
		labelMap.put("BizEnablerType.VOICE_VERIFICATION_CODE", "语音验证码");
		labelMap.put("CapitalAccountBookType.CASH", "现金账本");
		labelMap.put("CapitalAccountBookType.COUNT", "条数");
		labelMap.put("CapitalAccountBookType.GIFT", "赠送账本");
		labelMap.put("CapitalAccountBookType.MB", "流量(M)");
		labelMap.put("CapitalAccountBookType.MINUTE", "分钟");
		labelMap.put("CapitalAccountBookType.YUAN", "元");
		labelMap.put("CapitalAccountState.NORMAL", "正常");
		labelMap.put("CapitalAccountState.SUSPEND", "暂停");
		labelMap.put("CapitalAccountState.TERMINATE", "终止");
		labelMap.put("CarrierType.ALL", "三网合一");
		labelMap.put("CarrierType.MOBILE", "移动");
		labelMap.put("CarrierType.TELECOM", "电信小灵通");
		labelMap.put("CarrierType.TELECOM_CDMA", "电信CDMA");
		labelMap.put("CarrierType.UNICOM", "联通");
		labelMap.put("ChargeTransferPlatform.FLOW", "享流量平台");
		labelMap.put("ChargeTransferPlatform.FOUR", "400平台");
		labelMap.put("ChargeTransferPlatform.MOS", "MOS平台");
		labelMap.put("ChargeType.DIRECT_CHARGE", "充值");
		labelMap.put("ChargeType.GIFT", "赠送");
		labelMap.put("ChargeType.ONLINE_CHARGE", "在线充值");
		labelMap.put("ChargeType.REFUND", "退款");
		labelMap.put("ChargeType.TURN_IN", "转入");
		labelMap.put("ChargeType.TURN_OUT", "转出");
		labelMap.put("ChargeType.UNDO", "撤销");
		labelMap.put("OnlineChargeState.FAILURE", "支付失败");
		labelMap.put("OnlineChargeState.SUCCESS", "支付成功");
		labelMap.put("OnlineChargeState.TIMEOUT", "支付超时");
		labelMap.put("OnlineChargeState.UNPAID", "未支付");
		labelMap.put("Origin.OFFLINE", "线下");
		labelMap.put("Origin.ONLINE", "线上");
		labelMap.put("PaymentType.AFTER", "后付费");
		labelMap.put("PaymentType.BEFORE", "预付费");
		labelMap.put("PriceUnit.ITEM", "元/条 ");
		labelMap.put("PriceUnit.MB", "元/MB");
		labelMap.put("PriceUnit.MINUTE", "元/分钟");
		labelMap.put("PricingMode.PRICEUNIT", "按单价");
		labelMap.put("PricingType.DISGUNISHCARRIER", "区分运营商");
		labelMap.put("PricingType.THREENETUNIFORMPRICE", "三网同价");
		labelMap.put("SignType.APP", "应用");
		labelMap.put("SignType.BRAND", "商标证");
		labelMap.put("SignType.OTHER", "其他");
		labelMap.put("SignType.SITES", "网站");

		labelMap.put("SyntheticChargeState.WAIT", "待审核");
		labelMap.put("SyntheticChargeState.PASS", "已通过");
		labelMap.put("SyntheticChargeState.REJECT", "未通过");
		labelMap.put("SyntheticChargeState.SUCCESS", "支付成功");
		labelMap.put("SyntheticChargeState.FAILURE", "支付失败");
		labelMap.put("SyntheticChargeState.TIMEOUT", "支付超时");
		labelMap.put("SyntheticChargeState.UNPAID", "未支付");

		labelMap.put("UserStatus.NOT_ACTIVATED", "未开通");
		labelMap.put("UserStatus.NOT_CERTIFIED", "未认证");
		labelMap.put("UserStatus.VERIFIED", "已认证");
		labelMap.put("VerifyState.VERIFY_FAIL", "不通过");
		labelMap.put("VerifyState.VERIFY_SUCCESS", "已通过");
		labelMap.put("VerifyState.WAIT_VERIFY", "待审核");

		labelMap.put("TicketState.WAIT", "等待发送");
		labelMap.put("TicketState.SUCCESS", "提交成功");
		labelMap.put("TicketState.REJECTED", " 被拒绝");
		labelMap.put("TicketState.INCORRUPT_DATA", "数据格式错误");
		labelMap.put("TicketState.ATTEMPT_FAILED", "多次发送失败");
		labelMap.put("TicketState.INNER_END_FLAG", "帧结束标志");
		labelMap.put("TicketState.SEQUENCE_ERROR", "序列号错误");
		labelMap.put("TicketState.SYS_DENY", "系统拒绝发送");


		labelMap.put("StateReportResult.DELIVERED", "DELIVERED");
		labelMap.put("StateReportResult.EXPIRED", "EXPIRED");
		labelMap.put("StateReportResult.UNDELIVERABLE", "UNDELIVERABLE");
		labelMap.put("StateReportResult.REJECTED", "REJECTED");
		labelMap.put("StateReportResult.UNKNOWN", "UNKNOWN");
		labelMap.put("StateReportResult.DELETED", "DELETED");
		labelMap.put("StateReportResult.WAIT_SEND", "WAIT_SEND");
		labelMap.put("StateReportResult.SEND_DENY", "SEND_DENY");
		labelMap.put("StateReportResult.SUBMIT_FAILD", "SUBMIT_FAILD");

		labelMap.put("MsgPackState.INIT", "待发送");
		labelMap.put("MsgPackState.AUDITING", "待审核");
		labelMap.put("MsgPackState.ABANDON", "被丢弃");
		labelMap.put("MsgPackState.CANCEL", "取消");
		labelMap.put("MsgPackState.SECOND_AUDITING", "二次待审核");
		labelMap.put("MsgPackState.HANDLE", "发送中");
		labelMap.put("MsgPackState.OVER", "发送完成");

		labelMap.put("MsgDeliveryStatus.PENDING", "中");
		labelMap.put("MsgDeliveryStatus.SUCCESS", "成功");
		labelMap.put("MsgDeliveryStatus.FAILURE", "失败");

		labelMap.put("DataScope.PERSONAL", "个人");
		labelMap.put("DataScope.DEPARTMENT", "部门");
		labelMap.put("DataScope.GLOBAL", "全局");
		labelMap.put("DataScope.NONE", "不控制");

		labelMap.put("RoleType.NORMAL_ROLE", "普通角色");
		labelMap.put("RoleType.INIT_ROLE", "初始化角色");
		labelMap.put("RoleType.SUPPER_ROLE", "超级管理员");

		labelMap.put("SendTaskResult.ACTIVE_ACCOUNT_EMAIL_EXCEED_LIMIT", "激活邮件发送次数过多，请明天再试");
		labelMap.put("SendTaskResult.RETRIEVE_PWD_EMAIL_EXCEED_LIMIT","邮件发送次数过多，请明天再试");

		labelMap.put("CertifyState.NOT_CERTIFIED", "未认证");
		labelMap.put("CertifyState.WAIT","待审核");
		labelMap.put("CertifyState.VERIFIED", "已认证");
		labelMap.put("CertifyState.REJECT","审核不通过");
	}

	/**
	 * 数组合并成字符串，并用指定的字符分隔开
	 */
	public static <E extends Enum<E> & HasIndexValue> Map<Integer, E> indexMapping(Class<E> e) {
		Map<Integer, E> valueMapping = new HashMap<>();
		E[] list = e.getEnumConstants();
		for (int i = 0; i < list.length; i++) {
			valueMapping.put(list[i].getIndex(), list[i]);
		}
		return valueMapping;
	}

	public static <E extends Enum<E> & HasIndexValue> Map<Integer, E> indexMapping(E e) {
		return indexMapping(e.getDeclaringClass());
	}

	public static String getEnumLabel(Enum<?> hasIndexValue) {
		return labelMap.get(hasIndexValue.getClass().getSimpleName() + "." + hasIndexValue.name());
	}
	

}

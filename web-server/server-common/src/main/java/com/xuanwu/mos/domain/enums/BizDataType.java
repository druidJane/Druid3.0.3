package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

import java.io.File;

//
public enum BizDataType implements HasIndexValue {

	User(1), Contact(2), SendSms(3), SendBatch(4), SendNumber(5), SendMms(6), Keyword(7), Blacklist(8), MoTicket(
			9), Whitelist(10), NonWhitelist(11), SumDept(12), SumUser(13), SumChannel(14), SumBizType(15), TrendDept(
					16), TrendUser(17), TrendChannel(18), TrendBizType(19), SumEnt(20), SumNumber(21), TrendEnt(
							22), TrendNumber(23), Vote(24), Self(25), SelfMo(26), Lottery(27), Number(28), WeChatFans(
									29), WeChatReport(30), SendLimit(31), CopyDataForRecyNumber(32), VoteStatistics(
											33), WlotteryData(34), RegionSegmt(35), ChargeAccount(36), WhitePhone(38),DeptSendStat(39),UsersSendStat(40),
											BizTypeSendStat(41),BillCountsStat(42),UserSendDetailStat(43),BillCountDetailStat(44),Tmp(98), Other(99);

	private int index;

	private BizDataType(int index) {
		this.index = index;
	}

	public static BizDataType getType(int index) {
		for (BizDataType type : BizDataType.values()) {
			if (type.getIndex() == index)
				return type;
		}
		return Other;
	}

	@Override
	public int getIndex() {
		return index;
	}

	public static String getDirByType(String parentDir, BizDataType type) {
		StringBuilder sb = new StringBuilder();
		sb.append(parentDir);
		sb.append(type.name().toLowerCase());
		sb.append(File.separator);
		return sb.toString();
	}

}

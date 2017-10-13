/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.protobuf.mto.MTOResponse.ChannelInfo;
import com.xuanwu.msggate.common.util.XmlUtil;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Channel meta information
 * 
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-11
 * @Version 1.0.0
 */
public class ChannelBuild {

	public static ChannelInfo parseChannel(CarrierChannel channel) {
		ChannelInfo.Builder builder = ChannelInfo.newBuilder();
		builder.setHostname(channel.getHostname());
		builder.setPort(channel.getPort());
		builder.setAccount(channel.getAccount());
		builder.setPassword(channel.getPassword());
		builder.setIssignal(channel.isSignal());
		builder.setIsLongSignal(channel.isLongSignal());
		builder.setIsEraseSignal(channel.isEraseSignal());
		builder.setSignal(channel.getSignalInfo() == null ? "" : channel.getSignalInfo());
		if (channel.getCorpID() != null)
			builder.setCorpID(channel.getCorpID());
		builder.setMms(channel.isMms());
		builder.setSms(channel.isSms());
		builder.setLongSms(channel.isLongSms());
		builder.setWappush(channel.isWappush());
        builder.setVoiceNotice(channel.isVoiceNotice());
        builder.setVoiceCode(channel.isVoiceCode());
		builder.setStateReport(channel.isStateReport());
		builder.setMassCommit(channel.isMassCommit());
		if (channel.getProtoVersion() != null)
			builder.setProtoVersion(channel.getProtoVersion());
		builder.setMaxLength(channel.getMaxLength());
		builder.setExtendLength(channel.getExtendSpace());
		builder.setExtendNumLength(channel.getExtendNumLength());
		builder.setMoWaitTime(channel.getMoWaitTime());
		builder.setParameters(XmlUtil.toXML(channel.getParameters()));
		builder.setChannelNum(channel.getChannelNum());
		if(channel.getChannelShortNum() != null){
			builder.setChannelShortNum(channel.getChannelShortNum());
		}
		return builder.build();
	}

	@SuppressWarnings("unchecked")
	public static CarrierChannel parseFrom(ChannelInfo builder) {
		CarrierChannel channel = new CarrierChannel();
		channel.setHostname(builder.getHostname());
		channel.setPort(builder.getPort());
		channel.setAccount(builder.getAccount());
		channel.setPassword(builder.getPassword());
		channel.setSignal(builder.getIssignal());
		channel.setEraseSignal(builder.getIsEraseSignal());
		channel.setSignalInfo(builder.getSignal());
		channel.setLongSignal(builder.getIsLongSignal());
		channel.setMms(builder.getMms());
		channel.setSms(builder.getSms());
        channel.setVoiceNotice(builder.getVoiceNotice());
        channel.setVoiceCode(builder.getVoiceCode());
		channel.setCorpID(builder.getCorpID());
		channel.setLongSms(builder.getLongSms());
		channel.setWappush(builder.getWappush());
		channel.setStateReport(builder.getStateReport());
		channel.setMassCommit(builder.getMassCommit());
		channel.setProtoVersion(builder.getProtoVersion());
		channel.setMaxLength(builder.getMaxLength());
		channel.setMoWaitTime(builder.getMoWaitTime());
		channel.setExtendSpace(builder.getExtendLength());
		channel.setExtendNumLength(builder.getExtendNumLength());
		channel.setChannelNum(builder.getChannelNum());
		channel.setChannelShortNum(builder.getChannelShortNum());
		if (StringUtils.isNotEmpty(builder.getParameters())) {
			channel.setParameters((Map<String, Object>) XmlUtil.fromXML(builder
					.getParameters()));
		}
		return channel;
	}
}

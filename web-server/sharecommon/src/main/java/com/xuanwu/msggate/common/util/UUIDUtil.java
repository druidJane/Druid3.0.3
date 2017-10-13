/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.util;

import java.util.UUID;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-20
 * @Version 1.0.0
 */
public class UUIDUtil {
	public static com.xuanwu.msggate.common.protobuf.CommonItem.UUID.Builder tranUUID2Builder(
			UUID uid) {
		if (uid == null)
			return null;
		com.xuanwu.msggate.common.protobuf.CommonItem.UUID.Builder builder = com.xuanwu.msggate.common.protobuf.CommonItem.UUID
				.newBuilder();
		builder.setMostsigbits(uid.getMostSignificantBits()).setLeastsigbits(
				uid.getLeastSignificantBits());
		return builder;
	}

	public static UUID tranBuilder2UUID(
			com.xuanwu.msggate.common.protobuf.CommonItem.UUID bUid) {
		if (bUid == null)
			return null;
		return new UUID(bUid.getMostsigbits(), bUid.getLeastsigbits());
	}
	
	public static boolean isEmpty(UUID uuid) {
		UUID empty = new UUID(0, 0);
		return uuid.equals(empty);
	}
	 
}

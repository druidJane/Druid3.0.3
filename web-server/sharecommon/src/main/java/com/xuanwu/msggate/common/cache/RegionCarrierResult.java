/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache;

import com.xuanwu.msggate.common.sbi.entity.Carrier;
import com.xuanwu.msggate.common.sbi.entity.Region;

/**
 * Region and carrier result
 * 
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-8-8
 * @Version 1.0.0
 */
public class RegionCarrierResult {
	/**
	 * Region
	 */
	private Region region;
	/**
	 * Carrier
	 */
	private Carrier carrier;

	public RegionCarrierResult(Region region, Carrier carrier) {
		this.region = region;
		this.carrier = carrier;
	}

	public Region getRegion() {
		return region;
	}

	public Carrier getCarrier() {
		return carrier;
	}
}

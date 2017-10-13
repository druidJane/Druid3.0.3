/*   
* Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
*             All rights reserved                         
*/
package com.xuanwu.msggate.common.sbi.entity;

import java.util.UUID;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-10-11
 * @Version 1.0.0
 */
public class MTResp {
	/**
	 * 对应于下行包的uuid
	 */
	private UUID uuid;

	/**
	 * 网关返回的结果类型
	 */
	private MTResult result = MTResult.OTHER_ERROR;
	
	/**
	 * 结果说明
	 */
	private String message;
	
	/**
	 * 扩展属性
	 */
	private String attributes;

	/**
	 * 取得下行包的uuid
	 * @return 下行包的uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * 取得网关返回的结果类型
	 * @return 结果类型
	 */
	public MTResult getResult() {
		return result;
	}

	public void setResult(MTResult result) {
		this.result = result;
	}

	/**
	 * 取得网关返回的结果说明
	 * @return 结果说明
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 取得扩展属性
	 * @return 扩展属性
	 */
	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	
	public static MTResp build(MTResult result) {
		return build(result, null);
	}
	
	public static MTResp build(MTResult result, String msg) {
		MTResp resp = new MTResp();
		resp.setResult(result);
		resp.setMessage(msg);
		return resp;
	}
}

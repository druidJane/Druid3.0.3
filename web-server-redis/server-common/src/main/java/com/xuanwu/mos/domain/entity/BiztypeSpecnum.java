/*   
* Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
*             All rights reserved                         
*/
package com.xuanwu.mos.domain.entity;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author <a href="liangyuanming@139130.net">Liang YuanMing</a>
 * @Date 2012-11-23
 * @Version 1.0.0
 */
public class BiztypeSpecnum {
	private int id;
	private int enterpriseSpecnumBindId;
	private int biztypeId;
	private boolean isRemove;
	private int carrierId;
	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id 要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return enterpriseSpecnumBindId
	 */
	public int getEnterpriseSpecnumBindId() {
		return enterpriseSpecnumBindId;
	}
	/**
	 * @param enterpriseSpecnumBindId 要设置的 enterpriseSpecnumBindId
	 */
	public void setEnterpriseSpecnumBindId(int enterpriseSpecnumBindId) {
		this.enterpriseSpecnumBindId = enterpriseSpecnumBindId;
	}
	/**
	 * @return biztypeId
	 */
	public int getBiztypeId() {
		return biztypeId;
	}
	/**
	 * @param biztypeId 要设置的 biztypeId
	 */
	public void setBiztypeId(int biztypeId) {
		this.biztypeId = biztypeId;
	}
	/**
	 * @return isRemove
	 */
	public boolean isRemove() {
		return isRemove;
	}
	/**
	 * @param isRemove 要设置的 isRemove
	 */
	public void setRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}
	/**
	 * @return carrierId
	 */
	public int getCarrierId() {
		return carrierId;
	}
	/**
	 * @param carrierId 要设置的 carrierId
	 */
	public void setCarrierId(int carrierId) {
		this.carrierId = carrierId;
	}
}

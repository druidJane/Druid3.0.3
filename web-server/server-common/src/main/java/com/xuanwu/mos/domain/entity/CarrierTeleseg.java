package com.xuanwu.mos.domain.entity;


import com.xuanwu.mos.domain.AbstractEntity;

import java.util.Date;

/**
 * 运营商号码段
 * 
 * @author 林泽强
 * 
 */
public class CarrierTeleseg extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	/**
	 * 运营商号码段ID
	 */
	private Integer id;
	/**
	 * 运营商
	 */
	private Carrier carrier;
	/**
	 * 排序值
	 */
	private Integer sort;
	/**
	 * 是否显示
	 */
	private Integer showed;
	/**
	 * 号码段
	 */
	private String phone;
	/**
	 * 最后处理时间
	 */
	private Date handleTime;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getShowed() {
		return showed;
	}

	public void setShowed(Integer showed) {
		this.showed = showed;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}

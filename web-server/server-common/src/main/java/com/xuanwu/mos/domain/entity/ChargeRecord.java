package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.ChargeWay;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 充值记录
 * @Data 2017-4-7
 * @Version 1.0.0
 */
public class ChargeRecord extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/* 计费账户ID */
	private int capitalAccountId;
	/* 充值金额 */
	private BigDecimal chargeAmount = BigDecimal.ZERO;
	/* 充值时间 */
	private Date chargeTime;
	/* 备注 */
	private String remark;
	/* 自动充值时间 */
	private String autoChargeTime;
	/* 充值用户 */
	private String chargeUserName;
	/* 账户名称 */
	private String accountName;
	/* 充值方式 */
	private ChargeWay chargeWay;
	/* 充值方式 页面展示使用 */
	private String showChargeWay;
	/* 是否充值成功 */
	private boolean chargeState;
	/* 页面显示 */
	private String showChargeState;

	public String getShowChargeState() {
		if (chargeState) {
			return "充值成功";
		}
		return "充值失败";
	}

	public void setShowChargeState(String showChargeState) {
		this.showChargeState = showChargeState;
	}

	public boolean isChargeState() {
		return chargeState;
	}

	public void setChargeState(boolean chargeState) {
		this.chargeState = chargeState;
	}

	public String getShowChargeWay() {
		switch (chargeWay) {
			case HAND_CHARGE:
				return "手动充值";
			case AUTO_CHARGE:
				return "自动充值";
			default:
				return null;
		}
	}

	public void setShowChargeWay(String showChargeWay) {
		this.showChargeWay = showChargeWay;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getCapitalAccountId() {
		return capitalAccountId;
	}

	public void setCapitalAccountId(int capitalAccountId) {
		this.capitalAccountId = capitalAccountId;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public Date getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAutoChargeTime() {
		return autoChargeTime;
	}

	public void setAutoChargeTime(String autoChargeTime) {
		this.autoChargeTime = autoChargeTime;
	}

	public String getChargeUserName() {
		return chargeUserName;
	}

	public void setChargeUserName(String chargeUserName) {
		this.chargeUserName = chargeUserName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public ChargeWay getChargeWay() {
		return chargeWay;
	}

	public void setChargeWay(ChargeWay chargeWay) {
		this.chargeWay = chargeWay;
	}
}

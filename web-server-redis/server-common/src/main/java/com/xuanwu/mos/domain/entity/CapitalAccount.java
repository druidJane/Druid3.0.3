package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.ChargeWay;
import com.xuanwu.mos.domain.enums.DeductType;
import com.xuanwu.mos.domain.enums.PayingWay;
import com.xuanwu.mos.domain.enums.UserState;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 计费账户
 * @Data 2017-3-31
 * @Version 1.0.0
 */
public class CapitalAccount extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;

	/*充值日期 每月的第几日*/
	private int chargeDay;
	/*付费方式*/
	private PayingWay payingWay;
	/* 付费方式 页面展示使用 */
	private String showPayingWay;
	/*企业Id*/
	private int enterpriseId;
	/*账号名称*/
	private String accountName;
	/*充值方式*/
	private ChargeWay chargeWay;
	/* 充值方式 页面展示使用 */
	private String showChargeWay;
	/*0-默认, 1-根据企业扣费单价扣费(企业怎么扣，子账户就怎么扣)*/
	private DeductType deductType;
	/* 单价类型 页面显示使用 */
	private String showDeductType;
	/*最后修改人Id*/
	private int userId;
	/*最后修改时间*/
	private Date dateTime;

	private BigDecimal smsPrice = BigDecimal.ZERO;
	private BigDecimal smsPriceYD = BigDecimal.ZERO;
	private BigDecimal smsPriceLT = BigDecimal.ZERO;
	private BigDecimal smsPriceXLT = BigDecimal.ZERO;
	private BigDecimal smsPriceCDMA = BigDecimal.ZERO;

	private BigDecimal mmsPrice = BigDecimal.ZERO;
	private BigDecimal mmsPriceYD = BigDecimal.ZERO;
	private BigDecimal mmsPriceLT = BigDecimal.ZERO;
	private BigDecimal mmsPriceXLT = BigDecimal.ZERO;
	private BigDecimal mmsPriceCDMA = BigDecimal.ZERO;

	/*余额*/
	private BigDecimal balance = BigDecimal.ZERO;
	/*自动充值金额*/
	private BigDecimal autoChargeMoney = BigDecimal.ZERO;
	/*差异金额*/
	private BigDecimal differenceBalance = BigDecimal.ZERO;
	/*子账户金额*/
	private BigDecimal childBalance = BigDecimal.ZERO;
	/*充值金额*/
	private BigDecimal chargeMoney = BigDecimal.ZERO;
	/*账号状态*/
	private UserState userState;
	private String remark;
	/*父账户Id*/
	private int parentId;
	/*充值比例*/
	private double chargeRatio;
	/*是否自动充值*/
	private boolean autoChargeFlag;
	/*自动充值日期*/
	private int autoChargeDate;
	/*是否选中，页面展现*/
	private boolean checked;
	private String monthlyStatStart;
	private int state; // 账户状态  正常(0), 暂停(1), 终止(2); 0,1可以互转，从0或1转到2后不能再转

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	//by jiangziyuan
	private double restSendNum;//预算余额可发送短信多少条数
	private Date chargeTime;

	public Date getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getMonthlyStatStart() {
		return monthlyStatStart;
	}

	public void setMonthlyStatStart(String monthlyStatStart) {
		this.monthlyStatStart = monthlyStatStart;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getShowDeductType() {
		if (deductType != null) {
			switch (deductType) {
				case ENTERPRISE_PRICE:
					return "按企业单价";
				case CUSTOM_PRICE:
					return "自定义单价";
				default:
					return null;
			}
		}
		return null;
	}

	public void setShowDeductType(String showDeductType) {
		this.showDeductType = showDeductType;
	}

	public String getShowPayingWay() {
		if(null == payingWay)return "";
		switch (payingWay) {
			case PRE_PAID:
				return "预付费";
			case AFTER_PAID:
				return "后付费";
			default:
				return "";
		}
	}

	public String getShowChargeWay() {
		if(null == payingWay)return "";
		switch (chargeWay) {
			case HAND_CHARGE:
				return "手动充值";
			case AUTO_CHARGE:
				return "自动充值";
			default:
				return null;
		}
	}

	public void setShowPayingWay(String showPayingWay) {
		this.showPayingWay = showPayingWay;
	}

	public void setShowChargeWay(String showChargeWay) {
		this.showChargeWay = showChargeWay;
	}

	public int getChargeDay() {
		return chargeDay;
	}

	public void setChargeDay(int chargeDay) {
		this.chargeDay = chargeDay;
	}

	public PayingWay getPayingWay() {
		return payingWay;
	}

	public void setPayingWay(PayingWay payingWay) {
		this.payingWay = payingWay;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public BigDecimal getAutoChargeMoney() {
		return autoChargeMoney;
	}

	public void setAutoChargeMoney(BigDecimal autoChargeMoney) {
		this.autoChargeMoney = autoChargeMoney;
	}

	public DeductType getDeductType() {
		return deductType;
	}

	public void setDeductType(DeductType deductType) {
		this.deductType = deductType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public BigDecimal getSmsPrice() {
		return smsPrice;
	}

	public void setSmsPrice(BigDecimal smsPrice) {
		this.smsPrice = smsPrice;
	}

	public BigDecimal getSmsPriceYD() {
		return smsPriceYD;
	}

	public void setSmsPriceYD(BigDecimal smsPriceYD) {
		this.smsPriceYD = smsPriceYD;
	}

	public BigDecimal getSmsPriceLT() {
		return smsPriceLT;
	}

	public void setSmsPriceLT(BigDecimal smsPriceLT) {
		this.smsPriceLT = smsPriceLT;
	}

	public BigDecimal getSmsPriceXLT() {
		return smsPriceXLT;
	}

	public void setSmsPriceXLT(BigDecimal smsPriceXLT) {
		this.smsPriceXLT = smsPriceXLT;
	}

	public BigDecimal getSmsPriceCDMA() {
		return smsPriceCDMA;
	}

	public void setSmsPriceCDMA(BigDecimal smsPriceCDMA) {
		this.smsPriceCDMA = smsPriceCDMA;
	}

	public BigDecimal getMmsPrice() {
		return mmsPrice;
	}

	public void setMmsPrice(BigDecimal mmsPrice) {
		this.mmsPrice = mmsPrice;
	}

	public BigDecimal getMmsPriceYD() {
		return mmsPriceYD;
	}

	public void setMmsPriceYD(BigDecimal mmsPriceYD) {
		this.mmsPriceYD = mmsPriceYD;
	}

	public BigDecimal getMmsPriceLT() {
		return mmsPriceLT;
	}

	public void setMmsPriceLT(BigDecimal mmsPriceLT) {
		this.mmsPriceLT = mmsPriceLT;
	}

	public BigDecimal getMmsPriceXLT() {
		return mmsPriceXLT;
	}

	public void setMmsPriceXLT(BigDecimal mmsPriceXLT) {
		this.mmsPriceXLT = mmsPriceXLT;
	}

	public BigDecimal getMmsPriceCDMA() {
		return mmsPriceCDMA;
	}

	public void setMmsPriceCDMA(BigDecimal mmsPriceCDMA) {
		this.mmsPriceCDMA = mmsPriceCDMA;
	}

	public BigDecimal getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public double getChargeRatio() {
		return chargeRatio;
	}

	public void setChargeRatio(double chargeRatio) {
		this.chargeRatio = chargeRatio;
	}

	public boolean isAutoChargeFlag() {
		return autoChargeFlag;
	}

	public void setAutoChargeFlag(boolean autoChargeFlag) {
		this.autoChargeFlag = autoChargeFlag;
	}

	public BigDecimal getDifferenceBalance() {
		return differenceBalance;
	}

	public void setDifferenceBalance(BigDecimal differenceBalance) {
		this.differenceBalance = differenceBalance;
	}

	public BigDecimal getChildBalance() {
		return childBalance;
	}

	public void setChildBalance(BigDecimal childBalance) {
		this.childBalance = childBalance;
	}

	public int getAutoChargeDate() {
		return autoChargeDate;
	}

	public void setAutoChargeDate(int autoChargeDate) {
		this.autoChargeDate = autoChargeDate;
	}

	public double getRestSendNum() {
		return restSendNum;
	}

	public void setRestSendNum(double restSendNum) {
		this.restSendNum = restSendNum;
	}
}

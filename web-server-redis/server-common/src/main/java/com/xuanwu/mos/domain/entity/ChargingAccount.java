package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.utils.StringUtil;

import java.util.Date;

/**
 * 计费账户实体类
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public class ChargingAccount extends BaseEntity{
    private Integer id;
    /*余额*/
    private double balance;
    /*充值日期*/
    private Date chargeDate;
    /*付费方式*/
    private int payingWay;
    private String payingWayStr;
    private int enterpriseId;
    /*账号名称*/
    private String accountName;
    /*充值方式*/
    private int chargeWay;
    private String chargeWayStr;
    /*自动充值金额*/
    private double autoChargeMoney;
    private String autoChargeMoneyStr;
    /*0-默认, 1-根据企业扣费单价扣费(企业怎么扣，子账户就怎么扣)*/
    private int deductType;
    private String deductTypeStr;
    private double giftAmount;
    /*最后修改人Id*/
    private int userId;
    /*最后修改时间*/
    private Date dateTime;
    private double smsPrice;
    private double smsPriceYD;
    private double smsPriceLT;
    private double smsPriceXLT;
    private double smsPriceCDMA;
    private double mmsPrice;
    private double mmsPriceYD;
    private double mmsPriceLT;
    private double mmsPriceXLT;
    private double mmsPriceCDMA;
    /*充值金额*/
    private double chargeMoney;
    /*账号状态*/
    private int accountState;
    private String remark;
    private int parentId;
    /*充值比例*/
    private double chargeRatio;
    private ChargeRecord chargingRecord;
    private boolean autoChargeFlag;
    /*差异金额*/
    private double differenceBalance;
    /*子账户金额*/
    private double childBalance;
    private int reChargeDate;

    public int getReChargeDate() {
        return reChargeDate;
    }

    public void setReChargeDate(int reChargeDate) {
        this.reChargeDate = reChargeDate;
    }

    public double getSmsPriceYD() {
        return smsPriceYD;
    }

    public void setSmsPriceYD(double smsPriceYD) {
        this.smsPriceYD = smsPriceYD;
    }

    public double getSmsPriceLT() {
        return smsPriceLT;
    }

    public void setSmsPriceLT(double smsPriceLT) {
        this.smsPriceLT = smsPriceLT;
    }

    public double getSmsPriceXLT() {
        return smsPriceXLT;
    }

    public void setSmsPriceXLT(double smsPriceXLT) {
        this.smsPriceXLT = smsPriceXLT;
    }

    public double getSmsPriceCDMA() {
        return smsPriceCDMA;
    }

    public void setSmsPriceCDMA(double smsPriceCDMA) {
        this.smsPriceCDMA = smsPriceCDMA;
    }

    public double getMmsPriceYD() {
        return mmsPriceYD;
    }

    public void setMmsPriceYD(double mmsPriceYD) {
        this.mmsPriceYD = mmsPriceYD;
    }

    public double getMmsPriceLT() {
        return mmsPriceLT;
    }

    public void setMmsPriceLT(double mmsPriceLT) {
        this.mmsPriceLT = mmsPriceLT;
    }

    public double getMmsPriceXLT() {
        return mmsPriceXLT;
    }

    public void setMmsPriceXLT(double mmsPriceXLT) {
        this.mmsPriceXLT = mmsPriceXLT;
    }

    public double getMmsPriceCDMA() {
        return mmsPriceCDMA;
    }

    public void setMmsPriceCDMA(double mmsPriceCDMA) {
        this.mmsPriceCDMA = mmsPriceCDMA;
    }

    public String getAutoChargeMoneyStr() {
        return StringUtil.delZero(autoChargeMoney+"");
    }

    public double getChildBalance() {
        return childBalance;
    }

    public void setChildBalance(double childBalance) {
        this.childBalance = childBalance;
    }

    public double getDifferenceBalance() {
        return differenceBalance;
    }

    public void setDifferenceBalance(double differenceBalance) {
        this.differenceBalance = differenceBalance;
    }

    public boolean getAutoChargeFlag() {
        return autoChargeFlag;
    }

    public void setAutoChargeFlag(boolean autoChargeFlag) {
        this.autoChargeFlag = autoChargeFlag;
    }

    public double getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(double giftAmount) {
        this.giftAmount = giftAmount;
    }

    public double getChargeRatio() {
        return chargeRatio;
    }

    public void setChargeRatio(double chargeRatio) {
        this.chargeRatio = chargeRatio;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ChargeRecord getChargingRecord() {
        return chargingRecord;
    }

    public void setChargingRecord(ChargeRecord chargingRecord) {
        this.chargingRecord = chargingRecord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }

    public String getPayingWayStr() {
        switch (this.payingWay) {
            case 0: return "预付款";
            case 1: return "后付款";
            default: return "";
        }
    }

    public String getChargeWayStr() {
        switch (this.chargeWay)
        {
            case 0: return "手动充值";
            case 1: return "每月自动充值";
            default: return "";
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(Date chargeDate) {
        this.chargeDate = chargeDate;
    }

    public int getPayingWay() {
        return payingWay;
    }

    public void setPayingWay(int payingWay) {
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

    public double getAutoChargeMoney() {
        return autoChargeMoney;
    }

    public void setAutoChargeMoney(double autoChargeMoney) {
        this.autoChargeMoney = autoChargeMoney;
    }

    public int getDeductType() {
        return deductType;
    }

    public void setDeductType(int deductType) {
        this.deductType = deductType;
    }

    public String getDeductTypeStr() {
        return this.deductType == 0 ? "自定义单价" : "按企业单价";
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

    public double getSmsPrice() {
        return smsPrice;
    }

    public void setSmsPrice(double smsPrice) {
        this.smsPrice = smsPrice;
    }

    public double getMmsPrice() {
        return mmsPrice;
    }

    public void setMmsPrice(double mmsPrice) {
        this.mmsPrice = mmsPrice;
    }

    public double getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(double chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public int getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(int chargeWay) {
        this.chargeWay = chargeWay;
    }

    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id);
        sb.append(",\"accountName\":\"").append(accountName).append("\"");
        sb.append(",\"payingWayStr\":\"").append(getPayingWayStr()).append("\"");
        sb.append(",\"balance\":").append(balance);
        sb.append(",\"chargeWayStr\":\"").append(getChargeWayStr()).append("\"");
        sb.append(",\"chargeWay\":").append(chargeWay);
        sb.append(",\"remark\":\"").append(chargingRecord.getRemark()).append("\"");
        sb.append(",\"autoChargeTime\":\"").append(chargingRecord.getAutoChargeTime()).append("\"");
        sb.append(",\"deductTypeStr\":\"").append(getDeductTypeStr()).append("\"");
        sb.append(",\"autoChargeMoneyStr\":").append(autoChargeMoneyStr);
        sb.append(",\"smsPrice\":").append(smsPrice);
        sb.append(",\"mmsPrice\":").append(mmsPrice);
        sb.append('}');
        return sb.toString();
    }
}

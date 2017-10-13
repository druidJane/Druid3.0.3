package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.Entity;

import java.util.Date;

/**
 * 计数账号
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public class CountAccount implements Entity {

    private int id;
    private String name;
    private int parentId;
    private int chargeType;// 充值类型
    private int smsLimit;// 短信限制量，0－不限制，>0－为限制数
    private int smsRemain;// 短信剩余量
    private int mmsLimit;// 彩信限制量，0－不限制，>0－为限制数
    private int mmsRemain;// 彩信剩余量
    private Date autoChargeTime;// 最后自动充值时间
    private int modifyUser;// 修改用户，新增同样
    private Date modifyTime;// 修改时间，新增同样
    private int limitType;// 限制方式：1－短彩不区分限制，统一用短信限制；2－短彩区分限制(这里统一使用2)
    private int smsHasSend;//每日短信已发送量
    private int mmsHasSend;//每日彩信已发送量

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public int getSmsLimit() {
        return smsLimit;
    }

    public void setSmsLimit(int smsLimit) {
        this.smsLimit = smsLimit;
    }

    public int getSmsRemain() {
        return smsRemain;
    }

    public void setSmsRemain(int smsRemain) {
        this.smsRemain = smsRemain;
    }

    public int getMmsLimit() {
        return mmsLimit;
    }

    public void setMmsLimit(int mmsLimit) {
        this.mmsLimit = mmsLimit;
    }

    public int getMmsRemain() {
        return mmsRemain;
    }

    public void setMmsRemain(int mmsRemain) {
        this.mmsRemain = mmsRemain;
    }

    public Date getAutoChargeTime() {
        return autoChargeTime;
    }

    public void setAutoChargeTime(Date autoChargeTime) {
        this.autoChargeTime = autoChargeTime;
    }

    public int getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(int modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getLimitType() {
        return limitType;
    }

    public void setLimitType(int limitType) {
        this.limitType = limitType;
    }

    public int getSmsHasSend() {
        return smsHasSend;
    }

    public void setSmsHasSend(int smsHasSend) {
        this.smsHasSend = smsHasSend;
    }

    public int getMmsHasSend() {
        return mmsHasSend;
    }

    public void setMmsHasSend(int mmsHasSend) {
        this.mmsHasSend = mmsHasSend;
    }
}

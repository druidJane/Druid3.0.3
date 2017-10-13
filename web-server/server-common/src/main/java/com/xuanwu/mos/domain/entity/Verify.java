package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.utils.DateUtil;

import java.util.Date;

/**
 * 审核信息
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public class Verify extends BaseEntity {
    private Integer id;
    private String batchId;
    private Date verifyTime;
    private int verifyUserId;
    private String verifyUserName;
    private int verifyState;// 0:通过,1:不通过
    private String verifyStateStr;
    private String verifyRemark;
    private String verifyTimeStr;

    public Verify() {
    }

    public Verify(String batchId, Date verifyTime, int verifyUserId,
                  int verifyState, String verifyRemark) {
        this.batchId = batchId;
        this.verifyTime = verifyTime;
        this.verifyUserId = verifyUserId;
        this.verifyState = verifyState;
        this.verifyRemark = verifyRemark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public int getVerifyUserId() {
        return verifyUserId;
    }

    public void setVerifyUserId(int verifyUserId) {
        this.verifyUserId = verifyUserId;
    }

    public String getVerifyUserName() {
        return verifyUserName;
    }

    public void setVerifyUserName(String verifyUserName) {
        this.verifyUserName = verifyUserName;
    }

    public int getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(int verifyState) {
        this.verifyState = verifyState;
    }

    public String getVerifyStateStr() {
        if (verifyStateStr == null) {
            switch (verifyState) {
                case 0:
                    return "通过";

                default:
                    return "不通过";
            }
        }
        return verifyStateStr;
    }

    public void setVerifyStateStr(String verifyStateStr) {

        this.verifyStateStr = verifyStateStr;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public String getVerifyTimeStr() {
        verifyTimeStr = DateUtil.format(verifyTime,
                DateUtil.DateTimeType.DateTime);
        return verifyTimeStr;
    }

    public void setVerifyTimeStr(String verifyTimeStr) {
        this.verifyTimeStr = verifyTimeStr;
    }

    @Override
    public String toJSON() {
        return null;
    }
}

package com.xuanwu.mos.domain.entity;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
public class EnterpriseSpecnumBind {
    private int id;
    private int enterpriseId;
    private int specnumId;
    private float price;
    private int msgType;
    private boolean isSpecified;
    private String remark;
    private int state;
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
     * @return enterpriseId
     */
    public int getEnterpriseId() {
        return enterpriseId;
    }
    /**
     * @param enterpriseId 要设置的 enterpriseId
     */
    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    /**
     * @return specnumId
     */
    public int getSpecnumId() {
        return specnumId;
    }
    /**
     * @param specnumId 要设置的 specnumId
     */
    public void setSpecnumId(int specnumId) {
        this.specnumId = specnumId;
    }
    /**
     * @return price
     */
    public float getPrice() {
        return price;
    }
    /**
     * @param price 要设置的 price
     */
    public void setPrice(float price) {
        this.price = price;
    }
    /**
     * @return msgType
     */
    public int getMsgType() {
        return msgType;
    }
    /**
     * @param msgType 要设置的 msgType
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    /**
     * @return isSpecified
     */
    public boolean isSpecified() {
        return isSpecified;
    }
    /**
     * @param isSpecified 要设置的 isSpecified
     */
    public void setSpecified(boolean isSpecified) {
        this.isSpecified = isSpecified;
    }
    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }
    /**
     * @param remark 要设置的 remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * @return state
     */
    public int getState() {
        return state;
    }
    /**
     * @param state 要设置的 state
     */
    public void setState(int state) {
        this.state = state;
    }
}

package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang.Ziyuan on 2017/3/23.
 */
public class BizType extends AbstractEntity {

    private int id;

    private int userId;

    /** 是否可扩展标志 */
    private boolean extendFlag;

    /** 业务类型优先级 */
    private int priority;

    /** 业务类型名称 */
    private String name;

    /** NORMAL(0), SUSPEND(1), TERMINATE(2); 0,1可以互转，从0或1转到2后不能再转 */
    private int state;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /** 企业ID */
    private int enterpriseId;

    /** 0：企业默认类型，不可以删除；1：普通，可以删除 */
    private int type;

    /** 10：用户默认业务类型 */
    private int busType;

    /** 备注 */
    private String remark;

    /** 是否绑定 */
    private boolean bound;

    /** 单个彩信的最大字节长度 */
    private int mmsMaxLength;

    /** 发送速度 */
    private int speed;

    /**速度模式*/
    private int speedMode;

    @SuppressWarnings("unused")
    private String priorityStr;

    private List<String> endTimes;

    /* 绑定的端口可扩展长度 */
    private int numExtendSize;

    public int getNumExtendSize() {
        return numExtendSize;
    }

    public void setNumExtendSize(int numExtendSize) {
        this.numExtendSize = numExtendSize;
    }

    public static List<String> getStartTimeString(int hour){
        List<String> startTimeString = new ArrayList<String>();
        for(int i=hour;i<24;i++){
            String h = String.valueOf(i);
            if(h.length()<2)
                h = "0"+h;
            startTimeString.add(h+":00:00");
        }
        return startTimeString;
    }

    public static List<String> getEndTimeString(int hour){
        List<String> endTimeString = new ArrayList<String>();
        for(int i=23;i>=hour;i--){
            String h = String.valueOf(i);
            if(h.length()<2)
                h = "0"+h;
            endTimeString.add(h+":59:59");
        }
        return endTimeString;
    }
    /**
     * 业务类型绑定的通道
     */
    private List<CarrierChannel> carrierChannel;
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isExtendFlag() {
        return extendFlag;
    }

    public void setExtendFlag(boolean extendFlag) {
        this.extendFlag = extendFlag;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getBound() {
        return bound;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public String getPriorityStr(){
        return priorityStr = getPriorityString(this.priority);
    }
    /**
     * @param carrierChannel 要设置的 carrierChannel
     */
    public void setCarrierChannel(List<CarrierChannel> carrierChannel) {
        this.carrierChannel = carrierChannel;
    }

    /**
     * @return carrierChannel
     */
    public List<CarrierChannel> getCarrierChannel() {
        return carrierChannel;
    }

    public int getBusType() {
        return busType;
    }

    public void setBusType(int busType) {
        this.busType = busType;
    }

    public int getMmsMaxLength() {
        return mmsMaxLength;
    }

    public void setMmsMaxLength(int mmsMaxLength) {
        this.mmsMaxLength = mmsMaxLength;
    }



    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public int getSpeedMode() {
        return speedMode;
    }

    public void setSpeedMode(int speedMode) {
        this.speedMode = speedMode;
    }

    /**
     * @return endTimes
     */
    public List<String> getEndTimes() {
        return endTimes;
    }

    /**
     * @param endTimes 要设置的 endTimes
     */
    public void setEndTimes(List<String> endTimes) {
        this.endTimes = endTimes;
    }

    public String toJSONSimple(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id);
        sb.append(",\"name\":\"").append(StringUtil.fixJsonStr(name)).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String toJSON(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id);
        sb.append(",\"name\":\"").append(StringUtil.fixJsonStr(name)).append('\"');
        sb.append(",\"type\":").append(type);
        sb.append(",\"busType\":").append(busType);
        sb.append(",\"priority\":").append(priority);
        sb.append(",\"priorityStr\":\"").append(getPriorityString(priority)).append('\"');
        sb.append(",\"extend\":").append(extendFlag ? "true" : "false");
        sb.append(",\"extendFlag\":\"").append(extendFlag ? "是" : "否").append('\"');
        sb.append(",\"startTime\":\"").append(startTime).append('\"');
        sb.append(",\"endTime\":\"").append(endTime).append('\"');
        sb.append(",\"mmsMaxLength\":").append(mmsMaxLength);
        sb.append(",\"speed\":").append(speed);
        sb.append(",\"speedMode\":").append(speedMode);
        sb.append(",\"controlTime\":\"").append(startTime).append(" 到 ").append(endTime).append('\"');
        sb.append(",\"remark\":\"").append(remark).append('\"').append('}');
        return sb.toString();
    }

    public String toJSONHtml(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id);
        sb.append(",\"name\":\"").append(StringUtil.replaceHtml(name)).append('\"');
        sb.append(",\"type\":").append(type);
        sb.append(",\"busType\":").append(busType);
        sb.append(",\"priority\":").append(priority);
        sb.append(",\"priorityStr\":\"").append(getPriorityString(priority)).append('\"');
        sb.append(",\"extend\":").append(extendFlag ? "true" : "false");
        sb.append(",\"extendFlag\":\"").append(extendFlag ? "是" : "否").append('\"');
        sb.append(",\"startTime\":\"").append(startTime).append('\"');
        sb.append(",\"endTime\":\"").append(endTime).append('\"');
        sb.append(",\"mmsMaxLength\":").append(mmsMaxLength);
        sb.append(",\"speed\":").append(speed);
        sb.append(",\"speedMode\":").append(speedMode);
        sb.append(",\"controlTime\":\"").append(startTime).append(" 到 ").append(endTime).append('\"');
        sb.append(",\"remark\":\"").append(remark).append('\"').append('}');
        return sb.toString();
    }

    private String getPriorityString(int priority){
        switch(priority){
            case 4:
                return "低";
            case 3:
                return "中";
            case 2:
                return "较高";
            case 1:
                return "最高";
            default:
                return "";
        }
    }
}

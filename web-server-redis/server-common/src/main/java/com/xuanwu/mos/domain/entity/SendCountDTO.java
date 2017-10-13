package com.xuanwu.mos.domain.entity;

/** 统计发送相关的数量
 * Created by 郭垚辉 on 2017/3/29.
 */
public class SendCountDTO {
    /**
     * 发送数量
     */
    Integer sendCount;

    /**
     *
     */
    Integer sendAllCount;

    /**
     * 正在发送中的短信记录
     */
    Integer sendingCount;

    /**
     * 所有提交的短信数量
     */
    Integer postAllCount;

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getSendAllCount() {
        return sendAllCount;
    }

    public void setSendAllCount(Integer sendAllCount) {
        this.sendAllCount = sendAllCount;
    }

    public Integer getSendingCount() {
        return sendingCount;
    }

    public void setSendingCount(Integer sendingCount) {
        this.sendingCount = sendingCount;
    }

    public Integer getPostAllCount() {
        return postAllCount;
    }

    public void setPostAllCount(Integer postAllCount) {
        this.postAllCount = postAllCount;
    }
}

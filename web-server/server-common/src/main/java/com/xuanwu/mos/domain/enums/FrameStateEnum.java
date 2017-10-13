package com.xuanwu.mos.domain.enums;


import com.xuanwu.mos.domain.handler.HasIndexValue;

/** 信息帧的状态：frame表的状态
 * Created by 郭垚辉 on 2017/5/20.
 */
public enum FrameStateEnum implements HasIndexValue {
    /**
     * Wait to handle
     */
    WAIT(0,"待处理"),
    /**
     * Handling, Retrieved by the mto server
     */
    HANDLE(1,"处理中"),
    /**
     * Handled
     */
    OVER(2,"处理完成"),
    /**
     * Other state, can't be determined
     */
    OTHER(3,"其他状态"),
    /**
     * ABANDONED frame,not need to be sent
     */
    ABANDON(4,"被丢弃"),
    /**
     * Wait to audit
     */
    AUDITING(5,"待审核"),
    /**
     * auditing not pass
     */
    AUDITING_NOT_PASS(6,"审核不通过");

    private int index;

    private String FrameStateName;

    FrameStateEnum(int index, String FrameStateName) {
        this.FrameStateName = FrameStateName;
        this.index = index;
    }

    public static FrameStateEnum getFrameState(int index) {
        for (FrameStateEnum type : FrameStateEnum.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    public String getFrameStateName() {
        return this.FrameStateName;
    }
}


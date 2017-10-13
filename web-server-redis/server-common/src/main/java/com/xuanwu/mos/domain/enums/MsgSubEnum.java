package com.xuanwu.mos.domain.enums;

/**
 * Created by 郭垚辉 on 2017/4/19.
 */
public enum MsgSubEnum {
    /* 所有 */
    All(0),

    FirstMsg(1),

    /* 父短信 */
    ParentMsg(2),

    /* 子短信 */
    ChildrenMsg(3);

    private int index;

    private MsgSubEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static MsgSubEnum getType(int index) {
        switch (index) {
            case 0:
                return All;
            case 1:
                return FirstMsg;
            case 2:
                return ParentMsg;
            case 3:
                return ChildrenMsg;
            default:
                return All;
        }
    }
}
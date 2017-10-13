package com.xuanwu.mos.domain.enums;

/**
 * Created by 郭垚辉 on 2017/4/19.
 */

public enum MsgStatusEnum {
    /* 所有 */
    All(0),
    /* 已经提交 */
    Submited(1),
    /* 发送成功 */
    Success(2),
    /* 发送失败 */
    Failure(3);

    private int index;

    private MsgStatusEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static MsgStatusEnum getType(int index) {
        switch (index) {
            case 0:
                return All;
            case 1:
                return Submited;
            case 2:
                return Success;
            case 3:
                return Failure;
            default:
                return All;
        }
    }
}
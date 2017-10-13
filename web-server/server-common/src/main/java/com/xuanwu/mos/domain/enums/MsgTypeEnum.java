package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * Created by 郭垚辉 on 2017/4/19.
 */
public enum MsgTypeEnum implements HasIndexValue {
    /* 所有 */
    ALL(0),
    /* 短信 */
    SMS(1),
    /* 彩信 */
    MMS(2),
    /* WapPush */
    WAPPUSH(3),
    /** 语音通知 */
    VOICE_NOTICE(8),
    /** 语音验证码 */
    VOICE_CODE(9);


    private int index;

    private MsgTypeEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static MsgTypeEnum getType(int index) {
        switch (index) {
            case 0:
                return ALL;
            case 1:
                return SMS;
            case 2:
                return MMS;
            case 3:
                return WAPPUSH;
            case 8:
                return VOICE_NOTICE;
            case 9:
                return VOICE_CODE;
            default:
                return SMS;
        }
    }
}
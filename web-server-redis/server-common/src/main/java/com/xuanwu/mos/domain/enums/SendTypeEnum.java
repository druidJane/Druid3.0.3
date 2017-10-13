package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * Created by 郭垚辉 on 2017/5/20.
 */
public enum  SendTypeEnum implements HasIndexValue{

    MASS(0,"群发"), GROUP(1,"组发");

    private int index;

    private String sendTypeEnum;

    SendTypeEnum(int index, String sendTypeEnum) {
        this.sendTypeEnum = sendTypeEnum;
        this.index = index;
    }

    public static SendTypeEnum getPackState(int index) {
        for (SendTypeEnum type : SendTypeEnum.values()) {
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

    public String getsendTypeEnum() {
        return this.sendTypeEnum;
    }

}

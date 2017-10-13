package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/** 信息包或者是审核信息包使用该状态
 * Created by 郭垚辉 on 2017/5/20.
 */
public enum PackStateEnum implements HasIndexValue {
    INIT(0,"待发送"), AUDITING(1,"待审批"),  ABANDON(2,"被丢弃"), CANCEL(3,"取消"), SECOND_AUDITING(4,"待后台审核"),
    HANDLE(8,"发送中"), OVER(9,"完成");

    private int index;

    public String packStateName;

    PackStateEnum(int index, String packStateName) {
        this.packStateName = packStateName;
        this.index = index;
    }

    public static PackStateEnum getPackState(int index) {
        for (PackStateEnum type : PackStateEnum.values()) {
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

    public String getPackStateName() {
        return this.packStateName;
    }
}
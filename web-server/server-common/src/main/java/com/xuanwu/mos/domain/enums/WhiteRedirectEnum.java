package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public enum WhiteRedirectEnum implements HasIndexValue {
    EnterpriseSpecNum(0), ChannelId(1);

    private int index;

    private WhiteRedirectEnum(int index) {
        this.index = index;
    }

    public static WhiteRedirectEnum getType(int index) {
        for (WhiteRedirectEnum type : WhiteRedirectEnum.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

    @Override
    public int getIndex() {
        return index;
    }
}

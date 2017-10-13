package com.xuanwu.mos.domain.enums;

/**
 * Created by 郭垚辉 on 2017/6/28.
 */
public enum  TrustIpStatus {
    SUCCEED(0),
    FAILED(-1),
    EXCEPTION(-2);

    private int index;

    TrustIpStatus(int index) {
        this.index = index;
    }
}

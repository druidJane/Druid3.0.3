package com.xuanwu.mos.domain.entity;

/**
 * Created by Jiang.Ziyuan on 2017/3/27.
 */
public enum MTResult {
    SUCCESS(0),
    INVALID_ACCOUNT(-1),
    INVALID_PARAM(-2),
    CONNECT_TO_SERVER_FAIL(-3),
    ACCOUNT_NOT_EXIST_OR_WRONG_PIN(-6),
    CAPITAL_ACCOUNT_NOT_EXIST(-9),
    TOO_LARGE_PACK(-11),
    NO_SAVE_ACCOUNT(-12),
    NOT_ALLOW_SEND(-13),
    INNER_ERROR(-99),
    OTHER_ERROR(-100);

    private final int index;

    private MTResult(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public static MTResult getResult(int index) {
        MTResult[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            MTResult result = arr$[i$];
            if(result.getIndex() == index) {
                return result;
            }
        }

        return OTHER_ERROR;
    }
}

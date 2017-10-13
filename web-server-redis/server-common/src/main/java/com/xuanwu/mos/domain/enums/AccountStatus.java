package com.xuanwu.mos.domain.enums;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public enum AccountStatus implements HasIndexValue {
    UNCHECKED(0), CHECKED(1);

    private int index;

    private AccountStatus(int index) {
        this.index = index;
    }

    public static AccountStatus getStatus(int index) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.getIndex() == index) {
                return status;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }
}

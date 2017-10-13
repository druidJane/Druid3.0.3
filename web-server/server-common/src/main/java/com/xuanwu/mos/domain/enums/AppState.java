package com.xuanwu.mos.domain.enums;


import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * 应用状态
 *
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @version 1.0.0
 * @date 2016-08-25
 */
public enum AppState implements HasIndexValue {

    // 0:未上线,1:运营中,2:已暂停
    OFFLINE(0), ONLINE(1), PAUSED(2);

    private int index;

    private AppState(int index) {
        this.index = index;
    }

    public static AppState getState(int index) {
        for (AppState state : AppState.values()) {
            if (state.getIndex() == index) {
                return state;
            }
        }
        return null;
    }

    public static String getStateName(int index) {
        switch (index) {
            case 0:
                return "未上线";
            case 1:
                return "运营中";
            case 2:
                return "已暂停";
            default:
                return null;
        }
    }

    @Override
    public int getIndex() {
        return index;
    }

}

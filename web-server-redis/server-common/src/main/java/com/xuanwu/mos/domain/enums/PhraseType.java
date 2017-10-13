package com.xuanwu.mos.domain.enums;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
public enum PhraseType implements HasIndexValue {
    STATIC(1), DYNA(2), TD(3), OTHER(4);
    private int index;

    private PhraseType(int index) {
        this.index = index;
    }

    public static PhraseType getType(int index) {
        for (PhraseType type : PhraseType.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

package com.xuanwu.mos.domain.enums;

/**
 * Created by 郭垚辉 on 2017/4/19.
 */

public enum MediaTypeEnum {
    JPG(0),
    GIM(1),
    MID(2),
    ERRORMEDIATYPE(9);

    private int index ;
    private MediaTypeEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static MediaTypeEnum getType(int index){
        switch (index) {
            case 0:
                return JPG;
            case 1:
                return GIM;
            case 2:
                return MID;
            default:
                return ERRORMEDIATYPE;
        }
    }
}
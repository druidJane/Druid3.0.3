package com.xuanwu.mos.file;

/**
 * Created by linzeqiang on 2016/11/4.
 */
public class HeadInfo {
    private int index;
    private String name;

    public HeadInfo(int index, String name) {
        this.index = index;
        this.name = name;
    }
    public HeadInfo() {}

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

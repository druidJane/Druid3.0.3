package com.xuanwu.mos.vo;

/**
 * Created by gyh65 on 2017/5/8.
 */
public class ReturnFileData {
    private String name;
    private long size;

    public ReturnFileData() {
    }

    public ReturnFileData(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}

package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.dto.PageInfo;

/**
 * @Description 动态参数
 * Created by Jiang.Ziyuan on 2017/3/27.
 */
public class DynamicParam {

    /** 排序字段 */
    private String orderField;

    /** 是否降序 */
    private boolean isDesc;

    /** 分页类 */
    private PageInfo pi;

    /** 扩展参数1 */
    private Object ext;

    /** 扩展参数2 */
    private Object ext1;

    /** 扩展参数3 */
    private Object ext2;

    private boolean notIn;

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setDesc(boolean isDesc) {
        this.isDesc = isDesc;
    }

    public PageInfo getPi() {
        return pi;
    }

    public void setPi(PageInfo pi) {
        this.pi = pi;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public Object getExt1() {
        return ext1;
    }

    public void setExt1(Object ext1) {
        this.ext1 = ext1;
    }

    public Object getExt2() {
        return ext2;
    }

    public void setExt2(Object ext2) {
        this.ext2 = ext2;
    }

    public boolean isNotIn() {
        return notIn;
    }

    public void setNotIn(boolean notIn) {
        this.notIn = notIn;
    }
}

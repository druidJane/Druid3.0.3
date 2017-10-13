package com.xuanwu.mos.vo;

/** 该类用于封装row-data数据
 * Created by 郭垚辉 on 2017/4/27.
 */
public class RowDataVo<T> {
    // 数据的总记录数目
    private int total;
    // 当前页数
    private int page;
    // 具体数据
    private T data;

    private RowDataVo() {
    }

    public RowDataVo(int total, int page, T data) {
        this.total = total;
        this.page = page;
        this.data = data;
    }

    public static <T> RowDataVo<T> createRowDate(int total, int page, T t){
        return new RowDataVo<>(total,page,t);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.xuanwu.exception;

public class FilterError extends Exception{
    private int status;
    private String msg;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public FilterError(String msg,int status){
        super(msg);
        this.msg = msg;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
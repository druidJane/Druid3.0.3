package com.xuanwu.mos.exception;

import org.apache.shiro.authc.IncorrectCredentialsException;

/**
 * Created by Administrator on 2017/3/30.
 */
public class IncorrectCredentialsTimesException extends IncorrectCredentialsException {
    private static final long serialVersionUID = 1L;

    private int errTimes;

    public int getErrTimes() {
        return errTimes;
    }

    public void setErrTimes(int errTimes) {
        this.errTimes = errTimes;
    }

    public IncorrectCredentialsTimesException(String message) {
        super(message);
    }

    public IncorrectCredentialsTimesException(int errTimes,String message) {
        super(message);
        this.errTimes = errTimes;
    }
}

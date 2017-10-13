package com.xuanwu.mos.exception;

import javax.ws.rs.WebApplicationException;

/**
 * @author <a href="kangqinghua@wxchina.com">Qinghua Kang</a>
 * @version 1.0.0
 * @description: todo
 * @date 2016/11/8 17:50:01
 */

public class BusinessException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
}

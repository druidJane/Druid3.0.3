package com.xuanwu.mos.utils;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * Created by Administrator on 2017/4/13.
 */
public class Md5Utils {

    public static String mixLoginPasswd(String primPasswd, String salt) {
        return  DigestUtils.md5Hex(DigestUtils.md5Hex(primPasswd) + salt);
    }
}

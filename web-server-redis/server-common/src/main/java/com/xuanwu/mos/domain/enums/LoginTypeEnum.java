package com.xuanwu.mos.domain.enums;

/**
 * 用户登录前台的方式
 * Created by 郭垚辉 on 2017/7/19.
 */
public enum LoginTypeEnum {
    // 用户名+密码登录
    USERNAME_PASSWORD(0),
    // 手机号码+手机验证码登录
    PHONE_VERIFY_CODE(1);

    private int index;

    LoginTypeEnum(int index) {
        this.index = index;
    }
}

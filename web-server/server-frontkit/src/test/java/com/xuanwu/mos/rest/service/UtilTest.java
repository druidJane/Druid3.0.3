package com.xuanwu.mos.rest.service;

import java.util.regex.Pattern;

/**
 * Created by zhangz on 2017/6/14.
 */
public class UtilTest {
    public static void main(String[] args) {
        Pattern namePattern = Pattern.compile("^[(0-9)(a-z)(A-Z)(\\u4e00-\\u9fa5)(-_()（）{}\\[\\]\\【\\】)]*$");
        String str = "中文【】$";
        System.out.println(namePattern.matcher(str).matches());
        genPhone("155");
    }
    public static void genPhone(String prefix){
        StringBuilder builder;
        System.out.println("");
        for (int i = 0; i < 100; i++) {
            builder = new StringBuilder(prefix);
            for (int j = 0; j < 8; j++) {
                builder.append(String.valueOf(Math.round(9*Math.random())));
            }
            System.out.println(builder.toString());
        }
        //endregion


    }
}

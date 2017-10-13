package com.xuanwu.mos.vo;

/**
 * Created by zhangz on 2017/5/16.
 */
public class UserTaskParameter {
    private String key;
    private String value;
    public UserTaskParameter(){

    }
    public UserTaskParameter(String key,String value){
        this.key = key==null?"":key;
        this.value = value==null?"":value;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

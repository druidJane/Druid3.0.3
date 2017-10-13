package com.xuanwu.mos.vo;

/**
 * Created by 郭垚辉 on 2017/8/8.
 */
public class ContentVo {

    private Integer type;
    private Object content;

    public ContentVo() {
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

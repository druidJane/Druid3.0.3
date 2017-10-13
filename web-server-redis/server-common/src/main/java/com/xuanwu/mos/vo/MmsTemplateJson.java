package com.xuanwu.mos.vo;

import com.xuanwu.mos.domain.entity.MmsContent;

/**
 * Created by gyh65 on 2017/5/11.
 */
public class MmsTemplateJson {
    private int id;
    // 彩信标题
    private String title;
    // 彩信内容
    private MmsContent mms;
    // 彩信业务类型
    private int bizId;

    public MmsTemplateJson() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MmsContent getMms() {
        return mms;
    }

    public void setMms(MmsContent mms) {
        this.mms = mms;
    }

    public int getBizId() {
        return bizId;
    }

    public void setBizId(int bizId) {
        this.bizId = bizId;
    }
}

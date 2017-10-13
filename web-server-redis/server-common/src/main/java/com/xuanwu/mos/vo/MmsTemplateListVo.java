package com.xuanwu.mos.vo;

import java.util.Date;

/** 彩信模板管理vo
 * Created by 郭垚辉 on 2017/5/2.
 */
public class MmsTemplateListVo {
    private Integer id;
    private String title;
    private Date lastUpdateTime;
    private String identify;

    public MmsTemplateListVo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}

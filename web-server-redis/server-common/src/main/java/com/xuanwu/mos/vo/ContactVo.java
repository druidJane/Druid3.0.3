package com.xuanwu.mos.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** 发送彩信时处理前台的传来的通讯录对象
 * 在设计中，只有设计
 * 第一种，传单个电话号码，以p开头，例如p18206534521
 * 第二种，传文件名称，以f开头，以$结束 例如fxxx.txt$
 * Created by 郭垚辉 on 2017/5/26.
 */
public class ContactVo {

    /**
     * 上传的文件类型
     * 文件的话，type为file
     * 单个号码的话，type为single
     */
    private String type;
    /**
     * 前台左侧彩信发送联系人的列表的显示名称
     * 文件会显示用户上传时文件的名称
     * 号码会显示号码的名称
     */
    private String value;

    /**
     * 如果type.equals("file"),那么存在newName为新文件名称
     * 否则，该字段为空
     */
    private String newName;

    public ContactVo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}

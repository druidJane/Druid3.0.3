package com.xuanwu.mos.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/** 用于获取彩信的附件
 * Created by 郭垚辉 on 2017/5/26.
 */


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class MmsAttachFile{

    @JacksonXmlProperty(localName="FileName")
    public String fileName;

    @JacksonXmlProperty(localName="Content")
    public byte[] content;
}
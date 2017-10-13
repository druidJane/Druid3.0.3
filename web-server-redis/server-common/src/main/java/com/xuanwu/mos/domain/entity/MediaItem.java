package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.enums.MediaTypeEnum;

/**
 * Created by Jiang.Ziyuan on 2017/3/23.
 */
public interface MediaItem {
    MediaTypeEnum getType();

    String getMeta();

    void setMeta(String var1);

    byte[] getData();

    void setData(byte[] var1);

    com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem build();

}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.protobuf.CommonItem.Result;

import java.util.List;
import java.util.Set;

/**
 * Message content
 *
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-5-12
 * @Version 1.0.0
 */
public interface MsgContent {
    /**
     * Message type
     */
    public enum MsgType {
        SMS(0), LONGSMS(1), MMS(2), WAP_PUSH(3), VOICE_NOTICE(8),VOICE_CODE(9);
        private final int index;

        private MsgType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static MsgType getType(int index) {
            switch (index) {
                case 0:
                    return SMS;
                case 1:
                    return LONGSMS;
                case 2:
                    return MMS;
                case 3:
                    return WAP_PUSH;
              case 8:
                    return VOICE_NOTICE;
                case 9:
                    return VOICE_CODE;
                default:
                    throw new IllegalArgumentException(new CommonRespResult(Result.INVALID_PARAM, "（msgType: " +
                            "参数值无效，有效值为0、1、2、3、8、9）").toString());
            }
        }
    }

    /**
     * 获得当前信息的类型
     *
     * @return
     */
    public MsgContent.MsgType getType();

    /**
     * 获得当前短信内容的信息.
     * <p>
     * 如果是短信则是短信本身的内容,否则为彩信的文字内容
     *
     * @return
     */
    public String getContent();

    /**
     * 设置当前的信息值
     *
     * @param content
     */
    public void setContent(String content);

    /**
     * 获得彩信中所带的文件信息
     *
     * @return
     */
    public List<MediaItem> getMediaItems();

    /**
     * 设置彩信中所带的文件信息
     *
     * @param mediaItems
     */
    public void setMediaItems(List<MediaItem> mediaItems);

    /**
     * 设置当前彩信的总容量
     *
     * @param MMSLength
     */
    public void setAttachmentTotalLength(long attachmentTotalLength);

    /**
     * 获得当前彩信的总容量
     *
     * @return
     */
    public long getAttachmentTotalLength();

    /**
     * 设置当前彩信的所有附件的类型
     *
     * @param type
     */
    public void setAttachmentTypes(Set<String> attachmentTypes);

    /**
     * 获得当前彩信的所有附件的类型
     *
     * @return
     */
    public Set<String> getAttachmentTypes();

    /**
     * 获得彩信的标题
     *
     * @return
     */
    public String getTitle();

    /**
     * 设置彩信的标题
     *
     * @param title
     */
    public void setTitle(String title);

    /**
     * 获得彩信smil
     *
     * @return
     */
    public String getSmil();

    /**
     * 设置彩信smil
     *
     * @param smil
     */
    public void setSmil(String smil);

    /**
     * Builder protobuf data
     *
     * @return
     */
    public com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent build();
}

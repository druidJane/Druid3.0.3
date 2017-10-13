package com.xuanwu.mos.domain.entity;

import java.util.List;
import java.util.Set;

/**
 * Created by Jiang.Ziyuan on 2017/3/23.
 */
public interface MsgContent {

    MsgContent.MsgType getType();

    String getContent();

    void setContent(String var1);

    List<MediaItem> getMediaItems();

    void setMediaItems(List<MediaItem> var1);

    void setAttachmentTotalLength(long var1);

    long getAttachmentTotalLength();

    void setAttachmentTypes(Set<String> var1);

    Set<String> getAttachmentTypes();

    String getTitle();

    void setTitle(String var1);

    String getSmil();

    void setSmil(String var1);

    com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent build();

    public static enum MsgType {
        SMS(0),
        LONGSMS(1),
        MMS(2),
        WAP_PUSH(3),
        VOICE_NOTICE(8),
        VOICE_CODE(9);

        private final int index;

        private MsgType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        public static MsgContent.MsgType getType(int index) {
            switch(index) {
                case 0:
                    return SMS;
                case 1:
                    return LONGSMS;
                case 2:
                    return MMS;
                case 3:
                    return WAP_PUSH;
                case 4:
                case 5:
                case 6:
                case 7:
                default:
                    //throw new IllegalArgumentException((new CommonRespResult(Result.INVALID_PARAM, "（msgType: 参数值无效，有效值为0、1、2、3、8、9）")).toString());
                    throw new IllegalArgumentException();
                case 8:
                    return VOICE_NOTICE;
                case 9:
                    return VOICE_CODE;
            }
        }
    }
}

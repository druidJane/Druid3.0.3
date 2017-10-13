package com.xuanwu.mos.domain.entity;

import java.io.Serializable;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
public class ChannelCarrier extends BaseEntity  {
    private int carrierId;
    private int channelId;
    /**
     * @return carrierId
     */
    public int getCarrierId() {
        return carrierId;
    }
    /**
     * @param carrierId 要设置的 carrierId
     */
    public void setCarrierId(int carrierId) {
        this.carrierId = carrierId;
    }
    /**
     * @return channelId
     */
    public int getChannelId() {
        return channelId;
    }
    /**
     * @param channelId 要设置的 channelId
     */
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toJSON() {
        return null;
    }

    @Override
    public Serializable getId() {
        return null;
    }
}

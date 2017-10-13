package com.xuanwu.mos.domain.entity;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
public class ChannelRegion {
    private int id;
    private int channelId;
    private int regionId;
    private boolean isRemove;
    /**
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id 要设置的 id
     */
    public void setId(int id) {
        this.id = id;
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
    /**
     * @return regionId
     */
    public int getRegionId() {
        return regionId;
    }
    /**
     * @param regionId 要设置的 regionId
     */
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }
    /**
     * @return isRemove
     */
    public boolean isRemove() {
        return isRemove;
    }
    /**
     * @param isRemove 要设置的 isRemove
     */
    public void setRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }
}

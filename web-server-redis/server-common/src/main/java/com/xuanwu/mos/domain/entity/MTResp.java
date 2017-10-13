package com.xuanwu.mos.domain.entity;

import java.util.UUID;

/**
 * Created by Jiang.Ziyuan on 2017/3/27.
 */
public class MTResp {
    private UUID uuid;
    private MTResult result;
    private String message;
    private String attributes;

    public MTResp() {
        this.result = MTResult.OTHER_ERROR;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public MTResult getResult() {
        return this.result;
    }

    public void setResult(MTResult result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttributes() {
        return this.attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public static MTResp build(MTResult result) {
        return build(result, (String)null);
    }

    public static MTResp build(MTResult result, String msg) {
        MTResp resp = new MTResp();
        resp.setResult(result);
        resp.setMessage(msg);
        return resp;
    }
}

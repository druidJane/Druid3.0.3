package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * frame表中的biz_form 来源于网关
 * Created by 郭垚辉 on 2017/5/20.
 */
public enum BizFormEnum implements HasIndexValue {
    NORMAL(0, "检核通过"),
    /**
     * 黑名单信息帧
     */
    BLACK(1, "黑名单过滤"),
    /**
     * 红名单信息帧
     */
    RED(2, "红名单"),
    /**
     * 内容非法信息帧
     */
    ILLEGALKEY(3, "关键字过滤"),
    /**
     * 区域限制
     */
    REGION_LIMIT(4, "区域限制"),
    /**
     * 发送取消
     */
    CANCEL(5, "发送取消"),
    /**
     * 系统分配端口发送信息帧
     */
    SYS_BIND(6, "系统分配端口发送信息帧"),
    /**
     * 指定端口发送信息帧
     */
    SPECIAL_ASSIGN(7, "指定端口"),
    /**
     * 区域优先发送信息帧
     */
    REGION_PRIORITY(8, "区域优先"),
    /**
     * 通道切换
     */
    CHANNEL_CHANGE(9, "通道切换"),
    /**
     * 端口选择失败信息帧
     */
    NULL_SPECNUM(10, "端口选择失败"),
    /**
     * 号码非法
     */
    ILLEGAL_PHONE(11, "非法号码过滤"),
    /**
     * 信息已过期
     */
    EXPIRED(12, "信息已过期"),
    /**
     * 非白名单信息帧
     */
    NON_WHITELIST(13, "非白名单"),
    /**
     * 非法彩信帧
     */
    ILLEGAL_MMS(14, "非法彩信帧"),
    /**
     * 红名单转白名单帧
     */
    RED_WHITE_FORWARD(15, "红名单转白名单"),
    /**
     * 超过最大容量超过最大容量
     */
    MMS_OVER_LENGTH(16, "超过最大容量"),
    /**
     * 不是支持的类型
     */
    MMS_OVER_TYPE(17, "类型不支持"),
    /**
     * 重复号码
     */
    REPEAT_PHONE(18, "重复号码过滤"),
    /**
     * VIP 号码
     */
    VIP_PHONE(19, "VIP号码"),
    /**
     * 不在运营商号段
     */
    OUTOF_CARRIER(20, "不在运营商号段"),
    /**
     * 空白内容
     */
    BLANK_CONTENT(21, "空信息过滤"),
    /**
     * 审批不通过
     */
    AUDIT_DOES_NOT_PASS(22, "审批不通过"),
    /**
     * 业务应用不匹配
     */
    BIZ_APP_NOT_MATCH(23, "发送限制"),
    /**
     * 在指定的时间间隔内超过了发送次日数量
     */
    MORETHAN_SENDTIMES(24, "条数限制过滤"),

    /**
     * 余额不足
     */
    INSUFFICIENT_BALANCE(25, "余额不足"),

    /**
     * 在指定的时间间隔内重复提交（同号同内容）
     */
    REPEAT_SEND(26, "重复提交过滤"),

    /**
     * 用户内容不含有合法的签名
     */
    ILLEGAL_SIGNATURE(27, "用户内容不含有合法的签名"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(31, "系统错误");

    private int index;

    private String BizFormName;

    BizFormEnum(int index, String BizFormName) {
        this.BizFormName = BizFormName;
        this.index = index;
    }

    public static BizFormEnum getBizForm(int index) {
        for (BizFormEnum type : BizFormEnum.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    public String getBizFormName() {
        return this.BizFormName;
    }
}

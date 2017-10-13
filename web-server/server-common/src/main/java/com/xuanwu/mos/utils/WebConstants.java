/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.utils;


/**
 * @Description keys and other constants
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-9-3
 * @Version 1.0.0
 */
public class WebConstants {

    /**
     * 数据范围KEY
     */
    public static final String KEY_DATA_SCOPE = "_ds";

    /**
     * 授权企业名
     */
    public static final String KEY_ENT_NAME = "_entName";

    /**
     * 授权有效期
     */
    public static final String KEY_DEADLINE = "_deadline";

    /**
     * 权限ID KEY
     */
    public static final String KEY_PERM_ID = "_permId";

    /**
     * 操作对象 KEY，针对增、删、改、审、导入、导出等操作，记录日志用
     */
    public static final String KEY_OP_OBJ = "_opObj";

    /**
     * 当没有对应细分的权限时用，操作对象 KEY，针对增、删、改、审、导入、导出等操作，记录日志用
     */
    public static final String KEY_OP_TYPE = "_opType";

    /**
     * 导航栏KEY
     */
    public static final String KEY_LEFT_NAV = "_nav";

    /**
     * 默认编码
     */
    public static final String DEFAULT_CHARSET = "utf-8";

    /**
     * 请求头状态
     */
    public static final String HEADER_ACCESS_STATE = "Access-State";

    /**
     * 登录用户KEY
     */
    public static final String KEY_USER = "_user";

    /**
     * 全局配置KEY
     */
    public static final String KEY_CONFIG = "_config";

    /**
     * json成功消息
     */
    public final static String JSON_MSG_SUCC = "ok";

    /**
     * json失败消息
     */
    public final static String JSON_MSG_FAIL = "fail";

    public final static int BLACK_BIZ_TYPE = 5;

    public final static int BLACK_USER_TYPE = 0;

    public final static Integer[] UNSEND_FRAME_BIZFORMS = {1, 3, 4, 10, 11,
            14, 16, 17, 18, 20, 21};// 过滤帧类型

    public final static Integer ALL_FRAME_BIZFORM_LENGTH = 31;

    public final static String[] ALL_FRAME_BIZFORM_NAME = {"检核通过", "黑名单过滤", "红名单",
            "内容非法信息帧", "区域限制", "发送取消", "系统分配端口", "指定端口发送信息帧", "区域优先", "通道切换", "端口选择失败",
            "非法号码过滤", "信息已过期", "非白名单", "非法彩信", "红名单转白名单", "超出最大容量", "类型不支持",
            "重复号码过滤", "VIP号码", "不在运营商号段", "空信息过滤", "审核不通过","业务应用不匹配", "在指定的时间间隔内超过了发送次日数量", "余额不足","在指定的时间间隔内重复提交","重复提交过滤","用户内容不含有合法的签名","","","","","系统错误"};// 帧类型说明(对于bizform字段的顺序解释)

    public final static String[] MSG_TICKET_STATE = {"等待发送", "提交成功", "被拒绝",
            "数据格式错误", "多次发送失败", "帧结束标志", "序列号错误", "系统拒绝发送"};// 话单状态列表(对于state字段的顺序解释)

    public final static String[] STATE_REPORT_RESULT = {"发送成功", "信息超时",
            "发送失败", "拒绝发送", "发送失败", "等待发送"};// 状态报告状态列表(对于state字段的顺序解释，第五个元素"信息被删除"被弃用，使用"发送失败"来代替)
    public final static String[] PACK_STATE = {"待发送", "待审核","被丢弃", "取消","待后台审核","","","","发送中","完成"};// pack表`state` int(11) NOT NULL COMMENT '0：INIT（待发送）;1: AUDITING（待审核）;2: ABANDON（被丢弃）;3: CANCEL（取消）',

    public final static int STAT_SUM_ID = -1;

    public final static String STAT_TOTAL = "合计";

    public final static String STAT_CUR_PAGE = "当前页合计";

    public final static int STAT_RADIO_VAL = -99;

    public final static int SYS_ERROR_CODE = -1;

    public static final String SEND_SMS_ERROR = "sendSmsError";

    public final static class StatResultCode {

        public final static int BEGINTIME_GT_ENDTIME_CODE = -2;

        public final static int ENDTIME_GT_CURRENTTIME_CODE = -3;

        public final static int USERNAME_LEN_TOO_LONG_CODE = -4;

        public final static int NOT_DATA_CODE = -5;

        public final static int BEGINTIME_NOT_NULL_CODE = -7;

        public final static int ENDTIME_NOT_NULL_CODE = -8;

        public final static int STAT_TIME_TOO_LONG_CODE = -9;
    }

    public final static class BlacklistResultCode {

        /**
         * json成功结果码
         */
        public final static int JSON_RESULT_SUCC = 0;

        /**
         * json失败结果码
         */
        public final static int JSON_RESULT_FAIL = -1;

        /**
         * 手机号码为空结果码
         */
        public final static int PHONE_RESULT_NULL = -2;

        /**
         * 手机号码太长
         */
        public final static int PHONE_RESULT_TOO_LONG = -4;

        /**
         * 手机号码不在运行商号段
         */
        public final static int PHONE_RESULT_NO_CARRIER_TELESEG = -5;

        /**
         * 备注信息太长
         */
        public final static int REMARK_RESULT_TOO_LONG = -6;

        /**
         * 黑名单存在结果码
         */
        public final static int BLACKLIST_RESULT_EXIST = -3;

        /**
         * 删除黑名单的ID为空
         */
        public final static int BLACKLIST_ID_RESULT_NULL = -2;

        /**
         * 手机号码长度错误
         */
        public final static int WRONG_PHONE_LENGTH = -2;
    }

    public final static class WhitePhoneResuleCode {
        /**
         * json成功结果码
         */
        public final static int JSON_RESULT_SUCC = 0;
        /**
         * json失败结果码
         */
        public final static int JSON_RESULT_FAIL = -1;
        /**
         * 删除企业白名单的ID为空
         */
        public final static int WHITEPHONE_ID_RESULT_NULL = -2;
        /**
         * 企业白名单存在结果码
         */
        public final static int WHITEPHONE_RESULT_EXIST = -3;
         /**
         * 企业白名单手机号码不能为空结果码
         */
        public final static int WHITEPHONE_IS_NULL = -4;

    }

    public final static class NonWhitelistResultCode {

        /**
         * 转移的非白名单的ID为空
         */
        public final static int NONWHITELIST_ID_RESULT_NULL = -2;

        /**
         * json成功结果码
         */
        public final static int JSON_RESULT_SUCC = 0;

        /**
         * json失败结果码
         */
        public final static int JSON_RESULT_FAIL = -1;
    }

    public final static class WhitelistResultCode {
        /**
         * json成功结果码
         */
        public final static int JSON_RESULT_SUCC = 0;

        /**
         * json失败结果码
         */
        public final static int JSON_RESULT_FAIL = -1;

        /**
         * 手机号码为空结果码
         */
        public final static int PHONE_RESULT_NULL = -2;

        /**
         * 手机号码太长
         */
        public final static int PHONE_RESULT_TOO_LONG = -4;

        /**
         * 手机号码最大长度14
         */
        public final static int PHONE_LEN_MAX = 14;

        /**
         * 手机号码不在运行商号段
         */
        public final static int PHONE_RESULT_NO_CARRIER_TELESEG = -5;

        /**
         * 手机号存在结果码
         */
        public final static int WHITELIST_RESULT_EXIST = -3;

        /**
         * 删除白名单的ID为空
         */
        public final static int WHITELIST_ID_RESULT_NULL = -2;

        /**
         * 通道不支持白名单
         */
        public final static int CHANNEL_IS_NOTWHITELIST = -6;

        /**
         * 通道不存在
         */
        public final static int CHANNEL_IS_NULL = -7;

        /**
         * 手机号码不在网段
         */
        public final static int PHONE_IS_NOT_NETSEGMENT = -8;
    }

}

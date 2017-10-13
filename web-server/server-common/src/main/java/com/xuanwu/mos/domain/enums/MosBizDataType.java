package com.xuanwu.mos.domain.enums;

/**
 * MosBizDataType中的命名遵循如下规则：
 * 1、Front(前台)/Back(后台)_DataType_Exp(导出)/Imp(导入)
 * 2、DataType的命名与对应功能网页的URL中Index前的上一级目录名一致。
 * 3、枚举常量中的数据来源于：任务名字所对应在gsms_user_task_code的id
 * <p>
 * 例子：
 * 非白名单 URL：http://172.16.22.239:801/ChannelMgr/NoWhiteChannel/Index
 * 则后台导入为：Back_NoWhiteChannel_Imp
 * 后台导入为：  Back_NoWhiteChannel_Exp
 */
public enum MosBizDataType {

    /**
     * 后台
     */
    Back_User_Exp(1, "后台用户导出"),
    Back_KeyWord_Exp(2, "非法关键字导出"),
    Back_complaints_Exp(3, "投诉管理导出"),
    Back_WhiteList_Exp(4, "企业白名单导出"),
    Back_BlackList_Exp(5, "黑名单导出"),
    Back_RegionDnSeg_Exp(6, "区域号段维护导出"),
    Back_StateReportMapping_Exp(7, "状态报告映射导出"),
    Back_RedList_Exp(8, "企业后台红名单导出"),
    Back_NoWhiteChannel_Exp(9, "企业后台非白名单导出"),
    Back_WhiteChannel_Exp(10, "通道白名单导出", true),
    Back_Trend_Ent_Exp(11, "后台企业趋势统计导出"),
    Back_Trend_Channel_Exp(12, "后台通道趋势统计导出"),
    Back_SumEnterprise_Exp(13, "后台企业总量统计导出"),
    Back_SumChannel_Exp(14, "后台通道总量统计导出"),
    Back_ChannelDetail_Exp(15, "通道明细导出"),
    Back_SpecDetail_Exp(16, "端口明细导出"),
    Back_SumResEnterprise_Exp(17, "资源企业总量统计导出"),
    Back_Inbox_Exp(18, "上行查看导出", true),
    Back_SendTrackingSms_Exp(19, "后台短信记录导出", true),//分步导出
    Back_SendTrackingMms_Exp(20, "后台彩信记录导出", true),
    Back_complaints_Import(21, "投诉管理导入"),
    Back_User_Imp(22, "后台用户导入"),
    Back_KeyWord_Import(23, "非法关键字导入"),
    Back_BlackList_Import(24, "黑名单导入"),
    Back_RegionDnSeg_Imp(25, "区域号段维护导入"),
    Back_StateReportMapping_Imp(26, "状态报告映射导入"),
    Back_RedList_Imp(27, "后台红名单导入"),
    Back_WhiteChannel_Imp(28, "通道白名单导入"),
    Back_Channel_Import(29, "通道基本信息导入"),
    Back_RegionRate_Exp(30, "区域优先统计导出"),
    Back_CheckNumber_Exp(31, "后台对账号码导出", true),
    Back_CheckBatch_Exp(32, "后台对账批次导出", true),


    /**
     * 前台
     */
    Front_BlackList_Import(204, "黑名单导入"),
    Front_WhiteListWeb_Imp(205, "企业白名单导入"),
    Front_BlackList_Exp(211, "黑名单导出"),
    Front_SumDepartment_Exp(216, "部门总量统计导出(历史、实时)"),
    Front_SumAccount_Exp(217, "用户总量统计导出"),
    Front_SumBizType_Exp(218, "业务类型统计导出"),
    Front_BatchSms_Exp(222, "短信发送记录导出", true),
    Front_BatchMms_Exp(223, "彩信发送记录导出", true),

    /**
     * 其他
     */
    Tmp(-1, "临时"), Other(-2, "其他");

    private int index;
    private String taskName;
    private boolean isExportByStep;//是否分步导出（只对导出任务有效）

    MosBizDataType(int index, String taskName) {
        this.index = index;
        this.taskName = taskName;
    }

    MosBizDataType(int index, String taskName, boolean isExportByStep) {
        this.index = index;
        this.taskName = taskName;
        this.isExportByStep = isExportByStep;
    }

    public int getIndex() {
        return index;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public boolean isExportByStep() {
        return this.isExportByStep;
    }

    public static MosBizDataType getType(int index) {
        for (MosBizDataType type : MosBizDataType.values()) {
            if (type.getIndex() == index)
                return type;
        }
        return Other;
    }

    public static String getDirByType(String parentDir, MosBizDataType type) {
        StringBuilder sb = new StringBuilder();
        sb.append(parentDir);
        sb.append(type.name().toLowerCase());
        sb.append("/");
        return sb.toString();
    }

}

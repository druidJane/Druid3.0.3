package com.xuanwu.mos.domain.enums;

/** 审核
 * Created by 郭垚辉 on 2017/5/22.
 */
public enum AuditStateEnum implements HasIndexValue {
    NULL(-1,"待审核"),
    //通过短彩信的审核
    PASS(0,"通过"),
    //后台不通过短彩信的审核
    BACK_NOT_PASS(1,"不通过"),
    //前台不通过短彩信的审核
    NOT_PASS(2,"不通过"),
    CANCEL(3,"取消发送短彩信");

    private int index;
    private String auditStateName;

    AuditStateEnum(int index, String auditStateName) {
        this.index = index;
        this.auditStateName = auditStateName;
    }

    public static AuditStateEnum getAuditState(int index) {
        for (AuditStateEnum type : AuditStateEnum.values()) {
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

    public String getAuditStateName() {
        return this.auditStateName;
    }

}

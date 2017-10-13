package com.xuanwu.mos.vo;

/** 适用于短彩信的导出
 * Created by gyh65 on 2017/5/24.
 */
public class ExportParamsVo {
    // 发送用户
    private String sendUserName;
    // 提交时间--起始
    private String beginTime;
    // 提交时间--截止
    private String endTime;
    // 批次状态
    private int packState;
    // 批次名称
    private String batchName;
    // 发送部门
    private String sendDeptName;
    // 是否包括子部门
    private boolean subDept;
    // 分隔符
    private String delimiter;
    // 自定义分隔符
    private String otherDelimiter;
    // 文件默认类型
    private String fileType;
    // 导出文件类型
    private int exportType;
    // 0表示按批次记录导出
    private String exportContent = "0";

    public ExportParamsVo() {
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPackState() {
        return packState;
    }

    public void setPackState(int packState) {
        this.packState = packState;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getSendDeptName() {
        return sendDeptName;
    }

    public void setSendDeptName(String sendDeptName) {
        this.sendDeptName = sendDeptName;
    }

    public boolean isSubDept() {
        return subDept;
    }

    public void setSubDept(boolean subDept) {
        this.subDept = subDept;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getOtherDelimiter() {
        return otherDelimiter;
    }

    public void setOtherDelimiter(String otherDelimiter) {
        this.otherDelimiter = otherDelimiter;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getExportType() {
        return exportType;
    }

    public void setExportType(int exportType) {
        this.exportType = exportType;
    }

    public String getExportContent() {
        return exportContent;
    }

    public void setExportContent(String exportContent) {
        this.exportContent = exportContent;
    }
}

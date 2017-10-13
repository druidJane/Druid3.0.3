package com.xuanwu.mos.file.handler;

import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.file.exporter.ExportInfo;

/**
 * Created by 林泽强 on 2016/8/29.
 * 导入行处理器
 */
public abstract class ImportRowHandler implements RowHandler{

    private FileTask task;
    private ExportInfo resultInfo;

    public ImportRowHandler(FileTask task, ExportInfo resultInfo) {
        this.resultInfo = resultInfo;
        this.task = task;
    }

    public FileTask getTask() {
        return task;
    }

    public ExportInfo getResultInfo() {
        return resultInfo;
    }

}

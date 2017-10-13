package com.xuanwu.mos.file.exporter;

import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.ExportResult;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by 林泽强 on 2016/8/26.
 * 导出信息、文件任务处理结果
 */
public class ExportInfo {
    private ExportResult result = ExportResult.UNKNOWN_EXCEPTION;
    private FileType fileType;
    private BizDataType dataType;
    private String message = "";
    private String fileName;
    /** 文件导出目录 */
    private File directoy;

    /** 当前正在导出的文件 */
    private File curFile;
    /** 导出文件夹压缩文件 */
    private File zipFile;

    /** web project 上下文路径*/
    private String contextPath;

    private int fileNum;
    /** 单个文件最大记录数 */
    private int fileRecords;
    /** 已导出文件记录数 */
    private int exportedRecords;

    /** 是否是新创建的文件*/
    private boolean newFile;
    private boolean existsDirectoy = false;

    public ExportInfo(FileType fileType,BizDataType dataType,String contextPath,int fileRecords) {
        this.fileType = fileType;
        this.dataType = dataType;
        this.contextPath = contextPath;
        this.fileRecords = fileRecords;
    }


    public ExportInfo(FileType fileType, BizDataType dataType,
                      String contextPath, String fileName, int fileRecords) {
        this(fileType,dataType,contextPath,fileRecords);
        this.fileName = fileName;
    }

    public void createDirectory(){
        directoy = FileUtil.createExportDir(dataType, fileType, contextPath);
        existsDirectoy = true;
    }

    public void createImportFailedDirectory(){
        directoy = FileUtil.createImportFailedDir(dataType, fileName, contextPath);
        existsDirectoy = true;
    }

    public File getCurrentFile(int size, FileTask task) throws IOException, IllegalArgumentException {
        int current = exportedRecords + size;
        if (exportedRecords == 0 || current > fileRecords) {
            exportedRecords = 0;
            curFile = newFile(null, task);
        } else {
            this.newFile = false;
        }

        exportedRecords += size;
        return curFile;
    }

    private File newFile(String dayStr, FileTask task) {
        if (task == null) {
            throw new NullPointerException("FileTask obj not allowed null");
        }
        String fileTaskName = task.getParameter("name");
        this.newFile = true;
        StringBuilder sb = new StringBuilder();
        sb.append(directoy.getAbsoluteFile());
        sb.append(File.separator);
        if (StringUtils.isNotBlank(fileTaskName)) {
            sb.append(fileTaskName);
        }
        sb.append(System.currentTimeMillis());
        sb.append((fileType == FileType.ExcelX)
                ? FileType.Excel.getType() : fileType.getType());
        sb.toString();
        return new File(sb.toString());
    }

    public File getCurrentFileByDay(int size, String dayStr, boolean isNewDay) throws IOException, IllegalArgumentException {
        int current = exportedRecords + size;
        if (isNewDay || exportedRecords == 0 || current > fileRecords) {
            exportedRecords = 0;
            curFile = newFile(dayStr, null);
        } else {
            this.newFile = false;
        }

        exportedRecords += size;
        return curFile;
    }

    public boolean isNewFile() {
        return newFile;
    }

    public boolean isExistsDirectoy() {
        return existsDirectoy;
    }

    public void setNewFile(boolean newFile) {
        this.newFile = newFile;
    }

    public ExportResult getResult() {
        return result;
    }

    public void setResult(ExportResult result) {
        this.result = result;
    }

    public FileType getFileType() {
        return fileType;
    }

    public BizDataType getDataType() {
        return dataType;
    }

    public File getDirectoy() {
        return directoy;
    }

    public File getZipFile() {
        return zipFile;
    }

    public File createZipFile() {
        String zipFileName = directoy.getName() + FileType.Zip.getType();
        this.zipFile = FileUtil.getExportFile(dataType, zipFileName , contextPath);
        return zipFile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

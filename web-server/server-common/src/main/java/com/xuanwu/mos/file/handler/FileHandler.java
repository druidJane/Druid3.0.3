package com.xuanwu.mos.file.handler;

import com.xuanwu.mos.file.FileHead;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by 林泽强 on 2016/8/22.
 * 文件处理器抽象接口
 */
public interface FileHandler {

    /**
     * 读取文件头
     *
     * @param filePath  文件路径
     * @param delimiter 内容分隔符
     * @return FileHead 文件头对象
     */
    public FileHead readHead(String filePath, String delimiter);

    /**
     * 上传文件
     *
     * @param fileItem
     * @param file
     * @return
     */
    public boolean upload(FileItem fileItem, File file);


    /**
     * 读取文件行数
     * @param file
     * @return
     */
    public int readLineCount(File file);

    /**
     * 读取文件内容
     * @param file
     */
    public void readContent(File file, String delimiter, RowHandler rowHandler);

    /**
     * 导出文件
     * @param file 输出文件
     * @param delimiter 内容分隔符
     * @param isNew 是否新建文件
     * @param cellsList 文件内容列表，包括文件的列头
     * @param charset 文件编码
     * @return
     */
    public void writeFile(File file, String delimiter, boolean isNew, List<String[]> cellsList, Charset charset);

    /**
     * 导出文件
     * @param file 输出文件
     * @param delimiter 内容分隔符
     * @param isNew 是否新建文件
     * @param cellsList 文件内容列表，包括文件的列头
     * @return
     */
    public void writeFile(File file, String delimiter, boolean isNew, List<String[]> cellsList);

    /**
     * 读取文件头及内容
     * @param filePath
     * @param delimiter
     * @param rowHandler
     */
    public void readAll(String filePath, String delimiter, RowHandler rowHandler);
}

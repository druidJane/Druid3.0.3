package com.xuanwu.mos.vo;

/**
 * Created by 郭垚辉 on 2017/5/12.
 */
public class FileParams {
    private String oldName;
    private String newName;
    private String delimiter;

    public FileParams() {
    }

    public FileParams(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}

package com.xuanwu.mos.file.uploader;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.file.FileHead;

import java.io.InputStream;

/**
 * Created by 林泽强 on 2016-8-28. 上传结果
 */
public class UploadResult {
	
	private String originFileName;
	private String newFileName;
	private String fileName;
	private FileHead fileHead;
	private StatusCode statusCode;
	private String delimiter;
	private InputStream is;
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@JsonIgnore
	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}


	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	private Long fileSize;

	public UploadResult(StatusCode statusCode) {
		this(statusCode, null, null);
	}

	public UploadResult(StatusCode statusCode, String fileName, FileHead fileHead) {
		this.fileName = fileName;
		this.fileHead = fileHead;
		this.statusCode = statusCode;
	}

	public String getOriginFileName() {
		return originFileName;
	}

	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileHead getFileHead() {
		return fileHead;
	}

	public void setFileHead(FileHead fileHead) {
		this.fileHead = fileHead;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

}

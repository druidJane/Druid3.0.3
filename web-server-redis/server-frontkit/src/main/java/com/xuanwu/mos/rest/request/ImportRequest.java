package com.xuanwu.mos.rest.request;


import com.xuanwu.mos.file.HeadDMapToF;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by linzeqiang on 2016/11/7. 导入请求
 */
public class ImportRequest {

	private String fileName; // 文件名
	private Long fileSize; // 文件大小
	private String delimiter;
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private List<HeadDMapToF> headDMapToFList; // 导入文件——数据头映射

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	private Map params;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public List<HeadDMapToF> getHeadDMapToFList() {
		return headDMapToFList;
	}

	public void setHeadDMapToFList(List<HeadDMapToF> headDMapToFList) {
		this.headDMapToFList = headDMapToFList;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public boolean isCorrectFileHead() {
		Set<Integer> fileHeadIdxSet = new HashSet<>();
		for (HeadDMapToF headDMapToF : this.headDMapToFList) {
			if (headDMapToF.getFileHeadInfo() == null) { // 有文件列未选择情况
				return false;
			}
			fileHeadIdxSet.add(headDMapToF.getFileHeadInfo().getIndex());
		}
		if (fileHeadIdxSet.size() < headDMapToFList.size()) { // 有文件列重复情况
			return false;
		}
		return true;
	}
}

package com.xuanwu.mos.file;

import java.util.Date;

/**
 * @Description File Info
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2013-11-16
 * @Version 1.0.0
 */
public class FileInfo {

	protected int id;// 主键
	protected String name;// 原文件名
	protected String suffix;// 格式
	protected String path;// 路径
	protected float size;// 文件大小(KB)
	protected Date udate;// 上传日期
	protected FileUploadConfig.FileType type;// 文件类型(1:图片,2:文档,3:音频,4:压缩文件)
	protected int width;// 图片宽度
	protected int height;// 图片宽度
	protected int mlen;// 音频/视频 音轨长度(秒)
	protected FileHead fileHead;	//文件头

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public Date getUdate() {
		return udate;
	}

	public void setUdate(Date udate) {
		this.udate = udate;
	}

	public FileUploadConfig.FileType getType() {
		return type;
	}

	public void setType(FileUploadConfig.FileType type) {
		this.type = type;
	}

	public void setTypeIdx(int idx) {
		this.type = FileUploadConfig.FileType.getType(idx);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMlen() {
		return mlen;
	}

	public void setMlen(int mlen) {
		this.mlen = mlen;
	}

	public FileHead getFileHead() {
		return fileHead;
	}

	public void setFileHead(FileHead fileHead) {
		this.fileHead = fileHead;
	}

}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;


/**
 * Media item
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-13
 * @Version 1.0.0
 */
public interface MediaItem {
	/**
	 * 媒体文件类型
	 */
	public enum MediaType {
		JPG, GIM, MID,AMR, VIDEO_3GP,VIDEO_MP4
	}

	/**
	 * 获得当前文件的原数据
	 * 
	 * @return
	 */
	public MediaType getType();

	/**
	 * 文件元数据信息
	 * 
	 * @return
	 */
	public String getMeta();
	/**
	 * 文件元数据信息
	 * @return
	 */
	public void setMeta(String meta);
	/**
	 * 文件二进制数据
	 * 
	 * @return
	 */
	public byte[] getData();
	
	public void setData(byte[] data);

	/**
	 * Build protobuf data
	 * 
	 * @return
	 */
	public com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem build();
	
}

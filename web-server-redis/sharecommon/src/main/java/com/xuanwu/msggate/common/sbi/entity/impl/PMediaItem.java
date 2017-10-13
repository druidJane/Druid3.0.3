/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import com.google.protobuf.ByteString;

import com.xuanwu.msggate.common.sbi.entity.MediaItem;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-13
 * @Version 1.0.0
 */
public class PMediaItem implements MediaItem {
	/**
	 * The data of the media
	 */
	private byte[] data;

	/**
	 * Media type
	 */
	private MediaType type;

	/**
	 * The metadata of the media
	 */
	private String meta;

	/**
	 * 
	 * @param type
	 * @param meta
	 * @param data
	 */
	public PMediaItem(int type, String meta, byte[] data) {
		this.type = MediaType.values()[type];
		this.meta = meta;
		this.data =data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getData() {
		return data;
	}
	
	@Override
	public void setData(byte[] data){
		this.data = data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMeta() {
		return meta;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMeta(String meta){
		this.meta = meta;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MediaType getType() {
		return type;
	}

	@Override
	public com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem build() {
		com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem.Builder build = com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem
				.newBuilder();
		build.setMeta(meta);
		build.setData(ByteString.copyFrom(data));
		build.setMediaType(type.ordinal());
		return build.build();
	}

	@Override
	public String toString() {
		return "PMediaItem [" + (meta != null ? "meta=" + meta + ", " : "")
				+ (type != null ? "type=" + type : "") + "]";
	}
}

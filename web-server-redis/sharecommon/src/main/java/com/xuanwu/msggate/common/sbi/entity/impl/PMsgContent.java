/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity.impl;

import com.xuanwu.msggate.common.sbi.entity.MediaItem;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-13
 * @Version 1.0.0
 */
public class PMsgContent implements MsgContent {
	/**
	 * Message type
	 */
	private MsgType type;
	/**
	 * The basic text content
	 */
	private String content;
	/**
	 * The item of the medias
	 */
	private List<MediaItem> items = new ArrayList<MediaItem>();
	
	private long attachentTotalLength;
	
	private Set<String> attachmentTypes = Collections.emptySet();
	
	private String title;
	
	private String smil;
	
    public PMsgContent(){
		
	}

	public PMsgContent(MsgType msgType,
			com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent content) {
		this.type = msgType;
		this.content = content.getContent();

		for (com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem mediaItem : content
				.getMediasList()) {
			MediaItem item = new PMediaItem(mediaItem.getMediaType(),
					mediaItem.getMeta(), mediaItem.getData().toByteArray());
			items.add(item);
		}
	}

	@Override
	public String getContent() {
		return content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MediaItem> getMediaItems() {
		return items;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMediaItems(List<MediaItem> items) {
		this.items = items;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MsgType getType() {
		return type;
	}

	@Override
	public com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent build() {
		com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent.Builder builder = com.xuanwu.msggate.common.protobuf.CommonItem.MsgContent
				.newBuilder();
		builder.setContent((content == null) ? "" : content);
		List<com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem> medias = new ArrayList<com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem>();
		for (MediaItem item : items) {
			medias.add(item.build());
		}
		builder.addAllMedias(medias);

		return builder.build();
	}

	@Override
	public String toString() {
		return "PMsgContent [type=" + type + ", content=" + content
				+ ", items=" + items + ", attachentTotalLength="
				+ attachentTotalLength + ", attachmentTypes=" + attachmentTypes
				+ "]";
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public long getAttachmentTotalLength() {
		return attachentTotalLength;
	}

	@Override
	public Set<String> getAttachmentTypes() {
		return attachmentTypes;
	}

	@Override
	public void setAttachmentTotalLength(long attachmentTotalLength) {
		this.attachentTotalLength = attachmentTotalLength;
	}

	@Override
	public void setAttachmentTypes(Set<String> attachmentTypes) {
		this.attachmentTypes = attachmentTypes;
	}

	@Override
	public String getSmil() {
		return smil;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setSmil(String smil) {
		this.smil = smil;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}
}

/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.encode;

public class WordFragment {
	/**
	 * Encode type
	 */
	EncodeType type;
	/**
	 * Content
	 */
	String content;

	public WordFragment(EncodeType type, String content) {
		this.type = type;
		this.content = content;
	}

	/**
	 * Get type
	 * 
	 * @return the type
	 */
	public EncodeType getType() {
		return type;
	}

	/**
	 * Set type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(EncodeType type) {
		this.type = type;
	}

	/**
	 * Get content
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set content
	 * 
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

    @Override
    public String toString() {
        return "WordFragment [type=" + type + ", content=" + content + "]";
    }
}
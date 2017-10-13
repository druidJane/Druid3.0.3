package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.io.Serializable;

/**
 * @Description 切分后的文本内容
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-10-31
 * @Version 1.0.0
 */
public class SubContent extends AbstractEntity {

	@Override
	public Serializable getId() {
		return null;
	}

	public enum SubType {
		/** 文本 */
		TXT, 
		/** 变量 */
		VAR
	}
	
	private SubType type;
	
	private String content;
	
	public SubContent(SubType type, String content) {
		this.type = type;
		this.content = content;
	}

	public SubType getType() {
		return type;
	}
	
	public void setType(SubType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "SubContent [type=" + type + ", content=" + content + "]";
	}
}

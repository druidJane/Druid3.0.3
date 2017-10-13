/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.util;

import com.xuanwu.msggate.common.sbi.entity.MsgContent;

/**
 * Interface of the key filter
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-7-14
 * @Version 1.0.0
 */
public interface KeyFilter {
	public enum KewordType {
		/* 0--通道关键字,1--企业关键字,2--全局关键字 */
		CHANNEL_KEYWORD(0),ENT_KEYWORD(1), GBKEYWORD(2);
		private final int index;

		private KewordType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static KewordType getType(int index) {
			switch (index) {
			case 0: 
			   return CHANNEL_KEYWORD;
			case 1:
				return ENT_KEYWORD;
			case 2:
				return GBKEYWORD;
			default:
				return GBKEYWORD;
			}
		}
	}

	/**
	 * Clear all the key word
	 */
	public abstract void clear();

	/**
	 * Is legal of the content
	 * 
	 * @param content
	 * @return
	 */
	public abstract boolean isLegal(MsgContent msgContent,int type, int targetID);

	/**
	 * Add the filter key word
	 * 
	 * @param keyWord
	 * @return
	 */
	public abstract boolean addFilterKeyWord(String keyWord, int type, int targetID);

}

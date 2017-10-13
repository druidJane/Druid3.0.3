/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.util;

import com.xuanwu.msggate.common.sbi.entity.MsgContent;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-10-27
 * @Version 1.0.0
 */
public class KeyFilterImpl implements KeyFilter {

	private Map<Integer, WmParser> wmParserMap = new ConcurrentHashMap<Integer, WmParser>();

//	private Config config;
	
	public KeyFilterImpl() {
		// Initialize the global keyword
		wmParserMap.put(tran2Key(KewordType.GBKEYWORD.getIndex(), 0),
				new WmParser());
	}

	@Override
	public boolean addFilterKeyWord(String keyWord, int type, int targetID) {
		Integer key = tran2Key(type, targetID);
		WmParser wm = wmParserMap.get(key);
		if (wm == null) {
			wm = new WmParser();
			wmParserMap.put(key, wm);
		}
		return wm.addFilterKeyWord(keyWord);
	}

	@Override
	public boolean isLegal(MsgContent msgContent, int type, int targetID) {
		WmParser wm = wmParserMap.get(tran2Key(type, targetID));
		if (wm == null)
			return true;
		Vector<String> keywordSet = wm.isLegal(msgContent.getContent());
		if (keywordSet.isEmpty())
			return true;
        
//		find keyword and replace
//		String content = replace(keywordSet, msgContent.getContent());
//		msgContent.setContent(content);
		return false;
	}
	
//	private String replace(Vector<String> keywordSet, String source){
//		String target = source;
//		for(String keyword : keywordSet){
//			target = target.replaceAll(keyword, config.getHighlightHtmlTagStrat()+keyword+config.getHighlightHtmlTagEnd());
//		}
//		return target;
//	}

	private Integer tran2Key(int type, int targetID) {
		return Integer.valueOf((targetID << 4) | (type & 0xF));
	}
	
	@Override
	public void clear() {
		for (WmParser wm : wmParserMap.values()) {
			wm.clear();
		}
		
		wmParserMap.clear();
	}
	
//	@Autowired
//	public void setConfig(Config config){
//		this.config = config;
//	}
}

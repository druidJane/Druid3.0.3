/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * WM key word filter<br>
 * <b>Copied from others</b>
 * 
 * @Data 2010-7-14
 * @Version 1.0.0
 */
@SuppressWarnings("unchecked")
public class WmParser {
	private boolean initFlag = false;
	private int maxIndex = (int) java.lang.Math.pow(2, 16);
	private int shiftTable[] = new int[maxIndex];
	public Vector<AtomicPattern> hashTable[] = new Vector[maxIndex];
	private UnionPatternSet tmpUnionPatternSet = new UnionPatternSet();

	private ReentrantLock lock = new ReentrantLock();

	/**
	 * Add filter keyword
	 * 
	 * @param keyWord
	 * @param level
	 * @return
	 */
	public boolean addFilterKeyWord(String keyWord, int level) {
		if (initFlag == true)
			return false;
		UnionPattern unionPattern = new UnionPattern();
		String strArray[] = keyWord.split(" ");
		for (int i = 0; i < strArray.length; i++) {
			if(StringUtils.isBlank(strArray[i]))
				continue;
			
			Pattern pattern = new Pattern(strArray[i]);
			AtomicPattern atomicPattern = new AtomicPattern(pattern);
			unionPattern.addNewAtomicPattrn(atomicPattern);
			unionPattern.setLevel(level);
			atomicPattern.setBelongUnionPattern(unionPattern);
		}
		tmpUnionPatternSet.addNewUnionPattrn(unionPattern);
		return true;
	}

	public boolean addFilterKeyWord(String keyWord) {
		return addFilterKeyWord(keyWord, 1);
	}

	private boolean isValidChar(char ch) {
		return (ch == ' ') ? false : true;
		
		/*if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z')
				|| (ch >= 'a' && ch <= 'z'))
			return true;
		if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))
			return true;
		return false;*/
	}

	public int Parse(String content, Vector<String> keywordSet) {
		if (initFlag == false)
			init();
		Vector<AtomicPattern> aps = new Vector<AtomicPattern>();
		String preContent = preConvert(content);
		for (int i = 0; i < preContent.length();) {
			char checkChar = preContent.charAt(i);
			if (shiftTable[checkChar] == 0) {
				Vector<AtomicPattern> tmpAps = new Vector<AtomicPattern>();
				tmpAps = findMathAps(preContent.substring(0, i + 1),
						hashTable[checkChar]);
				aps.addAll(tmpAps);
				i++;
			} else
				i = i + shiftTable[checkChar];
		}
		parseAtomicPatternSet(aps, keywordSet);
		return 0;
	}

	public Vector<String> isLegal(String content) {
		if (initFlag == false)
			init();
		Vector<String> keywordSet = new Vector<String>();
		Vector<AtomicPattern> aps = new Vector<AtomicPattern>();
		String preContent = preConvert(content);
		for (int i = 0; i < preContent.length();) {
			char checkChar = preContent.charAt(i);
			if (shiftTable[checkChar] == 0) {
				Vector<AtomicPattern> tmpAps = new Vector<AtomicPattern>();
				tmpAps = findMathAps(preContent.substring(0, i + 1),
						hashTable[checkChar]);
				aps.addAll(tmpAps);
				i++;
			} else
				i = i + shiftTable[checkChar];
		}
		parseAtomicPatternSet(aps, keywordSet);
		return keywordSet;
		//if (levelSet.size() > 0)
		//	return false;
		//return true;
	}

	private void parseAtomicPatternSet(Vector<AtomicPattern> aps,
			Vector<String> keywordSet) {
		while (aps.size() > 0) {
			AtomicPattern ap = aps.get(0);
			UnionPattern up = ap.belongUnionPattern;
			if (up.isIncludeAllAp(aps) == true) {
				//levelSet.add(new Integer(up.getLevel()));
				keywordSet.add(ap.getPattern().str);
			}
			aps.remove(0);
		}
	}

	private Vector<AtomicPattern> findMathAps(String src,
			Vector<AtomicPattern> destAps) {
		Vector<AtomicPattern> aps = new Vector<AtomicPattern>();
		for (int i = 0; i < destAps.size(); i++) {
			AtomicPattern ap = destAps.get(i);
			if (ap.findMatchInString(src) == true)
				aps.add(ap);
		}
		return aps;
	}

	private String preConvert(String content) {
		String retStr = new String();
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (this.isValidChar(ch) == true) {
				retStr = retStr + ch;
			}
		}
		return retStr;
	}

	// shift table and hash table of initialize
	private void init() {
		lock.lock();
		try {
			if(initFlag == false) {
				//for (int i = 0; i < maxIndex; i++)
					//hashTable[i] = new Vector<AtomicPattern>();
				shiftTableInit();
				hashTableInit();
				initFlag = true;
			}
		} finally {
			lock.unlock();
		}
	}

	public void clear() {
		tmpUnionPatternSet.clear();
		initFlag = false;
	}

	private void shiftTableInit() {
		for (int i = 0; i < maxIndex; i++)
			shiftTable[i] = 2;
		Vector<UnionPattern> upSet = tmpUnionPatternSet.getSet();
		for (int i = 0; i < upSet.size(); i++) {
			Vector<AtomicPattern> apSet = upSet.get(i).getSet();
			for (int j = 0; j < apSet.size(); j++) {
				AtomicPattern ap = apSet.get(j);
				Pattern pattern = ap.getPattern();
				
				char ch = pattern.charAtEnd(1);
				if (ch != 0 && shiftTable[ch] != 0)
					shiftTable[ch] = 1;
				ch = pattern.charAtEnd(0);
				if (ch != 0)
					shiftTable[ch] = 0;
				
                /* 存在当关键字最后一个字符有重复的情况，过滤不了关键字问题*/
				/*if (pattern.charAtEnd(1) != 0)
					shiftTable[pattern.charAtEnd(1)] = 1;
				if (pattern.charAtEnd(0) != 0)
					shiftTable[pattern.charAtEnd(0)] = 0;*/
			}
		}
	}

	private void hashTableInit() {
		Vector<UnionPattern> upSet = tmpUnionPatternSet.getSet();
		for (int i = 0; i < upSet.size(); i++) {
			Vector<AtomicPattern> apSet = upSet.get(i).getSet();
			for (int j = 0; j < apSet.size(); j++) {
				AtomicPattern ap = apSet.get(j);
				Pattern pattern = ap.getPattern();
				if (pattern.charAtEnd(0) != 0) {
					if(hashTable[pattern.charAtEnd(0)] == null)
						hashTable[pattern.charAtEnd(0)] = new Vector<AtomicPattern>(); 
					hashTable[pattern.charAtEnd(0)].add(ap);
				}
			}
		}
	}
}

class Pattern { // string
	Pattern(String str) {
		this.str = str;
	}

	public char charAtEnd(int index) {
		if (str.length() > index) {
			return str.charAt(str.length() - index - 1);
		} else
			return 0;
	}

	public String str;

	public String getStr() {
		return str;
	};
}

class AtomicPattern {
	public boolean findMatchInString(String str) {
		if (this.pattern.str.length() > str.length())
			return false;
		int beginIndex = str.length() - this.pattern.str.length();
		String eqaulLengthStr = str.substring(beginIndex);
		if (this.pattern.str.equalsIgnoreCase(eqaulLengthStr))
			return true;
		return false;
	}

	AtomicPattern(Pattern pattern) {
		this.pattern = pattern;
	};

	private Pattern pattern;
	public UnionPattern belongUnionPattern;

	public UnionPattern getBelongUnionPattern() {
		return belongUnionPattern;
	}

	public void setBelongUnionPattern(UnionPattern belongUnionPattern) {
		this.belongUnionPattern = belongUnionPattern;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}

class SameAtomicPatternSet {
	SameAtomicPatternSet() {
		SAPS = new Vector<AtomicPattern>();
	};

	public Vector<AtomicPattern> SAPS;
}

class UnionPattern { // union string
	UnionPattern() {
		this.apSet = new Vector<AtomicPattern>();
	}

	public Vector<AtomicPattern> apSet;

	public void addNewAtomicPattrn(AtomicPattern ap) {
		this.apSet.add(ap);
	}

	public Vector<AtomicPattern> getSet() {
		return apSet;
	}

	public boolean isIncludeAllAp(Vector<AtomicPattern> inAps) {
		if (apSet.size() > inAps.size())
			return false;
		for (int i = 0; i < apSet.size(); i++) {
			AtomicPattern ap = apSet.get(i);
			if (isInAps(ap, inAps) == false)
				return false;
		}
		return true;
	}

	private boolean isInAps(AtomicPattern ap, Vector<AtomicPattern> inAps) {
		for (int i = 0; i < inAps.size(); i++) {
			AtomicPattern destAp = inAps.get(i);
			if (ap.getPattern().str.equalsIgnoreCase(destAp.getPattern().str) == true)
				return true;
		}
		return false;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	private int level;
}

class UnionPatternSet { // union string set
	UnionPatternSet() {
		this.unionPatternSet = new Vector<UnionPattern>();
	}

	public void addNewUnionPattrn(UnionPattern up) {
		this.unionPatternSet.add(up);
	}

	public Vector<UnionPattern> unionPatternSet;

	public Vector<UnionPattern> getSet() {
		return unionPatternSet;
	}

	public void clear() {
		unionPatternSet.clear();
	}
}

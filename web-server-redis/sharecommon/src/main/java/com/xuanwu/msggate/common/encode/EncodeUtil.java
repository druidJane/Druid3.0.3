/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.encode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Encode util
 * 
 * @author <a href="mailto:wanglianguang@139130.net>LianGuang Wang</a>
 * @Data 2010-6-3
 * @Version 1.0.0
 */
public class EncodeUtil {
	/**
	 * ASCII max length
	 */
	private final static int ASC_MAX_LENGTH = 160;

	/**
	 * Mix GB char max length
	 */
	private final static int MIX_MAX_LENGTH = 140;

	/**
	 * Encode the string to GBK byte
	 * 
	 * @param src
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encode2GBK(String src) {
		if (src == null)
			return new byte[] {};
		try {
			return src.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	/**
	 * Encode the string to ucs2 bytes
	 * 
	 * @param src
	 * @return
	 */
	public static byte[] encode2UCS2(String src) {
		if (src == null)
			return new byte[] {};
		try {
			return src.getBytes("UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	/**
	 * Decode to String from ASCII bytes
	 * 
	 * @param src
	 * @return
	 */
	public static byte[] encode2ASCII(String src) {
		if (src == null)
			return new byte[] {};
		try {
			return src.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	/**
	 * Decode to String from GBK bytes
	 * 
	 * @param target
	 * @return
	 */
	public static String decode2GBK(byte[] target) {
		try {
			return new String(target, "GBK");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	/**
	 * Decode to String from UCS2 bytes
	 * 
	 * @param target
	 * @return
	 */
	public static String decode2UCS2(byte[] target) {
		try {
			return new String(target, "UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	/**
	 * Decode to String from UCS2 bytes
	 * 
	 * @param target
	 * @return
	 */
	public static String decode2UCS2DrainZero(byte[] target) {
		return decodeDrainZero(target, "UTF-16BE");
	}

	public static String decode2GBKDrainZero(byte[] target) {
		return decodeDrainZero(target, "GBK");
	}

	public static String decode2ASCIIDrainZero(byte[] target) {
		return decodeDrainZero(target, "UTF-8");
	}

	public static String decodeDrainZero(byte[] target, String code) {
		try {
			byte[] a = new byte[(target.length)];
			int count = 0;
			for (int i = 0; i < target.length; i++) {
				if (target[i] != 0) {
					a[count++] = target[i];
				}
			}
			return new String(a, 0, count, code);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	/**
	 * Get the GBK code length
	 * 
	 * @param src
	 * @return
	 */
	public static int getGBKLength(String src) {
		try {
			return src.getBytes("GBK").length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	/**
	 * Splice String used the GBK code
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String[] spliceGBK(String src, int length) {
		if (src == null || length < 2)
			return new String[] {};

		List<String> temp = new ArrayList<String>();

		try {
			byte[] bytes = src.getBytes("GBK");
			int count = 0;
			int cmpL = 1;
			int point = 0;
			while (count < bytes.length) {
				// if it's two-bytes char
				if ((bytes[count] & 0x80) != 0) {
					count++;
					cmpL++;
				}

				if (cmpL > length) {
					count -= 2;
					cmpL -= 2;
					temp.add(new String(bytes, point, cmpL, "GBK"));
					point = point + cmpL;
					cmpL = 0;
				}
				if (cmpL == length || count == (bytes.length - 1)) {
					temp.add(new String(bytes, point, cmpL, "GBK"));
					point = point + cmpL;
					cmpL = 0;
				}

				count++;
				cmpL++;
			}
			return temp.toArray(new String[0]);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	public static String[] spliceUCS2(String src, int length) {
		if (src == null || length < 2)
			return new String[] {};

		List<String> temp = new ArrayList<String>();

		try {
			byte[] bytes = src.getBytes("UTF-16BE");
			int count = 1;
			int point = 0;
			while (count * 2 <= bytes.length) {
				if ((count * 2 - point) > length) {
					temp.add(new String(bytes, point, (count - 1) * 2 - point,
							"UTF-16BE"));
					count -= 1;
					point = count * 2;

				}
				if ((count * 2 - point) == length
						|| (count * 2 == bytes.length)) {
					temp.add(new String(bytes, point, count * 2 - point,
							"UTF-16BE"));
					point = count * 2;
				}
				count++;
			}
			return temp.toArray(new String[0]);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	public static boolean isASCII(String content) {
		byte[] bytes;
		try {
			bytes = content.getBytes("GBK");
			for (int i = 0; i < bytes.length; i++) {
				if ((bytes[i] & 0x80) != 0) {
					return false;
				}
			}
			return true;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
	}

	public static List<WordFragment> spliceContent(String content) {
		return spliceContent(content, ASC_MAX_LENGTH, MIX_MAX_LENGTH, 0);
	}
	
	/**
	 * 计算段数，全部以UTF_16BE编码的字节数，为偶数
	 * @param contentLen 内容字节数
	 * @param mixLen 每段最大字节数
	 * @param extLen 每段须保留的字节数
	 * @return 段数
	 */
	public static int simpleSpliceCount(int contentLen, int mixLen, int extLen){
		if(contentLen % 2 != 0 || mixLen % 2 != 0 || extLen % 2 != 0){
			throw new IllegalArgumentException("The contentLen, mixLen or extLen must be a even number, " + "contentLen:"
					+ contentLen + "mixLen:" + mixLen + " extLen:" + extLen);
		}
		int segLen = mixLen - extLen;
		if(segLen > 0){
			return (int)Math.ceil((double)contentLen / (double)segLen);
		} else {
			throw new RuntimeException("The segLen should be larger than zero, segLen:" + segLen);
		}
	}

	/**
	 * 内容分词
	 * 
	 * @param content
	 *            内容
	 * @param mixLength
	 *            每段最大字节数
	 * @param extendSpace
	 *            预留字节数
	 * @return
	 */
	public static List<WordFragment> simpleSpliceContent(String content,
			int mixLength, int extendSpace) {
		if (mixLength == 0) {
			throw new RuntimeException("The splice lenth can't be zero!");
		}
		List<WordFragment> temp = new ArrayList<WordFragment>();
		if (StringUtils.isEmpty(content)){
			temp.add(new WordFragment(EncodeType.UCS2, content));
			return temp;
		}

		try {
			byte[] bytes = content.getBytes("UTF-16BE");

			// consider the extend space for the multiple sections
			mixLength -= extendSpace;

			// quickly calculate the length,because the length is shorter than
			// the mixLenth in the most situations
			// doesn't consider the all ascii content,because the content
			// contains GB char under most situations.
			if (bytes.length <= mixLength) {
				temp.add(new WordFragment(EncodeType.UCS2, content));
				return temp;
			}

			String[] tempArr = spliceUCS2(content, mixLength);
			for (String str : tempArr) {
				temp.add(new WordFragment(EncodeType.UCS2, str));
			}

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
		return temp;
	}

	/**
	 * 简单分词工具<br>
	 * <b>针对当前所发的信息绝大多数是中文的情况下，只考虑中文字符串，以加速整个分词的性能</b>
	 * 
	 * @param content
	 * @param ascLength
	 * @param mixLength
	 * @param extendSpace
	 * @return
	 */
	public static List<WordFragment> simpleSpliceContentGBK(String content,
			int mixLength, int extendSpace) {
		if (mixLength == 0) {
			throw new RuntimeException("The splice lenth can't be zero!");
		}
		List<WordFragment> temp = new ArrayList<WordFragment>();
		if (StringUtils.isEmpty(content))
			return temp;

		byte[] byteCount = new byte[content.length()];

		try {
			byte[] bytes = content.getBytes("GBK");

			// consider the extend space for the multiple sections
			mixLength -= extendSpace;

			// quickly calculate the length,because the length is shorter than
			// the mixLenth in the most situations
			// doesn't consider the all asciicontent,because the content
			// contains GB char under most situations.
			if (bytes.length <= mixLength) {
				temp.add(new WordFragment(EncodeType.GB, content));
				return temp;
			}

			int endPoint = 0;
			int i = 0;
			// splice to analysis the content char
			while (i < bytes.length) {
				if ((bytes[i] & 0x80) != 0) {
					byteCount[endPoint++] = 2;
					i += 2;
				} else {
					byteCount[endPoint++] = 1;
					i++;
				}
			}
			int basePoint = 0;
			int vPoint = 0;
			int mixPoint = 0;
			int mixCount = 0;
			while (true) {
				mixPoint = 0;
				mixCount = 0;
				while ((basePoint + mixPoint) < endPoint) {
					mixCount += byteCount[basePoint + mixPoint];
					if (mixCount == mixLength)
						break;
					if (mixCount > mixLength) {
						mixCount -= byteCount[basePoint + mixPoint];
						mixPoint--;
						break;
					}
					mixPoint++;
				}
				temp.add(new WordFragment(EncodeType.GB, new String(bytes,
						vPoint, mixCount, "GBK")));
				basePoint += mixPoint + 1;
				vPoint += mixCount;
				if (!(vPoint < bytes.length)) {
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
		return temp;
	}

	/**
	 * 分词工具
	 * 
	 * @param content
	 *            待分词内容
	 * @param ascLength
	 *            ASCII码最大容纳长度
	 * @param mixLength
	 * @param extendSpace
	 * @return
	 */
	public static List<WordFragment> spliceContent(String content,
			int ascLength, int mixLength, int extendSpace) {
		List<WordFragment> temp = new ArrayList<WordFragment>();
		if (content == null)
			return Collections.emptyList();

		byte[] byteCount = new byte[content.length()];

		try {
			byte[] bytes = content.getBytes("GBK");
			// quickly calculate the length,because the length is shorter than
			// the mixLenth in the most situations
			// doesn't consider the all ascii content,because the content
			// contains GB char under most situations.
			if (bytes.length <= mixLength) {
				temp.add(new WordFragment(EncodeType.GB, content));
				return temp;
			}

			// consider the extend space for the multiple sectionsj
			ascLength -= extendSpace;
			mixLength -= extendSpace;

			int endPoint = 0;
			int i = 0;
			while (i < bytes.length) {
				if ((bytes[i] & 0x80) != 0) {
					byteCount[endPoint++] = 2;
					i += 2;
				} else {
					byteCount[endPoint++] = 1;
					i++;
				}
			}

			int basePoint = 0;
			int vPoint = 0;
			while (basePoint < endPoint) {
				int ascPoint = -1;
				int ascCount = 0;
				while (byteCount[basePoint + ascPoint + 1] != 2
						&& (basePoint + ascPoint + 1) < endPoint) {
					ascPoint++;
					ascCount++;
					if (ascCount == ascLength)
						break;
				}

				int mixPoint = -1;
				int mixCount = 0;
				while ((basePoint + mixPoint + 1) < endPoint) {
					mixPoint++;
					mixCount += byteCount[basePoint + mixPoint];
					if (mixCount == mixLength)
						break;
					if (mixCount > mixLength) {
						mixCount -= byteCount[basePoint + mixPoint];
						mixPoint--;
						break;
					}
				}

				if (ascCount >= mixCount) {
					temp.add(new WordFragment(EncodeType.ASC, new String(bytes,
							vPoint, ascCount, "GBK")));
					basePoint += ascPoint + 1;
					vPoint += ascCount;
				} else {
					temp.add(new WordFragment(EncodeType.GB, new String(bytes,
							vPoint, mixCount, "GBK")));
					basePoint += mixPoint + 1;
					vPoint += mixCount;
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
		return temp;
	}

	public static String decodeStringDrainZero(byte[] readRawBytes,
			EncodeType format) {
		switch (format) {
		case UCS2:
			return EncodeUtil.decode2UCS2DrainZero(readRawBytes);
		case GB:
			return EncodeUtil.decode2GBKDrainZero(readRawBytes);
		default:
			return EncodeUtil.decode2ASCIIDrainZero(readRawBytes);
		}
	}

	public static String gentStringEncode(byte[] readRawBytes, EncodeType encode)
			throws UnsupportedEncodingException {
		switch (encode) {
		case ASC:
			new String(readRawBytes, "UTF-8");
		case GB:
			return new String(readRawBytes, "GBK");
		case UCS2:
			return new String(readRawBytes, "UTF-16BE");
		case USIM:
			// 北京农信银确定编码而已,非标准编码形式
			//return new String(readRawBytes, "GBK");
			return HexUtils.byteArrayToHexString(readRawBytes);
		default:
			return new String(readRawBytes, "UTF-8");
		}
	}
	
	public static int computeStringSize(String content, EncodeType encode) {
		try {
			switch (encode) {
			case ASC:
				return content.getBytes("UTF-8").length;
			case UCS2:
				return content.getBytes("UTF-16BE").length;
			case GB:
				return content.getBytes("GBK").length;
			case USIM:
				// 北京农信银确定编码而已,非标准编码形式
				//return content.getBytes("GBK").length;
				return encodeString(content, encode).length;
			default:
				return content.getBytes("UTF-8").length;
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encode not supported.", e);
		}
	}

	public static byte[] encodeString(String content, EncodeType encode) {
		try {
			switch (encode) {
			case ASC:
				return content.getBytes("UTF-8");
			case UCS2:
				return content.getBytes("UTF-16BE");
			case GB:
				return content.getBytes("GBK");
			case USIM:
				// 北京农信银确定编码而已,非标准编码形式
				//return content.getBytes("GBK");
				return HexUtils.hexStringToByteArray(content);
			default:
				return content.getBytes("UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encode not supported.", e);
		}
	}

}

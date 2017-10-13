package com.xuanwu.msggate.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MsgIDUtil {
	private final static SimpleDateFormat SDF_SGIP = new SimpleDateFormat("MMddHHmmss");
	
	public static long parseCmpp(byte[] bytes){		
		int month = 0x0f & (bytes[0]>>4);
		int day = (0x1e & (bytes[0]<<1)) + (0x01 & (bytes[1]>>7));
		int hour = 0x1f & (bytes[1]>>2);
		int minute = (0x30 & (bytes[1]<<4)) + (0x0f & (bytes[2]>>4)); 
		int second = (0x3c & (bytes[2]<<2)) + (0x03 & (bytes[3]>>6));
		//int gwCode = (((0x3f & bytes[3])<<16)& 0xff0000) + ((bytes[4]<<8) & 0xff00) + (bytes[5]&0xff);
		int seq = ((bytes[6]<<8) & 0xff00) + (bytes[7]&0xff);
		
		StringBuffer strBuf = new StringBuffer();
		return Long.parseLong(strBuf.append(month).
				append(day).
				append(hour).
				append(minute).
				append(second).
				/*append(gwCode).*/
				append(seq).toString());
	}
	
	public static String parseSmgp(byte[] paramBuffer) {
		int length = paramBuffer.length;
		int i = 0, k = 0;
		String ud = "";
		String temp = new String("");
		for (i = 0; i < length; i++) {
			k = paramBuffer[i];
			if (k < 0)
				k = k + 256;
			temp = Integer.toHexString(k);
			if (temp.length() == 1)
				temp = "0" + temp;
			ud = ud + temp;
		}
		return ud.toUpperCase();
	}

	/**
	 * 获得移动序列号编码中的日期时间
	 * 
	 * @param msgId
	 *            移动序列号规则:8个字节(64位)
	 * @return 序列号解析出来的日期, 如果解析异常返回空
	 */
	public static Date parseCmppMsgIdToDate(byte[] msgId) {
		Date date = null;
		try {
			Calendar curCal = Calendar.getInstance();
			curCal.setTimeInMillis(System.currentTimeMillis());
			int curYear = curCal.get(Calendar.YEAR);
			int curMonth = curCal.get(Calendar.MONTH);

			int month = (0x0f & (msgId[0] >> 4)) - 1; // 序列号中用1~12表示月份,Calendar中用0~11表示月份
			int day = (0x1e & (msgId[0] << 1)) + (0x01 & (msgId[1] >> 7));
			int hour = 0x1f & (msgId[1] >> 2);
			int minute = (0x30 & (msgId[1] << 4)) + (0x0f & (msgId[2] >> 4));
			int second = (0x3c & (msgId[2] << 2)) + (0x03 & (msgId[3] >> 6));
			// 跨年情况, 年份减一(当前月份与当前月份不一致, 而且序列号的解析月份为12月)
			if (curMonth != month && month == 11 && curMonth == 0) {
				curYear = curMonth - 1;
			}
			curCal.set(curYear, month, day, hour, minute, second);
			date = curCal.getTime();
		} catch (Exception e) {
			// 如果日期解析异常, 返回空日期
			date = null;
		}
		return date;
	}
	
	/**
	 * 获得联通序列号编码中的日期时间
	 * 
	 * @param sqeuenceArr
	 *            联通序列号长整型数组
	 * @return 序列号解析出来的日期, 如果解析异常返回空
	 */
	public static Date parseSgipSqeToDate(long[] sqeuenceArr) {
		Date date = null;
		try {
			if (sqeuenceArr.length > 2) {
				long dateLong = sqeuenceArr[1];// (32位)命令产生的日期和时间
				// input.readFixed32Long();// (32位)由0开始，循环进位，直到进位满了之后再清零
				String dateStr = String.valueOf(dateLong);
				// 月份高位为0,需要补0. 比如 204165307, 其实是0204165307(02-04 16:30:07)
				for (int i = dateStr.length(); i < 10; i++) {
					dateStr = "0" + dateStr;
				}
				Date seqDate = SDF_SGIP.parse(dateStr); // 格式化后日期
				Calendar seqDateCal = Calendar.getInstance();// 日期转换成日历对象
				seqDateCal.setTime(seqDate);

				Calendar curCal = Calendar.getInstance(); // 当前日历对象
				curCal.setTimeInMillis(System.currentTimeMillis());

				// 判断是否跨年(当前月份与当前月份不一致, 而且序列号的解析月份为12月)
				if (seqDateCal.get(Calendar.MONTH) != curCal.get(Calendar.MONTH) && seqDateCal.get(Calendar.MONTH) == 11 && curCal.get(Calendar.MONTH) == 0) {
					seqDateCal.set(Calendar.YEAR, (curCal.get(Calendar.YEAR) - 1));
				} else {
					seqDateCal.set(Calendar.YEAR, (curCal.get(Calendar.YEAR)));
				}
				seqDate = seqDateCal.getTime();
			}
		} catch (Exception e) {
			date = null;
		}
		return date;
	}
}

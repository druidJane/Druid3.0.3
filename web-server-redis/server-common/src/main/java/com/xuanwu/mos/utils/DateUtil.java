/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO.
 *             All rights reserved
 */
package com.xuanwu.mos.utils;

import com.xuanwu.mos.dto.QueryParameters;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.xuanwu.mos.utils.DateUtil.DateTimeType.DateTime;

/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-5-21
 * @Version 1.0.0
 */
public class DateUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DateUtil.class);

	public static final long RAW_OFF_SET = TimeZone.getDefault().getRawOffset();

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd hh:mm:ss";
	public static final FastDateFormat defaultFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final FastDateFormat DATE_FORMAT_FORMAT = FastDateFormat.getInstance(DATE_FORMAT);

	// 取date中的时分秒
	public static final FastDateFormat hourFormat = FastDateFormat.getInstance("HH:mm:ss");
	//
	public static final FastDateFormat ustFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",TimeZone.getTimeZone("UTC"));

	public static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	public static final FastDateFormat DATETIME_FORMAT_FORMAT = FastDateFormat.getInstance(DATETIME_FORMAT);


	public static final String MONTH_FORMAT = "yyyy-MM";
	public static final FastDateFormat MONTH_FORMAT_FORMAT =FastDateFormat.getInstance(MONTH_FORMAT);

	public static final String YEAR_FORMAT = "yyyy";
	public static final FastDateFormat YEAR_FORMAT_FORMAT =FastDateFormat.getInstance(YEAR_FORMAT);

	/**
	 * Number of milliseconds in a standard second.
	 */
	public static final long MILLIS_PER_SECOND = 1000;

	/**
	 * Number of milliseconds in a standard minute.
	 */
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

	/**
	 * Number of milliseconds in a standard hour.
	 */
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

	/**
	 * Number of milliseconds in a standard day.
	 */
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	public enum DateTimeType {
		DateTime(0), // 日期时间
		Date(1), // 日期
		Time(2), // 时间
		Month(3), // 年月
		ExcelDefaultDate(4), // Excel时间格式
		CnTxtDefaultDate(5),// 中文时间格式
		MonthDate(6);//月日

		private int index;

		private DateTimeType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static DateTimeType getType(int index) {
			switch (index) {
				case 1:
					return Date;
				case 2:
					return Time;
				case 3:
					return Month;
				case 4:
					return ExcelDefaultDate;
				case 5:
					return CnTxtDefaultDate;
				case 6:
					return MonthDate;
				default:
					return DateTime;
			}
		}
	}

	private static final FastDateFormat sdfDate = FastDateFormat.getInstance(
			"yyyy-MM-dd");
	private static final FastDateFormat sdfTime =FastDateFormat.getInstance(
			"HH:mm:ss");

	private static final FastDateFormat sdf = FastDateFormat.getInstance(
			"yyyy-MM-dd HH:mm:ss");

	private static final FastDateFormat sdfMonth =FastDateFormat.getInstance(
			"yyyy-MM");

	private static final FastDateFormat sdfExcelDefaultDate = FastDateFormat.getInstance(
			"yyyy/MM/dd");

	private static final FastDateFormat sdfCnTxtDefaultDate = FastDateFormat.getInstance(
			"yyyy年MM月dd日");

	private static final SimpleDateFormat sdfMonthDate = new SimpleDateFormat(
			"MMdd");

	public static String format(Date date, DateTimeType type) {
		if (date == null) {
			return "";
		}
		switch (type) {
			case Date:
				return sdfDate.format(date);
			case Time:
				return sdfTime.format(date);
			case Month:
				return sdfMonth.format(date);
			case ExcelDefaultDate:
				return sdfExcelDefaultDate.format(date);
			case CnTxtDefaultDate:
				return sdfCnTxtDefaultDate.format(date);
			case MonthDate:
				return sdfMonthDate.format(date);
			default:
				return sdf.format(date);
		}
	}
	public static String format(Date date) {
		return format(date, DateTime);
	}

	public static Date parse(String str, DateTimeType type) {
		if (StringUtils.isBlank(str))
			return null;
		try {
			switch (type) {
				case Date:
					return sdfDate.parse(str);
				case Time:
					return sdfTime.parse(str);
				case ExcelDefaultDate:
					return sdfExcelDefaultDate.parse(str);
				case CnTxtDefaultDate:
					return sdfCnTxtDefaultDate.parse(str);
				default:
					return sdf.parse(str);
			}
		} catch (ParseException e) {
			logger.error("Parse date error:" + str);
			return null;
			// throw new RuntimeException("Parse date error.", e);
		}
	}

	public static Date tranDate(long time) {
		if (time == 0)
			return null;
		return new Date(time);
	}

	public static long parseDate(Date time) {
		return time == null ? 0 : time.getTime();
	}

	public static void sleepWithoutInterrupte(long time) {
		while (true) {
			try {
				Thread.sleep(time);
				break;
			} catch (InterruptedException e) {
				logger.error("Interrupted when sleep! this will be ignored!", e);
			}
		}
	}

	public static void sleepNanoWithoutInterrupte(long nanoseconds) {
		while (true) {
			try {
				TimeUnit.NANOSECONDS.sleep(nanoseconds);
				break;
			} catch (InterruptedException e) {
				logger.error("Interrupted when sleep! this will be ignored!", e);
			}
		}
	}

	public static String getCurrentDate() {
		FastDateFormat sf =  FastDateFormat.getInstance(DEFAULT_FORMAT);
		return sf.format(new Date());
	}

	public static String getCurrentDayBegin() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		return sdf.format(begin.getTime());
	}

	public static Date getCurDayBeginDate() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		return begin.getTime();
	}

	public static String getCurrentDayEnd() {
		Calendar end = Calendar.getInstance();
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		return sdf.format(end.getTime());
	}

	public static Date getCurDayEndDate() {
		Calendar end = Calendar.getInstance();
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		return end.getTime();
	}

	public static int getCurMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static boolean equalsDay(Date time1, Date time2) {
		return (fixTime(time1.getTime()) / DateUtil.MILLIS_PER_DAY == fixTime(time2.getTime()) / DateUtil.MILLIS_PER_DAY);
	}

	public static boolean equalsCurDay(Date time) {
		return (fixTime(System.currentTimeMillis()) / DateUtil.MILLIS_PER_DAY == fixTime(time.getTime()) / DateUtil.MILLIS_PER_DAY);
	}

	public static long fixTime(long time) {
		return time + RAW_OFF_SET;
	}

	public static long getCurrentSecond() {
		long second = System.currentTimeMillis() / 1000;
		return second;
	}

	public static String getRealCurMonthFirstDay() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.DAY_OF_MONTH, 1);
		return sdfDate.format(begin.getTime());
	}

	public static String getRealCurMonthFirstDayTime() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.DAY_OF_MONTH, 1);
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		return sdf.format(begin.getTime());
	}

	public static String getCurMonthFirstDay() {
		Calendar begin = Calendar.getInstance();
		if (begin.get(Calendar.DAY_OF_MONTH) == 1) {
			begin.set(Calendar.MONTH, begin.get(Calendar.MONTH) - 1);
		}
		begin.set(Calendar.DAY_OF_MONTH, 1);
		return sdfDate.format(begin.getTime());
	}

	public static String getCurMonthLastDay() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.DATE, begin.getActualMaximum(Calendar.DATE));
		return sdfDate.format(begin.getTime());
	}

	public static String getCurMonthFirstDayTime() {
		Calendar begin = Calendar.getInstance();
		if (begin.get(Calendar.DAY_OF_MONTH) == 1) {
			begin.set(Calendar.MONTH, begin.get(Calendar.MONTH) - 1);
		}
		begin.set(Calendar.DAY_OF_MONTH, 1);
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		return sdf.format(begin.getTime());
	}

	public static String getCurMonthLastDayTime() {
		Calendar end = Calendar.getInstance();
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.DAY_OF_MONTH,
				end.getActualMaximum(Calendar.DAY_OF_MONTH));
		return sdf.format(end.getTime());
	}

	public static String getCurPreviousDay() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH) - 1);
		return sdfDate.format(begin.getTime());
	}

	public static Date parseDate(String date) {
		try {
			if (StringUtils.isEmpty(date)) {
				return null;
			}
			return sdfDate.parse(date);
		} catch (ParseException e) {
			logger.error("Parse date error:" + date);
			return null;
		}
	}

	public static Date parseMonth(String date) {
		try {
			if (StringUtils.isEmpty(date)) {
				return null;
			}
			return sdfMonth.parse(date);
		} catch (ParseException e) {
			logger.error("Parse date error:" + date);
			return null;
		}
	}

	public static String getCurYearFirstMonth() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.MONTH, 0);
		return sdfMonth.format(begin.getTime());
	}

	public static String getCurPreviousMonth() {
		Calendar begin = Calendar.getInstance();
		int month = begin.get(Calendar.MONTH);
		if (month > 1)
			begin.set(Calendar.MONTH, begin.get(Calendar.MONTH) - 1);
		return sdfMonth.format(begin.getTime());
	}

	public static String getCurYearMonth() {
		Calendar begin = Calendar.getInstance();
		return sdfMonth.format(begin.getTime());
	}

	public static String getDayBeginTime() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		return sdfTime.format(begin.getTime());
	}

	public static String getDayEndTime() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.HOUR_OF_DAY, 23);
		begin.set(Calendar.MINUTE, 59);
		begin.set(Calendar.SECOND, 59);
		return sdfTime.format(begin.getTime());
	}

	public static String toMonthFirstDay(String month) {
		return month + "-01";
	}

	public static String toMonthLastDay(String month) {
		try {
			Date d = sdfMonth.parse(month);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return sdfDate.format(cal.getTime());
		} catch (ParseException e) {
			logger.error("Parse date error:" + month);
			return null;
		}
	}

	public static int getDays(String beginDate, String endDate) {
		try {
			Date d1 = sdfDate.parse(beginDate);
			Date d2 = sdfDate.parse(endDate);
			return (int) ((d2.getTime() - d1.getTime()) / (3600L * 1000 * 24));
		} catch (ParseException e) {
			logger.error("Parse date error:" + beginDate + " / " + endDate);
			return 0;
		}
	}
	
	public static String getCurDay() {
		return sdfDate.format(new Date());
	}

	public static int getMonths(String beginDate, String endDate) {
		try {
			Date d1 = sdfMonth.parse(beginDate);
			Date d2 = sdfMonth.parse(endDate);
			Calendar c1 = Calendar.getInstance();
			c1.setTime(d1);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(d2);

			int beginDateYear = c1.get(Calendar.YEAR);
			int beginDateMonth = c1.get(Calendar.MONTH);
			int endDateYear = c2.get(Calendar.YEAR);
			int endDateMonth = c2.get(Calendar.MONTH);
			if (beginDateYear == endDateYear) {
				return endDateMonth - beginDateMonth + 1;
			} else {
				return (endDateYear - beginDateYear) * 12
						+ (endDateMonth - beginDateMonth) + 1;
			}
		} catch (ParseException e) {
			logger.error("Parse date error:" + beginDate + " / " + endDate);
			return 0;
		}
	}

	/**
	 * 取得几天后的时间
	 */
	public static Date getAfterDate(Integer day) {
		Date date = new Date();
		if (day == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, day);
		date = cal.getTime();
		return date;
	}

	/**
	 * 取得几天前的时间
	 */
	public static Date getPrefixStartDate(Integer day) {
		Date date = new Date();
		if (day == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		date = cal.getTime();
		return date;
	}

	/**
	 * 取得几天后的截止时间
	 */
	public static Date getAfterEndDate(Integer day) {
		Date date = new Date();
		if (day == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		date = cal.getTime();
		return date;
	}

	/**
	 * 获得两个时间之间的天数，不计算时分秒
	 */
	public static int getDays(Date beginDate, Date endDate) {
		if (beginDate == null || endDate == null)
			return 0;
		beginDate = parse(format(beginDate, DateTimeType.Date),
				DateTimeType.Date);
		endDate = parse(format(endDate, DateTimeType.Date), DateTimeType.Date);
		return (int) ((endDate.getTime() - beginDate.getTime()) / (3600 * 1000 * 24));
	}

	//支付宝
	/** 年月日时分秒(无下划线) yyyyMMddHHmmss */
	public static final String dtLong                  = "yyyyMMddHHmmss";

	/** 完整时间 yyyy-MM-dd HH:mm:ss */
	public static final String simple                  = "yyyy-MM-dd HH:mm:ss";

	/** 年月日(无下划线) yyyyMMdd */
	public static final String dtShort                 = "yyyyMMdd";
	/**
	 * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
	 * @return
	 *      以yyyyMMddHHmmss为格式的当前系统时间
	 */
	public  static String getOrderNum(){
		Date date=new Date();
		FastDateFormat df=FastDateFormat.getInstance(dtLong);
		return df.format(date);
	}

	/**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public  static String getDateFormatter(){
		Date date=new Date();
		FastDateFormat df=FastDateFormat.getInstance(simple);
		return df.format(date);
	}

	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
	 * @return
	 */
	public static String getDate(){
		Date date=new Date();
		FastDateFormat df=FastDateFormat.getInstance(dtShort);
		return df.format(date);
	}
	/**
	 * 返回系统当前时间(精确到毫秒),用于发送短彩信批次名称
	 * @return
	 *      以yyyyMMddHHmmss为格式的当前系统时间
	 */
	public  static String getDateForBatchName(){
		Date date=new Date();
		FastDateFormat df=FastDateFormat.getInstance(dtLong);
		return df.format(date);
	}
	/**
	 * 产生随机的三位数
	 * @return
	 */
	public static String getThree(){
		Random rad=new Random();
		return rad.nextInt(1000)+"";
	}

	private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
		put("^\\d{4}$", "yyyy");
		put("^\\d{4}年$", "yyyy年");
		put("^\\d{4}-\\d{1,2}$", "yyyy-MM");
		put("^\\d{4}年\\d{1,2}月$", "yyyy年MM月");
		put("^\\d{4}/\\d{1,2}$", "yyyy/MM");
		put("^\\d{8}$", "yyyyMMdd");
		put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
		put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
		put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
		put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
		put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
		put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
		put("^\\d{12}$", "yyyyMMddHHmm");
		put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
		put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
		put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
		put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
		put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
		put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
		put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
		put("^\\d{14}$", "yyyyMMddHHmmss");
		put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
		put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
		put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
		put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
		put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
		put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
		put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
	}};

	/**
	 * Determine SimpleDateFormat pattern matching with the given date string. Returns null if
	 * format is unknown. You can simply extend DateUtil with more formats if needed.
	 *
	 * @param dateString The date string to determine the SimpleDateFormat pattern for.
	 * @return The matching SimpleDateFormat pattern, or null if format is unknown.
	 */
	public static String determineDateFormat(String dateString) {
		for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
			if (dateString.toLowerCase().matches(regexp)) {
				return DATE_FORMAT_REGEXPS.get(regexp);
			}
		}
		return ISO_8601_24H_FULL_FORMAT; // Unknown format.
	}
	/**
	 * All Dates are normalized to UTC, it is up the client code to convert to the appropriate TimeZone.
	 */
	public static final TimeZone UTC=TimeZone.getTimeZone("UTC");

	/**
	 * @see <a href="http://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">Combined Date and Time Representations</a>
	 */
	public static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	public static Date parseDateString(String dateStr) {
		return parseDateString(dateStr, null);
	}

	public static Date parseDateString(String dateStr, Date defaultDate) {
		try {
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat(determineDateFormat(dateStr));
//			FastDateFormat sdf = FastDateFormat.getInstance(determineDateFormat(dateStr));
			return simpleDateFormat.parse(dateStr);
		} catch (Exception ex) {
			return defaultDate;
		}
	}
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int[] getMonthDay(Date date) {
		int[] monthDay = new int[] { 0, 0 };
		if (date == null) {
			return monthDay;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		monthDay[0] = cal.get(Calendar.MONTH) + 1;
		monthDay[1] = cal.get(Calendar.DAY_OF_MONTH);
		return monthDay;
	}

	//获取上一个月第一天
	public static String getPreMonthFirstDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return sdf.format(cal.getTime());
	}

	//获取上一个月最后一天
	public static String getPreMonthLastDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 0);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return sdf.format(cal.getTime());
	}


	/**
	 * 将sourDateStr的年月日和Date中的时分秒进行拼接为一个格式为'yyyy-MM-dd HH:mm:ss'的形式
	 * 并将其转换为时间。
	 * 例子：
	 * @param sourDateStr 2016-03-03
	 * @param destDate
	 * @return
	 */
	public static Date joinDate(String sourDateStr, Date destDate) {
		String hourStr = hourFormat.format(destDate);
		StringBuffer buffer = new StringBuffer(sourDateStr);
		buffer.append(" ");
		buffer.append(hourStr);
		try {
			destDate = defaultFormat.parse(buffer.toString());
			return destDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将UST(标准零时区)的时间转换为CST(UTC+8-即中国标准时间)的时间
	 * 举个例子：
	 * 格式化："yyyy-MM-dd'T'HH:mm:ss.sss'Z'"
	 * @param ustTimeStr "2017-05-22T16:00:00.000Z"
	 * @return  Tue May 23 00:00:00 CST 2017
	 */
	public static Date ust2LocalTime(String ustTimeStr) {
		try {
			Date cstTime = ustFormat.parse(ustTimeStr);
			return cstTime;
		} catch (ParseException e) {
			logger.error("ust时间转换cst时间失败" + e.getMessage());
			return null;
		}
	}

	/**
	 * 对前台传输的时间进行拼接
	 * 例子：query的params中有
	 * postTime 2015-03-12
	 * beginTime：yyyy-MM-dd'T'HH:mm:ss.sss'Z' 格式
	 * endTime : yyyy-MM-dd'T'HH:mm:ss.sss'Z' 格式
	 * @param query
	 * @return
	 */
	public static QueryParameters assembleTime(QueryParameters query){
		String selectedDay = (String) query.getParams().get("postTime");
		String beginTimeStr = (String) query.getParams().get("beginTime");
		String endTimeStr = (String) query.getParams().get("endTime");
		Date beginTime = null;
		Date endTime = null;
		if (org.apache.commons.lang.StringUtils.isNotBlank(beginTimeStr)){
			beginTime = DateUtil.ust2LocalTime(beginTimeStr);
			beginTime = DateUtil.joinDate(selectedDay,beginTime);
		}
		if (org.apache.commons.lang.StringUtils.isNotBlank(endTimeStr)){
			endTime = DateUtil.ust2LocalTime(endTimeStr);
			endTime = DateUtil.joinDate(selectedDay,endTime);
		}

		query.addParam("beginTime",beginTime);
		query.addParam("endTime",endTime);
		System.out.println(beginTime);
		return query;
	}

	public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	public static int compareCurrentDate(Date date){
        try {
          
            Date currentDate=new Date();
     
            if (date.getTime() >= currentDate.getTime()) {
                return 0;
            } else if (date.getTime() < currentDate.getTime()) {
                return 1;
            } 
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 1;
	}
	public static String convertDate(String date,boolean isDays){
		String [] dateArr = null;
		String dateNew = "";
		if(date!=null && !"".equals(date)){
			dateArr = date.split("-");
			if(isDays == true){
				dateNew = dateArr[0]+"年"+dateArr[1]+"月"+dateArr[2]+"日";
			}else{
				dateNew = dateArr[0]+"年"+dateArr[1]+"月";
			}
			
		}
		return dateNew;
	}
}

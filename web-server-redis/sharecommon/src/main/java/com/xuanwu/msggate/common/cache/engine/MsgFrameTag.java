package com.xuanwu.msggate.common.cache.engine;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.util.DateUtil;

/**
 * @Description 信息帧表标记类，用于分表
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2013-9-18
 * @Version 1.0.0
 */
public class MsgFrameTag {

	public static final int MAX_DAYS = 366;
	private int dateByteLen = 4;
	private int curIdx = 0;
	private byte[] bitTags;
	private Date scanBeginTime;
	private Date prevAccessTime;
	private MsgType msgType;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfMd = new SimpleDateFormat("MMdd");
	private byte[] prevBitTags;
	
	public MsgFrameTag(MsgType msgType, String frameTags, Date lastAccessTime) throws Exception {
		this.msgType = msgType;
		bitTags = Hex.decodeHex(frameTags.toCharArray());
		prevBitTags = Arrays.copyOf(bitTags, bitTags.length);
		byte[] dateBytes = new byte[dateByteLen];
		System.arraycopy(bitTags, 0, dateBytes, 0, dateByteLen);
		//first run
		if(dateBytes[0] == 0 && dateBytes[1] == 0 
				&& dateBytes[2] == 0 && dateBytes[3] == 0){
			this.prevAccessTime = lastAccessTime;
		} else {
			this.prevAccessTime = sdf.parse(Hex.encodeHexString(dateBytes));
		}
		updateLastAccessTime(lastAccessTime);
	}
	
	private void updateNotSkipDays(Date lastAccessTime){
		int scanDays = getDays(lastAccessTime, prevAccessTime);
		scanDays += 1;
		int idx = this.curIdx;
		setNotSkip(idx);
		for(int i = 1; i < scanDays; i++){
			idx = getPrevIdx(idx);
			setNotSkip(idx);
		}
	}
	
	public void updateLastAccessTime(Date lastAccessTime) throws Exception {
		prevBitTags = Arrays.copyOf(bitTags, bitTags.length);//keep as previous bit tags
		byte[] dateBytes = Hex.decodeHex(sdf.format(lastAccessTime).toCharArray());
		System.arraycopy(dateBytes, 0, bitTags, 0, dateByteLen);
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastAccessTime);
		cal.set(Calendar.YEAR, 2012);
		this.scanBeginTime = cal.getTime();
		cal.set(2012, 0, 1);
		Date firstDay = cal.getTime();
		this.curIdx = getDays(scanBeginTime, firstDay);
		updateNotSkipDays(lastAccessTime);
		this.prevAccessTime = lastAccessTime;
	}
	
	public boolean isSkip(int idx){
		int tagIdx = idx / 8 + dateByteLen;
		int bitIdx = idx % 8;
		return (bitTags[tagIdx] & (0x80 >> bitIdx)) == 0;
	}
	
	public void setSkip(int idx){
		if(idx == curIdx)
			return;
		int tagIdx = idx / 8 + dateByteLen;
		int bitIdx = idx % 8;
		bitTags[tagIdx] = (byte)(bitTags[tagIdx] & ~(0x80 >> bitIdx));
	}
	
	public void setNotSkip(int idx){
		int tagIdx = idx / 8 + dateByteLen;
		int bitIdx = idx % 8;
		bitTags[tagIdx] = (byte)(bitTags[tagIdx] | (0x80 >> bitIdx));
	}
	
	public int getPrevIdx(int curIdx){
		int idx = curIdx - 1;
		if(idx < 0){
			idx = MAX_DAYS + idx;
		}
		return idx;
	}
	
	private int getDays(Date from, Date to){
		Calendar cal = Calendar.getInstance();
		cal.setTime(from);
		cal.set(Calendar.YEAR, 2012);
		Date _from = cal.getTime();
		cal.setTime(to);
		cal.set(Calendar.YEAR, 2012);
		Date _to = cal.getTime();
		return (int)(DateUtil.fixTime(_from.getTime()) / DateUtil.MILLIS_PER_DAY 
				- DateUtil.fixTime(_to.getTime()) / DateUtil.MILLIS_PER_DAY);
	}
	
	public int getCurIdx() {
		return curIdx;
	}
	
	public Date getScanBeginDate() {
		return scanBeginTime;
	}
	
	public boolean isChanged() {
		return !Arrays.equals(prevBitTags, bitTags);
	}
	
	public String forLog(){
		StringBuilder sb = new StringBuilder();
		sb.append(msgType).append(": ");
		Calendar cal = Calendar.getInstance();
		cal.set(2012, 0, 1);
		for(int i = 0; i < MAX_DAYS; i++){
			if(isSkip(i)){
				cal.add(Calendar.DAY_OF_MONTH, 1);
				continue;
			}
			sb.append(sdfMd.format(cal.getTime())).append(",");
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return sb.toString();
	}
	
	public String serialize(){
		return Hex.encodeHexString(bitTags);
	}
	
}

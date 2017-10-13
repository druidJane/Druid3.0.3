package com.xuanwu.msggate.common.util;

import com.xuanwu.msggate.common.encode.EncodeType;

/** 
* @Description: 系统编码，如果不是农信银的数字短信，短信的编码全部改为ucs2格式（Unicode的一种编码）
* @Author <a href="qiaotaosheng@wxchina.com">TaoSheng.Qiao</a>    
* @Date 2016年3月7日 上午11:49:56
*/

public class ChangeMsgFmt {
	
	public static int changeMsgFmt(int msgItem) {
		int msgFmtTemp = EncodeType.UCS2.getIndex();
		if(msgItem == EncodeType.USIM.getIndex()){
			msgFmtTemp = EncodeType.USIM.getIndex();
		}
		return msgFmtTemp;
	}

}

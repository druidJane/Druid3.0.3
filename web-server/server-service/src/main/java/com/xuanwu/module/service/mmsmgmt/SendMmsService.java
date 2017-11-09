package com.xuanwu.module.service.mmsmgmt;

import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.MmsContent;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;

import java.util.List;

/**
 * @Description
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-10-31
 * @Version 1.0.0
 */
public interface SendMmsService {

	public MsgFrame buildMassFrame(List<Contact> contacts, List<String[]> fileNames, MmsContent mms, MsgPack pack) throws Exception;
	
	public MsgFrame buildMassFrame(List<Contact> contacts, List<String[]> fileNames, MmsContent mms, MsgPack pack, String customUploadPath) throws Exception;

	public MsgFrame buildGroupFrame(List<Contact> contacts, List<String[]> fileNames, MmsContent mms, MsgPack pack) throws Exception;

	public MsgFrame buildGroupFrame(List<Contact> contacts, List<String[]> fileNames, MmsContent mms, MsgPack pack, String customUploadPath) throws Exception;

	public void setContactKeys(String[] keys);

}

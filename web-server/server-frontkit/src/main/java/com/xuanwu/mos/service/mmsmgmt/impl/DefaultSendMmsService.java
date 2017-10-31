package com.xuanwu.mos.service.mmsmgmt.impl;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.MmsContent;
import com.xuanwu.mos.domain.entity.SubContent;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.mos.file.handler.FileHandler;
import com.xuanwu.mos.file.handler.FileHandlerFactory;
import com.xuanwu.mos.file.handler.RowHandler;
import com.xuanwu.mos.rest.service.mmsmgmt.SendMmsService;
import com.xuanwu.mos.utils.DynVarUtil;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.msggate.common.sbi.entity.MediaItem;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.GroupMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.MassMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.PMediaItem;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgContent;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgSingle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultSendMmsService implements SendMmsService {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultSendMmsService.class);

	private Config config;

	private Map<String, Integer> contactMap;
	
	private static final String DIR_MASTHEAD = "/files/upload/masthead/";

	@Override
	public MsgFrame buildMassFrame(List<Contact> contacts, List<String[]> fileNames, MmsContent mms, MsgPack pack)
			throws Exception {
		return this.buildMassFrame(contacts, fileNames, mms, pack,null);
	}
	
	@Override
	public MsgFrame buildMassFrame(List<Contact> contacts,
                                   List<String[]> fileNames, MmsContent mms, MsgPack pack,
                                   String customUploadPath) throws Exception {
		MassMsgFrame frame = new MassMsgFrame();
		final List<MsgSingle> msgs = frame.getAllMsgSingle();
		PMsgContent msgContent = new PMsgContent();
		msgContent.setContent("");
		List<MediaItem> medias = new ArrayList<>();
		MediaItem media = new PMediaItem(0, "mms.smil", mms.getSmil().getBytes(WebConstants.DEFAULT_CHARSET));
		medias.add(media);
		media = new PMediaItem(0, "subject.txt", mms.getSubject().getBytes(WebConstants.DEFAULT_CHARSET));
		medias.add(media);
		File mediaFile = null;
		//region 将每一帧中的音频，视频，图片设置为附件，并添加到该pack包中
		for (MmsContent.Frame mf : mms.getFrames()) {
			if (mf.getText().getSize() > 0) {
				media = new PMediaItem(0, mf.getText().getName(), mf.getText()
						.getContent().getBytes(WebConstants.DEFAULT_CHARSET));
				medias.add(media);
			}
			if (mf.getImg().getSize() > 0) {
				if(StringUtils.isBlank(customUploadPath)){
					mediaFile = FileUtil.getImportedFile(BizDataType.Tmp, mf
							.getImg().getName(), Config.getContextPath());
				}else{
					mediaFile = FileUtil.getImportedCustomFile(mf.getImg().getName(), 
							Config.getContextPath(),customUploadPath);
				}
				media = new PMediaItem(0, mediaFile.getName(),
						FileUtil.readFile(mediaFile));
				medias.add(media);
			}
			if (mf.getAudio().getSize() > 0) {
				if(StringUtils.isBlank(customUploadPath)){
					mediaFile = FileUtil.getImportedFile(BizDataType.Tmp, mf
							.getAudio().getName(), Config.getContextPath());
				}else{
					mediaFile = FileUtil.getImportedCustomFile(mf.getAudio().getName(), 
							Config.getContextPath(),customUploadPath);
				}
				
				media = new PMediaItem(0, mediaFile.getName(),
						FileUtil.readFile(mediaFile));
				medias.add(media);
			}
			if (mf.getVideo().getSize() >0) {
				if(StringUtils.isBlank(customUploadPath)){
					mediaFile = FileUtil.getImportedFile(BizDataType.Tmp, mf
							.getVideo().getName(), Config.getContextPath());
				}else{
					mediaFile = FileUtil.getImportedCustomFile(mf.getVideo().getName(),
							Config.getContextPath(),customUploadPath);
				}
				media = new PMediaItem(0,mediaFile.getName(),
						FileUtil.readFile(mediaFile));
				medias.add(media);
			}
		}
		pack.setMmsAttachments(medias);
		//endregion
		frame.setContent(msgContent);

		for (Contact contact : contacts) {
			msgs.add(new PMsgSingle(MsgType.MMS, contact.getPhone(), null,
					null, null, contact.isVip(), 0));
		}
		final String phoneReg = config.getPhonePattern();
		for (String[] arr : fileNames) {
			FileType fileType = FileUtil.getFileType(arr[0]);
			File file = FileUtil.getImportedFile(BizDataType.SendMms, arr[0],
					Config.getContextPath());
			FileHandler fileHandler = FileHandlerFactory
					.getFileHandler(fileType);
			fileHandler.readAll(file.getAbsolutePath(), arr[1],
					new RowHandler() {
						@Override
						public boolean handleHead(String[] cells) {
							// do nothing
							return true;
						}

						@Override
						public boolean handleRow(String[] cells) {
							// 第一个元素必为手机号码
							if (cells == null || cells.length == 0)
								return true;
							if (cells[0].matches(phoneReg)) {
								msgs.add(new PMsgSingle(MsgType.MMS, cells[0],
										null, null, null, false, 0));
							}
							return true;
						}
					});
		}
		frame.setScheduleTime(pack.getScheduleTime());
		frame.setBizType(pack.getBizType());
		frame.setMsgType(pack.getMsgType());
		frame.setReportState(true);
		return frame;
	}
	
	@Override
	public MsgFrame buildGroupFrame(List<Contact> contacts,
                                    List<String[]> fileNames, final MmsContent mms, MsgPack pack)
			throws Exception {
		return buildGroupFrame(contacts, fileNames, mms, pack,null);
	}
	
	@Override
	public MsgFrame buildGroupFrame(List<Contact> contacts,
                                    List<String[]> fileNames, final MmsContent mms, MsgPack pack,
                                    String customUploadPath) throws Exception {
		GroupMsgFrame frame = new GroupMsgFrame();
		final List<MsgSingle> msgs = frame.getAllMsgSingle();
		ArrayList<MediaItem> medias = new ArrayList<MediaItem>();
		MediaItem media = new PMediaItem(0, "mms.smil", mms.getSmil().getBytes(
				WebConstants.DEFAULT_CHARSET));
		medias.add(media);
		media = new PMediaItem(0, "subject.txt", mms.getSubject().getBytes(
				WebConstants.DEFAULT_CHARSET));
		medias.add(media);
		File mediaFile = null;
		for (MmsContent.Frame mf : mms.getFrames()) {
			if (mf.getImg().getSize() > 0) {
				if(StringUtils.isBlank(customUploadPath)){
					mediaFile = FileUtil.getImportedFile(BizDataType.Tmp, mf
							.getImg().getName(), Config.getContextPath());
				}else{
					mediaFile = FileUtil.getImportedCustomFile(mf.getImg().getName(), 
							Config.getContextPath(),customUploadPath);
				}
				media = new PMediaItem(0, mediaFile.getName(),
						FileUtil.readFile(mediaFile));
				medias.add(media);
			}
			if (mf.getAudio().getSize() > 0) {
				if(StringUtils.isBlank(customUploadPath)){
					mediaFile = FileUtil.getImportedFile(BizDataType.Tmp, mf
							.getAudio().getName(), Config.getContextPath());
				}else{
					mediaFile = FileUtil.getImportedCustomFile(mf.getImg().getName(), 
							Config.getContextPath(),customUploadPath);
				}
				media = new PMediaItem(0, mediaFile.getName(),
						FileUtil.readFile(mediaFile));
				medias.add(media);
			}
			if (mf.getVideo().getSize() > 0) {
				if(StringUtils.isBlank(customUploadPath)){
					mediaFile = FileUtil.getImportedFile(BizDataType.Tmp, mf
							.getVideo().getName(), Config.getContextPath());
				}else{
					mediaFile = FileUtil.getImportedCustomFile(mf.getImg().getName(),
							Config.getContextPath(),customUploadPath);
				}
				media = new PMediaItem(0, mediaFile.getName(),
						FileUtil.readFile(mediaFile));
				medias.add(media);
			}
		}
		pack.setMmsAttachments(medias);
		String varValue = null;
		ArrayList<MediaItem> subMedias = null;
		PMsgContent msgContent = null;
		final StringBuilder sb = new StringBuilder(512);
		for (Contact contact : contacts) {
			msgContent = new PMsgContent();
			msgContent.setContent("");
			subMedias = new ArrayList<MediaItem>();
			msgContent.setMediaItems(subMedias);
			for (MmsContent.Frame mf : mms.getFrames()) {
				if (ListUtil.isBlank(mf.getText().getSubs()))
					continue;
				for (SubContent sub : mf.getText().getSubs()) {
					if (sub.getType() == SubContent.SubType.VAR) {
						varValue = DynVarUtil.getVarValue(this.contactMap,
								sub.getContent(), contact.getArray());
						if (varValue != null) {
							sb.append(varValue);
						}
					} else {
						sb.append(sub.getContent());
					}
				}
				subMedias.add(new PMediaItem(0, mf.getText().getName(), sb
						.toString().getBytes(WebConstants.DEFAULT_CHARSET)));
				sb.delete(0, sb.length());
			}
			msgs.add(new PMsgSingle(MsgType.MMS, contact.getPhone(),
					msgContent, null, null, contact.isVip(), 0));
		}
		final String phoneReg = config.getPhonePattern();
		for (String[] arr : fileNames) {
			FileType fileType = FileUtil.getFileType(arr[0]);
			File file = FileUtil.getImportedFile(BizDataType.SendMms, arr[0],
					Config.getContextPath());
			FileHandler fileHandler = FileHandlerFactory
					.getFileHandler(fileType);
			final Map<String, Integer> headMap = new HashMap<String, Integer>();
			fileHandler.readAll(file.getAbsolutePath(), arr[1],
					new RowHandler() {
						@Override
						public boolean handleHead(String[] cells) {
							for (int i = 0; i < cells.length; i++) {
								headMap.put(cells[i], i);
							}
							return true;
						}

						@Override
						public boolean handleRow(String[] cells) {
							// 第一个元素必为手机号码
							if (!cells[0].matches(phoneReg)) {
								return true;
							}
							String varValue = null;
							PMsgContent msgContent = new PMsgContent();
							ArrayList<MediaItem> subMedias = null;
							msgContent.setContent("");
							for (MmsContent.Frame mf : mms.getFrames()) {
								if (ListUtil.isBlank(mf.getText().getSubs()))
									continue;
								for (SubContent sub : mf.getText().getSubs()) {
									if (sub.getType() == SubContent.SubType.VAR) {
										varValue = DynVarUtil.getVarValue(
												headMap, sub.getContent(),
												cells);
										if (varValue != null) {
											sb.append(varValue);
										}
									} else {
										sb.append(sub.getContent());
									}
								}
								if (subMedias == null) {
									subMedias = new ArrayList<MediaItem>();
									msgContent.setMediaItems(subMedias);
								}
								try {
									subMedias.add(new PMediaItem(0, mf.getText().getName(), sb.toString().getBytes(WebConstants.DEFAULT_CHARSET)));
								} catch (UnsupportedEncodingException e) {
									logger.error("Unsupported encoding: "
											+ WebConstants.DEFAULT_CHARSET);
								}
								sb.delete(0, sb.length());
							}
							msgs.add(new PMsgSingle(MsgType.MMS, cells[0], msgContent, null, null, false, 0));
							return true;
						}
					});
		}
		frame.setScheduleTime(pack.getScheduleTime());
		frame.setBizType(pack.getBizType());
		frame.setMsgType(pack.getMsgType());
		frame.setReportState(true);
		return frame;
	}

	@Override
	public void setContactKeys(String[] keys) {
		HashMap<String, Integer> contactMap = new HashMap<String, Integer>();
		for (int i = 0; i < keys.length; i++) {
			contactMap.put(keys[i], i);
		}
		this.contactMap = contactMap;
	}
	@Autowired
	public void setConfig(Config config) {
		this.config = config;
	}

}

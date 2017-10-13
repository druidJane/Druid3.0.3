package com.xuanwu.msggate.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.xuanwu.msggate.common.sbi.entity.MediaItem;
import com.xuanwu.msggate.common.sbi.entity.MmsContent;
import com.xuanwu.msggate.common.sbi.entity.MmsPar;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

public class MmsParser {
	private static final Logger logger = LoggerFactory
			.getLogger(MmsParser.class);
	private static final String enc = "ISO-8859-1";
	public static MmsContent parseMmms(List<MediaItem> mediaItem)
			throws CoreException {
		if (ListUtil.isBlank(mediaItem)) {
			return null;
		}

		String smil = "";
		String subject = "";
		List<MmsPar> mmsPars = null;
		Map<String, MediaItem> mediaItemMap = new HashMap<String, MediaItem>();
		try {
			for (MediaItem item : mediaItem) {
				String filename = item.getMeta();
				String type = getMimeType(filename);
				ByteString attachment = ByteString.copyFrom(item.getData());
				if (type.equalsIgnoreCase("smil")) {
					smil = getStringWithoutBom(attachment.toStringUtf8());
				} else if (filename.startsWith("subject")) {
					subject = getStringWithoutBom(attachment.toStringUtf8());
				} else {
					if(type.equalsIgnoreCase("txt")){
						String content = getStringWithoutBom(attachment.toStringUtf8());
						ByteString byteStr = ByteString.copyFromUtf8(content);
						item.setData(byteStr.toByteArray());
					}
					mediaItemMap.put(filename, item);
				}
			}
			UnicodeInputStream uin = new UnicodeInputStream(new ByteArrayInputStream(smil.getBytes()), enc);
			String smilenc = uin.getEncoding();
			if(!smilenc.equalsIgnoreCase(enc)){
				BufferedReader in = new BufferedReader(new InputStreamReader(uin));
			    StringBuffer buffer = new StringBuffer();
			    String line = "";
			    while ((line = in.readLine()) != null){
			      buffer.append(line);
			    }
			    smil = buffer.toString();
			}
			mmsPars = parseMmsPar(smil, mediaItemMap);
		} catch (Exception e) {
			logger.error("MmsUtil getMmsContent failed cause by{}", e);
			throw new CoreException(
					"MmsUtil getMmsContent failed, exception is: "
							+ e.getMessage());
		}
		return new MmsContent(smil, subject, mmsPars);
	}

	@SuppressWarnings("unchecked")
	private static List<MmsPar> parseMmsPar(String smil,
			Map<String, MediaItem> mediaItemMap) throws CoreException {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		List<MmsPar> mmsPars = new ArrayList<MmsPar>();
		try {
			document = saxReader.read(new StringReader(smil));
			Element root = document.getRootElement();
			Element body = root.element("body");
			if (body == null || ListUtil.isBlank(body.elements())) {
				throw new RuntimeException("Illegal MMS frame.");
			}
			List<Element> pars = body.elements();

			for (int i = 0; i < pars.size(); i++) {
				Element par = pars.get(i);
				MmsPar mmsPar = new MmsPar();
				mmsPar.setOrderNo(i);
				mmsPar.setDur(Integer.parseInt(par.attributeValue("dur")
						.replace("ms", "")));
				List<Element> parAttachments = par.elements();
				if (ListUtil.isBlank(parAttachments)) {
					continue;
				}

				for (Element attachment : parAttachments) {
					String type = attachment.getName();
					String src = attachment.attributeValue("src");
					
					if (type.equalsIgnoreCase("img")) {
						mmsPar.setImg(mediaItemMap.get(src));
					} else if (type.equalsIgnoreCase("audio")) {
						mmsPar.setAudio(mediaItemMap.get(src));
					} else if (type.equalsIgnoreCase("text")) {
						mmsPar.setText(mediaItemMap.get(src));
					}
				}
				mmsPars.add(mmsPar);
			}
		} catch (Exception e) {
			logger.error("MmsParser parseMmsPar failed cause by{}", e);
			throw new CoreException(
					"MmsParser parseMmsPar failed, exception is: "
							+ e.getMessage());
		}
		return mmsPars;
	}

	public static String getMimeType(String fileName) {
		try {
			if (StringUtils.isBlank(fileName)) {
				throw new RuntimeException("Illegal file name.");
			}
			int pos = fileName.lastIndexOf(".");
			return fileName.substring(pos + 1).toLowerCase();
		} catch (Exception e) {
			String message = "MmsParser getMimeType() Exception is: "
					+ e.getMessage();
			logger.error(message);
			throw new RuntimeException(message);
		}
	}
	
	private static String getStringWithoutBom(String src){
	    try {
	    	UnicodeInputStream uin = new UnicodeInputStream(new ByteArrayInputStream(src.getBytes()), enc);
	    	String smilenc = uin.getEncoding();
	    	if(!smilenc.equalsIgnoreCase(enc)){
	    		BufferedReader in = new BufferedReader(new InputStreamReader(uin));
	    		StringBuffer buffer = new StringBuffer();
	    		String line = "";
				while ((line = in.readLine()) != null){
				  buffer.append(line);
				}
				src = buffer.toString();
	    	}
		} catch (IOException e) {
			String message = "MmsParser getStringWithoutBom() Exception is: "
				+ e.getMessage();
			logger.error(message);
			throw new RuntimeException(message);
		}
		return src;
	}
}

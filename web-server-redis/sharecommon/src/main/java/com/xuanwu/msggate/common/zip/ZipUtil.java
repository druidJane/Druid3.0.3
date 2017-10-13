/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

import com.google.protobuf.ByteString;

/**
 * Zip utils
 * 
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-7
 * @Version 1.0.0
 */
public class ZipUtil {
	/**
	 * Zip byte array
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static byte[] zipByteArray(byte[] source) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		GZIPOutputStream output = new GZIPOutputStream(stream);
		output.write(source);
		output.close();
		return stream.toByteArray();
	}

	/**
	 * Unzip byte array
	 * 
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static byte[] unzipByteArray(byte[] target) throws IOException {
		return IOUtils.toByteArray(new GZIPInputStream(
				new ByteArrayInputStream(target)));
	}
	
	/**
	 * Unzip byte array
	 * 
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static byte[] unzipByteArray(ByteString target) throws IOException {
		byte[] targets = target.toByteArray();
		return IOUtils.toByteArray(new GZIPInputStream(
				new ByteArrayInputStream(targets)));
	}

}

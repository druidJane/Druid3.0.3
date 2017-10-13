/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.impl;

import com.xuanwu.msggate.common.sbi.entity.MTResp;
import com.xuanwu.msggate.common.sbi.entity.MTResult;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgPack.PackState;
import com.xuanwu.msggate.common.sbi.entity.PlatformMode;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * @Description:
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-10-11
 * @Version 1.0.0
 */
public class MTClient {
	private static final Logger logger = LoggerFactory.getLogger(MTClient.class);

	private static final String CONNECT_TO_SERVER_FAIL = "连接不上服务器!";
	private static final String SYS_ERROR = "系统内部错误!";
	private int soTimeout;
	private int maxFrameLength;
	private String platform;
	private String version;

	/**
	 * 构造方法
	 * @param platform 平台名
	 * @param version 平台版本
	 * @param soTimeout Socket超时时间
	 * @param maxFrameLength 最大读取数据包的长度（字节）
	 */
	public MTClient(String platform, String version, int soTimeout, int maxFrameLength) {
		this.platform = platform;
		this.version = version;
		this.soTimeout = soTimeout;
		this.maxFrameLength = maxFrameLength;
	}
	
	public MTResp send(String ip, int port, String userName, String password, String phone, String msg, String params) {
		Socket client = connect(ip, port);
		if (client == null)
			return MTResp.build(MTResult.CONNECT_TO_SERVER_FAIL, CONNECT_TO_SERVER_FAIL);
		try {
			MTResp resp = login(userName, password, client);
			if (resp.getResult() == MTResult.SUCCESS) {
				byte[] recvBytes = transfer(client, MTSupport.tran2SendBytes(phone, msg, params, platform, version));
				return MTSupport.buildResp(recvBytes);
			}
			return resp;
		} catch (Exception e) {
			logger.error("Send msg pack by userName:{} failed,cause by:{}", userName, e);
		} finally {
			try {
				if (client != null)
					client.close();
			} catch (Exception e) {
				// Ignore it
			}
		}
		return MTResp.build(MTResult.INNER_ERROR, SYS_ERROR);
	}
	
	public MTResp send(String ip, int port, String userName, String password, MsgPack pack) {
		Socket client = connect(ip, port);
		if (client == null)
			return MTResp.build(MTResult.CONNECT_TO_SERVER_FAIL, CONNECT_TO_SERVER_FAIL);
		try {
			MTResp resp = login(userName, password, client);
			if (resp.getResult() == MTResult.SUCCESS) {
				byte[] recvBytes = transfer(client, MTSupport.tran2SendBytes(pack, platform, version));
				return MTSupport.buildResp(recvBytes);
			}
			return resp;
		} catch (Exception e) {
			logger.error("Send msg pack by userName:{} failed,cause by:{}", userName, e);
		} finally {
			try {
				if (client != null)
					client.close();
			} catch (Exception e) {
				// Ignore it
			}
		}
		return MTResp.build(MTResult.INNER_ERROR, SYS_ERROR);
	}

	public MTResp auditing(String ip, int port, String userName, String password, String packId,
			int state,int msgType,Date postTime,int platformId) throws CoreException {
		return sendAuditReq(ip, port, userName, password, packId, state,msgType,postTime,platformId);
	}
	
	public MTResp cancel(String ip, int port, String userName, String password, String packId,int msgType,Date postTime,int platformId) throws CoreException {
		return sendAuditReq(ip, port, userName, password, packId, PackState.CANCEL.getIndex(),msgType,postTime,platformId);
	}
	
	private MTResp sendAuditReq(String ip, int port, String userName, String password, String packId,
			int state,int msgType,Date postTime,int platformId){
		Socket client = connect(ip, port);
		if (client == null)
			return MTResp.build(MTResult.CONNECT_TO_SERVER_FAIL, CONNECT_TO_SERVER_FAIL);
		MTResp resp = MTResp.build(MTResult.INNER_ERROR);
		try {
			if(platformId == PlatformMode.MOS.getIndex()){// 后台审核不登录
				byte[] recvBytes = transfer(client, MTSupport.tran2AuditingBytes(packId,state,msgType,postTime,platformId));
				resp = MTSupport.buildResp(recvBytes);
			}else{
				resp = login(userName, password, client);
				if (resp.getResult() == MTResult.SUCCESS) {
					byte[] recvBytes = transfer(client, MTSupport.tran2AuditingBytes(packId,state,msgType,postTime,platformId));
					resp = MTSupport.buildResp(recvBytes);
				}
			}
		} catch (Exception e) {
			logger.error("Send audit request by userName:{} failed,cause by:{}", userName, e);
		} finally {
			try {
				if (client != null)
					client.close();
			} catch (Exception e) {
				// Ignore it
			}
		}
		return resp;
	}

	private Socket connect(String ip, int port) {
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			socket.setSoTimeout(soTimeout);
		} catch (UnknownHostException e) {
			logger.error("Connect to gateway UnknownHostException occur.");
		} catch (IOException e) {
			logger.error("Connect to gateway IOException occur, cause by: ", e);
		}
		return socket;
	}

	private MTResp login(String userName, String password, Socket client)
			throws Exception {
		byte[] recvBytes = transfer(client,
				MTSupport.tran2LoginBytes(userName, password, platform, version));
		return MTSupport.buildResp(recvBytes);
	}

	private byte[] transfer(Socket client, byte[] sendBytes) throws IOException {
		if (sendBytes == null) {
			throw new NullPointerException("Send bytes must be not null.");
		}
		try {
			InputStream in = client.getInputStream();
			OutputStream out = client.getOutputStream();
			writeInt(out, sendBytes.length);
			out.write(sendBytes);
			out.flush();

			// read bytes with header length
			int recvLength = readInt(in);
			if (recvLength < 0) {
				throw new IllegalArgumentException(
						"recvLength must be a positive integer: " + recvLength);
			}
			if (recvLength > maxFrameLength) {
				throw new IllegalArgumentException("recvLength (" + recvLength
						+ ") " + "must be equal to or less than "
						+ "maxFrameLength (" + maxFrameLength + ").");
			}

			byte[] recvBytes = new byte[recvLength];
			in.read(recvBytes);

			return recvBytes;
		} catch (IOException ioe) {
			throw ioe;
		}
	}

	private void writeInt(OutputStream out, int v) throws IOException {
		out.write((v >>> 24) & 0xFF);
		out.write((v >>> 16) & 0xFF);
		out.write((v >>> 8) & 0xFF);
		out.write((v >>> 0) & 0xFF);
	}

	private int readInt(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public void setLoginWaitTime(int loginWaitTime) {
		this.soTimeout = loginWaitTime;
	}

	public void setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
	}
}

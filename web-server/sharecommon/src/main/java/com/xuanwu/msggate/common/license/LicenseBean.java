package com.xuanwu.msggate.common.license;

import java.util.HashMap;
import java.util.Map;

public class LicenseBean {

	private String entName; //公司名
	
	private String mac;// 主机的ID号

	private long date; // 软件使用到期日期

	private String sign;
	
	private String publicKey;
	
	private String privateKey;
	
	private byte[] encodedData;
	
	private String version;
	
	/** extended attributes */
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	public String getEntName() {
		return entName;
	}
	
	public void setEntName(String entName) {
		this.entName = entName;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}
	
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public byte[] getEncodedData() {
		return encodedData;
	}

	public void setEncodedData(byte[] encodedData) {
		this.encodedData = encodedData;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public void addAttribute(String key, Object value) {
		attributes.put(key, value);
	}	
}

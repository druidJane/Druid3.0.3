package com.xuanwu.msggate.common.sbi.entity;

import java.util.Date;

public class EnterpriseAccessRecord {

	private int id;
	
	private int enterpriseID;
	
	private String method;
	
	private String version;
	
	private String extMsg;
	
	private Date lastAccessDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEnterpriseID() {
		return enterpriseID;
	}

	public void setEnterpriseID(int enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getExtMsg() {
		return extMsg;
	}

	public void setExtMsg(String extMsg) {
		this.extMsg = extMsg;
	}

	public Date getLastAccessDate() {
		return lastAccessDate;
	}
	
	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}
	
	public String getKey(){
		return getKey(this.enterpriseID, this.method, this.version, this.extMsg);
	}
	
	public static String getKey(int entID, String method, String version, String extMsg){
		StringBuilder sb = new StringBuilder();
		sb.append(entID);
		sb.append(method == null ? "" : method);
		sb.append(version == null ? "" : version);
		sb.append(extMsg == null ? "" : extMsg);
		return sb.toString();
	}

	
}

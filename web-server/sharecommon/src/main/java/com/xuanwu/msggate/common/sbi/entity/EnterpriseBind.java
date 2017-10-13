package com.xuanwu.msggate.common.sbi.entity;

public class EnterpriseBind {
	private int specNumID;
	private int entID;
	private String entIdentify = "";

	public int getSpecNumID() {
		return specNumID;
	}

	public void setSpecNumID(int specNumID) {
		this.specNumID = specNumID;
	}

	public int getEntID() {
		return entID;
	}

	public void setEntID(int entID) {
		this.entID = entID;
	}

	public String getEntIdentify() {
		return entIdentify;
	}

	public void setEntIdentify(String entIdentify) {
		this.entIdentify = (entIdentify == null ? "" : entIdentify);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + entID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnterpriseBind other = (EnterpriseBind) obj;
		if (entID != other.entID)
			return false;
		return true;
	}
}

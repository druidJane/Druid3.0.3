package com.xuanwu.msggate.common.sbi.entity;

import java.util.List;

/**
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-06-08
 * @Version 1.0.0
 */
public class DestBindResult {

	private DestBindType destBindType;

	private boolean isBypass;
	private Integer sourceID;

	private List<BindSpecNumResult> results;

	public boolean isBypass() {
		return isBypass;
	}

	public void setBypass(boolean isBypass) {
		this.isBypass = isBypass;
	}

	public Integer getSourceID() {
		return sourceID;
	}

	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}

	public List<BindSpecNumResult> getResults() {
		return results;
	}

	public void setResults(List<BindSpecNumResult> results) {
		this.results = results;
	}

	public DestBindType getDestBindType() {
		return destBindType;
	}

	public void setDestBindType(int destBindType) {
		this.destBindType = DestBindType.getType(destBindType);
	}

	public boolean isEmpty() {
		return (results == null || results.isEmpty()) ? true : false;
	}
}

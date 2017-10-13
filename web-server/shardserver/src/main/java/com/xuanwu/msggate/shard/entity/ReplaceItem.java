package com.xuanwu.msggate.shard.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class ReplaceItem {
	private String srcName;
	private String targetName;
	
	public ReplaceItem(String srcName, String targetName){
		this.srcName = srcName;
		this.targetName = targetName;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
}

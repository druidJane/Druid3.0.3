package com.xuanwu.msggate.common.sbi.entity;


/**
 * White redirect special service number
 * 
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-3
 * @Version 1.0.0
 */
public class WhiteRedirect {
	private int msgType;
	private Integer speSVSNumID;
	private Integer priRedirectID;
	private Integer minorRedirectID;

	private SpecSVSNumber specialNumber;
	private SpecSVSNumber priRedirect;
	private SpecSVSNumber minorRedirect;

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public Integer getPriRedirectID() {
		return priRedirectID;
	}

	public void setPriRedirectID(Integer priRedirectID) {
		this.priRedirectID = priRedirectID;
	}

	public Integer getMinorRedirectID() {
		return minorRedirectID;
	}

	public void setMinorRedirectID(Integer minorRedirectID) {
		this.minorRedirectID = minorRedirectID;
	}

	public SpecSVSNumber getPriRedirect() {
		return priRedirect;
	}

	public void setPriRedirect(SpecSVSNumber priRedirect) {
		this.priRedirect = priRedirect;
	}

	public SpecSVSNumber getMinorRedirect() {
		return minorRedirect;
	}

	public void setMinorRedirect(SpecSVSNumber minorRedirect) {
		this.minorRedirect = minorRedirect;
	}

	public Carrier getCarrier() {
		return specialNumber.getCarrier();
	}

	@Override
	public String toString() {
		return "WhiteRedirect [priRedirectID=" + priRedirectID
				+ ", minorRedirectID=" + minorRedirectID + "]";
	}

	public Integer getSpeSVSNumID() {
		return speSVSNumID;
	}

	public void setSpeSVSNumID(Integer speSVSNumID) {
		this.speSVSNumID = speSVSNumID;
	}

	public SpecSVSNumber getSpecialNumber() {
		return specialNumber;
	}

	public void setSpecialNumber(SpecSVSNumber specialNumber) {
		this.specialNumber = specialNumber;
	}
}
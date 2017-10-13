package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-06-08
 * @Version 1.0.0
 */
public abstract class AbstractDestBind {
	protected DestBindResult whiteDestResult;
	protected DestBindResult regionDestResult;
	protected List<DestBindResult> changeDestResult = new ArrayList<DestBindResult>();
	
	protected boolean changeRedirect;
	protected boolean whiteRedirect;
	protected boolean regionRedirect;

	public abstract void initMap();
	
	public void setDestResult(DestBindResult dest) {
		switch (dest.getDestBindType()) {
		case WHITE_REDIRECT:
			whiteDestResult = dest;
			break;
		case REGION_PRIOR:
			regionDestResult = dest;
			break;
		case CHANNEL_CHANGE:
			changeDestResult.add(dest);
		}
	}
	
	protected boolean isBypass() {
		return false;
	} 

	protected boolean hasChangeRedirect() {
		return changeRedirect;
	}

	protected void setChangeRedirect(boolean changeRedirect) {
		this.changeRedirect = changeRedirect;
	}

	protected boolean hasWhiteRedirect() {
		return whiteRedirect;
	}

	protected void setWhiteRedirect(boolean whiteRedirect) {
		this.whiteRedirect = whiteRedirect;
	}

	protected boolean hasRegionRedirect() {
		return regionRedirect;
	}

	protected void setRegionRedirect(boolean regionRedirect) {
		this.regionRedirect = regionRedirect;
	}

	protected List<BindSpecNumResult> duplitBasicResultByDestBind(List<DestBindResult> orgResults){
		List<BindSpecNumResult> duplitResults = new ArrayList<BindSpecNumResult>();
		for(DestBindResult destBindResult: orgResults){
			for(BindSpecNumResult result : destBindResult.getResults()){
				result.setBypass(destBindResult.isBypass());
				duplitResults.addAll(result.duplitWithCarrierAndMsgType());
			}
		}
		return duplitResults;
	}
	
	protected List<BindSpecNumResult> duplitBasicResult(List<BindSpecNumResult> orgResults){
		List<BindSpecNumResult> duplitResults = new ArrayList<BindSpecNumResult>(orgResults.size());
		for(BindSpecNumResult result : orgResults){
			duplitResults.addAll(result.duplitWithCarrierAndMsgType());
		}
		return duplitResults;
	}
	
	protected Integer tran2Key(MsgType msgType, Carrier carrier) {
		return Integer.valueOf(((msgType.getIndex() & 0x7) << 3)
				| (carrier.getIndex() & 0x7));
	}
	
	protected Integer tran2Key(Integer targetID, MsgType msgType, Carrier carrier) {
		return Integer.valueOf((targetID << 6) | ((msgType.getIndex() & 0x7) << 3)
				| (carrier.getIndex() & 0x7));
	}
}

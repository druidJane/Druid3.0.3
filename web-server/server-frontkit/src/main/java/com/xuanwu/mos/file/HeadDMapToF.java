package com.xuanwu.mos.file;

import java.io.Serializable;

/**
 * Created by linzeqiang on 2016/11/4. Head Data Map to File
 */
public class HeadDMapToF implements Serializable {

	private static final long serialVersionUID = 1L;
	private HeadInfo dataHeadInfo;
	private HeadInfo fileHeadInfo;

	public HeadInfo getDataHeadInfo() {
		return dataHeadInfo;
	}

	public void setDataHeadInfo(HeadInfo dataHeadInfo) {
		this.dataHeadInfo = dataHeadInfo;
	}

	public HeadInfo getFileHeadInfo() {
		return fileHeadInfo;
	}

	public void setFileHeadInfo(HeadInfo fileHeadInfo) {
		this.fileHeadInfo = fileHeadInfo;
	}

}

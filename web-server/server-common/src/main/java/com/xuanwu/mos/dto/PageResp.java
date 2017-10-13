/*
 * Copyright (c) 2015 by XuanWu Wireless Technology Co., Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.dto;

import java.util.Collections;

/**
 * @Description 分页处理结果
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2015年4月27日
 * @Version 1.0.0
 */
public class PageResp extends JsonResp {

	private int total;
	private Boolean hasMore=Boolean.FALSE;

	public PageResp(int total, Object data) {
		super(JsonResp.SUCCESS_STATUS, data);
		this.total = total;
	}

	public static PageResp emptyResult() {
		return new PageResp(0, Collections.EMPTY_LIST);
	}

	public static PageResp success(int total, Object data) {
		return new PageResp(total, data);
	}

	public static PageResp success(int total,Boolean hasMore, Object data) {
		PageResp pageResp= new PageResp(total, data);
		pageResp.setHasMore(hasMore);
		return pageResp;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}
}

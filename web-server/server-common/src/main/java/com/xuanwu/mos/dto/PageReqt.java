/*
 * Copyright (c) 2015 by XuanWu Wireless Technology Co., Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.dto;

import java.util.Map;

import javax.validation.constraints.Min;

/**
 * @Description 分页请求
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2015年4月27日
 * @Version 1.0.0
 */
public class PageReqt {

	/** 页码：1 ~ n */
	@Min(1)
	private Integer page;

	/** 每页大小 */
	@Min(1)
	private Integer count;

	/** 查询参数 */
	private Map<String, Object> params;

	/** 排序参数 */
	private Map<String, String> sorts;

	public Integer getPage() {
		return page;
	}

	//mysql查询语法中的offset
	public Integer getOffset(){
		if(null==count||null==page){
			return 0;
		}else {
			return count * (page - 1);
		}
	}

	//mysql查询语法中 Limit
	public Integer getLimit(){
		return count;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, String> getSorts() {
		return sorts;
	}

	public void setSorts(Map<String, String> sorts) {
		this.sorts = sorts;
	}
}

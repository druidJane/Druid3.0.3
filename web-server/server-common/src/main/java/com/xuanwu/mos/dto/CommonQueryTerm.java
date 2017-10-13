package com.xuanwu.mos.dto;

import java.util.Map;

/**
 * 使用Type 方便查询，如果用map的话，必须的看前端 或者 xml才知道查写什么东西
 * @author <a href="kangqinghua@wxchina.com">Qinghua Kang</a>
 * @version 1.0.0
 * @date 2016/11/2 18:18:01
 */
public class CommonQueryTerm {
    private Integer page=1;
    private Integer count=10;
    private PageInfo pageInfo;
    /** 排序参数 */
    private Map<String, String> sorts;

    public Integer getPage() {
        return page;
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

    public Map<String, String> getSorts() {
        return sorts;
    }

    public void setSorts(Map<String, String> sorts) {
        this.sorts = sorts;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}

package com.xuanwu.mos.config;

import java.util.Date;
import java.util.List;

/**
 * @Description 分表支持辅助接口
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2013-9-18
 * @Version 1.0.0
 */
public interface ShardingSupport {

	/**
	 * 根据开始时间与结束时间，按日切分为多个时间范围
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Date[]> splitByDay(Date beginTime, Date endTime);
}

package com.xuanwu.mos.file.exporter;

import com.xuanwu.mos.config.Config;

/**
 * Created by 林泽强 on 2016/8/25. 文件导出器
 */
public abstract class BaseExporter {

	protected Config config;

	public abstract void setConfig(Config config);

}

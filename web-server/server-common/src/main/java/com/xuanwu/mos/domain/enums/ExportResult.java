package com.xuanwu.mos.domain.enums;

/**
 * Created by 林泽强 on 2016/9/9.
 */
public enum ExportResult {
	/** 成功 */
	SUCCESS,
	/** 数据为空 */
	EMPTY_DATA,
	/** 失败：文件不存在 */
	FILE_NOT_FOUND,
	/** 失败：读写操作失败 */
	READ_WRITE_FAIL,
	/** 失败：未知异常 */
	UNKNOWN_EXCEPTION;
}

package com.xuanwu.mos.domain.enums;

/**
 * Created by 林泽强 on 2016-8-28. UploadResult状态编码
 */
public enum StatusCode implements HasIndexValue {
	/**
	 * 成功
	 */
	Success(0),
	/**
	 * 失败:单个文件大小超过限制
	 */
	FileSizeExceeded(-1),
	/**
	 * 失败:请求表单类型不正确
	 */
	InvalidContentType(-2),
	/**
	 * 失败:文件类型不正确
	 */
	InvalidFileType(-3),
	/**
	 * 失败：文件内容为空
	 */
	NoContent(-4),
	/**
	 * 失败:请使用正确的分隔符
	 */
	InvalidDelimiter(-5),
	/**
	 * 文件头或文件内容为空
	 */
	NoExistFileHead(-6),
	/**
	 * 失败:其它错误
	 */
	Other(-9);

	private int index;

	private StatusCode(int index) {
		this.index = index;
	}

	public String getStateDesc() {
		switch (index) {
		case 0:
			return "成功。";
		case -1:
			return "文件大小超过限制！";
		case -2:
			return "请求表单类型不正确！";
		case -3:
			return "文件类型不正确！";
		case -4:
			return "文件内容为空！";
		case -5:
			return "请使用正确的分隔符！";
		case -6:
			return "文件头或文件内容为空";
		default:
			return "其它错误！";
		}
	}

	public static StatusCode getStatus(int index) {
		for (StatusCode ret : StatusCode.values()) {
			if (ret.getIndex() == index)
				return ret;
		}
		throw new BusinessException("Invalid status value: " + index);
	}

	@Override
	public int getIndex() {
		return index;
	}
}

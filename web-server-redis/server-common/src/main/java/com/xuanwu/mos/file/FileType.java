package com.xuanwu.mos.file;

/**
 * Created by 林泽强 on 2016/8/26. 文件类型
 */
public enum FileType {

	Excel(".xls"), ExcelX(".xlsx"), Text(".txt"), Csv(".csv"), Zip(".zip"), Bwf(".bwf"), Media(".*");

	private final String type;

	private FileType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static FileType getType(String type) {
		type = type.toLowerCase();
		if (type.endsWith(".txt"))
			return Text;
		if (type.endsWith(".xls"))
			return Excel;
		if (type.endsWith(".xlsx"))
			return ExcelX;
		if (type.endsWith(".csv"))
			return Csv;
		if (type.endsWith(".zip"))
			return Zip;
		if (type.endsWith(".bwf"))
			return Bwf;
		return Media;
	}
}

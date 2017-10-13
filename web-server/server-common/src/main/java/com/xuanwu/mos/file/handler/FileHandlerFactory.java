package com.xuanwu.mos.file.handler;

import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.file.FileType;

/**
 * Created by 林泽强 on 2016/8/22. 文件处理器工厂
 */
public class FileHandlerFactory {
	
	public static FileHandler getFileHandler(FileType fileType) {
		switch (fileType) {
		case Text:
		case Csv:
			return new TextHandler();
		case Excel:
		case ExcelX:
			return new ExcelHandler();
        case Media:
            return new MediaHandler();
		default:
			throw new BusinessException("Invalid file type:" + fileType);
		}
	}
	
}

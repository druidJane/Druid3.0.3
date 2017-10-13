package com.xuanwu.mos.file.handler;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanwu.mos.file.FileUtil;

/**
 * Created by 林泽强 on 2016/8/22. 文件处理抽象类，实现公共方法
 */
public abstract class AbstractFileHandler implements FileHandler {

	private static final Logger logger = LoggerFactory.getLogger(AbstractFileHandler.class);

	@Override
	public boolean upload(FileItem fileItem, File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(fileItem.get());
			return true;
		} catch (Exception e) {
			logger.error("Upload file faild, cause by:{}", e.getMessage());
			return false;
		} finally {
			FileUtil.closeOutputStream(fos);
		}
	}
}

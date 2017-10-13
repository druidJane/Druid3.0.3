package com.xuanwu.mos.file.handler;

import com.xuanwu.mos.file.FileHead;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class MediaHandler extends AbstractFileHandler {

	@Override
	public FileHead readHead(String filePath, String delimiter) {
		return null;
	}

	@Override
	public void readContent(File file, String delimiter, RowHandler rowHandler) {
		
	}

	@Override
	public void readAll(String filePath, String delimiter, RowHandler rowHandler) {
	}

	@Override
	public void writeFile(File file, String delimiter, boolean isNew, List<String[]> cellsList) {
		
	}
	
	@Override
	public void writeFile(File file, String delimiter, boolean isNew,
			List<String[]> cellsList, Charset charset) {
		
	}

	@Override
	public int readLineCount(File file) {
		return 0;
	}

}

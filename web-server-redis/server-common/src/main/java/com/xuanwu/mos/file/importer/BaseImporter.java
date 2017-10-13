package com.xuanwu.mos.file.importer;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.file.FileHead;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.mos.file.exporter.ExportInfo;
import com.xuanwu.mos.file.handler.FileHandler;
import com.xuanwu.mos.file.handler.FileHandlerFactory;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.utils.Delimiters;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * Created by 林泽强 on 2016/8/25. 文件导入器
 */
public abstract class BaseImporter {

	private static final Logger logger = LoggerFactory.getLogger(BaseImporter.class);

	protected Config config;

	public UploadResult upload(BizDataType dataType, HttpServletRequest request) {
		// 判断是否文件表单
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			return new UploadResult(StatusCode.InvalidContentType);
		}
		try {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Set overall request size constraint
			upload.setSizeMax(config.getMaxUploadFileSize());
			upload.setHeaderEncoding("utf-8");
			String delimiter = Delimiters.COMMA; // default
			FileItem fileItem = null;
			String fileTypeStr = "";
			// Parse the request
			List<?> items = upload.parseRequest(request);
			if (items == null) {
				return new UploadResult(StatusCode.Other);
			}
			for (Object obj : items) {
				FileItem item = (FileItem) obj;
				if (item.getFieldName().equals("delimiter")) {
					delimiter = item.getString();
				} else if (item.getFieldName().equals("fileType")) {
					fileTypeStr = item.getString();
				} else {
					fileTypeStr = item.getString();
					fileItem = item;
				}
			}
			// fileTypeStr必须存在
			// check file type
			FileType fileType = FileUtil.getFileType(fileItem.getName());
//			if (!checkFileType(fileTypeStr, fileType)) {
//				return new UploadResult(StatusCode.InvalidFileType);
//			}
			// upload file
			FileHandler fileHandler = FileHandlerFactory.getFileHandler(fileType);
			File file = FileUtil.createImportFile(dataType, fileType, config.getContextPath());
			fileHandler.upload(fileItem, file);

			// read file head
			FileHead fileHead = fileHandler.readHead(file.getAbsolutePath(), delimiter);
			if (fileHead == null) {
				return new UploadResult(StatusCode.NoExistFileHead);
			}
			if (!fileHead.isDelimterSucc()) {
				return new UploadResult(StatusCode.InvalidDelimiter);
			}

			UploadResult result = new UploadResult(StatusCode.Success, file.getName(), fileHead);
			result.setFileSize(request.getContentLengthLong());
			result.setNewFileName(file.getName());
			result.setDelimiter(delimiter);
			result.setIs(fileItem.getInputStream());
			String originFileName = fileItem.getName();
			if (StringUtils.isNotBlank(originFileName)) {
				originFileName = originFileName.replaceAll("\\\\", "/");
				result.setOriginFileName(new File(originFileName).getName());
			}
			return result;
		} catch (Exception e) {
			logger.error("Upload file failed,cause by:{}", e);
			if (e instanceof FileUploadBase.SizeLimitExceededException) {
				return new UploadResult(StatusCode.FileSizeExceeded);
			}
		}

		return new UploadResult(StatusCode.Other);
	}

	public boolean checkFileType(String fileTypeStr, FileType fileType) {
		switch (fileTypeStr) {
		case "excel":
			if (fileType == FileType.Excel || fileType == FileType.ExcelX) {
				return true;
			}
			break;
		case "txt":
			if (fileType == FileType.Text) {
				return true;
			}
			break;
		case "csv":
			if (fileType == FileType.Csv) {
				return true;
			}
			break;
		}
		return false;
	}

	public abstract void batchImport(FileTask task, List<String[]> rowList, FileHandler fileHandler,
			ExportInfo resultInfo) throws Exception;

	public abstract void setConfig(Config config);

}

package com.xuanwu.mos.file;

import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.service.FtpService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description File Uploader
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2013-11-06
 * @Version 1.0.0
 */
@Component
public class FileHelper {

	private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

	@Autowired
	private FileUploadConfig config;

	@Autowired
	private FtpService ftpService;

	
	/**
	 * 上传文件
	 * 
	 * @param request
	 *            请求对象
	 * @param types
	 *            允许的文件类型
	 * @param count
	 *            允许上传个数
	 * @param enterpriseId
	 *            企业ID
	 * @return
	 */
	public FileUploadInfo uploadFiles(List<MultipartFile> list, String path, Integer count) {

		if (list == null) {
			return new FileUploadInfo(FileUploadInfo.UploadResult.EMPTY_REQUEST, null);
		}
		if (list.isEmpty()) {
			return new FileUploadInfo(FileUploadInfo.UploadResult.EMPTY_FILES, null);
		}
		if (count != null && list.size() > count) {
			return new FileUploadInfo(FileUploadInfo.UploadResult.TOO_MANY_FILES, null);
		}

		List<FileInfo> files = new ArrayList<FileInfo>();
		for (MultipartFile file : list) {
			try {
				FileInfo info = this.uploadFileToFTP(file, path);
				files.add(info);
			} catch (Exception e) {
				logger.error("An error occurred by uploadFileToFTP", e);
				return new FileUploadInfo(FileUploadInfo.UploadResult.ERROR, null);
			}
		}

		if (files.isEmpty()) {
			return new FileUploadInfo(FileUploadInfo.UploadResult.EMPTY_FILES, null);
		}
		return new FileUploadInfo(FileUploadInfo.UploadResult.SUCCESS, files);
	}

	/**
	 * 从请求体中读取文件数据
	 * 
	 * @param request
	 *            请求对象
	 * @param types
	 *            允许的文件类型
	 * @return
	 */
	public List<MultipartFile> getFiles(HttpServletRequest request, List<FileUploadConfig.FileType> types) {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());

		MultipartHttpServletRequest req = null;
		if (!resolver.isMultipart(request)) {
			logger.error("the http request is not a multipart");
			return null;
		}
		req = resolver.resolveMultipart(request);
		List<MultipartFile> list = req.getFiles(config.getInputName());
		List<MultipartFile> files = new ArrayList<>();
		for (MultipartFile file : list) {
			FileUploadConfig.FileType type = this.getFileType(file);
			if (this.validateFile(file.getSize(), types, type)) {
				files.add(file);
			}
		}
		return files;
	}

	
	/**
	 * 上传文件ChargeRecordAttachEntity 对应的表中 getFiles(HttpServletRequest request,
	 * List<FileType> types) {
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public MultipartFile validateAndParseMultipartFile(HttpServletRequest request) {
		List<FileUploadConfig.FileType> types = new ArrayList<FileUploadConfig.FileType>();
		types.add(FileUploadConfig.FileType.IMAGE);
		List<MultipartFile> multipartFileList = this.getFiles(request, types);
		if (multipartFileList == null) {
			throw new BusinessException("上传文件不能为空");
		}
		if (multipartFileList.isEmpty()) {
			throw new BusinessException("上传文件不能为空");
		}
		if (multipartFileList != null && multipartFileList.size() > 1) {
			throw new BusinessException("一次只能上传一个文件");
		}
		MultipartFile mpFile = multipartFileList.get(0);
		return mpFile;
		// try {
		// String name = mpFile.getOriginalFilename();// 原始文件名
		// ChargeRecordAttachEntity attachEntity=new ChargeRecordAttachEntity();
		// attachEntity.setAttach(mpFile.getBytes());
		// attachEntity.setAttachsize(new Long(mpFile.getSize()).doubleValue());
		// attachEntity.setAttachname(name);
		// attachEntity.setChargeId(Constants.ID_NEED_RESOLVED_LATER);
		// attachEntity.setCreatedate(new Date());
		// return chargeService.insertChargeRecordAttachEntity(attachEntity);
		// } catch (Exception e) {
		// logger.error("An error happened during
		// insertChargeRecordAttachEntity", e);
		// throw new BusinessException("上传文件出错");
		// }

	}
	/**
	 * 下载文件
	 */
	public boolean downloadFile(String path, OutputStream destStream){
		return ftpService.downFile(path,destStream);
	}
	/**
	 * 拼接保存文件的相对路径
	 *
	 * @param enterpriseId
	 * @return
	 */
	private String getFolder(int enterpriseId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		StringBuilder folder = new StringBuilder();
		folder.append("/").append(String.valueOf(enterpriseId)).append("/").append(sdf.format(new Date())).append("/");

		return folder.toString();
	}

	/**
	 * 获取当前文件类型
	 * 
	 * @param file
	 * @return
	 */
	private FileUploadConfig.FileType getFileType(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		Iterator<Entry<String, String[]>> iter = config.getExts().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) iter.next();
			String name = entry.getKey();
			String[] exts = entry.getValue();
			Arrays.sort(exts);
			if (Arrays.binarySearch(exts, suffix) >= 0) {
				return FileUploadConfig.FileType.valueOf(name.toUpperCase());
			}
		}

		return FileUploadConfig.FileType.OTHER;
	}

	/**
	 * 验证文件类型和大小
	 *
	 * @param fileSize
	 *            文件大小
	 * @param types
	 *            限制的文件类型
	 * @param type
	 *            当前文件类型
	 * @return
	 */
	private boolean validateFile(long fileSize, List<FileUploadConfig.FileType> types, FileUploadConfig.FileType type) {
		if (type == FileUploadConfig.FileType.OTHER) {
			return false;
		}

		// 判断是否在允许上传的文件列表当中
		boolean validate = false;
		for (FileUploadConfig.FileType fileType : types) {
			if (fileType == type) {
				validate = true;
				break;
			}
		}
		if (!validate) {
			return false;
		}

		Integer maxSize = config.getMaxSize().get(type.name().toLowerCase());
		if (maxSize == null) {// 文件类型没有配置限制大小
			return false;
		}

		if (fileSize <= 0 || fileSize > maxSize * 1024 * 1024) {// 文件为空或者超大
			return false;
		}

		return true;
	}

	/**
	 * 文件大小转换为KB
	 *
	 * @param size
	 * @return
	 */
	private long getSize(long size) {
		BigDecimal bg = new BigDecimal(size * 1.0 / 1024);
		return bg.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
	}

	/**
	 * 上传文件到FTP
	 *
	 * @param mpFile
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public FileInfo uploadFileToFTP(MultipartFile mpFile, String path) throws Exception {
		String folder = path;
		String name = mpFile.getOriginalFilename();// 原始文件名
		String suffix = name.substring(name.lastIndexOf(".") + 1).toLowerCase();// 文件后缀
		String fileName = UUID.randomUUID().toString() + "." + suffix;// 新文件名

		String filePath = folder + fileName;// 相对路径
		ftpService.uploadFile(filePath, mpFile.getInputStream());

		FileInfo info = new FileInfo();
		info.setName(fileName);
		info.setSuffix(suffix);
		info.setPath(folder);
		info.setSize(this.getSize(mpFile.getSize()));
		info.setUdate(new Date());
		return info;
	}

	/**
	 * 下载文件
	 */
	public boolean downloadFileFromFTP(String path, OutputStream destStream){
		return ftpService.downFile(path,destStream);
	}

}

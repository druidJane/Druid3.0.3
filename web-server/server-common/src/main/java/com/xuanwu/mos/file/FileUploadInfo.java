package com.xuanwu.mos.file;

import java.util.List;

/**
 * @Description File Upload Info
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2013-11-16
 * @Version 1.0.0
 */
public class FileUploadInfo {

	private UploadResult result;
	private List<FileInfo> files;

	public FileUploadInfo(UploadResult result, List<FileInfo> files) {
		this.result = result;
		this.files = files;
	}

	public UploadResult getResult() {
		return result;
	}

	public void setResult(UploadResult result) {
		this.result = result;
	}

	public List<FileInfo> getFiles() {
		return files;
	}

	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}

	public static enum UploadResult {
		SUCCESS(0), EMPTY_FILES(1), EMPTY_REQUEST(2), TOO_MANY_FILES(3), ERROR(-1);

		private int index;

		private UploadResult(int index) {
			this.index = index;
		}

		public static UploadResult getResult(int index) {
			for (UploadResult result : UploadResult.values()) {
				if (result.getIndex() == index) {
					return result;
				}
			}
			return ERROR;
		}

		public int getIndex() {
			return index;
		}
	}
}

package com.xuanwu.mos.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @Description File Upload Config
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2016-08-19
 * @Version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "fileUploadConfig")
public class FileUploadConfig {

	private String inputName;
	private HashMap<String, String[]> exts;
	private HashMap<String, Integer> maxSize;

	public static String ROOT_FOLDER;

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public HashMap<String, String[]> getExts() {
		return exts;
	}

	public void setExts(HashMap<String, String[]> exts) {
		this.exts = exts;
	}

	public HashMap<String, Integer> getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(HashMap<String, Integer> maxSize) {
		this.maxSize = maxSize;
	}

	public static String getRootFolder() {
		return ROOT_FOLDER;
	}

	public static void setRootFolder(String rootFolder) {
		ROOT_FOLDER = rootFolder;
	}

	public static enum FileType {
		OTHER(0), IMAGE(1), DOCUMENT(2), AUDIO(3), RAR(4);

		private int index;

		private FileType(int index) {
			this.index = index;
		}

		public static FileType getType(int index) {
			for (FileType type : FileType.values()) {
				if (type.getIndex() == index) {
					return type;
				}
			}
			return OTHER;
		}

		public int getIndex() {
			return index;
		}
	}
}

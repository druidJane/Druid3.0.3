package com.xuanwu.mos.file;

import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.MosBizDataType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 林泽强 on 2016/8/22. 文件操作工具类
 */
public class FileUtil {

	public static final String DIR_TMP = "/tmp/";

	public static final String DIR_FILES = "/files/";

	public static final int MAX_MEDIA_FILE_LENGTH = 1 * 1024 * 1024; //1M

	// 文件日期格式
	public static final FastDateFormat fileFormat = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

	// 序列号
	private static AtomicInteger genrator = new AtomicInteger();

	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

	static {
		FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
		FILE_TYPE_MAP.put("jpeg", "FFD8FF"); //JPEG (jpg)
		FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)
		FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)
		FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)
		FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
		FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)
		FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)
		FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
		FILE_TYPE_MAP.put("xml", "3C3F786D6C");
		FILE_TYPE_MAP.put("zip", "504B0304");
		FILE_TYPE_MAP.put("rar", "52617221");
		FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)
		FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
		FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
		FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)
		FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word
		FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
		FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
		FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
		FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
		FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
		FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
		FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)
		FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
		FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)
		FILE_TYPE_MAP.put("amr", "2321414D");  //AMR
		FILE_TYPE_MAP.put("mpg", "000001BA");
		FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
		FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
		FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)
	}

	public static String getCharset(byte[] head) {
		String code = "GBK";
		if (head[0] == (byte) 0xFF && head[1] == (byte) 0xFE) {
			code = "UTF-16LE";
		} else if (head[0] == (byte) 0xFE && head[1] == (byte) 0xFF) {
			code = "UTF-16BE";
		} else if (head[0] == (byte) 0xEF && head[1] == (byte) 0xBB && head[2] == (byte) 0xBF) {
			code = "UTF-8";
		}
		return code;
	}

	public static String getCharset(File file){
		String code = "GBK";
		FileInputStream fis = null;
		try{
			byte[] head = new byte[3];
			fis = new FileInputStream(file);
			fis.read(head);
			code = getCharset(head);
		} catch (Exception e){
			//
		} finally {
			closeInputStrem(fis);
		}
		return code;
	}

	/**
	 * 获取文件类型
	 *
	 * @param fileName
	 * @return
	 */
	public static FileType getFileType(String fileName) {
		return FileType.getType(fileName);
	}

	public static void closeReader(Reader reader) {
		if (reader == null)
			return;
		try {
			reader.close();
		} catch (Exception e) {
			// TODO: Ignore it
		}
	}

	// 创建导入文件对象
	public static File createImportFile(BizDataType dataType, FileType fileType, String contextPath) {
		String path = getFilePath(dataType);
		return createFile(contextPath, path, fileType);
	}

	// public static File getResultFile(BizDataType dataType,FileType fileType,
	// String contextPath) {
	// String path = getFilePath(dataType);
	// return null;
	// }

	public static File getImportedFile(BizDataType dataType, String fileName, String contextPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		String path = getFilePath(dataType);
		sb.append(path);
		sb.append(fileName);
		return new File(sb.toString());
	}

	private static File createFile(String contextPath, String path, FileType fileType) {
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		sb.append(path);
		File dir = new File(sb.toString());
		if (!dir.exists())
			dir.mkdirs();
		sb.append(fileFormat.format(new Date()));
		sb.append(getSerialNum());
		sb.append(fileType.getType());
		return new File(sb.toString());
	}

	private static String getSerialNum() {
		int num = genrator.getAndIncrement();
		num = Math.abs(num % 100);
		return String.format("%02d", num);
	}

	private static String getFilePath(BizDataType dataType) {
		return (dataType == BizDataType.Tmp) ? DIR_TMP : getDirByType(DIR_FILES, dataType);
	}
	public static String getDataServerPath(MosBizDataType dataType){
		return (dataType == MosBizDataType.Tmp) ? DIR_TMP : MosBizDataType.getDirByType(DIR_FILES, dataType);
	}
	private static String getDirByType(String parentDir, BizDataType type) {
		StringBuilder sb = new StringBuilder();
		sb.append(parentDir);
		sb.append(type.name().toLowerCase());
		//sb.append(File.separator);
		sb.append(File.separator);
		return sb.toString();
	}

	public static void closeOutputStream(OutputStream out) {
		if (out == null) {
			return;
		}
		try {
			out.flush();
			out.close();
		} catch (Exception e) {
			// Todo temporary ignore it
		}
	}

	public static File createExportDir(BizDataType dataType, FileType fileType, String contextPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		String path = getFilePath(dataType);
		sb.append(path);
		sb.append(dataType.name().toLowerCase());
		sb.append(fileFormat.format(new Date()));
		sb.append(getSerialNum());
		File dir = new File(sb.toString());
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}

	public static File createImportFailedDir(BizDataType dataType, String fileName, String contextPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		String path = getFilePath(dataType);
		sb.append(path);
		sb.append(dataType.name().toLowerCase());
		sb.append(subFileName(fileName));
		File dir = new File(sb.toString());
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}

	public static String subFileName(String fileName) {
		if (StringUtils.isBlank(fileName))
			throw new NullPointerException("File name is null");
		int idx = fileName.lastIndexOf('.');
		if (idx < 0) {
			return "";
		}
		return fileName.substring(0, idx);
	}

	public static File getExportFile(BizDataType dataType, String fileName, String contextPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		String path = getFilePath(dataType);
		sb.append(path);
		sb.append(fileName);
		return new File(sb.toString());
	}

	public static void closeWriter(Writer writer) {
		if (writer == null)
			return;
		try {
			writer.close();
		} catch (Exception e) {
			// TODO: Ignore it
		}
	}

	public static void closeOutputStrem(OutputStream out) {
		if (out == null)
			return;
		try {
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO: Ignore it
		}
	}

	public static void closeInputStrem(InputStream in) {
		if (in == null)
			return;
		try {
			in.close();
		} catch (Exception e) {
			// TODO: Ignore it
		}
	}

	/**
	 * 压缩目录
	 * 
	 * @param dir
	 * @param file
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static void zipDirectory(String dir, File file) throws IOException, IllegalArgumentException {
		ZipOutputStream out = null;
		try {
			File d = new File(dir);
			if (!d.isDirectory())
				throw new IllegalArgumentException("Not a directory:" + dir);

			File[] files = d.listFiles();
			out = new ZipOutputStream(new FileOutputStream(file));

			int bytesRead;
			byte[] buffer = new byte[8192];
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isDirectory())
					continue;
				FileInputStream in = null;
				try {
					in = new FileInputStream(f);
					ZipEntry entry = new ZipEntry(f.getName());
					out.putNextEntry(entry);
					while ((bytesRead = in.read(buffer)) != -1)
						out.write(buffer, 0, bytesRead);
				} finally {
					closeInputStrem(in);
				}
			}
			FileUtils.deleteDirectory(d);
		} finally {
			closeOutputStrem(out);
		}
	}


	/**
	 * 根据文件名和文件内容检查文件的有效性，
	 * 如果文件名和文件内容格式不一致，则判断为非法文件
	 * @param 
	 * @return
	 */
	public static boolean isValidFile(FileItem fileItem) {
		String fileType = FileUtil.getTypeName(fileItem.getName().toLowerCase());

		String fileContentFragment = FILE_TYPE_MAP.get(fileType);
		if (StringUtils.isNotBlank(fileContentFragment)) {
			FileInputStream fis = null;
			try {
				String fileContent = getFileHexString(fileItem.get());

				if (StringUtils.isNotBlank(fileContent))
					return fileContent.toUpperCase().startsWith(fileContentFragment);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeInputStrem(fis);
			}
		}
		return false;
	}

    public static String getType(String fileName) {
        int idx = fileName.lastIndexOf('.');
        if (idx < 0) {
            return "";
        }
        return fileName.substring(idx);
    }

	public static String getTypeName(String fileName){
		int idx = fileName.lastIndexOf('.');
		if(idx < 0){
			return "";
		}
		return fileName.substring(idx + 1);
	}

	public final static String getFileHexString(byte[] b) {
		StringBuilder stringBuilder = new StringBuilder();
		if (b == null || b.length <= 0) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static File createTmpFile(String contextPath, String fileType){
		return FileUtil.createCustomPathFile(contextPath,DIR_TMP,fileType);
	}

	public static File createCustomPathFile(String contextPath,String customPath, String fileType){
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		sb.append(customPath);
		File dir = new File(sb.toString());
		if (!dir.exists())
			dir.mkdirs();
		sb.append(fileFormat.format(new Date()));
		sb.append(getSerialNum());
		sb.append(fileType);
		return new File(sb.toString());
	}

	public static boolean delImportedFile(BizDataType dataType, String fileName,
										  String contextPath){
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		String path = getFilePath(dataType);
		sb.append(path);
		sb.append(fileName);
		File file = new File(sb.toString());
		return file.delete();
	}

	public static File getTmpFile(String contextPath, String fileName){
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		sb.append(DIR_TMP);
		sb.append(fileName);
		return new File(sb.toString());
	}


	public static File getImportedCustomFile(String fileName,
											 String contextPath,String customPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(contextPath);
		sb.append(customPath);
		sb.append(fileName);
		return new File(sb.toString());
	}

	public static byte[] readFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try{
			int fileLen = fis.available();
			if(fileLen > MAX_MEDIA_FILE_LENGTH){
				return null;
			}

			byte[] data = new byte[fileLen];
			fis.read(data);
			return data;
		} finally {
			if(fis != null){
				fis.close();
			}
		}
	}
}

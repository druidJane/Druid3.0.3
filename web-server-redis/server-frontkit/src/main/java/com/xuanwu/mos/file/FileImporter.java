package com.xuanwu.mos.file;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.Result;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.domain.enums.TaskResult;
import com.xuanwu.mos.exception.FileImportException;
import com.xuanwu.mos.file.exporter.ExportInfo;
import com.xuanwu.mos.file.handler.FileHandler;
import com.xuanwu.mos.file.handler.FileHandlerFactory;
import com.xuanwu.mos.file.importbean.ChargeAccountImport;
import com.xuanwu.mos.file.importbean.UserImport;
import com.xuanwu.mos.file.importer.BaseImporter;
import com.xuanwu.mos.file.importer.ImportInfo;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.service.ImportDataService;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.ListUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by 林泽强 on 2016/8/25. 文件导入器
 */
@Component
public class FileImporter extends BaseImporter {

    private static final Logger logger = LoggerFactory.getLogger(FileImporter.class);

    private static final List<String> SUPPORTED_FILE_TYPES = Arrays.asList(
            "mid", "amr", "jpg", "jpeg", "gif", "bmp", "png","mp3", "avi", "mkv", "mpeg", "rmvb"
    );

    // 该变量中的类型采用比对请求解析之后得到的content-type和上传文件的扩展名是否一致。
    // 而不使用解析该文件的二进制流，比较流的前几个十六进制值(找不到该类型固定的前几位类型) --- guoyaohui-2017-06-08
    private static final Map<String,String> SUPPORT_CONTENT_TYPES = new HashMap<>();


    @PostConstruct
    public void initContentType() {
        // 以下为找不到标准的文件头，因此使用比对contentType的方式

        // mos视频
        SUPPORT_CONTENT_TYPES.put("mp4", "video/mp4");
        SUPPORT_CONTENT_TYPES.put("rmvb", "video/rmvb");
        SUPPORT_CONTENT_TYPES.put("avi", "video/avi");
        SUPPORT_CONTENT_TYPES.put("mpeg", "video/mpeg");
        SUPPORT_CONTENT_TYPES.put("3gp", "video/3gpp");

        // mos音频
        SUPPORT_CONTENT_TYPES.put("mp3", "audio/mp3");
        SUPPORT_CONTENT_TYPES.put("wma", "audio/x-ms-wma");
        SUPPORT_CONTENT_TYPES.put("wav", "audio/wav");
    }



    @Autowired
    private ImportDataService importDataService;

    @Autowired
    private DataConverter converter;

    @Override
    @Autowired
    public void setConfig(Config config) {
        super.config = config;
    }

    @Override
    public void batchImport(FileTask task, List<String[]> rowList, FileHandler fileHandler, ExportInfo resultInfo) throws Exception {
        ImportInfo importInfo = null;
        task.addHandledCount(rowList.size());
        List<String[]> cellsList = Collections.emptyList();
        String[] headCells = null;
        switch (task.getDataType()) {
            case Blacklist:
                headCells = FileHead.getImportFailedHead(FileHeader.BLACKLIST);
                importInfo = importBlacklist(task, rowList);
                break;
            case WhitePhone:
                headCells = FileHead.getImportFailedHead(FileHeader.WHITEPHONE);
                importInfo = importWhitePhone(task, rowList);
                break;
            case Keyword:
                headCells = FileHead.getImportFailedHead(FileHeader.KEYWORD);
                importInfo = importKeyWord(task, rowList);
                break;
            case ChargeAccount:
                headCells = FileHead.getImportFailedHead(FileHeader.CHARGE_ACCOUNT);
                importInfo = importChargeAccount(task, rowList);
                break;
            case User:
                headCells = FileHead.getImportFailedHead(FileHeader.USER_MGR_FRONTKIT);
                importInfo = importUser(task, rowList);
                break;
            case Contact:
                headCells = FileHead.getImportFailedHead(FileHeader.CONTACT_IMPORT);
                importInfo = importContact(task, rowList);
                break;
            default:
                throw new FileImportException("Invalid data type: " + task.getDataType());
        }

        //写结果文件
        List<AbstractEntity> entities = importInfo.drainTo(new ArrayList<AbstractEntity>());
        cellsList = converter.getCellsList(entities, true);
        if (importInfo.getResult() == Result.Failed) {
            cellsList = rowList;
        } else {
            task.setResult(TaskResult.Success);
        }
        writeResultInfoFile(headCells, cellsList, task, resultInfo, fileHandler);
    }

    private ImportInfo importContact(FileTask task, List<String[]> rowList) {
        ImportInfo impInfo = new ImportInfo();
        try{
            int entId = Integer.parseInt(task.getParamsMap().get("entId"));
            int userId = Integer.parseInt(task.getParamsMap().get("userId"));
            List<Contact> contactList = converter.parseContact(entId, userId, rowList, task.getParamsMap());
            if (ListUtil.isNotBlank(contactList))
                impInfo = importDataService.importContact(entId, userId, contactList, task);
        }catch (Exception e) {
            logger.error("Import contact falied,cause by:{}", e);
            impInfo.setResult(Result.Failed);
        }
        return impInfo;
    }

    private ImportInfo importKeyWord(FileTask task, List<String[]> rowList) {
        ImportInfo impInfo = new ImportInfo();
        try {
            List<KeyWord> list = converter.parseKeyWord(rowList, task.getParamsMap());
            if (ListUtil.isNotBlank(list))
                impInfo = importDataService.importKeyWord(list, task);

        } catch (Exception e) {
            logger.error("Import keyWord failed,cause by:{}", e);
            impInfo.setResult(Result.Failed);
            throw e;
        }
        return impInfo;
    }

    private ImportInfo importWhitePhone(FileTask task, List<String[]> rowList) throws Exception{
        ImportInfo impInfo = new ImportInfo();
        try {
            List<WhitePhone> list = converter.parseWhitePhone(rowList, task.getParamsMap());
            if (ListUtil.isNotBlank(list))
                impInfo = importDataService.importWhitePhone(list, task);

        } catch (Exception e) {
            logger.error("Import whitePhone failed,cause by:{}", e);
            impInfo.setResult(Result.Failed);
            throw e;
        }
        return impInfo;
    }

    /**
     * 用户管理->导入用户
     */
    private ImportInfo importUser(FileTask task, List<String[]> rowList)throws Exception {
        ImportInfo importInfo = new ImportInfo();
        try {
            List<UserImport> users = converter.parseUser(rowList, task.getParamsMap());
            if (ListUtil.isNotBlank(users)) {
				importInfo = importDataService.importUser(users, task);
			}
        } catch (Exception e) {
            logger.error("Import user failed,cause by:{}", e);
            importInfo.setResult(Result.Failed);
            throw e;
        }
        return importInfo;
    }

    /**
     * 计费账户管理->导入充值
     */
    private ImportInfo importChargeAccount(FileTask task, List<String[]> rowList) throws Exception {
        ImportInfo impInfo = new ImportInfo();
        try {
            List<ChargeAccountImport> chargeAccounts = converter.parseChargeAccount(rowList, task.getParamsMap());
            if (ListUtil.isNotBlank(chargeAccounts))
                impInfo = importDataService.importChargeAccount(chargeAccounts, task);

        } catch (Exception e) {
            logger.error("Import charging account failed,cause by:{}", e);
            impInfo.setResult(Result.Failed);
            throw e;
        }
        return impInfo;
    }

    /**
     * 导入黑名单
     */
    private ImportInfo importBlacklist(FileTask task, List<String[]> rowList) throws Exception {
        ImportInfo impInfo = new ImportInfo();
        try {
            List<BlackList> blacklists = converter.parseBlacklist(rowList, task.getParamsMap());
            if (ListUtil.isNotBlank(blacklists))
                impInfo = importDataService.importBlacklist(blacklists, task);

        } catch (Exception e) {
            logger.error("Import blacklist failed,cause by:{}", e);
            impInfo.setResult(Result.Failed);
            throw e;
        }
        return impInfo;
    }

    /**
     * 写结果信息文件
     */
    private void writeResultInfoFile(String[] headCells, List<String[]> cellsList,
                                     FileTask task, ExportInfo resultInfo, FileHandler fileHandler) throws Exception {
        if (ListUtil.isBlank(cellsList)) {
            task.setFileName("");//not failed item, set the file name to blank
            return;
        }

        if (!resultInfo.isExistsDirectoy())
            resultInfo.createImportFailedDirectory();
        File file = resultInfo.getCurrentFile(cellsList.size(), task);

        List<String[]> tempList = new ArrayList<String[]>();
        if (resultInfo.isNewFile())
            tempList.add(headCells);
        tempList.addAll(cellsList);

        if (resultInfo.getFileType() == FileType.Csv) {
            fileHandler.writeFile(file, Delimiters.COMMA, resultInfo.isNewFile(),
                    tempList, Charset.forName("gbk"));
        } else {
            fileHandler.writeFile(file, Delimiters.COMMA, resultInfo.isNewFile(),
                    tempList, Charset.forName("UTF-8"));
        }

        /*fileHandler.writeFile(file, Delimiters.COMMA, resultInfo.isNewFile(),
                tempList, Charset.forName("UTF-8"));*/
        task.setFileName(file.getAbsolutePath());   //写回结果文件路径
    }

    /**
     * 压缩结果信息文件目录
     */
    public boolean zipResultInfoFileDirectory(ExportInfo resultInfo) {
        try {
            File dir = resultInfo.getDirectoy();
            if (dir == null || !dir.exists()) {
                return false;
            }
            File zipFile = resultInfo.createZipFile();
            FileUtil.zipDirectory(resultInfo.getDirectoy().getAbsolutePath(), zipFile);
            return true;
        } catch (Exception e) {
            logger.error("Zip export directory failed,cause by:{}", e);
            return false;
        }
    }

    public UploadResult uploadMedia(HttpServletRequest request) {
        return uploadMedia(request, null);
    }

    /**
     * 上传彩信中的图片视频音频等文件
     */
    public UploadResult uploadMedia(HttpServletRequest request, String customUploadPath) {
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

            FileItem fileItem = null;
            // Parse the request
            List<?> fileItems = upload.parseRequest(request);
            if (fileItems == null) {
                return new UploadResult(StatusCode.Other);
            }
            fileItem = (FileItem) fileItems.get(0);
            // upload file
            FileHandler fileHandler = FileHandlerFactory.getFileHandler(FileType.Media);
            File file = null;
            if (StringUtils.isBlank(customUploadPath)) {
                file = FileUtil.createTmpFile(Config.getContextPath(),
                        FileUtil.getType(fileItem.getName().toLowerCase()));
            } else {
                file = FileUtil.createCustomPathFile(Config.getContextPath(), customUploadPath,
                        FileUtil.getType(fileItem.getName().toLowerCase()));
            }
            // check file type
            if (!checkMediaFileType(fileItem)) {
                return new UploadResult(StatusCode.InvalidFileType);
            }

            fileHandler.upload(fileItem, file);
            UploadResult ret = new UploadResult(StatusCode.Success,
                    file.getName(), null);
            ret.setFileSize(file.length());
            ret.setNewFileName(file.getName());
            return ret;
        } catch (Exception e) {
            logger.error("Upload file failed,cause by:{}", e);
            if (e instanceof FileUploadBase.SizeLimitExceededException) {
                return new UploadResult(StatusCode.FileSizeExceeded);
            }
        }
        return new UploadResult(StatusCode.Other);
    }


    /**
     * 检查多媒体文件类型
     * 支持的类型为：mid amr jpg jpeg gif bmp png
     * @param fileItem
     * @return
     */
    public boolean checkMediaFileType(FileItem fileItem) {
        // 获取文件类型
        String fileType = FileUtil.getTypeName(fileItem.getName());
        if (org.apache.commons.lang.StringUtils.isBlank(fileType)) return false;

        if (SUPPORT_CONTENT_TYPES.keySet().contains(fileType)) {
            if (SUPPORT_CONTENT_TYPES.get(fileType).equals(fileItem.getContentType())) {
                return true;
            }
            return false;
        } else if (!SUPPORTED_FILE_TYPES.contains(fileType.toLowerCase())) {
            return false;
        }
        return FileUtil.isValidFile(fileItem);
    }
}

/*
 * Copyright (c) 2016年10月25日 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @version 1.0.0
 * @Description
 * @date 2016-10-25
 */
public class FtpUtil {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param url      FTP服务器hostname
     * @param port     FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path     FTP服务器保存目录
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url, int port, String username, String password, String path, String filename, InputStream input) {
        if (StringUtils.isBlank(filename)) {
            logger.error("uploadFile:filename is blank");
            return false;
        }
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.setDefaultTimeout(100 * 1000);
            ftp.connect(url, port);//连接FTP服务器
            ftp.setSoTimeout(60 * 1000);
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.error("uploadFile:ftp connect is not positive completion username:"+username+",password:"+password+",url:"+url+",port:"+port);
                ftp.disconnect();
                return success;
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setBufferSize(2 * 1024);
            ftp.setDataTimeout(60 * 1000);
            ftp.changeWorkingDirectory(path);
            filename = makeDir(ftp, filename);
            ftp.storeFile(filename, input);
            ftp.logout();
            success = true;
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }
        }
        return success;
    }

    private static String makeDir(FTPClient ftp, String fileName) throws IOException {
        int idx = fileName.indexOf(Delimiters.SLASH);
        if(-1 == idx){
            idx = fileName.indexOf(Delimiters.BACKSLASH);
        }
        if (-1 != idx) {
            String tmpPath = fileName.substring(0, idx);
            if(StringUtils.isNotBlank(tmpPath) && !Delimiters.DOT.equals(tmpPath)){
                ftp.makeDirectory(tmpPath);
                ftp.changeWorkingDirectory(tmpPath);
            }
            fileName = fileName.substring(idx+1);
            return  makeDir(ftp, fileName);
        }
        return fileName;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param url        FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     */
    public static boolean downFile(String url, int port, String username, String password, String remotePath, String fileName, OutputStream destStream){
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.setDefaultTimeout(100 * 1000);
            ftp.connect(url, port);//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.setSoTimeout(60 * 1000);
            ftp.login(username, password);//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.error("downFile:ftp connect is not positive completion");
                return false;
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setBufferSize(2 * 1024);
            ftp.setDataTimeout(60 * 1000);
            remotePath = remotePath+fileName.substring(0, fileName.lastIndexOf("/"));
            fileName = fileName.substring(fileName.lastIndexOf("/")+1, fileName.length());
            ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
            if (!ftp.retrieveFile(fileName, destStream)) {
                logger.info("not found file {}/{}",remotePath,fileName);
                return false;
            }
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }
        }
        return false;
    }
}

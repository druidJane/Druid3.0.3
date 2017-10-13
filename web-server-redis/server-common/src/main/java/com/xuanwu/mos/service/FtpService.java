/*
 * Copyright (c) 2016年10月25日 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.service;


import com.xuanwu.mos.config.FtpConfig;
import com.xuanwu.mos.utils.FtpUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @version 1.0.0
 * @Description
 * @date 2016-10-25
 */
@Component
public class FtpService {

    @Autowired
    private FtpConfig ftpConfig;

    /**
     * 文件上传
     */
    public boolean uploadFile(String fileName, InputStream is) {
        return FtpUtil.uploadFile(
                ftpConfig.getUrl(),
                ftpConfig.getPort(),
                ftpConfig.getUsername(),
                ftpConfig.getPassword(),
                ftpConfig.getBasePath(),fileName,is);
    }

    /**
     * 文件下载
     * @param fileName
     * @return
     */
    public boolean downFile(String fileName,OutputStream destStream){
        if (destStream != null) {
            return FtpUtil.downFile(ftpConfig.getUrl(),
                    ftpConfig.getPort(),
                    ftpConfig.getUsername(),
                    ftpConfig.getPassword(),
                    ftpConfig.getBasePath(),
                    fileName,
                    destStream);
        }
        return false;
    }
}

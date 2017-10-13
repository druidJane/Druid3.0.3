package com.xuanwu.mos.rest.service.smsmgmt.impl;

import com.alibaba.fastjson.JSONArray;
import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.SubContent;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.mos.file.handler.FileHandler;
import com.xuanwu.mos.file.handler.FileHandlerFactory;
import com.xuanwu.mos.file.handler.RowHandler;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.utils.DynVarUtil;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.GroupMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.MassMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgContent;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgSingle;
import com.xuanwu.msggate.common.util.Constants;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

/**
 * Created by Jiang.Ziyuan on 2017/3/28.
 */
@Service
public class SendSmsService {
    private final static Logger logger = LoggerFactory.getLogger(SendSmsService.class);

    private Config config;
    private Map<String, Integer> contactMap;
    // 性别(0:女士,1:先生) 出生日期(yyyy-MM-dd)
    protected final String[] keys = {  "发送号码","姓名","手机号码","性别","出生日期","编号","VIP","备注" };
    @PostConstruct
    public void init() {
        this.setContactKeys(keys);
    }
    @Autowired
    private FrontKitPackSender packSender;

    public MsgFrame buildGroupFrame(List<Contact> contacts,
                                    List<String[]> fileNames, final List<SubContent> subs, final MsgPack pack) {
        GroupMsgFrame frame = new GroupMsgFrame();
        final List<MsgSingle> msgs = frame.getAllMsgSingle();
        PMsgContent msgContent = null;
        String varValue = null;
        //免审变量短信，短信内容为json数据
        Boolean varTemplate = false;
        if(pack.getParameter(Constants.TEMPLATE_NO) != null){
            varTemplate = true;
        }
        final StringBuilder sb = new StringBuilder(512);
        final MsgContent.MsgType msgType = pack.getMsgType();
        //根据通讯录构造变量短信数据
        for (Contact contact : contacts) {
            String content = convertContent(subs, contact.getArray(), varTemplate, this.contactMap);
            msgContent = new PMsgContent();
            msgContent.setContent(content);
            msgs.add(new PMsgSingle(msgType, contact.getPhone(),
                    msgContent, null, null, contact.isVip(), 0));
            sb.delete(0, sb.length());
        }
        final String phoneReg = config.getPhonePattern();
        //根据导入文件构造变量短信数据
        for (String[] arr : fileNames) {
            FileType fileType = FileUtil.getFileType(arr[0]);
            File file = FileUtil.getImportedFile(BizDataType.SendSms, arr[0],
                    Config.getContextPath());
            FileHandler fileHandler = FileHandlerFactory
                    .getFileHandler(fileType);
            final Map<String, Integer> headMap = new HashMap<String, Integer>();

            final Boolean finalVarTemplate = varTemplate;
            fileHandler.readAll(file.getAbsolutePath(), arr[1],
                    new RowHandler() {
                        @Override
                        public boolean handleHead(String[] cells) {
                            for (int i = 0; i < cells.length; i++) {
                                headMap.put(cells[i], i);
                            }
                            return true;
                        }

                        @Override
                        public boolean handleRow(String[] cells) {
                            //构造变量短信数据
                            PMsgContent msgContent = new PMsgContent();
                            String content = convertContent(subs,cells,finalVarTemplate,headMap);
                            msgContent.setContent(content);
                            // 第一个元素必为手机号码
                            if (cells == null || cells.length == 0)
                                return true;
                            if (cells[0].matches(phoneReg)) {
                                msgs.add(new PMsgSingle(msgType, cells[0],
                                        msgContent, null, null, false, 0));
                            }
                            sb.delete(0, sb.length());
                            return true;
                        }
                    });
        }
        frame.setScheduleTime(pack.getScheduleTime());
        frame.setBizType(pack.getBizType());
        frame.setMsgType(pack.getMsgType());
        frame.setReportState(true);
        return frame;
    }

    public MsgFrame buildMassFrame(List<Contact> contacts,
                                   List<String[]> fileNames, final String content, MsgPack pack) {
        MassMsgFrame frame = new MassMsgFrame();
        final List<MsgSingle> msgs = frame.getAllMsgSingle();
        final PMsgContent msgContent = new PMsgContent();
        msgContent.setContent(content);
        frame.setContent(msgContent);
        MsgContent.MsgType msgType = pack.getMsgType();
        for (Contact contact : contacts) {
            msgs.add(new PMsgSingle(msgType, contact.getPhone(), null,
                    null, null, contact.isVip(), 0));
        }
        final String phoneReg = config.getPhonePattern();
        for (String[] arr : fileNames) {
            FileType fileType = FileUtil.getFileType(arr[0]);
            File file = FileUtil.getImportedFile(BizDataType.SendSms, arr[0],
                    Config.getContextPath());
            FileHandler fileHandler = FileHandlerFactory.getFileHandler(fileType);
            fileHandler.readAll(file.getAbsolutePath(), arr[1],
                    new RowHandler() {
                        @Override
                        public boolean handleHead(String[] cells) {
                            // do nothing
                            return true;
                        }

                        @Override
                        public boolean handleRow(String[] cells) {
                            // 第一个元素必为手机号码
                            if (cells == null || cells.length == 0)
                                return true;
                            if (cells[0].matches(phoneReg)) {
                                msgs.add(new PMsgSingle(MsgContent.MsgType.LONGSMS,
                                        cells[0], msgContent, null, null, false, 0));
                            }
                            return true;
                        }
                    });
        }
        frame.setScheduleTime(pack.getScheduleTime());
        frame.setBizType(pack.getBizType());
        frame.setMsgType(pack.getMsgType());
        frame.setReportState(true);
        return frame;
    }

    public void setContactKeys(String[] keys) {
        HashMap<String, Integer> contactMap = new HashMap<String, Integer>();
        for (int i = 0; i < keys.length; i++) {
            contactMap.put(keys[i], i);
        }
        this.contactMap = contactMap;
    }



    /**
     *
     * @param subs 模板内容集合，TXT VAR
     * @param strs 导入数据集合
     * @param finalVarTemplate 是否免审
     * @param headMap 数据列映射map
     * @return
     */
    String convertContent(List<SubContent> subs,String[] strs,boolean finalVarTemplate,Map<String, Integer> headMap){
        String content;
        JSONArray data = new JSONArray();
        String varValue = null;
        final StringBuilder sb = new StringBuilder(512);
        Integer varCount = 0;
        for (SubContent sub : subs) {
            if (finalVarTemplate && sub.getType() == SubContent.SubType.VAR){
                if(varCount > strs.length-1){
                    throw new BusinessException("模板数据数量不匹配:"+varCount);
                }
                varValue = checkVar(sub,strs,varCount);
                if(StringUtils.isEmpty(varValue)){
                    throw new BusinessException(sub.getContent()+"不能为空");
                }
                data.add(varValue);
                varCount++;
            }else if(sub.getType() == SubContent.SubType.VAR) {
                varValue = checkVar(sub,strs,varCount);
                if(StringUtils.isEmpty(varValue)){
                    varValue = DynVarUtil.getVarValue(headMap,
                            sub.getContent(), strs);
                }
                if (StringUtils.isNotEmpty(varValue)) {
                    sb.append(varValue);
                }
                varCount++;
            } else {
                sb.append(sub.getContent());
            }

        }
        if(finalVarTemplate){
            content = data.toJSONString();
        }else{
            content = sb.toString();
        }
        return content;
    }

    private String checkVar(SubContent sub,String[] strs,Integer varCount) {
        if(varCount+1 >= strs.length) return "";
        Pattern pattern = Pattern.compile("\\d*\\.?\\d+");
        //跳过第一个发送号码，从第二个数据开始匹配
        String str = strs[varCount+1];
        if("中文变量".equals(sub.getContent())){
            if(str.length() > 6){
                throw new BusinessException("中文变量长度不能超过6位:"+str);
            }
            if(pattern.matcher(str).matches() ) {
                throw new BusinessException("中文变量不能为纯数字:"+str);
            }
        }else if("数字变量".equals(sub.getContent())){
            if(pattern.matcher(str).matches() ) {
                if (str.length() > 15 && !str.startsWith(".")) {
                    throw new BusinessException("数字变量长度不能超过15位:"+str);
                }
            }else{
                throw new BusinessException("无法识别数字变量:"+str);
            }
        }else {
            //若为用户自定义变量，则另做处理
            str = null;
        }
        return str;
    }

    @Autowired
    public void setConfig(Config config) {
        this.config = config;
    }


}

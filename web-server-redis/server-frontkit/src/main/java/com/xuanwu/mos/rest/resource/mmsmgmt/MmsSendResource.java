package com.xuanwu.mos.rest.resource.mmsmgmt;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.BaseSendResource;
import com.xuanwu.mos.rest.service.ContactService;
import com.xuanwu.mos.rest.service.mmsmgmt.SendMmsService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.FileParams;
import com.xuanwu.mos.vo.MmsInfoVo;
import com.xuanwu.mos.vo.MmsTemplateJson;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgPack;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/** 彩信管理 --> 发送彩信
 * Created by 郭垚辉 on 2017/3/23.
 */
@Component
@Path(Keys.SMSMGR_SENDMMS)
public class MmsSendResource extends BaseSendResource {

    private static final Logger logger = LoggerFactory.getLogger(MmsSendResource.class);

    @Autowired
    private FileImporter importer;
    @Autowired
    private Config config;
    @Autowired
    private FrontKitPackSender packSender;
    @Autowired
    private SendMmsService sendMmsService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private BaseMsgResource baseMsgResource;


    /**
     * 上传通讯录文件,并重命名该文件-搞定
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path(Keys.SMSMGR_SENDMMS_INDEX+"/upload")
    public JsonResp uploadContacts(@Context HttpServletRequest request) {
        try {
            UploadResult result = importer.upload(BizDataType.SendMms, request);
            StatusCode statusCode = result.getStatusCode();
            if (statusCode != StatusCode.Success) {
                return JsonResp.fail(statusCode.getIndex(), statusCode.getStateDesc());
            }
            return JsonResp.success(new FileParams(result.getOriginFileName(),result.getNewFileName()));
        } catch (Exception e) {
            logger.error("upload mms file failed: ", e);
            return JsonResp.fail("upload mms file failed: " + e.getMessage());
        }
    }

    /**
     * 解析上传的通讯录文件，将除第一行之外的所有行的数据进行解析，--搞定
     * 解析结果：{"data":["13546986214","张治中","1985/12/21"]}
     * 上传文件的导入也在此处解析
     * @return 返回相应的解析结果
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDMMS_INDEX+"/import")
    public JsonResp parseMmsConcats(@Context HttpServletRequest request, FileParams fileParams) {
        try {
            return importFile(request, config, BizDataType.SendMms,fileParams);
        } catch (Exception e) {
            logger.error("import mms file failed: ", e);
            return JsonResp.fail(StatusCode.Other.getIndex(), "import mms file failed: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param fileNames
     */
    private void delFiles(List<String[]> fileNames) {
        try {
            for (String[] fileName : fileNames) {
                FileUtil.delImportedFile(BizDataType.SendMms, fileName[0],
                        Config.getContextPath());
            }
        } catch (Exception e) {
            logger.error("After sent delete files failed: ", e);
        }
    }

    /**
     * 获取发送状态
     * @param req
     * @return
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path(Keys.SMSMGR_SENDMMS_INDEX + "/getSendingState")
    public JsonResp getSendingState(@Context HttpServletRequest req) {
        String step = sendingMap.get(req.getSession().getId());
        return JsonResp.success(step == null ? "请求提交中..." : step);
    }

    /**
     * 发送短信
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDMMS_DOSENDMMS)
    public JsonResp doSend(@Context HttpServletRequest req, @Valid MmsInfoVo mmsInfoVo) {
//        TrustIpStatus ipStatus = baseMsgResource.checkTrustIp(req);
//        if (ipStatus== TrustIpStatus.FAILED) {
//            return JsonResp.fail("发送用户的IP不在企业信任IP范围内");
//        } else if (ipStatus == TrustIpStatus.EXCEPTION) {
//            return JsonResp.fail("无法获取客户端的真实IP，请联系后台管理人员解决");
//        }
        HashMap<Object, Object> resultMap = new HashMap<>();
        String sessionId = req.getSession().getId();
        String[] contacts = preHandlerConcact(mmsInfoVo);
        sendingMap.put(sessionId, "请求处理中...");
        List<String[]> fileNames = new ArrayList<String[]>(5);
        boolean suc = false;
        SimpleUser user =  SessionUtil.getCurUser();
        try {
            if (contacts == null) {
                throw new IllegalArgumentException("no contacts");
            }
            MmsTemplateJson mmsTemplateJson = JsonUtil.deserialize(mmsInfoVo.getMms(),MmsTemplateJson.class);
            MmsContent mmsContent = mmsTemplateJson.getMms();
            if (!preHandleMmsContent(mmsContent)) {
                throw new IllegalArgumentException("no mms content");
            }

            PMsgPack pack = new PMsgPack();
            pack.setMsgType(MsgContent.MsgType.MMS);
            if(StringUtils.isEmpty(mmsInfoVo.getBatchName())){
                String batchName = VoUtil.genBatchName(user);
                pack.setBatchName(batchName);
            }else{
                pack.setBatchName(mmsInfoVo.getBatchName());
            }
            pack.setRemark(mmsInfoVo.getRemark());
            pack.setBizType(mmsInfoVo.getBizTypeId());
            pack.setScheduleTime(DateUtil.parse(mmsInfoVo.getScheduledTime(), DateUtil.DateTimeType.DateTime));
            pack.setDistinct(mmsInfoVo.isDistinct());
            sendingMap.put(sessionId, "号码提取中...");
            buildFrame(contacts, fileNames, mmsContent, pack);

            sendingMap.put(sessionId, "正发往网关...");
            MTResp resp = checkPack(pack);
            if (resp == null) {            	

                Map<String, String> accountMap = baseMsgResource.covertAccountMap(user, NetUtil.checkUserTrueIp(req));
                resp = packSender.send(accountMap, pack);
            }
            String respMsg = null;
            if (resp.getResult() == MTResult.SUCCESS) {
                String packId = resp.getUuid().toString();
                String data = "{\"packId\":\"" + packId + "\"}";
                respMsg = JsonUtil.toJSON(data, "发送成功", 0);
                suc = true;
                sendingMap.put(sessionId, "发送成功");
                sysLogService.addLog(SessionUtil.getCurUser(),OperationType.SEND,"彩信",
                        Keys.SMSMGR_SENDMMS,"doSendMms","【"+pack.getBatchName()+"】"); //添加访问日志
                return JsonResp.success("彩信发送成功");
            } else {
                respMsg ="发送失败：" + (StringUtils.isBlank(resp.getMessage()) ? "其它错误" : resp.getMessage());
                sendingMap.put(sessionId, respMsg);
                return JsonResp.fail(resp.getResult().getIndex(),respMsg);
            }
        } catch (Exception e) {
            logger.error("doSend failed: ", e);
            return JsonResp.fail("发送失败：后台处理异常");
        } finally {
            if (suc && mmsInfoVo.isClearSent()) {
                delFiles(fileNames);
            }
            sendingMap.remove(sessionId);
        }
    }

    /**
     * 预先处理彩信内容
     * @param mms
     * @return
     */
    private boolean preHandleMmsContent(MmsContent mms) {
        if (ListUtil.isBlank(mms.getFrames())) {
            return false;
        }
        for (Iterator<MmsContent.Frame> it = mms.getFrames().iterator(); it
                .hasNext();) {
            MmsContent.Frame mf = it.next();
            if (mf.getText().getSize() + mf.getImg().getSize() +
                    mf.getAudio().getSize() +mf.getVideo().getSize() <= 0) {
                it.remove();
                continue;
            }
        }
        if (ListUtil.isBlank(mms.getFrames())) {
            return false;
        }
        return true;
    }

    /**
     * 创建彩信包
     * @param contacts
     * @param fileNames
     * @param mmsContent
     * @param pack
     * @throws Exception
     */
    private void buildFrame(String[] contacts, List<String[]> fileNames, MmsContent mmsContent, PMsgPack pack) throws Exception {
        boolean mass = true;
        for (MmsContent.Frame mf : mmsContent.getFrames()) {
            if (StringUtils.isNotBlank(mf.getText().getContent())) {
                mf.getText().setSubs(DynVarUtil.handleDynVars(mf.getText().getContent(), "[$", "$]"));
                if (mass && DynVarUtil.hasDynVars(mf.getText().getSubs())) {
                    mass = false;
                }
            }
        }
        mmsContent.setSmil(MmsUtil.toSmil(mmsContent));
        List<Contact> cs = new ArrayList<Contact>();
        List<String> rs = new ArrayList<String>();
        if (contacts != null && contacts.length > 0) {
            for (String str : contacts) {
                rs.add(str);
            }
        }
        loadContacts(contactService, config, rs, mass, cs, fileNames);
        MsgFrame frame = null;
        if (mass) {
            pack.setSendTypeIndex(MsgPack.SendType.MASS.getIndex());
            frame = sendMmsService.buildMassFrame(cs, fileNames, mmsContent, pack);
        } else {
            pack.setSendTypeIndex(MsgPack.SendType.GROUP.getIndex());
            frame = sendMmsService.buildGroupFrame(cs, fileNames, mmsContent, pack);
        }
        pack.getFrames().add(frame);
    }

    //region 显示用户余额信息
    @POST
    @Path(Keys.SMSMGR_SENDMMS_INDEX +"/getBalance")
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp getUserBalance(SpecsvsNumVo vo){
        try {
            MmsInfoVo infoVo = baseMsgResource.getBalanceByBizId(vo);
            return JsonResp.success(infoVo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户余额异常");
        }
        return JsonResp.fail();
    }
    //endregion
}

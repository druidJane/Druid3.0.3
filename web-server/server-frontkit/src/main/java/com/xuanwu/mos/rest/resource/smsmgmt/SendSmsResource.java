package com.xuanwu.mos.rest.resource.smsmgmt;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.BaseSendResource;
import com.xuanwu.mos.rest.service.BizTypeService;
import com.xuanwu.mos.rest.service.CapitalAccountService;
import com.xuanwu.mos.rest.service.ContactService;
import com.xuanwu.mos.rest.service.msgservice.PhraseService;
import com.xuanwu.mos.rest.service.smsmgmt.impl.SendSmsService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.FileParams;
import com.xuanwu.mos.vo.MmsInfoVo;
import com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType;
import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgPack;
import com.xuanwu.msggate.common.util.Constants;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by Jiang.Ziyuan on 2017/3/23.
 */
@Component
@Path(Keys.SMSMGR_SENDSMS)
public class SendSmsResource extends BaseSendResource {
    private static final Logger logger = LoggerFactory.getLogger(SendSmsResource.class);
    //变量短信参数解析错误
    private static final int VAR_ERROR = -11;

    protected Map<String, String> sendingMap = new ConcurrentHashMap<String, String>();
    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private FrontKitPackSender packSender;
    @Autowired
    private PhraseService phraseService;
    @Autowired
    private SendSmsService sendSmsService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private BaseMsgResource baseMsgResource;
    @Autowired
    private BizTypeService bizTypeService;
    @Autowired
    private Config config;
    @Autowired
    private FileImporter importer;
    @Autowired
    private CapitalAccountService accountService;

    /**
     * 普通短信，上传通讯录文件,并重命名该文件-
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path(Keys.SMSMGR_SENDSMS_COMMONSMS_SEND+"/upload")
    public JsonResp uploadContacts(@Context HttpServletRequest request) {
        try {
            UploadResult result = importer.upload(BizDataType.SendSms, request);
            StatusCode statusCode = result.getStatusCode();
            if (statusCode != StatusCode.Success) {
                return JsonResp.fail(statusCode.getIndex(), statusCode.getStateDesc());
            }
            return JsonResp.success(new FileParams(result.getOriginFileName(),result.getNewFileName()));
        } catch (Exception e) {
            logger.error("upload sms file failed: ", e);
            return JsonResp.fail("upload sms file failed: " + e.getMessage());
        }
    }
    /**
     * 变量短信，上传通讯录文件,并重命名该文件-
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path(Keys.SMSMGR_SENDSMS_VARIANTSMS_SEND+"/upload")
    public JsonResp uploadVarContacts(@Context HttpServletRequest request) {
        return uploadContacts(request);
    }

    /**普通短信
     * 解析上传的通讯录文件，将除第一行之外的所有行的数据进行解析，--搞定
     * 解析结果：{"data":["13546986214","张治中","1985/12/21"]}
     * 上传文件的导入也在此处解析
     * @return 返回相应的解析结果
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDSMS_COMMONSMS_SEND+"/import")
    public JsonResp parseMmsContacts(@Context HttpServletRequest request, FileParams fileParams) {
        try {
            return importFile(request, config, BizDataType.SendSms, fileParams);
        } catch (BusinessException e){
            return JsonResp.fail(e.getMessage());
        } catch (Exception e) {
            logger.error("import sms file failed: ", e);
            return JsonResp.fail();
        }
    }
    /**变量短信
     * 解析上传的通讯录文件，将除第一行之外的所有行的数据进行解析，--搞定
     * 解析结果：{"data":["13546986214","张治中","1985/12/21"]}
     * 上传文件的导入也在此处解析
     * @return 返回相应的解析结果
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDSMS_VARIANTSMS_SEND+"/import")
    public JsonResp parseVarMmsContacts(@Context HttpServletRequest request, FileParams fileParams) {
        return parseMmsContacts(request,fileParams);
    }

    //普通短信，获取用户相关数据
    @POST
    @Path(Keys.SMSMGR_SENDSMS_COMMONSMS_SEND + "/getUserBalance")
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp getUserBalance(SpecsvsNumVo vo){
        SimpleUser user = SessionUtil.getCurUser();
        MmsInfoVo result = new MmsInfoVo();
        try {
            BigDecimal balance = new BigDecimal(userService.getExsitBindAccountOfUser(user.getId(), user.getEnterpriseId())+"").setScale(4, BigDecimal.ROUND_DOWN);;
            //获取通道价格
            List<SpecsvsNumVo> list = bizTypeService.getCarrierDetailByBizId(user.getEnterpriseId(), vo.getBizTypeId(), vo.getMsgType());
            BigDecimal price = BigDecimal.ZERO;
            for(SpecsvsNumVo item : list){
                if(price.compareTo(item.getPrice()) == -1){
                    price = item.getPrice();
                }
            }
            //计算可发送数量
            BigDecimal restSendNum = BigDecimal.ZERO;
            if(balance.compareTo(BigDecimal.ZERO) == 1){
                restSendNum = balance.divide(price,0,BigDecimal.ROUND_DOWN);
                result.setRestSendNum(restSendNum.doubleValue());
            }
            result.setBalance(balance);

            //获取最新企业，用户签名数据
            Enterprise loginEnt = userService.getLoginEnt(user.getEnterpriseId());
            SimpleUser loginUser = userService.getLoginUser(user.getUsername(), Platform.FRONTKIT);
            result.setEntSigLocation(loginEnt.getSigLocation());
            result.setEntSignature(loginEnt.getSignature());
            result.setUserSigLocation(loginUser.getSigLocation());
            result.setUserSignature(loginUser.getSignature());
            result.setEnableKeywordFilter(loginEnt.isWarningKeyWord());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }

        return JsonResp.success(result);
    }

    /**
     * 变量短信获取用户数据
     * @param vo
     * @return
     */
    @POST
    @Path(Keys.SMSMGR_SENDSMS_VARIANTSMS_SEND + "/getUserBalance")
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp getVarUserBalance(SpecsvsNumVo vo){
        return getUserBalance(vo);
    }
	/**
	 * 发送变量短信
     * @param req
     * @param mmsInfoVo
     * @return
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDSMS_VARIANTSMS_SEND)
    public JsonResp sendVariantSms(@Context HttpServletRequest req, @Valid MmsInfoVo mmsInfoVo) {
        return doSend(req, mmsInfoVo);
    }

    /**
     * 发送普通短信
     * @param req
     * @param mmsInfoVo
     * @return
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDSMS_COMMONSMS_SEND)
    public JsonResp doSend(@Context HttpServletRequest req, @Valid MmsInfoVo mmsInfoVo) {
//        TrustIpStatus ipStatus = baseMsgResource.checkTrustIp(req);
//        if (ipStatus== TrustIpStatus.FAILED) {
//            return JsonResp.fail("发送用户的IP不在企业信任IP范围内");
//        } else if (ipStatus == TrustIpStatus.EXCEPTION) {
//            return JsonResp.fail("无法获取客户端的真实IP，请联系后台管理人员解决");
//        }
        SimpleUser user =  SessionUtil.getCurUser();
        Integer templateId = mmsInfoVo.getTemplateId();
        String sessionId = req.getSession().getId();
        sendingMap.put(sessionId, "请求处理中...");
        String[] contacts = preHandlerConcact(mmsInfoVo);
        List<String[]> fileNames = new ArrayList<String[]>(5);
        boolean suc = false;
        try {
            if (contacts == null) {
                throw new IllegalArgumentException("收信人为空");
            }
            if ("".equals(mmsInfoVo.getMms())) {
                throw new IllegalArgumentException("短信内容为空");
            }
            PMsgPack pack = new PMsgPack();
            if(templateId != null){
                Phrase phrase = phraseService.findMosPhraseById(templateId);
                //判断免审
                if(phrase != null && phrase.getTemplateType() == 1 && phrase.getPhraseType() == 1){
                    pack.getParameters().put(Constants.TEMPLATE_NO,phrase.getIdentify());
                }
            }
            pack.setMsgType(MsgType.LONGSMS);
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
            buildFrame(contacts, null, fileNames, mmsInfoVo.getMms(), pack);

            sendingMap.put(sessionId, "正发往网关...");
            MTResp resp = checkPack(pack);
            if (resp == null) {

                Map<String, String> accountMap = baseMsgResource.covertAccountMap(user, NetUtil.checkUserTrueIp(req));
                resp = packSender.send(accountMap, pack);
            }
            if (resp.getResult() == MTResult.SUCCESS) {
                sysLogService.addLog(SessionUtil.getCurUser(),OperationType.SEND,"短信",
                        Keys.SMSMGR_SENDSMS,"doSendSms","【"+pack.getBatchName()+"】"); //添加访问日志
            } else {
                logger.error("发送网关失败，MsgCode:"+resp.getResult().getIndex() + "," + resp.getResult());
                for(MsgFrame frame:pack.getFrames()){
                    logger.error("短信内容："+frame.getAllMsgSingle().toString());
                }

                return JsonResp.fail(resp.getResult().getIndex(),"发送网关失败，MsgCode:"+resp.getResult().getIndex() + "," + resp.getResult());
            }
            sendingMap.put(sessionId, "发送成功");
            return JsonResp.success();
        } catch (BusinessException e) {
            e.printStackTrace();
            return JsonResp.fail(VAR_ERROR,e.getMessage());
        } catch (Exception e) {
            logger.error("doSend failed: ", e);
            return JsonResp.fail();
        } finally {
            if (suc && mmsInfoVo.isClearSent())
                delFiles(fileNames);
            sendingMap.remove(sessionId);
        }
    }

    private void delFiles(List<String[]> fileNames) {
        try {
            for (String[] fileName : fileNames) {
                FileUtil.delImportedFile(BizDataType.SendSms, fileName[0],
                        Config.getContextPath());
            }
        } catch (Exception e) {
            logger.error("After sent delete files failed: ", e);
        }
    }

    private void buildFrame(String[] contacts, String[] fContacts,
                            List<String[]> fileNames, String content, PMsgPack pack) {
        List<SubContent> subs = DynVarUtil.handleDynVars(content, "[$", "$]");
        //短信内容是否含有变量通配符mass=true为无
        boolean mass = !DynVarUtil.hasDynVars(subs);
        List<String> rs = new ArrayList<String>();
        if (contacts != null && contacts.length > 0) {
            for (String str : contacts) {
                rs.add(str);
            }
        }
        if (fContacts != null && fContacts.length > 0) {
            for (String str : fContacts) {
                rs.add(str);
            }
        }
        List<Contact> cs = new ArrayList<Contact>();
        //获取联系人信息
        loadContacts(contactService, config, rs, mass, cs, fileNames);
        MsgFrame frame = null;
        //MASS群发，普通短信，Group组发，变量短信
        if (mass) {
            pack.setSendTypeIndex(MsgPack.SendType.MASS.getIndex());
            frame = sendSmsService.buildMassFrame(cs, fileNames, content, pack);
        } else {
            pack.setSendTypeIndex(MsgPack.SendType.GROUP.getIndex());
            frame = sendSmsService.buildGroupFrame(cs, fileNames, subs, pack);
        }
        pack.getFrames().add(frame);
    }

    //@Produces({MediaType.APPLICATION_JSON})
    public JsonResp getBizTypes() {
        SimpleUser loginUser = SessionUtil.getCurUser();
        List<BizType> bizs = userService.findBizByMsgType(loginUser.getId(),
                MsgContent.MsgType.LONGSMS.getIndex());
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (ListUtil.isNotBlank(bizs)) {
            HashSet<Integer> set = new HashSet<Integer>();
            int i = 0;
            for (BizType biz : bizs) {
                if (set.contains(biz.getId())) {
                    continue;
                }
                set.add(biz.getId());
                if (i > 0)
                    sb.append(Delimiters.COMMA);
                sb.append(biz.toJSONHtml());
                i++;
            }
        }
        sb.append(']');
        return JsonResp.success(sb.toString());
    }

    /**
     * 普通短信获取发送状态
     * @param req
     * @return
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDSMS_COMMONSMS_SEND+"/getSendingState")
    public JsonResp getSendingState(@Context HttpServletRequest req) {
        String step = sendingMap.get(req.getSession().getId());
        return JsonResp.success(step == null ? "请求提交中..." : step);
    }
    /**
     * 变量短信获取发送状态
     * @param req
     * @return
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path(Keys.SMSMGR_SENDSMS_VARIANTSMS_SEND+"/getSendingState")
    public JsonResp getVarSendingState(@Context HttpServletRequest req) {
        return getSendingState(req);
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSendSmsService(SendSmsService sendSmsService) {
        this.sendSmsService = sendSmsService;
    }

    /*@Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }*/

}

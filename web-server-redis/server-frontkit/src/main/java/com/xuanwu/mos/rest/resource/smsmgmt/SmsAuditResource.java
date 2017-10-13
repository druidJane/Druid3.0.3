package com.xuanwu.mos.rest.resource.smsmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.AuditStateEnum;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.VoUtilResource;
import com.xuanwu.mos.rest.resource.smsmgmt.sendtracking.SmsSendRecordResource;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.rest.service.smsmgmt.VerifyService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.MsgPackVo;
import com.xuanwu.msggate.common.protobuf.CommonItem;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgContent;
import com.xuanwu.msggate.common.sbi.exception.CoreException;
import com.xuanwu.msggate.common.zip.ZipUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 短信审核
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Component
@Path(Keys.SMSMGR_SENDPENDING)
public class SmsAuditResource {
    private static final Logger logger = LoggerFactory.getLogger(SmsAuditResource.class);

    @Autowired
    private MsgPackService msgPackService;
    @Autowired
    private BaseMsgResource baseMsgResource;
    @Autowired
    private FrontKitPackSender packSender;
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private VoUtilResource voUtilResource;
    @Autowired
    private SmsSendRecordResource smsSendRecordResource;

    //查看所有待审核短信
    @POST
    @Path(Keys.SMSMGR_SENDPENDING_LOADWAITBATCHS)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp listAuditSMS(@Valid PageReqt reqt, @Context HttpServletRequest request) {
        SimpleUser user = SessionUtil.getCurUser();
        // 从datascope中获取权限
        DataScope dataScope = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
        QueryParameters params = new QueryParameters(reqt);

        baseMsgResource.checkTableDataScope(params,dataScope);
        if (!ObjectUtil.validOnlyOne(params, "userId", "userIds","dataScope")) {
            return PageResp.success(0, Collections.emptyList());
        }
        params.addParam("msgType", MsgContent.MsgType.LONGSMS.getIndex());
        // 查询
        int packsCount = msgPackService.findMmsWaitAuditPacksCount(params);
        List<MmsMsgPack> msgPackList = new ArrayList<>();
        if (packsCount>0) {
        	
        	PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), packsCount);
            params.setPage(pageInfo);
            msgPackList = msgPackService.findMmsWaitAuditPacks(params);
        }
        List<MsgPackVo> voList = voUtilResource.assembleAuditPackVo(msgPackList);
        return PageResp.success(packsCount, voList);
    }

    //查看审核信息
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_CHECKBATCH + "/preAudit")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp showAuditSmsPack(@Context HttpServletRequest request, @Valid MsgPackVo msgPackVo) {
        if (!baseMsgResource.checkQueryAndAuditRelation(request, msgPackVo.getUserId())) {
            return JsonResp.fail("对不起，您没有审核权限！！！");
        }
        Parameters params = new Parameters();
        QueryParameters query = new QueryParameters();
        query.addParam("packId",msgPackVo.getPackId());
        params.setMsgType(MsgContent.MsgType.LONGSMS.getIndex());
        params.setQueryTime(msgPackVo.getPostTime());
        params.setQuery(query);
        MmsMsgPack verifyPack = msgPackService.findVerifyMsgPackById(params);
        if (verifyPack!=null) {
            byte[] data = new byte[0];
            try {
                data = ZipUtil.unzipByteArray(verifyPack.getFrameContent());
                PMsgContent msgContent;
                String smsContent = "";
                switch (verifyPack.getSendType()) {
                    case MASS:
                        CommonItem.MassPack massPack = CommonItem.MassPack.newBuilder().mergeFrom(data).build();
                        msgContent = new PMsgContent(com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType.LONGSMS, massPack.getContent());
                        smsContent = msgContent.getContent();
                        break;
                    case GROUP:
                        CommonItem.GroupPack groupPack = CommonItem.GroupPack.newBuilder().mergeFrom(data).build();
                        msgContent = new PMsgContent(com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType.LONGSMS, groupPack.getItems(0).getContent());
                        smsContent = msgContent.getContent();
                        break;
                }
                msgPackVo.setSmsContent(smsContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            msgPackVo.setUuid(verifyPack.getUuid());
            return JsonResp.success(msgPackVo);
        }
        return JsonResp.fail("该待审核的数据不存在");
    }

    //审核
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_CHECKBATCH)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp auditSms(@Context HttpServletRequest request, @Valid MsgPackVo msgPackVo) {
        SimpleUser user = SessionUtil.getCurUser();
        QueryParameters query = new QueryParameters();
        query.addParam("packId",msgPackVo.getPackId());
        int state = msgPackVo.getCheckState() ? AuditStateEnum.PASS.getIndex() : AuditStateEnum.NOT_PASS.getIndex();
        query.addParam("state",state);
        String uuid = msgPackVo.getUuid();
        String packId = msgPackVo.getPackId();
        Date postTime = msgPackVo.getPostTime();

        try {
            // 向网关发送请求
            Map<String, String> accountMap = baseMsgResource.covertAccountMap(user, NetUtil.checkUserTrueIp(request));
            MTResp resp = packSender.auditing(accountMap, packId, uuid, state, MsgTypeEnum.SMS.getIndex(), postTime);
            if (resp.getResult() == MTResult.SUCCESS) {

                sysLogService.addLog(user, OperationType.AUDIT,"短信","Public","Login","【" + msgPackVo.getBatchName() + "】"); //添加访问日志
                Verify verify = new Verify(packId, new Date(), user.getId(), state, msgPackVo.getAuditRemark());
                verifyService.storeVerify(verify);
                return JsonResp.success("审核成功");
            } else {
                if (StringUtils.isBlank(resp.getMessage())) {
                    return JsonResp.fail("该批次已审批完毕，不可重复审批。");
                } else {
                    return JsonResp.fail(resp.getMessage());
                }
            }
        } catch (CoreException e) {
//            logger.error("Do audit fail, cause by: ", e);
        }catch (RepositoryException e) {
//            logger.error("Do audit fail, cause by: ", e);
        }
        return JsonResp.fail("审核异常");
    }

    // 检核批次中全部的号码的详情
    @POST
    @Path(Keys.SMSMGR_SENDPENDING_INDEX+"/checkAllPack")
    @Produces({MediaType.APPLICATION_JSON})
    public PageResp checkPackDetail(@Valid PageReqt reqt) {
        QueryParameters query = new QueryParameters(reqt);
        Date postTime = DateUtil.tranDate((long) query.getParams().get("postTime"));
        Parameters params = new Parameters(query,MsgTypeEnum.SMS.getIndex(),postTime);
        return baseMsgResource.checkPackDetail(params,reqt);
    }

    //region 检核详情 -- 过滤号码
    @POST
    @Path(Keys.SMSMGR_SENDPENDING_INDEX+"/checkFilterPack")
    @Produces({MediaType.APPLICATION_JSON})
    public PageResp checkFilterPackTicket(@Valid PageReqt reqt) {
        QueryParameters query = new QueryParameters(reqt);
        query.addParam("filter",0);
        Date postTime = DateUtil.tranDate((long) query.getParams().get("postTime"));
        Parameters params = new Parameters(query,MsgTypeEnum.SMS.getIndex(),postTime);
        return baseMsgResource.checkPackDetail(params,reqt);
    }

    // 显示批次详情
    @POST
    @Path(Keys.SMSMGR_SENDPENDING_INDEX+"/showPack")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp batchDetailMsgPack(@Valid MsgPackVo msgPackVo) {
        return smsSendRecordResource.batchDetailMsgPack(msgPackVo);
    }

    @GET
    @Path(Keys.SMSMGR_SENDPENDING_LOADWAITBATCHS + "/fetchDeptData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteDepartment(@QueryParam("deptName") String deptName, @Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteDepartment(request,null, deptName);
    }
    @GET
    @Path(Keys.SMSMGR_SENDPENDING_LOADWAITBATCHS + "/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteUserName(@QueryParam("userName") String userName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteUser(request,null, userName);
    }
}

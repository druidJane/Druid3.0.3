package com.xuanwu.mos.rest.resource.mmsmgmt;

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
import com.xuanwu.mos.rest.resource.mmsmgmt.sendtracking.MmsSendRecordResource;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.rest.service.smsmgmt.VerifyService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.NetUtil;
import com.xuanwu.mos.utils.ObjectUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.vo.MsgPackVo;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

import org.apache.commons.lang.StringUtils;
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

/** 彩信管理 --> 彩信审核
 * Created by 郭垚辉 on 2017/4/13.
 */
@Component
@Path(Keys.SMSMGR_SENDPENDINGMMS)
public class MmsAuditResource extends BaseMmsResource {

    @Autowired
    private MsgPackService msgPackService;
    @Autowired
    private FrontKitPackSender packSender;
    @Autowired
    private BaseMsgResource baseMsgResource;
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private VoUtilResource voUtilResource;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private MmsSendRecordResource mmsSendRecordResource;

    //region 查看所有待审核彩信
    @POST
    @Path(Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp listAuditMms(@Valid PageReqt reqt, @Context HttpServletRequest request) {
        // 从dataScope中获取权限
        Enterprise curEnterprise = SessionUtil.getCurEnterprise();
        Integer enterpriseId = curEnterprise.getId();
        DataScope dataScope = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
        QueryParameters params = new QueryParameters(reqt);
        params = baseMsgResource.checkTableDataScope(params,dataScope);
        if (!ObjectUtil.validOnlyOne(params, "userId", "userIds","dataScope")) {
            return PageResp.success(0, Collections.emptyList());
        }
        params.addParam("msgType",MsgTypeEnum.MMS.getIndex());
        params.addParam("enterpriseId",enterpriseId);
        // 查询
        baseMsgResource.formatDate(params);
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
    //endregion

    //region 查看审核信息
    @POST
    @Path(Keys.SMSMGR_SENDPENDINGMMS_CHECKBATCH+"/preAudit")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp showAuditMmsPack(@Context HttpServletRequest request, @Valid MsgPackVo msgPackVo) {
        if (!baseMsgResource.checkQueryAndAuditRelation(request, msgPackVo.getUserId())) {
            return JsonResp.fail("对不起，您没有审核权限！！！");
        }
        Parameters params = new Parameters();
        QueryParameters query = new QueryParameters();
        query.addParam("packId",msgPackVo.getPackId());
        query.addParam("enterpriseId", SessionUtil.getCurUser().getEnterpriseId());
        params.setMsgType(MsgTypeEnum.MMS.getIndex());
        params.setQueryTime(msgPackVo.getPostTime());
        params.setQuery(query);
        MmsMsgPack verifyPack = msgPackService.findVerifyMsgPackById(params);
        if (verifyPack!=null) {
            msgPackVo.setMmsTitle(verifyPack.getMmsTitle());
            msgPackVo.setUuid(verifyPack.getUuid());
            return JsonResp.success(msgPackVo);
        }
        return JsonResp.fail("该待审核的数据不存在");
    }
    //endregion

    //region 审核
    @POST
    @Path(Keys.SMSMGR_SENDPENDINGMMS_CHECKBATCH)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp auditMms(@Context HttpServletRequest request,@Valid MsgPackVo msgPackVo) {
        SimpleUser user = SessionUtil.getCurUser();
        QueryParameters query = new QueryParameters();
        query.addParam("packId",msgPackVo.getPackId());
        int state = msgPackVo.getCheckState() ? AuditStateEnum.PASS.getIndex() : AuditStateEnum.NOT_PASS.getIndex();
        query.addParam("state",state);
        String uuid = msgPackVo.getUuid();
        String packId = msgPackVo.getPackId();
        Date postTime = msgPackVo.getPostTime();
        // 权限认证

        // 再向网关发送请求
        try {
            Map<String, String> accountMap = baseMsgResource.covertAccountMap(user, NetUtil.checkUserTrueIp(request));
            MTResp resp = packSender.auditing(accountMap, packId, uuid, state, MsgTypeEnum.MMS.getIndex(), postTime);
            if (resp.getResult() == MTResult.SUCCESS) {
                sysLogService.addLog(user, OperationType.AUDIT, "彩信", "Public", "Login", "【" + msgPackVo.getBatchName() + "】"); //添加访问日志
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
        } catch (RepositoryException e) {
//            logger.error("Do audit fail, cause by: ", e);
        }
        return JsonResp.fail("审核异常");
    }
    //endregion

    //region 查看当前的彩信的内容
    @POST
    @Path(Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS+"/showMms")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp showMms(@Valid MsgPackVo msgPackVo){
        return mmsPreviewByPackId(msgPackVo.getPackId(),msgPackVo.getPostTime());
    }
    //endregion

    //region 显示批次详情
    @POST
    @Path(Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS+"/showPack")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp batchDetailMsgPack(@Valid MsgPackVo msgPackVo) {
        return mmsSendRecordResource.batchDetailMsgPack(msgPackVo);
    }

    //region 检核详情
    @POST
    @Path(Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS+"/checkAllPack")
    @Produces({MediaType.APPLICATION_JSON})
    public PageResp checkPackDetail(@Valid PageReqt reqt) {
        QueryParameters query = new QueryParameters(reqt);
        Date postTime = DateUtil.tranDate((long) query.getParams().get("postTime"));
        Parameters params = new Parameters(query,MsgTypeEnum.MMS.getIndex(),postTime);
        return baseMsgResource.checkPackDetail(params,reqt);
    }

    //region 检核详情 -- 过滤号码
    @POST
    @Path(Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS+"/checkFilterPack")
    @Produces({MediaType.APPLICATION_JSON})
    public PageResp checkFilterPackTicket(@Valid PageReqt reqt) {
        QueryParameters query = new QueryParameters(reqt);
        query.addParam("filter",0);
        Date postTime = DateUtil.tranDate((long) query.getParams().get("postTime"));
        Parameters params = new Parameters(query,MsgTypeEnum.MMS.getIndex(),postTime);
        return baseMsgResource.checkPackDetail(params,reqt);
    }
    //endregion

    //region 自动补全相关
    // 补全用户
    @GET
    @Path(Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS + "/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteUserName(@QueryParam("userName") String userName,@Context HttpServletRequest request) {
        // 借用该resource的dataScope作为该自动补全的dataScope。否则该方法的dataScope的大小为NONE
        return baseMsgResource.autoCompleteUser(request,null, userName);
    }

    // 补全部门
    @GET
    @Path(Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS + "/fetchDeptData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteDeptName(@QueryParam("deptName") String deptName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteDepartment(request, null,deptName);
    }
    // endregion
}

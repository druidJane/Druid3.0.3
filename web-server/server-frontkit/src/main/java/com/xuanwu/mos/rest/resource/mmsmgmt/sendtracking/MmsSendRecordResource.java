package com.xuanwu.mos.rest.resource.mmsmgmt.sendtracking;

import com.alibaba.fastjson.JSONObject;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.Enterprise;
import com.xuanwu.mos.domain.entity.MmsMsgPack;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.UserTask;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.MosBizDataType;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.VoUtilResource;
import com.xuanwu.mos.rest.resource.mmsmgmt.BaseMmsResource;
import com.xuanwu.mos.rest.service.UserTaskService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.ObjectUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.VoUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.vo.MsgPackVo;
import com.xuanwu.mos.vo.UserTaskParameter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
 * 彩信管理 --> 彩信记录 --> 发送记录
 * 批次发送详情跟踪、查询、查看的功能
 * Created by 郭垚辉 on 2017/3/28.
 */
@Component
@Path(Keys.SMSMGR_SENDTRACKINGMMS)
public class MmsSendRecordResource extends BaseMmsResource {


    @Autowired
    private BaseMsgResource baseMsgResource;
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private VoUtilResource voUtilResource;

    private final static Logger logger = LoggerFactory.getLogger(MmsSendRecordResource.class);

    //region 查询发送记录
    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADBATCHS)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp index(@Context HttpServletRequest request, @Valid PageReqt reqt) {
        Parameters params = new Parameters(MsgTypeEnum.MMS.getIndex());
        Integer entId = SessionUtil.getCurUser().getEnterpriseId();

        QueryParameters query = new QueryParameters(reqt);
        DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);

        // 将query中的时间先取出来
        String beginTime = (String) query.getParams().get("beginTime");
        String endTime = (String) query.getParams().get("endTime");

        baseMsgResource.checkTableDataScope(query,ds);
        if (!ObjectUtil.validOnlyOne(query, "userId", "userIds","dataScope")) {
            return PageResp.success(0, Collections.emptyList());
        }
        query.addParam("msgType", MsgContent.MsgType.MMS.getIndex());
        query.addParam("entId", entId);
        query.addParam("deptId",null);
        query.addParam("enterpriseId",SessionUtil.getCurUser().getEnterpriseId());

        // 最后一位放总数，之前每一位对应每一天的统计数
        /*
        int[] totals = baseMsgResource.findMmsMsgPacksCountMultiDays(query, params);
        int total = totals[totals.length - 1];
        PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), total);
        */

        int[] totals = new int[2];
        PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount());
        int offset = pageInfo.getFrom();
        int requireCount = pageInfo.getSize();
        query.setPage(pageInfo);
        query.addParam("beginTime", beginTime);
        query.addParam("endTime", endTime);

        List<MmsMsgPack> msgPackList = baseMsgResource.findMmsMsgPacksMultiDays(query, params, offset, requireCount, totals);
        List<MsgPackVo>  resultMsgPackVo = voUtilResource.assembleSendRecordVo(msgPackList);
        int total = baseMsgResource.calculateTotal(resultMsgPackVo,reqt);
        return PageResp.success(total, resultMsgPackVo);
    }

    //endregion

    //region 获取当前批次的数据统计显示
    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADBATCHS+"/showPack")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp batchDetailMsgPack(@Valid MsgPackVo msgPackVo) {
        msgPackVo.setMsgType(MsgTypeEnum.MMS.getIndex());
        MmsMsgPack mmsMsgPack = baseMsgResource.statBatchPhoneDetail(msgPackVo.getPackId(), MsgContent.MsgType.MMS, true, msgPackVo.getPostTime());
        logger.debug("mmsPack id is  {}, info is {}", mmsMsgPack.getPackId(), mmsMsgPack.toString());
        baseMsgResource.findAuditRecord(mmsMsgPack,msgPackVo.getPackId());
        assembleCount2MsgPackVo(msgPackVo,mmsMsgPack);
        logger.debug("assembleCount is error");
        return JsonResp.success(msgPackVo);
    }

    //region 查看当前的短信包
    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADBATCHS+"/showMms")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp showMms(@Valid MsgPackVo msgPackVo){
        return mmsPreviewByPackId(msgPackVo.getPackId(),msgPackVo.getPostTime());
    }
    //endregion

    //region 组装审核信息和统计数据
    private MsgPackVo assembleCount2MsgPackVo (MsgPackVo msgPackVo,MmsMsgPack mmsMsgPack){
        VoUtil.assembleCheckMsgInfoVo(msgPackVo, mmsMsgPack);
        QueryParameters query = new QueryParameters();
        query.addParam("msgType",MsgTypeEnum.MMS.getIndex());
        query.addParam("packId",msgPackVo.getPackId());
        Parameters params = new Parameters(query);
        params.setMsgType(MsgTypeEnum.MMS.getIndex());
        params.setQueryTime(msgPackVo.getPostTime());
        String mmsTitle = baseMsgResource.getMmsContent(params);
        msgPackVo.setMmsTitle(mmsTitle);
        return msgPackVo;
    }
    //endregion

    //endregion

    //region 取消发送当前批次

    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_CANCLEBATCH)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp cancelSendMms(@Context HttpServletRequest request, @Valid MsgPackVo msgPackVo) {
        QueryParameters query = new QueryParameters();
        query.addParam("packId",msgPackVo.getPackId());
        Parameters params = new Parameters(query);
        params.setQueryTime(msgPackVo.getPostTime());
        params.setMsgType(MsgTypeEnum.MMS.getIndex());
        return baseMsgResource.cancelSendMms(request,params);
    }
    //endregion

    //region 导出选中的批次信息
    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_EXPORTBATCH)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp export(@Context HttpServletRequest request,@Valid PageReqt req){
        DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
        try {
            userTaskService.addExportTask(MosBizDataType.Front_BatchMms_Exp,convertExportData(req));

            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser, OperationType.EXPORT,"【发送记录】","Public","Login",""); //添加访问日志

        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }

    private UserTask convertExportData(PageReqt req) {
        SimpleUser user = SessionUtil.getCurUser();
        Enterprise ent = SessionUtil.getCurEnterprise();

        Integer enterpriseId = user.getEnterpriseId();
        List<UserTaskParameter> list = new ArrayList<>();
        list.add(new UserTaskParameter("exprottype", (String) req.getParams().get("exprottype")));
        list.add(new UserTaskParameter("exportcontent","0"));//前台只有批次导出，hardcode0
        list.add(new UserTaskParameter("starttime",(String) req.getParams().get("starttime")));
        list.add(new UserTaskParameter("enddate",(String) req.getParams().get("enddate")));
        list.add(new UserTaskParameter("senduser",(String) req.getParams().get("senduser")));
        list.add(new UserTaskParameter("batchname",(String) req.getParams().get("batchname")));
        list.add(new UserTaskParameter("batchstate",req.getParams().get("batchstate")+""));
        list.add(new UserTaskParameter("billing_type",String.valueOf(ent.getBillingType())));
        list.add(new UserTaskParameter("_enterprise_id",String.valueOf(enterpriseId)));
        list.add(new UserTaskParameter("_path",user.getPath()));
        DataScope _DataScope = SessionUtil.getDataSope("/MmsMgr/SendTracking/clicksmsexportweb");
        DataScope _IsCancel = SessionUtil.getDataSope("/MmsMgr/SendTracking/CancleBatch");
        list.add(new UserTaskParameter("_DataScope",_DataScope.name()));
        list.add(new UserTaskParameter("_IsCancel",_IsCancel.name()));
        String parameterStr = JSONObject.toJSON(list).toString();
        UserTask userTask = new UserTask();
        String taskName = (String)req.getParams().get("name");
        if (StringUtils.isNotBlank(taskName)) {
            userTask.setTaskName(taskName);
        }
        userTask.setMosParameters(parameterStr);
        userTask.setCreateUser(user.getId());
        String fileName = (String) req.getParams().get("fileName");
        userTask.setFileType(fileName.substring(fileName.lastIndexOf("."),fileName.length()));
        userTask.setFileSeparator((String) req.getParams().get("delimiter"));
        return userTask;
    }
    //endregion


    //region 检核详情
    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADBATCHS+"/checkAllPack")
    @Produces({MediaType.APPLICATION_JSON})
    public PageResp checkPackDetail(@Valid PageReqt reqt) {
        QueryParameters query = new QueryParameters(reqt);
        Date postTime = DateUtil.tranDate((long) query.getParams().get("postTime"));
        Parameters params = new Parameters(query,MsgTypeEnum.MMS.getIndex(),postTime);
        return baseMsgResource.checkPackDetail(params,reqt);
    }

    //region 检核详情 -- 过滤号码
    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADBATCHS+"/checkFilterPack")
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
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADBATCHS + "/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteUserName(@QueryParam("userName") String userName,@Context HttpServletRequest request) {
        // 借用该resource的dataScope作为该自动补全的dataScope。否则该方法的dataScope的大小为NONE
        return baseMsgResource.autoCompleteUser(request,null, userName);
    }

    // 补全部门
    @GET
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADBATCHS + "/fetchDeptData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteDeptName(@QueryParam("deptName") String deptName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteDepartment(request,null, deptName);
    }
    // endregion
}

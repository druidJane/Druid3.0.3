package com.xuanwu.mos.rest.resource.smsmgmt.sendtracking;

import com.alibaba.fastjson.JSONObject;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.MosBizDataType;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.VoUtilResource;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.rest.service.UserTaskService;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.rest.service.msgservice.MsgTicketService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.MsgPackVo;
import com.xuanwu.mos.vo.UserTaskParameter;
import com.xuanwu.msggate.common.protobuf.CommonItem;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.MassMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgContent;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgPack;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgSingle;
import com.xuanwu.msggate.common.sbi.exception.CoreException;
import com.xuanwu.msggate.common.zip.ZipUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 短信管理 --> 发送记录
 * @author <a href="mailto:jiangziyuan@wxchina.com">Ziyuan.Jiang</a>
 * @version 1.0.0
 * @date 2017/5/23.
 */
@Component
@Path(Keys.SMSMGR_SENDTRACKING )
public class SmsSendRecordResource {
    private Logger logger = LoggerFactory.getLogger(SmsSendRecordResource.class);
    @Autowired
    private FrontKitPackSender packSender;
    @Autowired
    private UserMgrService userMgrService;
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private BaseMsgResource baseMsgResource;
    @Autowired
    private MsgTicketService msgTicketService;
    @Autowired
    private VoUtilResource voUtilResource;
    @Autowired 
    private MsgPackService msgPackService;
    
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_LOADBATCHS)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp index(@Context HttpServletRequest request, @Valid PageReqt reqt) {
        SimpleUser user = SessionUtil.getCurUser();
        Parameters params = new Parameters(MsgContent.MsgType.LONGSMS.getIndex());
        Integer entId = user.getEnterpriseId();
        QueryParameters query = new QueryParameters(reqt);
        DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
//        userMgrService.convertQueryByDataScope(query,ds,user);
        //根据前端页面输入的部门名称获取用户Id
//        userMgrService.convertQueryByDept(query,reqt,user);
        baseMsgResource.checkTableDataScope(query,ds);
        // 将query中的时间先取出来
        String beginTime = (String) query.getParams().get("beginTime");
        String endTime = (String) query.getParams().get("endTime");
        query.addParam("msgType", MsgContent.MsgType.LONGSMS.getIndex());
        query.addParam("entId", entId);

        // 最后一位放总数，之前每一位对应每一天的统计数
        //findMmsMsgPacksCountMultiDays这个方法不限于mms使用，只要传的msg_type参数是sms，那查出来的值就是sms的了。
        if (!ObjectUtil.validOnlyOne(query, "userId", "userIds","dataScope")) {
            return PageResp.success(0, Collections.emptyList());
        }
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
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_CANCLEBATCH)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp cancelSendMms(@Context HttpServletRequest request,@Valid MsgPackVo msgPackVo) {
        QueryParameters query = new QueryParameters();
        query.addParam("packId",msgPackVo.getPackId());
        Parameters params = new Parameters(query);
        params.setQueryTime(msgPackVo.getPostTime());
        params.setMsgType(MsgTypeEnum.SMS.getIndex());
        return baseMsgResource.cancelSendMms(request, params);
    }
    @GET
    @Path(Keys.SMSMGR_SENDTRACKING_LOADBATCHS + "/fetchDeptData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteDepartment(@QueryParam("deptName") String deptName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteDepartment(request,null,deptName);
    }
    @GET
    @Path(Keys.SMSMGR_SENDTRACKING_LOADBATCHS + "/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteUserName(@QueryParam("userName") String userName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteUser(request,null,userName);
    }
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_RESEND)
    public JsonResp reSend(@Context HttpServletRequest request, List<MsgPackVo> voList){
        SimpleUser user = SessionUtil.getCurUser();
        long begin = System.currentTimeMillis();
        com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType msgType = com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType.LONGSMS;
        //批次数
        Integer batchCount = 0 ;
        //总重发数
        Integer ticketCount = 0 ;
        //每个批次重发数
        Integer ticketPerPack = 0;
        Map<String ,Object> result = new HashMap<>();
        List<Map<String ,Object>> batchList = new ArrayList<>();
        StringBuilder batchNameBuilder = new StringBuilder();

        //遍历pack
        for(MsgPackVo vo:voList){
            //ticketIds用于去重，pack_id frame_id parent_id 组合唯一
            Map<String,MsgTicket> ticketIds = new HashMap<>();
        	ticketPerPack = 0 ;
            PMsgPack pack = new PMsgPack();
            MassMsgFrame frame = new MassMsgFrame();
            final List<MsgSingle> msgs = frame.getAllMsgSingle();
            PMsgContent msgContent = new PMsgContent();
            //根据packid 获取状态报告失败的ticket
            Map<String ,Object> batch = new HashMap<>();
            Integer sendCount = 0;
            String batchName = "";
            QueryParameters query = new QueryParameters();
            query.addParam("packId",vo.getPackId());
            query.addParam("sysReject",true);
            Parameters params = new Parameters(query);
            params.setQueryTime(vo.getPostTime());
            params.setMsgType(MsgTypeEnum.SMS.getIndex());
            List<MsgTicket> list = msgTicketService.findFailedTicketByPackId(params);
            for(MsgTicket msgTicket:list){
                batchName = msgTicket.getBatchName()+"【重发】";
                String content = "";
                //分段发送失败，获取父长短信
                if(msgTicket.getParentId()!=0){
                    List<MsgTicket> parent = msgTicketService.findParentMsgTicketsById(msgTicket.getId(), MsgContent.MsgType.LONGSMS.getIndex(), vo.getPostTime());
                    if(parent == null || parent.size()==0){
                        continue;
                    }
                    MsgTicket ticket = parent.get(0);
                    //长短信唯一标示，以父长短信ticket_id.frame_id拼接
                    StringBuffer sb = new StringBuffer();
                    sb.append(ticket.getId()).append(Delimiters.DOT).append(ticket.getFrameId());
                    if(ticketIds.containsKey(sb.toString())){
                        continue;
                    }
                    ticketIds.put(sb.toString(),ticket);
                    msgTicket.setSmsContentSkipSign(ticket.getSmsContentSkipSign());
                }
                //群发,普通短信，拿frame短信内容
                if(msgTicket.getSendType() == 0){
                    try {
                        byte[] bytes = ZipUtil.unzipByteArray(msgTicket.getFrameContent());
                        CommonItem.MassPack massPack = CommonItem.MassPack.parseFrom(bytes);
                        content = massPack.getContent().getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    msgContent.setContent(content);
                    frame.setContent(msgContent);
                }else{
                    content = msgTicket.getSmsContentSkipSign();
                    msgContent.setContent(content);
                }
                pack.setDistinct(vo.getDistinct());
                pack.setUserID(user.getId());
                pack.setSendTypeIndex(msgTicket.getSendType());
                msgs.add(new PMsgSingle(msgType, msgTicket.getPhone(), msgContent,
                        null, null, false, 0));
                ticketCount++;
                ticketPerPack++;
            }
            frame.setBizType(pack.getBizType());
            frame.setMsgType(pack.getMsgType());
            frame.setReportState(true);
            pack.getFrames().add(frame);
            pack.setMsgType(msgType);
            pack.setBatchName(batchName);
            pack.setBizType(Integer.valueOf(vo.getBizTypeId()));
            MTResp resp = baseMsgResource.checkPack(pack);
            if (resp == null) {
                try {
                    Map<String, String> accountMap = baseMsgResource.covertAccountMap(user, NetUtil.checkUserTrueIp(request));
                    resp = packSender.send(accountMap, pack);
                    if(resp.getResult() == MTResult.SUCCESS){
                        sendCount++;
                        batch.put("result","成功");
                    }else{
                        logger.error("发送失败："+resp.getResult());
                        batch.put("result","失败："+resp.getResult());
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                    logger.error("发送失败："+resp.getResult());
                    batch.put("result","内部异常");
                }
            }

            batchNameBuilder.append(batchName).append(Delimiters.COMMA);
            batch.put("batchName",batchName);
            batch.put("sendCount",sendCount);
            batch.put("ticketCount",ticketCount);
            batch.put("ticketPerPack",ticketPerPack);
            batchCount++;
            batchList.add(batch);
        }
        batchNameBuilder.deleteCharAt(batchNameBuilder.length() - 1);
        sysLogService.addLog(user,OperationType.RESEND,"短信","Public","Login", getLogContent(batchNameBuilder)); //添加访问日志
        long end = System.currentTimeMillis();
        result.put("batchCount",batchCount);
        result.put("ticketCount",ticketCount);
        result.put("time",end - begin);
        result.put("user",user.getUsername());
        result.put("batchList",batchList);
        return JsonResp.success(result);
    }

    private static String getLogContent(StringBuilder builder) {
        String[] strs = builder.toString().split(Delimiters.COMMA);
        if (strs.length == 1 || strs.length == 2) {
            return "【" + builder.toString() + "】";
        }
        if (strs.length > 2) {
            return "【" + strs[0] + Delimiters.COMMA + strs[1] + "......】";
        }
        return "";
    }

    private List<MsgTicket> findFailTicket(MsgPackVo vo) {
         Parameters params = new Parameters(MsgContent.MsgType.LONGSMS.getIndex());
        SimpleUser curUser = SessionUtil.getCurUser();
        Integer entId = curUser.getEnterpriseId();

        QueryParameters query = new QueryParameters();
        query.addParam("packId", vo.getPackId());
        query.addParam("enterpriseId",curUser.getEnterpriseId());
        // 将query中的时间先取出来
        /*String beginTime = (String) query.getParams().get("beginTime");
        String endTime = (String) query.getParams().get("endTime");*/
        //DateUtil.format(new Date((Long) query.getParams().get("beginTime")));
        query.addParam("msgType", MsgContent.MsgType.LONGSMS.getIndex());
        query.addParam("entId", entId);
        // 彩信没有短信那种分段(短信70字一段)
        query.addParam("msgSub",-1);

        // 最后一位放总数，之前每一位对应每一天的统计数
        query.addParam("beginTime", DateUtil.format(vo.getPostTime()));
        query.addParam("endTime", DateUtil.format(vo.getPostTime()));
        int[] totals = baseMsgResource.findAllMmsNumberRecordCountMultiDays(query, params);
        int total = totals[totals.length - 1];
        PageInfo pageInfo = new PageInfo(1, total, total);
        query.setPage(pageInfo);
        List<MsgTicket> msgTickets = baseMsgResource.findAllMmsNumberRecordMultiDays(query, params, 0, total, totals);
        List<MsgTicket> result = new ArrayList<>();
        for(MsgTicket ticket : msgTickets){
            if(ticket.getStateReportResult()>1 && ticket.getParentId() == 0){
                result.add(ticket);
            }
        }
        return result;
    }

    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_INDEX + "/reSendList")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp reSendList(@Context HttpServletRequest request,@Valid PageReqt reqt) {
        Parameters params = new Parameters(MsgContent.MsgType.LONGSMS.getIndex());
 
        SimpleUser user = SessionUtil.getCurUser();
        Integer entId = user.getEnterpriseId();
        QueryParameters query = new QueryParameters(reqt);
        DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
        userMgrService.convertQueryByDataScope(query,ds,user);
        // 将query中的时间先取出来
        String beginTime = (String) query.getParams().get("beginTime");
        String endTime = (String) query.getParams().get("endTime");
        query.addParam("msgType", MsgContent.MsgType.LONGSMS.getIndex());
        query.addParam("entId", entId);
        // 最后一位放总数，之前每一位对应每一天的统计数
        //findMmsMsgPacksCountMultiDays这个方法不限于mms使用，只要传的msg_type参数是sms，那查出来的值就是sms的了。
        int[] totals = baseMsgResource.findMmsMsgPacksCountMultiDays(query, params);
        int total = totals[totals.length - 1];
        PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), total);
        int offset = pageInfo.getFrom();
        int requireCount = pageInfo.getSize();
        query.setPage(pageInfo);
        query.addParam("beginTime", beginTime);
        query.addParam("endTime", endTime);
        query.addParam("msgType",MsgContent.MsgType.LONGSMS.getIndex());

        List<MmsMsgPack> msgPackList = baseMsgResource.findMmsMsgPacksMultiDays(query, params, offset, requireCount, totals);
        for(MmsMsgPack msgPack : msgPackList){
            MsgTicket ticket = baseMsgResource.getTicketByPackId(msgPack.getPackId(), MsgContent.MsgType.LONGSMS.getIndex(), msgPack.getPostTime());
            QueryParameters querynew = new QueryParameters();
            querynew.addParam("packId", msgPack.getPackId());
            Parameters paramsnew = new Parameters(querynew, msgPack.getMsgType().getIndex(), msgPack.getPostTime());
            msgPack.setFailedTickets(msgPackService.findFailCount(paramsnew).getFailedTickets());
            msgPack.setSmsContent(ticket.getSmsContent());
        }
        return PageResp.success(total, msgPackList);
    }
    // 检核批次中全部的号码的详情
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_INDEX+"/checkAllPack")
    @Produces({MediaType.APPLICATION_JSON})
    public PageResp checkPackDetail(@Valid PageReqt reqt) {
        QueryParameters query = new QueryParameters(reqt);
        Date postTime = DateUtil.tranDate((long) query.getParams().get("postTime"));
        Parameters params = new Parameters(query,MsgTypeEnum.SMS.getIndex(),postTime);
        return baseMsgResource.checkPackDetail(params,reqt);
    }

    //region 检核详情 -- 过滤号码
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_INDEX+"/checkFilterPack")
    @Produces({MediaType.APPLICATION_JSON})
    public PageResp checkFilterPackTicket(@Valid PageReqt reqt) {
        QueryParameters query = new QueryParameters(reqt);
        query.addParam("filter",0);
        Date postTime = DateUtil.tranDate((long) query.getParams().get("postTime"));
        Parameters params = new Parameters(query,MsgTypeEnum.SMS.getIndex(),postTime);
        return baseMsgResource.checkPackDetail(params,reqt);
    }
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_INDEX+"/showPack")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp batchDetailMsgPack(@Valid MsgPackVo msgPackVo) {
        try {
            msgPackVo.setMsgType(MsgContent.MsgType.LONGSMS.getIndex());
            MmsMsgPack mmsMsgPack = baseMsgResource.statBatchPhoneDetail(msgPackVo.getPackId(), MsgContent.MsgType.LONGSMS, true, msgPackVo.getPostTime());
            baseMsgResource.findAuditRecord(mmsMsgPack,msgPackVo.getPackId());
            VoUtil.assembleCheckMsgInfoVo(msgPackVo, mmsMsgPack);
            MsgTicket ticket = baseMsgResource.getTicketByPackId(msgPackVo.getPackId(), MsgContent.MsgType.LONGSMS.getIndex(), msgPackVo.getPostTime());
            msgPackVo.setSmsContent(ticket.getSmsContent());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success(msgPackVo);
    }
    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_CLICKSMSEXPORTWEB)
    public JsonResp export(@Context HttpServletRequest request,@Valid PageReqt req){
        DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
        try {
            userTaskService.addExportTask(MosBizDataType.Front_BatchSms_Exp,convertExportData(req));

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
        DataScope _DataScope = SessionUtil.getDataSope("/SmsMgr/SendTracking/clicksmsexportweb");
        DataScope _IsCancel = SessionUtil.getDataSope("/SmsMgr/SendTracking/CancleBatch");
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
}

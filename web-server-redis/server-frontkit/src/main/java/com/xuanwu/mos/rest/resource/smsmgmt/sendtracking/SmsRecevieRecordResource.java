package com.xuanwu.mos.rest.resource.smsmgmt.sendtracking;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.entity.MTResp;
import com.xuanwu.mos.domain.entity.MTResult;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.TaskState;
import com.xuanwu.mos.domain.enums.TaskType;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.BaseSendResource;
import com.xuanwu.mos.rest.service.BizTypeService;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.rest.service.msgservice.MoTicketService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.ObjectUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.utils.XmlUtil;
import com.xuanwu.mos.vo.MmsInfoVo;
import com.xuanwu.mos.vo.MoReplyVo;
import com.xuanwu.mos.vo.MoTicketVo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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


@Component
@Path(Keys.SMSMGR_INBOX)
public class SmsRecevieRecordResource extends BaseSendResource {
    private static final Logger logger = LoggerFactory.getLogger(SmsRecevieRecordResource.class);
    @Autowired
    private MoTicketService moTicketService;
    @Autowired
    private BizTypeService bizTypeService;
    @Autowired
    private FileTaskExecutor executor;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private Config config;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private UserMgrService userMgrService;
    @Autowired
    private BaseMsgResource baseMsgResource;

    @POST
    @Path(Keys.SMSMGR_INBOX_GETSMSLIST)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp index(@Context HttpServletRequest request, @Valid PageReqt reqt) {
        int total = 0;
        List<MoTicketVo> resultMoTicketVo = new ArrayList<MoTicketVo>();
        try {
            MoTicketVo moTicketVo = new MoTicketVo();
            QueryParameters query = new QueryParameters(reqt);
            DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);
            query.addParam("msgType", MsgContent.MsgType.LONGSMS.getIndex());

            SimpleUser user = SessionUtil.getCurUser();
            query.addParam("enterpriseId",user.getEnterpriseId());

            baseMsgResource.checkTableDataScope(query,ds);
            if (!ObjectUtil.validOnlyOne(query, "userId", "userIds","dataScope")) {
                return PageResp.success(0, Collections.emptyList());
            }

            total = moTicketService.findMoTicketsCount(query);
            if (total == 0) {
                return PageResp.emptyResult();
            }
            PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), total);
            query.setPage(pageInfo);
            resultMoTicketVo = moTicketService.findMoTickets(query);
        } catch (Exception e) {
            logger.error("moTicket list failed:" + e);
            e.printStackTrace();
            return PageResp.fail();
        }
        return PageResp.success(total, resultMoTicketVo);
    }

    //加载【回复信息】列表
    @POST
    @Path(Keys.SMSMGR_INBOX_REPLYSMS + "/list")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp listReply(@Context HttpServletRequest request, @Valid PageReqt reqt) {
        int total = 0;
        List<MoReplyVo> resultMoReplyVos = new ArrayList<MoReplyVo>();
        try {
            QueryParameters query = new QueryParameters(reqt);
            total = moTicketService.findMoReplyCountByMoTicketId(query);
            if (total == 0) {
                return PageResp.emptyResult();
            }
            PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), total);
            query.setPage(pageInfo);
            resultMoReplyVos = moTicketService.findMoReplyByMoTicketId(query);
        } catch (Exception e) {
            logger.error("moReply list failed:" + e);
        }
        return PageResp.success(total, resultMoReplyVos);
    }

    //回复信息
    @POST
    @Path(Keys.SMSMGR_INBOX_REPLYSMS)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp addMoReply(@Context HttpServletRequest req, @Valid MoReplyVo vo) throws RepositoryException {
        //只传入了moTicketId、replyContent两个参数
        SimpleUser user = SessionUtil.getCurUser();
        try {
            List<Contact> phoneList = new ArrayList<>();
            Contact contact = new Contact();
            contact.setPhone(vo.getPhone());
            phoneList.add(contact);
            //2.发送短信并获取pack_id
            MmsInfoVo mmsInfoVo = new MmsInfoVo();
            mmsInfoVo.setUserId(user.getId());
            mmsInfoVo.setBatchName(user.getUsername()+"回复"+vo.getPhone());
            //1.获取当前用户的默认业务类型ID gsms_user_business_type type=0
            QueryParameters parameters = new QueryParameters();
            parameters.addParam("enterpriseId",user.getEnterpriseId());
            parameters.addParam("type", 0);//0为默认类型
            List<BizType> bizTypes = bizTypeService.findBizType(parameters);
            if(bizTypes == null || bizTypes.isEmpty()){
                throw new BusinessException("该企业默认类型为空");
            }
            Integer bizId = bizTypes.get(0).getId();
            mmsInfoVo.setBizTypeId(bizId);
            MTResp mtResp = baseMsgResource.sendMoMsg(req, vo.getReplyContent(), phoneList, mmsInfoVo, com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType.LONGSMS, user);
            //3.插记录到gsms_moreply
            if (mtResp.getResult() == MTResult.SUCCESS) {
                MoReplyVo moReplyVo = new MoReplyVo();
                moReplyVo.setMoTicketId(vo.getMoTicketId());
                moReplyVo.setReplyUserId(user.getId());
                moReplyVo.setReplyTime(new Date());
                moReplyVo.setBizType(mmsInfoVo.getBizTypeId());
                moReplyVo.setReplyContent(vo.getReplyContent());
                moReplyVo.setMsgType(MsgContent.MsgType.LONGSMS.getIndex());
                moReplyVo.setDepartmentId(SessionUtil.getCurUser().getParentId());
                moReplyVo.setBatchName(mmsInfoVo.getBatchName());
                moReplyVo.setPackId(mtResp.getUuid().toString());
                moReplyVo.setReplyType(1);//回复类型 1，上行回复；2，服务调查
                moTicketService.addMoReply(moReplyVo);
                //4.更新gsms_moticket has_reply =1
                MoTicketVo po = moTicketService.findMoTicketById(vo.getMoTicketId());
                po.setHasReply(true);
                moTicketService.updateMoTicket(po);
            }
        } catch (Exception e) {
            logger.error("add phrase failed: ", e);
            return JsonResp.fail();

        }
        return JsonResp.success();
    }

    @POST
    @Path(Keys.SMSMGR_INBOX_EXPORTINBOX)
    public JsonResp export(PageReqt req){
        SimpleUser curUser = SessionUtil.getCurUser();
        try {
            Map<String, Object> map = req.getParams();
            String fileName = (String) map.get("fileName");
            // 构造参数，String类型默认值为""，数字默认为-1
            String delimiter = (String) map.get("delimiter");
            Map<String, Object> params = new HashMap<>();
            MoTicketVo moTicketVo = new MoTicketVo();
            QueryParameters query = new QueryParameters(req);
            DataScope ds = SessionUtil.getDataSope("//"+Keys.SMSMGR_INBOX + "//"+Keys.SMSMGR_INBOX_EXPORTINBOX);
            params.put("msgType", MsgContent.MsgType.LONGSMS.getIndex());
            SimpleUser user = SessionUtil.getCurUser();
            params.put("enterpriseId",user.getEnterpriseId());
            //根据当前用户数据权限，封装条件
            userMgrService.convertQueryByDataScope(query,ds,user);
            //根据前台选择部门，获取用户IDS
            userMgrService.convertQueryByDept(query,req,user);
            params.put("specNumber", req.getParams().get("specNumber"));
            params.put("userId", query.getParams().get("userId"));
            params.put("userIds", query.getParams().get("userIds"));
            params.put("hasReply", req.getParams().get("hasReply"));
            params.put("phone", req.getParams().get("phone"));
            params.put("beginTime", req.getParams().get("beginTime"));
            params.put("endTime", req.getParams().get("endTime"));
            params.put("delimiter", delimiter);
            FileTask task = new FileTask();
            task.setFileName(fileName);
            task.setTaskName("短信接收记录导出");
            String taskName = (String) map.get("name");
            if (StringUtils.isNotBlank(taskName)) {
                task.setTaskName(task.getTaskName() + "(" + taskName + ")");
            }
            task.setType(TaskType.Export);
            task.setDataType(BizDataType.MoTicket);
            task.setPostTime(new Date());
            task.setUserId(curUser.getId());
            task.setFileSize(0l);
            task.setHanldePercent(0);
            task.setState(TaskState.Wait);
            task.setPlatformId(config.getPlatformId());
            task.setParameters(XmlUtil.toXML(params));
            fileTaskService.save(task);
            executor.putTask2Queue(task);
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser, OperationType.EXPORT,"【接收记录】","Public","Login",""); //添加访问日志
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    @GET
    @Path(Keys.SMSMGR_INBOX_GETSMSLIST + "/fetchDeptData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteDepartment(@QueryParam("deptName") String deptName, @Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteDepartment(request,null,deptName);
    }
    @GET
    @Path(Keys.SMSMGR_INBOX_GETSMSLIST + "/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteUserName(@QueryParam("userName") String userName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteUser(request,null,userName);
    }
}

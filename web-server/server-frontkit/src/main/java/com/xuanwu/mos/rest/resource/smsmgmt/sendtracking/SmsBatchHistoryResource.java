package com.xuanwu.mos.rest.resource.smsmgmt.sendtracking;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.BaseSendResource;
import com.xuanwu.mos.rest.resource.VoUtilResource;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.rest.service.msgservice.MsgFrameService;
import com.xuanwu.mos.rest.service.msgservice.MsgTicketService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.ObjectUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.vo.MsgTicketVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
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


@Component
@Path(Keys.SMSMGR_SENDTRACKING)
public class SmsBatchHistoryResource  extends BaseSendResource {
    private static final Logger logger = LoggerFactory.getLogger(SmsBatchHistoryResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MsgFrameService msgFrameService;

    @Autowired
    private UserMgrService userMgrService;

    @Autowired
    private BaseMsgResource baseMsgResource;

    @Autowired
    private MsgTicketService msgTicketService;

    @Autowired
    private VoUtilResource voUtilResource;

    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_BATCHHISTORY)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp index(@Context HttpServletRequest request, @Valid PageReqt reqt){
        Parameters params = new Parameters(MsgContent.MsgType.LONGSMS.getIndex());
        SimpleUser curUser = SessionUtil.getCurUser();
        Integer entId = curUser.getEnterpriseId();

        QueryParameters query = new QueryParameters(reqt);
        DataScope ds = (DataScope) request.getAttribute(WebConstants.KEY_DATA_SCOPE);

        // 将query中的时间先取出来
        //DateUtil.format(new Date((Long) query.getParams().get("beginTime")));
        String beginTime = (String) query.getParams().get("beginTime");
        String endTime = (String) query.getParams().get("endTime");
        baseMsgResource.checkTableDataScope(query,ds);
        if (!ObjectUtil.validOnlyOne(query, "userId", "userIds","dataScope")) {
            return PageResp.success(0, Collections.emptyList());
        }
        query.addParam("msgType", MsgContent.MsgType.LONGSMS.getIndex());
        // 彩信没有短信那种分段(短信70字一段)
        query.addParam("msgSub",-1);

        // 最后一位放总数，之前每一位对应每一天的统计数
        query.addParam("beginTime", beginTime);
        query.addParam("endTime", endTime);

        /*
        int[] totals = baseMsgResource.findAllMmsNumberRecordCountMultiDays(query, params);
        int total = totals[totals.length - 1];
        PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), total);
        */

        int[] totals = new int[2];
        PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount());

        int offset = pageInfo.getFrom();
        int requireCount = pageInfo.getSize();
        query.setPage(pageInfo);
        List<MsgTicket> msgTickets = baseMsgResource.findAllMmsNumberRecordMultiDays(query, params, offset, requireCount, totals);
        List<MsgTicketVo> voList = voUtilResource.assembleNumberRecordVo(msgTickets,true);
        int total = baseMsgResource.calculateTotal(voList,reqt);
        return PageResp.success(total, voList);
    }

    @POST
    @Path(Keys.SMSMGR_SENDTRACKING_INDEX + "/showTicket")
    public JsonResp showTicket(MsgTicket msgTicket){
        try {
            List<MsgTicket> tickets = msgTicketService.findTicketsByPackId(msgTicket.getPackId(), MsgContent.MsgType.LONGSMS.getIndex(), msgTicket.getPostTime(),
                    MsgTicket.MsgSub.ParentMsg,1, "id asc");
            if(tickets.size()>0){
                MsgTicket ticket = tickets.get(0);
                msgTicket.setSmsContent(ticket.getSmsContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success(msgTicket);
    }
    @GET
    @Path(Keys.SMSMGR_SENDTRACKING_BATCHHISTORY + "/fetchDeptData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteDepartment(@QueryParam("deptName") String deptName, @Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteDepartment(request,null,deptName);
    }
    @GET
    @Path(Keys.SMSMGR_SENDTRACKING_BATCHHISTORY + "/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteUserName(@QueryParam("userName") String userName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteUser(request,null,userName);
    }
}

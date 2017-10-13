package com.xuanwu.mos.rest.resource.mmsmgmt.sendtracking;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgContent;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.resource.BaseMsgResource;
import com.xuanwu.mos.rest.resource.VoUtilResource;
import com.xuanwu.mos.rest.resource.mmsmgmt.BaseMmsResource;
import com.xuanwu.mos.utils.ObjectUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.vo.MsgPackVo;
import com.xuanwu.mos.vo.MsgTicketVo;

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

/** 彩信管理 --> 彩信记录 --> 发送详情
 * 号码为维度的发送详情的查询、查看功能
 * Created by 郭垚辉 on 2017/4/13.
 */
@Component
@Path(Keys.SMSMGR_SENDTRACKINGMMS)
public class MmsNumberDetailResource  extends BaseMmsResource {

    @Autowired
    private BaseMsgResource baseMsgResource;
    @Autowired
    private VoUtilResource voUtilResource;


    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADNUMBERS)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp index(@Context HttpServletRequest request, @Valid PageReqt reqt){
        Parameters params = new Parameters(MsgTypeEnum.MMS.getIndex());
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
        query.addParam("msgType", MsgContent.MsgType.MMS.getIndex());
        query.addParam("entId", entId);
        // 彩信没有短信那种分段(短信70字一段)
        query.addParam("msgSub",-1);
        query.addParam("enterpriseId",curUser.getEnterpriseId());
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
        List<MsgTicketVo> voList =  voUtilResource.assembleNumberRecordVo(msgTickets,false);

        int total = baseMsgResource.calculateTotal(voList,reqt);
        return PageResp.success(total, voList);
    }

    //region 自动补全相关
    // 补全用户
    @GET
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADNUMBERS + "/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteUserName( @QueryParam("userName") String userName,@Context HttpServletRequest request) {
        // 借用该resource的dataScope作为该自动补全的dataScope。否则该方法的dataScope的大小为NONE
        return baseMsgResource.autoCompleteUser(request,null, userName);
    }

    // 补全部门
    @GET
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADNUMBERS + "/fetchDeptData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCompleteDeptName(@QueryParam("deptName") String deptName,@Context HttpServletRequest request) {
        return baseMsgResource.autoCompleteDepartment(request,null, deptName);
    }
    // endregion

    //region 查看当前的短信包
    @POST
    @Path(Keys.SMSMGR_SENDTRACKINGMMS_LOADNUMBERS+"/showMms")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp showMms(@Valid MsgPackVo msgPackVo){
        return mmsPreviewByPackId(msgPackVo.getPackId(),msgPackVo.getPostTime());
    }
    //endregion

}

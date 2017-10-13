package com.xuanwu.mos.rest.resource.informationmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.job.DeleteBlackJob;
import com.xuanwu.mos.rest.service.BizDataService;
import com.xuanwu.mos.rest.service.BizTypeService;
import com.xuanwu.mos.rest.service.BlackListService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**业务类型
 * Created by zhangz on 2017/3/23.
 */
@Component
@Path(Keys.INFO_BIZTYPE)
public class BizTypeResource {
    private Logger logger = LoggerFactory.getLogger(BizTypeResource.class);
    @Autowired
    private DeleteBlackJob deleteBlackJob;
    @Autowired
    private BizTypeService bizTypeService;
    @Autowired
    private BizDataService bizDataService;
    @Autowired
    private BlackListService blacklistService;
    @Autowired
    private SysLogService sysLogService;
    @POST
    @Path(Keys.INFO_BIZTYPE_LIST)
    public JsonResp list(@Valid PageReqt req) {
        QueryParameters params = new QueryParameters(req);
        params.addParam("enterpriseId",SessionUtil.getCurUser().getEnterpriseId());
        int total = bizTypeService.findBizTypeCount(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }
        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        List<BizType> bizTypes = bizTypeService.findBizType(params);
        return PageResp.success(total, bizTypes);
    }
    @POST
    @Path(Keys.INFO_BIZTYPE_ADD)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp save(@Valid BizType bizType) {
        SimpleUser user = SessionUtil.getCurUser();
        BizType exist = bizTypeService.findByNameAndEntId(bizType.getName(),user.getEnterpriseId());
        if(exist!=null){
            return JsonResp.fail(Messages.BIZTYPE_EXIST);
        }
        bizType.setEnterpriseId(user.getEnterpriseId());
        //按照UMP代码，继续hardcode 1
        bizType.setState(1);
        //0为默认业务类型
        bizType.setType(1);
        try {
            bizTypeService.storeBizType(bizType,user,bizType.getExtAttr1());
            
            sysLogService.addLog(user,OperationType.NEW,"【业务类型】","Public","Login","【" + bizType.getName() + "】"); //添加访问日志
            
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail(Messages.SYSTEM_ERROR);
        }
        return PageResp.success();
    }
    @POST
    @Path(Keys.INFO_BIZTYPE_ADD+"/getChannel")
    public JsonResp getChannel(){
        int enterpriseId = SessionUtil.getCurUser().getEnterpriseId();
        List<CarrierChannel> carrierChannels = bizTypeService
                .findCarrierChannelByEnterpriseId(enterpriseId);
        if (ListUtil.isNotBlank(carrierChannels)) {
            for (CarrierChannel carrierChannel : carrierChannels) {
                carrierChannel.setMsgTypeStr(matchingMsgType(carrierChannel.getMsgType()));
                for (Carrier c : carrierChannel.getCanSendCarrier()) {
                    Carrier carrier = bizDataService.getCarrierById(c.getId());
                    c.setName(carrier.getName());
                }
            }
        }
        return PageResp.success(carrierChannels.size(), carrierChannels);
    }
    @POST
    @Path(Keys.INFO_BIZTYPE_INDEX + "/checkChannelExtend")
    public JsonResp checkChannelExtend(@QueryParam("channelIds")String channelIds){
        String result = null;
        String[] realChannelid = channelIds.split(",");
        List<CarrierChannel> extendChannel = new ArrayList<>();
        for (String id : realChannelid) {
            CarrierChannel carrierChannel = bizTypeService
                    .findExtentChannel(Integer.parseInt(id));
            if (carrierChannel != null) {
                extendChannel.add(carrierChannel);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (ListUtil.isNotBlank(extendChannel)) {
            for (CarrierChannel c : extendChannel) {
                sb.append("【").append(c.getName()).append("】").append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
            return JsonResp.fail(StatusCode.Other.getIndex(),result);
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.INFO_BIZTYPE_INDEX + "/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp detail(@PathParam("id") Integer id){
        BizType bizType = bizTypeService.findBizTypeDetailById(id);
        for (CarrierChannel cc : bizType.getCarrierChannel()) {
            cc.setMsgTypeStr(matchingMsgType(cc.getMsgType()));
            for (Carrier c : cc.getCanSendCarrier()) {
                Carrier carrier = bizDataService.getCarrierById(c.getId());
                c.setName(carrier.getName());
            }
        }
        return JsonResp.success(bizType);
    }
    @POST
    @Path(Keys.INFO_BIZTYPE_UPDATE)
    public JsonResp update(@Valid BizType bizType,@QueryParam("channelIds")String channelIds){
        SimpleUser loginUser = SessionUtil.getCurUser();
        int enterpriseId = loginUser.getEnterpriseId();
        BizType exist = bizTypeService.findByNameAndEntId(bizType.getName(),
                enterpriseId);
        if (exist != null && !exist.getId().equals(bizType.getId())) {
            return JsonResp.fail(Messages.BIZTYPE_EXIST);
        }
        try {
            String[] channelids = channelIds.split(",");
            BizType update = new BizType();
            update.setId(bizType.getId());
            update.setName(bizType.getName());
            update.setStartTime(bizType.getStartTime());
            update.setEndTime(bizType.getEndTime());
            update.setPriority(bizType.getPriority());
            update.setExtendFlag(bizType.isExtendFlag());
            update.setRemark(bizType.getRemark());
            update.setSpeed(bizType.getSpeed());
            update.setSpeedMode(bizType.getSpeedMode());
            bizTypeService.modifyBizType(update, bizType.getExtAttr1(),channelids);
        } catch (Exception e) {
            logger.error("add biztype failed: ", e);
            return JsonResp.fail("更新业务类型发生异常，请联系管理员！");
        }
        
        sysLogService.addLog(loginUser,OperationType.MODIFY,"【业务类型】","Public","Login","【"+bizType.getName()+"】"); //添加访问日志
        
        return JsonResp.success();
    }
    @POST
    @Path(Keys.INFO_BIZTYPE_DEL)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp del(Integer[] ids, @QueryParam("name")String name){
        try {
            List<Integer> idList = new ArrayList<Integer>();
            StringBuilder _name = new StringBuilder();
            for(Integer id :ids){
                idList.add(id);
                BizType bizType = bizTypeService.findBizTypeById(id);
                name = bizType.getName();
                if (bizType.getType() == 0) {
                    return JsonResp.fail(Messages.DEFAULT_BIZTYPE);
                }
                _name.append(name+",");
                bizTypeService.deleteBizType(id);
            }
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            String logContent = _name.substring(0,_name.length()-1);
            sysLogService.addLog(loginUser,OperationType.DELETE,"【业务类型】",
                    "Public","Login","【"+ LogContentUtil.format(logContent)+"】"); //添加访问日志
            
            
            QueryParameters params = new QueryParameters();
            params.addParam("targets",idList);
            params.addParam("type",WebConstants.BLACK_BIZ_TYPE);
            List<BlackList> blacks = blacklistService.findBlackList(params);
            //删除该业务类型下的黑名单
            deleteBlackJob.putTaskQueue(blacks);
        }catch (Exception e){
            logger.error("delete biztype failed: ", e);
            e.printStackTrace();
            return JsonResp.fail("删除业务类型发生异常，请联系管理员！");
        }
        
        
        return JsonResp.success();
    }
    /**
     * 将数字msgtype类型匹配成相应的汉字类型
     * @param msgType
     * @return
     */
    private String matchingMsgType(String msgType){
        String msgTypeStr = "短信";
        int type = Integer.valueOf(msgType);
        switch (type){
            case 1:
                msgTypeStr = "短信";
                break;
            case 2:
                msgTypeStr = "彩信";
                break;
            case 8:
                msgTypeStr = "语音通知";
                break;
            case 9:
                msgTypeStr = "语音验证码";
                break;
            default:
                msgTypeStr = "短信";
        }
        return msgTypeStr;
    }
}

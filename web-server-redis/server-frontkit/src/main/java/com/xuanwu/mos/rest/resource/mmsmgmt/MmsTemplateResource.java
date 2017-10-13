package com.xuanwu.mos.rest.resource.mmsmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.MmsContent;
import com.xuanwu.mos.domain.entity.Phrase;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.rest.service.msgservice.PhraseService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.MmsTemplateJson;
import com.xuanwu.mos.vo.MmsTemplateListVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/** 彩信管理 --> 彩信模板管理
 * Created by 郭垚辉 on 2017/4/1.
 */
@Component
@Path(Keys.INFORMATIONMGR_TEMPLATE)
public class MmsTemplateResource {

    @Autowired
    private PhraseService phraseService;
    
    @Autowired
    private SysLogService sysLogService;

    /**
     * 根据模板的title名获取所有彩信模板
     * PageIndex=1&PageSize=15&SortName=id&Sortorder=desc&QueryCondition=&QueryType=&title=
     * @param
     * @return
     */
    @POST
    @Path(Keys.INFORMATIONMGR_TEMPLATE_LIST)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp listTemplateMms(@Valid PageReqt reqt){
        QueryParameters params = new QueryParameters(reqt);
        SimpleUser user = SessionUtil.getCurUser();
        List<Phrase> phraseList = null;
        params.addParam("userId",user.getId());
        params.addParam("msgType",MsgTypeEnum.MMS.getIndex());
        params.addParam("enterpriseId",user.getEnterpriseId());
        int phraseListCount = phraseService.findPhraseListCount(params);
        PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), phraseListCount);
        params.setPage(pageInfo);
        List<MmsTemplateListVo> voList = Collections.emptyList();
        if (phraseListCount > 0) {
            phraseList = phraseService.findPhraseList(params);
            voList = assembleMmsTemplateVo(phraseList);
        }
        return PageResp.success(phraseListCount, voList);
    }

    @POST
    @Path(Keys.INFORMATIONMGR_TEMPLATE_CREATE)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp addMmsTemplate(@Valid String mmsTemplateJson, @Context ServletRequest request) {
        try {
            Phrase phrase = new Phrase();
            MmsTemplateJson deserialize = JsonUtil.deserialize(mmsTemplateJson, MmsTemplateJson.class);
            String title = deserialize.getTitle();
            MmsContent mmsContent = deserialize.getMms();
            String uuid = UUID.randomUUID().toString();
            String identify = uuid.replaceAll("-","");
            phrase.setTitle(title);
            // 设置内容
            mmsContent.setSubject(title);
            phrase.setContent(MmsUtil.toTemplate(mmsContent).getBytes(WebConstants.DEFAULT_CHARSET));
            phrase.setUserId(SessionUtil.getCurUser().getId());
            phrase.setMsgType(MsgTypeEnum.MMS.getIndex());
            phrase.setLastUpdateTime(new Date());
            phrase.setIdentify(identify);
            boolean isAdd = phraseService.addPhrase(phrase);
            if (isAdd){
            	sysLogService.addLog(SessionUtil.getCurUser(),OperationType.NEW,"【彩信模板】","Public","Login","【" + title + "】"); //添加访问日
                return JsonResp.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResp.fail();
    }

    @POST
    @Path(Keys.INFORMATIONMGR_TEMPLATE_DELETE)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp deleteMmsTemplate(Integer[] ids) {
        if (ids==null||ids.length==0){
            return JsonResp.fail(Messages.SELECT_REMOVE_DATA);
        }
        StringBuilder _name = new StringBuilder();
        String name = "";
        Phrase phrase = null;
        List deleteTemplateList = new ArrayList();
        if (ids.length == 1) {
            phrase = phraseService.findPhraseById(ids[0]);            
        }
        for (Integer id : ids) {
        	_name.append(phraseService.findMosPhraseById(id).getTitle()+",");
            deleteTemplateList.add(id);
        }
        
        if(_name.length()>0){
        	name = _name.substring(0,_name.length()-1);
        }
        
        int i = phraseService.deletePhraseByIds(deleteTemplateList);
        if (i==1) {
        	sysLogService.addLog(SessionUtil.getCurUser(),OperationType.DELETE,"【彩信模板】","Public","Login","【"+name+"】"); //添加访问日志
            return JsonResp.success(phrase.getTitle()+"删除成功");
        } else if (i>1){
        	sysLogService.addLog(SessionUtil.getCurUser(),OperationType.DELETE,"【彩信模板】","Public","Login","【"+ LogContentUtil.format(name)+"】"); //添加访问日
            return JsonResp.success("彩信模板批量操作成功");
        } else {
            return JsonResp.fail("模板不存在，或删除出错");
        }
    }

    @POST
    @Path(Keys.INFORMATIONMGR_TEMPLATE_EDIT+"/update")
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp updateMmsTemplate(@Valid String mmsTemplateJson, @Context ServletRequest request) {
        try {
            MmsTemplateJson templateJson = JsonUtil.deserialize(mmsTemplateJson, MmsTemplateJson.class);
            String title = templateJson.getTitle();
            Integer id = templateJson.getId();
            Phrase phrase = new Phrase();
            phrase.setId(id);
            phrase.setTitle(title);
            MmsContent mmsContent = templateJson.getMms();
            mmsContent.setSubject(title);
            phrase.setContent(MmsUtil.toTemplate(mmsContent).getBytes(WebConstants.DEFAULT_CHARSET));
            phrase.setUserId(SessionUtil.getCurUser().getId());
            phrase.setMsgType(MsgTypeEnum.MMS.getIndex());
            phrase.setLastUpdateTime(new Date());
            phraseService.updatePhrase(phrase);
            
            sysLogService.addLog(SessionUtil.getCurUser(),OperationType.MODIFY,"【彩信模板】","Public","Login","【"+phrase.getTitle()+"】"); //添加访问日志
            
            return JsonResp.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResp.fail();
    }

    @POST
    @Path(Keys.INFORMATIONMGR_TEMPLATE_EDIT)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp findMmsTemplate(Integer templateId) {
        // 获取彩信模板的id
        Phrase phrase = phraseService.findPhraseById(templateId);
        if (phrase!=null) {
            return JsonResp.success(phrase.toMmsJson().toString());
        } else {
            return JsonResp.fail("无法找到你需要的模板信息");
        }
    }

    private List<MmsTemplateListVo> assembleMmsTemplateVo(List<Phrase> phraseList) {
        List<MmsTemplateListVo> resultVoList = new ArrayList<>();
        if (phraseList.isEmpty()) {
            return Collections.emptyList();
        } else {
            for (Phrase phrase:phraseList){
                MmsTemplateListVo vo = new MmsTemplateListVo();
                vo.setId(phrase.getId());
                vo.setTitle(phrase.getTitle());
                vo.setIdentify(phrase.getIdentify());
                vo.setLastUpdateTime(phrase.getLastUpdateTime());
                resultVoList.add(vo);
            }
            return resultVoList;
        }
    }
}

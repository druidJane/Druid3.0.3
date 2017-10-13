package com.xuanwu.mos.rest.resource.smsmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.Phrase;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.MsgTypeEnum;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.service.msgservice.PhraseService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.LogContentUtil;
import com.xuanwu.mos.utils.Messages;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 短信模板管理控制器
 * Created by Jiang.Ziyuan on 2017/5/4.
 */
@Component
@Path(Keys.INFO_COMMONTEMPLATE)
public class SmsTemplateResource{
    private static final Logger logger = LoggerFactory.getLogger(SmsTemplateResource.class);
    @Autowired
    private PhraseService phraseService;
    @Autowired
    private SysLogService sysLogService;

	/**
	 * 变量模板查询
     * @param req
     * @return
     */
    @POST
    @Path(Keys.INFO_VARIANTTEMPLATE_LIST)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp variantList(@Valid PageReqt req) {
        return list(req);
    }

    @POST
    @Path(Keys.INFO_COMMONTEMPLATE_LIST)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp list(@Valid PageReqt req) {
        SimpleUser loginUser = SessionUtil.getCurUser();
        int enterpriseId = loginUser.getEnterpriseId();
        QueryParameters params = new QueryParameters(req);
        params.addParam("enterpriseId", enterpriseId);
        params.addParam("userId",loginUser.getId());
        params.addParam("msgType",MsgTypeEnum.SMS.getIndex());

        int total = phraseService.findPhraseCount(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }

        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        List<Phrase> info = phraseService.list(params);
        return PageResp.success(total, info);
    }

    /**
     * 变量模板新增
     * @param phrase
     * @return
     */
    @POST
    @Path(Keys.INFO_VARIANTTEMPLATE_ADD)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp variantAdd(@Valid Phrase phrase) throws RepositoryException {
        return addPhrase(phrase);
    }

    @POST
    @Path(Keys.INFO_COMMONTEMPLATE_ADD)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp addPhrase(@Valid Phrase phrase) throws RepositoryException {
        String title = phrase.getTitle();
        String phraseSMSContent = phrase.getPhraseSMSContent();

        //生成uuid作为模板编码
        String uuid = UUID.randomUUID().toString();
        String identify = uuid.replaceAll("-","");

        /**当新增模板“发送免审”为【是】时，新增后，该模板审核结果为“待审核”；
         *当新增模板“发送免审”为【否】时，新增后，该模版审核结果为“已通过”。
         *审核状态-0:待审核1通过2不通过
         */
        if(phrase.getTemplateType() == 1){
            phrase.setAuditState(0);
        }else if(phrase.getTemplateType() == 0){
            phrase.setAuditState(1);
        }

        boolean result = false;
        try {
            phrase.setTitle(title);
            phrase.setContent(phraseSMSContent.getBytes(WebConstants.DEFAULT_CHARSET));
            phrase.setUserId(SessionUtil.getCurUser().getId());
            phrase.setMsgType(MsgTypeEnum.SMS.getIndex());
            phrase.setIdentify(identify);
            phrase.setLastUpdateTime(new Date());
            phrase.setCreateTime(new Date());
            phrase.setEnterpriseId(SessionUtil.getCurUser().getEnterpriseId());
            result = phraseService.addSMSPhrase(phrase);
        } catch (Exception e) {
            logger.error("add phrase failed: ", e);
            return JsonResp.fail();

        }
        if (result == true) {
        	 sysLogService.addLog(SessionUtil.getCurUser(),OperationType.NEW,"【短信模板】","Public","Login","【" + title + "】"); //添加访问日志
             
            return JsonResp.success();
        } else {
            return JsonResp.fail();
        }
    }

    /**
     * 变量模板删除
     * @param ids
     * @return
     */
    @POST
    @Path(Keys.INFO_VARIANTTEMPLATE_DEL)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp variantDel(List<Integer> ids) throws RepositoryException {
        return delete(ids);
    }

    /**
     * 删除模板
     */
    @POST
    @Path(Keys.INFO_COMMONTEMPLATE_DEL)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp delete(List<Integer> ids){
        if (ids == null || ids.size() == 0) {
            return JsonResp.fail(Messages.SELECT_REMOVE_DATA);
        }
        
        StringBuilder _name = new StringBuilder();
        String name = "";
        for(Integer id :ids){
        	_name.append(phraseService.findMosPhraseById(id).getTitle()+",");
        }
        
        if(_name.length()>0){
        	name = _name.substring(0,_name.length()-1);
        }
        
        if (phraseService.deletePhraseByIds(ids) == ids.size()) {
        	        	
        	sysLogService.addLog(SessionUtil.getCurUser(),OperationType.DELETE,"【短信模板】","Public","Login","【"+ LogContentUtil.format(name)+"】"); //添加访问日志
            
            return PageResp.success();
        }
        return PageResp.fail();
    }

    /**
     * 变量模板修改
     * @param phrase
     * @return
     */
    @POST
    @Path(Keys.INFO_VARIANTTEMPLATE_UPDATE)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp variantUpdate(@Valid Phrase phrase) throws RepositoryException {
        return update(phrase);
    }

    /**
     * 修改模板
     */
    @POST
    @Path(Keys.INFO_COMMONTEMPLATE_UPDATE)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp update(@Valid Phrase phrase) {
        Phrase updateParams = new Phrase();
        String phraseSMSContent = phrase.getPhraseSMSContent();
        try {
            SimpleUser user = SessionUtil.getCurUser();
            updateParams.setId(phrase.getId());
            updateParams.setContent(phraseSMSContent.getBytes(WebConstants.DEFAULT_CHARSET));
            updateParams.setTitle(phrase.getTitle());
            updateParams.setUserId(user.getId());
            updateParams.setLastUpdateTime(new Date());
            updateParams.setTemplateType(phrase.getTemplateType());

            if(phrase.getTemplateType() == 1){
                updateParams.setAuditState(0);//0:待审核1通过2不通过
            }else if(phrase.getTemplateType() == 0){
                updateParams.setAuditState(1);//0:待审核1通过2不通过
            }

            boolean updateResult = phraseService.updateSMSPhrase(updateParams);
            if (updateResult) {
            	sysLogService.addLog(SessionUtil.getCurUser(),OperationType.MODIFY,"【模板管理】","Public","Login","【"+phrase.getTitle()+"】"); //添加访问日志
                
                return PageResp.success();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return PageResp.fail();
    }
}

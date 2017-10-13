package com.xuanwu.mos.rest.resource.informationmt;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.entity.KeyWord;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.file.FileHeader;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.HeadDMapToF;
import com.xuanwu.mos.file.HeadInfo;
import com.xuanwu.mos.file.mapbean.KeywordMap;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.request.ImportRequest;
import com.xuanwu.mos.rest.service.FileTaskService;
import com.xuanwu.mos.rest.service.KeyWordService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.LogContentUtil;
import com.xuanwu.mos.utils.Messages;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**非法关键字
 * Created by zhangz on 2017/3/23.
 */
@Component
@Path(Keys.INFO_KEYWORD)
public class KeyWordResource {
    @Autowired
    private KeyWordService keyWordService;
    @Autowired
    private FileImporter fileImporter;
    @Autowired
    private FileTaskExecutor executor;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private Config config;
    @Autowired
    private SysLogService sysLogService;
    @POST
    @Path(Keys.INFO_KEYWORD_LIST)
    public JsonResp list(@Valid PageReqt req){
        QueryParameters params = new QueryParameters(req);
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("targetId",user.getEnterpriseId());
        int total = keyWordService.findKeywordCount(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }
        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        List<KeyWord> list = keyWordService.findKeywords(params);
        return PageResp.success(total, list);
    }
    @POST
    @Path(Keys.INFO_KEYWORD_ADD)
    public JsonResp add(@Valid KeyWord keyWord){
        SimpleUser user = SessionUtil.getCurUser();
        keyWord.setTargetId(user.getEnterpriseId());
        KeyWord isExists = keyWordService.isExists(keyWord);
        if(isExists != null){
            return JsonResp.fail(Messages.KEYWORD_EXIST);
        }
        try {
            keyWord.setUserId(user.getId());
            keyWordService.addOrUpdateKeyword(keyWord,null);
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.NEW,"【非法关键字】","Public","Login","【" + keyWord.getKeywordName() + "】"); //添加访问日志
            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail("新增关键字失败，请联系管理员！");
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.INFO_KEYWORD_UPDATE)
    public JsonResp update(@Valid KeyWord update){
        if(StringUtils.isEmpty(update.getKeywordName())){
            return JsonResp.fail("非法关键字不可为空！");
        }
        SimpleUser user = SessionUtil.getCurUser();
        update.setTargetId(user.getEnterpriseId());
        KeyWord isExists = keyWordService.isExists(update);
        if(isExists != null && !isExists.getId().equals(update.getId())){
            return JsonResp.fail(Messages.KEYWORD_EXIST);
        }
        try {
            update.setUserId(user.getId());
            update.setTargetId(user.getEnterpriseId());
            keyWordService.addOrUpdateKeyword(update,update.getId());
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.MODIFY,"【非法关键字】","Public","Login","【"+update.getKeywordName()+"】"); //添加访问日志
            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail("更新关键字失败，请联系管理员！");
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.INFO_KEYWORD_DELETE)
    public JsonResp del(Integer[] ids){
        if (ids == null || ids.length == 0) {
            return JsonResp.fail(Messages.SELECT_REMOVE_DATA);
        }
        try {
        	StringBuilder _name = new StringBuilder();
        	String name = "";
        	for(Integer id:ids){
	        	QueryParameters par =new QueryParameters();
	            par.addParam("id", id);
	            if(keyWordService.findKeywordCount(par)>0){
	            	_name.append(keyWordService.findKeywords(par).get(0).getKeywordName()+",");
	            }
        	}
        	if(_name.length()>0){
            	name = _name.substring(0,_name.length()-1);
            }
        	
            keyWordService.delKeyword(ids);
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.DELETE,"【非法关键字】",
                    "Public","Login","【"+ LogContentUtil.format(name)+"】"); //添加访问日志
            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail("删除关键字失败，请联系管理员！");
        }
        return JsonResp.success();
    }
    @GET
    @Path(Keys.INFO_KEYWORD_IMPORT)
    public JsonResp getFileHeader(){
        List<HeadDMapToF> headDMapToFList = new ArrayList<>();
        for (int i = 0; i < FileHeader.KEYWORD_IMPORT.length; i++) {
            HeadDMapToF headDMapToF = new HeadDMapToF();
            headDMapToF.setDataHeadInfo(new HeadInfo(i, FileHeader.KEYWORD[i]));
            headDMapToFList.add(headDMapToF);
        }
        return JsonResp.success(headDMapToFList);
    }
    @POST
    @Path(Keys.INFO_KEYWORD_IMPORT)
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public JsonResp upload(@Context HttpServletRequest request){
        UploadResult result = fileImporter.upload(BizDataType.Keyword, request);
        StatusCode statusCode = result.getStatusCode();
        if (statusCode != StatusCode.Success) {
            return JsonResp.fail(statusCode.getStateDesc());
        }
        return JsonResp.success(result);
    }
    @POST
    @Path(Keys.INFO_KEYWORD_IMPORT)
    public JsonResp doImport(@Valid ImportRequest req){
        try {
            if (!req.isCorrectFileHead()) {
                return JsonResp.fail(-1, Messages.INCORRECT_FILE_COLUMN);
            }
            SimpleUser curUser = SessionUtil.getCurUser();
            // 构造文件列映射
            List<HeadDMapToF> headDMapToFList = req.getHeadDMapToFList();
            KeywordMap keywordMap = new KeywordMap();

            for (HeadDMapToF headDMapToF : headDMapToFList) {
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.KEYWORD[0])) {
                    keywordMap.setKeywordName(headDMapToF.getFileHeadInfo().getIndex());
                }
            }
            FileTask task = new FileTask();
            task.setFileName(req.getFileName());
            task.setTaskName("非法关键字导入");
            task.setType(TaskType.Import);
            task.setDataType(BizDataType.Keyword);
            task.setPostTime(new Date());
            task.setUserId(curUser.getId());
            task.setFileSize(req.getFileSize());
            task.setPlatformId(config.getPlatformId());
            Map<String, String> params = new HashMap<>();
            params.put("userId", String.valueOf(curUser.getId()));
            params.put("entId", String.valueOf(curUser.getEnterpriseId()));
            params.put("delimeter", req.getDelimiter());
            params.put("keywordMap", keywordMap.tran2Params());

            task.setParamsMap(params);
            task.setHanldePercent(0);
            task.setState(TaskState.Wait);
            fileTaskService.save(task);
            executor.putTask2Queue(task);
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.IMPORT,"【非法关键字】","Public","Login",""); //添加访问日志
            
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.INFO_KEYWORD_EXPORT)
    public JsonResp export(@Valid PageReqt req){
        SimpleUser curUser = SessionUtil.getCurUser();
        try {
            Map<String, Object> map = req.getParams();
            String fileName = (String) map.get("fileName");
            // 构造参数，String类型默认值为""，数字默认为-1
            String delimiter = (String) map.get("delimiter");
            Map<String, Object> params = new HashMap<>();
            if (map.get("_lk_keywordName") != null) {
                params.put("_lk_keywordName", map.get("_lk_keywordName"));
            } else {
                params.put("_lk_keywordName", "");
            }
            params.put("targetId",curUser.getEnterpriseId());
            params.put("delimiter", delimiter);
            FileTask task = new FileTask();
            task.setFileName(fileName);
            task.setTaskName("非法关键字导出");
            String taskName = (String) map.get("name");
            if (org.apache.commons.lang.StringUtils.isNotBlank(taskName)) {
                task.setTaskName(task.getTaskName() + "(" + taskName + ")");
            }
            task.setType(TaskType.Export);
            task.setDataType(BizDataType.Keyword);
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
            sysLogService.addLog(loginUser,OperationType.EXPORT,"【非法关键字】","Public","Login",""); //添加访问日志
            
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
}

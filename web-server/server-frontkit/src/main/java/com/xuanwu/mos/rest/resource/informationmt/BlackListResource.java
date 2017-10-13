package com.xuanwu.mos.rest.resource.informationmt;

/**黑名单
 * Created by zhangz on 2017/3/23.
 */

import com.alibaba.fastjson.JSONObject;
import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.BlackList;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.UserTask;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.MosBizDataType;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.file.*;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.request.ImportRequest;
import com.xuanwu.mos.rest.service.BlackListService;
import com.xuanwu.mos.rest.service.UserTaskService;
import com.xuanwu.mos.service.FtpService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.LogContentUtil;
import com.xuanwu.mos.utils.Messages;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.WebConstants;
import com.xuanwu.mos.vo.UserTaskParameter;
import com.xuanwu.msggate.common.cache.engine.LongHashCache;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Component
@Path(Keys.INFO_BLACKLIST)
public class BlackListResource {
    private Logger logger = LoggerFactory.getLogger(BlackListResource.class);
    @Autowired
    private FileTaskExecutor executor;
    @Autowired
    private Config config;
    @Autowired
    private FileImporter fileImporter;
    @Autowired
    private BlackListService blackListService;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private SysLogService sysLogService;

    @POST
    @Path(Keys.INFO_BLACKLIST_LIST)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp list(@Valid PageReqt req){
        QueryParameters params = new QueryParameters(req);
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("enterpriseId",user.getEnterpriseId());
        int total = blackListService.findBlacklistCount(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }
        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        String targetName = (String) req.getParams().get("targetName");
        params.addParam("targetName",targetName);
        List<BlackList> list = blackListService.findBlacklistsDetail(params);
        return PageResp.success(total, list);
    }
    @POST
    @Path(Keys.INFO_BLACKLIST_ADD)
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp add(@Valid BlackList blackList){
        try {
            Integer targetId = blackList.getTarget();
            if (targetId <= 0) {
                return JsonResp.fail(WebConstants.JSON_MSG_FAIL);
            }
            BlackList add = new BlackList();
            add.setPhone(blackList.getPhone().trim());
            add.setTarget(targetId);
            int type = blackList.getType();
            if (type >= 0) {
                add.setType(type);
            }
            boolean exist = false;
            exist = blackListService.findTeleSeg(add);
            if (!exist) {
               return JsonResp.fail(WebConstants.BlacklistResultCode.PHONE_RESULT_NO_CARRIER_TELESEG,Messages.PHONE_RESULT_NO_CARRIER_TELESEG);
            }
            exist = blackListService.isExist(add);
            if (exist) {
                return JsonResp.fail(WebConstants.BlacklistResultCode.BLACKLIST_RESULT_EXIST,Messages.BLACKLIST_EXIST);
            }
            add.setRemark(blackList.getRemark());
            convertData(add);
            blackListService.addBlacklist(add);
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.NEW,"【黑名单】","Public","Login","【" + add.getPhone() + "】"); //添加访问日志
            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail("新增黑名单异常，请联系管理员！");
        }
        return JsonResp.success();
    }

    private void convertData(BlackList add) {
        SimpleUser user = SessionUtil.getCurUser();
        add.setEnterpriseID(user.getEnterpriseId());
        Date currentDate = new Date();
        add.setCreateTime(currentDate);
        add.setUser(user.getUsername());
        add.setHandleTime(currentDate);
        LongHashCache cache = new LongHashCache();
        long zipmes = cache.tran2Code(add.getPhone(), add.getType(), add.getTarget());
        add.setZipmes(zipmes);
    }

    /**
     * 删除
     * @param ids
     * @return
     * @throws RepositoryException
     */
    @POST
    @Path(Keys.INFO_BLACKLIST_DEL)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp delete(Integer[] ids) throws RepositoryException {
        if (ids == null || ids.length == 0) {
            return JsonResp.fail(Messages.SELECT_REMOVE_DATA);
        }
        StringBuilder _name = new StringBuilder();
    	String name = "";
        for (int id : ids) {
            try {
            	
            	QueryParameters par =new QueryParameters();
	            par.addParam("id", id);
	            par.addParam("type", 9);
	            if(blackListService.findBlacklistCount(par)>0){
	            	_name.append(blackListService.findBlackList(par).get(0).getPhone()+",");
	            }
	            if(_name.length()>0){
	            	name = _name.substring(0,_name.length()-1);
	            }
            	
                blackListService.delBlacklist(id);               
                
            } catch (RepositoryException e) {
                e.printStackTrace();
                return JsonResp.fail();
            }
        }
        SimpleUser loginUser = SessionUtil.getCurUser();
        sysLogService.addLog(loginUser,OperationType.DELETE,"【黑名单】",
                "Public","Login","【"+ LogContentUtil.format(name)+"】"); //添加访问日志

        return JsonResp.success();
    }
    /**
     * 上传
     */
    @POST
    @Path(Keys.INFO_BLACKLIST_IMPORT)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public JsonResp upload(@Context HttpServletRequest request) {
        //先将文件上传到本地服务器，并根据类型获取相应handler，校验头文件分隔符和文件类型
        UploadResult result = fileImporter.upload(BizDataType.Blacklist, request);
        StatusCode statusCode = result.getStatusCode();
        if (statusCode != StatusCode.Success) {
            return JsonResp.fail(statusCode.getStateDesc());
        }
        try {
            //上传文件到FTP
            String path = FileUtil.getDataServerPath(MosBizDataType.Front_BlackList_Import);
            result.setPath(path);
            ftpService.uploadFile(path + result.getFileName(),result.getIs());
                       
            return JsonResp.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
    }

    /**
     * 插库，由data-server处理
     */
    @POST
    @Path(Keys.INFO_BLACKLIST_IMPORT)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonResp doImport(@Valid ImportRequest req) {
        UserTask userTask = convertImportData(req);
        //插库
        try {
            userTaskService.addImportTask(MosBizDataType.Front_BlackList_Import,userTask);
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.IMPORT,"【黑名单】","Public","Login",""); //添加访问日志
            
            return JsonResp.success();
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
    }

    /**
     * 获取导入文件内容头格式
     * @return
     */
    @GET
    @Path(Keys.INFO_BLACKLIST_IMPORT)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp getFileHeader() {
        List<HeadDMapToF> headDMapToFList = new ArrayList<>();
        for (int i = 0; i < FileHeader.BLACKLIST.length; i++) {
            HeadDMapToF headDMapToF = new HeadDMapToF();
            headDMapToF.setDataHeadInfo(new HeadInfo(i, FileHeader.BLACKLIST[i]));
            headDMapToFList.add(headDMapToF);
        }

        return JsonResp.success(headDMapToFList);
    }
    @POST
    @Path(Keys.INFO_BLACKLIST_EXPORT)
    public JsonResp export(@Valid PageReqt req){
        try {
            userTaskService.addExportTask(MosBizDataType.Front_BlackList_Exp,convertExportData(req));
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.EXPORT,"【黑名单】","Public","Login",""); //添加访问日志
            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    private UserTask convertImportData(ImportRequest req) {
        SimpleUser user = SessionUtil.getCurUser();
        Integer enterpriseId = user.getEnterpriseId();
        List<UserTaskParameter> list = new ArrayList<>();
        list.add(new UserTaskParameter("phone","0"));
        list.add(new UserTaskParameter("remark","3"));
        list.add(new UserTaskParameter("name","2"));
        list.add(new UserTaskParameter("type","1"));
        list.add(new UserTaskParameter("_username",user.getUsername()));
        list.add(new UserTaskParameter("_enterprise_id",String.valueOf(enterpriseId)));
        list.add(new UserTaskParameter("_enterprise_name",user.getEnterpriseName()));
        String parameterStr = JSONObject.toJSON(list).toString();
        UserTask userTask = new UserTask();
        userTask.setMosParameters(parameterStr);
        userTask.setCreateUser(user.getId());
        userTask.setUploadFileName(req.getFileName());
        userTask.setUploadFileAddress(req.getPath().substring(0, req.getPath().lastIndexOf("/")));
        userTask.setFileSeparator(req.getDelimiter());
        return userTask;
    }
    private UserTask convertExportData(PageReqt req) {
        SimpleUser user = SessionUtil.getCurUser();
        Integer enterpriseId = user.getEnterpriseId();
        List<UserTaskParameter> list = new ArrayList<>();
        list.add(new UserTaskParameter("phone", (String) req.getParams().get("phone")));
        list.add(new UserTaskParameter("user_name",(String) req.getParams().get("user_name")));
        list.add(new UserTaskParameter("type",req.getParams().get("type")+""));
        list.add(new UserTaskParameter("enterprise_id",String.valueOf(enterpriseId)));
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

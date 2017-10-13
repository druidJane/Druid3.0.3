package com.xuanwu.mos.rest.resource.informationmt;

/**企业白名单
 * Created by zhangz on 2017/3/23.
 */

import com.alibaba.fastjson.JSONObject;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.UserTask;
import com.xuanwu.mos.domain.entity.WhitePhone;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.MosBizDataType;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.file.*;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.request.ImportRequest;
import com.xuanwu.mos.rest.service.UserTaskService;
import com.xuanwu.mos.rest.service.WhitePhoneService;
import com.xuanwu.mos.service.FtpService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.UserTaskParameter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Component
@Path(Keys.INFO_WITELISTWEB)
public class WhiteListResource {
    @Autowired
    protected WhitePhoneService whitePhoneService;
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private FileImporter fileImporter;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private SysLogService sysLogService;
    @POST
    @Path(Keys.INFO_WITELISTWEB_LIST)
    public JsonResp list(@Valid PageReqt req){
        QueryParameters params = new QueryParameters(req);
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("enterpriseId",user.getEnterpriseId());
        String beginTime = (String)req.getParams().get("_gt_begintime");
        String endTime = (String)req.getParams().get("_lt_endtime");
        if(StringUtils.isNotEmpty(beginTime)&&StringUtils.isNotEmpty(endTime)){
            if(DateUtil.parseDate(beginTime).getTime()>DateUtil.parseDate(endTime).getTime()){
                return JsonResp.fail("开始时间不能大于结束时间！");
            }
        }
        int total = whitePhoneService.findWhitePhoneCount(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }
        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        List<WhitePhone> list = whitePhoneService.findWhitePhonelists(params);
        return PageResp.success(total, list);
    }
    @POST
    @Path(Keys.INFO_WITELISTWEB_ADD)
    public JsonResp add(@Valid WhitePhone whitePhone){
        if(StringUtils.isEmpty(whitePhone.getTelphone())){
            return JsonResp.fail(WebConstants.WhitePhoneResuleCode.WHITEPHONE_IS_NULL, Messages.WHITEPHONE_IS_NULL);
        }
        SimpleUser  user = SessionUtil.getCurUser();
        WhitePhone add = new WhitePhone();
        add.setTelphone(whitePhone.getTelphone());
        QueryParameters params = new QueryParameters();
        params.addParam("telphone",add.getTelphone());
        params.addParam("enterpriseId",user.getEnterpriseId());
        int count = whitePhoneService.findWhitePhoneCount(params);
        if(count>0){
            return JsonResp.fail(WebConstants.WhitePhoneResuleCode.WHITEPHONE_RESULT_EXIST, Messages.WHITEPHONE_EXIST);
        }
        try {
            add.setIsNotice(WhitePhone.NoticeType.ENIMPORTED.getValue());
            add.setCreateTime(new Date());

            add.setEnterpriseId(user.getEnterpriseId());
            whitePhoneService.addWhitePhone(add);
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.NEW,"【企业白名单】","Public","Login","【" + whitePhone.getTelphone() + "】"); //添加访问日志
            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.INFO_WITELISTWEB_DEL)
    public JsonResp del(Integer[] ids){
        SimpleUser user = SessionUtil.getCurUser();
        try {
        	StringBuilder _name = new StringBuilder();
        	String name = "";
            for(Integer id:ids){
            	QueryParameters par =new QueryParameters();
                par.addParam("id", id);
                if(whitePhoneService.findWhitePhoneCount(par)>0){
                	_name.append(whitePhoneService.findWhitePhonelists(par).get(0).getTelphone()+",");
                }
            	
                whitePhoneService.delWhitePhone(id,user.getEnterpriseId());
            }
            
            if(_name.length()>0){
            	name = _name.substring(0,_name.length()-1);
            }
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.DELETE,"【企业白名单】",
                    "Public","Login","【"+ LogContentUtil.format(name)+"】"); //添加访问日志
            
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    @GET
    @Path(Keys.INFO_WITELISTWEB_IMPORT)
    public JsonResp getFileHeader(){
        List<HeadDMapToF> headDMapToFList = new ArrayList<>();
        for (int i = 0; i < FileHeader.WHITEPHONE.length; i++) {
            HeadDMapToF headDMapToF = new HeadDMapToF();
            headDMapToF.setDataHeadInfo(new HeadInfo(i, FileHeader.WHITEPHONE[i]));
            headDMapToFList.add(headDMapToF);
        }
        return JsonResp.success(headDMapToFList);
    }
    @POST
    @Path(Keys.INFO_WITELISTWEB_IMPORT)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public JsonResp upload(@Context HttpServletRequest request){
        //先将文件上传到本地服务器，并根据类型获取相应handler，解析校验头文件分隔符
        UploadResult result = fileImporter.upload(BizDataType.WhitePhone, request);
        StatusCode statusCode = result.getStatusCode();
        if (statusCode != StatusCode.Success) {
            return JsonResp.fail(statusCode.getStateDesc());
        }
        try {
            //上传文件到FTP
            String path = FileUtil.getDataServerPath(MosBizDataType.Front_WhiteListWeb_Imp);
            result.setPath(path);
            ftpService.uploadFile(path + result.getFileName(),result.getIs());
            return JsonResp.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
    }
    @POST
    @Path(Keys.INFO_WITELISTWEB_IMPORT)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonResp doImport(@Valid ImportRequest req){
        UserTask userTask = convertImportData(req);
        //插库
        try {
            userTaskService.addImportTask(MosBizDataType.Front_WhiteListWeb_Imp,userTask);
            
            SimpleUser loginUser = SessionUtil.getCurUser();
            sysLogService.addLog(loginUser,OperationType.IMPORT,"【企业白名单】","Public","Login",""); //添加访问日志
            
            return JsonResp.success();
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
    }

    private UserTask convertImportData(ImportRequest req) {
        SimpleUser user = SessionUtil.getCurUser();
        Integer enterpriseId = user.getEnterpriseId();
        List<UserTaskParameter> list = new ArrayList<>();
        list.add(new UserTaskParameter("telphone","0"));
        list.add(new UserTaskParameter("_enterprise_id",String.valueOf(enterpriseId)));
        String parameterStr = JSONObject.toJSON(list).toString();
        UserTask userTask = new UserTask();
        userTask.setMosParameters(parameterStr);
        userTask.setCreateUser(user.getId());
        userTask.setUploadFileName(req.getFileName());
        userTask.setUploadFileAddress(req.getPath().substring(0, req.getPath().lastIndexOf("/")));
        userTask.setFileSeparator(req.getDelimiter());
        return userTask;
    }
}


package com.xuanwu.mos.rest.resource.contactmgmt;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.executor.FileTaskExecutor;
import com.xuanwu.mos.file.FileHeader;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.HeadDMapToF;
import com.xuanwu.mos.file.HeadInfo;
import com.xuanwu.mos.file.mapbean.ContactMap;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.request.ImportRequest;
import com.xuanwu.mos.rest.service.*;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 个人通讯录
 */
@Component
@Path(Keys.CONTACTMGR_PCONTACT)
public class PContactResource {
    @Autowired
    private FileImporter fileImporter;
    @Autowired
    private ContactService cs;
    @Autowired
    private ContactGroupService cgs;
    @Autowired
    private Config config;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private FileTaskExecutor executor;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private CarrierService carrierService;
    @Autowired
    private ContactShareGroupService contactShareGroupService;
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_IMPORTCONTACTS_TREE)
    public JsonResp getPTree(){
        SimpleUser user = SessionUtil.getCurUser();
        ContactGroup rootCg = cgs.findContactGroupById(1);
        QueryParameters params = new QueryParameters();
        params.addParam("entId",user.getEnterpriseId());
        params.addParam("userId",user.getId());
        params.addParam("type",rootCg.getType());
        List<ContactGroup> cgList = cgs.findContactGroup(params);
        cgList.add(rootCg);
        return JsonResp.success(cgList);
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_INDEX + "/getContactByGroup")
    public JsonResp getContactByGroup(@Valid PageReqt req){
        QueryParameters params = new QueryParameters(req);
        try {
            SimpleUser user = SessionUtil.getCurUser();
            int userId = user.getId();
            params.addParam("userId",userId);
            String beginTime = (String)req.getParams().get("_gt_beginDate");
            String endTime = (String)req.getParams().get("_lt_endDate");
            if(StringUtils.isNotEmpty(beginTime)&&StringUtils.isNotEmpty(endTime)){
                if(DateUtil.parseDate(beginTime).getTime()>DateUtil.parseDate(endTime).getTime()){
                    return JsonResp.fail("开始时间不能大于结束时间！");
                }
            }
            int total = cs.findContactsCount(params);
             if (total == 0) {
                return PageResp.emptyResult();
            }
            PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
            params.setPage(pageInfo);
            List<Contact> contacts = cs.findContacts(params);
            return PageResp.success(total, contacts);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_ADDCONTACTGROUP)
    public JsonResp addGroup(@Valid ContactGroup add){
        String checkResult = null;
        if (StringUtils.isBlank(add.getName())) {
            return JsonResp.fail("群组名称不能为空！");
        }
        SimpleUser user = SessionUtil.getCurUser();
        ContactGroup group = cgs.findContactGroupById(add.getParentId());
        if (group == null) {
            return JsonResp.fail();
        }
        checkResult = this.checkContactGroupOwner(group.getId());
        if (checkResult != null) {
            return JsonResp.fail(checkResult);
        }
        int gid = group.getId();
        int type = group.getType();
        if (this.checkContactGroupExist(add.getName(), 0, gid, type)) {
            return JsonResp.fail("你的通讯录群组当中已经存在【" + add.getName() + "】！");
        }
        int maxSize = config.getMaxTreeDeep();
        String path = add.getPath();
        int size = path.split(Pattern.quote(Delimiters.DOT)).length;
        if (size >= maxSize) {
            return JsonResp.fail("每个通讯录群组最多只能添加" + maxSize + "级子群组！");
        }
        /*if (path.equals("1.")) {// 个人通讯录
            path = user.getId() + ".";
        }*/
        int userId = user.getId();
        int entId = user.getEnterpriseId();
        ContactGroup cg = new ContactGroup();
        cg.setParentId(gid);
        cg.setName(add.getName());
        cg.setUserId(userId);
        cg.setEnterpriseId(entId);
        cg.setType(type);
        cg.setPath(path);
        try {
            cgs.insertContactGroup(cg);
            sysLogService.addLog(user,OperationType.NEW,"【通讯录管理】/【个人通讯录】/【通讯录群组名称】","Public","Login","【" + add.getName() + "】"); //添加访问日志
            
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_UPDATECONTACTGROUP)
    public JsonResp updateGroup(@Valid ContactGroup update){
        String checkResult = JsonUtil.toJSON(-1);
        String name = update.getName();
        if (StringUtils.isBlank(name)) {
            return JsonResp.fail("群组名称不能为空！");
        }
        ContactGroup cg = cgs.findContactGroupById(update.getId());
        if (cg == null || cg.getId() <= 2) {
            return JsonResp.fail();
        }
        checkResult = this.checkContactGroupOwner(cg.getId());
        if (checkResult != null) {
            return JsonResp.fail(checkResult);
        }
        boolean exist = this.checkContactGroupExist(name, update.getId(), cg.getParentId(),
                cg.getType());
        if (exist) {
            return JsonResp.fail("你的通讯录群组当中已经存在【" + name + "】！");
        }
        cg.setName(name);
        try {
            cgs.updateContactGroup(cg);
            
            SimpleUser user = SessionUtil.getCurUser();
            sysLogService.addLog(user,OperationType.MODIFY,"【通讯录管理】/【个人通讯录】/【通讯录群组名称】","Public","Login","【"+name+"】"); //添加访问日志
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_DELETECONTACTGROUP)
    public JsonResp deleteGroup(@Valid ContactGroup del){
        String checkResult = null;
        ContactGroup cg = cgs.findContactGroupById(del.getId());
        if (cg == null || cg.getId() <= 2) {
            return JsonResp.fail();
        }
        checkResult = this.checkContactGroupOwner(cg.getId());
        if (checkResult != null) {
            return JsonResp.fail(checkResult);
        }
        String path = cg.getPath();
        try {
            cgs.deleteContactGroupsByPath(path,cg);
            QueryParameters params = new QueryParameters();
            params.addParam("groupId",cg.getId());
            params.addParam("userId",cg.getUserId());
            List<ContactShareGroup> list = contactShareGroupService.findContactShareGroups(params);
            if(list != null && !list.isEmpty()){
                for(ContactShareGroup csg:list){
                    contactShareGroupService.delete(csg);
                }
            }
            SimpleUser user = SessionUtil.getCurUser();
            sysLogService.addLog(user,OperationType.DELETE,"【通讯录管理】/【个人通讯录】/【通讯录群组名称】","Public","Login","【"+cg.getName()+"】"); //添加访问日志
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_ADDCONTACT)
    public JsonResp addContact(@Valid Contact add){
        String checkResult = JsonUtil.toJSON(-1);
        if (add == null || StringUtils.isEmpty(add.getPhone())) {
           return JsonResp.fail();
        }
        String teleSeg = add.getPhone().substring(0, 4);
        long count = carrierService.findTeleSegCount(teleSeg);
        if (count <= 0) {
            teleSeg = add.getPhone().substring(0, 3);
            count = carrierService.findTeleSegCount(teleSeg);
        }
        if (count <= 0) {
            return JsonResp.fail(WebConstants.BlacklistResultCode.PHONE_RESULT_NO_CARRIER_TELESEG,Messages.PHONE_RESULT_NO_CARRIER_TELESEG);
        }

        //判断所输入的电话号码是否附近需求——小灵通为10至12位，其余为11位 by jiangziyuan
        String tempPhoneTop = add.getPhone().substring(0, 1);
        String tempPhone = add.getPhone();
        if (tempPhoneTop.equals("0") && tempPhone.length() <= 10 && tempPhone.length() >= 12) {
            return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
        } else if(!tempPhoneTop.equals("0") && tempPhone.length() != 11) {
            return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
        }

        checkResult = this.checkContactGroupOwner(add.getGroupId());
        if (checkResult != null) {
            return JsonResp.fail(checkResult);
        }
        String phone = StringUtil.removeZhCnCode(add.getPhone());
        add.setPhone(phone);

        if (cs.checkContactExist(add.getPhone(), add.getGroupId())) {
            return JsonResp.fail("你已经在当前群组添加过该号码了！");
        }

        try {
            cs.insertContact(add);
            
            SimpleUser user = SessionUtil.getCurUser();
            sysLogService.addLog(user,OperationType.NEW,"【通讯录管理】/【个人通讯录】/【通讯录联系人】","Public","Login","【" + add.getName() + "】"); //添加访问日志
            
            return JsonResp.success();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_UPDATECONTACT)
    public JsonResp updateContact(@Valid Contact update){
        String checkResult = null;
        if (update == null || StringUtils.isEmpty(update.getPhone())) {
            return JsonResp.fail("请选择你所要修改的联系人！");
        }
        String teleSeg = update.getPhone().substring(0, 4);
        long count = carrierService.findTeleSegCount(teleSeg);
        if (count <= 0) {
            teleSeg = update.getPhone().substring(0, 3);
            count = carrierService.findTeleSegCount(teleSeg);
        }
        if (count <= 0) {
            return JsonResp.fail(WebConstants.BlacklistResultCode.PHONE_RESULT_NO_CARRIER_TELESEG,Messages.PHONE_RESULT_NO_CARRIER_TELESEG);
        }

        //判断所输入的电话号码是否附近需求——小灵通为10至12位，其余为11位 by jiangziyuan
        String tempPhoneTop = update.getPhone().substring(0, 1);
        String tempPhone = update.getPhone();
        if (tempPhoneTop.equals("0") && tempPhone.length() <= 10 && tempPhone.length() >= 12) {
            return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
        } else if(!tempPhoneTop.equals("0") && tempPhone.length() != 11) {
            return JsonResp.fail(WebConstants.BlacklistResultCode.WRONG_PHONE_LENGTH,Messages.WRONG_PHONE_LENGTH);
        }

        Contact exist = cs.findContactById(update.getId());
        checkResult = this.checkContactGroupOwner(update.getGroupId());
        if (exist == null || checkResult != null) {
            return JsonResp.fail();
        }

        String phone = StringUtil.removeZhCnCode(update.getPhone());
        update.setPhone(phone);

        if (!update.getPhone().equals(exist.getPhone())
                && cs.checkContactExist(update.getPhone(), update.getGroupId())) {
            return JsonResp.fail("你已经在当前群组添加过该号码了！");
        }

        update.setGroupId(exist.getGroupId());// 防止被篡改
        try {
            cs.updateContact(update);
            SimpleUser user = SessionUtil.getCurUser();
            sysLogService.addLog(user,OperationType.MODIFY,"【通讯录管理】/【个人通讯录】/【通讯录联系人】","Public","Login","【"+update.getName()+"】"); //添加访问日志
            
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_DELETECONTACT)
    public JsonResp deleteContact(Integer[] ids){
        String checkResult = null;
        if (ids==null || ids.length<1) {
            return JsonResp.fail("请选择你所要删除的联系人！");
        }
        List<Integer> _ids = new ArrayList<>();
        StringBuilder _names = new StringBuilder();
        for (int id : ids) {
            Contact c = cs.findContactById(id);
            if (c != null) {
                checkResult = this.checkContactGroupOwner(c.getGroupId());
                if (checkResult == null) {
                    _ids.add(id);
                    _names.append(c.getName()+",");
                }
            }
        }
        try {
            QueryParameters params = new QueryParameters();
            params.addParam("ids",_ids);
            cs.deleteContact(params);
            SimpleUser user = SessionUtil.getCurUser();
            String logContent = _names.substring(0,_names.length()-1);
            sysLogService.addLog(user,OperationType.DELETE,"【通讯录管理】/【个人通讯录】/【通讯录联系人】",
                    "Public","Login","【"+LogContentUtil.format(logContent)+"】"); //添加访问日志
            
            return JsonResp.success();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
    }
    /**
     * 获取导入文件内容头格式
     * @return
     */
    @GET
    @Path(Keys.CONTACTMGR_PCONTACT_IMPORTCONTACTS)
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp getFileHeader() {
        List<HeadDMapToF> headDMapToFList = new ArrayList<>();
        for (int i = 0; i < FileHeader.CONTACT_IMPORT.length; i++) {
            HeadDMapToF headDMapToF = new HeadDMapToF();
            headDMapToF.setDataHeadInfo(new HeadInfo(i, FileHeader.CONTACT_IMPORT[i]));
            headDMapToFList.add(headDMapToF);
        }

        return JsonResp.success(headDMapToFList);
    }
    /**
     * 上传
     */
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_IMPORTCONTACTS)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public JsonResp upload(@Context HttpServletRequest request) {
        UploadResult result = fileImporter.upload(BizDataType.Contact, request);
        StatusCode statusCode = result.getStatusCode();
        if (statusCode != StatusCode.Success) {
            return JsonResp.fail(statusCode.getStateDesc());
        }
        return JsonResp.success(result);
    }
    /**
     * 导入
     */
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_IMPORTCONTACTS)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonResp doImport(@Valid ImportRequest req) {
        try {
            if (!req.isCorrectFileHead()) {
                return JsonResp.fail(-1, Messages.INCORRECT_FILE_COLUMN);
            }
            SimpleUser curUser = SessionUtil.getCurUser();
            // 构造文件列映射
            List<HeadDMapToF> headDMapToFList = req.getHeadDMapToFList();
            ContactMap contactMap = new ContactMap();

            for (HeadDMapToF headDMapToF : headDMapToFList) {
                //{ "手机号码", "姓名", "性别", "Vip", "出生日期", "编号", "备注" };
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.CONTACT_IMPORT[0])) {
                    contactMap.setPhone(headDMapToF.getFileHeadInfo().getIndex());
                }
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.CONTACT_IMPORT[1])) {
                    contactMap.setName(headDMapToF.getFileHeadInfo().getIndex());
                }
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.CONTACT_IMPORT[2])) {
                    contactMap.setSex(headDMapToF.getFileHeadInfo().getIndex());
                }
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.CONTACT_IMPORT[3])) {
                    contactMap.setVip(headDMapToF.getFileHeadInfo().getIndex());
                }
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.CONTACT_IMPORT[4])) {
                    contactMap.setBirthday(headDMapToF.getFileHeadInfo().getIndex());
                }
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.CONTACT_IMPORT[5])) {
                    contactMap.setIdentifier(headDMapToF.getFileHeadInfo().getIndex());
                }
                if (headDMapToF.getDataHeadInfo().getName().equals(FileHeader.CONTACT_IMPORT[6])) {
                    contactMap.setRemark(headDMapToF.getFileHeadInfo().getIndex());
                }
            }
            FileTask task = new FileTask();
            task.setFileName(req.getFileName());
            task.setTaskName("个人通讯录导入");
            task.setType(TaskType.Import);
            task.setDataType(BizDataType.Contact);
            task.setPostTime(new Date());
            task.setUserId(curUser.getId());
            task.setFileSize(req.getFileSize());
            task.setPlatformId(config.getPlatformId());
            Map<String, String> params = new HashMap<>();
            params.put("userId", String.valueOf(curUser.getId()));
            params.put("entId", String.valueOf(curUser.getEnterpriseId()));
            params.put("delimeter", req.getDelimiter());
            params.put("contactMap", contactMap.tran2Params());
            params.put("groupId", String.valueOf(req.getParams().get("groupId")));
            task.setParamsMap(params);
            task.setHanldePercent(0);
            task.setState(TaskState.Wait);
            fileTaskService.save(task);
            executor.putTask2Queue(task);
            
            SimpleUser user = SessionUtil.getCurUser();
            sysLogService.addLog(user,OperationType.IMPORT,"【个人通讯录】/【通讯录联系人】","Public","Login",""); //添加访问日志
            
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    @POST
    @Path(Keys.CONTACTMGR_PCONTACT_EXPORTCONTACTS)
    public JsonResp export(@Valid ImportRequest req){
        SimpleUser curUser = SessionUtil.getCurUser();
        try {
            Map<String, Object> map = req.getParams();
            String fileName = (String) map.get("fileName");
            // 构造参数，String类型默认值为""，数字默认为-1
            String delimiter = (String) map.get("delimiter");
            Map<String, Object> params = new HashMap<>();

            if (map.get("_lk_name") != null) {
                params.put("_lk_name", map.get("_lk_name"));
            } else {
                params.put("_lk_name", "");
            }
            if (map.get("name") != null) {
                params.put("name", map.get("name"));
            } else {
                params.put("name", "");
            }
            if (map.get("_lk_phone") != null) {
                params.put("_lk_phone", map.get("_lk_phone"));
            } else {
                params.put("_lk_phone", "");
            }
            if (map.get("sex") != null) {
                params.put("sex", map.get("sex"));
            } else {
                params.put("sex", "");
            }
            if (map.get("showChild") != null) {
                params.put("showChild", map.get("showChild"));
            } else {
                params.put("showChild", "");
            }
            if (map.get("_gt_beginDate") != null) {
                params.put("_gt_beginDate", map.get("_gt_beginDate"));
            } else {
                params.put("_gt_beginDate", "");
            }
            if (map.get("_lt_endDate") != null) {
                params.put("_lt_endDate", map.get("_lt_endDate"));
            } else {
                params.put("_lt_endDate", "");
            }
            if (map.get("vip") != null) {
                params.put("vip", map.get("vip"));
            } else {
                params.put("vip", "");
            }
            if (map.get("groupId") != null) {
                params.put("groupId", map.get("groupId"));
            } else {
                params.put("groupId", "");
            }
            if (map.get("path") != null) {
                params.put("path", map.get("path"));
            } else {
                params.put("path", "");
            }
            if (map.get("_lk_identifier") != null) {
                params.put("_lk_identifier", map.get("_lk_identifier"));
            } else {
                params.put("_lk_identifier", "");
            }
            params.put("delimiter", delimiter);
            FileTask task = new FileTask();
            task.setFileName(fileName);
            if(map.get("name")!=null && !map.get("name").equals("")){
   	        	task.setTaskName("个人通讯录导出"+"("+map.get("name")+")");
   	        }else{
   	        	task.setTaskName("个人通讯录导出");
   	        }  

            task.setType(TaskType.Export);
            task.setDataType(BizDataType.Contact);
            task.setPostTime(new Date());
            task.setUserId(curUser.getId());
            task.setFileSize(0l);
            task.setHanldePercent(0);
            task.setState(TaskState.Wait);
            task.setPlatformId(config.getPlatformId());
            task.setParameters(XmlUtil.toXML(params));
            fileTaskService.save(task);
            executor.putTask2Queue(task);
            
            SimpleUser user = SessionUtil.getCurUser();
            sysLogService.addLog(user,OperationType.EXPORT,"【个人通讯录】/【通讯录联系人】","Public","Login",""); //添加访问日志
            
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success();
    }
    /**
     * 检测通讯录群组是否已有同名
     *
     * @param name
     *            需要检测的名称
     * @param id
     *            需要检测的ID
     * @param parentId
     *            需要检测的父级ID
     * @param type
     *            类型(0:个人通讯录,1:企业通讯录)
     * @return
     */
    private boolean checkContactGroupExist(String name, int id, int parentId,
                                           int type) {
        SimpleUser user = SessionUtil.getCurUser();
        int entId = user.getEnterpriseId();
        int userId = user.getId();
        List<ContactGroup> cgList = cgs.findContactGroupByName(name, entId, userId,
                type);
        for (ContactGroup cg : cgList) {
            if (cg.getParentId() == parentId && id != cg.getId()) {
                return true;
            }
        }
        return false;
    }
    /**
     * 校验通讯录群组所属人
     *
     * @param groupId
     * @return
     */
    private String checkContactGroupOwner(int groupId) {
        ContactGroup group = cgs.findContactGroupById(groupId);
        if (group == null) {
            return JsonUtil.toJSON(-1);
        }

        SimpleUser user = SessionUtil.getCurUser();
        int userId = user.getId();
        int eid = user.getEnterpriseId();
        int id = group.getId();
        int type = group.getType();
        if (id > 2 && type == 0 && group.getUserId() != userId) {
            return JsonUtil.toJSON("嘿，这貌似不是你的通讯录群组！", -1);
        } else if (id > 2 && group.getEnterpriseId() != eid) {
            return JsonUtil.toJSON("嘿，这貌似不是你们企业的通讯录群组！", -1);
        }
        return null;
    }
}

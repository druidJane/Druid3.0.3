package com.xuanwu.mos.rest.resource;


import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.file.FileHelper;
import com.xuanwu.mos.file.FileImporter;
import com.xuanwu.mos.file.uploader.UploadResult;
import com.xuanwu.mos.rest.resource.contactmgmt.EContactResource;
import com.xuanwu.mos.rest.resource.contactmgmt.PContactResource;
import com.xuanwu.mos.rest.service.*;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.service.*;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.ContentVo;
import com.xuanwu.mos.vo.MmsInfoVo;
import com.xuanwu.mos.vo.MsgVo;
import com.xuanwu.mos.vo.ReturnFileData;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * CommonResource
 *
 * @author <a href="mailto:jiji@javawind.com">XueFang.Xu</a>
 * @version 1.0.0
 * @date 2016-08-11
 */
@Component
@Path("common")
public class CommonResource {
    private Logger logger = LoggerFactory.getLogger(CommonResource.class);
    private static int defaultFetchSize = 10;

    @Autowired
    private UserService userService;
    @Autowired
    private Config config;
    @Autowired
    private KeyWordService keyWordService;
    @Autowired
    private CarrierService carrierService;
    @Autowired
    private AutoCompleteService autoCompleteService;
    @Autowired
    private BizTypeService bizTypeService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private FileImporter importer;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserMgrService userMgrService;
    @Autowired
    private FileHelper fileHelper;
    @Autowired
    private PlatformMode platformMode;
    @Autowired
    private MsgPackService msgPackService;
    @Autowired
    private BaseMsgResource baseMsgResource;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private PContactResource pContactResource;
    @Autowired
    private EContactResource eContactResource;

    @Autowired
    private ContactService cs;
    @Autowired
    private ContactGroupService cgs;
    @Autowired
    private ContactShareGroupService csgs;

    @Autowired
    private SysLogService sysLogService;

	private static final FastDateFormat sdf = FastDateFormat.getInstance(
			"yyyy-MM-dd HH:mm:ss");

    @POST
    @Path("checkLogin")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp checkLogin() {
        if (SessionUtil.isLogin()) {
            //登录成功之后清空消息通知版本号，强制重新加载数据
            SimpleUser curUser = SessionUtil.getCurUser();
            noticeService.resetUserNoticeVersion(curUser.getId());
            fileTaskService.resetUserUpAndLoadVersion(curUser.getId());
            return JsonResp.success(curUser.getUsername());
        }
        return JsonResp.fail(Messages.LOGIN_TIMEOUT);
    }

    @Path("menu")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp menu() {
        // 菜单
        Map<String, List<?>> resultMap = SessionUtil.getMenus();
        return JsonResp.success(resultMap);
    }

    /**
     * 获取未读消息数量
     * 以及最新前5条用于显示
     * @return
     */
    @POST
    @Path("notice/unReadNoticeCount")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getUnReadNoticeCount() {
        SimpleUser simpleUser = SessionUtil.getCurUser();
        int user = simpleUser.getId();
        AtomicInteger curVer = noticeService.getUserNoticeCurVersion(user);
        AtomicInteger preVer = noticeService.getUserNoticePreVersion(user);
        int count = -1;
        Collection<Notice> noticeList = null;
        if (curVer.intValue() > preVer.intValue()) {
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.addParam("enterpriseId", simpleUser.getEnterpriseId());
            queryParameters.addParam("userId", simpleUser.getId());
            queryParameters.addParam("state", NoticeState.UNREAD.getIndex());
            count = noticeService.count(queryParameters);

            PageInfo pageInfo = new PageInfo(1, 10, 10);
            queryParameters.setPage(pageInfo);
            noticeList = noticeService.list(queryParameters);
            //更新版本
            preVer.getAndSet(curVer.intValue());
        }

        return PageResp.success(count,noticeList);
    }


    /**
     * 获取个人公告已读未读
     * 以及最新前5条用于显示
     * @return
     */
    @POST
    @Path("notice/getAnnouncementList")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getAnnouncementList() {
        SimpleUser simpleUser = SessionUtil.getCurUser();
        Collection<Notice> noticeList = null;
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.addParam("enterpriseId", simpleUser.getEnterpriseId());
        queryParameters.addParam("userId", simpleUser.getId());
        queryParameters.addParam("msgType",NoticeMsgType.SYS_NOTICE.getIndex());
        PageInfo pageInfo = new PageInfo(1, 5, 5);
        queryParameters.setPage(pageInfo);
        noticeList = noticeService.list(queryParameters);
        return JsonResp.success(noticeList);
    }

    /**
     * 消息通知
     *
     * @return
     */
    @POST
    @Path("notice/list")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp noticeList(@Valid PageReqt req) {
        QueryParameters params = new QueryParameters(req);
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("userId", user.getId());
        params.addParam("enterpriseId", user.getEnterpriseId());
        int total = noticeService.count(params);
        if (total == 0) {
            return PageResp.emptyResult();
        }

        PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
        params.setPage(pageInfo);
        Collection<Notice> results = noticeService.list(params);
        return PageResp.success(total, results);
    }


    /**
     * 按当前查询条件修改消息状态为已读
     *
     * @return
     */
    @POST
    @Path("notice/updateStateByParams")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp updateStateByParams(@Valid PageReqt req) throws RepositoryException {
        QueryParameters params = new QueryParameters(req);
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("userId", user.getId());
        params.addParam("enterpriseId", user.getEnterpriseId());
        Collection<Notice> results = noticeService.list(params);
        int total = 0;
        if (results == null || results.size() == 0) {
            return JsonResp.success(0);
        }
        List ids = new ArrayList<Integer>();
        for (Notice notice : results) {
            ids.add(notice.getId());
        }
        total = noticeService.updateStateByIds(ids);
        return JsonResp.success(total);
    }

    /**
     * 按选中按钮逻辑删除消息
     *
     * @return
     */
    @POST
    @Path("notice/del")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp delNotice(@Valid List<Integer> ids) throws RepositoryException {
        int total = noticeService.deleteDetailByIds(ids);
        return JsonResp.success(total);
    }

    /**
     * 按选中按钮修改消息状态为已读
     *
     * @return
     */
    @POST
    @Path("notice/updateStateByIds")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp updateStateByIds(List<Integer> ids) throws RepositoryException {
        Integer result = noticeService.updateStateByIds(ids);
        return JsonResp.success(result);
    }

    /**
     * 获取未审核数量
     *
     * @return
     */
    @POST
    @Path("notice/unAuditNoticeCount")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp unAuditNoticeCount(@Context HttpServletRequest request) {

        QueryParameters params4sms = new QueryParameters();
        String url = UrlResourceUtil.handleUrlResource(Keys.SMSMGR_SENDPENDING, Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS);
        DataScope dataScope = SessionUtil.getDataSope(url);
        params4sms = baseMsgResource.checkUserDataScope(params4sms,dataScope);
        params4sms.addParam("subDept", false);

//        params4sms.addParam("beginTime", getCurMonthFirstDayTime());
//        params4sms.addParam("endTime",DateUtil.defaultFormat.format(new Date()));

        params4sms.addParam("msgType", 1);
        int smsAuditCount = msgPackService.findMmsWaitAuditPacksCount(params4sms);

        QueryParameters params4mms = new QueryParameters();
        params4mms.addParam("subDept", false);
        String urlmms = UrlResourceUtil.handleUrlResource(Keys.SMSMGR_SENDPENDINGMMS, Keys.SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS);
        DataScope dataScopemms = SessionUtil.getDataSope(urlmms);
        params4mms = baseMsgResource.checkUserDataScope(params4sms,dataScopemms);

//        params4mms.addParam("beginTime", getCurMonthFirstDayTime());
//        params4mms.addParam("endTime", DateUtil.defaultFormat.format(new Date()));  
        params4mms.addParam("msgType", 2);
        int mmsAuditCount = msgPackService.findMmsWaitAuditPacksCount(params4mms);
        HashMap<String,Integer> result = new HashMap<>();
        result.put("smsAuditCount",smsAuditCount);
        result.put("mmsAuditCount",mmsAuditCount);
        return JsonResp.success(result);
    }
	public static String getCurMonthFirstDayTime() {
		Calendar begin = Calendar.getInstance();
		if (begin.get(Calendar.DAY_OF_MONTH) == 1) {
			begin.set(Calendar.MONTH, begin.get(Calendar.MONTH) - 1);
		}
		begin.set(Calendar.DAY_OF_MONTH, 1);
//		begin.set(Calendar.HOUR_OF_DAY, 0);
//		begin.set(Calendar.MINUTE, 0);
//		begin.set(Calendar.SECOND, 0);
		return sdf.format(begin.getTime());


	}
    /**
     * 根据消息类型获取消息内容
     *
     * @return
     */
    @POST
    @Path("notice/getMessageContent")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getMessageContent(Map<String,Object> para) throws RepositoryException {
        QueryParameters qp = new QueryParameters();
        Parameters parameters = new Parameters(qp);
        Date pushTime = new Date((Long)para.get("pushTime"));
        parameters.setQueryTime(pushTime);
        String objectId = (String)para.get("objectId");
        qp.addParam("packId",objectId);
        int messageType = (Integer)para.get("messageType");
        if (messageType == 1) {//短信发送详情
            MsgPack pack =  msgPackService.findMsgPackById(objectId, MsgTypeEnum.SMS.getIndex(),pushTime);
            return JsonResp.success(pack);
        } else if (messageType == 2) {//彩信发送详情
            parameters.setMsgType(MsgTypeEnum.SMS.getIndex());
            MmsMsgPack pack =  msgPackService.findMmsMsgPackById(parameters);
            return JsonResp.success(pack);
        } else if (messageType == 3) {//短信审核详情
            parameters.setMsgType(1);
            MsgPack pack =  msgPackService.findMsgPackById(objectId, MsgTypeEnum.SMS.getIndex(),pushTime);
            return JsonResp.success(pack);
        } else if (messageType == 4) {//彩信审核详情
            parameters.setMsgType(MsgTypeEnum.SMS.getIndex());
            MmsMsgPack pack =  msgPackService.findMmsMsgPackById(parameters);
            return JsonResp.success(pack);
        } else {//系统公告
            String noticeMsg = announcementService.findAnnouncementById(Integer.valueOf(objectId)).getContent();
            return JsonResp.success(noticeMsg);
        }
    }
    @POST
    @Path("notice/getNoticeContent")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getMessageContent(int objectId) throws RepositoryException {
    	Announcement ann =  announcementService.findAnnouncementById(Integer.valueOf(objectId));
    	Map<String,String> map = new HashMap<>();
    	map.put("content", ann.getContent());
    	map.put("updateTime", DateUtil.defaultFormat.format(ann.getUpdateTime()));
    	return JsonResp.success(map);
    }

    /**
     * 获取未读消息数量
     *
     * @return
     */
    @POST
    @Path("upAndLoadCount")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getUpAndLoadCount() {
        SimpleUser simpleUser = SessionUtil.getCurUser();
        int user = simpleUser.getId();
        AtomicInteger curVer = fileTaskService.getUserUpAndLoadCurVersion(user);
        AtomicInteger preVer = fileTaskService.getUserUpAndLoadPreVersion(user);
        List<Integer> result = null;
        if (curVer.intValue() > preVer.intValue()) {
            result = fileTaskService.findResultCountByUserId(simpleUser.getId());
           //更新版本
            preVer.getAndSet(curVer.intValue());
        }

        return JsonResp.success(result);
    }

    /**
     * 修改未读消息数量
     *
     * @return
     */
    @POST
    @Path("updateUpAndLoadCount")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp updateUpAndLoadCount(Integer taskType) throws RepositoryException {
        SimpleUser simpleUser = SessionUtil.getCurUser();
        Integer result = fileTaskService.updateReadStateByUserId(simpleUser.getId(), taskType);
        return JsonResp.success(result);
    }

    /**
     * 获取账号基本信息
     *
     * @return
     */
    @POST
    @Path("acc/getAccountInfo")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getAccountInfo(){
        SimpleUser curUser = SessionUtil.getCurUser();
        SimpleUser account = userService.getAccountInfo(curUser.getId(),platformMode.getPlatform().getIndex());
        account.setLastLoginTime(curUser.getLastLoginTime());
        account.setBalance(userService.getExsitBindAccountOfUser(curUser.getId(), curUser.getEnterpriseId()));
        if(account.getIstest()==0 && account.getTestenddate()!=null){
    		int compareResult = DateUtil.compare_date(account.getTestenddate(), DateUtil.DATE_FORMAT_FORMAT.format(new Date()));
    		int remindDaysConfig = config.getExpirareminderdays();
    		int remindDays = DateUtil.getDays(DateUtil.DATE_FORMAT_FORMAT.format(new Date()), account.getTestenddate());
    		if(compareResult > 0 && remindDays <=remindDaysConfig){
    			account.setRemindflag(true);
    			account.setRemindDays(remindDays);
    		}
        }
        return JsonResp.success(account);
    }

    /**
     * 获取账号基本信息
     *
     * @return
     */
    @POST
    @Path("acc/updateAccInfo")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp updateAccInfo(@Valid Map<String,String> param) throws RepositoryException {
        SimpleUser curUser = SessionUtil.getCurUser();
        int result = userService.updateAccInfo(curUser.getId(),param.get("phone"),param.get("linkMan"));
        return JsonResp.success(result);
    }

    /**
     * 获取账号基本信息
     *
     * @return
     */
    @POST
    @Path("acc/updateAccPasswd")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp updateAccPasswd(@Valid Map<String,String> param) throws Exception {
        SimpleUser curUser = SessionUtil.getCurUser();
        PrivateKey keyPair = (PrivateKey)SessionUtil.getTokenVerify();
        String oldPasswd = param.get("oldPasswd");
        if (StringUtils.isBlank(oldPasswd)) {
            return JsonResp.fail(1,"旧密码不能为空,请重新输入");
        }
        oldPasswd = RSAUtil.decode(oldPasswd,keyPair);
        String mixOldPasswd = Md5Utils.mixLoginPasswd(oldPasswd,config.getMixKey());
        if (!mixOldPasswd.equals(curUser.getSecondPassword())) {
            return JsonResp.fail(1,"旧密码错误，请重新输入");
        }
        String newPasswd = param.get("newPasswd");
        if (StringUtils.isBlank(newPasswd)) {
            return JsonResp.fail(2,"新密码不能为空，请重新输入");
        }
        newPasswd = RSAUtil.decode(newPasswd,keyPair);
        String mixNewPasswd = Md5Utils.mixLoginPasswd(newPasswd,config.getMixKey());
        if (mixNewPasswd.equals(curUser.getPassword())) {
            return JsonResp.fail(2,"您当前修改的登录密码与发送密码一致，请用其他密码！");
        }
        String mixTransmitPasswd = AuthSecurityUtil.encrypt(newPasswd);
        if (mixTransmitPasswd.equals(curUser.getTransmitPassword())) {
            return JsonResp.fail(2,"您当前修改的登录密码与透传密码一致，请用其他密码！");
        }
        userService.updatePasswd(curUser.getId(),mixNewPasswd);
        sysLogService.addLog(curUser, OperationType.RESET_LOGIN_PWD,"系统","common","acc/updateAccPasswd",curUser.getUsername()+"修改登录密码"); //添加访问日志
        //同步更新session里保持的登录密码
        curUser.setSecondPassword(mixNewPasswd);
        return JsonResp.success();
    }


    /**
     * 找【业务类型】
     */
    @GET
    @Path("getBizTypes")
    public JsonResp getBizTypes() {
        SimpleUser loginUser = SessionUtil.getCurUser();
        List<BizType> bizs = userService.findBizByMsgType(loginUser.getId(),
                MsgContent.MsgType.LONGSMS.getIndex());
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (ListUtil.isNotBlank(bizs)) {
            HashSet<Integer> set = new HashSet<Integer>();
            int i = 0;
            for (BizType biz : bizs) {
                if (set.contains(biz.getId())) {
                    continue;
                }
                set.add(biz.getId());
                if (i > 0)
                    sb.append(Delimiters.COMMA);
                sb.append(biz.toJSONHtml());
                i++;
            }
        }
        sb.append(']');
        return JsonResp.success(sb.toString());
    }

    /**
     * 自动用户账号
     */
    @GET
    @Path("complete/fetchUserData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteUserName(@QueryParam("userName") String userName) {
        SimpleUser user = SessionUtil.getCurUser();
        List<Map<String, String>> map = autoCompleteService.autoCompleteUserName(userName, user.getEnterpriseId());
        return JsonResp.success(map);
    }

    /**
     * 自动补全企业账号
     */
    @POST
    @Path("complete/fetchEnterprise")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteDomain() {
        Map<String,String> enterprise = new HashMap<>();
        SimpleUser user = SessionUtil.getCurUser();
        enterprise.put("enterpriseId",String.valueOf(user.getEnterpriseId()));
        enterprise.put("enterpriseName",SessionUtil.getCurEnterprise().getEnterpriseName());
        return JsonResp.success(enterprise);
    }

    /**
     * 自动补全部门账号
     */
    @GET
    @Path("complete/fetchDepartment")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteDepartment(@Context HttpServletRequest request,@QueryParam("deptName") String deptName) {
        Map<String,String> department = new HashMap<>();
        SimpleUser user = SessionUtil.getCurUser();


        //获取企业根部门节点
        Department rootDept = userMgrService.
                getDeptByEntId(user.getEnterpriseId());
        String path = rootDept.getId() + Delimiters.DOT;
        List<Department> childDepartments = userMgrService.
                getChildDepartments(path);

       if(childDepartments!=null && childDepartments.size()>0){
    	   for(Department dept:childDepartments){
    		   department.put("deptId", String.valueOf(dept.getId()));
    		   department.put("deptName",dept.getDeptName());
    	   }
       }
        return JsonResp.success(childDepartments);
    }
    /**
     * 自动补全业务类型 企业级别
     */
    @GET
    @Path("complete/fetchBizTypeData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteBizType(@QueryParam("bizType") String bizType) {
        SimpleUser user = SessionUtil.getCurUser();

        List<Map<String, String>> map = autoCompleteService.acomplpleteBizType(user, bizType, DataScope.NONE, defaultFetchSize);
        return JsonResp.success(map);
    }
    /**
     * 自动补全业务类型 登录用户绑定的业务类型
     */
    @GET
    @Path("complete/fetchUserBindBizTypeData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteUserBindBizType(@QueryParam("bizType") String bizType) {
        SimpleUser user = SessionUtil.getCurUser();

        List<Map<String, String>> map = autoCompleteService.acomplpleteBizType(user, bizType, DataScope.PERSONAL, defaultFetchSize);
        return JsonResp.success(map);
    }
    /**
     * 自动补全业务类型 企业级别
     */
    @POST
    @Path("fetchBizTypeForMsg")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp fetchBizTypeForMsg(MsgVo msgVo) {
        SimpleUser user = SessionUtil.getCurUser();
        List<Map<String, String>> maps = autoCompleteService.acomplpleteBizType(user, null, DataScope.PERSONAL, defaultFetchSize);
        if (maps != null && maps.size() > 0) {
            List<Map<String, String>> resultMapList = new ArrayList<>();
            for (Map<String, String> map : maps) {
                Integer bizId = Integer.parseInt(map.get("id"));
                List<SpecsvsNumVo> specsvsNumVos = bizTypeService.getCarrierDetailByBizId(user.getEnterpriseId(), bizId, msgVo.getMsgType());
                if (specsvsNumVos != null && specsvsNumVos.size() > 0) {
                    resultMapList.add(map);
                }
            }
            if (resultMapList.size() > 0) {
                return JsonResp.success(resultMapList);
            }
        }
        return JsonResp.fail("没有数据");
    }
    /**
     * 自动补全计费帐户
     * @return
     */
    @GET
    @Path("complete/fetchAccountData")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteAccount(@QueryParam("accountName") String accountName) {
        SimpleUser user = SessionUtil.getCurUser();
        List<Map<String, String>> map = autoCompleteService.acomplpleteAccount(user, accountName, DataScope.NONE);
        return JsonResp.success(map);
    }

    @GET
    @Path("getAllCarrier")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getAllCarrier() {
        List<Carrier> list = carrierService.findAllCarrier();
        return JsonResp.success(list);
    }

    /**
     * 上传彩信的图片，音频，视频
     *
     * @param req
     * @return
     */
    @POST
    @Path(Keys.UPLOADFILE)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public JsonResp mediaUpload(@Context HttpServletRequest req) {
        try {
            UploadResult result = importer.uploadMedia(req);
            StatusCode statusCode = result.getStatusCode();
            if (statusCode == StatusCode.Success) {
                return JsonResp.success(new ReturnFileData(result.getNewFileName(), result.getFileSize()));
            }
            return JsonResp.fail(statusCode.getIndex(), statusCode.getStateDesc());
        } catch (Exception e) {
            logger.error("upload mms media file failed: ", e);
            return JsonResp.fail("upload mms media file failed: " + e.getMessage());
        }
    }

    /**
     * ftp服务端下载文档
     * path是ftp服务器端mos下一级目录文件全路径比如path='/doc/webservice.docx'
     */
    @GET
    @Path("download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void downloadFile(@QueryParam("path") String path, @Context HttpServletResponse response) throws IOException {
        String mimeType;
        if (!StringUtils.isBlank(path) && !path.contains("..")) { // 防止用户访问系统如ump的文件
            mimeType = new MimetypesFileTypeMap().getContentType(path);
        } else {
            throw new IOException();
        }

        response.setHeader("content-type", mimeType);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Content-disposition", "attachment;filename=" + path);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            if (!fileHelper.downloadFile(path, os)) {
                response.sendError(404);
            }
            os.flush();
        } catch (IOException e) {
            throw new IOException();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * 获取企业下所有角色
     */
    @POST
    @Path("getAllRoles")
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp getAllRoles(){
        SimpleUser user = SessionUtil.getCurUser();
        List<Role> roles = roleService.findRoles(user.getEnterpriseId(),
                Platform.FRONTKIT);
        return JsonResp.success(roles);
    }

    /**
     * 获取企业下部门树（获取的是企业全局的部门树）
     */
    @POST
    @Path("getDeptTree")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getDeptTree() {
        SimpleUser user = SessionUtil.getCurUser();
        //获取企业根部门节点
        Department rootDept = userMgrService.
                getDeptByEntId(user.getEnterpriseId());
        String path = rootDept.getId() + Delimiters.DOT;
        List<Department> childDepartments = userMgrService.
                getChildDepartments(path);
        childDepartments.add(0, rootDept);
        return JsonResp.success(childDepartments);
    }
    /**
     * 获取企业下所有部门(去掉根部门)
     */
    @GET
    @Path("complete/fetchDepartmentInfo")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp acomplpleteDepartment(@QueryParam("deptName") String deptName) {
        SimpleUser user = SessionUtil.getCurUser();
        //获取企业根部门节点
        Department rootDept = userMgrService.getDeptByEntId(user.getEnterpriseId());
        QueryParameters parmas = new QueryParameters();
        String path = rootDept.getId() + Delimiters.DOT;
        parmas.addParam("deptName",deptName);
        parmas.addParam("path",path);
        List<Department> childDepartments = userMgrService.autoCompleteDepartments(parmas);
        childDepartments.add(0, rootDept);
        List<Map<String ,String>> result = new ArrayList<>();
        for(Department department : childDepartments){
            Map<String,String> item = new HashMap<>();
            item.put("id",String.valueOf(department.getId()));
            item.put("name",department.getDeptName());
            item.put("path",department.getPath());
            result.add(item);
        }
        return JsonResp.success(result);
    }

    /**
     * 用户列表查询条件->业务类型
     */
    @POST
    @Path("/getAllBusinessType")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getAllBusinessType() {
        SimpleUser user = SessionUtil.getCurUser();
        List<BizType> bizTypes = bizTypeService.
                findBizTypeByEntId(user.getEnterpriseId());
        return JsonResp.success(bizTypes);
    }

    /**
     * 获取企业信息
     */
    @POST
    @Path("/getEntInfo")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getEntInfo() {
        SimpleUser curUser = SessionUtil.getCurUser();
        Enterprise enterprise = (Enterprise) userService.
                findUserById(curUser.getEnterpriseId());
        return JsonResp.success(enterprise);
    }

    /**
     * 重置发送或透传密码
     */
    @POST
    @Path("/resetSendOrMidPwd")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp resetSendOrMidPwd(User user) throws Exception {
        SimpleUser curUser = SessionUtil.getCurUser();
        String pwd = RandomStringUtils.randomAlphanumeric(8);
        Enterprise curEnterprise = SessionUtil.getCurEnterprise();
        String logContent = "【"+user.getUserName() + "@" + curEnterprise.getDomain() +"】";
        if (user.getAccountType() == UserAccountType.OSPF) {
            user.setMidPassword(AuthSecurityUtil.encrypt(pwd));
            if (userService.resetMidPwd(user)) {
                //同步更新session中的透传密码
                curUser.setTransmitPassword(user.getMidPassword());
                sysLogService.addLog(curUser, OperationType.RESET_TRANSMIT_PWD,"用户账号","common","resetSendOrMidPwd",logContent);
                return JsonResp.success("重置透传密码成功！新密码为：" + pwd);
            }
        } else {
            //重置发送密码
            user.setPassword(Md5Utils.mixLoginPasswd(pwd, config.getMixKey()));
            if (userService.resetSendPwd(user)) {
                //同步更新session中的发送密码
                curUser.setSendMd5Password(DigestUtils.md5Hex(pwd));
                sysLogService.addLog(curUser, OperationType.RESET_SEND_PWD,"用户账号","common","resetSendOrMidPwd",logContent);
                return JsonResp.success("重置发送密码成功！新密码为：" + pwd);
            }
        }
        return JsonResp.fail();
    }

    /**
     * 重置登录密码
     */
    @POST
    @Path("/resetLoginPwd")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp resetLoginPwd(User user) throws RepositoryException {
        String password = RandomStringUtils.randomAlphanumeric(8);
        user.setSecondPassword(Md5Utils.mixLoginPasswd(password, config.getMixKey()));
        Enterprise curEnterprise = SessionUtil.getCurEnterprise();
        if (userService.resetLoginPwd(user)) {
            //同步更新session里保持的发送密码
            SimpleUser curUser = SessionUtil.getCurUser();
            curUser.setSecondPassword(user.getSecondPassword());
            String logContent = "【"+user.getUserName() + "@" + curEnterprise.getDomain() +"】";
            sysLogService.addLog(curUser, OperationType.RESET_LOGIN_PWD,"用户账号","common","resetLoginPwd",logContent);
            return JsonResp.success("重置登录密码成功！新密码为：" + password);
        }
        return JsonResp.fail();
    }

    @POST
    @Path("getChannelByBiztype")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp getChannelByBiztype(SpecsvsNumVo vo){
        if(vo.getMsgType() ==null){
            return JsonResp.fail("msgType cannot be null!");
        }
        SimpleUser user = SessionUtil.getCurUser();
        List<SpecsvsNumVo> list = bizTypeService.getCarrierDetailByBizId(user.getEnterpriseId(),vo.getBizTypeId(),vo.getMsgType());
        if(list == null || list.isEmpty()){
            return JsonResp.fail("该业务类型无可发送通道！");
        }
        return JsonResp.success(list);
    }

    /**
     * 获取企业下所有的短信/彩信分配的端口
     * @param vo
     * @return
     */
    @POST
    @Path("getAllChannelByMsgType")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp ge(MsgVo vo){
        int enterpriseId = SessionUtil.getCurUser().getEnterpriseId();
        List<CarrierChannel> carrierChannels = bizTypeService.findCarrierChannelByEnterpriseId(enterpriseId,vo.getMsgType());
        if(carrierChannels == null || carrierChannels.isEmpty()){
            return JsonResp.fail("该业务类型无可发送通道！");
        }
        List<SpecsvsNumVo> vos = new ArrayList<>();
        SpecsvsNumVo numVo;
        for (CarrierChannel channel:carrierChannels) {
            numVo = new SpecsvsNumVo();
            numVo.setChannelId(channel.getId());
            numVo.setChannelName(channel.getName());
            numVo.setBasicNumber(channel.getBasicNumber());
            vos.add(numVo);
        }
        return JsonResp.success(vos);
    }

    @POST
    @Path("checkEnabledExport")
    public JsonResp checkEnabledExport(){
        int maxRecords = config.getMaxExportQuantity(Platform.BACKEND);
        SimpleUser user = SessionUtil.getCurUser();
        Enterprise loginEnt = userService.getLoginEnt(user.getEnterpriseId());
        if(!loginEnt.getEnabledExport()){
            return JsonResp.fail(String.valueOf(maxRecords));
        }
        return JsonResp.success(maxRecords);
    }

    //region 检测关键字
    @POST
    @Path("/checkKeyword")
    public JsonResp checkKeyword(ContentVo vo){
        StringBuffer sb = new StringBuffer();
        QueryParameters params = new QueryParameters();
        int entId = SessionUtil.getCurUser().getEnterpriseId();
        params.addParam("targetId",entId);
        Enterprise loginEnt = userService.getLoginEnt(entId);

        if(loginEnt.isEnableKeywordFilter()){
            List<KeyWord> keywords = keyWordService.findKeywords(params);
            if (vo.getType() == (MsgTypeEnum.MMS.getIndex())) {
                // 检测彩信所有帧的关键字
                List<String> content = (List<String>) vo.getContent();
                StringBuffer buffer = null;
                StringBuffer resultBuffer = new StringBuffer();

                for (int i = 0;i<content.size();i++) {
                    String subContent = content.get(i).replaceAll(" ","");
                    buffer = new StringBuffer();

                    // 没有将该方法抽离到单独的方法中，是因为担心关键词量太大，会造成方法频繁调用时
                    // 消耗栈帧的数量过大，引起的OOM
                    for(KeyWord keyWord:keywords){
                        if(subContent.contains(keyWord.getKeywordName())){
                            buffer.append(keyWord.getKeywordName()+",");
                        }
                    }

                    if (StringUtils.isNotEmpty(buffer)){
                        String bufStr = buffer.toString().substring(0,buffer.length()-1);
                        if (i>0) {
                            resultBuffer.append("第").append(i).append("帧: ").append(bufStr).append(";");
                        } else {
                            resultBuffer.append("彩信标题:  ").append(bufStr).append(";");
                        }
                    }
                }
                sb = resultBuffer;
            } else if (vo.getType() == (MsgTypeEnum.SMS.getIndex())) {
                // 检测短信的关键字
                String content = (String) vo.getContent();
                content = content.replaceAll(" ","");
                for(KeyWord keyWord:keywords){
                    if(content.contains(keyWord.getKeywordName())){
                        sb.append(keyWord.getKeywordName()+",");
                    }
                }
            } else {
                return JsonResp.fail("无效操作");
            }

            if(StringUtils.isNotEmpty(sb)){
                return JsonResp.fail(sb.toString());
            }
        }
        return JsonResp.success();
    }
    //endregion
    @POST
    @Path("/getContactByGroup")
    public JsonResp getContactByGroup(@Valid PageReqt req){
        return pContactResource.getContactByGroup(req);
    }
    /**
     * 根据群组Id获取联系人
     * @param req
     * @return
     */
    @POST
    @Path("/getContactByEGroup")
    public JsonResp getContactByEGroup(@Valid PageReqt req){
        return eContactResource.getContactByGroup(req);
    }
    /**
     *
     * @param csg 入口，通讯录管理查询为个人级别，发送短信查询为企业级别
     * @return
     */
    @POST
    @Path("/getETree")
    public JsonResp getETree(ContactShareGroup csg){
        return eContactResource.getETree(csg);
    }
    @POST
    @Path("/getPTree")
    public JsonResp getPTree(){
        return pContactResource.getPTree();
    }

    @POST
    @Path("/getUserSignature")
    public JsonResp getUserSignature(){
        MmsInfoVo result = new MmsInfoVo();
        SimpleUser user = SessionUtil.getCurUser();
        //获取最新企业，用户签名数据
        Enterprise loginEnt = userService.getLoginEnt(user.getEnterpriseId());
        SimpleUser loginUser = userService.getLoginUser(user.getUsername(), Platform.FRONTKIT);
        result.setEntSigLocation(loginEnt.getSigLocation());
        result.setEntSignature(loginEnt.getSignature());
        result.setUserSigLocation(loginUser.getSigLocation());
        result.setUserSignature(loginUser.getSignature());
        return JsonResp.success(result);
    }
}

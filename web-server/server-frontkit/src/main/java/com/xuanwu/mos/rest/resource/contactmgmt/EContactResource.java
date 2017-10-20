package com.xuanwu.mos.rest.resource.contactmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.Contact;
import com.xuanwu.mos.domain.entity.ContactGroup;
import com.xuanwu.mos.domain.entity.ContactShareGroup;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.User;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.dto.PageReqt;
import com.xuanwu.mos.dto.PageResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.service.ContactGroupService;
import com.xuanwu.mos.rest.service.ContactService;
import com.xuanwu.mos.rest.service.ContactShareGroupService;
import com.xuanwu.mos.rest.service.SysLogService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.utils.SessionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**共享通讯录，取代mos企业通讯录功能
 * Created by zhangz on 2017/5/11.
 */
@Path(Keys.CONTACTMGR_ECONTACT)
@Component
public class EContactResource {
    @Autowired
    private ContactService cs;
    @Autowired
    private ContactGroupService cgs;
    @Autowired
    private ContactShareGroupService csgs;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private UserMgrService userMgrService;
    /**
     *
     * @param csg 入口，通讯录管理查询为个人级别，发送短信查询为企业级别
     * @return
     */
    @POST
    @Path(Keys.CONTACTMGR_ECONTACT_INDEX + "/getETree")
    public JsonResp getETree(ContactShareGroup csg){
        SimpleUser user = SessionUtil.getCurUser();
        ContactGroup root = new ContactGroup();
        try {
            if(csg != null && "message".equals(csg.getSource())){
                root = csgs.findETree(null,user.getEnterpriseId());
            }else{
                root = csgs.findETree(user.getId(),null);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            return JsonResp.fail();
        }
        return JsonResp.success(root);
    }
    @POST
    @Path(Keys.CONTACTMGR_ECONTACT_ADDCONTACTGROUP)
    public JsonResp addGroup(@Valid ContactShareGroup shareGroup){
        SimpleUser user = SessionUtil.getCurUser();
        shareGroup.setUserId(user.getId());
        QueryParameters params = new QueryParameters();
        params.addParam("userId",user.getId());
        params.addParam("groupId",shareGroup.getGroupId());
        List<ContactShareGroup> exist = csgs.findContactShareGroups(params);
        if(!exist.isEmpty()){
            return JsonResp.fail("共享通讯录已存在！");
        }
        try {
            shareGroup.setEnterpriseId(user.getEnterpriseId());
            csgs.insert(shareGroup);
            sysLogService.addLog(user,OperationType.NEW,"【通讯录管理】/【共享通讯录】/【通讯录名称】","Public","Login","新增共享：" +
                    "【" + cgs.findContactGroupById(shareGroup.getGroupId()).getName() + "】"); //添加访问日志
            return JsonResp.success();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
    }

    @POST
    @Path(Keys.CONTACTMGR_ECONTACT_DELETECONTACTGROUP)
    public JsonResp deleteGroup(@Valid ContactShareGroup del){
        SimpleUser user = SessionUtil.getCurUser();
        del.setUserId(user.getId());
        QueryParameters params = new QueryParameters();
        params.addParam("userId",user.getId());
        params.addParam("groupId",del.getGroupId());
        List<ContactShareGroup> exist = csgs.findContactShareGroups(params);
        if(exist.isEmpty()){
            return JsonResp.fail("不能删除共享子组！");
        }
        try {
            csgs.delete(del);
            sysLogService.addLog(user,OperationType.DELETE,"【通讯录管理】/【共享通讯录】/【通讯录名称】","Public","Login","【"+cgs.findContactGroupById(del.getGroupId()).getName()+"】"); //添加访问日志
            return JsonResp.success();
        } catch (RepositoryException e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
    }

    /**
     * 根据群组Id获取联系人
     * @param req
     * @return
     */
    @POST
    @Path(Keys.CONTACTMGR_ECONTACT_INDEX + "/getContactByEGroup")
    public JsonResp getContactByGroup(@Valid PageReqt req){
        QueryParameters params = new QueryParameters(req);
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("enterpriseId",user.getEnterpriseId());
        try {
            String source = (String) req.getParams().get("source");
            // 发送短信查询共享通讯录，企业级别，且根据是否共享子组，是否显示，获取联系人
            if(source != null && "message".equals(source)) {
                QueryParameters userParams = new QueryParameters();
                userParams.addParam("platformId", Platform.FRONTKIT.getIndex());
                userParams.addParam("enterpriseId", user.getEnterpriseId());
                List<Integer> userIds = new ArrayList<>();
                List<User> users = userMgrService.listUsers(userParams);
                for (User u : users) {
                    userIds.add(u.getId());
                }
                params.addParam("userIds",userIds);
                params.addParam("message",true);
            }else{
                params.addParam("userId",user.getId());
            }
            Integer groupId = (Integer)req.getParams().get("groupId");
            int total = 0;
            List<ContactShareGroup> exist = csgs.findContactShareGroups(params);
            //exist.isEmpty()，即页面点击子组
            if(exist.isEmpty() && groupId != null && groupId != 1){
                total = cs.findContactsCount(params);
            }else{
                total = csgs.findShareContactCount(params);
            }
            if (total == 0) {
                return PageResp.emptyResult();
            }
            PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
            params.setPage(pageInfo);
            List<Contact> contacts = null;
            if(exist.isEmpty() && groupId != null && groupId != 1){
                contacts = cs.findContacts(params);
            }else{
                contacts = csgs.findShareContactDetail(params);
            }
            return PageResp.success(total, contacts);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fail("系统异常，请联系管理员！");
        }
    }
    @POST
    @Path(Keys.CONTACTMGR_ECONTACT_INDEX)
    public JsonResp list(){
        return JsonResp.success();
    }
}

package com.xuanwu.mos.rest.resource;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.dto.PageInfo;
import com.xuanwu.mos.rest.service.*;
import com.xuanwu.mos.rest.service.statistics.StatisticsService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.JsonUtil;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.msggate.common.util.StringUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动补全通用控制器
 * Created by Jiang.Ziyuan on 2017/3/30.
 */
@Component
public class AutoFillResource {

    private static int defaultFetchSize = 10;

    private UserService userService;
    private BizTypeService bizTypeService;
    private IBizDataService bizData;
    private StatisticsService statisticsService;
    private CarrierChannelService carrierChannelService;
    private AccountService accountService;
    private MenuDiyService menuDiyService;

    public void fetchDeptData(Model model, SimpleUser user, DataScope ds) {
        List<GsmsUser> depts = new ArrayList<GsmsUser>();
        List<GsmsUser> ret = bizData.getDeptsIncludeDel(user.getEnterpriseId());
        if (ListUtil.isNotBlank(ret)) {
            switch (ds) {
                case GLOBAL:
                case NONE:
                    depts = ret;
                    break;
                case DEPARTMENT:
                    ret = bizData.getDeptsIncludeDel(user.getEnterpriseId());
                    GsmsUser temp = bizData.getDeptById(user.getEnterpriseId(),
                            user.getParentId());
                    depts.add(temp);
                    for (GsmsUser dept : ret) {
                        if (dept.getPath().startsWith(temp.getFullPath())) {
                            depts.add(dept);
                        }
                    }
                    break;
                case PERSONAL:
                    depts.add(bizData.getDeptById(user.getEnterpriseId(),
                            user.getParentId()));
                    break;
                default:
                    break;
            }
        }
        String jsonData = JsonUtil.toJSON(depts.toString(), "ok", 0);
        //model.addAttribute(Keys.JSON_DATA, jsonData);
    }

    /**
     * 获取可用的user，即状态为0的user，主要用于补全
     */
    public void fetchUsedUserData(Model model, SimpleUser user, DataScope ds,
                                  String userName, int fetchSize) {
        List<User> users = new ArrayList<User>();
        List<User> ret = null;
        if(ds == null) {
            User u = (User) userService.findUserById(user.getId());
            if (u != null) {
                users.add(u);
            }
        } else {
            switch (ds) {
                case GLOBAL:
                case NONE:
                    ret = userService.findSelfUsersSimple(user.getEnterpriseId(), null,
                            userName, fetchSize);
                    break;
                case PERSONAL:
                    User u = (User) userService.findUserById(user.getId());
                    if (u != null) {
                        users.add(u);
                    }
                    break;
                default:
                    break;
            }
        }
        if (ListUtil.isNotBlank(ret)) {
            users.addAll(ret);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < users.size(); i++) {
            sb.append(i == 0 ? "" : ",").append(users.get(i));
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchAccessPortData(Model model, SimpleUser user, DataScope ds,
                                    String userName) {
        fetchAccessPortData(model, user, ds, userName, defaultFetchSize);
    }

    public void fetchUserData(Model model, SimpleUser user, DataScope ds,
                              String userName) {
        fetchUserData(model, user, ds, userName, defaultFetchSize);
    }

    public void fetchUserDataNotDel(Model model, SimpleUser user, DataScope ds,
                                    String userName) {
        fetchUserDataNotDel(model, user, ds, userName, defaultFetchSize);
    }

    public void fetchAccessPortData(Model model, SimpleUser user, DataScope ds,
                                    String userName, int fetchSize) {
        List<CarrierChannel> channelList = carrierChannelService.findSimpleChannelByNum(null, 0, -1, 0);
        List<Account> accounts = accountService.findAccounts(0, null, null, null, null, 0, 0);
        StringBuilder sb = new StringBuilder();
        boolean hasChannel = false;
        sb.append("[");
        for (int i = 0; i < channelList.size(); i++) {
            sb.append(i == 0 ? "" : ",").append("{\"id\":").append(channelList.get(i).getId());
            sb.append(",\"name\":\"").append(channelList.get(i).getChannelNum()).append("\"");
            sb.append("}");
            hasChannel = true;
        }
        for (int i = 0; i < accounts.size(); i++) {
            sb.append(hasChannel ? "," : i == 0 ? "" : ",").append("{\"id\":").append(accounts.get(i).getId());
            sb.append(",\"name\":\"").append(accounts.get(i).getEmail()).append("\"");
            sb.append("}");
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchUserData(Model model, SimpleUser user, DataScope ds,
                              String userName, int fetchSize) {
        List<User> users = new ArrayList<User>();
        List<User> ret = null;
        if(ds == null) {
            User u = (User) userService.findUserById(user.getId());
            if (u != null) {
                users.add(u);
            }
        } else {
            switch (ds) {
                case GLOBAL:
                case NONE:
                    ret = userService.findUsersSimple(user.getEnterpriseId(), null,
                            userName, fetchSize);
                    break;
                case DEPARTMENT:
                    GsmsUser dept = bizData.getDeptById(user.getEnterpriseId(),
                            user.getParentId());
                    ret = userService.findUsersSimple(0, dept.getFullPath(), userName,
                            fetchSize);
                    break;
                case PERSONAL:
                    User u = (User) userService.findUserById(user.getId());
                    if (u != null) {
                        users.add(u);
                    }
                    break;
            }
        }
        if (ListUtil.isNotBlank(ret)) {
            users.addAll(ret);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < users.size(); i++) {
            sb.append(i == 0 ? "" : ",").append(users.get(i));
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchUserDataNotDel(Model model, SimpleUser user, DataScope ds,
                                    String userName, int fetchSize) {
        List<User> users = new ArrayList<User>();
        List<User> ret = null;
        switch (ds) {
            case GLOBAL:
            case NONE:
                ret = userService.findUsersSimpleNotDel(user.getEnterpriseId(), null,
                        userName, fetchSize);
                break;
            case DEPARTMENT:
                GsmsUser dept = bizData.getDeptById(user.getEnterpriseId(),
                        user.getParentId());
                ret = userService.findUsersSimple(0, dept.getFullPath(), userName,
                        fetchSize);
                break;
            case PERSONAL:
                User u = (User) userService.findUserById(user.getId());
                if (u != null) {
                    users.add(u);
                }
                break;
        }
        if (ListUtil.isNotBlank(ret)) {
            users.addAll(ret);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < users.size(); i++) {
            sb.append(i == 0 ? "" : ",").append(users.get(i));
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchBizTypeData(Model model, SimpleUser user, DataScope ds,
                                 String bizName) {
        fetchBizTypeData(model, user, ds, bizName, defaultFetchSize);
    }

    public void fetchBizTypeDataNotDel(Model model, SimpleUser user, DataScope ds,
                                       String bizName) {
        fetchBizTypeDataNotDel(model, user, ds, bizName, defaultFetchSize);
    }

    public void fetchBizTypeData(Model model, SimpleUser user, DataScope ds,
                                 String bizName, int fetchSize) {
        List<BizType> bizs = new ArrayList<BizType>();
        List<BizType> ret = bizTypeService.findBizTypesSimple(
                user.getEnterpriseId(), bizName, fetchSize);
        if (ListUtil.isNotBlank(ret)) {
            bizs.addAll(ret);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < bizs.size(); i++) {
            sb.append(i == 0 ? "" : ",").append(bizs.get(i).toJSONSimple());
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchMenuOptionData(Model model, SimpleUser user,int accountId, DataScope ds,
                                    String optionName) {
        int userId = ds == DataScope.PERSONAL?user.getId():0;
        List<MenuDiy>  menus =  menuDiyService.findMenuOptions(userId, accountId, optionName, defaultFetchSize);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < menus.size(); i++) {
            sb.append(i == 0 ? "" : ",").append(menus.get(i).toJSONSimple());
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchBizTypeDataNotDel(Model model, SimpleUser user, DataScope ds,
                                       String bizName, int fetchSize) {
        List<BizType> bizs = new ArrayList<BizType>();
        List<BizType> ret = bizTypeService.findBizTypesSimpleNotDel(
                user.getEnterpriseId(), bizName, fetchSize);
        if (ListUtil.isNotBlank(ret)) {
            bizs.addAll(ret);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < bizs.size(); i++) {
            sb.append(i == 0 ? "" : ",").append(bizs.get(i).toJSONSimple());
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchChannelData(Model model, SimpleUser user, DataScope ds) {
        List<CarrierChannel> channelList = statisticsService.findAllChannel();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int channalSize = channelList.size();
        for (int i = 0; i < channalSize; i++) {
            boolean isRemove = channelList.get(i).isRemove();
            if(isRemove){
                continue;
            }
            sb.append(channelList.get(i).toJSONSimple());
            sb.append("," );
        }
        if(sb.length() > 1){
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public void fetchUserDataDosend(Model model, SimpleUser user, DataScope ds,
                                    String userName, UserState[] states) {
        fetchUserDataDosend(model, user, ds, userName, -1,
                Platform.FRONTKIT.getIndex(), defaultFetchSize, states);
    }

    public boolean hasDoSendPermission(SimpleUser user,Integer targetUser) {
        List<Integer> userIds = userService.findSendSmsPermitId(user
                .getEnterpriseId());
        boolean flag = false;
        if (ListUtil.isNotBlank(userIds)) {
            Map<Integer, Integer> userMap = new HashMap<Integer, Integer>();
            for (Integer userId : userIds) {
                userMap.put(userId, userId);
            }
            if (userMap.get(targetUser) != null) {
                flag = true;
            }
        }
        return flag;
    }

    public void fetchUserDataDosend(Model model, SimpleUser user, DataScope ds,
                                    String userName, int parentId, int platformId, int fetchSize,
                                    UserState[] states) {
        DynamicParam param = new DynamicParam();
        param.setPi(new PageInfo(1, fetchSize, fetchSize));
        User _user = new User();
        _user.setEnterpriseId(user.getEnterpriseId());
        _user.setUserName(StringUtil.isBlank(userName) ? null : userName);
        _user.setPlatformId(platformId);
        List<User> users = new ArrayList<User>();
        List<GsmsUser> rets = null;
        switch (ds) {
            case GLOBAL:
            case NONE:
                rets = userService.findSimpleUsers(_user, param, states);
                break;
            case DEPARTMENT:
                String path = userService.findPathById(user.getId());
                _user.setPath(path);
                rets = userService.findSimpleUsers(_user, param, states);
                break;
            case PERSONAL:
                User u = (User) userService.findUserById(user.getId());
                if (u != null) {
                    users.add(u);
                }
                break;
        }
        if (ListUtil.isNotBlank(rets)) {
            for (GsmsUser ret : rets) {
                users.add((User) ret);
            }
        }
        List<Integer> userIds = userService.findSendSmsPermitId(user
                .getEnterpriseId());
        users = fileDosend(users, userIds);
        if (ListUtil.isBlank(users)) {
            users = new ArrayList<User>();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < users.size(); i++) {
            sb.append(i == 0 ? "" : ",").append(users.get(i));
        }
        sb.append("]");
        /*model.addAttribute(Keys.JSON_DATA,
                JsonUtil.toJSON(sb.toString(), "ok", 0));*/
    }

    public List<User> fileDosend(List<User> ls, List<Integer> userIds) {
        if (CollectionUtils.isEmpty(ls) || CollectionUtils.isEmpty(userIds)) {
            return null;
        }
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (Integer integer : userIds) {
            map.put(integer, "");
        }
        List<User> ret = new ArrayList<User>();
        for (User g : ls) {
            if (map.get(g.getId()) != null) {
                ret.add(g);
            }
        }
        return ret;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBizTypeService(BizTypeService bizTypeService) {
        this.bizTypeService = bizTypeService;
    }

    @Autowired
    public void setBizData(IBizDataService bizData) {
        this.bizData = bizData;
    }

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Autowired
    public void setCarrierChannelService(CarrierChannelService carrierChannelService) {
        this.carrierChannelService = carrierChannelService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}

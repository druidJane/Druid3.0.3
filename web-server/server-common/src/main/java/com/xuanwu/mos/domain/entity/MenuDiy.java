package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 自定义菜单
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public class MenuDiy extends BaseEntity {

    private int id;// 主键
    private int accountId;// 所属公众账号id
    private Account account;// 所属公众账号
    private int userId;// 所属用户id
    private String title;// 菜单名称
    private Date addTime;// 添加时间
    private Date modifyTime;// 修改时间
    private int state;// 状态(0:待使用,1:正使用)
    private List<MenuDiyOption> options = new ArrayList<MenuDiyOption>();// 菜单选项

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<MenuDiyOption> getOptions() {
        return options;
    }

    public void setOptions(List<MenuDiyOption> options) {
        this.options = options;
    }

    public String toJSONSimple(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id);
        sb.append(",\"name\":\"").append(StringUtil.fixJsonStr(title)).append('\"');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String toJSON() {
       return null;
    }

    public String toJSONData() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"id\":" + id);
        sb.append(",\"userId\":" + userId);
        sb.append(",\"title\":\"" + StringUtil.fixJsonStr(title) + "\"");
        sb.append(",\"addTime\":\""
                + DateUtil.format(addTime, DateUtil.DateTimeType.DateTime) + "\"");
        sb.append(",\"modifyTime\":\""
                + DateUtil.format(modifyTime, DateUtil.DateTimeType.DateTime) + "\"");
        sb.append(",\"state\":" + state);
        if (options == null || options.isEmpty()) {
            sb.append(",\"options\":null");
        } else {
            Collections.sort(options, new MenuDiyOption());
            sb.append(",\"options\":[");
            for (MenuDiyOption o : options) {
                sb.append(o.toJSON()).append(",");
            }
            sb.deleteCharAt(sb.toString().length() - 1);
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    public String toPostData() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"button\":[");
        for (MenuDiyOption opt : options) {
            sb.append("{\"name\":\"" + StringUtil.fixJsonStr(opt.getName())
                    + "\"");
            if (!opt.getSubOptions().isEmpty()) {
                sb.append(",\"sub_button\":[");
                for (MenuDiyOption o : opt.getSubOptions()) {
                    sb.append("{\"name\":\""
                            + StringUtil.fixJsonStr(o.getName()) + "\"");
                    sb.append(o.getParamData());
                    sb.append("},");
                }
                sb.deleteCharAt(sb.toString().length() - 1);
                sb.append("]");
            } else {
                sb.append(opt.getParamData());
            }
            sb.append("},");
        }
        sb.deleteCharAt(sb.toString().length() - 1);
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "MenuDiy [id=" + id + ", userId=" + userId + ", title=" + title
                + ", addTime=" + addTime + ", modifyTime=" + modifyTime
                + ", state=" + state + ", options=" + options + "]";
    }
}

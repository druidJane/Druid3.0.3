package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.StringUtil;

import java.util.Date;

/**
 * 公众账号
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public class CommonAccount extends BaseEntity {

    private int id;// 主键
    private String openId;// 公众账号唯一标识,自动写入
    private String nick;// 名称
    private String email;// 登录邮箱
    private String name;// 微信号
    private AccountState state;// 状态(0:启用,1:暂停,2:删除)
    private String token;// 接收消息Token
    private String appId;// 自定义菜单APPID
    private String appSecret;// 自定义菜单APP密钥
    private int userId;// 新增用户ID
    private String userName;// 新增用户名
    private String remark;// 备注
    private Date addTime;// 新增时间
    private Date modifyTime;// 修改时间
    private AccountStatus status;// 状态(0:验证未通过,1:验证通过)
    private boolean syncMember ;// 同步关注用户状态(true:正在同步,false:同步完成)
    private Date lastSyncTime ;// 最后一次同步时间
    private int massSendCount ;// 当月群发次数
    private Date lastFSendTime ;//最后一次群发时间

    private String accessToken;// Access_Token
    private Date expireTime;// Access_Token失效时间

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public void setStateIdx(int idx) {
        this.state = AccountState.getState(idx);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setStatusIdx(int idx) {
        this.status = AccountStatus.getStatus(idx);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public static enum AccountFindType {
        ID(0), OPENID(1), EMAIL(2), NAME(3);

        private int index;

        private AccountFindType(int index) {
            this.index = index;
        }

        public static AccountFindType getType(int index) {
            for (AccountFindType type : AccountFindType.values()) {
                if (type.getIndex() == index) {
                    return type;
                }
            }
            return null;
        }

        public int getIndex() {
            return index;
        }
    }

    public static enum AccountState {
        NORMAL(0), SUSPEND(1), DELETED(2), NOT_LIMIT(-1) ;

        private int index;

        private AccountState(int index) {
            this.index = index;
        }

        public static AccountState getState(int index) {
            for (AccountState state : AccountState.values()) {
                if (state.getIndex() == index) {
                    return state;
                }
            }
            return null;
        }

        public int getIndex() {
            return index;
        }
    }

    public static enum AccountStatus {
        UNCHECKED(0), CHECKED(1);

        private int index;

        private AccountStatus(int index) {
            this.index = index;
        }

        public static AccountStatus getStatus(int index) {
            for (AccountStatus status : AccountStatus.values()) {
                if (status.getIndex() == index) {
                    return status;
                }
            }
            return null;
        }

        public int getIndex() {
            return index;
        }
    }

    public boolean isSyncMember() {
        return syncMember;
    }

    public void setSyncMember(boolean syncMember) {
        this.syncMember = syncMember;
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public int getMassSendCount() {
        return massSendCount;
    }

    public void setMassSendCount(int massSendCount) {
        this.massSendCount = massSendCount;
    }

    public Date getLastFSendTime() {
        return lastFSendTime;
    }

    public void setLastFSendTime(Date lastFSendTime) {
        this.lastFSendTime = lastFSendTime;
    }

    @Override
    public String toJSON() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"id\":" + id);
        sb.append(",\"openId\":\"" + openId + "\"");
        sb.append(",\"nick\":\"" + nick + "\"");
        sb.append(",\"email\":\"" + email + "\"");
        sb.append(",\"name\":\"" + name + "\"");
        sb.append(",\"state\":" + state.getIndex());
        sb.append(",\"token\":\"" + token + "\"");
        sb.append(",\"appId\":\"" + appId + "\"");
        sb.append(",\"appSecret\":\"" + appSecret + "\"");
        sb.append(",\"userId\":" + userId);
        sb.append(",\"userName\":\"" + userName + "\"");
        sb.append(",\"remark\":\"" + StringUtil.fixJsonStr(remark) + "\"");
        sb.append(",\"addTime\":\""
                + DateUtil.format(addTime, DateUtil.DateTimeType.DateTime) + "\"");
        sb.append(",\"modifyTime\":\""
                + DateUtil.format(modifyTime, DateUtil.DateTimeType.DateTime) + "\"");
        sb.append(",\"status\":" + status.getIndex());
        sb.append(",\"syncMember\":\"" + syncMember+ "\"");
        sb.append(",\"lastSyncTime\":\""
                + DateUtil.format(lastSyncTime, DateUtil.DateTimeType.DateTime) + "\"");
        sb.append("}");
        return sb.toString();
    }
}

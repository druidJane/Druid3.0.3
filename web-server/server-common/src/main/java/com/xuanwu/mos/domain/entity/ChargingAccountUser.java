package com.xuanwu.mos.domain.entity;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public class ChargingAccountUser extends BaseEntity{
    private int id;
    private String accountName;
    private String linkMan;
    private int state;
    private int capitalAccountId;
    private int enterpriseId;
    private String phone;
    private String deptName;
    /*是否已包含用户*/
    private boolean includedType;
    /*扩展码*/
    private String identify;
    private int roleId;
    private int parentId;
    private boolean showAllChild;
    private String userIds;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isShowAllChild() {
        return showAllChild;
    }

    public void setShowAllChild(boolean showAllChild) {
        this.showAllChild = showAllChild;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCapitalAccountId() {
        return capitalAccountId;
    }

    public void setCapitalAccountId(int capitalAccountId) {
        this.capitalAccountId = capitalAccountId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public boolean getIncludedType() {
        return includedType;
    }

    public void setIncludedType(boolean includedType) {
        this.includedType = includedType;
    }

    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id);
        sb.append(",\"accountName\":\"").append(accountName).append("\"");
        sb.append(",\"linkMan\":\"").append(linkMan).append("\"");
        sb.append(",\"deptName\":\"").append(deptName).append("\"");
        sb.append(",\"phone\":\"").append(phone).append("\"");
        sb.append(",\"state\":").append(state);
        sb.append('}');
        return sb.toString();
    }
}

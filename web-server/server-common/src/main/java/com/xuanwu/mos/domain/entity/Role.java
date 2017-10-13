package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.IndustryType;
import com.xuanwu.mos.domain.enums.RoleType;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @version 1.0.0
 * @Description
 * @date 2016-10-08
 */
public class Role extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private int id;// 角色ID
    private String name;// 角色名称
    private int userId;// 用户ID
    private String userName;// 用户名
    private String remark;// 备注
    private boolean isDefault;//
    private Date lastModifyDate;// 最后修改时间
    private int lastModifyUserId;// 最后修改的用户ID
    private String lastModifyUserName;// 最后修改的用户名
    private RoleType roleType;// 0:普通角色,1:初始化角色,9:超级管理员
    private int enterpriseId;// 企业ID
    private int platformId;
    private String platformName;
    private IndustryType industryType;
    private String permissionIds;
    private String permissionNames;//特定格式的权限列表
    private List<Permission> permissions;// 权限列表
    private List<RolePermission> rolePermissions;// 权限列表
    private boolean checked;//前台页面是否选中
    private boolean haveRole;//原先是否含有该角色
    private boolean userBindFlag;//是否与用户绑定
    private int rootDeptId;//部门根节点ID

    public int getRootDeptId() {
        return rootDeptId;
    }

    public void setRootDeptId(int rootDeptId) {
        this.rootDeptId = rootDeptId;
    }

    public String getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String permissionIds) {
        this.permissionIds = permissionIds;
    }

    public boolean isUserBindFlag() {
        return userBindFlag;
    }

    public void setUserBindFlag(boolean userBindFlag) {
        this.userBindFlag = userBindFlag;
    }

    public String getPermissionNames() {
        return permissionNames;
    }

    public void setPermissionNames(String permissionNames) {
        this.permissionNames = permissionNames;
    }

    /**
     * 是否绑定
     */
    private boolean bound;

    private boolean showPermissions = false;

    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets remark.
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Gets last modify date.
     *
     * @return the last modify date
     */
    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    /**
     * Sets last modify date.
     *
     * @param lastModifyDate the last modify date
     */
    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    /**
     * Gets last modify user id.
     *
     * @return the last modify user id
     */
    public int getLastModifyUserId() {
        return lastModifyUserId;
    }

    /**
     * Sets last modify user id.
     *
     * @param lastModifyUserId the last modify user id
     */
    public void setLastModifyUserId(int lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    /**
     * Gets last modify user name.
     *
     * @return the last modify user name
     */
    public String getLastModifyUserName() {
        return lastModifyUserName;
    }

    /**
     * Sets last modify user name.
     *
     * @param lastModifyUserName the last modify user name
     */
    public void setLastModifyUserName(String lastModifyUserName) {
        this.lastModifyUserName = lastModifyUserName;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    /**
     * Gets enterprise id.
     *
     * @return the enterprise id
     */
    public int getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * Sets enterprise id.
     *
     * @param enterpriseId the enterprise id
     */
    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    /**
     * Gets permissions.
     *
     * @return the permissions
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Sets permissions.
     *
     * @param permissions the permissions
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Gets role permissions.
     *
     * @return the role permissions
     */
    public List<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    /**
     * Sets role permissions.
     *
     * @param rolePermissions the role permissions
     */
    public void setRolePermissions(List<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    /**
     * Gets bound.
     *
     * @return the bound
     */
    public boolean getBound() {
        return bound;
    }

    /**
     * Sets bound.
     *
     * @param bound the bound
     */
    public void setBound(boolean bound) {
        this.bound = bound;
    }

    /**
     * Is show permissions boolean.
     *
     * @return the boolean
     */
    public boolean isShowPermissions() {
        return showPermissions;
    }

    /**
     * Sets show permissions.
     *
     * @param showPermissions the show permissions
     */
    public void setShowPermissions(boolean showPermissions) {
        this.showPermissions = showPermissions;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public IndustryType getIndustryType() {
        return industryType;
    }

    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }

    public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isHaveRole() {
		return haveRole;
	}

	public void setHaveRole(boolean haveRole) {
		this.haveRole = haveRole;
	}
}

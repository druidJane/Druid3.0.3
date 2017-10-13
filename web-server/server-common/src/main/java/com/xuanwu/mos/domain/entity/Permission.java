package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.DataScope;

import org.eclipse.jetty.util.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @version 1.0.0
 * @Description 权限实体 ，对应于数据库中的权限记录
 * @date 2016 -10-08
 */
public class Permission extends AbstractEntity {
    private static final long serialVersionUID = -1529450337715510535L;

    private int id;
    private String permissionCode;
    private String displayName;
    private String operationObj;
    private String areaName;
    private String controllerName;
    private String actionName;
    private Integer formMethod;
    private int parentId;
    private boolean isMenu;
    private String menuImagePath;
    private String menuDisplayName;
    private int menuDisplayOrder;
    private boolean menuHasHyperlink;
    private boolean isDisplay;
    private int level;
    private String remark;
    private int type;
    private int quickLink;
    private String operationStr;
    private int industryType;
    private String url;

    private String dataScopes; //数据域
    private String dependIds; //依赖权限
    /**
     * supported data scopes
     */
    private int platformId;
    private Platform platform;
    private Permission parent;// 父权限
    private List<Permission> children;// 子权限列表
    private boolean checked;// 角色当中是否选中
    private DataScope dataScope;// 角色当中所选权限范围
    private boolean base = false; //是否为默认权限

    private String permURL;

    public String getPermURL() {
        if (permURL == null) {
            if (StringUtil.isNotBlank(areaName) || StringUtil.isNotBlank(controllerName) || StringUtil.isNotBlank(actionName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("/");
                sb.append(areaName);
                sb.append("/");
                sb.append(controllerName);
                sb.append("/");
                sb.append(actionName);
                permURL = sb.toString();
            }
        }
        return permURL;
    }

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

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets parent id.
     *
     * @return the parent id
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Sets parent id.
     *
     * @param parentId the parent id
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * Is menu boolean.
     *
     * @return the boolean
     */
    public boolean isMenu() {
        return isMenu;
    }

    /**
     * Sets menu.
     *
     * @param isMenu the is menu
     */
    public void setMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    /**
     * Gets menu image path.
     *
     * @return the menu image path
     */
    public String getMenuImagePath() {
        return menuImagePath;
    }

    /**
     * Sets menu image path.
     *
     * @param menuImagePath the menu image path
     */
    public void setMenuImagePath(String menuImagePath) {
        this.menuImagePath = menuImagePath;
    }

    /**
     * Gets menu display name.
     *
     * @return the menu display name
     */
    public String getMenuDisplayName() {
        return menuDisplayName;
    }

    /**
     * Sets menu display name.
     *
     * @param menuDisplayName the menu display name
     */
    public void setMenuDisplayName(String menuDisplayName) {
        this.menuDisplayName = menuDisplayName;
    }

    /**
     * Gets menu display order.
     *
     * @return the menu display order
     */
    public int getMenuDisplayOrder() {
        return menuDisplayOrder;
    }

    /**
     * Sets menu display order.
     *
     * @param menuDisplayOrder the menu display order
     */
    public void setMenuDisplayOrder(int menuDisplayOrder) {
        this.menuDisplayOrder = menuDisplayOrder;
    }

    /**
     * Is menu has hyperlink boolean.
     *
     * @return the boolean
     */
    public boolean isMenuHasHyperlink() {
        return menuHasHyperlink;
    }

    /**
     * Sets menu has hyperlink.
     *
     * @param menuHasHyperlink the menu has hyperlink
     */
    public void setMenuHasHyperlink(boolean menuHasHyperlink) {
        this.menuHasHyperlink = menuHasHyperlink;
    }

    /**
     * Is display boolean.
     *
     * @return the boolean
     */
    public boolean isDisplay() {
        return isDisplay;
    }

    /**
     * Sets display.
     *
     * @param isDisplay the is display
     */
    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(int level) {
        this.level = level;
    }


    /**
     * Gets platform id.
     *
     * @return the platform id
     */
    public int getPlatformId() {
        return platformId;
    }

    /**
     * Sets platform id.
     *
     * @param platformId the platform id
     */
    public void setPlatformId(int platformId) {
        this.platform = Platform.getType(platformId);
        this.platformId = platformId;
    }

    /**
     * Gets platform.
     *
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Sets platform.
     *
     * @param platform the platform
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public List<Permission> getChildren() {
        return children;
    }

    /**
     * Sets children.
     *
     * @param children the children
     */
    public void setChildren(List<Permission> children) {
        this.children = children;
    }

    /**
     * Add to children.
     *
     * @param permission the permission
     */
    public void addToChildren(Permission permission) {
        if (this.children == null) {
            this.children = new ArrayList<Permission>();
        }
        this.children.add(permission);
    }

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public Permission getParent() {
        return parent;
    }

    /**
     * Sets parent.
     *
     * @param parent the parent
     */
    public void setParent(Permission parent) {
        this.parent = parent;
    }

    /**
     * Is checked boolean.
     *
     * @return the boolean
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Sets checked.
     *
     * @param checked the checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public DataScope getDataScope() {
        if (dataScope == null) {
            return DataScope.NONE;
        }
        return dataScope;
    }

    public void setDataScope(DataScope dataScope) {
        if (dataScope != null) {
            this.dataScope = dataScope;
        } else {
            this.dataScope = DataScope.NONE; //默认数据域
        }

    }

    public String getDependIds() {
        return dependIds;
    }

    public void setDependIds(String dependIds) {
        this.dependIds = dependIds;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets operation obj.
     *
     * @return the operation obj
     */
    public String getOperationObj() {
        return operationObj;
    }

    /**
     * Sets operation obj.
     *
     * @param operationObj the operation obj
     */
    public void setOperationObj(String operationObj) {
        this.operationObj = operationObj;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    public String getDataScopes() {
        return dataScopes;
    }

    public void setDataScopes(String dataScopes) {
        this.dataScopes = dataScopes;
    }

    /**
     * Gets serial version uid.
     *
     * @return the serial version uid
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(int type) {
        this.type = type;
    }

    public int getQuickLink() {
        return quickLink;
    }

    public void setQuickLink(int quickLink) {
        this.quickLink = quickLink;
    }

    /**
     * Gets operationStr.
     *
     * @return the operationStr
     */
    public String getOperationStr() {
        return operationStr;
    }

    /**
     * Sets operationStr.
     *
     * @param operationStr the operationStr
     */
    public void setOperationStr(String operationStr) {
        this.operationStr = operationStr;
    }

    /**
     * Gets industry type.
     *
     * @return the industry type
     */
    public int getIndustryType() {
        return industryType;
    }

    /**
     * Sets industry type.
     *
     * @param industryType the industry type
     */
    public void setIndustryType(int industryType) {
        this.industryType = industryType;
    }

    public boolean getBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }


    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Integer getFormMethod() {
        return formMethod;
    }

    public void setFormMethod(Integer formMethod) {
        this.formMethod = formMethod;
    }

    @Override
    public String toString() {
        return "Permission [id=" + id + ", displayName=" + displayName
                + ", parentID=" + parentId + ", isMenu=" + isMenu + ", menuImagePath="
                + menuImagePath + ", menuDisplayName=" + menuDisplayName
                + ", menuDisplayOrder=" + menuDisplayOrder
                + ", menuHasHyperlink=" + menuHasHyperlink + ", isDisplay="
                + isDisplay + ", level=" + level + ", url=" + url + ", dataScopes=" + dataScopes
                + ", platform=" + platform
                + ", parent=" + parent + ", children=" + children
                + ", checked=" + checked + "]";
    }

    /**
     * To json string.
     *
     * @return the string
     */
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":" + id);
        sb.append(",\"displayName\":\"" + displayName + "\"");
        sb.append(",\"parentId\":" + parentId);
        sb.append(",\"isMenu\":" + isMenu);
        sb.append(",\"menuImagePath\":\"" + menuImagePath + "\"");
        sb.append(",\"menuDisplayName\":\"" + menuDisplayName + "\"");
        sb.append(",\"menuDisplayOrder\":" + menuDisplayOrder);
        sb.append(",\"menuHasHyperlink\":\"" + menuHasHyperlink + "\"");
        sb.append(",\"isDisplay\":" + isDisplay);
        sb.append(",\"level\":" + level);
        sb.append(",\"url\":\"" + url + "\"");
        sb.append(",\"dataScopes\":\"" + dataScopes + "\"");
        sb.append(",\"platformId\":" + platform.getIndex());
        sb.append(",\"platformName\":\"" + platform.name() + "\"");
        if (children != null && children.size() > 0) {
            sb.append(",\"children\":[");
            int i = 0;
            for (Permission c : children) {
                sb.append((i++) > 0 ? "," : "");
                sb.append(c.toJSON());
            }
            sb.append("]");
        } else {
            sb.append(",\"children\":null");
        }
        sb.append(",\"checked\":" + checked);
        sb.append(",\"dataScope\":" + dataScope);
        sb.append("}");

        return sb.toString();
    }
}

package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.utils.StringUtil;

/**
 * 通讯录共享组
 * Created by Jiang.Ziyuan on 2017/3/27.
 */
public class ContactShareGroup extends AbstractEntity {
    private int id;
    private ContactGroup contactGroup;
    /** 是否显示详细信息true:显示，false：不显示 **/
    private boolean showContactFlag;
    /** 是否共享子组true:显示，false：不显示 **/
    private boolean shareChildFlag;
    private int userId ;
    private int groupId;
    private int enterpriseId;

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    // 查询来源，默认为空，发送短信入口：message
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ContactGroup getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean isShowContactFlag() {
        return showContactFlag;
    }

    public void setShowContactFlag(boolean showContactFlag) {
        this.showContactFlag = showContactFlag;
    }

    public boolean isShareChildFlag() {
        return shareChildFlag;
    }

    public void setShareChildFlag(boolean shareChildFlag) {
        this.shareChildFlag = shareChildFlag;
    }
    public String toEZTreeJSON(boolean hasChild) {
        StringBuilder queryResultPath = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        queryResultPath.append(contactGroup.getPath());
        if (this.isShareChildFlag() == false) {
            queryResultPath.append("#");
        }
        sb.append("{\"id\":").append(contactGroup.getId());
        sb.append(",\"pId\":").append(contactGroup.getParentId());
        sb.append(",\"name\":\"").append(StringUtil.fixJsonStr(contactGroup.getName()));
        sb.append("(");
        sb.append(showContactFlag?"显示,":"不显示,");
        sb.append(contactGroup.getChildCount());
        sb.append(")");
        sb.append('\"');
        sb.append(",\"isParent\":\"").append(hasChild).append('\"');
        sb.append(",\"childCount\":\"").append(contactGroup.getChildCount()).append('\"');
        sb.append(",\"path\":\"").append(contactGroup.getPath()).append('\"');
        sb.append(",\"shareChildFlag\":\"").append(shareChildFlag).append('\"');
        sb.append(",\"showContactFlag\":\"").append(showContactFlag).append('\"');
        sb.append(",\"userId\":\"").append(contactGroup.getUserId()).append('\"');
        sb.append(",\"entId\":\"").append(contactGroup.getEnterpriseId()).append('\"');
        sb.append(",\"enable\":\"").append(true).append('\"');
        sb.append(",\"qPath\":\"").append(queryResultPath).append('\"');
        sb.append("}");
        return sb.toString();
    }
}

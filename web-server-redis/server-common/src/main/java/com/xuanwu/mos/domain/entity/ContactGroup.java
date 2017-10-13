package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.util.List;

/**
 * @Description 通讯录组
 * @Data 2012-9-28
 * @Version 1.0.0
 */
public class ContactGroup extends AbstractEntity {

	private int id;

	public void setId(int id) {
		this.id = id;
	}

	/** 所属用户ID */
	private int userId;
	
	private String username;

	/** 父ID */
	private int parentId;

	/** 组名 */
	private String name;

	/** 层级路径：如1.3.2.，查找时避免递归 */
	private String path;

	/** 0:个人通信录组;1:企业通信录组;2:共享通讯录 */
	private int type;

	/** 所属企业ID */
	private int enterpriseId;

	/** 通讯录组所包含的联系人数 */
	private int childCount;
	
	/** 是否包含子组 */
	private boolean containChild;
	/**
	 * 是否显示
	 */
	private Boolean showContact;

	private List<Integer> userIds;

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public Boolean getShowContact() {
		return showContact;
	}

	public void setShowContact(Boolean showContact) {
		this.showContact = showContact;
	}

	private List<ContactGroup> children;

	public List<ContactGroup> getChildren() {
		return children;
	}

	public void setChildren(List<ContactGroup> children) {
		this.children = children;
	}

	public int getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public int getChildCount() {
		return childCount;
	}

	public boolean isContainChild() {
		return containChild;
	}

	public void setContainChild(boolean containChild) {
		this.containChild = containChild;
	}

	public String toZTreeJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"pId\":").append(parentId);
		sb.append(",\"name\":\"").append(name).append('\"');
		sb.append(",\"path\":\"").append(path).append('\"');
		sb.append(",\"childCount\":\"").append(childCount).append('\"');
	/*	sb.append(",\"shareChildFlag\":\"").append(true).append('\"');
		sb.append(",\"showContactFlag\":\"").append(true).append('\"');	*/
		sb.append(",\"userId\":\"").append(userId).append('\"');		
		sb.append(",\"entId\":\"").append(enterpriseId).append('\"');		
		sb.append('}');
		return sb.toString();
	}
	public String toEZTreeJSON(boolean showContactFlag) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"pId\":").append(parentId);
		sb.append(",\"name\":\"").append(name);
		sb.append("(");
		sb.append(showContactFlag?"显示,":"不显示,");
		sb.append(childCount);
		sb.append(")");	
		sb.append('\"');
		sb.append(",\"path\":\"").append(path).append('\"');
		sb.append(",\"childCount\":\"").append(childCount).append('\"');
		sb.append(",\"shareChildFlag\":\"").append(true).append('\"');
		sb.append(",\"showContactFlag\":\"").append(showContactFlag).append('\"');		
		sb.append(",\"userId\":\"").append(userId).append('\"');		
		sb.append(",\"entId\":\"").append(enterpriseId).append('\"');		
		sb.append('}');
		return sb.toString();
	}


	public String toJSON() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"id\":" + id);
		sb.append(",\"parentId\":" + parentId);
		sb.append(",\"userId\":" + userId);
		sb.append(",\"name\":\"" + name + "\"");
		sb.append(",\"path\":\"" + path + "\"");
		sb.append(",\"type\":" + type);
		sb.append(",\"enterpriseId\":" + enterpriseId);
		sb.append(",\"childCount\":" + childCount);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "ContactGroup [id=" + id + ", userId=" + userId + ", parentId="
				+ parentId + ", name=" + name + ", path=" + path + ", type="
				+ type + ", enterpriseId=" + enterpriseId + ", childCount="
				+ childCount + "]";
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}



}

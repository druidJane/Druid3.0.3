/*
 * Copyright (c) 2016年10月12日 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.dto;


import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.utils.ListUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:liujiawen@wxchina.com">Jiawen.Liu</a>
 * @version 1.0.0
 * @Description
 * @date 2016-10-12
 */
public class PermDto {

	private int id;
	private String displayName;
	private int parentId;
	private boolean isMenu;
	private boolean isDisplay;
	private String remark;
	private int type;
	private int level;
	private int industryType;
	private String url;
	private int platformId;
	private boolean base = false; //是否为基础权限
	private List<DataScope> dataScopes; //数据域
	private List<PermDto> sub;// 子权限列表
	private List<Integer> dependIds; //依赖权限ID

	public static PermDto copyFromPerm(Permission permission) {
		if (null == permission) {
			return null;
		}
		PermDto perm = new PermDto();
		perm.setId((Integer) permission.getId());
		perm.setParentId(permission.getParentId());
		perm.setDisplayName(permission.getDisplayName());
		perm.setMenu(permission.isMenu());
		perm.setDisplay(permission.isDisplay());
		perm.setRemark(permission.getRemark());
		perm.setType(permission.getType());
		perm.setLevel(permission.getLevel());
		perm.setBase(permission.getBase());
		perm.setIndustryType(permission.getIndustryType());
		perm.setUrl(permission.getPermURL());
		if (StringUtils.isNotBlank(permission.getDataScopes())) {
			List<DataScope> scopes = new ArrayList<>();
			String [] scopeStrs = permission.getDataScopes().split(",");
			for (String str : scopeStrs) {
				scopes.add(DataScope.getScope(Integer.valueOf(str)));
			}
			perm.setDataScopes(scopes);
		}
		if (StringUtils.isNotBlank(permission.getDependIds())) {
			List<Integer> dependIds = new ArrayList<>();
			String [] dependIdStrs = permission.getDependIds().split(",");
			for (String str : dependIdStrs) {
				dependIds.add(Integer.valueOf(str));
			}
			perm.setDependIds(dependIds);
		}
		perm.setPlatformId(permission.getPlatformId());
		perm.setSub(copyFromPerms(permission.getChildren()));
		return perm;
	}

	public static List<PermDto> copyFromPerms(List<Permission> permissions) {
		if (ListUtil.isBlank(permissions)) {
			return Collections.emptyList();
		}
		List<Permission> copyPerms = new ArrayList<>();
		Collections.addAll(copyPerms, new Permission[permissions.size()]);
		Collections.copy(copyPerms, permissions);
		filterIsDisplayPerms(permissions, copyPerms);
		List<PermDto> list = new ArrayList<>();
		for (Permission permission : copyPerms) {
			list.add(copyFromPerm(permission));
		}
		return list;
	}

	private static void filterIsDisplayPerms(List<Permission> permissions, List<Permission> copyPerms) {
		for (Permission permission : permissions) {
			if (permission.getChildren() != null) {
				int childSize = permission.getChildren().size();
				if (childSize > 1) {
					filterIsDisplayPerms(permission.getChildren(), copyPerms);
				} else if (!permission.isDisplay() && childSize == 1) {
					changeParentPermId(copyPerms, permission.getId(),
							permission.getParentId(), permission.getChildren().get(0));
				}
			}
		}
	}

	private static void changeParentPermId(List<Permission> copyPerms, int curPermId,
										   int parentId, Permission singleChildPerm) {
		singleChildPerm.setParentId(parentId);
		changePermNode(parentId, curPermId, copyPerms, singleChildPerm);

	}

	private static void changePermNode(int parentId, int curPermId, List<Permission> copyPerms,
										  Permission singleChildPerm) {
		for (Permission perm : copyPerms) {
			if (perm.getId() == parentId) {
				List<Permission> parentChildPerms = perm.getChildren();
				for (int i = 0; i < parentChildPerms.size(); i++) {
					if (parentChildPerms.get(i).getId() == curPermId) {
						parentChildPerms.set(i, singleChildPerm);
						break;
					}
				}
				perm.setChildren(parentChildPerms);
				break;
			}
			if (perm.getChildren() != null && perm.getChildren().size() > 1) {
				changePermNode(parentId, curPermId, perm.getChildren(), singleChildPerm);
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean isMenu() {
		return isMenu;
	}

	public void setMenu(boolean menu) {
		isMenu = menu;
	}

	public boolean isDisplay() {
		return isDisplay;
	}

	public void setDisplay(boolean display) {
		isDisplay = display;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIndustryType() {
		return industryType;
	}

	public void setIndustryType(int industryType) {
		this.industryType = industryType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<DataScope> getDataScopes() {
		return dataScopes;
	}

	public void setDataScopes(List<DataScope> dataScopes) {
		this.dataScopes = dataScopes;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}


	public List<PermDto> getSub() {
		return sub;
	}

	public void setSub(List<PermDto> sub) {
		this.sub = sub;
	}

	public List<Integer> getDependIds() {
		return dependIds;
	}

	public void setDependIds(List<Integer> dependIds) {
		this.dependIds = dependIds;
	}

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}
}

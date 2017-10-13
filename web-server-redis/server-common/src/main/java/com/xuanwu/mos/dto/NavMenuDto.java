/*
 * Copyright (c) 2016 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.dto;

import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.utils.ListUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:liqihui@wxchina.com">Qihui.Li</a>
 * @version 1.0.0
 * @date Aug 3, 2016
 */
public class NavMenuDto implements Serializable{

	public static Comparator<NavMenuDto> MENUDTO_COMPARATOR = new Comparator<NavMenuDto>() {
		@Override
		public int compare(NavMenuDto o1, NavMenuDto o2) {
			if (null == o1) {
				return -1;
			}
			if (null == o2) {
				return 1;
			}
			return o1.getSort() - o2.getSort();
		}
	};

	private Integer id;
	/**
	 * 菜单名称
	 */
	private String menuName;

	private String displayName;
	/**
	 * 资源路径
	 */
	private String url;
	/**
	 * 图标样式
	 */
	private String icoCls;

	private int sort;

	private List<NavMenuDto> lists;

	public static NavMenuDto copyFromSysPermission(Permission permission) {
		NavMenuDto menu = new NavMenuDto();
		String url = permission.getPermURL();
		menu.setId((Integer) permission.getId());
		menu.setMenuName(permission.getMenuDisplayName());
		menu.setDisplayName(permission.getDisplayName());
		if (StringUtils.isNotBlank(url)) {
			url = "#" + url;
		}
		menu.setUrl(url);
		menu.setIcoCls(permission.getMenuImagePath());
		menu.setSort(permission.getMenuDisplayOrder());
		menu.setLists(new ArrayList<NavMenuDto>()); // Add a empty list if not
													// children exist

		return menu;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcoCls() {
		return icoCls;
	}

	public void setIcoCls(String icoCls) {
		this.icoCls = icoCls;
	}

	public List<NavMenuDto> getLists() {
		return lists;
	}

	public void setLists(List<NavMenuDto> lists) {
		this.lists = lists;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void sort() {
		if (ListUtil.isNotBlank(lists)) {
			Collections.sort(lists, MENUDTO_COMPARATOR);
			for (NavMenuDto menuDto : lists) {
				menuDto.sort();
			}
		}
	}

	@Override
	public String toString() {
		return "NavMenuDto{" + "id=" + id + ", name='" + menuName + '\'' + ", url='" + url + '\'' + ", icoCls='" + icoCls
				+ '\'' + ", lists=" + lists + '}';
	}
}

/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.utils;


import com.xuanwu.mos.domain.entity.Permission;
import com.xuanwu.mos.dto.PermDto;

import java.util.*;

/**
 * @Description: 父子权限归属处理和排序
 * @Author <a href="jiji@javawind.com">Xuefang.Xu</a>
 * @Date 2012-9-7
 * @Version 1.0.0
 */
public class PermissionUtil {

	private final static String CHILD = "：";
	private final static String COM = "，";
	private final static String SEM = "；";
	private final static String LP = "(";
	private final static String RP = ")";

	/**
	 * 排列和归类权限
	 */
	public static List<PermDto> sortPermissions(List<Permission> permissions) {
		List<Permission> list = new ArrayList<Permission>();
		if (permissions == null || permissions.size() == 0) {
			return Collections.emptyList();
		}
		Map<Integer, List<Permission>> parentMap = new HashMap<>();
		for (Permission p : permissions) {
			int parentId = p.getParentId();
			if (parentId == 0 && p.isMenu()) {
				list.add(p);
			} else {
				putPermMap(parentMap, p);
			}
		}
		setSubPerms(list, parentMap);
		return PermDto.copyFromPerms(list);
	}

	private static void putPermMap(Map<Integer, List<Permission>> parentMap, Permission p) {
		List<Permission> perms = parentMap.get(p.getParentId());
		if (null == perms) {
			perms = new ArrayList<>();
			parentMap.put(p.getParentId(), perms);
		}
		perms.add(p);
	}

	private static void setSubPerms(List<Permission> list, Map<Integer, List<Permission>> parentMap) {
		for (Permission perm : list) {
			int id = (int) perm.getId();
			List<Permission> subs = parentMap.get(id);
			if (ListUtil.isNotBlank(subs)) {
				setSubPerms(subs, parentMap);
				perm.setChildren(subs);
			}
		}
	}

	/**
	 * 选中权限
	 */
	public static List<Permission> checkPermissions(List<Permission> permissions, List<Permission> checked) {
		if (checked == null || checked.size() == 0) {
			return permissions;
		}

		for (Permission p : permissions) {
			for (Permission c : checked) {
				if (c.getId() == p.getId()) {
					p.setChecked(true);
					p.setDataScope(c.getDataScope());
					break;
				}
			}
			List<Permission> children = p.getChildren();
			if (children != null && children.size() > 0) {
				checkPermissions(children, checked);
			}
		}

		return permissions;
	}

	/**
	 * 生成权限字符串
	 */
	/*public static String getPermissionsStr(List<Permission> permissions) {
		if (permissions == null || permissions.size() == 0) {
			return "";
		}

		List<PermDto> list = sortPermissions(permissions);

		StringBuffer sb = new StringBuffer();
		for (PermDto p : list) {
			sb.append(getPermissionStr(p) + SEM);
		}
		return sb.toString();
	}*/

	/**
	 * 生成权限字符串
	 */
	public static String getPermissionStr(PermDto permission) {
		if (permission == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(permission.getDisplayName());
		if (permission.getParentId() == 0) {
			sb.append(CHILD);
		}

		if (permission.getSub() != null) {
			if (permission.getParentId() > 0) {
				sb.append(LP);
			}
			for (PermDto c : permission.getSub()) {
				String str = sb.toString();
				if (!str.endsWith(CHILD) && !str.endsWith(LP)) {
					sb.append(COM);
				}
				sb.append(getPermissionStr(c));
			}
			if (permission.getParentId() > 0) {
				sb.append(RP);
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		List<Permission> list = new ArrayList<Permission>();
		Permission a = new Permission();
		a.setId(1);
		a.setParentId(0);
		a.setDisplayName("根目录");
		list.add(a);

		Permission b = new Permission();
		b.setId(2);
		b.setParentId(1);
		b.setDisplayName("一级目录");
		list.add(b);

		Permission c = new Permission();
		c.setId(3);
		c.setParentId(1);
		c.setDisplayName("一级目录");
		list.add(c);

		Permission d = new Permission();
		d.setId(4);
		d.setParentId(3);
		d.setDisplayName("二级目录");
		list.add(d);

		Permission e = new Permission();
		e.setId(5);
		e.setParentId(1);
		e.setDisplayName("一级目录");
		list.add(e);

		Permission f = new Permission();
		f.setId(6);
		f.setParentId(5);
		f.setDisplayName("二级目录");
		list.add(f);

		Permission g = new Permission();
		g.setId(7);
		g.setParentId(6);
		g.setDisplayName("三级目录");
		list.add(g);

		Permission h = new Permission();
		h.setId(8);
		h.setParentId(0);
		h.setDisplayName("根目录");
		list.add(h);

		 /*List<PermDto> ls = sortPermissions(list);*/
		//System.out.println(getPermissionsStr(ls));
		System.out.println("----------------------------------");
		/*for (PermDto p : ls) {
			System.out.println(getPermissionStr(p));
		}*/
	}
}

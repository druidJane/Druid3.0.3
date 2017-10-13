/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-7-13
 * @Version 1.0.0
 */
public class ListUtil {

	/**
	 * Null-safe check if the specified collection is empty.
	 * <p>
	 * Null returns true.
	 * 
	 * @param list
	 *            the collection to check, may be null
	 * @return true if empty or null
	 */
	public static <T> boolean isBlank(List<T> list) {
		return (list == null || list.isEmpty());
	}

	/**
	 * Null-safe check if the specified collection is not empty.
	 * <p>
	 * Null returns false.
	 * 
	 * @param list
	 *            the collection to check, may be null
	 * @return true if non-null and non-empty
	 */
	public static <T> boolean isNotBlank(List<T> list) {
		return !isBlank(list);
	}

	public static <T> List<T> cloneList(List<T> list) {
		List<T> temp = new ArrayList<T>();
		if (list == null) {
			return temp;
		}
		temp.addAll(list);
		return temp;
	}

	public static <T> void clearList(List<T> list) {
		if (list != null) {
			list.clear();
		}
	}

	public static <T> List<List<T>> splitList(List<T> list, int maxSize) {
		List<List<T>> temp = new ArrayList<List<T>>();
		if (list.size() == 0 || maxSize <= 0) {
			return temp;
		}
		int i = 0;
		while (i < list.size()) {
			int toIndex = (i + maxSize);
			if (toIndex > list.size()) {
				toIndex = toIndex - maxSize + (list.size() - i);
			}
			temp.add(new ArrayList<T>(list.subList(i, toIndex)));
			i += maxSize;
		}
		return temp;
	}
	
	public static <T> List<T> map2List(Collection<T> vals){
		List<T> ts = new ArrayList<T>();
		if(vals == null){
			return ts;
		}
		Iterator<T> it = vals.iterator();
		while(it.hasNext()){
			ts.add(it.next());
		}
		return ts;
	}
}

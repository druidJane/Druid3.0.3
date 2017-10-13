package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

/**
 * 运营商名称
 * @author lizongxian
 *  
 */
public class Carrier extends AbstractEntity {
	/**
	 * 运营商ID
	 */
	private int id;
	/**
	 * 运营商名称
	 */
	private String name;
	/**
	 * 排序值
	 */
	private int sort;
	/**
	 * 是否显示
	 */
	private int showed;
	
	private boolean selected;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getShowed() {
		return showed;
	}

	public void setShowed(int showed) {
		this.showed = showed;
	}

	/**
	* @param selected 要设置的 selected
	*/
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	* @return selected
	*/
	public boolean isSelected() {
		return selected;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id);
		sb.append(",\"name\":\"").append(name).append("\"");
		sb.append('}');
		return sb.toString();
	}
	

}

package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 公告管理
 * @Data 2017-4-1
 * @Version 1.0.0
 */
public class Announcement extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/* 企业ID */
	private int entId;
	/* 公告标题 */
	@NotEmpty
	private String title;
	/* 公告内容 */
	@NotEmpty
	private String content;
	/* 是否显示.默认显示 */
	private int showState;
	/* 公告的级别 */
	private int level;
	/* 发布用户ID,默认为当前用户 */
	private int postUserId;
	/* 发布部门ID，默认为当前用户所在部门 */
	private int postDepartmentId;
	/* 发布时间 */
	private Date postTime;
	/* 更新时间 */
	private Date updateTime;
	/* 过期时间 */
	private Date expiredTime;
	/* 是否删除 */
	private boolean isRemove;
	/* 发布用户名 */
	private String postUserName;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getShowState() {
		return showState;
	}

	public void setShowState(int showState) {
		this.showState = showState;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPostUserId() {
		return postUserId;
	}

	public void setPostUserId(int postUserId) {
		this.postUserId = postUserId;
	}

	public int getPostDepartmentId() {
		return postDepartmentId;
	}

	public void setPostDepartmentId(int postDepartmentId) {
		this.postDepartmentId = postDepartmentId;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public boolean isRemove() {
		return isRemove;
	}

	public void setRemove(boolean remove) {
		isRemove = remove;
	}

	public String getPostUserName() {
		return postUserName;
	}

	public void setPostUserName(String postUserName) {
		this.postUserName = postUserName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}

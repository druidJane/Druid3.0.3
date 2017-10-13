package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.OperationType;

import java.util.Date;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 系统日志
 * @Data 2017-3-30
 * @Version 1.0.0
 */
public class SystemLog extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;

	/** 用户ID **/
	private int userId;

	/** 用户名 **/
	private String userName;

	/** 操作时间 **/
	private Date operateTime;

	/** MVC区域名称 **/
	private String areaName;

	/** MVC控制器名称 **/
	private String controllerName;

	/** MVC动作名称 **/
	private String actionName;

	/** 操作对象 **/
	private String operationObj;

	/** 操作内容 **/
	private String content;

	/** 操作类型：新增（NEW），修改（MODIFY）,删除（DELETE）, **/
	private OperationType operationType;

	private String showOperationType;

	/** 提交方式：GET:0， POST：1，目前全设置为1 */
	private int formMethod;

	/** 企业ID **/
	private int enterpriseId;

	/** 备注 **/
	private String remark;
	
	public String getShowOperationType() {
		if (operationType != null) {
			return operationType.getOperationName();
		}
		return null;
	}

	public void setShowOperationType(String showOperationType) {
		this.showOperationType = showOperationType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
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

	public String getOperationObj() {
		return operationObj;
	}

	public void setOperationObj(String operationObj) {
		this.operationObj = operationObj;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFormMethod() {
		return formMethod;
	}

	public void setFormMethod(int formMethod) {
		this.formMethod = formMethod;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public OperationType getOperationType() {
		return operationType;
	}
	

	public void setOperationType(int operationType) {
		this.operationType = OperationType.getOperationType(operationType);
	}
}

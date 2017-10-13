/*   
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.file.importer.ImportInfoBuild;
import com.xuanwu.mos.utils.XmlUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件任务
 * 历史原因，gsms_file_hanlder_task表废弃，与实体UserTask一同映射gsms_user_task
 * 相关属性映射user_task相关字段
 * @author 林泽强
 */
public class FileTask extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId; // 操作用户
	private String taskName;
	private Integer percent; // 完成百分比
	private TaskState state; // 状态:0--未开始,1--处理中,2--已完成
	private String fileName;
	private Long fileSize;
	private TaskType type; // 类型: 1--导入,2--导出
	private BizDataType dataType; // 数据类型:用户,联系人
	private Date postTime; // 提交时间
	private Date handleTime; // 处理时间
	private Date commitTime; // 处理完成时间
	private Integer platformId; // 0:Backend; 2:FrontKit;
	private String progress; // 处理进度
	private String message; // 处理结果报告
	private TaskResult result; // 任务结果
	private String delimiter;
	private String parameter;
	private Integer taskType = 1;//前台系统插入任务类型，默认为1，由前台系统处理
	private String fileAddress;

	public String getFileAddress() {
		return fileAddress;
	}

	public void setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	// 导入信息构造器
	private ImportInfoBuild importInfoBuild = new ImportInfoBuild();
	// 已处理数据量
	private Integer handledCount;
	// 总数据量
	private Integer total;
	// 参数map
	private Map<String, String> paramsMap = new HashMap<>();
	//任务完成之后是否阅读
	private NoticeState isRead;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	public TaskState getState() {
		return state;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

	public BizDataType getDataType() {
		if(dataType==null){
			dataType = BizDataType.Other;
		}
		return dataType;
	}

	public Integer getDataTypeIndex() {
		return dataType.getIndex();
	}

	public void setDataType(BizDataType dataType) {
		this.dataType = dataType;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public NoticeState getIsRead() {
		return isRead;
	}

	public void setIsRead(NoticeState isRead) {
		this.isRead = isRead;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TaskResult getResult() {
		return result;
	}

	public void setResult(TaskResult result) {
		this.result = result;
	}

	public ImportInfoBuild getImportInfoBuild() {
		return importInfoBuild;
	}

	public Integer getHandledCount() {
		return handledCount;
	}

	public void setHandledCount(Integer handledCount) {
		this.handledCount = handledCount;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// 当前任务百分比
	public int getCurPercent() {
		if (handledCount == null || handledCount == 0) {
			return 0;
		}
		if (total == null || total == 0) {
			return 0;
		}

		return (int) (((double) handledCount / (double) total) * 100);
	}

	public void setHanldePercent(int percent) {
		this.percent = percent;
		if (this.percent > 100)
			this.percent = 100;
	}

	// 添加执行数据量
	public void addHandledCount(int count) {
		if (this.handledCount == null) {
			this.handledCount = 0;
		}
		this.handledCount += count;
	}

	public String getParameters() {
		return (paramsMap == null || paramsMap.isEmpty()) ? "" : XmlUtil.toXML(paramsMap);
	}

	@SuppressWarnings("unchecked")
	public void setParameters(String parameters) {
		if (StringUtils.isNotBlank(parameters)) {
			try {

				this.paramsMap = (Map<String, String>) XmlUtil.fromXML(parameters);
			} catch (Exception e) {
				//data-server 任务设置该字段格式为[{"key":"value"},{}....]捕获异常，不做处理
				this.parameter = parameters;
			}
		}
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Map<String, String> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}

	// 设置状态枚举索引
	public void setStateIndex(int index) {
		this.state = TaskState.getState(index);
	}

	// 设置结果枚举索引
	public void setResultIndex(int index) {
		this.result = TaskResult.getResult(index);
	}

	// 设置任务类型枚举索引
	public void setTypeIndex(int index) {
		this.type = TaskType.getType(index);
	}

	// 设置数据类型枚举索引
	public void setDataTypeIndex(int index) {
		this.dataType = BizDataType.getType(index);
	}

	@Override
	public String toString() {
		return "FileTask{" + "id=" + id + ", userId=" + userId + ", taskName='" + taskName + '\'' + ", percent="
				+ percent + ", state=" + state + ", fileName='" + fileName + '\'' + ", fileSize=" + fileSize + ", type="
				+ type + ", dataType=" + dataType + ", postTime=" + postTime + ", handleTime=" + handleTime
				+ ", commitTime=" + commitTime + ", platformId=" + platformId + ", progress='" + progress + '\''
				+ ", message='" + message + '\'' + ", result=" + result + ", delimiter='" + delimiter + '\''
				+ ", importInfoBuild=" + importInfoBuild + ", handledCount=" + handledCount + ", total=" + total
				+ ", paramsMap=" + paramsMap + '}';
	}

	public String getProgress() {
		 if(!StringUtils.isBlank(progress))
		 return progress;
		 if(handledCount != null && total != null) {
			 StringBuilder sb = new StringBuilder();
			 sb.append(handledCount);
			 sb.append("/");
			 sb.append((total));
			 return sb.toString();
		 }else {
			 return "计算中...";
		 }
	}

	public String getStateName() {
		switch (this.state) {
		case Wait:
			return "未开始";
		case Handle:
			return "处理中";
		case Over:
			return "已完成";
		default:
			return "异常";
		}
	}

	public String getParameter(String key) {
		return paramsMap.get(key);
	}
}

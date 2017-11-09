package com.xuanwu.module.service;

import com.xuanwu.mos.domain.entity.UserTask;
import com.xuanwu.mos.domain.enums.MosBizDataType;
import com.xuanwu.mos.domain.enums.TaskState;
import com.xuanwu.mos.domain.enums.TaskType;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.UserTaskRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zhangz on 2017/5/16.
 */
@Service
public class UserTaskService {
    @Autowired
    private UserTaskRepo userTaskRepo;

    @Autowired
    private FileTaskService fileTaskService;

    public void insert(UserTask userTask) throws RepositoryException {
        userTaskRepo.insert(userTask);
    }

    public void addImportTask(MosBizDataType dataType, UserTask task) throws RepositoryException {
        if(null == task.getCreateUser()){
            throw new BusinessException("createUser cannot be null!");
        }
        String uploadFileName = task.getUploadFileName();
        task.setUploadFileName(uploadFileName);
        String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".")).toLowerCase();// 文件后缀
        task.setFileType(suffix);
        task.setTaskName(dataType.getTaskName());
        task.setTaskCode(dataType.getIndex());
        Date currentDate = new Date();
        task.setCreateTime(currentDate);
        task.setStartTime(currentDate);
        task.setProgress("0/0");
        task.setState(TaskState.Wait);
        task.setOperateType(TaskType.Import);
        task.setResultCode(0);
        task.setPercent(0);
        userTaskRepo.insert(task);
        fileTaskService.updateUserUpAndLoadCurVersion(task.getCreateUser());
    }
    public void addExportTask(MosBizDataType dataType, UserTask task) throws RepositoryException {
        if(null == task.getCreateUser()){
            throw new BusinessException("createUser cannot be null!");
        }
        if(task.getTaskName()!=null && !task.getTaskName().equals("")){
        	 task.setTaskName(dataType.getTaskName()+"("+task.getTaskName()+")");
        }else{
        	task.setTaskName(dataType.getTaskName());
        }  
        task.setTaskCode(dataType.getIndex());
        Date currentDate = new Date();
        task.setCreateTime(currentDate);
        task.setStartTime(currentDate);
        task.setProgress("0/0");
        task.setState(TaskState.Wait);
        task.setOperateType(TaskType.Export);
        task.setResultCode(0);
        task.setPercent(0);
        userTaskRepo.insert(task);
        fileTaskService.updateUserUpAndLoadCurVersion(task.getCreateUser());
    }
}

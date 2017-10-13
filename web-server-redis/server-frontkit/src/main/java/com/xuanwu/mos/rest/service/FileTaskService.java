package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.FileTask;
import com.xuanwu.mos.domain.enums.TaskType;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.FileTaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangz on 2017/4/1.
 */
@Service
public class FileTaskService {
    private Map<Integer,AtomicInteger> userUpAndLoadPreVersionMap = new ConcurrentHashMap<>();
    private Map<Integer,AtomicInteger> userUpAndLoadCurVersionMap = new ConcurrentHashMap<>();
    @Autowired
    private FileTaskRepo fileTaskRepo;

    public FileTask save(FileTask task) throws RepositoryException {
        fileTaskRepo.save(task);
        updateUserUpAndLoadCurVersion(task.getUserId());
        return task;
    }
    public int commitHandlingFileTask(FileTask task) {
        return fileTaskRepo.updateHandlingTask(task);
    }
    public List<Integer> fetchUnHandledTaskIDs(int platformId) {
        return fileTaskRepo.fetchUnHandledTaskIDs(platformId);
    }
    public List<FileTask> fetchUnLoadTasks(List<Integer> unLoadTaskIDs) {
        return fileTaskRepo.fetchUnLoadTasks(unLoadTaskIDs);
    }
    public List<FileTask> fetchUnHandledTasks(int platformId) {
        return fileTaskRepo.fetchUnHandledTasks(platformId);
    }

    public int commitHandledFileTask(FileTask task) {
        int count = fileTaskRepo.updateHandledTask(task);
        updateUserUpAndLoadCurVersion(task.getUserId());
        return count;
    }
    public List<FileTask> findTasks(QueryParameters params) {
        return fileTaskRepo.findResults(params);
    }

    public int findTasksCount(QueryParameters params){
        return fileTaskRepo.findResultCount(params);
    }

    public List findResultCountByUserId(Integer userId) {
        return fileTaskRepo.findResultCountByUserId(userId);
    }

    public int updateReadStateByUserId(Integer userId, Integer taskType) throws RepositoryException {
        int count = fileTaskRepo.updateReadStateByUserId(userId,taskType);
        updateUserUpAndLoadCurVersion(userId);
        return count ;
    }

    public AtomicInteger getUserUpAndLoadPreVersion(Integer user) {
        return userUpAndLoadPreVersionMap.get(user);
    }

    public void setUserUpAndLoadPreVersion(Integer user, AtomicInteger version) {
        this.userUpAndLoadPreVersionMap.put(user,version);
    }

    public AtomicInteger getUserUpAndLoadCurVersion(Integer user) {
        return userUpAndLoadCurVersionMap.get(user);
    }

    public void setUserUpAndLoadCurVersion(Integer user,AtomicInteger version) {
        this.userUpAndLoadCurVersionMap.put(user,version);
    }

    public void resetUserUpAndLoadVersion(Integer user) {
        setUserUpAndLoadPreVersion(user, new AtomicInteger(0));
        setUserUpAndLoadCurVersion(user, new AtomicInteger(1));
    }

    public void updateUserUpAndLoadCurVersion(Integer user) {
        if (getUserUpAndLoadCurVersion(user) == null) {
            setUserUpAndLoadCurVersion(user, new AtomicInteger(1));
        } else {
            getUserUpAndLoadCurVersion(user).getAndIncrement();
        }
    }
}

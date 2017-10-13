package com.xuanwu.mos.service;

import com.xuanwu.mos.config.SyncTask;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.SystemLog;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.repo.SysLogRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.utils.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 日志管理
 * @Data 2017-3-30
 * @Version 1.0.0
 */
@Service
public class SysLogService implements SyncTask {

	private static final Logger logger = LoggerFactory.getLogger(SysLogService.class);

	private LinkedBlockingDeque<SystemLog> addQueue = new LinkedBlockingDeque<SystemLog>();

	@Autowired
	private SysLogRepo sysLogRepo;

	public boolean addSysLog(SystemLog sysLog) {
		return addQueue.add(sysLog);
	}

	public int count(QueryParameters params) {
		return sysLogRepo.findResultCount(params);
	}

	public Collection<SystemLog> list(QueryParameters params) {
		return sysLogRepo.findResults(params);
	}

	@Override
	public long getPeriod() {
		return 5000;
	}
	
	public void addLog(SimpleUser user,OperationType operationType,String operationObj,String controllerName,String actionName,String content){
		SystemLog sysLog = new SystemLog();
        sysLog.setOperateTime(new Date());
        sysLog.setUserId(user.getId());
        sysLog.setEnterpriseId(user.getEnterpriseId());
        sysLog.setOperationType(operationType.getIndex());
        sysLog.setActionName(actionName);
        sysLog.setControllerName(controllerName);
        sysLog.setOperationObj(operationObj);
        sysLog.setContent(content);
        sysLog.setUserName(user.getUsername());
        sysLog.setFormMethod(1);
        addSysLog(sysLog);
	}

	@Override
	public void run() {
		try{
			if(addQueue.isEmpty()){
				return;
			}
			List<SystemLog> logs = new ArrayList<SystemLog>();
			addQueue.drainTo(logs);
			List<List<SystemLog>> subs = ListUtil.splitList(logs, 1000);
			for(List<SystemLog> sub : subs){
				sysLogRepo.save(sub);
			}
		} catch (Exception e){
			logger.error("Store system log failed: ", e);
		}
	}
}

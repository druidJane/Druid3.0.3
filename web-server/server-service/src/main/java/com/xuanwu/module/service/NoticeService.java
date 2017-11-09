package com.xuanwu.module.service;


import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.entity.Notice;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.NoticeScope;
import com.xuanwu.mos.domain.enums.NoticeState;
import com.xuanwu.mos.domain.repo.NoticeRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.utils.SessionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zengzl 2017-4-20  消息通知
 */
@Component
public class NoticeService {
    private Logger logger = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    private NoticeRepo noticeRepo;

    @Autowired
    private PlatformMode platformMode;

    private Map<Integer,AtomicInteger> userNoticePreVersionMap = new ConcurrentHashMap<>();

    private Map<Integer,AtomicInteger> userNoticeCurVersionMap = new ConcurrentHashMap<>();


    public int count(QueryParameters params) {
        return noticeRepo.findResultCount(params);
    }

    public Collection<Notice> list(QueryParameters params) {
        return noticeRepo.findResults(params);
    }

    public int deleteDetailByIds(List<Integer> ids) throws RepositoryException {
        SimpleUser user = SessionUtil.getCurUser();
        List<Integer> users = new ArrayList<>();
        users.add(user.getId());
        updateUserNoticeVersion(users);
        return noticeRepo.deleteDetailByIds(ids);
    }

    @Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
    public void updateByObjectId(String objectId,String title,Integer user) throws RepositoryException {
        Notice notice = noticeRepo.getNoticeByObjectId(objectId);
        noticeRepo.updateNotice(notice.getId(),user,title,new Date());
        List<Integer> users = new ArrayList<>();
        if (notice.getScope() == NoticeScope.NONE) {//系统公告,针对该企业全部用户
            users = noticeRepo.getUsersByEnterpriseId(notice.getEnterpriseId());
        } else if (notice.getScope() == NoticeScope.PERSONAL) { //仅个人
            users.add(notice.getReadUser());
        } else if (notice.getScope() == NoticeScope.PERMISSION) { //按权限
            users = noticeRepo.getUsersByPermission(platformMode.getPlatform(),getUrlByPermission(notice.getReadPermission()),notice.getEnterpriseId());
        }
        if (users.size() > 0) {
            noticeRepo.updateDetails(users, notice.getId(), NoticeState.UNREAD.getIndex());
            updateUserNoticeVersion(users);
        }
    }

    @Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
    public void deleteByObjectId(String objectId) throws RepositoryException {
        Notice notice = noticeRepo.getNoticeByObjectId(objectId);
        noticeRepo.deleteById(notice.getId(), notice.getEnterpriseId());
        List<Integer> affectUsers = noticeRepo.getUsersByMessageId(notice.getId());
        updateUserNoticeVersion(affectUsers);
        noticeRepo.deleteDetailsByMessageId(notice.getId());
    }


    @Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
    public void insertNotice(Notice notice) throws RepositoryException {
        noticeRepo.insert(notice);
        List<Integer> users = new ArrayList<>();
        if (notice.getScope() == NoticeScope.NONE) {//系统公告,针对该企业全部用户
            users = noticeRepo.getUsersByEnterpriseId(notice.getEnterpriseId());
        } else if (notice.getScope() == NoticeScope.PERSONAL) { //仅个人
            users.add(notice.getReadUser());
        } else if (notice.getScope() == NoticeScope.PERMISSION) { //按权限
            users = noticeRepo.getUsersByPermission(platformMode.getPlatform(),getUrlByPermission(notice.getReadPermission()),notice.getEnterpriseId());
        }
        if (users.size() > 0) {
            noticeRepo.insertDetails(users, notice.getId(), NoticeState.UNREAD.getIndex());
            updateUserNoticeVersion(users);
        }
    }

    private void updateUserNoticeVersion(List<Integer> users) {
        for (Integer user:users) {
            if (getUserNoticeCurVersion(user) == null) {
                setUserNoticeCurVersion(user,new AtomicInteger(1));
            } else {
                getUserNoticeCurVersion(user).getAndIncrement();
            }
        }
    }

    private String getUrlByPermission(Integer permissionId) {
        switch (permissionId) {
            case 1:
                return Keys.SMSMGR_SENDTRACKING_CHECKBATCH;//TO-D0
            case 2:
                return Keys.SMSMGR_SENDTRACKINGMMS_CHECKBATCH;//TO-DO
            default:
                return null;
        }
    }


    public int updateStateByIds(List<Integer> ids) throws RepositoryException {
        SimpleUser user = SessionUtil.getCurUser();
        List<Integer> users = new ArrayList<>();
        users.add(user.getId());
        updateUserNoticeVersion(users);
        return noticeRepo.updateStateByIds(ids);
    }

    public AtomicInteger getUserNoticePreVersion(Integer user) {
        return userNoticePreVersionMap.get(user);
    }

    public void setUserNoticePreVersion(Integer user,AtomicInteger version) {
        this.userNoticePreVersionMap.put(user,version);
    }

    public AtomicInteger getUserNoticeCurVersion(Integer user) {
        return userNoticeCurVersionMap.get(user);
    }

    public void setUserNoticeCurVersion(Integer user,AtomicInteger version) {
        this.userNoticeCurVersionMap.put(user,version);
    }

    public void resetUserNoticeVersion(Integer user) {
       setUserNoticePreVersion(user,new AtomicInteger(0));
       setUserNoticeCurVersion(user,new AtomicInteger(1));
    }

}

package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.Announcement;
import com.xuanwu.mos.domain.entity.Notice;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.NoticeMsgType;
import com.xuanwu.mos.domain.enums.NoticeScope;
import com.xuanwu.mos.domain.enums.ShowState;
import com.xuanwu.mos.domain.repo.AnnouncementRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 公告管理
 * @Data 2017-4-1
 * @Version 1.0.0
 */
@Service
public class AnnouncementService {

	@Autowired
	private AnnouncementRepo announcementRepo;

	@Autowired
	private NoticeService noticeService;

	public int count(QueryParameters params) {
		return announcementRepo.findResultCount(params);
	}

	public Collection<Announcement> list(QueryParameters params) {
		return announcementRepo.findResults(params);
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean updateAnnouncement(Announcement announcement) throws RepositoryException {
		Announcement oldAnnouncement = announcementRepo.findAnnouncementById(announcement.getId());
		int ret = announcementRepo.update(announcement);
		ShowState oldShowState = ShowState.getState(oldAnnouncement.getShowState());
		ShowState newShowState = ShowState.getState(announcement.getShowState());
		if (ShowState.SHOW.equals(oldShowState) && ShowState.SHOW.equals(newShowState)) {
			//修改公共消息，并重新推送消息
			noticeService.updateByObjectId(String.valueOf(announcement.getId()),
					announcement.getTitle(),announcement.getId());
		} else if (ShowState.HIDE.equals(oldShowState) && ShowState.SHOW.equals(newShowState)) {
			addNotice(announcement);
		} else if (ShowState.SHOW.equals(oldShowState) && ShowState.HIDE.equals(newShowState)) {
			noticeService.deleteByObjectId(String.valueOf(announcement.getId()));
		}
		return ret > 0;
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean deleteAnnouncements(Integer[] ids) throws RepositoryException {
		Map<Integer, Announcement> announcementMap = announcementRepo.findAnnouncementByIds(ids);
		int count =  announcementRepo.updateByIds(ids);
		//删除公告消息通知
		for (Integer objectId:ids) {
			int showState = announcementMap.get(objectId).getShowState();
			if (ShowState.SHOW.getIndex() == showState) {
				noticeService.deleteByObjectId(String.valueOf(objectId));
			}
		}
		return count > 0;
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public void addAnnouncement(Announcement announcement) throws RepositoryException {
		announcementRepo.insert(announcement);
		if (ShowState.SHOW.equals(ShowState.getState(announcement.getShowState()))) {
			addNotice(announcement);
		}
	}

	private void addNotice(Announcement announcement) throws RepositoryException {
		//新增公告消息通知
		Notice notice = new Notice();
		SimpleUser curUser = SessionUtil.getCurUser();
		notice.setCreateUser(curUser.getId());
		notice.setEnterpriseId(curUser.getEnterpriseId());
		notice.setCreateTime(new Date());
		notice.setScope(NoticeScope.NONE);
		notice.setObjectId(String.valueOf(announcement.getId()));
		notice.setMessageType(NoticeMsgType.SYS_NOTICE);
		notice.setMessageTitle(announcement.getTitle());
		notice.setPushTime(new Date());
		noticeService.insertNotice(notice);
	}

	public Announcement findAnnouncementById(Integer id) {
		return announcementRepo.findAnnouncementById(id);
	}
}

package com.xuanwu.mos.rest.resource.sysmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.Announcement;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.service.AnnouncementService;
import com.xuanwu.mos.service.NoticeService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.LogContentUtil;
import com.xuanwu.mos.utils.Messages;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Date;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 公告管理
 * @Data 2017-3-24
 * @Version 1.0.0
 */
@Component
@Path(Keys.SYSTEMMGR_ANNOUNCEMENTMGR)
public class AnnouncementResource {

	@Autowired
	private AnnouncementService announcementService;
	
	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private NoticeService noticeService;

	@POST
	@Path(Keys.SYSTEMMGR_ANNOUNCEMENTMGR_GETALLANNOUNCEMENT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp list(@Valid PageReqt req){
		QueryParameters params = new QueryParameters(req);
		String endTime = (String) params.getParams().get("endTime");
		params.getParams().put("endTime", endTime + " 23:59:59");
		params.addParam("enterpriseId", SessionUtil.getCurUser().getEnterpriseId());
		int total = announcementService.count(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}
		PageInfo page = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(page);
		Collection<Announcement> announcements = announcementService.list(params);
		return PageResp.success(total, announcements);
	}

	/**
	 * 公告查看
	 */
	@GET
	@Path(Keys.SYSTEMMGR_ANNOUNCEMENTMGR_GETALLANNOUNCEMENT_DETAIL)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp detail(@QueryParam("id") Integer id){
		Announcement announcement = announcementService.findAnnouncementById(id);
		announcement.setTitle(StringUtil.replaceHtml(announcement.getTitle()));
		announcement.setContent(StringUtil.replaceHtml(announcement.getContent()));
		return PageResp.success(announcement);
	}

	/**
	 * 公告修改
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ANNOUNCEMENTMGR_MODIFYANNOUNCEMENT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp update(@Valid Announcement announcement){
		try {
			SimpleUser user = SessionUtil.getCurUser();
			announcement.setUpdateTime(new Date());
			announcement.setPostUserId(user.getId());
			announcement.setPostDepartmentId(user.getParentId());
			boolean updateResult = announcementService.updateAnnouncement(announcement);
			if (updateResult) {
				sysLogService.addLog(user,OperationType.MODIFY,"【公告管理】","Public","Login","【"+announcement.getTitle()+"】"); //添加访问日志
	            
				return PageResp.success();
			}
		} catch (RepositoryException e) {
			return PageResp.fail(Messages.SYSTEM_ERROR);
		}
		return PageResp.fail();
	}

	/**
	 * 删除公告
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ANNOUNCEMENTMGR_DELANNOUNCEMENT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp delete(Integer[] ids){
		try {
			if (ids == null || ids.length == 0) {
				return JsonResp.fail(Messages.SELECT_REMOVE_DATA);
			}
			
			if (announcementService.deleteAnnouncements(ids)) {
				StringBuilder _name = new StringBuilder();
				for(Integer id:ids){
					_name.append(announcementService.findAnnouncementById(id).getTitle()+",");
				}				
				SimpleUser user = SessionUtil.getCurUser();
				String logContent = _name.substring(0,_name.length()-1);
				sysLogService.addLog(user,OperationType.DELETE,"【公告管理】",
						"Public","Login","【"+ LogContentUtil.format(logContent)+"】"); //添加访问日志
	            
				return PageResp.success();
			}
		} catch (RepositoryException e) {
			return PageResp.fail(Messages.SYSTEM_ERROR);
		}
		return PageResp.fail();
	}

	/**
	 * 新增公告
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ANNOUNCEMENTMGR_ADDANNOUNCEMENT)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp add(@Valid Announcement announcement){
		SimpleUser curUser = SessionUtil.getCurUser();
		announcement.setPostTime(new Date());
		announcement.setUpdateTime(new Date());
		announcement.setEntId(curUser.getEnterpriseId());
		announcement.setPostUserId(curUser.getId());
		announcement.setPostDepartmentId(curUser.getParentId());
		announcement.setExpiredTime(new Date(0));
		try {
			announcementService.addAnnouncement(announcement);

			sysLogService.addLog(curUser,OperationType.NEW,"【公告管理】","Public","Login","【" + announcement.getTitle() + "】"); //添加访问日志
            
			return PageResp.success();
		} catch (RepositoryException e) {
			return PageResp.fail(Messages.SYSTEM_ERROR);
		}
	}
}

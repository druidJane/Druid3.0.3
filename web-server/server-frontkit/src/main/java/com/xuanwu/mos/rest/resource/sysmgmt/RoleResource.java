package com.xuanwu.mos.rest.resource.sysmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.Department;
import com.xuanwu.mos.domain.entity.Role;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.IndustryType;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.RoleType;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.service.RoleService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.Messages;
import com.xuanwu.mos.utils.SessionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 角色管理
 * @Data 2017-3-24
 * @Version 1.0.0
 */
@Component
@Path(Keys.SYSTEMMGR_ROLEMGR)
public class RoleResource {

	private static final Logger logger = LoggerFactory.getLogger(RoleResource.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private UserMgrService userMgrService;

	@POST
	@Path(Keys.SYSTEMMGR_ROLEMGR_GETALLROLE)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp list(@Valid PageReqt req) {
		SimpleUser user = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		params.addParam("platformId", Platform.FRONTKIT.getIndex());
		params.addParam("enterpriseId", user.getEnterpriseId());
		int total = roleService.findRolesCount(params);
		if (total == 0) {
			PageResp.emptyResult();
		}

		PageInfo page = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(page);
		List<Role> roles = roleService.findRoles(params);
		return PageResp.success(total, roles);
	}

	@POST
	@Path(Keys.SYSTEMMGR_ROLEMGR_INDEX_PERMISSION)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getPermissions() {
		try {
			List<PermDto> permissions = roleService.findPermissionsByPlatform(Platform.FRONTKIT.getIndex());
			return PageResp.success(permissions);
		} catch (Exception e) {
			logger.error("get permissions failed by : ", e);
		}
		return JsonResp.fail();
	}

	@POST
	@Path(Keys.SYSTEMMGR_ROLEMGR_INDEX_DETAIL)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getDetail(Role role) {
		role = roleService.findRoleById(role.getId());
		return PageResp.success(role);
	}

	/**
	 * 角色修改
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ROLEMGR_UPDATEROLE)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp update(Role role) {
		SimpleUser user = SessionUtil.getCurUser();
		role.setEnterpriseId(user.getEnterpriseId());
		role.setPlatformId(Platform.FRONTKIT.getIndex());
		role.setUserId(user.getId());
		role.setLastModifyDate(new Date());
		role.setLastModifyUserId(user.getId());
		role.setIndustryType(IndustryType.BASE_ROLE);
		try {
			if (roleService.isExists(role)) {
				return JsonResp.fail("角色名称已存在！");
			}
			if (roleService.updateRole(role)) {
				sysLogService.addLog(user, OperationType.MODIFY, "【角色管理】", "Public", "Login", "【"+role.getName()+"】"); // 添加访问日志
				return JsonResp.success();
			}
		} catch (RepositoryException e) {
			logger.error("update role failed by :", e);
		}
		return JsonResp.fail();
	}

	/**
	 * 删除角色
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ROLEMGR_DELETEROLE)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp deleteRole(Integer[] ids) {
		if (ids == null || ids.length == 0) {
			return JsonResp.fail(Messages.SELECT_REMOVE_DATA);
		}

		try {
			String logContent = roleService.getDelRoleName(ids);
			for (int id : ids) {
				roleService.deleteRole(id);
			}
			SimpleUser user = SessionUtil.getCurUser();
			sysLogService.addLog(user, OperationType.DELETE, "【角色管理】", "Public", "Login",
					"【" + logContent + "】"); // 添加访问日志
		} catch (RepositoryException e) {
			logger.error("delete role failed by :", e);
			return JsonResp.fail();
		}
		return JsonResp.success();
	}

	/**
	 * 新增角色
	 */
	@POST
	@Path(Keys.SYSTEMMGR_ROLEMGR_ADDROLE)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp addRole(Role role) {
		SimpleUser user = SessionUtil.getCurUser();
		role.setEnterpriseId(user.getEnterpriseId());
		role.setUserId(user.getId());
		role.setPlatformId(Platform.FRONTKIT.getIndex());
		role.setDefault(false);
		role.setLastModifyDate(new Date());
		role.setLastModifyUserId(user.getId());
		role.setRoleType(RoleType.NORMAL_ROLE);
		role.setIndustryType(IndustryType.BASE_ROLE);
		try {
			if (roleService.isExists(role)) {
				return JsonResp.fail("角色名称已存在！");
			}
			Department rootDept = userMgrService.
					getDeptByEntId(user.getEnterpriseId());
			role.setRootDeptId(rootDept.getId());
			if (roleService.addRole(role)) {
				sysLogService.addLog(user, OperationType.NEW, "【角色管理】", "Public",
						"Login", "【" + role.getName() + "】"); // 添加访问日志
				return JsonResp.success();
			}
		} catch (RepositoryException e) {
			logger.error("add role failed by :", e);
		}
		return JsonResp.fail();
	}
}

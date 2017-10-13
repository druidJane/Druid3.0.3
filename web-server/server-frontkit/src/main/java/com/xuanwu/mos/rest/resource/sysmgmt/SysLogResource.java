package com.xuanwu.mos.rest.resource.sysmgmt;

import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.entity.SystemLog;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.utils.SessionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Discription 日志管理
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Data 2017-3-24
 * @Version 1.0.0
 */
@Component
@Path(Keys.SYSTEMMGR_LOGMGR)
public class SysLogResource {

	@Autowired
	private SysLogService sysLogService;

	@GET
	@Path(Keys.SYSTEMMGR_LOGMGR_INDEX_OPERATION_TYPE)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp getAllOperationType(){
		Map<Integer, String> typeMap = new HashMap<>();
		OperationType[] types = OperationType.values();
		for (OperationType type : types) {
			typeMap.put(type.getIndex(), type.getOperationName());
		}
		return JsonResp.success(typeMap);
	}

	@POST
	@Path(Keys.SYSTEMMGR_LOGMGR_GETLOGLIST)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsonResp list(@Valid PageReqt req){
		SimpleUser curUser = SessionUtil.getCurUser();
		QueryParameters params = new QueryParameters(req);
		params.addParam("entId", curUser.getEnterpriseId());
		String endTime = (String) params.getParams().get("endTime");
		params.getParams().put("endTime", endTime + " 23:59:59");
		int total = sysLogService.count(params);
		if (total == 0) {
			return PageResp.emptyResult();
		}

		PageInfo pageInfo = new PageInfo(req.getPage(), req.getCount(), total);
		params.setPage(pageInfo);
		Collection<SystemLog> systemLogs = sysLogService.list(params);
		return PageResp.success(total, systemLogs);
	}

}

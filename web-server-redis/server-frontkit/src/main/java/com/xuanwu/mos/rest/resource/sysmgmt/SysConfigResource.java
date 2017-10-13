package com.xuanwu.mos.rest.resource.sysmgmt;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Keys;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.service.CapitalAccountService;
import com.xuanwu.mos.rest.service.CarrierService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Discription 系统配置
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Data 2017-3-23
 * @Version 1.0.0
 */
@Component
@Path(Keys.SYSTEMMGR_SYSCONFIG)
public class SysConfigResource {

	@Autowired
	private UserService userService;

	@Autowired
	private CapitalAccountService accountService;

	@Autowired
	private CarrierService carrierService;

	@Autowired
	private Config config;
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 企业信息
	 */
	@POST
	@Path(Keys.SYSTEMMGR_SYSCONFIG_ENTERPRISE)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp enterpriseInfo() {
		SimpleUser loginUser = SessionUtil.getCurUser();
		CapitalAccount capitalAccount = accountService.
				findParentAccountInfo(loginUser.getEnterpriseId(), UserState.NORMAL);
		BigDecimal smsPrice = accountService.findMsgPriceWithEnterpriseId(MsgTicket.MsgType.SMS,
				loginUser.getEnterpriseId());
		BigDecimal mmsPrice = accountService.findMsgPriceWithEnterpriseId(MsgTicket.MsgType.MMS,
				loginUser.getEnterpriseId());
		Enterprise enterprise = commonEnterpriseInfo();
		enterprise.setSmsPrice(smsPrice);
		enterprise.setMmsPrice(mmsPrice);
		CapitalAccount diffCapitalAccount = accountService.
				getDifferenceByEnterpriseId(loginUser.getEnterpriseId());
		Map<String, Object> results = new HashMap<>();
		results.put("capitalAccount", capitalAccount);
		results.put("enterprise", enterprise);
		results.put("diffCapitalAccount", diffCapitalAccount);
		return JsonResp.success(results);
	}

	/**
	 * 参数配置信息
	 */
	@POST
	@Path(Keys.SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp parameterConfig(){
		Enterprise enterprise = commonEnterpriseInfo();
		return JsonResp.success(enterprise);
	}

	public Enterprise commonEnterpriseInfo() {
		SimpleUser loginUser = SessionUtil.getCurUser();
		Enterprise enterprise = (Enterprise) userService.findUserById(loginUser
				.getEnterpriseId());
		if (StringUtils.isBlank(enterprise.getBalanceRemind())) {
			enterprise.setBalanceRemind("1000");
		}
		if (StringUtils.isBlank(enterprise.getMoUserName())) {
			enterprise.setMoUserName(enterprise.getDomain() + "@" + enterprise.getDomain());
			User moUser = userService.findByName(loginUser.getEnterpriseId(),
					enterprise.getMoUserName(), true);
			enterprise.setDefaultMoUserId(moUser.getId());
		}
		if (StringUtils.isNotBlank(enterprise.getMonthlyStatStart())) {
			enterprise.setChargeDay(Integer.valueOf(enterprise.
					getMonthlyStatStart().split(",")[1]));
		} else {
			enterprise.setChargeDay(1);
		}
		if (enterprise.getAuditingNum() == 0) {
			enterprise.setAuditingNum(200);
		}
		if (enterprise.getAuditingMmsNum() == 0) {
			enterprise.setAuditingMmsNum(200);
		}
		if (StringUtils.isBlank(enterprise.getDeptNoPrefix())) {
			enterprise.setDeptNoPrefix(config.getDeptNoPrefix(Platform.BACKEND));
		}
		return enterprise;
	}

	/**
	 * 参数配置更新
	 */
	@POST
	@Path(Keys.SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	public JsonResp paramUpdate(Enterprise enterprise){
		try {
			SimpleUser user = SessionUtil.getCurUser();
			User moUser = userService.findByName(user.getEnterpriseId(),
					enterprise.getMoUserName(), false);
			if (moUser == null) {
				return JsonResp.fail("默认接收上行短信账号不存在！");
			}
			enterprise.setDefaultMoUserId(moUser.getId());
			enterprise.setMonthlyStatStart("0" + "," + enterprise.getChargeDay());
			enterprise.setMonthlyStatEnd("1" + "," + enterprise.getChargeDay());
			userService.updateParamsConfig(user.getEnterpriseId(), enterprise);
			sysLogService.addLog(user, OperationType.MODIFY,"【系统配置】",
					"Public","Login","【"+ SessionUtil.getCurEnterprise().getEnterpriseName() +"】");
		} catch (RepositoryException e) {
			return JsonResp.fail();
		}
		return JsonResp.success();
	}
}

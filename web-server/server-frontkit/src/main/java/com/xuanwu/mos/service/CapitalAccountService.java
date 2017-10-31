package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.CapitalAccount;
import com.xuanwu.mos.domain.entity.ChargeRecord;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.enums.ChargeWay;
import com.xuanwu.mos.domain.enums.DeductType;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.domain.enums.UserType;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.CapitalAccountRepo;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.mos.utils.SessionUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription
 * @Data 2017-3-31
 * @Version 1.0.0
 */
@Service
public class CapitalAccountService {

	@Autowired
	private CapitalAccountRepo capitalAccountRepo;
	@Autowired
	private ChargeRecordService recordService;

	public CapitalAccount getDifferenceByEnterpriseId(int enterpriseId) {
		return capitalAccountRepo.getDifferenceByEnterpriseId(enterpriseId);
	}

	public int count(QueryParameters params) {
		return capitalAccountRepo.findResultCount(params);
	}

	public List<CapitalAccount> list(QueryParameters params) {
		List<CapitalAccount> capitalAccounts = capitalAccountRepo.findResults(params);
		BigDecimal price = null;
		for (CapitalAccount capitalAccount : capitalAccounts) {
			if (capitalAccount.getChargeWay() == ChargeWay.AUTO_CHARGE &&
					!capitalAccount.isAutoChargeFlag() && capitalAccount.getChargeTime() != null) {
				capitalAccount.setRemark(DateUtil.format(capitalAccount.getChargeTime(),
						DateUtil.DateTimeType.DateTime) + "，自动充值失败");
			}
			if (capitalAccount.getDeductType() == DeductType.CUSTOM_PRICE) {
				price = capitalAccountRepo.findMsgPriceWithCapitalId(MsgTicket.MsgType.SMS,
						capitalAccount.getId());
				if (price != null) {
					capitalAccount.setSmsPrice(price);
				}
				price = capitalAccountRepo.findMsgPriceWithCapitalId(MsgTicket.MsgType.MMS,
						capitalAccount.getId());
				if (price != null) {
					capitalAccount.setMmsPrice(price);
				}
			} else {
				price = findMsgPriceWithEnterpriseId(MsgTicket.MsgType.SMS,
						capitalAccount.getEnterpriseId());
				if (price != null) {
					capitalAccount.setSmsPrice(price);
				}
				price = findMsgPriceWithEnterpriseId(MsgTicket.MsgType.MMS,
						capitalAccount.getEnterpriseId());
				if (price != null) {
					capitalAccount.setMmsPrice(price);
				}
			}
		}
		return capitalAccounts;
	}

	public BigDecimal findMsgPriceWithEnterpriseId(MsgTicket.MsgType msgType, int enterpriseId) {
		return capitalAccountRepo.findMsgPriceWithEnterpriseId(msgType, enterpriseId);
	}

	public CapitalAccount findParentAccountInfo(int enterpriseId, UserState userState) {
		return capitalAccountRepo.findParentAccountInfo(enterpriseId, userState);
	}

	public boolean isExistAccountName(CapitalAccount account) {
		return capitalAccountRepo.isExistAccountName(account) > 0;
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean addCapitalAccount(CapitalAccount account) throws RepositoryException {
		int capitalAccountId = capitalAccountRepo.addCapitalAccount(account);
		if (capitalAccountId > 0) {
			BigDecimal smsPrice = findMsgPriceWithEnterpriseId(MsgTicket.MsgType.SMS,
					account.getEnterpriseId());
			BigDecimal mmsPrice = findMsgPriceWithEnterpriseId(MsgTicket.MsgType.MMS,
					account.getEnterpriseId());
			if (account.getDeductType() == DeductType.ENTERPRISE_PRICE) {
				account.setSmsPrice(smsPrice);
				account.setMmsPrice(mmsPrice);
			}
			//分别代表短彩信 【移动，联通，小联通，电信】
			int[] carrierIds = {1, 2, 3, 4};
			capitalAccountRepo.addAccountCarrierPrice(account.getEnterpriseId(), capitalAccountId,
					MsgTicket.MsgType.SMS, account.getSmsPrice(), carrierIds);

			capitalAccountRepo.addAccountCarrierPrice(account.getEnterpriseId(), capitalAccountId,
					MsgTicket.MsgType.MMS, account.getMmsPrice(), carrierIds);
			return true;
		}
		return capitalAccountId > 0;
	}

	public List<CapitalAccount> findChildAccountForCharging(int enterpriseId, Integer parentAccountId) {
		return capitalAccountRepo.findChildAccountForCharging(enterpriseId, parentAccountId);
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public JsonResp charging(List<CapitalAccount> childAccounts,
							 CapitalAccount parentAccount, Integer capitalAccountId) throws RepositoryException {
		//所有计费子账户余额
		BigDecimal childBalance = BigDecimal.ZERO;
		if (ListUtil.isNotBlank(childAccounts) && parentAccount != null) {
			CapitalAccount childAccount = null;
			for (CapitalAccount account : childAccounts) {
				if (account.getParentId() == parentAccount.getId()) {
					if(account.getId().equals(capitalAccountId))childAccount = account;
					childBalance = childBalance.add(account.getBalance());
				}
			}
			if (childAccount != null) {
				boolean chargeFlag = canCharge(parentAccount, childBalance, childAccount);
				if (chargeFlag) {
					childAccount.setBalance(childAccount.getBalance().add(childAccount.getChargeMoney()));
					capitalAccountRepo.updateBalanceForCharging(childAccount);
					childAccount.setAutoChargeFlag(true);
					childAccount.setRemark("人工充值成功");
					childAccount.setChargeTime(new Date());
					childAccount.setUserId(SessionUtil.getCurUser().getId());
					capitalAccountRepo.addChargingRecordForCharging(childAccount);
					return JsonResp.success();
				} else {
					return JsonResp.fail("余额不足，无法完成充值！");
				}
			}
		}
		return JsonResp.fail();
	}

	public boolean canCharge(CapitalAccount parentAccount, BigDecimal childBalance, CapitalAccount chargeAccount) {
		boolean chargeFlag = false;
		if (parentAccount.getChargeRatio() == 0 || chargeAccount.
				getChargeMoney().doubleValue() < 0) {
			chargeFlag = true;
		}
		if (!chargeFlag && isEnoughBalance(parentAccount, childBalance, chargeAccount)) {
			chargeFlag = true;
		}
		return chargeFlag;
	}

	public boolean isEnoughBalance(CapitalAccount parentAccount, BigDecimal allBalance, CapitalAccount chargeAccount){
		if (chargeAccount.getChargeMoney().doubleValue() > 0 && parentAccount.getChargeRatio() > 0) {
			BigDecimal tempBalance = parentAccount.getBalance().
					multiply(BigDecimal.valueOf(parentAccount.getChargeRatio()));
			if(allBalance.add(chargeAccount.getChargeMoney()).doubleValue() <= tempBalance.doubleValue()){
				return true;
			}
			return false;
		}
		return true;
	}

	public CapitalAccount findCapitalAccountInfo(int enterpriseId, Integer capitalAccountId) {
		return capitalAccountRepo.getById(capitalAccountId, enterpriseId);
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean editUserAccountBind(boolean isInclude, Integer capitalAccountId,
									   int enterpriseId, String[] userIds) throws RepositoryException {
		if (userIds.length > 0) {
			if(isInclude){
				return capitalAccountRepo.editIncludeUserAccount(capitalAccountId, userIds);
			} else {
				CapitalAccount account = capitalAccountRepo.findParentAccountInfo(enterpriseId, null);
				if (account != null) {
					return capitalAccountRepo.cancelUserAccount(userIds, account.getId());
				}
			}
		}
		return true;
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean updateCapitalAccount(CapitalAccount account) throws RepositoryException {
		boolean result = capitalAccountRepo.updateCapitalAccount(account);
		if (result && account.getDeductType().equals(DeductType.CUSTOM_PRICE)) {
			//分别代表短彩信 【移动，联通，小联通，电信】
			int[] carrierIds = {1, 2, 3, 4};
			for (int carrierId : carrierIds) {
				capitalAccountRepo.updateCarrierPrice(account.getEnterpriseId(), account.getId(),
						MsgTicket.MsgType.SMS, account.getSmsPrice(), carrierId);

				capitalAccountRepo.updateCarrierPrice(account.getEnterpriseId(), account.getId(),
						MsgTicket.MsgType.MMS, account.getMmsPrice(),carrierId);
			}
			return true;
		}
		return result;
	}

	public boolean delCapitalAccount(Integer[] ids, int enterpriseId) throws RepositoryException {
		CapitalAccount accountInfo = capitalAccountRepo.findParentAccountInfo(enterpriseId, null);
		if (accountInfo != null) {
			capitalAccountRepo.delCapitalAccount(ids, enterpriseId, accountInfo.getId());
			return true;
		}
		return false;
	}

	public List<CapitalAccount> findCapitalAccountByType(int enterpriseId, Integer deptId, UserType type) {
		switch (type) {
			case ENTERPRISE:
				return capitalAccountRepo.findCapitalAccountByEntId1(enterpriseId);
			case DEPARTMENT:
				return capitalAccountRepo.findCapitalAccountByEntId2(enterpriseId);
			case PERSONAL:
				return capitalAccountRepo.findCapitalAccountByEntId2(deptId);
			default:
				return Collections.emptyList();
		}
	}

	public List<CapitalAccount> findBindCapitalAccounts(int userId) {
		return capitalAccountRepo.findBindCapitalAccounts(userId);
	}

	@Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
	public boolean handToAutoCharging(List<CapitalAccount> childAccounts,
									  CapitalAccount parentAccount, ChargeRecord record) throws RepositoryException {
		//所有计费子账户余额
		BigDecimal childBalance = BigDecimal.ZERO;
		CapitalAccount childAccount = null;
		for (CapitalAccount obj : childAccounts) {
			if (obj.getParentId() == parentAccount.getId()) {
				if (obj.getId() == record.getCapitalAccountId()) {
					childAccount = obj;
					childAccount.setChargeMoney(record.getChargeAmount());
				}
				childBalance = childBalance.add(obj.getBalance());
			}
		}
		if (childAccount != null) {
			boolean chargeFlag = canCharge(parentAccount, childBalance, childAccount);
			if (chargeFlag) {
				childAccount.setBalance(childAccount.getBalance().add(childAccount.getChargeMoney()));
				capitalAccountRepo.updateBalanceForCharging(childAccount);
				record.setChargeState(true);
				record.setChargeTime(new Date());
				record.setRemark(record.getRemark() + "转人工充值成功!");
				recordService.updateChargingRecord(record);
				return true;
			}
			return false;
		}
		return false;
	}
	 public List<CapitalAccount> findCapitalAccountsSimpleNotDel(int entId, String name){

	        QueryParameters params = new QueryParameters();
	        if(!StringUtils.isBlank(name)){
	            params.addParam("name",name);
	        }
	        params.addParam("entId",entId);
	       // params.addParam("fetchSize",fetchSize);
	        return capitalAccountRepo.findCapitalAccountsSimpleNotDel(params);
	    }

	//by jiangziyuan
	/**
	 * 1、显示当前用户所属子账户余额/企业账户余额；当当前账号有归属计费子账号时，
	 * 显示计费子账号的余额，当当前账号无归属计费子账号时，显示企业账号余额.
	 * 2、显示：预算余额可发送短信多少条数（该条数为分段条数），
	 * 以余额除以当前所选择业务类型关联端口的单价（多个取最高单价），取整数部分显示
	 * */
	public CapitalAccount getUserBalance(int enterpriseId) {
		CapitalAccount result = new CapitalAccount();
		CapitalAccount account = capitalAccountRepo.getDifferenceByEnterpriseId(enterpriseId);
		result = account;
		Double price = null;
		if(account != null){
			//当当前账号有归属计费子账号时
			result.setBalance(account.getChildBalance());
			price = capitalAccountRepo.findMsgPriceWithCapitalId(MsgTicket.MsgType.SMS,
					account.getId()).doubleValue();
			if (!price.equals(BigDecimal.ZERO.doubleValue())) {
				result.setSmsPrice(BigDecimal.valueOf(price));
				result.setRestSendNum(account.getBalance().doubleValue() / price);
			}else{
				result.setRestSendNum(Math.floor(result.getBalance().doubleValue()));
			}
		}else if(account == null){
			//当当前账号无归属计费子账号时
			CapitalAccount childAccount = capitalAccountRepo.findCurrentAccountByEntId(enterpriseId);
			result.setBalance(account.getChildBalance());
			price = capitalAccountRepo.findMsgPriceWithCapitalId(MsgTicket.MsgType.SMS,
					childAccount.getId()).doubleValue();
			if (!price.equals(BigDecimal.ZERO.doubleValue())) {
				result.setSmsPrice(BigDecimal.valueOf(price));
				result.setRestSendNum(childAccount.getBalance().doubleValue() / price);
			}
		}
		return result;
	}

	public int bindEntAccount(Integer entAccountInfoId, int userId) throws RepositoryException {
		return capitalAccountRepo.bindEntAccount(entAccountInfoId, userId);
	}

	public String getDelAccountName(Integer[] ids) {
		return capitalAccountRepo.getDelAccountName(ids);
	}
}

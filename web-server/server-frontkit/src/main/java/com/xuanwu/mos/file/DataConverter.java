package com.xuanwu.mos.file;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.BlacklistType;
import com.xuanwu.mos.file.importbean.ChargeAccountImport;
import com.xuanwu.mos.file.importbean.UserImport;
import com.xuanwu.mos.file.mapbean.*;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.vo.MoTicketVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by 林泽强 on 2016/8/29. 数据转换器
 */
@Component
public class DataConverter {

	@Autowired
	private Config config;


	private String getCellVal(String[] cells, int idx) {
		if (cells == null || cells.length == 0) {
			return "";
		}
		if (idx < 0 || idx > cells.length - 1) {
			return "";
		}
		String val = cells[idx];
		return (val == null) ? "" : val.trim();
	}

	public <E> List<String[]> getCellsList(List<E> list, boolean addExtAttr) {
		List<String[]> cellsList = new ArrayList<String[]>();
		for (E e : list) {
			if (e instanceof KeyWord) {
				KeyWord k = (KeyWord) e;
				List<String> cells = new ArrayList<String>();
				cells.add(k.getKeywordName());
				cells.add(DateUtil.format(k.getHandleTime(), DateUtil.DateTimeType.DateTime));
				if (addExtAttr)
					cells.add(k.getExtAttr1());
				cellsList.add(cells.toArray(new String[0]));
			}else if (e instanceof BlackList){
				BlackList b = (BlackList) e;
				List<String> cells = new ArrayList<String>();
				cells.add(b.getPhone());
				cells.add(BlacklistType.getTypeName(b.getType()));
				cells.add(b.getTargetName());
				cells.add(b.getRemark());
				cellsList.add(cells.toArray(new String[0]));
			}else if (e instanceof Contact){
				Contact c = (Contact) e;
				List<String> cells = new ArrayList<String>();
				//导入，输出失败文件
				if (addExtAttr){
					cells.add(c.getPhone());
					cells.add(c.getName());
					cells.add(c.getSexStr());
					cells.add(c.getVipStr());
					cells.add(c.getBirthdayStr());
					cells.add(c.getIdentifier());
					cells.add(c.getRemark());
					cells.add(c.getExtAttr1());
				}else{
					//导出 { "姓名","手机号码","所属组" , "性别", "出生日期", "编号", "Vip", "备注" };
					cells.add(c.getName());
					cells.add(c.getPhone());
					cells.add(c.getGroup().getName());
					cells.add(c.getSex()==1?"先生":"女士");
					cells.add(DateUtil.format(c.getBirthday(), DateUtil.DateTimeType.Date));
					cells.add(c.getIdentifier());
					cells.add(c.isVip()?"是":"否");
					cells.add(c.getRemark());
				}

				cellsList.add(cells.toArray(new String[0]));
			} else if (e instanceof ChargeRecord) {
				ChargeRecord record = (ChargeRecord) e;
				List<String> cells = new ArrayList<>();
				cells.add(record.getAccountName());
				DecimalFormat decimalFormat = new DecimalFormat("#.0000");
				cells.add(decimalFormat.format(record.getChargeAmount()));
				cells.add(record.getShowChargeWay());
				cells.add(record.getChargeUserName());
				cells.add(record.getShowChargeState());
				cells.add(DateUtil.format(record.getChargeTime(), DateUtil.DateTimeType.DateTime));
				cells.add(record.getRemark());
				cellsList.add(cells.toArray(new String[0]));
			} else if (e instanceof ChargeAccountImport) {
				ChargeAccountImport accountImport = (ChargeAccountImport) e;
				List<String> cells = new ArrayList<>();
				cells.add(accountImport.getAccountName());
				cells.add(accountImport.getChargeMoney());
				cells.add(accountImport.getExtAttr1());
				cellsList.add(cells.toArray(new String[0]));
			} else if (e instanceof User) {
				User user = (User) e;
				List<String> cells = new ArrayList<>();
				cells.add(user.getShowAccountType());
				cells.add(user.getUserName());
				cells.add(user.getLinkMan());
				cells.add(user.getPhone());
				cells.add(user.getParentIdentify());
				cells.add(user.getEnterpriseName());
				cells.add(user.getIdentify());
				cells.add(user.getSignature());
				cells.add(user.getShowProtocolType());
				cells.add(user.getSrcPort());
				cells.add(user.getCallbackAddress());
				cells.add(user.getCustomerSignature() == 0 ? "否" : "是");
				cells.add(user.getSendSpeed()+"");
				cells.add(user.getLinkNum()+"");

				cells.add(user.getUpPush() == 0 ? "停用" : "启用");
				cells.add(user.getPushAddress() == null ? "" : user.getPushAddress());
				cells.add(user.getStatusReportPush() == 0 ? "停用" : "启用");
				cells.add(user.getReportPushAddress() == null ? "" : user.getReportPushAddress());

				cells.add(user.getRemark());
				cellsList.add(cells.toArray(new String[0]));
			} else if (e instanceof UserImport) {
				UserImport user = (UserImport) e;
				List<String> cells = new ArrayList<>();
				cells.add(user.getAccountType());
				cells.add(user.getUserName());
				cells.add(user.getLinkMan());
				cells.add(user.getSendPwd());
				cells.add(user.getMidPwd());
				cells.add(user.getLoginPwd());
				cells.add(user.getPhone());
				cells.add(user.getDeptIdentify());
				cells.add(user.getDeptName());
				cells.add(user.getUserIdentify());
				cells.add(user.getSignature());
				cells.add(user.getSigLocation());
				cells.add(user.getBizTypeIds());
				cells.add(user.getProtocolType());
				cells.add(user.getSrcPort());
				cells.add(user.getCallbackAddress());
				cells.add(user.getCustomerSignature());
				cells.add(user.getSendSpeed());
				cells.add(user.getLinkNum());
				cells.add(user.getRoleNames());
				cells.add(user.getRemark());
				cells.add(user.getExtAttr1());
				cellsList.add(cells.toArray(new String[0]));
			}else if(e instanceof MoTicketVo){
				MoTicketVo moTicketVo = (MoTicketVo) e;
				List<String> cells = new ArrayList<>();
				cells.add(moTicketVo.getContactName());
				cells.add(moTicketVo.getPhone());
				cells.add(moTicketVo.getContent());
				cells.add(moTicketVo.getUserName());
				cells.add(moTicketVo.getEnterpriseName());
				cells.add(moTicketVo.getSpecNumber());
				cells.add(DateUtil.format(moTicketVo.getPostTime(), DateUtil.DateTimeType.DateTime));
				cells.add(moTicketVo.getHasReply()?"已回复":"未回复");
				cellsList.add(cells.toArray(new String[0]));
			}else if(e instanceof BillingAcountInfo){
				BillingAcountInfo billAccountInfo = (BillingAcountInfo) e;
				List<String> cells = new ArrayList<>();
				cells.add(billAccountInfo.getAccountName());
				if (addExtAttr)
					cells.add(billAccountInfo.getDeductTimeStr());
				cells.add(String.valueOf(billAccountInfo.getSmsConsumeStr()));
				cells.add(String.valueOf(billAccountInfo.getMmsConsumeStr()));
				cells.add(String.valueOf(billAccountInfo.getSumConsumeStr()));
				cellsList.add(cells.toArray(new String[0]));
			}else if(e instanceof UserStatistics){
				UserStatistics userStatistics = (UserStatistics) e;
				List<String> cells = new ArrayList<>();
				cells.add(userStatistics.getStatDateStr());		
				cells.add(userStatistics.getUserName());				
				cells.add(userStatistics.getDeptName());						
				cells.add(String.valueOf(userStatistics.getAllReceiveSum()));
				cells.add(String.valueOf(userStatistics.getAllSendSum()));
				
				cells.add(String.valueOf(userStatistics.getAllSuccessSum()));
				cells.add(String.valueOf(userStatistics.getSuccessSumYD()));
				cells.add(String.valueOf(userStatistics.getSuccessSumLT()));
				cells.add(String.valueOf(userStatistics.getSuccessSumCDMA()));
				cells.add(String.valueOf(userStatistics.getSuccessSumXLT()));
				

				cellsList.add(cells.toArray(new String[0]));
			}else if(e instanceof BizTypeStatistics){
				BizTypeStatistics bizTypeStatistics = (BizTypeStatistics) e;
				List<String> cells = new ArrayList<>();
				cells.add(bizTypeStatistics.getStatDateStr());
				cells.add(bizTypeStatistics.getBizTypeName());								
				cells.add(String.valueOf(bizTypeStatistics.getAllReceiveSum()));
				cells.add(String.valueOf(bizTypeStatistics.getAllSendSum()));
				
				cells.add(String.valueOf(bizTypeStatistics.getAllSuccessSum()));
				cells.add(String.valueOf(bizTypeStatistics.getSuccessSumYD()));
				cells.add(String.valueOf(bizTypeStatistics.getSuccessSumLT()));
				cells.add(String.valueOf(bizTypeStatistics.getSuccessSumCDMA()));
				cells.add(String.valueOf(bizTypeStatistics.getSuccessSumXLT()));
				
				
				cellsList.add(cells.toArray(new String[0]));
			}else if(e instanceof DepartmentStatistics){
				DepartmentStatistics dept = (DepartmentStatistics) e;
				List<String> cells = new ArrayList<>();
				cells.add(dept.getStatDateStr());				
				cells.add(dept.getDeptName());
				cells.add(String.valueOf(dept.getAllReceiveSum()));
				cells.add(String.valueOf(dept.getAllSendSum()));
				
				cells.add(String.valueOf(dept.getAllSuccessSum()));
				cells.add(String.valueOf(dept.getSuccessSumYD()));
				cells.add(String.valueOf(dept.getSuccessSumLT()));
				cells.add(String.valueOf(dept.getSuccessSumCDMA()));
				cells.add(String.valueOf(dept.getSuccessSumXLT()));
				
				
				cellsList.add(cells.toArray(new String[0]));
			}
		}

		return cellsList;
	}

	public List<ChargeAccountImport> parseChargeAccount(List<String[]> rowList, Map<String, String> params) {
		ChargeAccountMap accountMap = ChargeAccountMap.parseFrom(params.get("accountMap"));
		List<ChargeAccountImport> chargingAccounts = new ArrayList<>();
		for (String[] cells : rowList) {
			ChargeAccountImport accountImport = new ChargeAccountImport();
			if (accountMap.getAccountName() >= 0) {
				accountImport.setAccountName(getCellVal(cells, accountMap.getAccountName()));
			}
			if (accountMap.getChargeMoney() >= 0) {
				accountImport.setChargeMoney(getCellVal(cells, accountMap.getChargeMoney()));
			}
			chargingAccounts.add(accountImport);
		}
		return chargingAccounts;
	}

	public List<UserImport> parseUser(List<String[]> rowList, Map<String, String> params) {
		UserMap userMap = UserMap.parseFrom(params.get("userMap"));
		List<UserImport> users = new ArrayList<>();
		for (String[] cells : rowList) {
			UserImport user = new UserImport();
			if (userMap.getAccountType() >= 0) {
				user.setAccountType(getCellVal(cells, userMap.getAccountType()));
			}
			if (userMap.getUserName() >= 0) {
				user.setUserName(getCellVal(cells, userMap.getUserName()));
			}
			if (userMap.getLinkMan() >= 0) {
				user.setLinkMan(getCellVal(cells, userMap.getLinkMan()));
			}
			if (userMap.getSendMsgPwd() >= 0) {
				user.setSendPwd(getCellVal(cells, userMap.getSendMsgPwd()));
			}
			if (userMap.getMidPwd() >= 0) {
				user.setMidPwd(getCellVal(cells, userMap.getMidPwd()));
			}
			if (userMap.getLoginPwd() >= 0) {
				user.setLoginPwd(getCellVal(cells, userMap.getLoginPwd()));
			}
			if (userMap.getPhone() >= 0) {
				user.setPhone(getCellVal(cells, userMap.getPhone()));
			}
			if (userMap.getDeptIdentify() >= 0) {
				user.setDeptIdentify(getCellVal(cells, userMap.getDeptIdentify()));
			}
			if (userMap.getDeptName() >= 0) {
				user.setDeptName(getCellVal(cells, userMap.getDeptName()));
			}
			if (userMap.getUserIdentify() >= 0) {
				user.setUserIdentify(getCellVal(cells, userMap.getUserIdentify()));
			}
			if (userMap.getSignature() >= 0) {
				user.setSignature(getCellVal(cells, userMap.getSignature()));
			}
			if (userMap.getSigLocation() >= 0) {
				user.setSigLocation(getCellVal(cells, userMap.getSigLocation()));
			}
			if (userMap.getBizIds() >= 0) {
				user.setBizTypeIds(getCellVal(cells, userMap.getBizIds()));
			}
			if (userMap.getProtocolType() >= 0) {
				user.setProtocolType(getCellVal(cells, userMap.getProtocolType()));
			}
			if (userMap.getSrcPort() >= 0) {
				user.setSrcPort(getCellVal(cells, userMap.getSrcPort()));
			}
			if (userMap.getCallbackAddress() >= 0) {
				user.setCallbackAddress(getCellVal(cells, userMap.getCallbackAddress()));
			}
			if (userMap.getCustomerSignature() >= 0) {
				user.setCustomerSignature(getCellVal(cells, userMap.getCustomerSignature()));
			}
			if (userMap.getSendSpeed() >= 0) {
				user.setSendSpeed(getCellVal(cells, userMap.getSendSpeed()));
			}
			if (userMap.getLinkNum() > 0) {
				user.setLinkNum(getCellVal(cells, userMap.getLinkNum()));
			}
			if (userMap.getRoleNames() >= 0) {
				user.setRoleNames(getCellVal(cells, userMap.getRoleNames()));
			}
			//by jiangziyuan
			if (userMap.getUpPush() >= 0) {
				user.setUpPush(getCellVal(cells, userMap.getUpPush()));
			}
			if (userMap.getStatusReportPush() >= 0) {
				user.setStatusReportPush(getCellVal(cells, userMap.getStatusReportPush()));
			}
			if (userMap.getPushAddress() >= 0) {
				user.setPushAddress(getCellVal(cells, userMap.getPushAddress()));
			}
			if (userMap.getReportPushAddress() >= 0) {
				user.setReportPushAddress(getCellVal(cells, userMap.getReportPushAddress()));
			}

			if (userMap.getRemark() >= 0) {
				user.setRemark(getCellVal(cells, userMap.getRemark()));
			}
			users.add(user);
		}
		return users;
	}

	public List<BlackList> parseBlacklist(List<String[]> rowList, Map<String, String> paramsMap) {
		BlackListMap blacklistMap = BlackListMap.parseFrom(paramsMap.get("blacklistMap"), config.getPlatform());
		List<BlackList> blacklists = new ArrayList<BlackList>();
		for (String[] cells : rowList) {
			BlackList blacklist = new BlackList();
			if (blacklistMap.getPhone() >= 0) {
				blacklist.setPhone(getCellVal(cells, blacklistMap.getPhone()));
			}

			if (blacklistMap.getType() >= 0) {
				blacklist.setBlacklistType(BlackList.BlacklistType.getType(getCellVal(cells, blacklistMap.getType())));
				blacklist.setTmpTypeName(getCellVal(cells, blacklistMap.getType()));
			}
			if (blacklistMap.getTarget() >= 0) {
				blacklist.setTargetName(getCellVal(cells, blacklistMap.getTarget()));
			}
			if (blacklistMap.getRemark() >= 0) {
				blacklist.setRemark(getCellVal(cells, blacklistMap.getRemark()));
			}

			Date currentTime = new Date();
			blacklist.setCreateTime(currentTime);
			blacklist.setUser(paramsMap.get("userId"));
			blacklist.setHandleTime(currentTime);
			blacklist.setEnterpriseID(Integer.valueOf(paramsMap.get("entId")));
			blacklists.add(blacklist);
		}
		return blacklists;
	}


	private Date fixDate(String str) {
		Date date = null;
		if (str.indexOf("/") != -1) {
			date = DateUtil.parse(str, DateUtil.DateTimeType.ExcelDefaultDate);
		} else if (str.indexOf("年") != -1) {
			date = DateUtil.parse(str, DateUtil.DateTimeType.CnTxtDefaultDate);
		} else {
			str = str.replace(Delimiters.DOT,"-").replace("、","-");
			date = DateUtil.parse(str, DateUtil.DateTimeType.Date);
		}
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			if (year < 1000 || year > 9999) {
				return null;
			}
		}
		return date;
	}

	private int fixSex(String str) {
		if ("女".equals(str.trim())||"女士".equals(str.trim())||"小姐".equals(str.trim()) ) {
			return 0;
		}else if("男".equals(str.trim())||"先生".equals(str.trim())||"男士".equals(str.trim()) ){
			return 1;
		}else{
			return -1;
		}
		
	}

	public List<WhitePhone> parseWhitePhone(List<String[]> rowList, Map<String, String> paramsMap) {
		List<WhitePhone> whitePhonelists = new ArrayList<WhitePhone>();
		for (String[] cells : rowList) {
			WhitePhone whitePhone = new WhitePhone();
			WhitePhoneMap whitePhoneMap = WhitePhoneMap.parseFrom(paramsMap.get("whitePhoneMap"));
			whitePhone.setTelphone(getCellVal(cells, whitePhoneMap.getTelphone()));
			Date currentTime = new Date();
			whitePhone.setCreateTime(currentTime);
			whitePhone.setIsNotice(WhitePhone.NoticeType.ENIMPORTED.getValue());
			whitePhone.setEnterpriseId(Integer.parseInt(paramsMap.get("entId")));
			whitePhonelists.add(whitePhone);
		}
		return whitePhonelists;
	}

	public List<KeyWord> parseKeyWord(List<String[]> rowList, Map<String, String> paramsMap) {
		List<KeyWord> keywordlists = new ArrayList<KeyWord>();
		for (String[] cells : rowList) {
			KeyWord keyWord = new KeyWord();
			KeywordMap keywordMap = KeywordMap.parseFrom(paramsMap.get("keywordMap"));
			keyWord.setKeywordName(getCellVal(cells, keywordMap.getKeywordName()));
			Date currentTime = new Date();
			keyWord.setHandleTime(currentTime);
			String userId = paramsMap.get("userId");
			String entId = paramsMap.get("entId");
			keyWord.setUserId(Integer.valueOf(userId));
			keyWord.setTargetId(Integer.valueOf(entId));
			keywordlists.add(keyWord);
		}
		return keywordlists;
	}

	public List<Contact> parseContact(int entId, int userId, List<String[]> rowList,
									  Map<String, String> params) {
		List<Contact> contactList = new ArrayList<Contact>();
		for (String[] cells : rowList) {
			ContactMap map = ContactMap.parseFrom(params.get("contactMap"));
/*
			ContactGroup group = new ContactGroup();
			String gname = getCellVal(cells, map.getGroupName());
			group.setName(gname);
			group.setEnterpriseId(entId);
			group.setUserId(userId);*/

			Contact contact = new Contact();
			contact.setName(getCellVal(cells, map.getName()));
			contact.setPhone(getCellVal(cells, map.getPhone()));
			if (map.getSex() >= 0) {
				String sexVal = getCellVal(cells, map.getSex());
				if(!StringUtils.isEmpty(sexVal)){
					int sex = fixSex(sexVal);
					contact.setSexStr(sexVal);
					contact.setSex(sex);
				}else{
					contact.setSex(1);
				}
			}
			if (map.getBirthday() >= 0) {
				String birthdayStr = getCellVal(cells, map.getBirthday());
				if(!StringUtils.isEmpty(birthdayStr)){
					contact.setBirthdayStr(birthdayStr);
					Date birthday = fixDate(birthdayStr);
					if(birthday==null || DateUtil.compareCurrentDate(birthday) == 0){
						contact.setExt1("errorDate");
					}
					contact.setBirthday(birthday);
				}
			}
			if (map.getIdentifier() >= 0) {
				contact.setIdentifier(getCellVal(cells, map.getIdentifier()));
			}
			if (map.getVip() >= 0) {
				String vipVal = getCellVal(cells, map.getVip());
				if(!StringUtils.isEmpty(vipVal)){
					int vip = fixVip(vipVal);
					contact.setVipStr(vipVal);
					contact.setVipVal(vip);
					contact.setVip(vip == 1);
				}else{
					contact.setVip(false);
				}

			}
			if (map.getRemark() >= 0) {
				contact.setRemark(getCellVal(cells, map.getRemark()));
			}
			contact.setGroupId(Integer.valueOf(params.get("groupId")));
			contactList.add(contact);
		}
		return contactList;
	}

	private int fixVip(String str) {
		if ("是".equals(str.trim())) {
			return 1;
		}else if("否".equals(str.trim())){
			return 0;
		}else{
			return -1;
		}
	}
}

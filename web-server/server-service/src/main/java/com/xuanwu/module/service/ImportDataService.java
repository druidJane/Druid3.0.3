package com.xuanwu.module.service;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.file.importbean.ChargeAccountImport;
import com.xuanwu.mos.file.importbean.UserImport;
import com.xuanwu.mos.file.importer.ImportInfo;
import com.xuanwu.mos.file.importer.ImportInfoBuild;
import com.xuanwu.mos.rest.repo.BlackListRepo;
import com.xuanwu.mos.rest.repo.CapitalAccountRepo;
import com.xuanwu.mos.rest.repo.FileTaskRepo;
import com.xuanwu.mos.utils.ConverterUser;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.ImportUserCheck;
import com.xuanwu.mos.utils.ListUtil;
import com.xuanwu.msggate.common.cache.engine.LongHashCache;
import com.xuanwu.msggate.common.util.PhoneUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.websocket.CaseInsensitiveKeyMap;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by zhangz on 2017/4/1.
 */
@Service
public class ImportDataService {
    @Autowired
    private ContactService contactService;
    @Autowired
    private BlackListService blackListService;
    @Autowired
    private KeyWordService keyWordService;
    @Autowired
    protected WhitePhoneService whitePhoneService;
    @Autowired
    private BizTypeService bizTypeService;
    @Autowired
    private Config config;
    @Autowired
    private BizDataService bizDataService;
    @Autowired
    private FileTaskRepo fileTaskRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private BlackListRepo blackListRepo;
    @Autowired
    private CapitalAccountRepo capitalAccountRepo;
    @Autowired
    private CapitalAccountService capitalAccountService;
    @Autowired
    private UserMgrService userMgrService;
    @Autowired
    private RoleService roleService;

    Pattern namePattern = Pattern.compile("^[\\u4e00-\\u9fa5\\w-\\(\\)\\[\\]【】\\（\\）]+$");

    public ImportInfo importBlacklist(List<BlackList> blacklists, FileTask task) throws Exception {
        ImportInfo importInfo = new ImportInfo();
        ImportInfoBuild importInfoBuild = task.getImportInfoBuild();
        try {
            final String phoneReg = config.getPhonePattern();
            List<String> checkPhoneList = new ArrayList<String>();
            Map<Integer, String> entMap = new HashMap<Integer, String>();
            for (BlackList blacklist : blacklists) {
                // 检查手机号码是否合法
                blacklist.setRemove(false);
                blacklist.setPhone(blacklist.getPhone().trim());
                if (StringUtils.isBlank(blacklist.getPhone())) {
                    importInfoBuild.increment(CheckResult.BlankPhone, blacklist);
                    continue;
                }
                if (!blacklist.getPhone().matches(phoneReg)) {
                    importInfoBuild.increment(CheckResult.IllegalPhone, blacklist);
                    continue;
                }
                String phone = validPhone(blacklist.getPhone());
                if (phone == null) {
                    importInfoBuild.increment(CheckResult.IllegalPhone, blacklist);
                    continue;
                }
                blacklist.setPhone(phone);

                // 检查黑名单类型
                if (blacklist.getType() == BlacklistType.Illegal.getIndex()) {
                    importInfoBuild.increment(CheckResult.IllegalBlacklistType, blacklist);
                    continue;
                }
                String targetName = blacklist.getTargetName();
                if (blacklist.getType() != BlacklistType.Global.getIndex()) {
                    // 检查所属对象是否为空
                    if (StringUtil.isBlank(targetName)) {
                        importInfoBuild.increment(CheckResult.IllegalBlacklistTarget, blacklist);
                        continue;
                    }
                }
                // 检查黑名单所属对象
                int type = blacklist.getType();
                switch (BlackList.BlacklistType.getType(type)) {
                    case User:
                        User user = userService.findByName(blacklist.getEnterpriseID(), targetName, false);
                        if (user != null) {
                            blacklist.setTarget(user.getId());
                        } else {
                            importInfoBuild.increment(CheckResult.BlacklistUserNExists, blacklist);
                            continue;
                        }
                        break;
                    case Enterprise:
                        User enterprise = userService.findEnterpriseByName(targetName);
                        if (enterprise != null) {
                            blacklist.setTarget(enterprise.getId());
                        } else {
                            importInfoBuild.increment(CheckResult.BlacklistEntNExists, blacklist);
                            continue;
                        }
                        break;
                    case BizType:
                        BizType bizType = bizTypeService.findByNameAndEntId(targetName, blacklist.getEnterpriseID());
                        if (bizType != null) {
                            blacklist.setTarget(bizType.getId());
                        } else {
                            importInfoBuild.increment(CheckResult.BlacklistEntNExists, blacklist);
                            continue;
                        }
                        break;
                    default:
                        importInfoBuild.increment(CheckResult.BlacklistBizNExists, blacklist);
                }
                // 检查备注长度
                if (StringUtils.isNotBlank(blacklist.getRemark())) {
                    if (blacklist.getRemark().length() > 200) {
                        importInfoBuild.increment(CheckResult.BlacklistRemarkTooLong, blacklist);
                        continue;
                    }
                }
                // 前台文件去重
                String tempData = phone + Delimiters.SICOLON + blacklist.getType() + Delimiters.SICOLON + targetName;
                if (checkPhoneList.contains(tempData)) {
                    importInfoBuild.increment(CheckResult.BlacklistExists, blacklist);
                    continue;
                } else {
                    checkPhoneList.add(tempData);
                }
                boolean exist = blackListRepo.isExist(blacklist) == 1;
                if (exist) {
                    importInfoBuild.increment(CheckResult.BlacklistExists, blacklist);
                    continue;
                }

                LongHashCache cache = new LongHashCache();
                long zipmes = cache.tran2Code(blacklist.getPhone(), blacklist.getType(), blacklist.getTarget());
                blacklist.setZipmes(zipmes);
                blackListService.addBlacklist(blacklist);
                importInfoBuild.increment(CheckResult.Legal, blacklist);
            }
            handleImportResult(task, importInfo, importInfoBuild);
        } catch (Exception e) {
            importInfo.setResult(Result.Failed);
            e.printStackTrace();
            throw e;
        }
        return importInfo;
    }
    private String validPhone(String phone) {
        String tempPhone = PhoneUtil.removeZhCnCode(phone);
        Carrier c = bizDataService.getCarrierType(tempPhone);
        if (c == null) {
            tempPhone = PhoneUtil.removeZeroHead(phone);
            c = bizDataService.getCarrierType(tempPhone);
        }
        if (c == null) {
            return null;
        }
        return tempPhone;
    }

    public ImportInfo importChargeAccount(List<ChargeAccountImport> chargeAccounts, FileTask task) throws RepositoryException {
        ImportInfo importInfo = new ImportInfo();
        ImportInfoBuild importInfoBuild = task.getImportInfoBuild();
        Map<String, String> params = task.getParamsMap();
        BigDecimal totalMoney = BigDecimal.ZERO;
        String chargeMoneyRegex = "^(-)?\\d+(\\.\\d{1,4})?$";
        try {
            int entId = Integer.valueOf(params.get("entId"));
            int userId = Integer.valueOf(params.get("userId"));
            CapitalAccount parentAccount = capitalAccountRepo.findParentAccountInfo(entId, UserState.NORMAL);
            CapitalAccount accountBalanceInfo = capitalAccountRepo.getDifferenceByEnterpriseId(entId);
            List<CapitalAccount> childAccounts = capitalAccountRepo.
                    findChildAccountForCharging(entId, parentAccount.getId());
            Map<String, CapitalAccount> childAccountMap = new CaseInsensitiveKeyMap<>();
            for (CapitalAccount child : childAccounts) {
                if (child.getParentId() == parentAccount.getId()) {
                    totalMoney = totalMoney.add(child.getBalance());
                    childAccountMap.put(child.getAccountName(), child);
                }
            }

            for (ChargeAccountImport accountImport : chargeAccounts) {
                CapitalAccount account = new CapitalAccount();
                account.setAccountName(accountImport.getAccountName());
                BigDecimal chargeMoney = null;
                try {
                    chargeMoney = BigDecimal.valueOf(Double.
                            valueOf(accountImport.getChargeMoney().trim()));
                    account.setChargeMoney(chargeMoney);
                } catch (NumberFormatException e) {
                    importInfoBuild.increment(CheckResult.ChargeMoneyFormatException, accountImport);
                    continue;
                }
                CapitalAccount chargeAccount = childAccountMap.get(account.getAccountName().trim());
                if (chargeAccount != null) {
                    if (!chargeAccount.getChargeWay().equals(ChargeWay.HAND_CHARGE)) {
                        importInfoBuild.increment(CheckResult.ChargeWayNotHandledCharge, accountImport);
                        continue;
                    }
                    //校验充值金额
                    if (account.getChargeMoney().doubleValue() == 0) {
                        importInfoBuild.increment(CheckResult.ChargeMoneyIllegal, accountImport);
                        continue;
                    }
                    if (!(accountImport.getChargeMoney().trim()).matches(chargeMoneyRegex)) {
                        importInfoBuild.increment(CheckResult.ChargeMoneyIllegal, accountImport);
                        continue;
                    }
                    if (parentAccount.getChargeRatio() > 0 && account.getChargeMoney().doubleValue() >
                            accountBalanceInfo.getDifferenceBalance().doubleValue()) {
                        importInfoBuild.increment(CheckResult.ChargeMoneyIllegal, accountImport);
                        continue;
                    }
                    chargeAccount.setChargeMoney(account.getChargeMoney());
                    boolean chargeFlag = capitalAccountService.canCharge(parentAccount, totalMoney, chargeAccount);
                    if (chargeFlag) {
                        chargeAccount.setAccountName(account.getAccountName());
                        chargeAccount.setBalance(chargeAccount.getBalance().add(account.getChargeMoney()));
                        capitalAccountRepo.updateBalanceForCharging(chargeAccount);
                        chargeAccount.setAutoChargeFlag(true);
                        chargeAccount.setRemark("导入充值成功");
                        chargeAccount.setChargeTime(new Date());
                        chargeAccount.setUserId(userId);
                        capitalAccountRepo.addChargingRecordForCharging(chargeAccount);
                        importInfoBuild.increment(CheckResult.Legal, accountImport);
                    } else {
                        importInfoBuild.increment(CheckResult.ChargeAccountBalanceNotEnough, accountImport);
                        continue;
                    }
                } else {
                    importInfoBuild.increment(CheckResult.NotFoundChildAccountName, accountImport);
                    continue;
                }
            }
            handleImportResult(task, importInfo, importInfoBuild);
        } catch (Exception e) {
            importInfo.setResult(Result.Failed);
            throw e;
        }
        return importInfo;
    }

    public ImportInfo importUser(List<UserImport> users, FileTask task) throws RepositoryException {
        ImportInfo importInfo = new ImportInfo();
        ImportInfoBuild importInfoBuild = task.getImportInfoBuild();
        int entId = Integer.valueOf(task.getParameter("entId"));
        String path = task.getParameter("path");
        Integer parentId = Integer.valueOf(task.getParameter("parentId"));
        String domain = task.getParameter("domain");

        try {
            //企业范围
            List<User> dbUsers = userMgrService.findAllUserByEntId(entId);
            Set<String> dbUserNames = dbUserNames(dbUsers);
            Set<String> dbUserIdentity = dbUserIdentitys(dbUsers);
            List<Department> dbDepts = userMgrService.getDeptIncludeChildDept(path, parentId);
            Map<String, Department> dbDeptMap = dbDeptWithIdentityKey(dbDepts);
            //用户名去重
            Set<String> tempUserNames = new HashSet<>();
            //用户扩展码去重
            Set<String> tempUserIdentity = new HashSet<>();

            //根据entId找对应的gsms_user_ext记录，看看is_transparent_send（是否透传0:否,1:是）是不是为true，如果为false，则过滤掉账号类型为”透传“的用户
            String transparentSend = task.getParameter("transparentSend");
            boolean isTransparentSend = true;
            if (null != transparentSend && transparentSend.equals("true")) {
                isTransparentSend = true;
            } else {
                isTransparentSend = false;
            }

            for (UserImport userImport : users) {
                User user = new User();
                ImportUserCheck.tranUserImportToUser(userImport, user);

                //by jiangziyuan
                if (isTransparentSend == false && user.getAccountType().getIndex() == 3) {
                    importInfoBuild.increment(CheckResult.DransparentSendIllegal, userImport);
                    continue;
                }

                //账号类型校验
                if (user.getAccountType() == null) {
                    importInfoBuild.increment(CheckResult.UserAccountTypeIllegal, userImport);
                    continue;
                }

                if (!ImportUserCheck.validUserName(importInfoBuild, user, userImport,
                        tempUserNames, dbUserNames)) {
                    continue;
                }
                /**
                 * by jiangziyuan
                 * 1,用户账号需补加@企业域，如果账号类型为”透传“，则不需要（ 1:web， 2:接口，3:透传 ）;
                 * 2,当账号类型为【透传】时，"上行推送"与"状态报告推送"的值固定为[停用]--0;
                 * 3,当账号类型为【透传】时，"上行推送地址"与"状态报告推送地址"的值固定为空;
                 * 4,当账号类型不为【透传】时，"上行推送地址"与"状态报告推送地址"的值字符长度限制为100;
                 */
                if (user.getAccountType().getIndex() == 3) {
                    user.setUserName(user.getUserName());
                    user.setUpPush(0);
                    user.setStatusReportPush(0);
                    user.setPushAddress("");
                    user.setReportPushAddress("");
                } else {
                    user.setUserName(user.getUserName() + "@" + domain);
                    if (!ImportUserCheck.validPushAddress(importInfoBuild, user, userImport)) {
                        continue;
                    }
                    if (!ImportUserCheck.validReportPushAddress(importInfoBuild, user, userImport)) {
                        continue;
                    }
                }
                if (!ImportUserCheck.validLinkMan(importInfoBuild, user, userImport)) {
                    continue;
                }

                if (!ImportUserCheck.validPassword(user, userImport, importInfoBuild)) {
                    continue;
                }

                //手机号校验
                if (!user.getPhone().matches(config.getPhonePattern())) {
                    importInfoBuild.increment(CheckResult.PhoneRuleLimit, userImport);
                    continue;
                }

                if (!ImportUserCheck.validDeptIdentityAndRoleAndBizType(user, userImport, dbDeptMap, importInfoBuild)) {
                    continue;
                }

                if (!ImportUserCheck.validUserIdentity(importInfoBuild, user, userImport,
                        dbUserIdentity, tempUserIdentity)) {
                    continue;
                }

                //校验用户签名
                if (user.getSignature().length() > 30) {
                    importInfoBuild.increment(CheckResult.UserSignatureTooLong, userImport);
                    continue;
                }

                if (!ImportUserCheck.validSigLocation(user, userImport, importInfoBuild)) {
                    continue;
                }

                if (!ImportUserCheck.validProtocolType(user, userImport, importInfoBuild)) {
                    continue;
                }

                if (!ImportUserCheck.validSrcPort(user, userImport, importInfoBuild)) {
                    continue;
                }

                if (!ImportUserCheck.validCallbackAddress(user, userImport, importInfoBuild)) {
                    continue;
                }

                if (!ImportUserCheck.validCustomerSignature(user, userImport, importInfoBuild)) {
                    continue;
                }

                if (!ImportUserCheck.validSendSpeed(user, userImport, importInfoBuild)) {
                    continue;
                }

                if (!ImportUserCheck.validLinkNum(user, userImport, importInfoBuild)) {
                    continue;
                }

                //校验备注信息
                if (user.getRemark().length() > 200) {
                    importInfoBuild.increment(CheckResult.RemarkTooLong, userImport);
                    continue;
                }
                Department dept = dbDeptMap.get(user.getParentIdentify());
                user.setUserType(UserType.PERSONAL);
                user.setParentId(dept.getId());
                user.setPath(dept.getFullPath());
                user.setCreateTime(new Date());
                user.setEnterpriseId(entId);
                user.setPlatformId(Platform.FRONTKIT.getIndex());
                user.setState(UserState.NORMAL);
                user.setPriority(10);
                user.setContentSendnum(1);//对应moc后台——系统管理——发送限制——用户发送限制（tab页）——修改【同号同内容限制】，目前写死为“1”
                ConverterUser.encryptPassword(user, config);
                userMgrService.addAccount(user);
                importInfoBuild.increment(CheckResult.Legal, userImport);
            }
            handleImportResult(task, importInfo, importInfoBuild);
        } catch (Exception e) {
            importInfo.setResult(Result.Failed);
            throw e;
        }
        return importInfo;
    }

    private Map<String, Department> dbDeptWithIdentityKey(List<Department> dbDepts) {
        Map<String, Department> map = new HashMap<>();
        for (Department dbDept : dbDepts) {
            map.put(dbDept.getIdentify(), dbDept);
        }
        return map;
    }

    private Set<String> dbUserIdentitys(List<User> dbUsers) {
        Set<String> dbUserIdentitys = new HashSet<>();
        for (User dbUser : dbUsers) {
            dbUserIdentitys.add(dbUser.getIdentify());
        }
        return dbUserIdentitys;
    }

    private Set<String> dbUserNames(List<User> dbUsers) {
        Set<String> dbUserNames = new HashSet<>();
        for (User dbUser : dbUsers) {
            dbUserNames.add(dbUser.getUserName().split("@")[0]);
        }
        return dbUserNames;
    }

    private void handleImportResult(FileTask task, ImportInfo importInfo, ImportInfoBuild importInfoBuild) {
        // 更新文件任务
        task.setMessage(task.getImportInfoBuild().getHandleReport());
        task.setState(TaskState.Handle);
        if (task.getTotal() == null) {
            task.setTotal(0);
        }
        task.setHandledCount(task.getTotal() - task.getImportInfoBuild().getFailed().size()); // 已处理记录
        task.setHanldePercent(task.getCurPercent());
        fileTaskRepo.updateHandlingTask(task);
        // 组织返回信息
        importInfo.setResult(Result.Success);
        importInfo.setFailedItems(ListUtil.cloneList(importInfoBuild.getFailed()));
        importInfoBuild.getFailed().clear();
    }

    public ImportInfo importWhitePhone(List<WhitePhone> list, FileTask task) {
        ImportInfo impInfo = new ImportInfo();
        ImportInfoBuild importInfoBuild = task.getImportInfoBuild();
        try {
            for(WhitePhone whitePhone:list){
                if (StringUtils.isBlank(whitePhone.getTelphone())) {
                    importInfoBuild.increment(CheckResult.WhitePhone, whitePhone);
                    continue;
                }
                // 检查手机号码是否合法
                final String phoneReg = config.getPhonePattern();
                whitePhone.setTelphone(whitePhone.getTelphone().trim());
                if (!whitePhone.getTelphone().matches(phoneReg)) {
                    importInfoBuild.increment(CheckResult.IllegalPhone, whitePhone);
                    continue;
                }
                QueryParameters params = new QueryParameters();
                params.addParam("telphone",whitePhone.getTelphone());
                int count = whitePhoneService.findWhitePhoneCount(params);
                if(count>0){
                    importInfoBuild.increment(CheckResult.WhitelistExists, whitePhone);
                    continue;
                }
                try {
                    whitePhoneService.addWhitePhone(whitePhone);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    importInfoBuild.increment(CheckResult.SYSTEM_ERROR, whitePhone);
                }
                importInfoBuild.increment(CheckResult.Legal, whitePhone);
            }
            handleImportResult(task, impInfo, importInfoBuild);
        } catch (Exception e) {
            impInfo.setResult(Result.Failed);
            throw e;
        }
        return impInfo;
    }

    public ImportInfo importKeyWord(List<KeyWord> list, FileTask task) {
        ImportInfo impInfo = new ImportInfo();
        ImportInfoBuild importInfoBuild = task.getImportInfoBuild();
        try {
            for(KeyWord keyWord:list){
                if (StringUtils.isBlank(keyWord.getKeywordName())) {
                    importInfoBuild.increment(CheckResult.IllegalKeyword, keyWord);
                    continue;
                }
                QueryParameters params = new QueryParameters();
                params.addParam("keywordName",keyWord.getKeywordName());
                params.addParam("targetId",keyWord.getTargetId());
                int count = keyWordService.findKeywordCount(params);
                if(count>0){
                    importInfoBuild.increment(CheckResult.KeywordExists, keyWord);
                    continue;
                }
                if(!namePattern.matcher(keyWord.getKeywordName()).matches() || keyWord.getKeywordName().length() > 50){
                    importInfoBuild.increment(CheckResult.IllegalKeyword, keyWord);
                    continue;
                }
                try {
                    keyWordService.addOrUpdateKeyword(keyWord,null);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    importInfoBuild.increment(CheckResult.SYSTEM_ERROR, keyWord);
                }
                importInfoBuild.increment(CheckResult.Legal, keyWord);
            }
            handleImportResult(task, impInfo, importInfoBuild);
        } catch (Exception e) {
            impInfo.setResult(Result.Failed);
            throw e;
        }
        return impInfo;
    }

    public ImportInfo importContact(int entId, int userId, List<Contact> contactList,
                                    FileTask task) throws Exception{
        ImportInfo impInfo = new ImportInfo();
        ImportInfoBuild infoBuild = task.getImportInfoBuild();
        final String phoneReg = config.getPhonePattern();
        final String commonNamePattern = config.getCommonNamePattern();
        List<Contact> importList = new ArrayList<>();
        for (Contact c : contactList) {
            if (StringUtils.isBlank(c.getName())) {
                infoBuild.increment(CheckResult.ContactNameEmpty, c);
                continue;
            } else if(c.getName().length()>50){
                infoBuild.increment(CheckResult.ContactIllegalName, c);
                continue;
            }else if (!namePattern.matcher(c.getName()).matches()) {
                infoBuild.increment(CheckResult.ContactIllegalName, c);
                continue;
            }
            if (c.getIdentifier().length()>50) {
                infoBuild.increment(CheckResult.ContactIllegalIdentifier, c);
                continue;
            }
            if (c.getRemark().length()>200) {
                infoBuild.increment(CheckResult.ContactIllegalRemark, c);
                continue;
            }
            if (StringUtils.isBlank(c.getPhone())
                    || !c.getPhone().matches(phoneReg)) {
                infoBuild.increment(CheckResult.IllegalPhone, c);
                continue;
            }
            if(c.getVipVal()==-1){
            	 infoBuild.increment(CheckResult.IllegalVIP, c);
                 continue;
            }
            if(c.getSex() ==-1){
           	 	infoBuild.increment(CheckResult.IllegalSex, c);
                continue;
           }
            if(c.getExt1()!=null){
            	infoBuild.increment(CheckResult.IllegalBirthday, c);
                continue;
            }
            String phone = com.xuanwu.mos.utils.StringUtil.removeZhCnCode(c.getPhone());
            c.setPhone(phone);
            importList.add(c);
        }
        try {
            contactService.importContactData(importList,userId,infoBuild,entId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handleImportResult(task, impInfo, infoBuild);
        infoBuild.getFailed().clear();
        return impInfo;
    }
}

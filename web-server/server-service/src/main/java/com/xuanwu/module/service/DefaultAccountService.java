package com.xuanwu.module.service;

import com.xuanwu.mos.domain.entity.Account;
import com.xuanwu.mos.domain.entity.CommonAccount;
import com.xuanwu.mos.domain.enums.AccountStatus;
import com.xuanwu.mos.rest.repo.AccountRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 公众账号数据服务
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Component
public class DefaultAccountService implements AccountService{

    private static final Logger logger = LoggerFactory
            .getLogger(DefaultAccountService.class);

    private AccountRepo accountDao;

    @Override
    public Account findAccountById(int id) {
        return accountDao.findAccount(String.valueOf(id), CommonAccount.AccountFindType.ID);
    }

    @Override
    public Account findAccountByEmail(String email) {
        return accountDao.findAccount(email, CommonAccount.AccountFindType.EMAIL);
    }

    @Override
    public Account findAccountByName(String name) {
        return accountDao.findAccount(name, CommonAccount.AccountFindType.NAME);
    }

    @Override
    public int findAccountsCount(int userId, String email, String name,
                                 String nick, Account.AccountState state) {
        return accountDao.findAccountsCount(userId, email, name, nick, state);
    }

    @Override
    public List<Account> findAccounts(int userId, String email, String name,
                                      String nick, Account.AccountState state, int offset, int reqNum) {
        return accountDao.findAccounts(userId, email, name, nick, state,
                offset, reqNum);
    }

    @Override
    public List<Account> findAccountsByState(Account.AccountState state) {
        return accountDao.findAccountsByState(state);
    }

    @Override
    public boolean addAccount(Account account) {
        return accountDao.addAccount(account);
    }

    @Override
    public boolean updateAccount(Account account) {
        return accountDao.updateAccount(account);
    }

    @Override
    public boolean deleteAccountById(int id) {
        return accountDao.deleteAccountById(id);
    }

    @Override
    public boolean updateAccountState(int id, Account.AccountState state) {
        return accountDao.updateAccountState(id, state);
    }

    @Override
    public boolean updateAccountStatus(int id, AccountStatus status) {
        return accountDao.updateAccountStatus(id, status);
    }

    @Override
    public boolean updateAccountLastSyncTime(int id, Date lastSyncTime) {
        return accountDao.updateAccountLastSyncTime(id, lastSyncTime);
    }

    @Override
    public String getAccessToken(int id) {
        /*Account account = accountDao.findAccount(String.valueOf(id),
                CommonAccount.AccountFindType.ID);
        if (account == null) {
            return null;
        }

        String appId = account.getAppId();
        String appSecret = account.getAppSecret();
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)) {
            return null;
        }

        String accessToken = account.getAccessToken();
        Date expireTime = account.getExpireTime();
        if (StringUtils.isNotBlank(accessToken) && expireTime != null
                && expireTime.after(new Date())) {
            return accessToken;
        }

        String accessTokenUrl = WeChatKeys.ACCESS_TOKEN_URL.replace("${appId}",
                appId);
        accessTokenUrl = accessTokenUrl.replace("${appSecret}", appSecret);

        WeChatHttpClient httpClient = new WeChatHttpClient();
        for (int i = 0; i < WeChatKeys.API_RETRY_TIMES; i++) {
            String response = httpClient.get(accessTokenUrl);
            JsonData jsonData = JsonParse.format(response);
            if (jsonData == null) {
                continue;
            }

            accessToken = jsonData.getAccess_token();
            int expires = jsonData.getExpires_in();
            if (accessToken != null) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, expires);
                account.setAccessToken(accessToken);
                account.setExpireTime(cal.getTime());
                accountDao.updateAccessToken(account);
                return accessToken;// break;
            } else {
                DateUtil.sleepWithoutInterrupte(WeChatKeys.API_RETRY_SLEEP_TIMES);
                logger.error("Get accessToken error by response: {} ,[Id={}]",
                        response, id);
            }
        }

        return accessToken;*/
        return null;
    }

    @Override
    public boolean updateAccessToken(Account account) {
        return accountDao.updateAccessToken(account);
    }

    @Override
    public boolean updateSyncMember(int id, boolean sync) {
        return accountDao.updateSyncMember(id, sync);
    }

    @Autowired
    public void setAccountDao(AccountRepo accountDao) {
        this.accountDao = accountDao;
    }
}

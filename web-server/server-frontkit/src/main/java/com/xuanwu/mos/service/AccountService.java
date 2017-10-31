package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.Account;
import com.xuanwu.mos.domain.enums.AccountStatus;

import java.util.Date;
import java.util.List;

/**
 * 公众账号数据服务
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public interface AccountService {

    /**
     * 查找公众账号
     * @return
     */
    public Account findAccountById(int id);

    public Account findAccountByEmail(String email);

    public Account findAccountByName(String name);

    /**
     * 公众账号总数
     *
     * @param userId
     * @param email
     * @param name
     * @param nick
     * @param state
     * @return
     */
    public int findAccountsCount(int userId, String email, String name,
                                 String nick, Account.AccountState state);

    /**
     * 公众账号列表
     *
     * @param userId
     * @param email
     * @param name
     * @param nick
     * @param state
     * @param offset
     * @param reqNum
     * @return
     */
    public List<Account> findAccounts(int userId, String email, String name,
                                      String nick, Account.AccountState state, int offset, int reqNum);

    /**
     * 公众账号列表
     *
     * @param state
     * @return
     */
    public List<Account> findAccountsByState(Account.AccountState state);

    /**
     * 新增公众账号
     *
     * @param account
     * @return
     */
    public boolean addAccount(Account account);

    /**
     * 修改公众账号
     *
     * @param account
     * @return
     */
    public boolean updateAccount(Account account);

    /**
     * 删除公众账号
     *
     * @param id
     * @return
     */
    public boolean deleteAccountById(int id);

    /**
     * 更改公众账号状态
     *
     * @param id
     * @param state
     * @return
     */
    public boolean updateAccountState(int id, Account.AccountState state);

    /**
     * 更改公众账号最后一次同步时间
     *
     * @param id
     * @param lastSyncTime
     * @return
     */
    public boolean updateAccountLastSyncTime(int id, Date lastSyncTime);

    /**
     * 更改公众账号验证状态
     *
     * @param id
     * @param status
     * @return
     */
    public boolean updateAccountStatus(int id, AccountStatus status);

    /**
     * 获取公众账号的AccessToken
     *
     * @param id
     * @return
     */
    public String getAccessToken(int id);

    /**
     * 更新公众账号的AccessToken
     *
     * @param account
     * @return
     */
    public boolean updateAccessToken(Account account);

    /**
     * 更新公众账号的同步状态
     *
     * @param id
     * @param sync
     * @return
     */
    public boolean updateSyncMember(int id, boolean sync);
}

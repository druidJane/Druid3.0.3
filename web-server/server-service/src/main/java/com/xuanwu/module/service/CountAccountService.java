package com.xuanwu.module.service;

import com.xuanwu.mos.domain.entity.CountAccount;

import java.util.Date;
import java.util.List;

/**
 * 用户计数账号
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public interface CountAccountService {
    /**
     * 新增计数账号
     *
     * @param countAccount
     * @return
     */
    public void addCountAccount(CountAccount countAccount, int userId);

    /**
     * 获得用户计数账号
     * @param userId
     * @return
     */
    public CountAccount getCountAccountByUserId(int userId);

    public void updateCountAccount(CountAccount countAccount);

    public void deleteByUserIds(List<Integer> userIds);

    public void updateCountAccountRemain(CountAccount account);

    public int findCountAccountCount(Date modifyDate, int chargeType);

    public List<CountAccount> findCountAccount(Date modifyDate, int chargeType);

    public void updateCountAccountExt(CountAccount account);

    public void updateCountAccountLimit(int accountId, int daySendLimit);

    public CountAccount findCountAccountById(int accountId);

    public CountAccount findCountAccountByName(String name);

    public void updateMonthlyCountAccountLimit(int accountId, int smsSendLimit,
                                               int mmsSendLimit);

    //新增用户时，往gsms_user_account_bind表中新增一条记录,by jiangziyuan
    public void insertUserAccountBind(int capitalAccountId, String[] userIds);
}

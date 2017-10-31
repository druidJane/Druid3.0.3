package com.xuanwu.mos.service;

import com.xuanwu.mos.domain.entity.CountAccount;
import com.xuanwu.mos.rest.repo.ChargeAccountRepo;
import com.xuanwu.mos.rest.repo.CountAccountRepo;
import com.xuanwu.mos.utils.SessionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Component("countAccountService")
public class DefaultCountAccountService implements CountAccountService {

    private CountAccountRepo countAccountDao;

    //by jiangziyuan
    private ChargeAccountRepo chargeAccountDao;

    @Override
    public void addCountAccount(CountAccount countAccount, int userId) {
        countAccountDao.addCountAccount(countAccount, userId);
    }

    @Override
    public CountAccount getCountAccountByUserId(int userId){
        return countAccountDao.getCountAccountByUserId(userId);
    }

    @Override
    public void updateCountAccount(CountAccount countAccount){
        CountAccount c = countAccountDao.getCountAccountById(countAccount.getId());
        int smsLimit0 = c.getSmsLimit();
        int smsRemain0 = c.getSmsRemain();
        int mmsLimit0 = c.getMmsLimit();
        int mmsRemain0 = c.getMmsRemain();

        int smsLimit1 = countAccount.getSmsLimit();
        int mmsLimit1 = countAccount.getMmsLimit();
        //设置短信发送量
        if(smsLimit1>0){//
            if(smsLimit1>smsLimit0){//新的短信县质量大于之前的
                countAccount.setSmsRemain(smsRemain0+(smsLimit1-smsLimit0));
            }else{
                countAccount.setSmsRemain((smsRemain0-(smsLimit0-smsLimit1))<0?0:(smsRemain0-(smsLimit0-smsLimit1)));
            }
        }else{//不限制
        }
        //设置彩信发送量
        if(mmsLimit1>0){
            if(mmsLimit1>mmsLimit0){
                countAccount.setMmsRemain(mmsRemain0+(mmsLimit1-mmsLimit0));
            }else{
                countAccount.setMmsRemain((mmsRemain0-(mmsLimit0-mmsLimit1))<0?0:(mmsRemain0-(mmsLimit0-mmsLimit1)));
            }
        }
        countAccountDao.updateCountAccount(countAccount);
    }

    @Override
    public void updateCountAccountLimit(int accountId,int daySendLimit) {
        CountAccount  countAccount= countAccountDao.getCountAccountById(accountId);
        int orgSmsHasSend = countAccount.getSmsHasSend();
        int orgMmsHasSend = countAccount.getMmsHasSend();
        //设置短彩信日发送限制量
        if(daySendLimit>0){ //如果有限制则进行处理，否则全部归零
            if(countAccount.getSmsLimit()>0){
                countAccount.setSmsRemain((daySendLimit-orgSmsHasSend)<0?0:(daySendLimit-orgSmsHasSend));
            }else{
                countAccount.setSmsRemain(daySendLimit);
            }
            if(countAccount.getMmsLimit()>0){
                countAccount.setMmsRemain((daySendLimit-orgMmsHasSend)<0?0:(daySendLimit-orgMmsHasSend));
            }else{
                countAccount.setMmsRemain(daySendLimit);
            }

        }else{
            countAccount.setSmsHasSend(0);
            countAccount.setMmsHasSend(0);
            countAccount.setSmsRemain(0);
            countAccount.setMmsRemain(0);
        }
        countAccount.setSmsLimit(daySendLimit);
        countAccount.setMmsLimit(daySendLimit);
        countAccount.setModifyUser(SessionUtil.getCurUser().getId());
        countAccount.setModifyTime(new Date());
        countAccountDao.updateCountAccount(countAccount);
    }

    @Override
    public void updateMonthlyCountAccountLimit(int accountId, int smsSendLimit,
                                               int mmsSendLimit) {
        CountAccount  countAccount= countAccountDao.getCountAccountById(accountId);
        int orgSmsHasSend = countAccount.getSmsHasSend();
        int orgMmsHasSend = countAccount.getMmsHasSend();
        //设置短彩信日发送限制量
        if(smsSendLimit>0){ //如果有限制则进行处理，否则全部归零
            if(countAccount.getSmsLimit()>0){
                countAccount.setSmsRemain((smsSendLimit-orgSmsHasSend)<0?0:(smsSendLimit-orgSmsHasSend));
            }else{
                countAccount.setSmsRemain(smsSendLimit);
            }
        }
        if(mmsSendLimit>0){
            if(countAccount.getMmsLimit()>0){
                countAccount.setMmsRemain((mmsSendLimit-orgMmsHasSend)<0?0:(mmsSendLimit-orgMmsHasSend));
            }else{
                countAccount.setMmsRemain(mmsSendLimit);
            }
        }
        if(smsSendLimit<=0 && mmsSendLimit<=0){
            countAccount.setSmsHasSend(0);
            countAccount.setMmsHasSend(0);
            countAccount.setSmsRemain(0);
            countAccount.setMmsRemain(0);
        }
        countAccount.setSmsLimit(smsSendLimit);
        countAccount.setMmsLimit(mmsSendLimit);
        countAccount.setModifyUser(SessionUtil.getCurUser().getId());
        countAccount.setModifyTime(new Date());
        countAccountDao.updateCountAccount(countAccount);
    }

    //by jiangziyuan
    @Override
    public void insertUserAccountBind(int capitalAccountId, String[] userIds) {
        chargeAccountDao.insertUserAccountBind(capitalAccountId, userIds);
    }

    @Override
    public void updateCountAccountRemain(CountAccount account) {
        countAccountDao.updateCountAccount(account);
    }

    @Override
    public void deleteByUserIds(List<Integer> userIds){
        countAccountDao.deleteByUserIds(userIds);
    }

    @Autowired
    public void setCountAccountDao(CountAccountRepo countAccountDao) {
        this.countAccountDao = countAccountDao;
    }

    //by jiangziyuan
    @Autowired
    public void setChargeAccountDao(ChargeAccountRepo chargeAccountDao) {
        this.chargeAccountDao = chargeAccountDao;
    }

    @Override
    public int findCountAccountCount(Date modifyDate, int chargeType) {
        return countAccountDao.findCountAccountCount(modifyDate, chargeType);
    }

    @Override
    public List<CountAccount> findCountAccount(Date modifyDate, int chargeType) {
        return countAccountDao.findCountAccount(modifyDate, chargeType);
    }

    @Override
    public void updateCountAccountExt(CountAccount account) {
        countAccountDao.updateCountAccount(account);
    }

    @Override
    public CountAccount findCountAccountById(int accountId) {
        return countAccountDao.getCountAccountById(accountId);
    }

    @Override
    public CountAccount findCountAccountByName(String name) {
        return countAccountDao.findCountAccountByName(name);
    }
}

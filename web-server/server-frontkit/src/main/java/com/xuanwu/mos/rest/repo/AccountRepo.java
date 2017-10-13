package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Account;
import com.xuanwu.mos.domain.entity.CommonAccount;
import com.xuanwu.mos.domain.enums.AccountStatus;
import com.xuanwu.msggate.common.sbi.entity.CarrierMsgTypePrice;
import com.xuanwu.msggate.common.sbi.entity.RedSpecNumResult;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Repository
public class AccountRepo extends GsmsMybatisEntityRepository<CommonAccount> {
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.AccountMapper";
    }

    /**
     * Find all the valid account
     * @return
     */
    public Account findAccount(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findAccount"), id);
        }
    }

    public List<CarrierMsgTypePrice> findEntCarrierPrices(int entID) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("entID", entID);
            return session.selectList(fullSqlId("findEntCarrierPrices"), params);
        }
    }

    public List<CarrierMsgTypePrice> findUserCarrierPrices(int capitalAccountID) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("capitalAccountID", capitalAccountID);
            return session.selectList(fullSqlId("findUserCarrierPrices"), params);
        }
    }

    public Integer findUserDefaultBizType(int userID) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findUserDefaultBizType"), userID);
        }
    }

    /**
     * Find the account is free sms auditing or not, 0-not free auditing
     * @param account
     * @return
     */
    public int findAccountSmsFreeAudit(Account account) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findAccountSmsFreeAudit"), account);
        }
    }

    /**
     * Find the account is free mms auditing or not, 0-not free auditing
     * @param account
     * @return
     */
    public int findAccountMmsFreeAudit(Account account) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findAccountMmsFreeAudit"), account);
        }
    }

    /**
     * Find the account has send sms permission or not, 0-has not send permission
     * @param account
     * @return
     */
    public int findAccountSendSmsPermit(Account account) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findAccountSendSmsPermit"), account);
        }
    }

    /**
     * Find the account has send mms permission or not, 0-has not send permission
     * @param account
     * @return
     */
    public int findAccountSendMmsPermit(Account account) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findAccountSendMmsPermit"), account);
        }
    }

    /**
     * Find the enterprise redlist
     * @param entID
     * @return
     */
    public List<RedSpecNumResult> findRedBindResult(int entID) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("entID", entID);
            return session.selectList(fullSqlId("findRedBindResult"), params);
        }
    }

    public int findUserVersion() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findUserVersion"));
        }
    }

    public int findSpecNumVersion() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findSpecNumVersion"));
        }
    }

    public Account findAccount(String idOrName, CommonAccount.AccountFindType type) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("idOrName", idOrName);
            params.put("type", type);
            return session.selectOne(fullSqlId("findAccount"), params);
        }
    }

    public Account findAccountById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            return session.selectOne(fullSqlId("findAccountById"), params);
        }
    }

    public List<Account> findAccountsByState(Account.AccountState state) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("state", state);
            return session.selectList(fullSqlId("findAccountsByState"), params);
        }
    }

    public int findAccountsCount(int userId,String email,String name,String nick, Account.AccountState state) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("email", email);
            params.put("name", name);
            params.put("nick", nick);
            params.put("state", state);
            return session.selectOne(fullSqlId("findAccountsCount"),params);
        }
    }

    public List<Account> findAccounts(int userId,String email,String name,String nick,
                                             Account.AccountState state,int offset,int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("email", email);
            params.put("name", name);
            params.put("nick", nick);
            params.put("state", state);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            return session.selectList(fullSqlId("findAccounts"), params);
        }
    }

    public boolean addAccount(Account account) {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("account", account);
            ret = session.insert(fullSqlId("addAccount"), params);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }

/*    public int updateAccount(Account account){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ret = session.update(fullSqlId("updateAccount"), account);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update account failed: ", e);
        }
        return ret;
    }  */
    public boolean updateAccount(Account account){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ret = session.update(fullSqlId("updateAccount"), account);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update account failed: ", e);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }

    public boolean updateSyncMember(int id,boolean sync){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("sync", sync);
            ret = session.update(fullSqlId("updateSyncMember"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update SyncMember failed: ", e);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }

/*    public int deleteAccountById(int id){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            ret = session.delete(fullSqlId("deleteAccountById"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update account failed: ", e);
        }
        return ret;
    }*/
    public boolean deleteAccountById(int id){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            ret = session.delete(fullSqlId("deleteAccountById"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update account failed: ", e);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }

/*    public int updateAccountState(int id,Account.AccountState state){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("state", state);
            ret = session.update(fullSqlId("updateAccountState"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update AccountState failed: ", e);
        }
        return ret;
    }*/
    public boolean updateAccountState(int id,Account.AccountState state){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("state", state);
            ret = session.update(fullSqlId("updateAccountState"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update AccountState failed: ", e);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }

/*    public int updateAccountStatus(int id,AccountStatus state){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("state", state);
            ret = session.update(fullSqlId("updateAccountStatus"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update AccountStatus failed: ", e);
        }
        return ret;
    }*/
    public boolean updateAccountStatus(int id,AccountStatus state){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("state", state);
            ret = session.update(fullSqlId("updateAccountStatus"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update AccountStatus failed: ", e);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }

    public boolean updateAccountLastSyncTime(int id, Date lastSyncTime){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("lastSyncTime", lastSyncTime);
            ret = session.update(fullSqlId("updateAccountLastSyncTime"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update AccountLastSyncTime failed: ", e);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }

    public int updateAccountOpenId(int id, String openId){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("openId", openId);
            ret = session.update(fullSqlId("updateAccountOpenId"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update AccountOpenId failed: ", e);
        }
        return ret;
    }

    public int updateFSendCount(int id, int massSendCount,Date lastFSendTime ){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("massSendCount", massSendCount);
            map.put("lastFSendTime", lastFSendTime);
            ret = session.update(fullSqlId("updateFSendCount"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update FSendCount failed: ", e);
        }
        return ret;
    }

    public boolean updateAccessToken(Account account ){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("account", account);
            ret = session.update(fullSqlId("updateAccessToken"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Save/Update AccessToken failed: ", e);
        }
        if(ret == 1){
            return true;
        }else{
            return false;
        }
    }
}

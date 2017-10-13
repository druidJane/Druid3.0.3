package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.*;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Repository
public class ChargeAccountRepo extends GsmsMybatisEntityRepository<ChargingAccount> {
    private static final Logger logger = LoggerFactory.getLogger(ChargeAccountRepo.class);
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.ChargeAccountMapper";
    }

    public int findChargeAccountCount(ChargingAccount chargingAccount) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne(fullSqlId("findChargeAccountCount"),chargingAccount);
        } finally {
            session.close();
        }
    }

    public List<ChargingAccount> findChargeAccounts(ChargingAccount chargingAccount, DynamicParam param) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chargingAccount", chargingAccount);
            params.put("param", param);
            List<ChargingAccount> chargeAccounts = session.selectList(fullSqlId("findChargeAccounts"),params);
            Double price = null;
            for (ChargingAccount chargeAccount : chargeAccounts) {
                if (chargeAccount.getDeductType() == 0) {
                    Map<String, Object> params1 = new HashMap<>();
                    params1.put("msgType", MsgTicket.MsgType.SMS.getIndex());
                    params1.put("capitalAccountId", chargeAccount.getId());
                    price = session.selectOne(fullSqlId("findMsgPriceWithCapitalId"),params1);
                    if (price != null) {
                        chargeAccount.setSmsPrice(price);
                    }
                    Map<String, Object> params2 = new HashMap<>();
                    params2.put("msgType", MsgTicket.MsgType.MMS.getIndex());
                    params2.put("capitalAccountId", chargeAccount.getId());
                    price = session.selectOne(fullSqlId("findMsgPriceWithCapitalId"),params2);
                    if (price != null) {
                        chargeAccount.setMmsPrice(price);
                    }
                } else {
                    Map<String, Object> params3 = new HashMap<>();
                    params3.put("msgType", MsgTicket.MsgType.SMS.getIndex());
                    params3.put("enterpriseId", chargeAccount.getEnterpriseId());
                    price = session.selectOne(fullSqlId("findMsgPriceWithEnterpriseId"),params3);
                    if (price != null) {
                        chargeAccount.setSmsPrice(price);
                    }
                    Map<String, Object> params4 = new HashMap<>();
                    params4.put("msgType", MsgTicket.MsgType.MMS.getIndex());
                    params4.put("enterpriseId", chargeAccount.getEnterpriseId());
                    price = session.selectOne(fullSqlId("findMsgPriceWithEnterpriseId"),params4);
                    if (price != null) {
                        chargeAccount.setMmsPrice(price);
                    }
                }
            }
            return chargeAccounts;
        } finally {
            session.close();
        }
    }

    public ChargingAccount findChargeAccountInfo(int enterpriseId, int capitalAccountId) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("enterpriseId", enterpriseId);
            params.put("capitalAccountId", capitalAccountId);
            return session.selectOne(fullSqlId("findChargeAccountInfo"),params);
        } finally {
            session.close();
        }
    }

    public ChargingAccount findParentAccountInfo(int enterpriseId, int state) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("enterpriseId", enterpriseId);
            params.put("state", state);
            return session.selectOne(fullSqlId("findParentAccountInfo"),params);
        } finally {
            session.close();
        }
    }

    public List<ChargingAccount> findChildAccountForCharging(int enterpriseId, int parentId) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("enterpriseId", enterpriseId);
            params.put("parentId", parentId);
            return session.selectList(fullSqlId("findChildAccountForCharging"),params);
        } finally {
            session.close();
        }
    }

    public boolean updateBalanceForCharging(int id, double chargeMoney) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("chargeMoney", chargeMoney);
            int result = session.update(fullSqlId("updateBalanceForCharging"), params);
            session.commit();
            return result > 0;
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();

        } finally {
            session.close();
        }
        return false;
    }

    public boolean addChargingRecordForCharging(ChargingAccount chargingAccount) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.insert(fullSqlId("addChargingRecordForCharging"), chargingAccount);
            session.commit();
            return result > 0;
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    public ChargingAccount getDifferenceByEnterpriseId(int enterpriseId) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne(fullSqlId("getDifferenceByEnterpriseId"), enterpriseId);
        } finally {
            session.close();
        }
    }

    public int findChargeAccountUserCount(ChargingAccountUser accountUser) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne(fullSqlId("findChargeAccountUserCount"),accountUser);
        } finally {
            session.close();
        }
    }

    public List<ChargingAccountUser> findChargeAccountUser(ChargingAccountUser accountUser, DynamicParam param) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("accountUser", accountUser);
            params.put("param", param);
            return session.selectList(fullSqlId("findChargeAccountUser"),params);
        } finally {
            session.close();
        }
    }

    public boolean isExistAccount(int enterpriseId, String accountName) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("enterpriseId", enterpriseId);
            params.put("accountName", accountName);
            int count = session.selectOne(fullSqlId("isExistAccount"),params);
            return count > 0;
        } finally {
            session.close();
        }
    }

    //这个以后要在对应的service方法里改用事务
    public boolean updateChargingAccount(ChargingAccount account) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            session.update(fullSqlId("updateChargingAccount"), account);
            //短信单价
            HashMap<String, Object> map = new HashMap<>();
            map.put("enterpriseId", account.getEnterpriseId());
            map.put("id", account.getId());
            map.put("msgType", MsgTicket.MsgType.SMS.getIndex());
            map.put("msgPrice", account.getSmsPriceYD());
            map.put("carrierId", 1);
            session.update(fullSqlId("updateCarrierPrice"), map);

            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("enterpriseId", account.getEnterpriseId());
            map1.put("id", account.getId());
            map1.put("msgType", MsgTicket.MsgType.SMS.getIndex());
            map1.put("msgPrice", account.getSmsPriceLT());
            map1.put("carrierId", 2);
            session.update(fullSqlId("updateCarrierPrice"), map1);

            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("enterpriseId", account.getEnterpriseId());
            map2.put("id", account.getId());
            map2.put("msgType", MsgTicket.MsgType.SMS.getIndex());
            map2.put("msgPrice", account.getSmsPriceXLT());
            map2.put("carrierId", 3);
            session.update(fullSqlId("updateCarrierPrice"), map2);

            HashMap<String, Object> map3 = new HashMap<>();
            map3.put("enterpriseId", account.getEnterpriseId());
            map3.put("id", account.getId());
            map3.put("msgType", MsgTicket.MsgType.SMS.getIndex());
            map3.put("msgPrice", account.getSmsPriceCDMA());
            map3.put("carrierId", 4);
            session.update(fullSqlId("updateCarrierPrice"), map3);

            //彩信单价
            HashMap<String, Object> map4 = new HashMap<>();
            map4.put("enterpriseId", account.getEnterpriseId());
            map4.put("id", account.getId());
            map4.put("msgType", MsgTicket.MsgType.MMS.getIndex());
            map4.put("msgPrice", account.getMmsPriceYD());
            map4.put("carrierId", 1);
            session.update(fullSqlId("updateCarrierPrice"), map4);

            HashMap<String, Object> map5 = new HashMap<>();
            map5.put("enterpriseId", account.getEnterpriseId());
            map5.put("id", account.getId());
            map5.put("msgType", MsgTicket.MsgType.MMS.getIndex());
            map5.put("msgPrice", account.getMmsPriceLT());
            map5.put("carrierId", 2);
            session.update(fullSqlId("updateCarrierPrice"), map5);

            HashMap<String, Object> map6 = new HashMap<>();
            map6.put("enterpriseId", account.getEnterpriseId());
            map6.put("id", account.getId());
            map6.put("msgType", MsgTicket.MsgType.MMS.getIndex());
            map6.put("msgPrice", account.getMmsPriceXLT());
            map6.put("carrierId", 3);
            session.update(fullSqlId("updateCarrierPrice"), map6);

            HashMap<String, Object> map7 = new HashMap<>();
            map7.put("enterpriseId", account.getEnterpriseId());
            map7.put("id", account.getId());
            map7.put("msgType", MsgTicket.MsgType.MMS.getIndex());
            map7.put("msgPrice", account.getMmsPriceCDMA());
            map7.put("carrierId", 4);
            session.update(fullSqlId("updateCarrierPrice"), map7);
            session.commit();
            return true;
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    //这个以后要在对应的service方法里改用事务
    public boolean deleteChargingAccount(int capitalAccountId, int enterpriseId) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("enterpriseId", enterpriseId);
            map.put("state", -1);
            ChargingAccount parentAccountInfo = session.selectOne(fullSqlId("findParentAccountInfo"), map);
            if (parentAccountInfo != null) {
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("capitalAccountId", capitalAccountId);
                map1.put("parentCapitalAccountId", parentAccountInfo.getId());
                session.update(fullSqlId("updateAccountBind"), map1);

                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("capitalAccountId", capitalAccountId);
                map2.put("enterpriseId", enterpriseId);
                session.delete(fullSqlId("deleteChargingAccount"), map2);
            }
            session.commit();
            return true;
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    public List<Department> getAsyncChargeAccountDepts(boolean isTop, Integer parentId,
                                                       int enterpriseId, Integer capitalAccountId) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            if (isTop) {
                Map<String, Object> params = new HashMap<>();
                params.put("enterpriseId", enterpriseId);
                params.put("capitalAccountId", capitalAccountId);
                return session.selectList(fullSqlId("getAsyncChargeAccountDeptTop"), params);
            } else {
                //return accountMapper.getAsyncChargeAccountDept(parentId, capitalAccountId);
                Map<String, Object> params = new HashMap<>();
                params.put("parentId", parentId);
                params.put("capitalAccountId", capitalAccountId);
                return session.selectList(fullSqlId("getAsyncChargeAccountDept"), params);//??
            }
        } finally {
            session.close();
        }

    }

    public int findAccountUserCount(ChargingAccountUser accountUser) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne(fullSqlId("findAccountUserCount"), accountUser);
        } finally {
            session.close();
        }
    }

    public List<ChargingAccountUser> findAccountUsers(ChargingAccountUser accountUser, DynamicParam param) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("accountUser", accountUser);
            params.put("param", param);
            return session.selectOne(fullSqlId("findAccountUsers"), params);
        } finally {
            session.close();
        }
    }

    //这个以后要在对应的service方法里改用事务
    public boolean editIncludeUserAccount(int capitalAccountId, String[] userIds) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            session.delete(fullSqlId("deleteUserAccountBind"), userIds);
            HashMap<String, Object> map = new HashMap<>();
            map.put("userIds", userIds);
            map.put("capitalAccountId", capitalAccountId);
            session.insert(fullSqlId("insertUserAccountBind"), map);
            session.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return false;
    }

    //by jiangziyuan
    public boolean insertUserAccountBind(int capitalAccountId, String[] userIds) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userIds", userIds);
            map.put("capitalAccountId", capitalAccountId);
            session.insert(fullSqlId("insertUserAccountBind"), map);
            session.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public boolean editCancelUserAccount(int enterpriseId, String[] userIds) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("enterpriseId", enterpriseId);
            params.put("state", Account.AccountState.NOT_LIMIT.getIndex());
            ChargingAccount parentAccountInfo = session.selectOne(fullSqlId("findParentAccountInfo"),params);
            if (parentAccountInfo != null) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("userIds", userIds);
                map.put("capitalAccountId", parentAccountInfo.getId());
                session.update(fullSqlId("updateCancelUserAccount"), map);
            }
            session.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public boolean addChargeAccount(ChargingAccount account) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int capitalAccountId = session.insert(fullSqlId("addChargeAccount"), account);
            if (capitalAccountId > 0) {
                //短信单价
                HashMap<String, Object> map = new HashMap<>();
                map.put("enterpriseId", account.getEnterpriseId());
                map.put("capitalAccountId", capitalAccountId);
                map.put("msgType", MsgTicket.MsgType.SMS.getIndex());
                map.put("msgPrice", account.getSmsPriceYD());
                map.put("carrierId", 1);
                session.insert(fullSqlId("addAccountCarrierPrice"), map);


                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("enterpriseId", account.getEnterpriseId());
                map1.put("capitalAccountId", capitalAccountId);
                map1.put("msgType", MsgTicket.MsgType.SMS.getIndex());
                map1.put("msgPrice", account.getSmsPriceLT());
                map1.put("carrierId", 2);
                session.insert(fullSqlId("addAccountCarrierPrice"), map1);

                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("enterpriseId", account.getEnterpriseId());
                map2.put("capitalAccountId", capitalAccountId);
                map2.put("msgType", MsgTicket.MsgType.SMS.getIndex());
                map2.put("msgPrice", account.getSmsPriceXLT());
                map2.put("carrierId", 3);
                session.insert(fullSqlId("addAccountCarrierPrice"), map2);

                HashMap<String, Object> map3 = new HashMap<>();
                map3.put("enterpriseId", account.getEnterpriseId());
                map3.put("capitalAccountId", capitalAccountId);
                map3.put("msgType", MsgTicket.MsgType.SMS.getIndex());
                map3.put("msgPrice", account.getSmsPriceCDMA());
                map3.put("carrierId", 4);
                session.insert(fullSqlId("addAccountCarrierPrice"), map3);

                //彩信单价
                HashMap<String, Object> map4 = new HashMap<>();
                map4.put("enterpriseId", account.getEnterpriseId());
                map4.put("capitalAccountId", capitalAccountId);
                map4.put("msgType", MsgTicket.MsgType.MMS.getIndex());
                map4.put("msgPrice", account.getMmsPriceYD());
                map4.put("carrierId", 1);
                session.insert(fullSqlId("addAccountCarrierPrice"), map4);

                HashMap<String, Object> map5 = new HashMap<>();
                map5.put("enterpriseId", account.getEnterpriseId());
                map5.put("capitalAccountId", capitalAccountId);
                map5.put("msgType", MsgTicket.MsgType.MMS.getIndex());
                map5.put("msgPrice", account.getMmsPriceLT());
                map5.put("carrierId", 2);
                session.insert(fullSqlId("addAccountCarrierPrice"), map5);

                HashMap<String, Object> map6 = new HashMap<>();
                map6.put("enterpriseId", account.getEnterpriseId());
                map6.put("capitalAccountId", capitalAccountId);
                map6.put("msgType", MsgTicket.MsgType.MMS.getIndex());
                map6.put("msgPrice", account.getMmsPriceXLT());
                map6.put("carrierId", 3);
                session.insert(fullSqlId("addAccountCarrierPrice"), map6);

                HashMap<String, Object> map7 = new HashMap<>();
                map7.put("enterpriseId", account.getEnterpriseId());
                map7.put("capitalAccountId", capitalAccountId);
                map7.put("msgType", MsgTicket.MsgType.MMS.getIndex());
                map7.put("msgPrice", account.getMmsPriceCDMA());
                map7.put("carrierId", 4);
                session.insert(fullSqlId("addAccountCarrierPrice"), map7);

                session.commit();
                return true;
            } else {
                session.rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public int getRecordCount(ChargeRecord chargingRecord) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne(fullSqlId("getRecordCount"), chargingRecord);
        } finally {
            session.close();
        }
    }

    public List<ChargeRecord> listRecords(ChargeRecord chargingRecord, DynamicParam param) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chargingRecord", chargingRecord);
            params.put("param", param);
            return session.selectList(fullSqlId("listRecords"), params);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List<ChargeRecord> findFailChargingRecords(int enterpriseId) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            return session.selectOne(fullSqlId("findFailChargingRecords"), enterpriseId);
        } finally {
            session.close();
        }
    }

    public boolean updateChargingRecord(ChargeRecord record) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update(fullSqlId("updateChargingRecord"), record);
            session.commit();
            return true;
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }
}

package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.CapitalAccount;
import com.xuanwu.mos.domain.entity.MsgTicket;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription
 * @Data 2017-3-31
 * @Version 1.0.0
 */
@Repository
public class CapitalAccountRepo extends GsmsMybatisEntityRepository<CapitalAccount> {

	private static final Logger logger = LoggerFactory.getLogger(CapitalAccountRepo.class);

	@Override
	protected String namesapceForSqlId() {
		return "com.xuanwu.mos.mapper.CapitalAccountMapper";
	}

	public CapitalAccount getDifferenceByEnterpriseId(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getDifferenceByEnterpriseId"), enterpriseId);
		}
	}

	public BigDecimal findMsgPriceWithCapitalId(MsgTicket.MsgType msgType, int capitalId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("msgType", msgType);
			params.put("capitalId", capitalId);
			return session.selectOne(fullSqlId("findMsgPriceWithCapitalId"), params);
		}
	}

	public BigDecimal findMsgPriceWithEnterpriseId(MsgTicket.MsgType msgType, int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("msgType", msgType);
			params.put("enterpriseId", enterpriseId);
			return session.selectOne(fullSqlId("findMsgPriceWithEnterpriseId"), params);
		}
	}

	public CapitalAccount findParentAccountInfo(int enterpriseId, UserState userState) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("enterpriseId", enterpriseId);
			params.put("userState", userState);
			return session.selectOne(fullSqlId("findParentAccountInfo"), params);
		}
	}

	public int addCapitalAccount(CapitalAccount account) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.insert(fullSqlId("addCapitalAccount"), account);
			return account.getId();
		} catch (Exception e) {
			logger.error("Add capitalAccount failed : " + e);
			throw new RepositoryException(e);
		}
	}

	public void addAccountCarrierPrice(int enterpriseId, int capitalAccountId,
									   MsgTicket.MsgType msgType, BigDecimal msgPrice,
									   int[] carrierIds) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("enterpriseId", enterpriseId);
			params.put("capitalAccountId", capitalAccountId);
			params.put("msgType", msgType);
			params.put("msgPrice", msgPrice);
			params.put("carrierIds", carrierIds);
			session.insert(fullSqlId("addAccountCarrierPrice"), params);
			session.commit(true);
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

	public List<CapitalAccount> findChildAccountForCharging(int enterpriseId, Integer parentAccountId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("enterpriseId", enterpriseId);
			params.put("parentAccountId", parentAccountId);
			return session.selectList(fullSqlId("findChildAccountForCharging"), params);
		}
	}

	public void updateBalanceForCharging(CapitalAccount account) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.update(fullSqlId("updateBalanceForCharging"), account);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update balance for charging failed：" + e);
			throw new RepositoryException(e);
		}
	}

	public void addChargingRecordForCharging(CapitalAccount account) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.insert(fullSqlId("addChargingRecordForCharging"), account);
			session.commit(true);
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

	public boolean editIncludeUserAccount(Integer capitalAccountId, String[] userIds) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("userIds", userIds);
			session.delete(fullSqlId("deleteUserAccountBind"), params);
			params.put("capitalAccountId", capitalAccountId);
			session.insert(fullSqlId("insertUserAccountBind"), params);
			session.commit(true);
			return true;
		} catch (Exception e) {
			logger.error("Edit include user account failed：" + e);
			throw new RepositoryException(e);
		}
	}

	public boolean cancelUserAccount(String[] userIds, Integer capitalAccountId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("capitalAccountId", capitalAccountId);
			params.put("userIds", userIds);
			session.update(fullSqlId("cancelUserAccount"), params);
			session.commit(true);
			return true;
		} catch (Exception e) {
			logger.error("Cancel user account failed：" + e);
			throw new RepositoryException(e);
		}
	}

	public boolean updateCapitalAccount(CapitalAccount account) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.update(fullSqlId("updateCapitalAccount"), account);
			session.commit(true);
			return true;
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

	public void updateCarrierPrice(int enterpriseId, Integer capitalAccountId,
								   MsgTicket.MsgType msgType,
								   BigDecimal msgPrice, int carrierId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("enterpriseId", enterpriseId);
			params.put("capitalAccountId", capitalAccountId);
			params.put("carrierId", carrierId);
			params.put("msgType", msgType);
			params.put("msgPrice", msgPrice);
			session.update(fullSqlId("updateCarrierPrice"), params);
			session.commit(true);
		} catch (Exception e) {
			logger.error("Update carrier price failed, cause is:{}", e);
			throw new RepositoryException(e);
		}
	}

	public boolean delCapitalAccount(Integer[] ids, int enterpriseId,
									 int parentAccountId) throws RepositoryException {
		SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
		try {
			for (Integer capitalAccountId : ids) {
				Map<String, Object> params = new HashMap<>();
				params.put("capitalAccountId", capitalAccountId);
				params.put("enterpriseId", enterpriseId);
				params.put("parentAccountId", parentAccountId);
				session.update(fullSqlId("updateAccountBind"), params);
				session.update(fullSqlId("delCapitalAccount"), params);
			}
			session.commit(true);
			return true;
		} catch (Exception e) {
			session.rollback();
			throw new RepositoryException(e);
		} finally {
			session.close();
		}
	}

	public List<CapitalAccount> findCapitalAccountByEntId1(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findCapitalAccountByEntId1"), enterpriseId);
		}
	}

	public List<CapitalAccount> findCapitalAccountByEntId2(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findCapitalAccountByEntId2"), enterpriseId);
		}
	}

	public List<CapitalAccount> findCapitalAccountByDeptId(Integer deptId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findCapitalAccountByDeptId"), deptId);
		}
	}

	public List<CapitalAccount> findBindCapitalAccounts(int userId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectList(fullSqlId("findBindCapitalAccounts"), userId);
		}
	}

	public int isExistAccountName(CapitalAccount account) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("isExistAccountName"), account);
		}
	}
    public List<CapitalAccount> findCapitalAccountsSimpleNotDel(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findCapitalAccountsSimpleNotDel"),params);
        }
    }

	//by jiangziyuan
	public CapitalAccount findCurrentAccountByEntId(int enterpriseId) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("findCurrentAccountByEntId"), enterpriseId);
		}
	}

	public int bindEntAccount(Integer entAccountInfoId, int userId) throws RepositoryException {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> params = new HashMap<>();
			params.put("entAccountInfoId", entAccountInfoId);
			params.put("userId", userId);
			return session.insert(fullSqlId("bindEntAccount"), params);
		} catch (Exception e) {
			logger.error("Bind enterprise account failed, cause is:{}", e);
			throw new RepositoryException(e);
		}
	}

	public String getDelAccountName(Integer[] ids) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return session.selectOne(fullSqlId("getDelAccountName"), ids);
		}
	}
}

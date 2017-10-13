package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.exception.RepositoryException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
@Repository
public class CarrierChannelRepo extends GsmsMybatisEntityRepository<CarrierChannel> {
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.CarrierChannelMapper";
    }

    public List<CarrierChannel> findSimpleChannelByNum(String channelNum,int msgType,int state,int isRemove) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("channelNum", channelNum);
            params.put("msgType", msgType);
            params.put("state", state);
            params.put("isRemove", isRemove);
            return session.selectList(fullSqlId("findSimpleChannelByNum"), params);
        }
    }

    public int findCarrierChannelCount(String name,String channelNum,int carrier,int msgType, int enterpriseId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("channelNum", channelNum);
            params.put("carrier", carrier);
            params.put("msgType", msgType);
            params.put("enterpriseId", enterpriseId);
            return session.selectOne(fullSqlId("findCarrierChannelCount"), params);
        }
    }

    public List<CarrierChannel> findCarrierChannel(String name,String channelNum,int carrier,int msgType, int enterpriseId,
                                                   int offset,int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("channelNum", channelNum);
            params.put("carrier", carrier);
            params.put("msgType", msgType);
            params.put("enterpriseId", enterpriseId);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            return session.selectList(fullSqlId("findCarrierChannel"), params);
        }
    }

    public CarrierChannel findChannelById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findChannelById"), id);
        }
    }

    public int findSysConfigMixMaxLength() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findSysConfigMixMaxLength"));
        }
    }

    public CarrierChannel findCarrierChannelByName(String name) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findCarrierChannelByName"), name);
        }
    }

    /**
     * @param identity
     * @return
     */
    public CarrierChannel findCarrierChannelByIdentity(String identity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findCarrierChannelByIdentity"), identity);
        }
    }


    /**
     * @param rc
     * @return
     */
    public int storeRegionCarrier(RegionCarrier rc) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.insert(fullSqlId("storeRegionCarrier"), rc);
        }
    }

    /**
     *
     * @param enterpriseID
     * @return
     */
    public List<CarrierChannel> findSimpleChannelByEnterprise(int enterpriseID,int isWhitelist) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("enterpriseID", enterpriseID);
            params.put("isWhitelist", isWhitelist);
            return session.selectList(fullSqlId("findSimpleChannelByEnterprise"), params);
        }
    }

    /**
     *
     * @param enterpriseID
     * @return
     */
    public List<Long> findChannelIdsByEnterprise(int enterpriseID,int isWhitelist) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("enterpriseID", enterpriseID);
            params.put("isWhitelist", isWhitelist);
            return session.selectList(fullSqlId("findChannelIdsByEnterprise"), params);
        }
    }

    /**
     * @param cc
     * @return
     */
    public int storeCarrierChannel(CarrierChannel cc) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.insert(fullSqlId("storeCarrierChannel"), cc);
        }
    }

    /**
     * @param cc
     */
    public void storeChannelCarrier(ChannelCarrier cc) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(fullSqlId("storeChannelCarrier"), cc);
        }
    }

    /**
     * @param rc
     */
    public void storeChannelRegion(ChannelRegion rc) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(fullSqlId("storeChannelRegion"), rc);
        }
    }

    /**
     * @param specsvsNum
     */
    public void storeSpecsvsNum(SpecsvsNum specsvsNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(fullSqlId("storeSpecsvsNum"), specsvsNum);
        }
    }

    /**
     * @param esb
     */
    public void storeEnterpriseSpecnumBind(EnterpriseSpecnumBind esb) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(fullSqlId("storeEnterpriseSpecnumBind"), esb);
        }
    }

    /**
     * @param bs
     */
    public void storeBiztypeSpecnum(BiztypeSpecnum bs){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(fullSqlId("storeBiztypeSpecnum"), bs);
        }
    }

    public List<Long> findChannelByParam(String name,String channelNum,int enterpriseID) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("channelNum", channelNum);
            params.put("enterpriseID", enterpriseID);
            return session.selectList(fullSqlId("findChannelByParam"), params);
        }
    }

    /**
     * @param id
     * @return
     */
    public CarrierChannel findCarrierChannelById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findCarrierChannelById"), id);
        }
    }

    public List<CarrierChannel> findAllWhiteChannels() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findAllWhiteChannels"));
        }
    }

    public List<CarrierChannel> findAllNonWhiteChannels() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findAllNonWhiteChannels"));
        }
    }

    /**
     * @param id
     * @return
     */
    public List<BiztypeSpecnum> findAllBizTypeCarrier(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findAllBizTypeCarrier"),id);
        }
    }

    /**
     * @param id
     */
    public void delOldChannelCarrier(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delOldChannelCarrier"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete OldChannelCarrier failed: ", e);
            throw new RepositoryException(e);
        }
    }

    public void delBizTypeCarrier(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delBizTypeCarrier"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete BizTypeCarrier failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delOldChannelRegion(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delOldChannelRegion"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete OldChannelRegion failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delWhileRedirect(int id, int type) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("type", type);
            session.delete(fullSqlId("delWhileRedirect"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete WhileRedirect failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delSpecnumRegionPriority(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delSpecnumRegionPriority"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete SpecnumRegionPriority failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     *  将状态改为1
     */
    public void delChannelChange(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delChannelChange"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete ChannelChange failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delChannelChangeDetail(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delChannelChangeDetail"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete ChannelChangeDetail failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param carrierChannel
     */
    public void modifyCarrierChannel(CarrierChannel carrierChannel) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update(fullSqlId("modifyCarrierChannel"), carrierChannel);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update CarrierChannel failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param specsvsNum
     */
    public void modifySpecsvsNum(SpecsvsNum specsvsNum) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update(fullSqlId("modifySpecsvsNum"), specsvsNum);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update SpecsvsNum failed: ", e);
            throw new RepositoryException(e);
        }
    }


    /**
     * @param id
     * @return
     */
    public int findIsChannelSended(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findIsChannelSended"), id);
        }
    }

    /**
     * @param id
     * @return
     */
    public List<SpecsvsNum> findSpecsvsNumByChannelId(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findSpecsvsNumByChannelId"), id);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public SpecsvsNum findSecondSpecsvsNumByChannelId(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findSecondSpecsvsNumByChannelId"), id);
        }
    }
    /**
     *
     * @param id
     * @return
     */
    public SpecsvsNum findCarrierChannelBySpecsvsNum(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findCarrierChannelBySpecsvsNum"), id);
        }
    }

    /**
     * @param specsvsNums
     * @return
     */
    public List<EnterpriseSpecnumBind> findEnterpriseSpecnumBindBySpecNumId(List<SpecsvsNum> specsvsNums) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findEnterpriseSpecnumBindBySpecNumId"), specsvsNums);
        }
    }

    /**
     * @param enterpriseSpecnumBinds
     * @return
     */
    public List<BiztypeSpecnum> findBizTypeSpecnum(List<EnterpriseSpecnumBind> enterpriseSpecnumBinds) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findBizTypeSpecnum"), enterpriseSpecnumBinds);
        }
    }

    /**
     * @param id
     */
    public void delBiztypeSpecnum(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delBiztypeSpecnum"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete BiztypeSpecnum failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delEnterpriseSpecnumBind(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delEnterpriseSpecnumBind"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete EnterpriseSpecnumBind failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delSpecsvsNum(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delSpecsvsNum"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete SpecsvsNum failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delChannelRegion(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delChannelRegion"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete ChannelRegion failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delChannelCarrier(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delChannelCarrier"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete ChannelCarrier failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param id
     */
    public void delCarrierChannel(int id) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            session.delete(fullSqlId("delCarrierChannel"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete CarrierChannel failed: ", e);
            throw new RepositoryException(e);
        }
    }

    public Integer fetchCarrierByChannelId(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("fetchCarrierByChannelId"), id);
        }
    }

    public List<CarrierChannel> findAllChannel() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findAllChannel"));
        }
    }

    public String findChannelName(int channelId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findChannelName"), channelId);
        }
    }

    public CarrierChannel findChannelNamePro(int channelId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findChannelNamePro"), channelId);
        }
    }

    /**
     * @param regionCarrier
     */
    public void modifiRegionCarrier(RegionCarrier regionCarrier) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update(fullSqlId("modifiRegionCarrier"), regionCarrier);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update RegionCarrier failed: ", e);
            throw new RepositoryException(e);
        }
    }

    /**
     * @param enterpriseId
     * @return
     */
    public Integer findEnterpriseSpecBind(int specnumId, int enterpriseId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("specnumId", specnumId);
            params.put("enterpriseId", enterpriseId);
            return session.selectOne(fullSqlId("findEnterpriseSpecBind"), params);
        }
    }

    /**
     * @param esbId
     */
    public void delBiztypeSpecnumByBindId(Integer esbId) throws RepositoryException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("esbId", esbId);
            session.delete(fullSqlId("delBiztypeSpecnumByBindId"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete BiztypeSpecnumByBindId failed: ", e);
            throw new RepositoryException(e);
        }
    }

    public SpecsvsNum findParentSpecsvsNum(int channelId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findParentSpecsvsNum"), channelId);
        }
    }

}

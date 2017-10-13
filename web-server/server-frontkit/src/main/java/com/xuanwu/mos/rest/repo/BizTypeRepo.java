package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.BiztypeSpecnum;
import com.xuanwu.mos.domain.entity.CarrierChannel;
import com.xuanwu.mos.domain.entity.SpecsvsNumVo;
import com.xuanwu.mos.dto.QueryParameters;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1115 on 2017/3/24.
 */
@Repository
public class BizTypeRepo  extends GsmsMybatisEntityRepository<BizType> {
    private Logger logger = LoggerFactory.getLogger(BizTypeRepo.class.getName());
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.BizTypeMapper";
    }
    /**
     * 根据 enterpriseId,id,name 查找,数量
     */
    public int findBizTypeCount(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findBizTypeCount"), params);
        }
    }
    /**
     * 根据 enterpriseId,id,name 查找,结果集
     */
    public List<BizType> findBizType(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findBizType"), params);
        }
    }
    /**
     * 根据 userId 关联 gsms_user_business_type
     */
    public List<BizType> findUserBizType(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findUserBizType"), params);
        }
    }

    public BizType findByNameAndEntId(Map params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findBizTypeByNameAndEntId"), params);
        }
    }

    public void storeBizType(BizType bizType) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(fullSqlId("storeBizType"), bizType);
        }
    }

    public void storeBizTypeSpecnum(List<BiztypeSpecnum> bizTypeSpecList) {
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            for(BiztypeSpecnum specnum:bizTypeSpecList){
                session.insert(fullSqlId("storeBizTypeSpecnum"), specnum);
            }
            session.commit(true);
        }
    }

    public void storeUserBusinessType(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(fullSqlId("storeUserBusinessType"), params);
        }
    }

    public BizType findBizTypeDetailById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findBizTypeDetailById"), id);
        }
    }

    public BizType findBizTypeById(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findBizTypeById"), params);
        }
    }

    public void deleteBizType(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update(fullSqlId("deleteBizType"), id);
            session.delete(fullSqlId("delBizTypeSpecnum"), id);
            session.delete(fullSqlId("delUserBusinessType"), id);
            session.commit();
        }
    }

    public List<BizType> findBizTypesSimpleNotDel(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findBizTypesSimpleNotDel"),params);
        }
    }

    public List<BizType> findBizTypesByDeptId(int deptId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findBizTypesByDeptId"),deptId);
        }
    }

    public List<BizType> findBizTypeByEntId(int enterpriseId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findBizTypeByEntId"),enterpriseId);
        }
    }
    public List<BizType> findBizTypeByEntId4BizStat(int enterpriseId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findBizTypeByEntId4BizStat"),enterpriseId);
        }
    }
    public List<BizType> findBizTypesSimple(int entId, String name,
                                            int fetchSize) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("entId", entId);
            params.put("name", name);
            params.put("fetchSize", fetchSize);
            return session.selectList(fullSqlId("findBizTypesSimple"), params);
        }
    }

    public List<BizType> findAllBizType(){
        return null;
    }

    /**
     * @param enterpriseId
     * @return
     */
    public List<CarrierChannel> findCarrierChannelByEnterpriseId(
            int enterpriseId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            QueryParameters params = new QueryParameters();
            params.addParam("enterpriseId",enterpriseId);
            return session.selectList(fullSqlId("findCarrierChannelByEnterpriseId"), params);
        }
    }

    public List<CarrierChannel> findCarrierChannelByEnterpriseId(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findCarrierChannelByEnterpriseId"), params);
        }
    }

    public List<BizType> findChildTypeByPath(String path) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findChildTypeByPath"), path);
        }
    }

    public void modifyBizType(BizType update, String carrierChannel, String[] channelids) {

        try(SqlSession sessionBatch = sqlSessionFactory.openSession(ExecutorType.BATCH)){
            int bizTypeId = update.getId();
            sessionBatch.update("modifyBizType",update);
            sessionBatch.delete("delBizTypeSpecnum",bizTypeId);
            String[] biztypeSpecnums = carrierChannel.split(",");
            for (String biztypeSpecnum : biztypeSpecnums) {
                String[] bs = biztypeSpecnum.split(":");
                BiztypeSpecnum bizTypeSpec = new BiztypeSpecnum();
                bizTypeSpec.setBiztypeId(bizTypeId);
                bizTypeSpec.setEnterpriseSpecnumBindId(Integer.parseInt(bs[0]));
                bizTypeSpec.setCarrierId(Integer.parseInt(bs[1]));
                bizTypeSpec.setRemove(false);
                sessionBatch.update("storeBizTypeSpecnum",bizTypeSpec);
            }
            sessionBatch.commit();
        }
    }

    public CarrierChannel findExtentChannel(int id) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return (CarrierChannel) session.selectOne(fullSqlId("findExtentChannel"),id);
        }
    }
    public List<SpecsvsNumVo> getCarrierDetailByBizId(Integer biztypeId,Integer entId,Integer msgType){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("entId", entId);
            params.put("id", biztypeId);
            params.put("msgType", msgType);
            return session.selectList(fullSqlId("getCarrierDetailByBizId"),params);
        }
    }

    public BizType findCommonBusTypeByUserId(Integer userId) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findCommonBusTypeByUserId"),userId);
        }
    }
}

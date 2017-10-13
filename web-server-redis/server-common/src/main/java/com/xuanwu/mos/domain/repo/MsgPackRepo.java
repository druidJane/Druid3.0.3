package com.xuanwu.mos.domain.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MmsMsgPack;
import com.xuanwu.mos.domain.entity.MsgPack;
import com.xuanwu.mos.dto.QueryParameters;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
@Repository
public class MsgPackRepo extends GsmsMybatisEntityRepository<MsgPack> {
    private static final Logger logger = LoggerFactory.getLogger(MsgPackRepo.class);

    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.MsgPackMapper";
    }

    public MsgPack findMsgPackById(String id, int msgType, Date postTime) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("msgType", msgType);
            params.put("postTime", postTime);
            return session.selectOne(fullSqlId("findMsgPackById"), params);
        }
    }

    public String findBatchNameById(String id, int msgType, Date postTime) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("msgType", msgType);
            params.put("postTime", postTime);
            return session.selectOne(fullSqlId("findBatchNameById"), params);
        }
    }

    public List<String> findPackIdsByName(Date beginTime, Date endTime, String batchName,
                                          int msgType) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            params.put("batchName", batchName);
            params.put("msgType", msgType);
            return session.selectList(fullSqlId("findPackIdsByName"), params);
        }
    }


    public boolean deleteVerifyPack(String packId) {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("packId", packId);
            ret = session.delete(fullSqlId("deleteVerifyPack"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete MmsMsgPack failed: ", e);
        }
        if (ret == 0) {
            return false;
        } else {
            return true;
        }
    }

    //------------------add by guoyaohui-----------------------------------------

    public MmsMsgPack findVerifyMsgPackById(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findVerifyMsgPackById"), params);
        }
    }

    public boolean updateAuditPackState(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int resultRow = session.update(fullSqlId("updateAuditPackState"), params);
            session.commit(true);
            if (resultRow > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 获取彩信每天的pack -guo
     */
    public int findMmsMsgPacksCount(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findMmsMsgPacksCount"), params);
        }
    }

    /**
     * 获取每天彩信的pack  -guo
     */
    public List<MmsMsgPack> findMmsMsgPacks(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            List<MmsMsgPack> msgPackList = session.selectList(fullSqlId("findMmsMsgPacks"), params);
            return msgPackList;
        }
    }

    /**
     *
     * @param params
     * @return
     */
    public List<String> findPackIdsByName(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findMmsPackIdsByName"), params);
        }
    }

    /**
     * 获取审核彩信数量 -guo
     */
    public int findMmsWaitAuditPacksCount(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findMmsWaitAuditPacksCount"), params);
        }
    }

    /**
     * 获取审核彩信 -guo
     */
    public List<MmsMsgPack> findMmsWaitAuditPacks(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findMmsWaitAuditPacks"), params);
        }
    }

    /**
     * 获取前台审核的审核记录
     */
    public MmsMsgPack findFrontAuditRecord(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findFrontAuditRecord"), params);
        }
    }

    /**
     * 获取后台审核的审核记录
     */
    public MmsMsgPack findBackAuditRecord(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findBackAuditRecord"), params);
        }
    }
    /**
     * 获取批次状态报告成功数
     */
    public MmsMsgPack findSuccessCount(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findSuccessCount"), params);
        }
    }
    /**
     * 获取批次状态报告失败数
     */
    public MmsMsgPack findFailCount(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findFailCount"), params);
        }
    }

    /**
     * 获取信息包
     */
    public MmsMsgPack findMmsMsgPackById(Parameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findMmsMsgPackById"), params);
        }
    }

}

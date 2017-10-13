package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.MsgFrame;
import com.xuanwu.mos.domain.entity.PackStatInfo;

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
public class MsgFrameRepo extends GsmsMybatisEntityRepository<MsgFrame> {
    private static final Logger logger = LoggerFactory.getLogger(MsgFrameRepo.class);
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.MsgFrameMapper";
    }

    public List<PackStatInfo> findPackStatInfos(String packId, int msgType, Date postTime) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("packId", packId);
            params.put("msgType", msgType);
            params.put("postTime", postTime);
            return session.selectList(fullSqlId("findPackStatInfos"), params);
        }
    }

    public MsgFrame findMsgFrameById(long id, int msgType,Date postTime) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("msgType", msgType);
            params.put("postTime", postTime);
            return session.selectOne(fullSqlId("findMsgFrameById"), params);
        }
    }

    public MsgFrame findSingleMsgFrameByPackId(Parameters params){
        try (SqlSession session = sqlSessionFactory.openSession()){
            return session.selectOne(fullSqlId("findSingleMsgFrameByPackId"),params);
        }
    }

    /**
     * 核检详情
     * @param params
     * @return
     */
    public List<MsgFrame> checkRecordDetail(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("checkRecordDetail"),params);
        }
    }

    public int findMsgCountByPackId(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findMsgCountByPackId"),params);
        }
    }
    public List<MsgFrame> findNotHandleFrame(Parameters params) {
        try(SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findNotHandleFrame"),params);
        }
    }
}

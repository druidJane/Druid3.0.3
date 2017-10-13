package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.vo.MoReplyVo;
import com.xuanwu.mos.vo.MoTicketVo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:jiangziyuan@wxchina.com">Ziyuan.Jiang</a>
 * @version 1.0.0
 * @date 2017/5/24.
 */
@Repository
public class MoTicketRepo extends GsmsMybatisEntityRepository<MoTicketVo> {
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.MoTicketMapper";
    }

    public int findMoReplyCountByMoTicketId(QueryParameters req){
        try(SqlSession sqlsession = sqlSessionFactory.openSession()){
            return sqlsession.selectOne(fullSqlId("findMoReplyCountByMoTicketId"), req);
        }
    }

    public List<MoReplyVo> findMoReplyByMoTicketId(QueryParameters req){
        try(SqlSession sqlsession = sqlSessionFactory.openSession()){
            return sqlsession.selectList(fullSqlId("findMoReplyByMoTicketId"), req);
        }
    }

    public int addMoReply(MoReplyVo moReplyVo) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("moTicketId", moReplyVo.getMoTicketId());
            params.put("replyUserId", moReplyVo.getReplyUserId());
            params.put("replyTime", moReplyVo.getReplyTime());
            params.put("replyContent", moReplyVo.getReplyContent());
            params.put("bizType", moReplyVo.getBizType());
            params.put("msgType", moReplyVo.getMsgType());
            params.put("batchName", moReplyVo.getBatchName());
            params.put("departmentId", moReplyVo.getDepartmentId());
            params.put("packId", moReplyVo.getPackId());
            params.put("replyType", moReplyVo.getReplyType());
            int ret = session.insert(fullSqlId("addMoReply"), params);
            session.commit(true);
            return ret;
        }
    }
}

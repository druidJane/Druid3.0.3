package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.Phrase;
import com.xuanwu.mos.dto.QueryParameters;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 郭垚辉 on 2017/4/5.
 */

@Repository
public class PhraseRepo extends GsmsMybatisEntityRepository<Phrase> {

    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.PhraseMapper";
    }

    public int findPhraseListCount(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(fullSqlId("findPhraseListCount"), params);
        }
    }

    public List<Phrase> findPhraseList(QueryParameters params) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(fullSqlId("findPhraseList"), params);
        }
    }

    public int addPhrase(Phrase phrase) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("content", phrase.getContent());
            params.put("userId", phrase.getUserId());
            params.put("title", phrase.getTitle());
            params.put("identify", phrase.getIdentify());
            params.put("msgType", phrase.getMsgType());
            params.put("lastUpdateTime", phrase.getLastUpdateTime());
            int addPhrase = session.insert(fullSqlId("addPhrase"), params);
            session.commit();
            return addPhrase;
        }
    }

    public int addSMSPhrase(Phrase phrase) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("content", phrase.getContent());
            params.put("userId", phrase.getUserId());
            params.put("title", phrase.getTitle());
            params.put("msgType", phrase.getMsgType());
            params.put("lastUpdateTime", phrase.getLastUpdateTime());

            params.put("identify", phrase.getIdentify());
            params.put("createTime", phrase.getCreateTime());
            params.put("auditstate", phrase.getAuditState());
            params.put("enterpriseId", phrase.getEnterpriseId());
            params.put("templatetype", phrase.getTemplateType());
            params.put("phraseType", phrase.getPhraseType());
            int ret = session.insert(fullSqlId("addSMSPhrase"), params);
            session.commit(true);
            return ret;
        }
    }

    public Phrase findPhraseById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("id", id);
            Phrase phrase = session.selectOne(fullSqlId("findPhraseById"), params);
            return phrase;
        }
    }

    public int updatePhrase(Phrase phrase) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("id", phrase.getId());
            params.put("title", phrase.getTitle());
            params.put("content", phrase.getContent());
            params.put("userId", phrase.getUserId());
            params.put("msgType", phrase.getMsgType());
            params.put("lastUpdateTime", phrase.getLastUpdateTime());
            int updateRows = session.update(fullSqlId("updatePhrase"), params);
            session.commit();
            return updateRows;
        }
    }

    public int updateSMSPhrase(Phrase phrase) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("id", phrase.getId());
            params.put("content", phrase.getContent());
            params.put("title", phrase.getTitle());
            params.put("userId", phrase.getUserId());
            params.put("lastUpdateTime", phrase.getLastUpdateTime());
            params.put("templatetype", phrase.getTemplateType());
            params.put("auditState", phrase.getAuditState());
            int updateRows = session.update(fullSqlId("updateSMSPhrase"), params);
            session.commit(true);
            return updateRows;
        }
    }

    public int deletePhraseByIds(List<Integer> deleteList) {
        int deleteRows = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("ids", deleteList);
            deleteRows = session.delete(fullSqlId("deletePhraseByIds"), params);
            session.commit();
        }
        return deleteRows;
    }

    public Phrase findPhraseByTitle(String title, int msgType, Integer userId, int offset, int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("title", title);
            params.put("msgType", msgType);
            params.put("userId", userId);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            return session.selectOne(fullSqlId("findPhraseByTitle"), params);
        }
    }

    public int findPhraseCount(int userId, String title, String identify, String content, int msgType, int type, int auditingState) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("title", title);
            params.put("identify", identify);
            params.put("content", content);
            params.put("msgType", msgType);
            params.put("type", type);
            params.put("auditingState", auditingState);
            int count = session.selectOne(fullSqlId("findPhraseCount"), params);
            return count;
        }
    }

    public List<Phrase> findPhrase(int userId, String title, String identify, String content, int msgType, int type, int auditingState, int offset, int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("title", title);
            params.put("identify", identify);
            params.put("content", content);
            params.put("msgType", msgType);
            params.put("type", type);
            params.put("auditingState", auditingState);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            return session.selectList(fullSqlId("findPhrase"), params);
        }
    }

    public int deletePhraseById(int[] ids) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("ids", ids);
            int deleteRows = session.delete(fullSqlId("deletePhraseById"), params);
            return deleteRows;
        }
    }

    public Phrase findMosPhraseById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("id", id);
            return session.selectOne(fullSqlId("findMosPhraseById"), params);
        }
    }

    public void updatePhraseByBizTypeAndMsgType(int bizTypeId, int msgType) {
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("bizTypeId", bizTypeId);
            params.put("msgType", msgType);
            session.update(fullSqlId("updatePhraseByBizTypeAndMsgType"), params);
        }
    }

    public Phrase findPhraseByNO(String identify) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("identify", identify);
            return session.selectOne(fullSqlId("findPhraseByNO"), params);
        }
    }

    public List<Phrase> findPhraseByAuditingState(int auditingState, int msgType) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("auditingState", auditingState);
            params.put("msgType", msgType);
            return session.selectList(fullSqlId("findPhraseByAuditingState"), params);
        }
    }

    /**
     * @param phrase
     * @return
     * by jiangziyuan
     */

    public int addMosPhrase(Phrase phrase) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("content", phrase.getContent());
            params.put("userId", phrase.getUserId());
            params.put("title", phrase.getTitle());
            params.put("msgType", phrase.getMsgType());
            params.put("lastUpdateTime", phrase.getLastUpdateTime());
            params.put("identify", phrase.getIdentify());
            params.put("auditingState", phrase.getAuditState());
            params.put("enterpriseId", phrase.getEnterpriseId());
            params.put("templateType", phrase.getTemplateType());
            return session.insert(fullSqlId("addMosPhrase"), params);
        }
    }

    /**
     * 【发送短信】时【引用短语】加载gsms_phrase表的内容  by jiangziyuan
     * */

    public int findPhraseByTemplatetypeCount(int userId, String title, String identify, String content, int msgType, int type, int auditingState,int templatetype){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("title", title);
            params.put("identify", identify);
            params.put("content", content);
            params.put("msgType", msgType);
            params.put("type", type);
            params.put("auditingState", auditingState);
            params.put("templatetype", templatetype);
            int findPhraseByTemplatetypeCount = session.selectOne(fullSqlId("findPhraseByTemplatetypeCount"), params);
            return findPhraseByTemplatetypeCount;
        }
    }

    public List<Phrase> findPhraseByTemplatetype(int userId, String title, String identify, String content, int msgType, int type, int auditingState,int offset, int reqNum, int templatetype) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("title", title);
            params.put("identify", identify);
            params.put("content", content);
            params.put("msgType", msgType);
            params.put("type", type);
            params.put("auditingState", auditingState);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            params.put("templatetype", templatetype);
            return session.selectList(fullSqlId("findPhraseByTemplatetype"), params);
        }
    }

}

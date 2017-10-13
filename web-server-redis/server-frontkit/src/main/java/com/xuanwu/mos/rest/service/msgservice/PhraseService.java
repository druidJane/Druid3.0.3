package com.xuanwu.mos.rest.service.msgservice;

import com.xuanwu.mos.domain.entity.Phrase;
import com.xuanwu.mos.dto.QueryParameters;

import java.util.List;

/**
 * 模板服务
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public interface PhraseService {

    /**
     * @param id
     * @return
     */
    public Phrase findPhraseById(int id);

    /**
     * @return by jiangziyuan
     */
    public Phrase findMosPhraseById(int id);

    /**

     */
    public boolean updatePhrase(Phrase phrase);

    /**
     * @param phrase
     * @return
     */
    public boolean addPhrase(Phrase phrase);

    /**
     * @param phrase
     * @return
     */
    public boolean addSMSPhrase(Phrase phrase);

    public int findPhraseListCount(QueryParameters params);

    public List<Phrase> findPhraseList(QueryParameters params);

    public int deletePhraseByIds(List<Integer> deleteList);

    public int findPhraseCount(QueryParameters params);

    public List<Phrase> list(QueryParameters params);

    public boolean updateSMSPhrase(Phrase phrase);

    void updatePhraseByBizTypeAndMsgType(Integer bizTypeId, Integer msgType);
}

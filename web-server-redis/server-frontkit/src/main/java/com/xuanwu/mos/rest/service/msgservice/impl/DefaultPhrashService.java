package com.xuanwu.mos.rest.service.msgservice.impl;

import com.xuanwu.mos.domain.entity.Phrase;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.rest.repo.PhraseRepo;
import com.xuanwu.mos.rest.service.msgservice.PhraseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认的模板服务
 * Created by Jiang.Ziyuan on 2017/4/10.
 */
@Service("phraseService")
public class DefaultPhrashService implements PhraseService {
    @Autowired
    private PhraseRepo phraseDao;

    public int findPhraseCount(QueryParameters params) {
        return phraseDao.findResultCount(params);
    }

    @Override
    public List<Phrase> list(QueryParameters params) {
        List<Phrase> phraseList = phraseDao.findResults(params);
        return phraseList;
    }

    @Override
    public Phrase findPhraseById(int id) {
        return phraseDao.findPhraseById(id);
    }

    //by jiangziyuan
    @Override
    public Phrase findMosPhraseById(int id) {
        return phraseDao.findMosPhraseById(id);
    }

    @Override
    public boolean updatePhrase(Phrase phrase) {
        try {
            phraseDao.updatePhrase(phrase);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateSMSPhrase(Phrase phrase) {
        try {
            phraseDao.updateSMSPhrase(phrase);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean addPhrase(Phrase phrase) {
        try {
            phraseDao.addPhrase(phrase);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean addSMSPhrase(Phrase phrase) {
        try {
            phraseDao.addSMSPhrase(phrase);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int findPhraseListCount(QueryParameters params) {
        return phraseDao.findPhraseListCount(params);
    }

    @Override
    public List<Phrase> findPhraseList(QueryParameters params) {
        return phraseDao.findPhraseList(params);
    }

    @Override
    public int deletePhraseByIds(List<Integer> deleteList) {
        return phraseDao.deletePhraseByIds(deleteList);
    }

    public void updatePhraseByBizTypeAndMsgType(Integer bizTypeId, Integer msgType) {
        phraseDao.updatePhraseByBizTypeAndMsgType(bizTypeId, msgType);
    }
}

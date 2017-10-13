package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.KeyWord;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.KeyWordRepo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangz on 2017/4/11.
 */
@Service
public class KeyWordService {
    @Autowired
    private KeyWordRepo keyWordRepo;
    public int findKeywordCount(QueryParameters params) {
        return keyWordRepo.findResultCount(params);
    }
    public List<KeyWord> findKeywords(QueryParameters params) {
        //TODO 升降序排序未实现
        List<KeyWord> keywordList=keyWordRepo.findResults(params);
        return keywordList;
    }
    public KeyWord isExists(KeyWord keyWord) {
        QueryParameters params = new QueryParameters();
        if(StringUtils.isNoneEmpty(keyWord.getKeywordName())){
            params.addParam("keywordName",keyWord.getKeywordName());
            params.addParam("targetId",keyWord.getTargetId());
        }
        List<KeyWord> list = this.findKeywords(params);
        if(list !=null && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public boolean addOrUpdateKeyword(KeyWord keyWord, Integer id) throws RepositoryException {
        Date currentDate = new Date();
        boolean result = false;
        if(id==null){
            KeyWord add = new KeyWord();
            add.setTargetId(keyWord.getTargetId());
            add.setUserId(keyWord.getUserId());
            add.setKeywordName(keyWord.getKeywordName());
            add.setHandleTime(currentDate);
            add.setType(1);//按照UMP代码，继续hardcode
            keyWordRepo.insert(add);
            result = true;
        }else{
            KeyWord update = keyWordRepo.getById(id,null);
            if(update==null){
                return false;
            }
            update.setKeywordName(keyWord.getKeywordName());
            update.setHandleTime(currentDate);
            keyWordRepo.update(update);
            result = true;
        }
        return result;
    }

    public void delKeyword(Integer[] ids) throws RepositoryException {
        for(Integer id :ids){
            KeyWord del = keyWordRepo.getById(id, null);
            if(del!=null){
                del.setRemoved(true);
                keyWordRepo.update(del);
            }
        }
    }
}

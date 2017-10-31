package com.xuanwu.mos.service;

import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.BizType;
import com.xuanwu.mos.domain.entity.CapitalAccount;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.DataScope;
import com.xuanwu.mos.domain.repo.UserRepo;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangz on 2017/3/30.
 */
@Service
public class AutoCompleteService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BizTypeService bizTypeService;
    @Autowired
    private CapitalAccountService capitalAccountService;
    @Autowired
    private BizDataService bizDataService;

    public List<Map<String, String>> autoCompleteDomain(String enterprise) {
        return userRepo.autoCompleteDomain(enterprise);
    }
    public List<Map<String, String>> autoCompleteUserName(String userName, Integer enterpriseId) {
        if(enterpriseId==null){
            throw new BusinessException("enterpriseId cannot be null!");
        }
        QueryParameters parmas = new QueryParameters();
        parmas.addParam("userName",userName);
        parmas.addParam("enterpriseId",enterpriseId);
        parmas.addParam("platformId", Platform.FRONTKIT.getIndex());
        List<SimpleUser> userList = userRepo.autoCompleteUserName(parmas);
        List<Map<String ,String>> result = new ArrayList<>();
        for(SimpleUser user : userList){
            Map<String,String> item = new HashMap<>();
            item.put("id",String.valueOf(user.getId()));
            item.put("name",user.getUsername());
            result.add(item);
        }
        return result;
    }

    public List<Map<String, String>> acomplpleteBizType(SimpleUser user, String bizType, DataScope none, int fetchSize) {
        List<Map<String, String>> result = new ArrayList<>();
        List<BizType> ret = new ArrayList<>();
        switch (none){
            case NONE:
                ret = bizTypeService.findBizTypesSimpleNotDel(
                        user.getEnterpriseId(), bizType, fetchSize);
                break;
            case PERSONAL:
                QueryParameters params = new QueryParameters();
                params.addParam("userId",user.getId());
                ret = bizTypeService.findUserBizType(params);
        }

        for(BizType biz:ret){
            Map<String,String> item = new HashMap<>();
            item.put("id",biz.getId().toString());
            item.put("name",biz.getName());
            item.put("startTime",biz.getStartTime());
            item.put("endTime",biz.getEndTime());
            result.add(item);
        }
        return result;
    }
    public List<Map<String, String>> acomplpleteAccount(SimpleUser user, String accountName, DataScope none){
    	List<Map<String, String>> result = new ArrayList<>();
        List<CapitalAccount> ret = capitalAccountService.findCapitalAccountsSimpleNotDel(
                user.getEnterpriseId(), accountName);
        
        if("企业统一账户".indexOf(accountName)>=0){ 
            List<CapitalAccount> capitalAccountList = capitalAccountService.findCapitalAccountsSimpleNotDel(
                    user.getEnterpriseId(), "");
            for(CapitalAccount account:capitalAccountList){
            	if((Integer)account.getParentId()==null || account.getParentId()<=0){
            		Map<String,String> item = new HashMap<>();
            		item.put("id",account.getId().toString());
            		item.put("name","企业统一账户");
            		result.add(item);
            	}
            }
        	//CapitalAccount rootCapitalAccoun = capitalAccountService.
        }
        for(CapitalAccount account:ret){
        	if((Integer)account.getParentId()==null || account.getParentId()<=0){
        		continue;
        	}
            Map<String,String> item = new HashMap<>();
            item.put("id",account.getId().toString());
            item.put("name",account.getAccountName());
            result.add(item);
        }
        return result;
    }
}

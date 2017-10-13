package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.rest.repo.BizTypeRepo;
import com.xuanwu.mos.rest.repo.CarrierChannelRepo;
import com.xuanwu.mos.rest.service.msgservice.PhraseService;
import com.xuanwu.mos.utils.ListUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.xuanwu.mos.utils.Delimiters.COLON;
import static com.xuanwu.mos.utils.Delimiters.COMMA;

/**
 * Created by zhangz on 2017/3/24.
 */
@Service
public class BizTypeService {
    @Autowired
    private BizTypeRepo bizTypeRepo;
    @Autowired
    private CarrierChannelRepo carrierChannelRepo;
    @Autowired
    private PhraseService phraseService;

    public int findBizTypeCount(QueryParameters params) {
        return bizTypeRepo.findBizTypeCount(params);
    }
    public List<BizType> findBizType(QueryParameters params) {
        return bizTypeRepo.findBizType(params);
    }

    /**
     * 根据企业ID，业务类型名称查询
     * @return
     */
    public BizType findByNameAndEntId(String name,Integer entId) {
        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("name", name);
        params.put("entId", entId);
        return bizTypeRepo.findByNameAndEntId(params);
    }

    /**
     * 保存业务类型及关联的号码段
     * @param bizType
     * @param user
     * @param channelCarrier
     *@param  @return
     */
    @Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
    public void storeBizType(BizType bizType, SimpleUser user, String channelCarrier) {
        bizTypeRepo.storeBizType(bizType);
        int bizTypeId = bizType.getId();
        List<BiztypeSpecnum> bizTypeSpecList = new ArrayList<BiztypeSpecnum>();
        String[] biztypeSpecnums = channelCarrier.split(COMMA);
        for (String biztypeSpecnum : biztypeSpecnums) {
            BiztypeSpecnum bizTypeSpec = new BiztypeSpecnum();
            String[] bs = biztypeSpecnum.split(COLON);
            bizTypeSpec.setBiztypeId(bizTypeId);
            bizTypeSpec.setEnterpriseSpecnumBindId(Integer.parseInt(bs[0]));
            bizTypeSpec.setCarrierId(Integer.parseInt(bs[1]));
            bizTypeSpec.setRemove(false);
            bizTypeSpecList.add(bizTypeSpec);
        }
        bizTypeRepo.storeBizTypeSpecnum(bizTypeSpecList);
        QueryParameters params = new QueryParameters();
        params.addParam("bizTypeId",bizTypeId);
        params.addParam("userId",user.getId());
        params.addParam("busType",0);
        bizTypeRepo.storeUserBusinessType(params);
    }

    public BizType findBizTypeById(int id) {
        return bizTypeRepo.getById(id,null);
    }
    @Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
    public void deleteBizType(int id) throws RepositoryException {
        bizTypeRepo.deleteBizType(id);
    }

    public List<BizType> findBizTypesSimpleNotDel(int entId, String name, int fetchSize) {
        if(fetchSize <= 0){
            return Collections.emptyList();
        }
        QueryParameters params = new QueryParameters();
        if(!StringUtils.isBlank(name)){
            params.addParam("name",name);
        }
        params.addParam("entId",entId);
        params.addParam("fetchSize",fetchSize);
        return bizTypeRepo.findBizTypesSimpleNotDel(params);
    }

    public List<BizType> findBizTypesByDeptId(int deptId) {
        return bizTypeRepo.findBizTypesByDeptId(deptId);
    }

    public List<BizType> findBizTypeByEntId(int enterpriseId) {
        return bizTypeRepo.findBizTypeByEntId(enterpriseId);
    }
    public List<BizType> findBizTypeByEntId4BizStat(int enterpriseId) {
        return bizTypeRepo.findBizTypeByEntId4BizStat(enterpriseId);
    }
    public List<BizType> findBizTypesSimple(int entId, String name, int fetchSize) {
        if(org.apache.commons.lang.StringUtils.isBlank(name)){
            name = null;
        }
        if(fetchSize <= 0){
            return Collections.emptyList();
        }
        return bizTypeRepo.findBizTypesSimple(entId, name, fetchSize);
    }
    /**
     * @param enterpriseId
     * @return
     */
    public List<CarrierChannel> findCarrierChannelByEnterpriseId(
            int enterpriseId) {
        return bizTypeRepo.findCarrierChannelByEnterpriseId(enterpriseId);
    }

    public List<CarrierChannel> findCarrierChannelByEnterpriseId(int enterpriseId,int msgType) {
        QueryParameters params = new QueryParameters();
        params.addParam("msgType",msgType);
        params.addParam("enterpriseId",enterpriseId);
        return bizTypeRepo.findCarrierChannelByEnterpriseId(params);
    }

    public BizType findBizTypeDetailById(Integer id) {
        return bizTypeRepo.findBizTypeDetailById(id);
    }
    @Transactional(transactionManager = "gsmsTransactionManager", rollbackFor = Exception.class)
    public void modifyBizType(BizType update, String carrierChannel, String[] channelids) {
        bizTypeRepo.modifyBizType(update,carrierChannel,channelids);
        List<CarrierChannel> carrierChannels = new ArrayList<CarrierChannel>();
        for (String carrierId : channelids) {
            carrierChannels.add(carrierChannelRepo.findChannelById(Integer.parseInt(carrierId)));
        }
        boolean sms = false;
        boolean mms = false;
        if (ListUtil.isNotBlank(carrierChannels)) {
            for (CarrierChannel cc : carrierChannels) {
                if (cc.isSms() || cc.isLongSms()) {
                    sms = true;
                }
                if (cc.isMms()) {
                    mms = true;
                }
            }
        }
        if (!sms) {
            phraseService.updatePhraseByBizTypeAndMsgType(update.getId(),MsgContent.MsgType.SMS.getIndex());
        }
        if (!mms) {
            phraseService.updatePhraseByBizTypeAndMsgType(update.getId(),MsgContent.MsgType.MMS.getIndex());
        }
    }

    public List<BizType> findChildTypeByPath(String path) {
        return bizTypeRepo.findChildTypeByPath(path);
    }

    public CarrierChannel findExtentChannel(int id) {
        return bizTypeRepo.findExtentChannel(id);
    }

    public List<SpecsvsNumVo> getCarrierDetailByBizId(Integer entId,Integer biztypeId,Integer msgType){
        if(entId == null){
            throw new BusinessException("entId cannot be null!");
        }
        return bizTypeRepo.getCarrierDetailByBizId(biztypeId,entId,msgType);
    }

    public BizType findCommonBusTypeByUserId(Integer userId) {
        if (userId == null) {
            throw new BusinessException("userId cannot be null!");
        }
        return bizTypeRepo.findCommonBusTypeByUserId(userId);
    }
    public List<BizType> findUserBizType(QueryParameters params){
        if (params.getParams().get("userId") == null) {
            throw new BusinessException("userId cannot be null!");
        }
        return bizTypeRepo.findUserBizType(params);
    }
}

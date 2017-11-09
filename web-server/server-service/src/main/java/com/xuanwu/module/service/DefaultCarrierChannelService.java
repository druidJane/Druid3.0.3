package com.xuanwu.module.service;

import com.xuanwu.mos.domain.enums.WhiteRedirectEnum;
import com.xuanwu.mos.rest.repo.CarrierChannelRepo;
import com.xuanwu.mos.rest.repo.CarrierRepo;
import com.xuanwu.mos.rest.repo.MsgTicketRepo;
import com.xuanwu.mos.utils.ListUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Component
public class DefaultCarrierChannelService implements CarrierChannelService{

    public CarrierChannelRepo carrierChannelDao;
    public CarrierRepo carrierDao;
    public MsgTicketRepo msgTicketDao;

    @Autowired
    public void setCarrierDao(CarrierRepo carrierDao) {
        this.carrierDao = carrierDao;
    }

    @Autowired
    public void setMsgTicketDao(MsgTicketRepo msgTicketDao){
        this.msgTicketDao = msgTicketDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findCarrierChannelCount
     * (java.lang.String, java.lang.String, int, int)
     */
    @Override
    public int findCarrierChannelCount(String name, String channelNum,
                                       int carrier, int msgType, int enterpriseId) {
        return carrierChannelDao.findCarrierChannelCount(name, channelNum,
                carrier, msgType, enterpriseId);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findCarrierChannel
     * (java.lang.String, java.lang.String, int, int, int, int)
     */
    @Override
    public List<CarrierChannel> findCarrierChannel(String name,
                                                   String channelNum, int carrier, int msgType, int enterpriseId,
                                                   int offset, int rp) {
        return carrierChannelDao.findCarrierChannel(name, channelNum, carrier,
                msgType, enterpriseId, offset, rp);
    }

    @Override
    public List<CarrierChannel> findSimpleChannelByEnterprise(int enterpriseID, int isWhitelist) {
        return carrierChannelDao.findSimpleChannelByEnterprise(enterpriseID, isWhitelist);
    }

    @Override
    public List<CarrierChannel> findSimpleChannelByNum(String channelNum,
                                                       int msgType, int state, int isRemove) {
        return carrierChannelDao.findSimpleChannelByNum(channelNum, msgType,
                state, isRemove);
    }

    @Override
    public List<CarrierChannel> findAllWhiteChannels() {
        List<CarrierChannel> channelList = carrierChannelDao
                .findAllWhiteChannels();
        // 可发送运营商-----start
        for (CarrierChannel channel : channelList) {
            List<Carrier> carrierList = carrierDao
                    .findCarriersByChannelId(channel.getId());
            StringBuffer carrierName = new StringBuffer();
            for (int i = 0; i < carrierList.size(); i++) {
                if (i != carrierList.size() - 1) {
                    carrierName.append(carrierList.get(i).getName())
                            .append(",");
                } else {
                    carrierName.append(carrierList.get(i).getName());
                }
            }
            channel.setCanSendCarriers(carrierName.toString());
        }// ------end
        return channelList;
    }

    @Override
    public List<CarrierChannel> findAllNonWhiteChannels() {
        List<CarrierChannel> channelList = carrierChannelDao
                .findAllNonWhiteChannels();
        // 可发送运营商-----start
        for (CarrierChannel channel : channelList) {
            List<Carrier> carrierList = carrierDao
                    .findCarriersByChannelId(channel.getId());
            StringBuffer carrierName = new StringBuffer();
            for (int i = 0; i < carrierList.size(); i++) {
                if (i != carrierList.size() - 1) {
                    carrierName.append(carrierList.get(i).getName())
                            .append(",");
                } else {
                    carrierName.append(carrierList.get(i).getName());
                }
            }
            channel.setCanSendCarriers(carrierName.toString());
        }
        // ------end
        return channelList;
    }

    @Autowired
    public void setCarrierChannelDao(CarrierChannelRepo carrierChannelDao) {
        this.carrierChannelDao = carrierChannelDao;
    }

    @Override
    public CarrierChannel findChannelByID(int id) {
        return carrierChannelDao.findChannelById(id);
    }

    @Override
    public List<Long> findChannelByParam(String name, String channelNum,
                                         int enterpriseID) {
        List<Long> cacheDesIDs =carrierChannelDao.findChannelByParam(name, channelNum,
                enterpriseID);
        if(cacheDesIDs.size() < 1){
            cacheDesIDs.add(Long.valueOf(0));
        }
        return cacheDesIDs;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findSysConfigMixMaxLength
     * ()
     */
    @Override
    public int findSysConfigMixMaxLength() {
        return carrierChannelDao.findSysConfigMixMaxLength();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findCarrierChannelByName
     * (java.lang.String)
     */
    @Override
    public CarrierChannel findCarrierChannelByName(String name) {
        return carrierChannelDao.findCarrierChannelByName(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.xuanwu.web.common.service.CarrierChannelService#
     * findCarrierChannelByIdentity(java.lang.String)
     */
    @Override
    public CarrierChannel findCarrierChannelByIdentity(String identity) {
        return carrierChannelDao.findCarrierChannelByIdentity(identity);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#storeCarrierChannel
     * (com.xuanwu.web.common.entity.CarrierChannel)
     */
    @Override
    @Transactional
    public boolean storeCarrierChannel(RegionCarrier regionCarrier,
                                       CarrierChannel carrierChannel,
                                       List<ChannelCarrier> channelCarriers,
                                       List<ChannelRegion> channelRegions, SpecsvsNum specsvsNum,
                                       boolean isBindBusinessType,
                                       EnterpriseSpecnumBind enterpriseSpecnumBind,
                                       List<BiztypeSpecnum> biztypeSpecnums) throws Exception {
        carrierChannelDao.storeRegionCarrier(regionCarrier);
        carrierChannel.setRegionCarrierId(regionCarrier.getId());
        carrierChannelDao.storeCarrierChannel(carrierChannel);
        for(int i=0;i<channelCarriers.size();i++){
            channelCarriers.get(i).setChannelId(carrierChannel.getId());
            carrierChannelDao.storeChannelCarrier(channelCarriers.get(i));
        }
        for(int i=0;i<channelRegions.size();i++){
            channelRegions.get(i).setChannelId(carrierChannel.getId());
            carrierChannelDao.storeChannelRegion(channelRegions.get(i));
        }
        specsvsNum.setChannelId(carrierChannel.getId());
        carrierChannelDao.storeSpecsvsNum(specsvsNum);
        SpecsvsNum extendNum = SpecsvsNum.cloneSpecsvsNum(specsvsNum);
        extendNum.setId(0);
        extendNum.setParentNumId(specsvsNum.getId());
        extendNum.setType(SpecsvsNum.Type.EXTEND.getIndex());
        extendNum.setAssignType(SpecsvsNum.AssignType.FIXED.getIndex());
        carrierChannelDao.storeSpecsvsNum(extendNum);
        enterpriseSpecnumBind.setSpecnumId(extendNum.getId());
        carrierChannelDao.storeEnterpriseSpecnumBind(enterpriseSpecnumBind);
        if (isBindBusinessType) {
            for (BiztypeSpecnum bs : biztypeSpecnums) {
                bs.setEnterpriseSpecnumBindId(enterpriseSpecnumBind.getId());
                carrierChannelDao.storeBiztypeSpecnum(bs);
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findCarrierChannelById
     * (int)
     */
    @Override
    public CarrierChannel findCarrierChannelById(int id) {
        return carrierChannelDao.findCarrierChannelById(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findAllBizTypeCarrier
     * (java.lang.String)
     */
    @Override
    public List<BiztypeSpecnum> findAllBizTypeCarrier(int id) {
        return carrierChannelDao.findAllBizTypeCarrier(id);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#modifyCarrierChannel
     * (com.xuanwu.web.common.entity.CarrierChannel)
     */
    @Override
    @Transactional
    public boolean modifyCarrierChannel(RegionCarrier regionCarrier,CarrierChannel carrierChannel,
                                        SpecsvsNum specsvsNum, boolean isChangeCarrier,
                                        List<ChannelCarrier> channelCarriers, boolean isChangeRegion,
                                        List<ChannelRegion> channelRegions,SimpleUser user) throws Exception {
        if (regionCarrier != null) {
            carrierChannelDao.modifiRegionCarrier(regionCarrier);
        }
        carrierChannelDao.modifyCarrierChannel(carrierChannel);
        carrierChannelDao.modifySpecsvsNum(specsvsNum);
        if (isChangeCarrier) {
            carrierChannelDao.delOldChannelCarrier(carrierChannel.getId());
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (ChannelCarrier cc : channelCarriers) {
                carrierChannelDao.storeChannelCarrier(cc);
                sb.append(cc.getCarrierId());
            }
            sb.append("]");
            String regex = sb.toString();
            List<BiztypeSpecnum> bizTypeCarriers = carrierChannelDao
                    .findAllBizTypeCarrier(carrierChannel.getId());
            List<BiztypeSpecnum> outofTypeBizTypeCarriers = new ArrayList<BiztypeSpecnum>();
            for (BiztypeSpecnum bs : bizTypeCarriers) {
                if (!String.valueOf(bs.getCarrierId()).matches(regex))
                    outofTypeBizTypeCarriers.add(bs);
            }
            if (ListUtil.isNotBlank(outofTypeBizTypeCarriers)) {
                for (BiztypeSpecnum btc : outofTypeBizTypeCarriers) {
                    carrierChannelDao.delBizTypeCarrier(btc.getId());
                }
            }
        }
        if (isChangeRegion) {
            carrierChannelDao.delOldChannelRegion(carrierChannel.getId());
            if (ListUtil.isNotBlank(channelRegions)) {
                for (ChannelRegion cr : channelRegions) {
                    carrierChannelDao.storeChannelRegion(cr);
                }
            }
        }
        if (carrierChannel.getState() == 0) {
            List<SpecsvsNum> specsvsNums = carrierChannelDao
                    .findSpecsvsNumByChannelId(carrierChannel.getId());
            if (ListUtil.isNotBlank(specsvsNums)) {
                List<Integer> esbIds = new ArrayList<Integer>();
                for (SpecsvsNum sn : specsvsNums) {
                    Integer esbId = carrierChannelDao.findEnterpriseSpecBind(
                            sn.getId(), user.getEnterpriseId());
                    carrierChannelDao.delSpecnumRegionPriority(sn.getId());
                    if (esbId != null) {
                        esbIds.add(esbId);
                    }
                }
                if (ListUtil.isNotBlank(esbIds)) {
                    for (Integer esbId : esbIds) {
                        carrierChannelDao.delBiztypeSpecnumByBindId(esbId);
                        carrierChannelDao.delWhileRedirect(esbId,
                                WhiteRedirectEnum.EnterpriseSpecNum.getIndex());
                    }
                }
            }
            carrierChannelDao.delWhileRedirect(carrierChannel.getId(),
                    WhiteRedirectEnum.ChannelId.getIndex());
            carrierChannelDao.delChannelChange(carrierChannel.getId());
            carrierChannelDao.delChannelChangeDetail(carrierChannel.getId());
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findIsChannelSended
     * (int)
     */
    @Override
    public int findIsChannelSended(int id) {
        return carrierChannelDao.findIsChannelSended(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findSpecsvsNumByChannelId
     * (int)
     */
    @Override
    public List<SpecsvsNum> findSpecsvsNumByChannelId(int id) {
        return carrierChannelDao.findSpecsvsNumByChannelId(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findSecondSpecsvsNumByChannelId
     * (int)
     */
    @Override
    public SpecsvsNum findSecondSpecsvsNumByChannelId(int id) {
        return carrierChannelDao.findSecondSpecsvsNumByChannelId(id);
    }
    /*
     * (non-Javadoc)
     *
     * @see com.xuanwu.web.common.service.CarrierChannelService#
     * findEnterpriseSpecnumBindBySpecNumId(java.util.List)
     */
    @Override
    public List<EnterpriseSpecnumBind> findEnterpriseSpecnumBindBySpecNumId(
            List<SpecsvsNum> specsvsNums) {
        return carrierChannelDao
                .findEnterpriseSpecnumBindBySpecNumId(specsvsNums);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#findBizTypeSpecnum
     * (java.util.List)
     */
    @Override
    public List<BiztypeSpecnum> findBizTypeSpecnum(
            List<EnterpriseSpecnumBind> enterpriseSpecnumBinds) {
        return carrierChannelDao.findBizTypeSpecnum(enterpriseSpecnumBinds);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.xuanwu.web.common.service.CarrierChannelService#delCarrierChannel
     * (int)
     */
    @Override
    @Transactional
    public void delCarrierChannel(List<BiztypeSpecnum> biztypeSpecnums,
                                  List<EnterpriseSpecnumBind> enterpriseSpecnumBinds,
                                  List<SpecsvsNum> specsvsNums, int id) throws Exception {
        if (ListUtil.isNotBlank(biztypeSpecnums)) {
            for (BiztypeSpecnum bs : biztypeSpecnums) {
                carrierChannelDao.delBiztypeSpecnum(bs.getId());
            }
        }
        if (ListUtil.isNotBlank(enterpriseSpecnumBinds)) {
            for (EnterpriseSpecnumBind esb : enterpriseSpecnumBinds) {
                carrierChannelDao.delEnterpriseSpecnumBind(esb.getId());
                carrierChannelDao.delWhileRedirect(esb.getId(),
                        WhiteRedirectEnum.EnterpriseSpecNum.getIndex());

            }
        }
        if (ListUtil.isNotBlank(specsvsNums)) {
            for (SpecsvsNum specsvsNum : specsvsNums) {
                carrierChannelDao.delSpecsvsNum(specsvsNum.getId());
                carrierChannelDao.delSpecnumRegionPriority(specsvsNum.getId());
                carrierChannelDao.delWhileRedirect(specsvsNum.getId(), WhiteRedirectEnum.ChannelId.getIndex());
            }
        }
        carrierChannelDao.delChannelRegion(id);
        carrierChannelDao.delChannelCarrier(id);
        carrierChannelDao.delCarrierChannel(id);

        carrierChannelDao.delWhileRedirect(id, WhiteRedirectEnum.ChannelId.getIndex());
        carrierChannelDao.delChannelChange(id);
        carrierChannelDao.delChannelChangeDetail(id);
    }

    @Override
    public SpecsvsNum findCarrierChannelBySpecsvsNum(int id) {
        return carrierChannelDao.findCarrierChannelBySpecsvsNum(id);
    }

    @Override
    public int findTicketChannelSended(int id,String msgType) {
        return msgTicketDao.findTicketChannelSended(id,msgType);
    }
}

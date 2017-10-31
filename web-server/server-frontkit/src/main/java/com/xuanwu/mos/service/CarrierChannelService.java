package com.xuanwu.mos.service;

import java.util.List;

/**
 * 通道管理的业务方法
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public interface CarrierChannelService {
    /**
     *
     * @param name
     * @param channelNum
     * @param carrier
     * @param msgType
     * @return
     */
    public int findCarrierChannelCount(String name, String channelNum,
                                       int carrier, int msgType, int enterpriseId);

    /**
     *
     * @param name
     * @param channelNum
     * @param carrier
     * @param msgType
     * @param offset
     * @param rp
     * @return
     */
    public List<CarrierChannel> findCarrierChannel(String name,
                                                   String channelNum, int carrier, int msgType, int enterpriseId,
                                                   int offset, int rp);

    /**
     * 查找简单通道列表
     *
     * @param channelNum
     *            通道号(空表示不限制)
     * @param msgType
     *            支持类型(0:不限制,1:短信,2:彩信)
     * @param state
     *            状态(-1:不限制,0:停用,1:启用)
     * @param isRemove
     *            删除标识(-1:不限制,0:正常,1:已删除)
     * @return
     */
    public List<CarrierChannel> findSimpleChannelByNum(String channelNum,
                                                       int msgType, int state, int isRemove);

    /**
     *
     * @param enterpriseID
     * @return
     */
    public List<CarrierChannel> findSimpleChannelByEnterprise(int enterpriseID, int isWhitelist);

    /**
     *
     * @param id
     * @return
     */
    public CarrierChannel findChannelByID(int id);

    /**
     *
     * @param name
     * @param channelNum
     * @return
     */
    public List<Long> findChannelByParam(String name, String channelNum,
                                         int enterpriseID);

    /**
     * @return
     */
    public int findSysConfigMixMaxLength();

    /**
     * @param name
     * @return
     */
    public CarrierChannel findCarrierChannelByName(String name);

    /**
     * @param identity
     * @return
     */
    public CarrierChannel findCarrierChannelByIdentity(String identity);

    /**
     * @return
     */
    public boolean storeCarrierChannel(RegionCarrier regionCarrier,
                                       CarrierChannel carrierChannel,
                                       List<ChannelCarrier> channelCarriers,
                                       List<ChannelRegion> channelRegions, SpecsvsNum specsvsNum,
                                       boolean isBindBusinessType,
                                       EnterpriseSpecnumBind enterpriseSpecnumBind,
                                       List<BiztypeSpecnum> biztypeSpecnums) throws Exception;

    /**
     * 查询所有白名单通道
     * */
    public List<CarrierChannel> findAllWhiteChannels();

    /**
     * @param id
     * @return
     */
    public CarrierChannel findCarrierChannelById(int id);

    /**
     * 查询所有非白名单通道
     *
     * @return
     */
    public List<CarrierChannel> findAllNonWhiteChannels();

    /**
     * @param id
     * @return
     */
    public List<BiztypeSpecnum> findAllBizTypeCarrier(int id);

    /**
     * @return
     */
    public boolean modifyCarrierChannel(RegionCarrier regionCarrier, CarrierChannel carrierChannel,
                                        SpecsvsNum specsvsNum, boolean isChangeCarrier,
                                        List<ChannelCarrier> channelCarriers, boolean isChangeRegion,
                                        List<ChannelRegion> channelRegions, SimpleUser user) throws Exception;

    /**
     * @param id
     * @return
     */
    public int findIsChannelSended(int id);

    /**
     * @param id
     * @return
     */
    public List<SpecsvsNum> findSpecsvsNumByChannelId(int id);

    /**
     * @param id
     * @return
     */
    public SpecsvsNum findSecondSpecsvsNumByChannelId(int id);

    /**
     *
     * @param id
     * @return
     */
    public SpecsvsNum findCarrierChannelBySpecsvsNum(int id);
    /**
     * @param specsvsNums
     * @return
     */
    public List<EnterpriseSpecnumBind> findEnterpriseSpecnumBindBySpecNumId(
            List<SpecsvsNum> specsvsNums);

    /**
     * @param enterpriseSpecnumBinds
     * @return
     */
    public List<BiztypeSpecnum> findBizTypeSpecnum(
            List<EnterpriseSpecnumBind> enterpriseSpecnumBinds);

    /**
     * @param id
     */
    public void delCarrierChannel(List<BiztypeSpecnum> biztypeSpecnums,
                                  List<EnterpriseSpecnumBind> enterpriseSpecnumBinds,
                                  List<SpecsvsNum> specsvsNums, int id) throws Exception;

    public int findTicketChannelSended(int id, String msgType);
}

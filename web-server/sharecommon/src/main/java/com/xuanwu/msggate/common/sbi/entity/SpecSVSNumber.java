/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.util.XmlUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Special service number
 *
 * @author <a href="mailto:wanglianguang@139130.net">LianGuang Wang</a>
 * @Data 2010-6-23
 * @Version 1.0.0
 */
public class SpecSVSNumber {
    /**
     * Assign number type
     */
    public enum NumAssignType {
        SPECIAL_ASSIGN(0), IDENTITY_ASSIGN(1);
        private final int index;

        private NumAssignType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static NumAssignType getType(int index) {
            switch (index) {
                case 0:
                    return SPECIAL_ASSIGN;
                default:
                    return IDENTITY_ASSIGN;
            }
        }
    }

    /**
     * Special service number
     */
    public enum SpecNumType {
        ROOT(0), EXTEND(1), VRITUAL_ROOT(2);
        private final int index;

        private SpecNumType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static SpecNumType getType(int index) {
            switch (index) {
                case 0:
                    return ROOT;
                case 2:
                    return VRITUAL_ROOT;
                default:
                    return EXTEND;
            }
        }
    }

    /**
     * Identity
     */
    private Integer id;

    /**
     * Is special service number or not
     */
    private boolean isWhite;

    /**
     * Parent special service number
     */
    private Integer parentID;

    /**
     * Parent special serivce number
     */
    private SpecSVSNumber parent;

    /**
     * The sub special service numbers of this node
     */
    private List<SpecSVSNumber> subNums = new ArrayList<SpecSVSNumber>();

    /**
     * Virtual channel map to this number
     */
    private List<CarrierChannel> virtualChannels = new ArrayList<CarrierChannel>();

    /**
     * The region redirect
     */
    //private RegionSpecNum regionSpecNum = new RegionSpecNum();

    /**
     * The White redirect
     */
    //private WhiteRedirectSpecNum redirectSpecNum = new WhiteRedirectSpecNum();

    /**
     * Special number of current node
     */
    private String number;

    /**
     * Extend flag
     */
    private boolean extendFlag;
    /**
     * Extend size
     */
    private int extendSize;

    /**
     * Basic number
     */
    private String basicNumber;

    /**
     * Share flag
     */
    private boolean shareFlag;

    /**
     * Channel id
     */
    private Integer channelID;
    /**
     * Carrier channel
     */
    private CarrierChannel channel;

    /**
     * Special number type
     */
    private SpecNumType numType;

    /**
     * Special number assign type
     */
    private NumAssignType assignType;

    /**
     * Parameters
     */
    private Map<String, Object> paras;

    private DestBindSpecNum destBindSpecNum;


    /**
     * Return the region carrier that the number belonged to
     */
    public RegionCarrier getRegionCarrier() {
        return this.getCarrierChannel().getRegionCarrier();
    }

    public List<Integer> getLimitRegions() {
        return this.getCarrierChannel().getLimitRegions();
    }

    /**
     * Get paras
     *
     * @return the paras
     */
    public Map<String, Object> getParas() {
        return paras;
    }

    /**
     * Set paras
     *
     * @param paras the paras to set
     */
    public void setParas(String text) {
        if (StringUtils.isBlank(text))
            return;
        paras = (Map<String, Object>) XmlUtil.fromXML(text);
    }

    /**
     * Get number
     *
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set number
     *
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Get extendFlag
     *
     * @return the extendFlag
     */
    public boolean isExtendFlag() {
        return extendFlag;
    }

    /**
     * Set extendFlag
     *
     * @param extendFlag the extendFlag to set
     */
    public void setExtendFlag(boolean extendFlag) {
        this.extendFlag = extendFlag;
    }

    /**
     * Get extendSize
     *
     * @return the extendSize
     */
    public int getExtendSize() {
        return extendSize;
    }

    /**
     * Set extendSize
     *
     * @param extendSize the extendSize to set
     */
    public void setExtendSize(int extendSize) {
        this.extendSize = extendSize;
    }

    /**
     * Get basicNumber
     *
     * @return the basicNumber
     */
    public String getBasicNumber() {
        return basicNumber;
    }

    /**
     * Set basicNumber
     *
     * @param basicNumber the basicNumber to set
     */
    public void setBasicNumber(String basicNumber) {
        this.basicNumber = basicNumber;
    }

    /**
     * Get shareFlag
     *
     * @return the shareFlag
     */
    public boolean isShareFlag() {
        return shareFlag;
    }

    /**
     * Set shareFlag
     *
     * @param shareFlag the shareFlag to set
     */
    public void setShareFlag(boolean shareFlag) {
        this.shareFlag = shareFlag;
    }

    public SpecNumType getSpecNumType() {
        return numType;
    }

    public void setSpecNumType(SpecNumType specNumType) {
        this.numType = specNumType;
    }

    public void setType(int index) {
        this.numType = SpecNumType.getType(index);
    }

    public boolean hasParentChannel() {
        return (channel.getParent() != null);
    }

    /**
     * Get carrierChannel
     *
     * @return the carrierChannel
     */
    public CarrierChannel getCarrierChannel() {
        return channel;
    }

    /**
     * Set carrierChannel
     *
     * @param carrierChannel the carrierChannel to set
     */
    public void setCarrierChannel(CarrierChannel carrierChannel) {
        this.channel = carrierChannel;
    }

    public NumAssignType getAssignType() {
        return assignType;
    }

    public void setAssignType(int index) {
        this.assignType = NumAssignType.getType(index);
    }

    /**
     * Get id
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set id
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get isWhite
     *
     * @return the isWhite
     */
    public boolean isWhite() {
        return isWhite;
    }

    public boolean hasRegionRedirect() {
        boolean bool = (destBindSpecNum == null) ? false : destBindSpecNum.hasRegionRedirect();
        return (bool || channel.hasRegionRedirect());
    }

//	public void addRegionRedirect(BindSpecNumResult result){
//		regionSpecNum.addResult(result);
//	}

    public boolean hasWhiteRedirect() {
        boolean bool = (destBindSpecNum == null) ? false : destBindSpecNum.hasWhiteRedirect();
        return (bool || channel.hasWhiteRedirect());
    }

    public boolean hasChangeRedirect() {
        boolean bool = (destBindSpecNum == null) ? false : destBindSpecNum.hasChangeRedirect();
        return (bool || channel.hasChangeRedirect());
    }

    /**
     * Set isWhite
     *
     * @param isWhite the isWhite to set
     */
    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    /**
     * Get parentID
     *
     * @return the parentID
     */
    public Integer getParentID() {
        return parentID;
    }

    /**
     * Set parentID
     *
     * @param parentID the parentID to set
     */
    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    /**
     * Get parent
     *
     * @return the parent
     */
    public SpecSVSNumber getParent() {
        return parent;
    }

    /**
     * Set parent
     *
     * @param parent the parent to set
     */
    public void setParent(SpecSVSNumber parent) {
        this.parent = parent;
    }

//	public WhiteRedirectSpecNum getRedirectSpecNum() {
//		return redirectSpecNum;
//	}
//
//	public void setRedirectSpecNum(WhiteRedirectSpecNum redirectSpecNum) {
//		this.redirectSpecNum = redirectSpecNum;
//	}
//
//	public RegionSpecNum getRegionSpecNum() {
//		return regionSpecNum;
//	}
//
//	public void setRegionSpecNum(RegionSpecNum regionSpecNum) {
//		this.regionSpecNum = regionSpecNum;
//	}

    /**
     * Get channelID
     *
     * @return the channelID
     */
    public Integer getChannelID() {
        return channelID;
    }

    /**
     * Set channelID
     *
     * @param channelID the channelID to set
     */
    public void setChannelID(Integer channelID) {
        this.channelID = channelID;
    }

    /**
     * Get numType
     *
     * @return the numType
     */
    public SpecNumType getNumType() {
        return numType;
    }

    /**
     * Set numType
     *
     * @param numType the numType to set
     */
    public void setNumType(SpecNumType numType) {
        this.numType = numType;
    }

    public List<SpecSVSNumber> getSubNums() {
        return subNums;
    }

    public void setSubNums(List<SpecSVSNumber> subNums) {
        this.subNums = subNums;
    }

    public List<CarrierChannel> getVirtualChannels() {
        return virtualChannels;
    }

    public void setVirtualChannels(List<CarrierChannel> virtualChannels) {
        this.virtualChannels = virtualChannels;
    }

    public void addVirtualChannel(CarrierChannel virtualChannel) {
        virtualChannels.add(virtualChannel);
    }

    public Carrier getCarrier() {
        return getCarrierChannel().getRegionCarrier().getCarrier();
    }

    /**
     * Binded enterprises
     */
    private List<EnterpriseBind> bindEnterprises = new ArrayList<EnterpriseBind>();

    public List<EnterpriseBind> getBindEnterprises() {
        return bindEnterprises;
    }

    public void addBindEnterprises(EnterpriseBind enterprise) {

        if (!bindEnterprises.contains(enterprise))

            bindEnterprises.add(enterprise);
    }

    public DestBindSpecNum getDestBindSpecNum() {
        return destBindSpecNum;
    }

    public void setDestBindSpecNum(DestBindSpecNum destBindSpecNum) {
        this.destBindSpecNum = destBindSpecNum;
    }

    @Override
    public String toString() {
        return "SpecSVSNumber [id=" + id + ", isWhite=" + isWhite + ", parent="
                + parent + ", number=" + number + ", extendFlag=" + extendFlag
                + ", extendSize=" + extendSize + ", basicNumber=" + basicNumber
                + ", shareFlag=" + shareFlag + ", channel=" + channel
                + ", numType=" + numType + ", assignType=" + assignType
                + ", paras=" + paras + "]";
    }

}

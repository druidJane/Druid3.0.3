/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.impl.FetchFrame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Carrier channel
 *
 * @author <a href="mailto:wanglianguang@139130.net>Guang Wang</a>
 * @Data 2010-6-29
 * @Version 1.0.0
 */
public class CarrierChannel {
    /**
     * Channel type
     */
    public enum ChannelType {
        PHYSIC_CHANNEL(0), VIRTUAL_CHANNEL(1);
        private final int index;

        private ChannelType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static ChannelType getType(int index) {
            switch (index) {
                case 0:
                    return PHYSIC_CHANNEL;
                default:
                    return VIRTUAL_CHANNEL;
            }
        }
    }

    /**
     * chargetype parameterName
     */
    public static final String CHARGE_TYPE_NAME = "charge_type";

    /**
     * Channel type
     */
    public enum ChargeType {
        NoCharge(1, "不计费"), AcceptCharge(2, "接收方计费");
        private final int type;
        private final String descript;

        private ChargeType(int type, String descript) {
            this.type = type;
            this.descript = descript;
        }

        public int getType() {
            return this.type;
        }

        public String getDescript() {
            return this.descript;
        }

        public static ChargeType getChargeType(int type) {
            switch (type) {
                case 1:
                    return NoCharge;
                case 2:
                    return AcceptCharge;
                default:
                    return null;
            }
        }
    }

    /**
     * Identity
     */
    private Integer id;
    /**
     * Is signal
     */
    private boolean signal;

    /**
     * Is long signal
     */
    private boolean longSignal;

    /**
     * Is erase singal
     */
    private boolean eraseSignal;

    /**
     * Signal content
     */
    private String signalInfo;

    /**
     * Long singal content
     */
    private String longSignalInfo;

    /**
     * Enterprise identity
     */
    private String corpID;

    /**
     * Support MMS or not
     */
    private boolean mms;
    /**
     * Support SSM or not
     */
    private boolean sms;
    /**
     * Support wappush or not
     */
    private boolean wappush;
    /**
     * Support voiceNotice or not
     */
    private boolean voiceNotice;
    /**
     * Support voiceCode or not
     */
    private boolean voiceCode;
    /**
     * Support state report or not
     */
    private boolean stateReport;
    /**
     * Support mass commit or not
     */
    private boolean massCommit;
    /**
     * Is support long sms
     */
    private boolean longSms;

    /**
     * Is support mo
     */
    private boolean mo;

    /**
     * Mo max wait time, the unit is hour.
     */
    private int moWaitTime;
    /**
     * The max length of the bytes in one sms
     */
    private int maxLength;
    /**
     * The needed bytes when split sms
     */
    private int extendSpace;

    /**
     * The carrier channel's number length can be extend
     */
    private int extendNumLength;

    /**
     * Account of the channel
     */
    private String account;
    /**
     * Password
     */
    private String password;
    /**
     * Channel name
     */
    private String name;
    /**
     * The hostname of the remote
     */
    private String hostname;
    /**
     * port
     */
    private int port;

    /**
     * Channel type
     */
    private ChannelType type;

    /**
     * Region carrier id
     */
    private Integer regionCarrierID;
    /**
     * Region
     */
    private RegionCarrier regionCarrier;

    /**
     * Protocol version
     */
    private String protoVersion;

    private Date sendStartTime;

    private Date sendEndTime;

    /**
     * Virtual channel maps
     */
    private List<VirtualChannelNumMap> virtualSpecs = new ArrayList<VirtualChannelNumMap>();

    /**
     * Duplicated virtual channels
     */
    private List<Integer> virtualChannels = new ArrayList<Integer>();

    private List<Integer> limitRegions = new ArrayList<Integer>();

    private List<Integer> supportCarriers = new ArrayList<Integer>();

    private Integer parentId;

    /**
     * Identity of the channel
     */
    private String identity;

    private String channelNum;

    private String channelShortNum;

    /**
     * Parameters associated with the channel
     */
    private Map<String, Object> parameters = new HashMap<String, Object>();

    private long mmsMaxLength;

    private String mmsSupportType;

    private CarrierChannel parent;

    private DestBindCarrierChannel destBindChannel;

    /**
     * Get maxLength
     *
     * @return the maxLength
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Set maxLength
     *
     * @param maxLength the maxLength to set
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Get extendSpace
     *
     * @return the extendSpace
     */
    public int getExtendSpace() {
        return extendSpace;
    }

    /**
     * Set extendSpace
     *
     * @param extendSpace the extendSpace to set
     */
    public void setExtendSpace(int extendSpace) {
        this.extendSpace = extendSpace;
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
     * Get signal
     *
     * @return the signal
     */
    public boolean isSignal() {
        return signal;
    }

    /**
     * Set signal
     *
     * @param signal the signal to set
     */
    public void setSignal(boolean signal) {
        this.signal = signal;
    }

    /**
     * Get mms
     *
     * @return the mms
     */
    public boolean isMms() {
        return mms;
    }

    /**
     * Set mms
     *
     * @param mms the mms to set
     */
    public void setMms(boolean mms) {
        this.mms = mms;
    }

    /**
     * Get sms
     *
     * @return the sms
     */
    public boolean isSms() {
        return sms;
    }

    /**
     * Set sms
     *
     * @param sms the sms to set
     */
    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public boolean isVoiceNotice() {
        return voiceNotice;
    }

    public void setVoiceNotice(boolean voiceNotice) {
        this.voiceNotice = voiceNotice;
    }

    public boolean isVoiceCode() {
        return voiceCode;
    }

    public void setVoiceCode(boolean voiceCode) {
        this.voiceCode = voiceCode;
    }

    /**
     * Get wappush
     *
     * @return the wappush
     */
    public boolean isWappush() {
        return wappush;
    }

    /**
     * Set wappush
     *
     * @param wappush the wappush to set
     */
    public void setWappush(boolean wappush) {
        this.wappush = wappush;
    }

    /**
     * Get stateReport
     *
     * @return the stateReport
     */
    public boolean isStateReport() {
        return stateReport;
    }

    /**
     * Set stateReport
     *
     * @param stateReport the stateReport to set
     */
    public void setStateReport(boolean stateReport) {
        this.stateReport = stateReport;
    }

    /**
     * Get massCommit
     *
     * @return the massCommit
     */
    public boolean isMassCommit() {
        return massCommit;
    }

    /**
     * Set massCommit
     *
     * @param massCommit the massCommit to set
     */
    public void setMassCommit(boolean massCommit) {
        this.massCommit = massCommit;
    }

    public boolean isEraseSignal() {
        return eraseSignal;
    }

    public void setEraseSignal(boolean eraseSignal) {
        this.eraseSignal = eraseSignal;
    }

    public boolean isMo() {
        return mo;
    }

    public void setMo(boolean mo) {
        this.mo = mo;
    }

    public int getMoWaitTime() {
        return moWaitTime;
    }

    public void setMoWaitTime(int moWaitTime) {
        this.moWaitTime = moWaitTime;
    }

    public Date getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(Date sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public Date getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(Date sendEndTime) {
        this.sendEndTime = sendEndTime;
    }

    /**
     * Get regionCarrier
     *
     * @return the regionCarrier
     */
    public RegionCarrier getRegionCarrier() {
        return regionCarrier;
    }

    /**
     * Set regionCarrier
     *
     * @param regionCarrier the regionCarrier to set
     */
    public void setRegionCarrier(RegionCarrier region) {
        this.regionCarrier = region;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(int index) {
        this.type = ChannelType.getType(index);
    }

    public boolean isLongSms() {
        return longSms;
    }

    public void setLongSms(boolean longSms) {
        this.longSms = longSms;
    }

    public List<Integer> getLimitRegions() {
        return limitRegions;
    }

    public void setLimitRegions(List<Integer> limitRegions) {
        this.limitRegions = limitRegions;
    }

    public List<Integer> getSupportCarriers() {
        return supportCarriers;
    }

    public void setSupportCarriers(List<Integer> supportCarriers) {
        this.supportCarriers = supportCarriers;
    }

    public boolean isInSupportCarrier(Carrier carrier) {
        for (Integer index : supportCarriers) {
            if (Carrier.getType(index) == carrier) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get regionCarrierID
     *
     * @return the regionCarrierID
     */
    public Integer getRegionCarrierID() {
        return regionCarrierID;
    }

    /**
     * Set regionCarrierID
     *
     * @param regionCarrierID the regionCarrierID to set
     */
    public void setRegionCarrierID(Integer regionCarrierID) {
        this.regionCarrierID = regionCarrierID;
    }

    public List<Integer> getAllMapChannels() {
        return virtualChannels;
    }

    public Integer getRetrieveId() {
        return isNotChildChannel() ? id : parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public List<VirtualChannelNumMap> getVirtualSpecs() {
        return virtualSpecs;
    }

    public void setVirtualSpecs(List<VirtualChannelNumMap> virtualSpecs) {
        this.virtualSpecs = virtualSpecs;
    }

    public boolean isNotChildChannel() {
        return (parentId == null || parentId.equals(0)) ? true : false;
    }

    public void confMapChannels() {
        virtualChannels.clear();
        virtualChannels.add(id);
        for (VirtualChannelNumMap map : virtualSpecs) {
            virtualChannels.add(map.getVirtualChannelID());
        }
    }

    public SpecSVSNumber getMapSpcNumByChannelID(int channelID) {
        for (VirtualChannelNumMap map : virtualSpecs) {
            if (map.getVirtualChannelID() == channelID)
                return map.getSpecNum();
        }
        return null;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtoVersion() {
        return protoVersion;
    }

    public void setProtoVersion(String protoVersion) {
        this.protoVersion = protoVersion;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getSignalInfo() {
        return signalInfo;
    }

    public void setSignalInfo(String signalInfo) {
        this.signalInfo = signalInfo;
    }

    public boolean isLongSignal() {
        return longSignal;
    }

    public void setLongSignal(boolean longSignal) {
        this.longSignal = longSignal;
    }

    public String getLongSignalInfo() {
        return longSignalInfo;
    }

    public void setLongSignalInfo(String longSignalInfo) {
        this.longSignalInfo = longSignalInfo;
    }

    public String getCorpID() {
        return corpID;
    }

    public void setCorpID(String corpID) {
        this.corpID = corpID;
    }

    public Carrier getCarrier() {
        return getRegionCarrier().getCarrier();
    }

    public int getExtendNumLength() {
        return extendNumLength;
    }

    public void setExtendNumLength(int extendNumLength) {
        this.extendNumLength = extendNumLength;
    }

    public int getSpeed() {
        Object speedPam = getParameters().get("speed");
        if (speedPam == null || String.valueOf(speedPam).trim().equals(""))
            return 200;

        return Integer.parseInt(speedPam.toString());
    }

    public int getMassPhoneNum() {
        Object phoneNum = getParameters().get("phonenum");
        if (phoneNum == null || String.valueOf(phoneNum).trim().equals(""))
            return -1;
        return Integer.parseInt(phoneNum.toString());
    }

    /**
     * Confirms whether in valid send time
     *
     * @return true--in valid send time period
     */
    public boolean isInValidSendTime(FetchFrame fetchFrame) {
        boolean validResult = false;
        boolean start = false;
        boolean end = false;
        if (fetchFrame.getBoeTime() == null && fetchFrame.getEoeTime() == null) {
            start = true;
            end = true;
        } else if (fetchFrame.getBoeTime() != null
                && fetchFrame.getEoeTime() == null) {
            start = validStart(fetchFrame);
            end = true;
        } else if (fetchFrame.getBoeTime() != null
                && fetchFrame.getEoeTime() != null) {
            start = validStart(fetchFrame);
            end = validEnd(fetchFrame);
        }
        if (start && end) {
            validResult = true;
        } else {
            validResult = false;
        }
        return validResult;

    }

    private Calendar getCalendar(Date time) {
        Calendar calendar = Calendar.getInstance();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(time);
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,
                tempCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, tempCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, tempCalendar.get(Calendar.SECOND));
        return calendar;
    }

    private boolean validStart(FetchFrame fetchFrame) {
        boolean start = false;
        /*
		 * if (sendStartTime == null && fetchFrame.getBoeTime() == null) { start
		 * = true; } Calendar startTime = Calendar.getInstance(); if
		 * (fetchFrame.getBoeTime() == null && sendStartTime != null) {
		 * startTime = getCalendar(sendStartTime); } else if
		 * (fetchFrame.getBoeTime() != null && sendStartTime == null) {
		 * startTime = getCalendar(fetchFrame.getBoeTime()); } else if
		 * (fetchFrame.getBoeTime() != null && sendStartTime != null) { Calendar
		 * boeTime = getCalendar(fetchFrame.getBoeTime()); Calendar
		 * sendStartTimeCalendar = getCalendar(sendStartTime); if
		 * (sendStartTimeCalendar.before(boeTime)) { startTime = boeTime; } else
		 * { startTime = sendStartTimeCalendar; } }
		 */
        Calendar startTime = getCalendar(fetchFrame.getBoeTime());
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(new Date());
        if (currentTime.after(startTime)) {
            start = true;
        }
        return start;
    }

    private boolean validEnd(FetchFrame fetchFrame) {
        boolean end = false;
		/*
		 * if (sendEndTime == null && fetchFrame.getEoeTime() == null) { end =
		 * true; } Calendar endTime = Calendar.getInstance(); if
		 * (fetchFrame.getEoeTime() == null && sendEndTime != null) { endTime =
		 * getCalendar(sendEndTime); } else if (fetchFrame.getEoeTime() != null
		 * && sendEndTime == null) { endTime =
		 * getCalendar(fetchFrame.getEoeTime()); } else if
		 * (fetchFrame.getEoeTime() != null && sendEndTime != null) { Calendar
		 * eoeTime = getCalendar(fetchFrame.getEoeTime()); Calendar
		 * sendEndTimeCalendar = getCalendar(sendEndTime); if
		 * (sendEndTimeCalendar.after(eoeTime)) { endTime = eoeTime; } else {
		 * endTime = sendEndTimeCalendar; } }
		 */
        Calendar endTime = getCalendar(fetchFrame.getEoeTime());
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(new Date());
        if (currentTime.before(endTime)) {
            end = true;
        }
        return end;
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }

    public String getChannelShortNum() {
        return channelShortNum;
    }

    public void setChannelShortNum(String channelShortNum) {
        this.channelShortNum = channelShortNum;
    }

    public void setMmsMaxLength(long mmsMaxLength) {
        this.mmsMaxLength = mmsMaxLength;
    }

    public long getMmsMaxLength() {
        return mmsMaxLength;
    }

    public void setMmsSupportType(String mmsSupportType) {
        this.mmsSupportType = mmsSupportType;
    }

    public String getMmsSupportType() {
        return mmsSupportType;
    }

    public CarrierChannel getParent() {
        return parent;
    }

    public void setParent(CarrierChannel parent) {
        this.parent = parent;
    }

    public DestBindCarrierChannel getDestBindChannel() {
        return destBindChannel;
    }

    public void setDestBindChannel(DestBindCarrierChannel destBindChannel) {
        this.destBindChannel = destBindChannel;
    }

    public boolean hasChangeRedirect() {
        boolean bool = false;
        if (destBindChannel != null)
            bool = destBindChannel.hasChangeRedirect();

        if (!bool && parent != null) {
            DestBindCarrierChannel dest = parent.getDestBindChannel();
            return (dest == null) ? false : dest.hasChangeRedirect();
        }
        return bool;
    }

    public boolean hasRegionRedirect() {
        boolean bool = false;
        if (destBindChannel != null)
            bool = destBindChannel.hasRegionRedirect();

        if (!bool && parent != null) {
            DestBindCarrierChannel dest = parent.getDestBindChannel();
            return (dest == null) ? false : dest.hasRegionRedirect();
        }
        return bool;
    }

    public boolean hasWhiteRedirect() {
        boolean bool = false;
        if (destBindChannel != null)
            bool = destBindChannel.hasWhiteRedirect();

        if (!bool && parent != null) {
            DestBindCarrierChannel dest = parent.getDestBindChannel();
            return (dest == null) ? false : dest.hasWhiteRedirect();
        }
        return bool;
    }

    // public DestBindCarrierChannel getWholeDestChannel(){
    // if(destBindChannel != null)
    // return destBindChannel;
    // if(parent != null)
    // return parent.getDestBindChannel();
    // return null;
    // }

    @Override
    public String toString() {
        return "CarrierChannel [id=" + id + ", signal=" + signal
                + ", longSignal=" + longSignal + ", eraseSignal=" + eraseSignal
                + ", signalInfo=" + signalInfo + ", longSignalInfo="
                + longSignalInfo + ", corpID=" + corpID + ", mms=" + mms
                + ", sms=" + sms + ", wappush=" + wappush + ", voiceNotice=" + voiceNotice
                + ", voiceCode=" + voiceCode + ", stateReport="
                + stateReport + ", massCommit=" + massCommit + ", longSms="
                + longSms + ", mo=" + mo + ", moWaitTime=" + moWaitTime
                + ", maxLength=" + maxLength + ", extendSpace=" + extendSpace
                + ", extendNumLength=" + extendNumLength + ", account="
                + account + ", password=" + password + ", name=" + name
                + ", hostname=" + hostname + ", port=" + port + ", type="
                + type + ", regionCarrierID=" + regionCarrierID
                + ", regionCarrier=" + regionCarrier + ", protoVersion="
                + protoVersion + ", sendStartTime=" + sendStartTime
                + ", sendEndTime=" + sendEndTime + ", virtualSpecs="
                + virtualSpecs + ", virtualChannels=" + virtualChannels
                + ", limitRegions=" + limitRegions + ", supportCarriers="
                + supportCarriers + ", parentId=" + parentId + ", identity="
                + identity + ", channelNum=" + channelNum
                + ", channelShortNum=" + channelShortNum + ", parameters="
                + parameters + "]";
    }

	/*
	 * private boolean validFrame(FetchFrame fetchFrame){ boolean validResult =
	 * false; Calendar currentTime = Calendar.getInstance();
	 * currentTime.setTime(new Date()); Calendar startTime =
	 * getCalendar(fetchFrame.getBoeTime()); Calendar endTime =
	 * getCalendar(fetchFrame.getEoeTime());
	 * if(currentTime.after(startTime)&&currentTime.before(endTime)) validResult
	 * = true; return validResult; }
	 */

	/*
	 * private boolean validGateway(FetchFrame fetchFrame){ boolean validResult
	 * = false; Calendar currentTime = Calendar.getInstance();
	 * currentTime.setTime(new Date()); Calendar startTime =
	 * getCalendar(sendStartTime); Calendar endTime = getCalendar(sendEndTime);
	 * if(currentTime.after(startTime)&&currentTime.before(endTime)) validResult
	 * = true; return validResult; }
	 */

}

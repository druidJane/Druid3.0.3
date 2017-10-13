package com.xuanwu.mos.domain.entity;

import java.util.Date;

/**
 * Created by Jiang.Ziyuan on 2017/4/21.
 */
public class SpecsvsNum {

    private int id;
    private int channelId;
    private Integer parentNumId;
    private String number = "";
    private boolean extend;
    private int extendSize;
    private boolean shareFlag;
    private int type;
    private int assignType;
    private boolean isWhite;
    private String basicNumber = "";
    private String parameters = "";
    private int version;
    private boolean isRemove;
    private int creatorId;
    private Date createTime;
    private int referenceCount;
    private String remark = "";

    public static SpecsvsNum cloneSpecsvsNum(SpecsvsNum specsvsNum) {
        SpecsvsNum clone = new SpecsvsNum();
        clone.setChannelId(specsvsNum.getChannelId());
        clone.setNumber(specsvsNum.getNumber());
        clone.setExtend(specsvsNum.isExtend());
        clone.setExtendSize(specsvsNum.getExtendSize());
        clone.setShareFlag(specsvsNum.isShareFlag());
        clone.setWhite(specsvsNum.isWhite());
        clone.setBasicNumber(specsvsNum.getBasicNumber());
        clone.setParameters(specsvsNum.getParameters());
        clone.setVersion(specsvsNum.getVersion());
        clone.setRemove(specsvsNum.isRemove());
        clone.setCreatorId(specsvsNum.getCreatorId());
        clone.setCreateTime(specsvsNum.getCreateTime());
        clone.setReferenceCount(specsvsNum.getReferenceCount());
        clone.setRemark(specsvsNum.getRemark());
        return clone;
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            要设置的 id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return channelId
     */
    public int getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     *            要设置的 channelId
     */
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    /**
     * @return parentNumId
     */
    public Integer getParentNumId() {
        return parentNumId;
    }

    /**
     * @param parentNumId
     *            要设置的 parentNumId
     */
    public void setParentNumId(Integer parentNumId) {
        this.parentNumId = parentNumId;
    }

    /**
     * @return number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number
     *            要设置的 number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return extend
     */
    public boolean isExtend() {
        return extend;
    }

    /**
     * @param extend
     *            要设置的 extend
     */
    public void setExtend(boolean extend) {
        this.extend = extend;
    }

    /**
     * @return extendSize
     */
    public int getExtendSize() {
        return extendSize;
    }

    /**
     * @param extendSize
     *            要设置的 extendSize
     */
    public void setExtendSize(int extendSize) {
        this.extendSize = extendSize;
    }

    /**
     * @return shareFlag
     */
    public boolean isShareFlag() {
        return shareFlag;
    }

    /**
     * @param shareFlag
     *            要设置的 shareFlag
     */
    public void setShareFlag(boolean shareFlag) {
        this.shareFlag = shareFlag;
    }

    /**
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     *            要设置的 type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return assignType
     */
    public int getAssignType() {
        return assignType;
    }

    /**
     * @param assignType
     *            要设置的 assignType
     */
    public void setAssignType(int assignType) {
        this.assignType = assignType;
    }

    /**
     * @return isWhite
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * @param isWhite
     *            要设置的 isWhite
     */
    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    /**
     * @return basicNumber
     */
    public String getBasicNumber() {
        return basicNumber;
    }

    /**
     * @param basicNumber
     *            要设置的 basicNumber
     */
    public void setBasicNumber(String basicNumber) {
        this.basicNumber = basicNumber;
    }

    /**
     * @return parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     *            要设置的 parameters
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * @return version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version
     *            要设置的 version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return isRemove
     */
    public boolean isRemove() {
        return isRemove;
    }

    /**
     * @param isRemove
     *            要设置的 isRemove
     */
    public void setRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }

    /**
     * @return creatorId
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId
     *            要设置的 creatorId
     */
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            要设置的 createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return referenceCount
     */
    public int getReferenceCount() {
        return referenceCount;
    }

    /**
     * @param referenceCount
     *            要设置的 referenceCount
     */
    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            要设置的 remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public enum Type {
        ROOT(0), // 根端口号
        EXTEND(1), // 扩展端口号
        VIRTUAL(2);// 虚拟根端口号

        private final int index;

        private Type(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static Type getType(int index) {
            Type[] types = Type.values();
            for (Type type : types) {
                if (type.getIndex() == index) {
                    return type;
                }
            }
            return ROOT;
        }

        public static Type parseType(String str) {
            return Type.valueOf(str);
        }
    }

    public enum AssignType {
        FIXED(0), // 固定分配
        ENTERPRISE(1);// 企业标识

        private final int index;

        private AssignType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static AssignType getType(int index) {
            AssignType[] assignTypes = AssignType.values();
            for (AssignType assignType : assignTypes) {
                if (assignType.getIndex() == index) {
                    return assignType;
                }
            }
            return FIXED;
        }

        public static AssignType parseType(String str) {
            return AssignType.valueOf(str);
        }
    }
}

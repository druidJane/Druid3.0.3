package com.xuanwu.mos.domain.entity;

/**
 * Created by Jiang.Ziyuan on 2017/3/23.
 */
public class Region extends BaseEntity {
    private int id;
    private int parentId;
    private String name;
    private String areaIdentity;
    private int type;
    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }
    /**
     * @param id 要设置的 id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return parentId
     */
    public int getParentId() {
        return parentId;
    }
    /**
     * @param parentId 要设置的 parentId
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    /**
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return areaIdentity
     */
    public String getAreaIdentity() {
        return areaIdentity;
    }
    /**
     * @param areaIdentity 要设置的 areaIdentity
     */
    public void setAreaIdentity(String areaIdentity) {
        this.areaIdentity = areaIdentity;
    }
    /**
     * @return type
     */
    public int getType() {
        return type;
    }
    /**
     * @param type 要设置的 type
     */
    public void setType(int type) {
        this.type = type;
    }
    /* (non-Javadoc)
     * @see com.xuanwu.web.common.entity.BaseEntity#toJSON()
     */
    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id);
        sb.append(",\"name\":\"").append(name).append("\"");
        sb.append('}');
        return sb.toString();
    }
}

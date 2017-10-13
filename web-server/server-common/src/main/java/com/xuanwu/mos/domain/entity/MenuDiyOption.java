package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.utils.StringUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 自定义菜单选项
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public class MenuDiyOption extends BaseEntity implements
        Comparator<MenuDiyOption> {

    private int id;// 主键
    private int menuId;// 所属菜单
    private int parentId;// 所属父菜单,0表示根菜单
    private String name;// 按钮描述，即按钮名字，不超过16个字节，子菜单不超过40个字节
    private MenuType type;// 按钮类型，目前有click/view类型
    private String key;// 按钮KEY值，用于消息接口(event类型)推送，不超过128字节
    private String url;// 用户点击view类型按钮后，会直接跳转到开发者指定的url中
    private int ordx;
    private List<MenuDiyOption> subOptions;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public void setTypeIdx(int idx) {
        this.type = MenuType.getType(idx);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrdx() {
        return ordx;
    }

    public void setOrdx(int ordx) {
        this.ordx = ordx;
    }

    public List<MenuDiyOption> getSubOptions() {
        return subOptions;
    }

    public void setSubOptions(List<MenuDiyOption> subOptions) {
        this.subOptions = subOptions;
    }

    public static enum MenuType {
        CLICK(0), VIEW(1);

        private int index;

        private MenuType(int index) {
            this.index = index;
        }

        public static MenuType getType(int index) {
            for (MenuType type : MenuType.values()) {
                if (type.getIndex() == index) {
                    return type;
                }
            }
            return null;
        }

        public int getIndex() {
            return index;
        }
    }

    @Override
    public String toJSON() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"id\":" + id);
        sb.append(",\"type\":" + type.getIndex());
        sb.append(",\"name\":\"" + StringUtil.fixJsonStr(name) + "\"");
        sb.append(",\"key\":\"" + StringUtil.fixJsonStr(key) + "\"");
        sb.append(",\"url\":\"" + StringUtil.fixJsonStr(url) + "\"");
        if (subOptions == null || subOptions.isEmpty()) {
            sb.append(",\"subOptions\":null");
        } else {
            Collections.sort(subOptions, new MenuDiyOption());
            sb.append(",\"subOptions\":[");
            for (MenuDiyOption o : subOptions) {
                sb.append(o.toJSON()).append(",");
            }
            sb.deleteCharAt(sb.toString().length() - 1);
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    public String getParamData() {
        StringBuffer sb = new StringBuffer();
        sb.append(",\"type\":\"" + type.name().toLowerCase() + "\"");
        if (type == MenuType.CLICK) {
            sb.append(",\"key\":\"" + StringUtil.fixJsonStr(key) + "\"");
        } else {
            sb.append(",\"url\":\"" + StringUtil.fixJsonStr(url) + "\"");
        }
        return sb.toString();
    }

    @Override
    public int compare(MenuDiyOption o1, MenuDiyOption o2) {
        return o1.getOrdx() - o2.getOrdx();
    }
}

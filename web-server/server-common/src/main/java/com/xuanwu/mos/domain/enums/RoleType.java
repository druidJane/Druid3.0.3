/*   
* Copyright (c) 2016/12/2 by XuanWu Wireless Technology Co., Ltd 
*             All rights reserved  
*/
package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/**
 * The enum Role type.
 */
public enum RoleType implements HasIndexValue {
    /**
     * 普通角色
     */
    NORMAL_ROLE(0),
    /**
     * 初始化角色
     */
    INIT_ROLE(1),
    /**
     * 超级管理员
     */
    SUPPER_ROLE(9);
    private final int index;

    private RoleType(int index) {
        this.index = index;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets type.
     *
     * @param index the index
     * @return the type
     */
    public static RoleType getType(int index) {
        switch (index) {
            case 0:
                return NORMAL_ROLE;
            case 1:
                return INIT_ROLE;
            case 9:
                return SUPPER_ROLE;
            default:
                throw new IllegalArgumentException("Unsupport role type: " + index);
        }
    }
}
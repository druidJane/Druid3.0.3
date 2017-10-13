/*   
* Copyright (c) 2016/10/19 by XuanWu Wireless Technology Co., Ltd 
*             All rights reserved  
*/
package com.xuanwu.mos.domain.entity;

import java.util.List;

/**
 * @author <a href="mailto:jiangpeng@wxchina.com">Peng.Jiang</a>
 * @version 1.0.0
 * @date 2016/10/19
 */
public class PlatformEntity {

    private Integer id;
    private String name;
    private Boolean operate;
    private List<Role> roles;
    private boolean checked;
    private boolean havePlatform;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOperate() {
        return operate;
    }

    public void setOperate(Boolean operate) {
        this.operate = operate;
    }

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isHavePlatform() {
		return havePlatform;
	}

	public void setHavePlatform(boolean havePlatform) {
		this.havePlatform = havePlatform;
	}

	
}

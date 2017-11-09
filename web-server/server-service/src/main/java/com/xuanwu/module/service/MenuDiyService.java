package com.xuanwu.module.service;

import com.xuanwu.mos.domain.entity.MenuDiy;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义菜单数据服务
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Component
public interface MenuDiyService {
    /**
     * 自定义菜单数量
     *
     * @param userId
     * @param accountId
     * @param title
     * @return
     */
    public int findMenusCount(int userId, int accountId, String title);

    /**
     * 自定义菜单列表
     *
     * @param userId
     * @param accountId
     * @param title
     * @param offset
     * @param reqNum
     * @return
     */
    public List<MenuDiy> findMenus(int userId, int accountId, String title,
                                   int offset, int reqNum);


    /**
     * 简单菜单列表查询
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<MenuDiy> findMenuOptions(int userId, int accountId, String name, int fetchSize);

    /**
     * 查找自定义菜单
     *
     * @param id
     * @return
     */
    public MenuDiy findMenuById(int id);

    /**
     * 新增自定义菜单
     *
     * @param menuDiy
     * @return
     */
    public boolean addMenu(MenuDiy menuDiy);

    /**
     * 修改自定义菜单
     *
     * @param menuDiy
     * @return
     */
    public boolean updateMenu(MenuDiy menuDiy);

    /**
     * 更新自定义菜单状态
     *
     * @param id
     * @param state
     * @return
     */
    public boolean updateMenuState(int id, int state);

    /**
     * 删除自定义菜单
     *
     * @param id
     * @return
     */
    public boolean deleteMenu(int id);

    public List<MenuDiy> findMenuByKey(int accountId, String key);

}

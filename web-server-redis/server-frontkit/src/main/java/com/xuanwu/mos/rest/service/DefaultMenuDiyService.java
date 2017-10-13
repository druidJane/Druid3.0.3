package com.xuanwu.mos.rest.service;

import com.xuanwu.mos.domain.entity.Account;
import com.xuanwu.mos.domain.entity.MenuDiy;
import com.xuanwu.mos.rest.repo.AccountRepo;
import com.xuanwu.mos.rest.repo.MenuDiyRepo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public class DefaultMenuDiyService implements MenuDiyService {

    private MenuDiyRepo menuDiyDao;

    private AccountRepo accountDao;

    @Override
    public int findMenusCount(int userId, int accountId, String title) {
        return menuDiyDao.findMenusCount(userId, accountId, title);
    }

    @Override
    public List<MenuDiy> findMenus(int userId, int accountId, String title,
                                   int offset, int reqNum) {
        return menuDiyDao.findMenus(userId, accountId, title, offset, reqNum);
    }

    @Override
    public List<MenuDiy> findMenuOptions(int userId, int accountId, String name,int fetchSize){
        List<MenuDiy> list = new ArrayList<MenuDiy>();
        HashMap<Integer, Account> map = new HashMap<Integer, Account>();
        list = menuDiyDao.findMenusOption(userId, accountId, name, fetchSize);
        for (MenuDiy menu : list) {
            Account account = accountDao.findAccountById(menu.getAccountId());
            menu.setAccount(account);
        }
        return list;
    }


    @Override
    public MenuDiy findMenuById(int id) {
        return menuDiyDao.findMenuById(id);
    }

    @Override
    public boolean addMenu(MenuDiy menuDiy) {
        return menuDiyDao.addMenu(menuDiy);
    }

    @Override
    public boolean updateMenu(MenuDiy menuDiy) {
        return menuDiyDao.updateMenu(menuDiy);
    }

    @Override
    public boolean updateMenuState(int id, int state) {
        return menuDiyDao.updateMenuState(id, state);
    }

    @Override
    public boolean deleteMenu(int id) {
        return menuDiyDao.deleteMenu(id);
    }

    @Override
    public List<MenuDiy> findMenuByKey(int accountId, String key) {
        return menuDiyDao.findMenuByKey(accountId, key);
    }

    @Autowired
    public void setMenuDiyDao(MenuDiyRepo menuDiyDao) {
        this.menuDiyDao = menuDiyDao;
    }

    @Autowired
    public void setAccountDao(AccountRepo accountDao) {
        this.accountDao = accountDao;
    }
}

package com.xuanwu.mos.rest.repo;

import com.xuanwu.mos.db.GsmsMybatisEntityRepository;
import com.xuanwu.mos.domain.entity.MenuDiy;
import com.xuanwu.mos.domain.entity.MenuDiyOption;
import com.xuanwu.mos.exception.RepositoryException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义菜单数据服务
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
@Repository
public class MenuDiyRepo extends GsmsMybatisEntityRepository<MenuDiy> {
    @Override
    protected String namesapceForSqlId() {
        return "com.xuanwu.mos.mapper.MenuDiyMapper";
    }

    public int findMenusCount(int userId,int accountId,String title) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("accountId", accountId);
            params.put("title", title);
            return session.selectOne(fullSqlId("findMenusCount"), params);
        }
    }

    public List<MenuDiy> findMenus(int userId, int accountId, String title,
                                                   int offset, int reqNum) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("accountId", accountId);
            params.put("title", title);
            params.put("offset", offset);
            params.put("reqNum", reqNum);
            return session.selectList(fullSqlId("findMenus"), params);
        }
    }

    public List<MenuDiy> findMenusOption(int userId, int accountId, String title,
                                   int fetchSize) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("accountId", accountId);
            params.put("title", title);
            params.put("fetchSize", fetchSize);
            return session.selectList(fullSqlId("findMenusOption"), params);
        }
    }

    public MenuDiy findMenuById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            return session.selectOne(fullSqlId("findMenuById"), params);
        }
    }

    public boolean addMenu(MenuDiy menuDiy) {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("menuDiy", menuDiy);
            ret = session.insert(fullSqlId("addMenu"), params);
        }
        if(ret == 0){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateMenu(MenuDiy menuDiy) {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("menuDiy", menuDiy);
            ret = session.update(fullSqlId("updateMenu"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update Menu failed: ", e);
        }
        if(ret == 0){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateMenuState(int id, int state){
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("state", state);
            ret = session.update(fullSqlId("updateMenuState"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update MenuState failed: ", e);
        }
        if(ret == 0){
            return false;
        }else{
            return true;
        }
    }

    public int mutexMenuState(int id,int accountId,  int state) throws RepositoryException {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("accountId", accountId);
            map.put("state", state);
            ret = session.update(fullSqlId("mutexMenuState"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("Update MenuState failed: ", e);
            throw new RepositoryException(e);
        }
        return ret;
    }

    public boolean deleteMenu(int id) {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            ret = session.delete(fullSqlId("deleteMenu"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete menu failed: ", e);
        }
        if(ret == 0){
            return false;
        }else{
            return true;
        }
    }

    public int addMenuOption(MenuDiyOption menuDiyOption) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("menuDiyOption", menuDiyOption);
            return session.insert(fullSqlId("addMenuOption"), params);
        }
    }

    public int deleteMenuOptions(int menuId) {
        int ret = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("menuId", menuId);
            ret = session.delete(fullSqlId("deleteMenuOptions"), map);
            session.commit(true);
        } catch (Exception e) {
            logger.error("delete MenuOptions failed: ", e);
        }
        return ret;
    }

    public List<MenuDiy> findMenuByKey(int accountId, String key) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("accountId", accountId);
            params.put("key", key);
            return session.selectList(fullSqlId("findMenuByKey"), params);
        }
    }

    public List<MenuDiyOption> findMenuOptionByKey(int accountId, String key, String url) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("accountId", accountId);
            params.put("key", key);
            params.put("url", url);
            return session.selectList(fullSqlId("findMenuOptionByKey"), params);
        }
    }
}

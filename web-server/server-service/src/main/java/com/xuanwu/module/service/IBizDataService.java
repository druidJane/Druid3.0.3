package com.xuanwu.module.service;

import com.xuanwu.mos.config.Platform;

import java.util.List;

/**
 * 基本业务数据服务
 * Created by Jiang.Ziyuan on 2017/4/24.
 */
public interface IBizDataService {
    public enum GsmsSyncVersion{
        /** 对应于通道端口等版本号 */
        SPEC_SVS(1),
        /** 对应于号码段版本号 */
        PHONE_SEG(2),
        /** 对应于系统配置版本号 */
        SYS_CONFIG(3),
        /** 对应于优先级版本号 */
        PRIORITY(4),
        /** 对应于用户版本号 */
        USER(5),
        /** 对应于角色权限绑定关系版本号 */
        ROLE_PERM(6);
        private final int index;

        private GsmsSyncVersion(int index){
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static GsmsSyncVersion getType(int index){
            switch(index){
                case 1: return SPEC_SVS;
                case 2: return PHONE_SEG;
                case 3: return SYS_CONFIG;
                case 4: return PRIORITY;
                case 5: return USER;
                case 6: return ROLE_PERM;
                default: throw new RuntimeException("Unsupport gsms sync version type");
            }
        }
    }

    /**
     * 同步角色权限关系
     */
    public void syncRolePerm();

    /**
     * 同步号码段
     */
    public void syncPhoneSeg();

    /**
     * Get permission by ID
     * @param id
     * @return permission
     */
    public Permission getPermission(Integer id);

    /**
     * Get permission by permUrl
     * @param permUrl
     * @return
     */
    public Permission getPermission(String permUrl);

    /**
     * get role-permission by roleID and platform
     * @param roleID
     * @param platform
     * @return
     */
    public List<RolePermission> getRolePermissions(Integer roleID, Platform platform);

    /**
     * 通过ID取得地区代码
     * @param id
     * @return
     */
    public Region getRegionById(int id);

    /**
     * 通过ID取得运营商信息
     * @param id
     * @return
     */
    public Carrier getCarrierById(int id);

    /**
     * 通过号码取得运营商类型
     * @param phone
     * @return
     */
    public Carrier getCarrierType(String phone);

    /**
     * 通过企业ID取得所有部门信息，包含企业本身
     * @param entId
     * @return
     */
    public List<GsmsUser> getDepts(int entId);

    /**
     * 通过企业ID取得所有部门信息，包含企业本身
     * @param entId
     * @return
     */
    public List<GsmsUser> getDeptsIncludeDel(int entId);

    /**
     * 通过企业ID及部门ID取得部门信息
     * @param entId 企业ID
     * @param id 部门ID
     * @return
     */
    public GsmsUser getDeptById(int entId, int id);

    /**
     * 根据企业ID更新对应的部门信息
     * @param entId 企业ID
     * @param dept 更新的部门
     * @return
     */
    public boolean updateDept(int entId, GsmsUser dept);
}

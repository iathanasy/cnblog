package com.icss.cnblog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.entity.SysMenuEntity;

import java.util.List;

/**
 * 菜单权限表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    /**
     * 获取所有菜单信息
     * @return
     */
    List<SysMenuEntity> list();

    /**
     * 菜单树
     * @return
     */
    List<SysMenuEntity> tree();

    public List<SysMenuEntity> selectMenuListByUserId(Long userId);

    /**
     * 获取资源的url和perms
     * @return
     */
    public List<SysMenuEntity> selectUrlAndPermsMenuList();

    /**
     * 获取不包含按钮的菜单列表
     * @param userId
     * @return
     */
    public List<SysMenuEntity> selectAllNotButtonMenuList(Long userId);

    /**
     * 获取所有菜单列表
     * @return
     */
    public List<SysMenuEntity> selectAllMenuList();

    /**
     * 构建系统后台导航菜单
     * @param userId
     * @return
     */
    public List<SysMenuEntity> selectNavMenus(Long userId);

    /**
     * 获取无子菜单Id
     * @param meuns
     * @return
     */
    public List<Long> selectNotChildMenusId(List<SysMenuEntity> meuns);

    /**
     * 查询子菜单
     * @param parentId
     * @return
     */
    public List<SysMenuEntity> selectListParentId(Long parentId);

    public void delete(Long menuId);
}


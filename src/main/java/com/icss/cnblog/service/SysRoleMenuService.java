package com.icss.cnblog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.entity.SysRoleMenuEntity;

import java.util.HashSet;
import java.util.List;

/**
 * 角色和菜单关联表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据用户ID，获取菜单名称
     */
    HashSet<String> queryMenuNameList(Long userId);

    /**
     * 根据角色ID，获取菜单ID按钮列表
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdButtonList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}


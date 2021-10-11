package com.icss.cnblog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.icss.cnblog.entity.SysRoleMenuEntity;

import java.util.HashSet;
import java.util.List;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuEntity> {

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
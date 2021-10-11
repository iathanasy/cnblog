package com.icss.cnblog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.icss.cnblog.entity.SysUserRoleEntity;

import java.util.HashSet;
import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRoleEntity> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据用户ID，获取角色列表
     */
    HashSet<String> queryRoleNameList(Long userId);

    /**
            * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

    /**
     * 根据用户ID数组，批量删除
     * @param userIds
     * @return
     */
    int deleteBatchByUserId(Long[] userIds);
}
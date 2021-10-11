package com.icss.cnblog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.entity.SysUserRoleEntity;

import java.util.HashSet;
import java.util.List;

/**
 * 用户和角色关联表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

    void saveOrUpdate(Long userId, List<Long> roleIdList);

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


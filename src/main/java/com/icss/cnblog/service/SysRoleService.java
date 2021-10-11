package com.icss.cnblog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.entity.SysRoleEntity;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;

import java.util.List;

/**
 * 角色信息表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-05 15:28:03
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    PageUtils queryPage(Query query);

    public List<SysRoleEntity> selectRoleListByUserId(Long userId);

    public List<SysRoleEntity> selectRoleList();

    void add(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);
}


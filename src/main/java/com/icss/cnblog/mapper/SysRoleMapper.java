package com.icss.cnblog.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.icss.cnblog.entity.SysRoleEntity;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    public List<SysRoleEntity> selectRoleListByUserId(Long userId);

    public List<SysRoleEntity> selectRoleList();


}
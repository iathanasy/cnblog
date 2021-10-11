package com.icss.cnblog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.icss.cnblog.entity.SysMenuEntity;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {

    public List<SysMenuEntity> selectMenuListByUserId(Long userId);

    public List<SysMenuEntity> selectUrlAndPermsMenuList();

    public List<SysMenuEntity> selectAllNotButtonMenuList(Long userId);

    public List<SysMenuEntity> selectAllMenuList();
}